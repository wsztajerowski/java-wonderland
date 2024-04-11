package pl.symentis;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import org.bson.*;
import org.bson.types.ObjectId;

import java.util.function.Consumer;

public class MongoDbTestHelpers {
    private final String connectionString;

    public MongoDbTestHelpers(String connectionString) {
        this.connectionString = connectionString;
    }

    public static BsonDocument byId(Object idValue) {
        BsonValue id = switch (idValue){
            case String s -> new BsonString(s);
            case ObjectId oid -> new BsonObjectId(oid);
            default -> throw new IllegalStateException("Unexpected value: " + idValue);
        };
        return new BsonDocument("_id", id);
    }

    public static BsonDocument all() {
        return new BsonDocument();
    }

    public void assertFindResult(String collectionName, BsonDocument filter, Consumer<FindIterable<Document>> resultsAssertion) {
        ConnectionString connection = new ConnectionString(connectionString);
        try (MongoClient mongoClient = new MongoClient(connection)) {
            FindIterable<Document> documents = mongoClient.getDatabase(connection.getDatabase())
                .getCollection(collectionName)
                .find(filter);
            resultsAssertion.accept(documents);
        }
    }
}
