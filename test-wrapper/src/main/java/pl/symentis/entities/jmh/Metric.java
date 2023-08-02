
package pl.symentis.entities.jmh;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Metric {

    public Double score;
    public Double scoreError;
    public List<Double> scoreConfidence = new ArrayList<>();
    public Map<Double, Double> scorePercentiles = new HashMap<>();
    public String scoreUnit;
    public List<List<Double>> rawData = new ArrayList<>();

    public Metric withScore(Double score) {
        this.score = score;
        return this;
    }

    public Metric withScoreError(Double scoreError) {
        this.scoreError = scoreError;
        return this;
    }

    public Metric withScoreConfidence(List<Double> scoreConfidence) {
        this.scoreConfidence = scoreConfidence;
        return this;
    }

    public Metric withScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
        return this;
    }

    public Metric withRawData(List<List<Double>> rawData) {
        this.rawData = rawData;
        return this;
    }

    @Override
    public String toString() {
        return "Metric{" +
            "score=" + score +
            ", scoreError=" + scoreError +
            ", scoreConfidence=" + scoreConfidence +
            ", scorePercentiles=" + scorePercentiles +
            ", scoreUnit='" + scoreUnit + '\'' +
            ", rawData=" + rawData +
            '}';
    }
}
