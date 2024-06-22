package pl.symentis.entities.jmh;

import dev.morphia.annotations.Entity;

@Entity
public record JmhBenchmarkId(
    String requestId,
    String benchmarkName,
    String benchmarkType
) { }
