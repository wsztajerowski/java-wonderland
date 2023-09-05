package pl.symentis.entities.jmh;

import dev.morphia.annotations.Entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Entity
public record BenchmarkMetadata(
    LocalDateTime createdAt,
    Map<String, String> flamegraphPaths) {

    public BenchmarkMetadata(Map<String, String> flamegraphPaths) {
        this(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime(), flamegraphPaths);
    }
}
