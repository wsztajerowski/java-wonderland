package pl.symentis.commands;

import dev.morphia.UpdateOptions;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;

import java.util.concurrent.Callable;

import static dev.morphia.query.filters.Filters.eq;
import static dev.morphia.query.updates.UpdateOperators.set;
import static pl.symentis.services.MorphiaService.getMorphiaService;
import static pl.symentis.services.ResultLoaderService.getResultLoaderService;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

@Command(name = "jmh", description = "Run JHM benchmarks")
public class JmhSubcommand implements Callable<Integer> {

    @Mixin
    private JmhBenchmarksSharedOptions sharedJmhOptions;

    @Mixin
    private CommonSharedOptions commonSharedOptions;

    @Override
    public Integer call() throws Exception {
        int processExitCode = benchmarkProcessBuilder(sharedJmhOptions.benchmarkPath)
            .addArgumentWithValue("-f", sharedJmhOptions.forks)
            .addArgumentWithValue("-i", sharedJmhOptions.iterations)
            .addArgumentWithValue("-wi", sharedJmhOptions.warmupIterations)
            .addArgumentWithValue("-rf", "json")
            .addOptionalArgument(commonSharedOptions.testNameRegex)
            .buildAndStartProcess()
            .waitFor();

        if (processExitCode != 0) {
            return processExitCode;
        }

        for (JmhResult jmhResult : getResultLoaderService().loadJmhResults()) {
            JmhBenchmarkId benchmarkId = new JmhBenchmarkId()
                .withCommitSha(commonSharedOptions.commitSha)
                .withBenchmarkName(jmhResult.benchmark)
                .withBenchmarkType(jmhResult.mode)
                .withRunAttempt( commonSharedOptions.runAttempt);
            getMorphiaService()
                .getTestResultsDatastore()
                .find(JmhBenchmark.class)
                .filter(eq("benchmarkId", benchmarkId))
                .update(new UpdateOptions().upsert(true),
                    set("jmhResult", jmhResult));
        }

        return 0;
    }
}
