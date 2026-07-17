package com.riya.mcengine.domains;

import com.riya.mcengine.random.RandomSource;

/**
 * Buffon's Needle: Drop needle of length L on floor with parallel lines spacing D apart.
 * P(needle crosses line) = 2L / (πD).
 * Rearrange: π ≈ 2L / (D * P_cross).
 *
 * Ground truth: π = 3.14159265...
 * Setup: L = 1, D = 2 → P(cross) = 1/π ≈ 0.3183
 *
 * Shows:
 * - How Monte Carlo estimates irrational constants
 * - Convergence to ground truth
 * - Why rare events (small L/D) hurt variance
 */
public class BuffonNeedle {

    private final double needleLength;
    private final double lineSpacing;
    private final double crossingProbability;

    public BuffonNeedle(double needleLength, double lineSpacing) {
        this.needleLength = needleLength;
        this.lineSpacing = lineSpacing;
        this.crossingProbability = (2.0 * needleLength) / (Math.PI * lineSpacing);
    }

    /**
     * Single trial: drop needle at random angle/position, check if it crosses.
     */
    public boolean trial(RandomSource rng) {
        double angle = rng.nextDouble() * Math.PI;
        double positionY = rng.nextDouble() * lineSpacing;

        double projectionLength = Math.abs((needleLength / 2.0) * Math.sin(angle));
        return positionY < projectionLength || positionY > (lineSpacing - projectionLength);
    }

    /**
     * Run n trials, return: estimated π, actual crossings, error
     */
    public double[] estimate(int n, RandomSource rng) {
        int crossings = 0;
        for (int i = 0; i < n; i++) {
            if (trial(rng)) crossings++;
        }

        double piEstimate = (2.0 * needleLength) / (lineSpacing * crossings / (double) n);
        double error = Math.abs(piEstimate - Math.PI);

        return new double[]{piEstimate, crossings, error};
    }

    public double getGroundTruth() {
        return Math.PI;
    }

    public double getCrossingProbability() {
        return crossingProbability;
    }
}
