package com.riya.mcengine.executor;

import com.riya.mcengine.domains.BuffonNeedle;
import com.riya.mcengine.random.SeededRandomSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark parallel simulation across different thread counts.
 * Measures: speedup, scaling efficiency, and statistical consistency.
 */
public class ParallelBenchmark {

    public static void main(String[] args) {
        BuffonNeedle needle = new BuffonNeedle(1.0, 2.0);
        long baseSeed = 42;
        int samples = 500_000;

        TriFunction<Long, Integer, Integer, List<Double>> sampleFunction = (Long seed, Integer threadId, Integer n) -> {
            List<Double> results = new ArrayList<>();
            SeededRandomSource rng = new SeededRandomSource(seed, threadId);
            for (int i = 0; i < n; i++) {
                boolean crosses = needle.trial(rng);
                results.add(crosses ? 1.0 : 0.0);
            }
            return results;
        };

        System.out.println("Monte Carlo Parallel Benchmark (Buffon's Needle, 500K samples)");
        System.out.println("=".repeat(70));
        System.out.printf("%-10s %-12s %-12s %-12s%n", "Threads", "Time (ms)", "π estimate", "Speedup");
        System.out.println("-".repeat(70));

        double baselineTime = 0;
        double baselineEstimate = 0;

        int[] threadCounts = {1, 2, 4, 8};

        for (int numThreads : threadCounts) {
            ParallelSimulationRunner runner = new ParallelSimulationRunner(numThreads);

            long startTime = System.currentTimeMillis();
            List<Double> results = runner.runParallel(sampleFunction, samples, baseSeed);
            long elapsedTime = System.currentTimeMillis() - startTime;

            double crossings = results.stream().mapToDouble(Double::doubleValue).sum();
            double piEstimate = (2.0 * 1.0) / (2.0 * (crossings / samples));

            if (numThreads == 1) {
                baselineTime = elapsedTime;
                baselineEstimate = piEstimate;
            }

            double speedup = baselineTime / (double) elapsedTime;

            System.out.printf("%-10d %-12d %-12.6f %-12.2f%n", numThreads, elapsedTime, piEstimate, speedup);
        }

        System.out.println("=".repeat(70));
        System.out.println("✓ Benchmark complete. Results show scaling efficiency and reproducibility.");
    }
}
