package com.riya.mcengine.empirical;

import java.util.*;

/**
 * Empirical validation harness: run 10K replications of each estimator,
 * measure coverage, variance, and error vs. ground truth.
 */
public class EmpiricalValidation {

    public static class Result {
        public String domain;
        public String estimator;
        public double pointEstimate;
        public double standardError;
        public double bias;
        public double mse;
        public double ciLower;
        public double ciUpper;
        public boolean containsTrue;
        public long computeTimeMs;

        @Override
        public String toString() {
            return String.format(
                "%s | %s | Est: %.6f ± %.6f | Bias: %.6f | MSE: %.6f | CI: [%.6f, %.6f] | Contains: %s",
                domain, estimator, pointEstimate, standardError, bias, mse, ciLower, ciUpper, containsTrue
            );
        }
    }

    /**
     * Compute 95% CI assuming normality: mean ± 1.96 * se
     */
    public static Result computeResult(
        String domain,
        String estimator,
        List<Double> replications,
        double groundTruth,
        long computeTimeMs
    ) {
        if (replications.isEmpty()) {
            throw new IllegalArgumentException("Empty replications");
        }

        double sum = 0, sumSq = 0;
        for (double x : replications) {
            sum += x;
            sumSq += x * x;
        }

        int n = replications.size();
        double mean = sum / n;
        double variance = sumSq / n - mean * mean;
        double se = Math.sqrt(variance / n);
        double bias = mean - groundTruth;
        double mse = variance + bias * bias;

        double z95 = 1.96;
        double ciLower = mean - z95 * se;
        double ciUpper = mean + z95 * se;
        boolean contains = groundTruth >= ciLower && groundTruth <= ciUpper;

        Result r = new Result();
        r.domain = domain;
        r.estimator = estimator;
        r.pointEstimate = mean;
        r.standardError = se;
        r.bias = bias;
        r.mse = mse;
        r.ciLower = ciLower;
        r.ciUpper = ciUpper;
        r.containsTrue = contains;
        r.computeTimeMs = computeTimeMs;
        return r;
    }

    public static void printResults(List<Result> results) {
        Map<String, List<Result>> byDomain = new LinkedHashMap<>();
        for (Result r : results) {
            byDomain.computeIfAbsent(r.domain, k -> new ArrayList<>()).add(r);
        }

        for (String domain : byDomain.keySet()) {
            System.out.println("\n=== " + domain + " ===");
            for (Result r : byDomain.get(domain)) {
                System.out.println(r);
            }
        }

        System.out.println("\n=== Coverage (% of 10K reps containing truth) ===");
        Map<String, Integer> coverageByEstimator = new LinkedHashMap<>();
        for (Result r : results) {
            String key = r.estimator;
            if (r.containsTrue) {
                coverageByEstimator.put(key, coverageByEstimator.getOrDefault(key, 0) + 1);
            }
        }
        for (String est : coverageByEstimator.keySet()) {
            double pct = 100.0 * coverageByEstimator.get(est) / results.size();
            System.out.println(est + ": " + String.format("%.1f%%", pct));
        }

        System.out.println("\n=== Variance Reduction (relative to naive) ===");
        Map<String, Map<String, Double>> varianceByDomainEstimator = new LinkedHashMap<>();
        for (Result r : results) {
            varianceByDomainEstimator
                .computeIfAbsent(r.domain, k -> new LinkedHashMap<>())
                .put(r.estimator, r.standardError * r.standardError);
        }

        for (String domain : varianceByDomainEstimator.keySet()) {
            Map<String, Double> variances = varianceByDomainEstimator.get(domain);
            double naiveVar = variances.getOrDefault("Naive", 1.0);
            System.out.println(domain + ":");
            for (String est : variances.keySet()) {
                double ratio = naiveVar / variances.get(est);
                System.out.println("  " + est + ": " + String.format("%.2f", ratio) + "x");
            }
        }
    }
}
