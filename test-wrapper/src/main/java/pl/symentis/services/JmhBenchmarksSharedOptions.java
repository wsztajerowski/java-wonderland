package pl.symentis.services;

public record JmhBenchmarksSharedOptions(int warmupIterations, int forks, int iterations, String benchmarkPath) { }
