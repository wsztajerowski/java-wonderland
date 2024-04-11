package pl.symentis.services;

import dev.morphia.annotations.Entity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import pl.symentis.MongoDbTestHelpers;
import pl.symentis.TestcontainersWithS3AndMongoBaseIT;
import pl.symentis.entities.jmh.JmhBenchmark;

import java.nio.file.Path;
import java.util.List;

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
    void successful_scenario(){
        // given
        String jhhTestBenchmark = Path.of("target", "fake-jmh-benchmarks.jar").toAbsolutePath().toString();
        JmhWithAsyncProfilerSubcommandService sut = getJmhWithAsyncProfilerSubcommandService()
            .withMongoConnectionString(getConnectionString())
            .withS3Service(getS3ServiceBuilder()
                .withS3Client(awsS3Client)
                .withBucketName(TEST_BUCKET_NAME)
                .build())
            .withCommonOptions(new CommonSharedOptions("commit-sha", 1, "", "incrementUsingSynchronized"))
            .withJmhOptions(new JmhBenchmarksSharedOptions(0, 1, 1, jhhTestBenchmark))
            .withAsyncPath(System.getenv("ASYNC_PATH"))
            .withOutput("flamegraph")
            .withInterval(9990)
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
                .containsEntry("flame-cpu-forward", "gha-outputs/commit-commit-sha/attempt-1/jmh/pl.symentis.fake.Incrementing_Synchronized.incrementUsingSynchronized-Throughput/flame-cpu-forward.html")
        );

        // and
        JSONArray objectsInTestBucket = listObjectsInTestBucket();
        assertThatJson(objectsInTestBucket)
            .isArray()
            .hasSize(2);
        List<JSONObject> jsonObject = List.of(objectsInTestBucket.getJSONObject(0), objectsInTestBucket.getJSONObject(1));
        assertThat(jsonObject)
            .allSatisfy(element -> {
                assertThat(element.get("Size"))
                    .isNotNull()
                    .matches(o -> Integer.parseInt(o.toString()) > 100_000);
                assertThat(element.get("Key"))
                    .asString()
                    .containsAnyOf("flame-cpu-forward.html", "flame-cpu-reverse.html");
            });
    }
}