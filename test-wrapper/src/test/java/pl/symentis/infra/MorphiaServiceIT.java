package pl.symentis.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.symentis.MongoDbTestHelpers;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.symentis.MongoDbTestHelpers.byId;
import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

@Testcontainers
class MorphiaServiceIT {
    private static final String TEST_DB_NAME = "morphia-service-tests";
    @Container
    protected final static MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:7.0.5"))
        .withExposedPorts(27017);
    private MorphiaService sut;
    private MongoDbTestHelpers helper;

    @BeforeEach
    void setupSut(){
        sut = getMorphiaServiceBuilder()
            .withConnectionString(MONGO_DB_CONTAINER.getConnectionString())
            .withDbName(TEST_DB_NAME)
            .build();
        helper = new MongoDbTestHelpers(MONGO_DB_CONTAINER.getConnectionString(), TEST_DB_NAME);
    }

    @Test
    void test_isMongoRunning() {
        assertThat(MONGO_DB_CONTAINER.isRunning())
            .isTrue();
    }

    @Test
    void read_data_saved_by_morphia_service(){
        // given
        TestEntity entity = new TestEntity("benchmarks", 23);

        // when
        sut.save(entity);

        // then
        helper.assertFindResult(byId("benchmarks"), documents ->
            assertThat(documents.first())
                .isNotNull()
                .containsEntry("_id", "benchmarks")
                .containsEntry("quantity", 23) );
    }


}