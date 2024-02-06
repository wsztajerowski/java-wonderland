package pl.symentis.services;

import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.entities.jcstress.JCStressTestId;
import pl.symentis.entities.jcstress.JCStressTestMetadata;

import java.nio.file.Path;

import static pl.symentis.commands.JCStressHtmlResultParser.getJCStressHtmlResultParser;
import static pl.symentis.infra.MorphiaService.getMorphiaService;
import static pl.symentis.infra.S3Service.getS3Service;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

public class JCStressSubcommandService {

    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";
    private final CommonSharedOptions commonOptions;
    private final String benchmarkPath;

    JCStressSubcommandService(CommonSharedOptions commonOptions, String benchmarkPath) {
        this.commonOptions = commonOptions;
        this.benchmarkPath = benchmarkPath;
    }

    public void executeCommand() {
        try {
            benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", JCSTRESS_RESULTS_DIR)
                .addOptionalArgument(commonOptions.testNameRegex())
                .buildAndStartProcess()
                .waitFor();
        } catch (AssertionError | InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        Path resultFilepath = Path.of(JCSTRESS_RESULTS_DIR, "index.html");
        JCStressResult jcStressResult = getJCStressHtmlResultParser(resultFilepath, commonOptions.commitSha(), commonOptions.runAttempt())
            .parse();

        getMorphiaService()
            .save(new JCStressTest(
                new JCStressTestId(commonOptions.commitSha(), commonOptions.runAttempt()),
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
