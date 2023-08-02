package pl.symentis.test_runner;

import picocli.CommandLine;
import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.entities.jcstress.JCStressTestId;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import static java.text.MessageFormat.format;
import static pl.symentis.MorphiaService.getMorphiaService;
import static pl.symentis.S3Service.getS3Service;
import static pl.symentis.benchmark_builder.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.test_runner.JCStressHtmlResultParser.getJCStressHtmlResultParser;

@CommandLine.Command(name = "jcstress", description = "Run JCStress performance tests")
public class JCStressSubcommand implements Callable<Integer> {

    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";

    @CommandLine.Mixin
    private CommonSharedOptions commonSharedOptions;
    @CommandLine.Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-stress-tests.jar}", description = "Path to JCStress benchmark jar (default: ${DEFAULT-VALUE})")
    String benchmarkPath;

    @Override
    public Integer call() throws Exception {
        try {
            benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", JCSTRESS_RESULTS_DIR)
                .buildAndStartProcess()
                .waitFor();
        } catch (AssertionError e) {
            // do nothing
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

        return 0;
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
