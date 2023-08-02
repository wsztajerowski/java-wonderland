package pl.symentis;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MorphiaService {
    private final Datastore testResultsDatastore;

    private MorphiaService(){
        String connectionString = System.getenv("MONGO_CONNECTION_STRING");
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build();
        MongoClient mongoClient = MongoClients.create(settings);
        Datastore benchmarks = Morphia.createDatastore(mongoClient, "test-results");
        benchmarks
            .getMapper()
            .mapPackage("pl.symentis.entites");
        this.testResultsDatastore = benchmarks;
    }

    public static MorphiaService getMorphiaService() {
        return new MorphiaService();
    }

    public Datastore getTestResultsDatastore() {
        return testResultsDatastore;
    }
}
