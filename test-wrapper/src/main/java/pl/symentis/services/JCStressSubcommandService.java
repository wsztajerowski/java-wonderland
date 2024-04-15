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
    private static final Logger logger = LoggerFactory.getLogger(JCStressSubcommandService.class);
    private final CommonSharedOptions commonOptions;
    private final S3Service s3Service;
    private final MorphiaService morphiaService;
    private final Path benchmarkPath;
    private final Path outputPath;

    private final JCStressOptions jcStressOptions;

    JCStressSubcommandService(S3Service s3Service, MorphiaService morphiaService, CommonSharedOptions commonOptions, Path benchmarkPath, Path outputPath, JCStressOptions jcStressOptions) {
        this.s3Service = s3Service;
        this.morphiaService = morphiaService;
        this.commonOptions = commonOptions;
        this.benchmarkPath = benchmarkPath;
        this.jcStressOptions = jcStressOptions;
        this.outputPath = outputPath;
    }

    public void executeCommand() {
        Path reportPath = jcStressOptions.reportPath();
        Path s3Prefix = jcstressS3Prefix(commonOptions.commitSha(), commonOptions.runAttempt());
        try {
            logger.info("Running JCStress - S3 result path: {}", s3Prefix);
            benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-r", reportPath)
                .addArgumentIfValueIsNotNull("-c", jcStressOptions.cpuNumber())
                .addArgumentIfValueIsNotNull("-f", jcStressOptions.forks())
                .addArgumentIfValueIsNotNull("-fsm", jcStressOptions.forkMultiplier())
                .addArgumentIfValueIsNotNull("-hs", jcStressOptions.heapSize())
                .addArgumentIfValueIsNotNull("-jvmArgs", jcStressOptions.jvmArgs())
                .addArgumentIfValueIsNotNull("-jvmArgsPrepend", jcStressOptions.jvmArgsPrepend())
                .addArgumentIfValueIsNotNull("-pth", jcStressOptions.preTouchHeap())
                .addArgumentIfValueIsNotNull("-sc", jcStressOptions.splitCompilationModes())
                .addArgumentIfValueIsNotNull("-spinStyle", jcStressOptions.spinStyle())
                .addArgumentIfValueIsNotNull("-strideCount", jcStressOptions.strideCount())
                .addArgumentIfValueIsNotNull("-strideSize", jcStressOptions.strideSize())
                .addArgumentIfValueIsNotNull("-t", commonOptions.testNameRegex())
                .withOutputPath(outputPath)
                .buildAndStartProcess()
                .waitFor();
        } catch (AssertionError | InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        logger.info("Parsing JCStress html output");
        Path resultFilepath = reportPath.resolve( "index.html");
        JCStressResult jcStressResult = getJCStressHtmlResultParser(resultFilepath, s3Prefix)
            .parse();

        logger.info("Saving benchmarks into DB");
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
                        .saveFileOnS3(s3Key, reportPath.resolve(testOutputFilename));
                }
            );

        logger.info("Saving test outputs on S3");
        s3Service
            .saveFileOnS3(s3Prefix.resolve("outputs/output.txt").toString(), outputPath);
    }
}
