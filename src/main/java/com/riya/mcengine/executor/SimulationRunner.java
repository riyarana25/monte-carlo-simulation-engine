package com.riya.mcengine.executor;

import java.util.ArrayList;
import java.util.List;

import com.riya.mcengine.core.Simulation;
import com.riya.mcengine.core.State;
import com.riya.mcengine.random.RandomSource;
import com.riya.mcengine.random.SeededRandomSource;

/**
 * Executes Monte Carlo simulations in a deterministic manner.
 */
public class SimulationRunner {

    /**
     * Runs the given simulation for the specified number of iterations.
     *
     * @param simulation simulation definition
     * @param numberOfIterations number of Monte Carlo runs
     * @param seed seed for deterministic replay
     * @return list of final states from each run
     */
    public <S extends State> List<S> run(
            Simulation<S> simulation,
            int numberOfIterations,
            long seed
    ) {
        RandomSource randomSource = new SeededRandomSource(seed);
        List<S> results = new ArrayList<>();

        for (int iteration = 0; iteration < numberOfIterations; iteration++) {
            S currentState = simulation.initialState();
            S finalState = simulation.transition()
                                     .next(currentState, randomSource);
            results.add(finalState);
        }

        return results;
    }
}
