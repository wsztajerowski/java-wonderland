package pl.symentis.entities.jmh;

import dev.morphia.annotations.Entity;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
public record BenchmarkMetadata(
    OffsetDateTime createdAt,
    Map<String, String> flamegraphPaths) {

    public BenchmarkMetadata(Map<String, String> flamegraphPaths) {
        this(OffsetDateTime.now(), flamegraphPaths);
    }
}
