package pl.symentis.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.PrePersist;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class BenchmarkMetadata {

    @PrePersist
    public void addCreatedAt(){
        if (createdAt == null){
            createdAt = LocalDateTime.now();
        }
    }

    private LocalDateTime createdAt;

    private Map<String, String> flamegraphPaths = new HashMap<>();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, String> getFlamegraphPaths() {
        return flamegraphPaths;
    }

    public void setFlamegraphPaths(Map<String, String> flamegraphPaths) {
        this.flamegraphPaths = flamegraphPaths;
    }

    public void addFlamegraphPath(String graphType, String graphPath) {
        this.flamegraphPaths.put(graphType, graphPath);
    }

    @Override
    public String toString() {
        return "BenchmarkMetadata{" +
            "createdAt=" + createdAt +
            ", flamegraphPaths=" + flamegraphPaths +
            '}';
    }
}
