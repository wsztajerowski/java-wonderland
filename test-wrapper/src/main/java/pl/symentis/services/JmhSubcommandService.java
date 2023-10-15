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

    private final CommonSharedOptions commonOptions;
    private final JmhBenchmarksSharedOptions jmhBenchmarksOptions;

    JmhSubcommandService(CommonSharedOptions commonOptions, JmhBenchmarksSharedOptions jmhBenchmarksOptions) {
        this.commonOptions = commonOptions;
        this.jmhBenchmarksOptions = jmhBenchmarksOptions;
    }

    public void executeCommand() {
        try {
            int exitCode = benchmarkProcessBuilder(jmhBenchmarksOptions.benchmarkPath())
                .addArgumentWithValue("-f", jmhBenchmarksOptions.forks())
                .addArgumentWithValue("-i", jmhBenchmarksOptions.iterations())
                .addArgumentWithValue("-wi", jmhBenchmarksOptions.warmupIterations())
                .addArgumentWithValue("-rf", "json")
                .addOptionalArgument(commonOptions.testNameRegex())
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
                commonOptions.commitSha(),
                jmhResult.benchmark(),
                jmhResult.mode(),
                commonOptions.runAttempt());
            getMorphiaService()
                .upsert(JmhBenchmark.class)
                .byFieldValue("benchmarkId", benchmarkId)
                .setValue("jmhResult", jmhResult)
                .execute();
        }
    }
}
