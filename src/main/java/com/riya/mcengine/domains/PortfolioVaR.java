package com.riya.mcengine.domains;

import com.riya.mcengine.random.NormalDistribution;
import com.riya.mcengine.random.RandomSource;

/**
 * Portfolio Value-at-Risk (VaR): 2-asset portfolio with correlated returns.
 * Geometric Brownian motion: dS = μS dt + σS dW
 *
 * Setup: S1=100, S2=100, w1=0.6, w2=0.4, r=0.05, σ1=0.2, σ2=0.3, ρ=0.5
 * 1-day horizon, 95% VaR.
 *
 * Analytical approximation (delta-normal): VaR ≈ 1.645 * σ_p * V0
 *
 * Shows:
 * - Multi-dimensional simulation (correlated Gaussians)
 * - Why delta-normal fails under skew (real returns are leptokurtic)
 * - Variance reduction potential (control variate: delta-normal approximation)
 */
public class PortfolioVaR {

    private final double[] initialPrices;
    private final double[] weights;
    private final double[] drifts;
    private final double[] vols;
    private final double correlation;
    private final double timeHorizon;
    private final NormalDistribution normal = new NormalDistribution();

    public PortfolioVaR() {
        this.initialPrices = new double[]{100.0, 100.0};
        this.weights = new double[]{0.6, 0.4};
        this.drifts = new double[]{0.05, 0.05};
        this.vols = new double[]{0.2, 0.3};
        this.correlation = 0.5;
        this.timeHorizon = 1.0 / 252.0;
    }

    /**
     * Simulate one step: returns vector for both assets using correlated Gaussians.
     */
    public double[] sampleReturns(RandomSource rng) {
        double z1 = normal.sample(rng).doubleValue();
        double z2 = normal.sample(rng).doubleValue();

        double corr_z1 = z1;
        double corr_z2 = correlation * z1 + Math.sqrt(1 - correlation * correlation) * z2;

        double r1 = drifts[0] * timeHorizon + vols[0] * Math.sqrt(timeHorizon) * corr_z1;
        double r2 = drifts[1] * timeHorizon + vols[1] * Math.sqrt(timeHorizon) * corr_z2;

        return new double[]{r1, r2};
    }

    /**
     * Simulate portfolio: (1 + r1) * w1 + (1 + r2) * w2
     */
    public double singleScenario(RandomSource rng) {
        double[] returns = sampleReturns(rng);
        double portfolioReturn = weights[0] * (1 + returns[0]) + weights[1] * (1 + returns[1]);
        return portfolioReturn - 1.0;
    }

    /**
     * Delta-normal control variate: (r1*w1 + r2*w2) where r ~ N(μ, σ^2)
     */
    public double deltaNormalControl(RandomSource rng) {
        double[] returns = sampleReturns(rng);
        return weights[0] * returns[0] + weights[1] * returns[1];
    }

    /**
     * Compute analytical delta-normal VaR as control mean.
     */
    public double getDeltaNormalVaR() {
        double muP = weights[0] * drifts[0] + weights[1] * drifts[1];
        double sigmaP = Math.sqrt(
            weights[0] * weights[0] * vols[0] * vols[0] +
            weights[1] * weights[1] * vols[1] * vols[1] +
            2 * weights[0] * weights[1] * correlation * vols[0] * vols[1]
        );
        double sqrtT = Math.sqrt(timeHorizon);
        return 1.645 * sigmaP * sqrtT;
    }

    /**
     * Run MC simulation and return VaR estimate
     */
    public double[] estimateVaR(int n, RandomSource rng) {
        double[] returns = new double[n];
        for (int i = 0; i < n; i++) {
            returns[i] = singleScenario(rng);
        }

        java.util.Arrays.sort(returns);
        double var95 = -returns[(int) (0.05 * n)];
        double mean = java.util.Arrays.stream(returns).average().orElse(0);
        double variance = java.util.Arrays.stream(returns)
            .map(r -> (r - mean) * (r - mean))
            .average().orElse(0);

        return new double[]{var95, Math.sqrt(variance), mean};
    }
}
