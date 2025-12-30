package com.riya.mcengine.experiment;

import com.riya.mcengine.core.Simulation;
import com.riya.mcengine.core.Transition;
import com.riya.mcengine.random.BernoulliDistribution;
import com.riya.mcengine.random.RandomSource;

public class BernoulliSimulation implements Simulation<BernoulliState> {

    private final BernoulliDistribution distribution;

    public BernoulliSimulation(double probability) {
        this.distribution = new BernoulliDistribution(probability);
    }

    @Override
    public BernoulliState initialState() {
        return new BernoulliState(false);
    }

    @Override
    public Transition<BernoulliState> transition() {
        return (currentState, randomSource) ->
                new BernoulliState(distribution.sample(randomSource));
    }
}
