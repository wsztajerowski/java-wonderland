package pl.symentis.infra;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;

import static dev.morphia.Morphia.createDatastore;
import static java.util.Objects.requireNonNull;

public class MorphiaServiceBuilder {

    private String connectionString;

    private MorphiaServiceBuilder(){
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
        requireNonNull(connectionString, "Please provide non-null connection string in form: mongodb://server:port/database_name");
        ConnectionString typedConnectionString = new ConnectionString(connectionString);
        String database = typedConnectionString.getDatabase();
        requireNonNull(database, "Connection string has to contain database name! Please provide connection string in form: mongodb://server:port/database_name");
        MongoClient mongoClient = MongoClients
            .create(typedConnectionString);
        Datastore datastore = createDatastore(mongoClient, database);
        datastore
            .getMapper()
            .mapPackage("pl.symentis.entities");
        return new MorphiaService(datastore);
    }
}
