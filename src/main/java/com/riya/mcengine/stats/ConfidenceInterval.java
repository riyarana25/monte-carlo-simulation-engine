package com.riya.mcengine.stats;

/**
 * Represents a confidence interval for a statistic.
 */
public class ConfidenceInterval {

    private final double lowerBound;
    private final double upperBound;
    private final double confidenceLevel;

    public ConfidenceInterval(double lowerBound, double upperBound, double confidenceLevel) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.confidenceLevel = confidenceLevel;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getConfidenceLevel() {
        return confidenceLevel;
    }
}
