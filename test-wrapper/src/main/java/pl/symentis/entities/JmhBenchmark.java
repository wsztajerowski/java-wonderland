package pl.symentis.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;

@Entity("jmh-benchmarks")
public class JmhBenchmark {
    @Id
    private JmhBenchmarkId benchmarkId;

    private JmhResult jmhResult;

    private BenchmarkMetadata benchmarkMetadata;

    public JmhBenchmarkId getBenchmarkId() {
        return benchmarkId;
    }

    public void setBenchmarkId(JmhBenchmarkId benchmarkId) {
        this.benchmarkId = benchmarkId;
    }

    @PrePersist
    public void addBenchmarkTypeToCompoundId(){
        this.benchmarkId.setBenchmarkType(this.jmhResult.getMode());
    }

    @Override
    public String toString() {
        return "JmhBenchmark{" +
            "benchmarkId=" + benchmarkId +
            ", jmhResult=" + jmhResult +
            ", benchmarkMetadata=" + benchmarkMetadata +
            '}';
    }
}
