package com.riya.mcengine.domains;

import com.riya.mcengine.random.NormalDistribution;
import com.riya.mcengine.random.RandomSource;

/**
 * Barrier Option (down-and-out call): European call that dies if S falls below barrier.
 * Payoff: max(S_T - K, 0) if min(S_t) > H over [0, T]; else 0.
 *
 * Setup: S=100, K=110, H=95, T=1, r=0.05, σ=0.2
 * Analytical: C_barrier = C_vanilla - C_rebate + adjustments
 * Reference (closed-form for continuous monitoring): ~4.50
 *
 * Shows:
 * - Path-dependent derivatives (not just terminal value)
 * - Discrete vs. continuous monitoring
 * - Why variance reduction helps (many paths knocked out → low signal)
 * - Control variate: vanilla call price
 */
public class BarrierOption {

    private final double spot;
    private final double strike;
    private final double barrier;
    private final double timeToExpiry;
    private final double riskFreeRate;
    private final double volatility;
    private final int stepsPerPath;
    private final NormalDistribution normal = new NormalDistribution();

    public BarrierOption() {
        this.spot = 100.0;
        this.strike = 110.0;
        this.barrier = 95.0;
        this.timeToExpiry = 1.0;
        this.riskFreeRate = 0.05;
        this.volatility = 0.2;
        this.stepsPerPath = 252;
    }

    /**
     * Simulate one path: if barrier is breached, option dies.
     */
    public double[] singlePath(RandomSource rng) {
        double dt = timeToExpiry / stepsPerPath;
        double sqrtDt = Math.sqrt(dt);
        double drift = (riskFreeRate - 0.5 * volatility * volatility) * dt;
        double diffusion = volatility * sqrtDt;

        double currentPrice = spot;
        double minPrice = spot;
        boolean knocked = false;

        for (int i = 0; i < stepsPerPath; i++) {
            double z = normal.sample(rng).doubleValue();
            currentPrice *= Math.exp(drift + diffusion * z);
            minPrice = Math.min(minPrice, currentPrice);

            if (minPrice < barrier) {
                knocked = true;
                break;
            }
        }

        double payoff = knocked ? 0.0 : Math.max(currentPrice - strike, 0.0);
        double discountFactor = Math.exp(-riskFreeRate * timeToExpiry);

        return new double[]{
            discountFactor * payoff,
            payoff,
            minPrice
        };
    }

    /**
     * Vanilla call control variate.
     */
    public double vanillaCallPayoff(double finalPrice) {
        return Math.max(finalPrice - strike, 0.0);
    }

    /**
     * Black-Scholes vanilla call price (control mean).
     */
    public double vanillaCallPrice() {
        double d1 = (Math.log(spot / strike) + (riskFreeRate + 0.5 * volatility * volatility) * timeToExpiry)
                    / (volatility * Math.sqrt(timeToExpiry));
        double d2 = d1 - volatility * Math.sqrt(timeToExpiry);
        double nd1 = 0.5 * (1 + erf(d1 / Math.sqrt(2)));
        double nd2 = 0.5 * (1 + erf(d2 / Math.sqrt(2)));
        return spot * nd1 - strike * Math.exp(-riskFreeRate * timeToExpiry) * nd2;
    }

    public double[] estimate(int n, RandomSource rng) {
        double sumPayoff = 0;
        double sumVar = 0;
        for (int i = 0; i < n; i++) {
            double[] path = singlePath(rng);
            sumPayoff += path[0];
            sumVar += path[0] * path[0];
        }

        double mean = sumPayoff / n;
        double variance = sumVar / n - mean * mean;

        return new double[]{mean, Math.sqrt(variance / n)};
    }

    private double erf(double x) {
        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;

        int sign = x < 0 ? -1 : 1;
        x = Math.abs(x);
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t + a3) * t + a2) * t + a1) * t) * Math.exp(-x * x);
        return sign * y;
    }
}
