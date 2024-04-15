package pl.symentis.services;

import dev.morphia.annotations.Entity;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.symentis.MongoDbTestHelpers;
import pl.symentis.TestcontainersWithS3AndMongoBaseIT;
import pl.symentis.entities.jmh.JmhBenchmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static pl.symentis.MongoDbTestHelpers.all;
import static pl.symentis.infra.S3ServiceBuilder.getS3ServiceBuilder;
import static pl.symentis.services.JmhSubcommandServiceBuilder.getJmhSubcommandService;

class JmhSubcommandServiceIT extends TestcontainersWithS3AndMongoBaseIT {

    private static MongoDbTestHelpers helper;

    @BeforeAll
    static void setupHelper(){
        helper = new MongoDbTestHelpers(getConnectionString());
    }

    @Test
    void successful_scenario() throws IOException {
        // given
        Path result = Files.createTempFile("results", "jmh.json");
        Path output = Files.createTempFile("outputs", "jmh.txt");
        Path jhhTestBenchmark = Path.of("target", "fake-jmh-benchmarks.jar").toAbsolutePath();
        JmhSubcommandService sut = getJmhSubcommandService()
            .withMongoConnectionString(getConnectionString())
            .withCommonOptions(new CommonSharedOptions("abcdef12", 1,  "incrementUsingSynchronized"))
            .withJmhOptions(new JmhBenchmarksSharedOptions(0, 1, 1,"", jhhTestBenchmark))
            .withS3Service(getS3ServiceBuilder()
                .withS3Client(awsS3Client)
                .withBucketName(TEST_BUCKET_NAME)
                .build())
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
                .extracting("_id.benchmarkName", as(STRING))
                .endsWith("incrementUsingSynchronized")
        );

        // and
        JSONArray objectsInTestBucket = listObjectsInTestBucket();
        assertThatJson(objectsInTestBucket)
            .inPath("$[*].Key")
            .isArray()
            .anySatisfy(o -> assertThat(o)
                .asString()
                .isEqualTo("gha-outputs/commit-abcdef12/attempt-1/jmh/output.txt"));
    }


}