package pl.symentis.services;

import dev.morphia.annotations.Entity;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import pl.symentis.MongoDbTestHelpers;
import pl.symentis.TestcontainersWithS3AndMongoBaseIT;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.services.options.AsyncProfilerOptions;
import pl.symentis.services.options.CommonSharedOptions;
import pl.symentis.services.options.JmhOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static pl.symentis.MongoDbTestHelpers.all;
import static pl.symentis.infra.S3ServiceBuilder.getS3ServiceBuilder;
import static pl.symentis.services.JmhWithAsyncProfilerSubcommandServiceBuilder.serviceBuilder;
import static pl.symentis.services.options.JmhBenchmarkOptions.jmhBenchmarkOptionsBuilder;
import static pl.symentis.services.options.JmhIterationOptions.jmhIterationOptionsBuilder;
import static pl.symentis.services.options.JmhJvmOptions.jmhJvmOptionsBuilder;
import static pl.symentis.services.options.JmhOutputOptions.jmhOutputOptionsBuilder;
import static pl.symentis.services.options.JmhWarmupOptions.jmhWarmupOptionsBuilder;

@EnabledIfEnvironmentVariable(named = "ASYNC_PATH", matches = ".*")
class JmhWithAsyncProfilerSubcommandServiceIT  extends TestcontainersWithS3AndMongoBaseIT {

    private static MongoDbTestHelpers helper;

    @BeforeAll
    static void setupHelper(){
        helper = new MongoDbTestHelpers(getConnectionString());
    }

    @Test
    void successful_scenario() throws IOException {
        // given
        Path jmhTestBenchmark = Path.of("target", "fake-jmh-benchmarks.jar").toAbsolutePath();
        Path result = Files.createTempFile("results", "jmh.json");
        Path output = Files.createTempFile("outputs", "jmh.txt");
        Path asyncOutput = Files.createTempDirectory("async-outputs");
        JmhWithAsyncProfilerSubcommandService sut = serviceBuilder()
            .withMongoConnectionString(getConnectionString())
            .withS3Service(getS3ServiceBuilder()
                .withS3Client(awsS3Client)
                .withBucketName(TEST_BUCKET_NAME)
                .build())
            .withCommonOptions(new CommonSharedOptions("abcdef12", 1))
            .withJmhOptions( new JmhOptions(
                jmhBenchmarkOptionsBuilder()
                    .withBenchmarkPath(jmhTestBenchmark)
                    .withForks(1)
                    .build(),
                jmhOutputOptionsBuilder()
                    .withMachineReadableOutput(result)
                    .withProcessOutput(output)
                    .build(),
                jmhWarmupOptionsBuilder()
                    .withWarmupIterations(0)
                    .build(),
                jmhIterationOptionsBuilder()
                    .withIterations(1)
                    .build(),
                jmhJvmOptionsBuilder().build()))
            .withAsyncProfilerOptions(AsyncProfilerOptions.asyncProfilerOptionsBuilder()
                .withAsyncPath(Path.of(System.getenv("ASYNC_PATH")))
                .withAsyncOutputType("flamegraph")
                .withAsyncOutputPath(asyncOutput)
                .withAsyncInterval(9990)
                .build())
            .build();

        // when
        sut.executeCommand();

        // then
        String collectionName = JmhBenchmark.class.getAnnotation(Entity.class).value();
        helper.assertFindResult(collectionName, all(), documents ->
            assertThat(documents.first())
                .isNotNull()
                .containsEntry("_t", "JmhBenchmark")
                .extracting("benchmarkMetadata.profilerOutputPaths", as(MAP))
                .containsEntry("flame-cpu-forward", "gha-outputs/commit-abcdef12/attempt-1/jmh-with-async/pl.symentis.fake.Incrementing_Synchronized.incrementUsingSynchronized-Throughput/flame-cpu-forward.html")
        );

        // and
        JSONArray objectsInTestBucket = listObjectsInTestBucket();
        assertThatJson(objectsInTestBucket)
            .inPath("$[*].Key")
            .isArray()
            .anySatisfy(o -> assertThat(o)
                .asString()
                .endsWith("flame-cpu-forward.html"))
            .anySatisfy(o -> assertThat(o)
                .asString()
                .endsWith("flame-cpu-reverse.html"))
            .anySatisfy(o -> assertThat(o)
                .asString()
                .isEqualTo("gha-outputs/commit-abcdef12/attempt-1/jmh-with-async/output.txt"));
    }
}