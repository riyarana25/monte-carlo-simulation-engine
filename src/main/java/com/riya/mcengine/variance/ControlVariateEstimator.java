package com.riya.mcengine.variance;

import java.util.ArrayList;
import java.util.List;

/**
 * Control Variate variance reduction: E[X] ≈ E[X + c(Y - E[Y])].
 * Optimal c* = -Cov(X,Y) / Var(Y).
 *
 * When Y is correlated with X and E[Y] is known, this reduces variance.
 * Example: pricing call option (X) using stock price (Y) as control.
 */
public class ControlVariateEstimator {

    private final double optimalCoefficient;
    private final double controlMean;

    /**
     * @param samples paired (outcome, control) samples
     * @param knownControlMean E[Y] from theory
     */
    public ControlVariateEstimator(List<double[]> samples, double knownControlMean) {
        this.controlMean = knownControlMean;
        this.optimalCoefficient = computeOptimalCoefficient(samples);
    }

    /**
     * Compute c* = -Cov(X,Y) / Var(Y)
     */
    private double computeOptimalCoefficient(List<double[]> samples) {
        if (samples.size() < 2) return 0.0;

        double meanX = 0, meanY = 0;
        for (double[] pair : samples) {
            meanX += pair[0];
            meanY += pair[1];
        }
        meanX /= samples.size();
        meanY /= samples.size();

        double cov = 0, varY = 0;
        for (double[] pair : samples) {
            cov += (pair[0] - meanX) * (pair[1] - meanY);
            varY += (pair[1] - meanY) * (pair[1] - meanY);
        }
        cov /= samples.size();
        varY /= samples.size();

        return varY > 1e-10 ? -cov / varY : 0.0;
    }

    /**
     * Adjusted estimate: X_adj = X + c*(Y - E[Y])
     */
    public double estimateAdjusted(double outcome, double control) {
        return outcome + optimalCoefficient * (control - controlMean);
    }

    public double getOptimalCoefficient() {
        return optimalCoefficient;
    }
}
