package pl.symentis.services;

public record JCStressOptions(
    Integer cpuNumber,
    Integer forks,
    Integer forkMultiplier,
    Integer heapSize,
    String jvmArgs,
    String jvmArgsPrepend,
    String spinStyle,
    String reportPath,
    Boolean splitCompilationModes,
    Boolean preTouchHeap,
    Integer strideCount,
    Integer strideSize) {
}
