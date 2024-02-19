package pl.symentis.infra;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;

import static dev.morphia.Morphia.createDatastore;
import static java.util.Objects.requireNonNull;

public class MorphiaServiceBuilder {
    public static final String DEFAULT_DB_NAME = "test-results";

    private String dbName;
    private String connectionString;

    private MorphiaServiceBuilder(){
        this.dbName = DEFAULT_DB_NAME;
    }

    public static MorphiaServiceBuilder getMorphiaServiceBuilder(){
        return new MorphiaServiceBuilder();
    }

    public MorphiaServiceBuilder withDbName(String dbName){
        requireNonNull(dbName, "Provided database name cannot be null!");
        this.dbName = dbName;
        return this;
    }

    public MorphiaServiceBuilder withConnectionString(String connectionString){
        requireNonNull(connectionString, "Provided connection string cannot be null!");
        this.connectionString = connectionString;
        return this;
    }

    public MorphiaService build() {
        requireNonNull(connectionString, "Please provide connection string!");
        MongoClient mongoClient = MongoClients
            .create(new ConnectionString(connectionString));
        Datastore datastore = createDatastore(mongoClient, dbName);
        datastore
            .getMapper()
            .mapPackage("pl.symentis.entities");
        return new MorphiaService(datastore);
    }
}
