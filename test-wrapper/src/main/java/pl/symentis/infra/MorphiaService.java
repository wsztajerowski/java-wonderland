package pl.symentis.infra;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.UpdateOptions;
import dev.morphia.query.filters.Filter;

import java.util.List;

import static dev.morphia.Morphia.createDatastore;
import static dev.morphia.query.filters.Filters.eq;
import static dev.morphia.query.updates.UpdateOperators.set;
import static java.lang.System.getenv;
import static java.util.Objects.requireNonNull;

public class MorphiaService {
    protected final Datastore datastore;

    MorphiaService(Datastore datastore) {
        this.datastore = datastore;
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

    public <T> void save(T entity) {
        datastore
            .insert(entity);
    }

    public <T> List<T> listAll(Class<T> entity) {
        return datastore
            .find(entity)
            .stream()
            .toList();
    }

    public <T> MorphiaUpsertService<T> upsert(Class<T> entity) {
        return new MorphiaUpsertService<>(entity);
    }

    public class MorphiaUpsertService<T> {
        private final Class<T> entity;
        private Filter filter;

        public MorphiaUpsertService(Class<T> entity) {
            this.entity = entity;
        }

        public MorphiaUpsertService<T> byFieldValue(String fieldName, Object fieldValue) {
            filter = eq(fieldName, fieldValue);
            return this;
        }

        public void setValue(String fieldName, Object fieldValue) {
            requireNonNull(filter, "Please set filter (e.g. by using byFieldValue method) before executing this method!");
            datastore
                .find(entity)
                .filter(filter)
                .update(new UpdateOptions().upsert(true),
                    set(fieldName, fieldValue));
        }
    }
}
