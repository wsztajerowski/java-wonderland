package pl.symentis.services;

import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.entities.jcstress.JCStressTestId;
import pl.symentis.entities.jcstress.JCStressTestMetadata;
import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;

import java.nio.file.Path;

import static java.text.MessageFormat.format;
import static pl.symentis.commands.JCStressHtmlResultParser.getJCStressHtmlResultParser;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

public class JCStressSubcommandService {

    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";
    private final CommonSharedOptions commonOptions;
    private final String benchmarkPath;
    private final S3Service s3Service;
    private final MorphiaService morphiaService;

    JCStressSubcommandService(S3Service s3Service, MorphiaService morphiaService, CommonSharedOptions commonOptions, String benchmarkPath) {
        this.s3Service = s3Service;
        this.morphiaService = morphiaService;
        this.commonOptions = commonOptions;
        this.benchmarkPath = benchmarkPath;
    }

    public void executeCommand() {
        try {
            int exitCode = benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", JCSTRESS_RESULTS_DIR)
                .addOptionalArgument(commonOptions.testNameRegex())
                .buildAndStartProcess()
                .waitFor();
            if (exitCode != 0){
                throw new JavaWonderlandException(format("Benchmark process exit with non-zero code: {0}", exitCode));
            }
        } catch (AssertionError | InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        Path resultFilepath = Path.of(JCSTRESS_RESULTS_DIR, "index.html");
        JCStressResult jcStressResult = getJCStressHtmlResultParser(resultFilepath, commonOptions.commitSha(), commonOptions.runAttempt())
            .parse();

        morphiaService
            .save(new JCStressTest(
                new JCStressTestId(commonOptions.commitSha(), commonOptions.runAttempt()),
                new JCStressTestMetadata(),
                jcStressResult));

        jcStressResult
            .getAllUnsuccessfulTest()
            .forEach((testName, s3Key) ->
                s3Service
                    .saveFileOnS3(s3Key, Path.of(JCSTRESS_RESULTS_DIR, testName + ".html"))
            );
    }
}
