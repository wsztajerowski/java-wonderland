package pl.symentis.entities.jmh;

import dev.morphia.annotations.Entity;

@Entity
public record JmhBenchmarkId(
    String commitSha,
    String benchmarkName,
    String benchmarkType,
    Integer runAttempt) { }
