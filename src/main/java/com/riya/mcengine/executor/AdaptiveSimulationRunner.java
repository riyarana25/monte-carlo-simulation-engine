package com.riya.mcengine.executor;

import java.util.ArrayList;
import java.util.List;

import com.riya.mcengine.core.Simulation;
import com.riya.mcengine.core.State;
import com.riya.mcengine.random.RandomSource;
import com.riya.mcengine.random.SeededRandomSource;
import com.riya.mcengine.stats.ConvergenceChecker;

/**
 * Runs simulations until convergence is reached.
 */
public class AdaptiveSimulationRunner {

    public <S extends State> List<S> runUntilConverged(
            Simulation<S> simulation,
            int maxIterations,
            long seed,
            double tolerance,
            int windowSize
    ) {
        RandomSource randomSource = new SeededRandomSource(seed);
        ConvergenceChecker convergenceChecker = new ConvergenceChecker(tolerance);
        List<S> results = new ArrayList<>();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            S state = simulation.initialState();
            S finalState = simulation.transition().next(state, randomSource);
            results.add(finalState);

            // convergence check must be domain-mapped later
            if (iteration >= 2 * windowSize) {
                // Build a domain-agnostic metrics list (TODO: replace with real mapping from S to double)
                List<Double> metrics = new ArrayList<>();
                // TODO: map recent `results` to `metrics` using a domain-specific measurement

                if (convergenceChecker.hasConverged(metrics)) {
                    break; // convergence reached
                }
            }
        }

        return results;
    }
}
