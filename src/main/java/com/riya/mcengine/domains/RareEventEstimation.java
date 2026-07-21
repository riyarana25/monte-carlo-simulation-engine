package com.riya.mcengine.domains;

import com.riya.mcengine.random.NormalDistribution;
import com.riya.mcengine.random.RandomSource;

/**
 * Rare Event: Estimate P(Z > threshold) where Z ~ N(0, 1).
 *
 * Naive: 10^6 samples, most are < 4σ. Wasteful.
 * Importance Sampling: shift mean to threshold, reweight.
 *
 * Tail probability, e.g., P(Z > 5) = 2.87 × 10^-7
 * IS: sample from N(5, 1), scale by likelihood ratio exp(-Z^2/2 + (Z-5)^2/2)
 *
 * Shows:
 * - Why naive MC fails for rare events (extreme variance)
 * - Importance sampling as the fix
 * - Exponential variance reduction
 * - The connection to large deviations theory
 */
public class RareEventEstimation {

    private final double threshold;
    private final NormalDistribution normal = new NormalDistribution();

    public RareEventEstimation(double threshold) {
        this.threshold = threshold;
    }

    /**
     * Naive: sample from N(0,1), indicator if > threshold.
     * Very inefficient for large thresholds.
     */
    public double naiveSample(RandomSource rng) {
        double z = normal.sample(rng).doubleValue();
        return z > threshold ? 1.0 : 0.0;
    }

    /**
     * Importance sampling: sample from N(threshold, 1), reweight.
     * Likelihood ratio: p(z)/q(z) = exp(-z^2/2) / exp(-(z-threshold)^2/2)
     *                              = exp(-threshold*z + threshold^2/2)
     */
    public double importanceSamplePayoff(RandomSource rng) {
        double z = threshold + normal.sample(rng).doubleValue();
        double likelihood = Math.exp(-threshold * z + threshold * threshold / 2.0);
        return z > threshold ? likelihood : 0.0;
    }

    /**
     * Analytical tail probability P(Z > threshold) for a standard normal.
     */
    public double analyticalProbability() {
        return 0.5 * (1.0 - erf(threshold / Math.sqrt(2.0)));
    }

    /**
     * Run naive and IS estimators, return both.
     */
    public double[] estimate(int n, RandomSource rng) {
        double naiveSum = 0;
        double isSum = 0;

        for (int i = 0; i < n; i++) {
            naiveSum += naiveSample(rng);
            isSum += importanceSamplePayoff(rng);
        }

        return new double[]{
            naiveSum / n,      // Naive estimate
            isSum / n,         // IS estimate
            analyticalProbability()
        };
    }

    private double erf(double x) {
        double a1 = 0.254829592, a2 = -0.284496736, a3 = 1.421413741;
        double a4 = -1.453152027, a5 = 1.061405429;
        double p = 0.3275911;

        int sign = x < 0 ? -1 : 1;
        x = Math.abs(x);
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t + a3) * t + a2) * t + a1) * t) * Math.exp(-x * x);
        return sign * y;
    }
}
