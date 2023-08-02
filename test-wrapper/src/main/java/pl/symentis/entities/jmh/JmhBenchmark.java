package pl.symentis.entities.jmh;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;

@Entity("jmh-benchmarks")
public class JmhBenchmark {
    public JmhBenchmark(){
        this.benchmarkMetadata = new BenchmarkMetadata();
    }
    @Id
    public JmhBenchmarkId benchmarkId;

    public JmhResult jmhResult;

    public BenchmarkMetadata benchmarkMetadata;

    @PrePersist
    public void addBenchmarkTypeToCompoundId(){
        this.benchmarkId.benchmarkType =this.jmhResult.mode;
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
