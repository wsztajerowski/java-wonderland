
package pl.symentis.entities;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Metric {

    private Double score;
    private Double scoreError;
    private List<Double> scoreConfidence = new ArrayList<>();
    private Map<Double, Double> scorePercentiles = new HashMap<>();
    private String scoreUnit;
    private List<List<Double>> rawData = new ArrayList<>();

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Metric withScore(Double score) {
        this.score = score;
        return this;
    }

    public Double getScoreError() {
        return scoreError;
    }

    public void setScoreError(Double scoreError) {
        this.scoreError = scoreError;
    }

    public Metric withScoreError(Double scoreError) {
        this.scoreError = scoreError;
        return this;
    }

    public List<Double> getScoreConfidence() {
        return scoreConfidence;
    }

    public void setScoreConfidence(List<Double> scoreConfidence) {
        this.scoreConfidence = scoreConfidence;
    }

    public Metric withScoreConfidence(List<Double> scoreConfidence) {
        this.scoreConfidence = scoreConfidence;
        return this;
    }


    public String getScoreUnit() {
        return scoreUnit;
    }

    public void setScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
    }

    public Metric withScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
        return this;
    }

    public List<List<Double>> getRawData() {
        return rawData;
    }

    public void setRawData(List<List<Double>> rawData) {
        this.rawData = rawData;
    }

    public Metric withRawData(List<List<Double>> rawData) {
        this.rawData = rawData;
        return this;
    }

    public Map<Double, Double> getScorePercentiles() {
        return scorePercentiles;
    }

    public void setScorePercentiles(Map<Double, Double> scorePercentiles) {
        this.scorePercentiles = scorePercentiles;
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
