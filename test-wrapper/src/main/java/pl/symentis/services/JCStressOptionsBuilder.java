package pl.symentis.services;

import java.nio.file.Path;

public final class JCStressOptionsBuilder {
    private Integer cpuNumber;
    private Integer forks;
    private Integer forkMultiplier;
    private Integer heapSize;
    private String jvmArgs;
    private String jvmArgsPrepend;
    private String spinStyle;
    private Path reportPath;
    private Boolean splitCompilationModes;
    private Boolean preTouchHeap;
    private Integer strideCount;
    private Integer strideSize;

    private JCStressOptionsBuilder() {
    }

    public static JCStressOptionsBuilder jcStressOptionsBuilder() {
        return new JCStressOptionsBuilder();
    }

    public JCStressOptionsBuilder withCpuNumber(Integer cpuNumber) {
        this.cpuNumber = cpuNumber;
        return this;
    }

    public JCStressOptionsBuilder withForks(Integer forks) {
        this.forks = forks;
        return this;
    }

    public JCStressOptionsBuilder withForkMultiplier(Integer forkMultiplier) {
        this.forkMultiplier = forkMultiplier;
        return this;
    }

    public JCStressOptionsBuilder withHeapSize(Integer heapSize) {
        this.heapSize = heapSize;
        return this;
    }

    public JCStressOptionsBuilder withJvmArgs(String jvmArgs) {
        this.jvmArgs = jvmArgs;
        return this;
    }

    public JCStressOptionsBuilder withJvmArgsPrepend(String jvmArgsPrepend) {
        this.jvmArgsPrepend = jvmArgsPrepend;
        return this;
    }

    public JCStressOptionsBuilder withSpinStyle(String spinStyle) {
        this.spinStyle = spinStyle;
        return this;
    }

    public JCStressOptionsBuilder withReportPath(Path reportPath) {
        this.reportPath = reportPath;
        return this;
    }

    public JCStressOptionsBuilder withSplitCompilationModes(Boolean splitCompilationModes) {
        this.splitCompilationModes = splitCompilationModes;
        return this;
    }

    public JCStressOptionsBuilder withPreTouchHeap(Boolean preTouchHeap) {
        this.preTouchHeap = preTouchHeap;
        return this;
    }

    public JCStressOptionsBuilder withStrideCount(Integer strideCount) {
        this.strideCount = strideCount;
        return this;
    }

    public JCStressOptionsBuilder withStrideSize(Integer strideSize) {
        this.strideSize = strideSize;
        return this;
    }

    public JCStressOptions build() {
        return new JCStressOptions(cpuNumber, forks, forkMultiplier, heapSize, jvmArgs, jvmArgsPrepend, spinStyle, reportPath, splitCompilationModes, preTouchHeap, strideCount, strideSize);
    }
}
