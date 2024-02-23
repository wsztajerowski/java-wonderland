package pl.symentis.services;

import dev.morphia.annotations.Entity;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.symentis.MongoDbTestHelpers;
import pl.symentis.TestcontainersWithS3AndMongoBaseIT;
import pl.symentis.entities.jcstress.JCStressTest;
import pl.symentis.infra.MorphiaServiceBuilder;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static pl.symentis.MongoDbTestHelpers.all;
import static pl.symentis.infra.S3ServiceBuilder.getS3ServiceBuilder;

class JCStressSubcommandServiceIT extends TestcontainersWithS3AndMongoBaseIT {

    private static MongoDbTestHelpers helper;

    @BeforeAll
    static void setupHelper(){
        helper = new MongoDbTestHelpers(MONGO_DB_CONTAINER.getConnectionString(), MorphiaServiceBuilder.getDbName());
    }

    @Test
    void successful_scenario(){
        // given
        String stressTestJarPath = createPathForTestResource("fake-stress-tests.jar").toAbsolutePath().toString();
        JCStressSubcommandService sut = JCStressSubcommandServiceBuilder.getJCStressSubcommandService()
            .withMongoConnectionString(MONGO_DB_CONTAINER.getConnectionString())
            .withS3Service(getS3ServiceBuilder()
                .withS3Client(awsS3Client)
                .withBucketName(TEST_BUCKET_NAME)
                .build())
            .withCommonOptions(new CommonSharedOptions("commit-sha", 1, "", "IntegerIncrementing"))
            .withBenchmarkPath(stressTestJarPath)
            .build();

        // when
        sut.executeCommand();

        // then
        String collectionName = JCStressTest.class.getAnnotation(Entity.class).value();
        helper.assertFindResult(collectionName, all(), documents ->
            assertThat(documents.first())
                .isNotNull()
                .containsEntry("_t", "JCStressTest")
                .extracting("result", as(MAP))
                    .containsEntry("totalTests", 2)
                    .containsEntry("passedTests", 1)
                    .extracting("testsWithFailedResults", as(MAP))
                        .containsKey("pl.symentis.IntegerIncrementing.TestWithForbiddenResults")
        );

        // and
        JSONArray objectsInTestBucket = listObjectsInTestBucket();
        assertThatJson(objectsInTestBucket)
            .inPath("$[*].Key")
            .isArray()
            .anySatisfy(o -> assertThat(o)
                .asString()
                .endsWith("TestWithForbiddenResults.html"));
    }

}