package pl.symentis.infra;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;

import static dev.morphia.Morphia.createDatastore;
import static java.util.Objects.requireNonNull;

public class MorphiaServiceBuilder {

    private static String dbName  = "test-results";
    private String connectionString;

    private MorphiaServiceBuilder(){
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        MorphiaServiceBuilder.dbName = dbName;
    }

    public static MorphiaServiceBuilder getMorphiaServiceBuilder(){
        return new MorphiaServiceBuilder();
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
