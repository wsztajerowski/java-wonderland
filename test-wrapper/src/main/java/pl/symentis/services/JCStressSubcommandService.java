package pl.symentis.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.entities.jcstress.JCStressTestId;
import pl.symentis.entities.jcstress.JCStressTestMetadata;
import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;

import java.nio.file.Path;

import static pl.symentis.commands.JCStressHtmlResultParser.getJCStressHtmlResultParser;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.services.S3PrefixProvider.jcstressS3Prefix;

public class JCStressSubcommandService {
    private final Logger logger = LoggerFactory.getLogger(JmhSubcommandService.class);
    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";
    private final CommonSharedOptions commonOptions;
    private final S3Service s3Service;
    private final MorphiaService morphiaService;
    private final Path benchmarkPath;
    private final Path jcstressResultsPath;
    private final Path outputPath;

    JCStressSubcommandService(S3Service s3Service, MorphiaService morphiaService, CommonSharedOptions commonOptions, Path benchmarkPath, Path outputPath, Path resultsDir) {
        this.s3Service = s3Service;
        this.morphiaService = morphiaService;
        this.commonOptions = commonOptions;
        this.benchmarkPath = benchmarkPath;
        this.outputPath = outputPath;
        this.jcstressResultsPath = resultsDir;
    }

    public void executeCommand() {
        try {
            logger.info("Running JMH - S3 result path: {}", jcstressResultsPath);
            benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", jcstressResultsPath)
                .addArgumentIfValueIsNotNull("-t", commonOptions.testNameRegex())
                .withOutputPath(outputPath)
                .buildAndStartProcess()
                .waitFor();
        } catch (AssertionError | InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        logger.info("Parsing JCStress html output");
        Path resultFilepath = jcstressResultsPath.resolve( "index.html");
        Path s3Prefix = jcstressS3Prefix(commonOptions.commitSha(), commonOptions.runAttempt());
        JCStressResult jcStressResult = getJCStressHtmlResultParser(resultFilepath, s3Prefix)
            .parse();

        logger.info("Saving benchmarks into Mongo");
        JCStressTest stressTestResult = new JCStressTest(
            new JCStressTestId(commonOptions.commitSha(), commonOptions.runAttempt()),
            new JCStressTestMetadata(),
            jcStressResult);
        logger.debug("JCStress results: {}", stressTestResult);
        morphiaService
            .save(stressTestResult);

        jcStressResult
            .getAllUnsuccessfulTest()
            .forEach((testName, s3Key) ->
                {
                    String testOutputFilename = testName + ".html";
                    s3Service
                        .saveFileOnS3(s3Key, jcstressResultsPath.resolve(testOutputFilename));
                }
            );

        logger.info("Saving test outputs on S3");
        s3Service
            .saveFileOnS3(s3Prefix.resolve("outputs/output.txt").toString(), outputPath);
    }
}
