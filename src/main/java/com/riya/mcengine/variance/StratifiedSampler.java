package com.riya.mcengine.variance;

import com.riya.mcengine.random.RandomSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Stratified sampling: partition domain into strata, sample proportionally
 * from each. Reduces variance when outcome varies by stratum.
 *
 * Example: rare events—sample more from tail region.
 */
public class StratifiedSampler {

    private final List<StratumBound> strata;

    public StratifiedSampler(List<StratumBound> strata) {
        this.strata = strata;
    }

    /**
     * Sample uniformly from stratum i.
     */
    public double sampleFromStratum(int stratumIndex, RandomSource rng) {
        StratumBound s = strata.get(stratumIndex);
        double u = rng.nextDouble();
        return s.lower + u * (s.upper - s.lower);
    }

    /**
     * Stratified estimate: average stratum contributions.
     */
    public double estimate(List<Double> outcomesByStratum) {
        double sum = 0;
        for (int i = 0; i < strata.size(); i++) {
            double stratumWeight = strata.get(i).weight;
            sum += stratumWeight * outcomesByStratum.get(i);
        }
        return sum;
    }

    public int getNumStrata() {
        return strata.size();
    }

    public StratumBound getStratum(int i) {
        return strata.get(i);
    }

    public static class StratumBound {
        public double lower;
        public double upper;
        public double weight;

        public StratumBound(double lower, double upper, double weight) {
            this.lower = lower;
            this.upper = upper;
            this.weight = weight;
        }
    }
}
