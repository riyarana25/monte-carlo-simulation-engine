package com.riya.mcengine.experiment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riya.mcengine.executor.SimulationRunner;
import com.riya.mcengine.experiment.BernoulliSimulation;
import com.riya.mcengine.experiment.BernoulliState;
import com.riya.mcengine.stats.StatisticsUtils;
// import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

// import static org.junit.jupiter.api.Assertions.assertTrue;

public class BernoulliSimulationTest {

    @Test
    void monteCarloEstimatesProbability() {
        double probability = 0.6;
        SimulationRunner runner = new SimulationRunner();
        BernoulliSimulation simulation = new BernoulliSimulation(probability);

        List<BernoulliState> results =
                runner.run(simulation, 10_000, 42L);

        List<Double> numeric =
                results.stream()
                       .map(s -> s.isSuccess() ? 1.0 : 0.0)
                       .collect(Collectors.toList());

        double estimatedMean = StatisticsUtils.mean(numeric);

        assertTrue(Math.abs(estimatedMean - probability) < 0.05);
    }
}
