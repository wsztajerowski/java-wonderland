package pl.symentis.services;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;

import static dev.morphia.Morphia.createDatastore;
import static java.lang.System.getenv;

public class MorphiaService {
    private final Datastore testResultsDatastore;

    MorphiaService(Datastore datastore) {
        this.testResultsDatastore = datastore;
    }

    public static MorphiaService getMorphiaService() {
        MongoClient mongoClient = MongoClients
            .create(new ConnectionString(getenv("MONGO_CONNECTION_STRING")));
        Datastore datastore = createDatastore(mongoClient, "test-results");
        datastore
            .getMapper()
            .mapPackage("pl.symentis.entities");
        return new MorphiaService(datastore);
    }

    public Datastore getTestResultsDatastore() {
        return testResultsDatastore;
    }
}
