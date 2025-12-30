package com.riya.mcengine.stats;

import java.util.List;

/**
 * Simple convergence checker based on running standard deviation.
 */
public class ConvergenceChecker {

    private final double tolerance;

    public ConvergenceChecker(double tolerance) {
        this.tolerance = tolerance;
    }

    public boolean hasConverged(List<Double> values) {
        if (values.size() < 2) {
            return false;
        }
        double stdDev = StatisticsUtils.standardDeviation(values);
        return stdDev < tolerance;
    }
}
