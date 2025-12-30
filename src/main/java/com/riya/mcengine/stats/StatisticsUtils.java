package com.riya.mcengine.stats;

import java.util.List;

/**
 * Utility methods for basic statistical computations.
 */
public final class StatisticsUtils {

    private StatisticsUtils() {
        // utility class
    }

    public static double mean(List<Double> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Values list must not be empty");
        }

        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public static double variance(List<Double> values) {
        double mean = mean(values);
        double sumSquaredDiffs = 0.0;

        for (double value : values) {
            double diff = value - mean;
            sumSquaredDiffs += diff * diff;
        }

        return sumSquaredDiffs / values.size();
    }

    public static double standardDeviation(List<Double> values) {
        return Math.sqrt(variance(values));
    }
}
