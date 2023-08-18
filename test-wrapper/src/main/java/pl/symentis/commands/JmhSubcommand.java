package pl.symentis.commands;

import dev.morphia.UpdateOptions;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;

import static dev.morphia.query.filters.Filters.eq;
import static dev.morphia.query.updates.UpdateOperators.set;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.services.MorphiaService.getMorphiaService;
import static pl.symentis.services.ResultLoaderService.getResultLoaderService;

@Command(name = "jmh", description = "Run JHM benchmarks")
public class JmhSubcommand implements Runnable {

    @Mixin
    private JmhBenchmarksSharedOptions sharedJmhOptions;

    @Mixin
    private CommonSharedOptions commonSharedOptions;

    @Override
    public void run() {
        try {
            benchmarkProcessBuilder(sharedJmhOptions.benchmarkPath)
                .addArgumentWithValue("-f", sharedJmhOptions.forks)
                .addArgumentWithValue("-i", sharedJmhOptions.iterations)
                .addArgumentWithValue("-wi", sharedJmhOptions.warmupIterations)
                .addArgumentWithValue("-rf", "json")
                .addOptionalArgument(commonSharedOptions.testNameRegex)
                .buildAndStartProcess()
                .waitFor();
        } catch (InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        for (JmhResult jmhResult : getResultLoaderService().loadJmhResults()) {
            JmhBenchmarkId benchmarkId = new JmhBenchmarkId(
                commonSharedOptions.commitSha,
                jmhResult.benchmark(),
                jmhResult.mode(),
                commonSharedOptions.runAttempt);
            getMorphiaService()
                .getTestResultsDatastore()
                .find(JmhBenchmark.class)
                .filter(eq("benchmarkId", benchmarkId))
                .update(new UpdateOptions().upsert(true),
                    set("jmhResult", jmhResult));
        }
    }
}
