package pl.symentis.commands;

import picocli.CommandLine;
import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.entities.jcstress.JCStressTestId;

import java.nio.file.Path;

import static java.text.MessageFormat.format;
import static pl.symentis.commands.JCStressHtmlResultParser.getJCStressHtmlResultParser;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.services.MorphiaService.getMorphiaService;
import static pl.symentis.services.S3Service.getS3Service;

@CommandLine.Command(name = "jcstress", description = "Run JCStress performance tests")
public class JCStressSubcommand implements Runnable {

    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";

    @CommandLine.Mixin
    private CommonSharedOptions commonSharedOptions;

    @CommandLine.Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-stress-tests.jar}", description = "Path to JCStress benchmark jar (default: ${DEFAULT-VALUE})")
    String benchmarkPath;

    @Override
    public void run() {
        try {
            benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", JCSTRESS_RESULTS_DIR)
                .buildAndStartProcess()
                .waitFor();
        } catch (AssertionError e) {
            // do nothing
        } catch (InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        Path resultFilepath = Path.of(JCSTRESS_RESULTS_DIR, "index.html");
        JCStressHtmlResultParser resultParser = getJCStressHtmlResultParser(resultFilepath);

        JCStressTestId testId = new JCStressTestId()
            .withCommitSha(commonSharedOptions.commitSha)
            .withRunAttempt(commonSharedOptions.runAttempt);
        JCStressResult jcStressResult = createJCStressResult(resultParser);
        JCStressTest jcStressTest = new JCStressTest()
            .withTestId(testId)
            .withResult(jcStressResult);

        getMorphiaService()
            .getTestResultsDatastore()
            .insert(jcStressTest);

        jcStressResult
            .getAllUnsuccessfulTest()
            .forEach((testName, s3Key) ->
                getS3Service()
                    .saveFileOnS3(s3Key, Path.of(JCSTRESS_RESULTS_DIR, testName + ".html"))
            );
    }

    private JCStressResult createJCStressResult(JCStressHtmlResultParser resultParser) {
        JCStressResult jcStressResult = new JCStressResult()
            .withTotalTests(resultParser.getTotalTests())
            .withPassedTests(resultParser.getPassedTests())
            .withJavaVendor(resultParser.getJavaVendor())
            .withJavaVmVendor(resultParser.getJavaVmVendor())
            .withJavaVmName(resultParser.getJavaVmName())
            .withJavaVmVersion(resultParser.getJavaVmVersion())
            .withOsArch(resultParser.getOsArch())
            .withOsName(resultParser.getOsName())
            .withOsVersion(resultParser.getOsVersion());
        resultParser
            .getTestsWithErrorResults()
            .forEach(test -> jcStressResult.addTestWithErrorResults(test, createS3Path(test)));
        resultParser
            .getTestsWithFailedResults()
            .forEach(test -> jcStressResult.addTestWithFailedResults(test, createS3Path(test)));
        resultParser
            .getTestsWithInterestingResults()
            .forEach(test -> jcStressResult.addTestWithInterestingResults(test, createS3Path(test)));
        return jcStressResult;
    }

    private String createS3Path(String testName) {
        return format("gha-outputs/commit-{0}/attempt-{1}/jcstress/{2}.html",
            commonSharedOptions.commitSha,
            commonSharedOptions.runAttempt,
            testName);
    }
}
