package pl.symentis.services;

import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.entities.jcstress.JCStressTestId;
import pl.symentis.entities.jcstress.JCStressTestMetadata;

import java.nio.file.Path;

import static java.text.MessageFormat.format;
import static pl.symentis.commands.JCStressHtmlResultParser.getJCStressHtmlResultParser;
import static pl.symentis.infra.MorphiaService.getMorphiaService;
import static pl.symentis.infra.S3Service.getS3Service;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

public class JCStressSubcommandService {

    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";
    private final String benchmarkPath;
    private final String commitSha;
    private final int runAttempt;
    private final String testNameRegex;

    JCStressSubcommandService(CommonSharedOptions commonOptions, String benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        this.commitSha = commonOptions.commitSha();
        this.runAttempt = commonOptions.runAttempt();
        this.testNameRegex = commonOptions.testNameRegex();
    }

    public void executeCommand() {
        try {
            int exitCode = benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", JCSTRESS_RESULTS_DIR)
                .addOptionalArgument(testNameRegex)
                .buildAndStartProcess()
                .waitFor();
            if (exitCode != 0){
                throw new JavaWonderlandException(format("Benchmark process exit with non-zero code: {0}", exitCode));
            }
        } catch (AssertionError | InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        Path resultFilepath = Path.of(JCSTRESS_RESULTS_DIR, "index.html");
        JCStressResult jcStressResult = getJCStressHtmlResultParser(resultFilepath, commitSha, runAttempt)
            .parse();

        getMorphiaService()
            .save(new JCStressTest(
                new JCStressTestId(commitSha,runAttempt),
                new JCStressTestMetadata(),
                jcStressResult));

        jcStressResult
            .getAllUnsuccessfulTest()
            .forEach((testName, s3Key) ->
                getS3Service()
                    .saveFileOnS3(s3Key, Path.of(JCSTRESS_RESULTS_DIR, testName + ".html"))
            );
    }
}
