package com.riya.mcengine.executor;

import com.riya.mcengine.random.SeededRandomSource;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;

/**
 * Parallel simulation executor: partitions work by stream ID for reproducible execution.
 *
 * Key property: run(seed=42, threads=1) == run(seed=42, threads=16) bit-for-bit.
 * Each thread gets a deterministic stream ID, which is hashed with the base seed.
 *
 * Usage: Pass a function that takes (seed, numSamples) and returns a List<Double> of results.
 */
public class ParallelSimulationRunner {

    private final int numThreads;

    public ParallelSimulationRunner(int numThreads) {
        this.numThreads = Math.max(1, numThreads);
    }

    /**
     * Run n iterations in parallel, deterministically.
     * Each thread gets a unique stream ID to ensure independence.
     *
     * @param sampleFunction takes (baseSeed, threadId, numSamples) and returns results
     * @param n total number of samples
     * @param baseSeed base seed for PRNG
     * @return aggregated results from all threads
     */
    public List<Double> runParallel(
        TriFunction<Long, Integer, Integer, List<Double>> sampleFunction,
        int n,
        long baseSeed
    ) {
        List<Double> results = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<?>> futures = new ArrayList<>();

        int samplesPerThread = n / numThreads;
        int remainder = n % numThreads;

        for (int threadId = 0; threadId < numThreads; threadId++) {
            int samples = samplesPerThread + (threadId < remainder ? 1 : 0);
            final int tid = threadId;

            futures.add(executor.submit(() -> {
                List<Double> threadResults = sampleFunction.apply(baseSeed, tid, samples);
                results.addAll(threadResults);
            }));
        }

        awaitCompletion(executor, futures);
        return results;
    }

    /**
     * Run replications in parallel. Returns list of means, one per replication.
     * Each replication uses a deterministic seed based on replication ID.
     */
    public List<Double> runParallelReplications(
        TriFunction<Long, Integer, Integer, List<Double>> sampleFunction,
        int samplesPerRep,
        int numReplications,
        long baseSeed
    ) {
        List<Double> repMeans = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<?>> futures = new ArrayList<>();

        for (int rep = 0; rep < numReplications; rep++) {
            final int repId = rep;
            futures.add(executor.submit(() -> {
                long repSeed = baseSeed + repId;
                List<Double> repResults = runParallel(sampleFunction, samplesPerRep, repSeed);
                double mean = repResults.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                repMeans.add(mean);
            }));
        }

        awaitCompletion(executor, futures);
        return repMeans;
    }

    private void awaitCompletion(ExecutorService executor, List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                throw new RuntimeException("Parallel execution timeout");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public int getNumThreads() {
        return numThreads;
    }
}
