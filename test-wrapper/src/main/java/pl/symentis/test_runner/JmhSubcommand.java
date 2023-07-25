package pl.symentis.test_runner;

import dev.morphia.UpdateOptions;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import pl.symentis.entities.JmhBenchmark;
import pl.symentis.entities.JmhBenchmarkId;
import pl.symentis.entities.JmhResult;

import java.util.concurrent.Callable;

import static dev.morphia.query.filters.Filters.eq;
import static dev.morphia.query.updates.UpdateOperators.set;
import static pl.symentis.MorphiaService.getMorphiaService;
import static pl.symentis.ResultLoaderService.getResultLoaderService;
import static pl.symentis.benchmark_builder.BenchmarkProcessBuilder.benchmarkProcessBuilder;

@Command(name = "jmh", description = "Run JHM benchmarks")
public class JmhSubcommand implements Callable<Integer> {

    @Mixin
    private JmhBenchmarksSharedOptions sharedJmhOptions;

    @Override
    public Integer call() throws Exception {
        int processExitCode = benchmarkProcessBuilder(sharedJmhOptions.benchmarkPath)
            .addArgumentWithValue("-f", sharedJmhOptions.forks)
            .addArgumentWithValue("-i", sharedJmhOptions.iterations)
            .addArgumentWithValue("-wi", sharedJmhOptions.warmupIterations)
            .addArgumentWithValue("-rf", "json")
            .addOptionalArgument(sharedJmhOptions.benchmarkNameRegex)
            .buildAndStartProcess()
            .waitFor();

        if (processExitCode != 0) {
            return processExitCode;
        }

        for (JmhResult jmhResult : getResultLoaderService().loadJmhResults()) {
            JmhBenchmarkId benchmarkId = new JmhBenchmarkId()
                .withCommitSha(sharedJmhOptions.commitSha)
                .withBenchmarkName(jmhResult.benchmark)
                .withBenchmarkType(jmhResult.mode)
                .withRunAttempt( sharedJmhOptions.runAttempt);
            getMorphiaService()
                .getBenchmarkDatastore()
                .find(JmhBenchmark.class)
                .filter(eq("benchmarkId", benchmarkId))
                .update(new UpdateOptions().upsert(true),
                    set("jmhResult", jmhResult));
        }

        return 0;
    }
}
