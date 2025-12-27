package com.riya.mcengine.random;

/**
 * Bernoulli distribution (true with probability p).
 */
public class BernoulliDistribution implements Distribution<Boolean> {

    private final double probability;

    public BernoulliDistribution(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Probability must be in [0, 1]");
        }
        this.probability = probability;
    }

    @Override
    public Boolean sample(RandomSource randomSource) {
        return randomSource.nextDouble() < probability;
    }
}
