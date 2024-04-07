package pl.symentis.services;

import dev.morphia.annotations.Entity;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import pl.symentis.MongoDbTestHelpers;
import pl.symentis.TestcontainersWithS3AndMongoBaseIT;
import pl.symentis.entities.jmh.JmhBenchmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static pl.symentis.MongoDbTestHelpers.all;
import static pl.symentis.infra.S3ServiceBuilder.getS3ServiceBuilder;
import static pl.symentis.services.JmhWithAsyncProfilerSubcommandServiceBuilder.getJmhWithAsyncProfilerSubcommandService;

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
        String jhhTestBenchmark = Path.of("target", "fake-jmh-benchmarks.jar").toAbsolutePath().toString();
        Path result = Files.createTempFile("results", "jmh.json");
        Path output = Files.createTempFile("outputs", "jmh.txt");
        Path asyncOutput = Files.createTempDirectory("async-outputs");
        JmhWithAsyncProfilerSubcommandService sut = getJmhWithAsyncProfilerSubcommandService()
            .withMongoConnectionString(getConnectionString())
            .withS3Service(getS3ServiceBuilder()
                .withS3Client(awsS3Client)
                .withBucketName(TEST_BUCKET_NAME)
                .build())
            .withCommonOptions(new CommonSharedOptions("abcdef12", 1, "", "incrementUsingSynchronized"))
            .withJmhOptions(new JmhBenchmarksSharedOptions(0, 1, 1, jhhTestBenchmark))
            .withAsyncPath(System.getenv("ASYNC_PATH"))
            .withAsyncOutputType("flamegraph")
            .withAsyncOutputPath(asyncOutput)
            .withAsyncInterval(9990)
            .withResultsPath(result)
            .withOutputPath(output)
            .build();

        // when
        sut.executeCommand();

        // then
        String collectionName = JmhBenchmark.class.getAnnotation(Entity.class).value();
        helper.assertFindResult(collectionName, all(), documents ->
            assertThat(documents.first())
                .isNotNull()
                .containsEntry("_t", "JmhBenchmark")
                .extracting("benchmarkMetadata.flamegraphPaths", as(MAP))
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