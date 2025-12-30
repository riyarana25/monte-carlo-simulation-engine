package com.riya.mcengine.stats;

import java.util.List;

/**
 * Computes confidence intervals assuming normal approximation.
 */
public class ConfidenceIntervalCalculator {

    /**
     * Computes a two-sided confidence interval.
     *
     * @param values sample values
     * @param confidenceLevel e.g. 0.95
     */
    public ConfidenceInterval compute(List<Double> values, double confidenceLevel) {
        if (confidenceLevel <= 0.0 || confidenceLevel >= 1.0) {
            throw new IllegalArgumentException("confidenceLevel must be in (0, 1)");
        }

        double mean = StatisticsUtils.mean(values);
        double stdDev = StatisticsUtils.standardDeviation(values);
        int n = values.size();

        // Z-score for common confidence levels
        double z;
        if (confidenceLevel == 0.95) {
            z = 1.96;
        } else if (confidenceLevel == 0.99) {
            z = 2.576;
        } else {
            throw new IllegalArgumentException("Unsupported confidence level");
        }

        double marginOfError = z * (stdDev / Math.sqrt(n));

        return new ConfidenceInterval(
                mean - marginOfError,
                mean + marginOfError,
                confidenceLevel
        );
    }
}
