package pl.symentis;

import com.mongodb.MongoClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.symentis.infra.MorphiaServiceBuilder;

public class TestcontainersWithS3AndMongoBaseIT extends TestcontainersWithS3BaseIT {
    protected static final String TEST_DB_NAME = "integration-tests";
    @Container
    protected final static MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:7.0.5"))
        .withEnv("MONGO_INITDB_DATABASE", TEST_DB_NAME)
        .withExposedPorts(27017);

    protected MongoClient getMongoClient(){
        return new MongoClient(MONGO_DB_CONTAINER.getConnectionString());
    }

    @BeforeAll
    static void setTestDbName(){
        MorphiaServiceBuilder.setDbName(TEST_DB_NAME);
    }

    @AfterEach
    void dropTestDb(){
        try(MongoClient mongoClient = getMongoClient()) {
            mongoClient
                .dropDatabase(TEST_DB_NAME);
        }
    }
}
