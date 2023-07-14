package pl.symentis;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MorphiaService {
    private static MorphiaService INSTANCE;
    private final Datastore benchmarkDatastore;

    private MorphiaService(){
        String connectionString = System.getenv("MONGO_CONNECTION_STRING");
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build();
        MongoClient mongoClient = MongoClients.create(settings);
        Datastore datastore = Morphia.createDatastore(mongoClient, "benchmarks");
        datastore.getMapper().mapPackage("pl.symentis.entites");
        this.benchmarkDatastore = datastore;
    }

    public static MorphiaService getMorphiaService() {
        if (INSTANCE == null){
            INSTANCE = new MorphiaService();
        }
        return INSTANCE;
    }

    public Datastore getBenchmarkDatastore() {
        return benchmarkDatastore;
    }
}
