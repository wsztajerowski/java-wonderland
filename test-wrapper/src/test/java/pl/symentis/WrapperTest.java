package pl.symentis;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.UpdateOptions;
import dev.morphia.query.updates.UpdateOperators;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.reflect.TypeToken;
import pl.symentis.entities.BenchmarkMetadata;
import pl.symentis.entities.JmhBenchmark;
import pl.symentis.entities.JmhBenchmarkId;
import pl.symentis.entities.JmhResult;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static dev.morphia.query.filters.Filters.eq;
import static java.nio.file.Files.list;
import static java.text.MessageFormat.format;
import static pl.symentis.FileUtils.getFilenameWithoutExtension;
import static pl.symentis.MorphiaService.getMorphiaService;

@Disabled
class WrapperTest {

    @Test
    void run_jmh_benchmark() throws IOException, InterruptedException {
//        java -jar jmh-benchmarks.jar  -f 1 -wi 1 -i 1 -rf json
        List<String> commands = new ArrayList<>();
        commands.add("java"); // command
        commands.add("-jar"); // command
        commands.add("../jmh-benchmarks/target/jmh-benchmarks.jar"); // command
        commands.add("-f"); // command
        commands.add("1"); // command
        commands.add("-i"); // command
        commands.add("5"); // command
        commands.add("-wi"); // command
        commands.add("5"); // command
        commands.add("-rf"); // command
        commands.add("json"); // command

        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.inheritIO();
        // starting the process
        Process process = pb.start();
        process.waitFor();
    }

    @Test
    void run_jmh_benchmark_with_async_profiler() throws IOException, InterruptedException {
//        java -jar jmh-benchmarks.jar  -f 1 -wi 1 -i 1 -rf json
        List<String> commands = new ArrayList<>();
        commands.add("java"); // command
        commands.add("-jar"); // command
        commands.add("../jmh-benchmarks/target/jmh-benchmarks.jar"); // command
        commands.add("-f"); // command
        commands.add("1"); // command
        commands.add("-i"); // command
        commands.add("1"); // command
        commands.add("-wi"); // command
        commands.add("1"); // command
        commands.add("-rf"); // command
        commands.add("json"); // command
        commands.add("-prof"); // command
        commands.add("async:libPath=/Users/Wiktor/workspace/way-to-neo/async-profiler/build/libasyncProfiler.so;output=flamegraph;interval=999;dir=flame"); // command

        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.inheritIO();
        pb.redirectErrorStream(true);
        // starting the process
        Process process = pb.start();
        process.waitFor();
    }

    @Test
    void save_flamegraphs_on_S3() {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.EU_NORTH_1;
        try (S3Client s3 = S3Client.builder()
            .region(region)
            .credentialsProvider(credentialsProvider)
            .build()) {

            String bucketName = "java-wonderland";
            String objectKey = "gha-outputs/async/graph.html";
            Path flamegraph = Paths.get("flame", "pl.symentis.Incrementing_DekkersLock.g1-Throughput", "flame-cpu-forward.html");
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
//                .metadata(metadata)
                .build();

            s3.putObject(putOb, RequestBody.fromFile(flamegraph));
        }
    }

    @Test
    void count_docs_from_mongo() {
        List<JmhBenchmark> jmhBenchmarks = getMorphiaService()
            .getBenchmarkDatastore()
            .find(JmhBenchmark.class)
            .iterator()
            .toList();

        jmhBenchmarks
            .forEach(System.out::println);
    }

    @Test
    void json_to_object_mapping_using_gson() throws IOException {
        List<JmhResult> benchmarks = loadBenchamrkResultAndAddFlamegraphPaths();
        System.out.println(benchmarks);
    }

    @NotNull
    private static List<JmhResult> loadBenchamrkResultAndAddFlamegraphPaths() throws IOException {
        Gson gson = Converters.registerLocalDateTime(new GsonBuilder()
//            .registerTypeAdapter(Id.class, new IdTypeAdapter())
                .enableComplexMapKeySerialization()
                .serializeNulls()
//            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .setVersion(1.0))
            .create();

        BufferedReader bufferedReader = new BufferedReader(
            new FileReader("jmh-result.json"));
        Type listType = new TypeToken<List<JmhResult>>() {
        }.getType();
        List<JmhResult> benchmarks = gson.fromJson(bufferedReader, listType);

//        Type listOfBenchmarksType = Types.newParameterizedType(List.class, Benchmark.class);
//        JsonAdapter<List<Benchmark>> jsonAdapter = moshi.adapter(listOfBenchmarksType);
//
//
//        Source fileSource = source(path);
//        List<Benchmark> benchmarks = jsonAdapter.fromJson(of(buffer(fileSource)));

        return benchmarks;
    }

    @Test
    void save_morphia_entity_to_mongo() throws IOException {
        String connectionString = System.getenv("MONGO_CONNECTION_STRING");
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            final Datastore datastore = Morphia.createDatastore(mongoClient, "benchmarks");

            // tell morphia where to find your classes
            // can be called multiple times with different packages or classes
            datastore.getMapper().mapPackage("pl.symentis.entites");

//            List<Benchmark> benchmarks = mapJsonFileToJavaList();
            List<JmhResult> benchmarks = loadBenchamrkResultAndAddFlamegraphPaths();
            int counter = 7;
            String commitSha = "129345abcefe";
            for (JmhResult jmhResult : benchmarks) {
                String dirSuffix = switch (jmhResult.mode) {
                    case "thrpt":
                        yield "-Throughput";
                    default:
                        throw new IllegalArgumentException("Unknown benchmark mode: " + jmhResult.mode);
                };
                JmhBenchmarkId benchmarkId = new JmhBenchmarkId()
                    .withCommitSha(commitSha)
                    .withBenchmarkName(jmhResult.benchmark)
                    .withBenchmarkType(jmhResult.mode)
                    .withRunAttempt( counter++);
                String flamegraphsDir = jmhResult.benchmark + dirSuffix;
                String s3Preffix = format("gha-outputs/commit-{0}/attempt-{1}/", benchmarkId.commitSha, benchmarkId.runAttempt);
                BenchmarkMetadata benchmarkMetadata = new BenchmarkMetadata();
                list(Path.of(flamegraphsDir))
                    .forEach(path -> {
                        String s3Key = s3Preffix + path.toString();
                        saveFlamegraphOnS3(s3Key, path);
                        String flamegraphName = getFilenameWithoutExtension(path);
                        benchmarkMetadata.addFlamegraphPath(flamegraphName, s3Key);
                    });


                UpdateResult update = datastore
                    .find(JmhBenchmark.class)
                    .filter(eq("benchmarkId", benchmarkId))
                    .update(new UpdateOptions()
                        .upsert(true), UpdateOperators.set("benchmarkMetadata", benchmarkMetadata));
                System.out.println("Update results: " + update);
            }

        }
    }

    private void saveFlamegraphOnS3(String objectKey, Path pathToFile) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.EU_NORTH_1;
        try (S3Client s3 = S3Client.builder()
            .region(region)
            .credentialsProvider(credentialsProvider)
            .build()) {

            String bucketName = "java-wonderland";
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
//                .metadata(metadata)
                .build();

            s3.putObject(putOb, RequestBody.fromFile(pathToFile));
        }
    }
}
