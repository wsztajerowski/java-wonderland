package pl.symentis.services;

import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;

import static java.text.MessageFormat.format;
import static pl.symentis.infra.MorphiaService.getMorphiaService;
import static pl.symentis.infra.ResultLoaderService.getResultLoaderService;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

public class JmhSubcommandService {

    private final String benchmarkPath;
    private final int forks;
    private final int iterations;
    private final int warmupIterations;
    private final String testNameRegex;
    private final String commitSha;
    private final int runAttempt;

    JmhSubcommandService(String benchmarkPath, int forks, int iterations, int warmupIterations, String testNameRegex, String commitSha, int runAttempt) {
        this.benchmarkPath = benchmarkPath;
        this.forks = forks;
        this.iterations = iterations;
        this.warmupIterations = warmupIterations;
        this.testNameRegex = testNameRegex;
        this.commitSha = commitSha;
        this.runAttempt = runAttempt;
    }

    public void executeCommand() {
        try {
            int exitCode = benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-f", forks)
                .addArgumentWithValue("-i", iterations)
                .addArgumentWithValue("-wi", warmupIterations)
                .addArgumentWithValue("-rf", "json")
                .addOptionalArgument(testNameRegex)
                .buildAndStartProcess()
                .waitFor();
            if (exitCode != 0) {
                throw new JavaWonderlandException(format("Benchmark process exit with non-zero code: {0}", exitCode));
            }
        } catch (InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        for (JmhResult jmhResult : getResultLoaderService().loadJmhResults()) {
            JmhBenchmarkId benchmarkId = new JmhBenchmarkId(
                commitSha,
                jmhResult.benchmark(),
                jmhResult.mode(),
                runAttempt);
            getMorphiaService()
                .upsert(JmhBenchmark.class)
                .byFieldValue("benchmarkId", benchmarkId)
                .setValue("jmhResult", jmhResult);
        }
    }
}