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

    public LocalDateTime createdAt;

    public Map<String, String> flamegraphPaths = new HashMap<>();

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
