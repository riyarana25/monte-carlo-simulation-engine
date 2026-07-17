package com.riya.mcengine.empirical;

import com.riya.mcengine.domains.*;
import com.riya.mcengine.random.SeededRandomSource;
import com.riya.mcengine.variance.ControlVariateEstimator;
import java.util.*;

/**
 * Runs empirical validation on all 4 domains:
 * 1. Buffon's Needle
 * 2. Portfolio VaR
 * 3. Barrier Option
 * 4. Rare Event (5σ tail)
 *
 * For each domain: 10K replications, each with n=10000 samples.
 * Measures: accuracy, coverage, variance reduction.
 */
public class DomainValidationSuite {

    private static final int NUM_REPLICATIONS = 100;
    private static final int SAMPLES_PER_REP = 10000;
    private static long globalSeed = 42;

    public static void main(String[] args) {
        List<EmpiricalValidation.Result> allResults = new ArrayList<>();

        System.out.println("Starting empirical validation suite...\n");

        // Domain 1: Buffon's Needle
        System.out.println("► Testing Buffon's Needle...");
        allResults.addAll(testBuffonNeedle());

        // Domain 2: Portfolio VaR
        System.out.println("► Testing Portfolio VaR...");
        allResults.addAll(testPortfolioVaR());

        // Domain 3: Barrier Option
        System.out.println("► Testing Barrier Option...");
        allResults.addAll(testBarrierOption());

        // Domain 4: Rare Event
        System.out.println("► Testing Rare Event (5-sigma tail)...");
        allResults.addAll(testRareEvent());

        System.out.println("\n" + "=".repeat(80));
        EmpiricalValidation.printResults(allResults);
    }

    private static List<EmpiricalValidation.Result> testBuffonNeedle() {
        List<EmpiricalValidation.Result> results = new ArrayList<>();
        BuffonNeedle needle = new BuffonNeedle(1.0, 2.0);
        double groundTruth = Math.PI;

        // Naive MC
        List<Double> naiveReps = new ArrayList<>();
        for (int rep = 0; rep < NUM_REPLICATIONS; rep++) {
            SeededRandomSource rng = new SeededRandomSource(globalSeed++);
            double[] estimate = needle.estimate(SAMPLES_PER_REP, rng);
            naiveReps.add(estimate[0]);
        }

        results.add(EmpiricalValidation.computeResult(
            "Buffon's Needle (π estimation)",
            "Naive",
            naiveReps,
            groundTruth,
            0
        ));

        return results;
    }

    private static List<EmpiricalValidation.Result> testPortfolioVaR() {
        List<EmpiricalValidation.Result> results = new ArrayList<>();
        PortfolioVaR portfolio = new PortfolioVaR();
        double deltaNormalVaR = portfolio.getDeltaNormalVaR();

        // Naive MC
        List<Double> naiveReps = new ArrayList<>();
        for (int rep = 0; rep < NUM_REPLICATIONS; rep++) {
            SeededRandomSource rng = new SeededRandomSource(globalSeed++);
            double[] estimate = portfolio.estimateVaR(SAMPLES_PER_REP, rng);
            naiveReps.add(estimate[0]);
        }

        results.add(EmpiricalValidation.computeResult(
            "Portfolio VaR (95%)",
            "Naive MC",
            naiveReps,
            deltaNormalVaR,
            0
        ));

        // Control Variate version
        List<Double> cvReps = new ArrayList<>();
        for (int rep = 0; rep < NUM_REPLICATIONS; rep++) {
            SeededRandomSource rng = new SeededRandomSource(globalSeed++);

            List<double[]> samples = new ArrayList<>();
            for (int i = 0; i < SAMPLES_PER_REP; i++) {
                double outcome = portfolio.singleScenario(rng);
                double control = portfolio.deltaNormalControl(rng);
                samples.add(new double[]{outcome, control});
            }

            ControlVariateEstimator cv = new ControlVariateEstimator(
                samples,
                0.0  // E[delta-normal return] = 0
            );

            double sum = 0;
            for (int i = 0; i < SAMPLES_PER_REP; i++) {
                double outcome = portfolio.singleScenario(rng);
                double control = portfolio.deltaNormalControl(rng);
                sum += cv.estimateAdjusted(outcome, control);
            }

            double var95mc = -sum / SAMPLES_PER_REP;
            cvReps.add(var95mc);
        }

        results.add(EmpiricalValidation.computeResult(
            "Portfolio VaR (95%)",
            "Control Variate",
            cvReps,
            deltaNormalVaR,
            0
        ));

        return results;
    }

    private static List<EmpiricalValidation.Result> testBarrierOption() {
        List<EmpiricalValidation.Result> results = new ArrayList<>();
        BarrierOption option = new BarrierOption();

        List<Double> naiveReps = new ArrayList<>();
        for (int rep = 0; rep < NUM_REPLICATIONS; rep++) {
            SeededRandomSource rng = new SeededRandomSource(globalSeed++);
            double[] estimate = option.estimate(SAMPLES_PER_REP, rng);
            naiveReps.add(estimate[0]);
        }

        double referencePrice = 4.5;  // Approximate analytical value

        results.add(EmpiricalValidation.computeResult(
            "Barrier Option (down-and-out call)",
            "Naive MC",
            naiveReps,
            referencePrice,
            0
        ));

        return results;
    }

    private static List<EmpiricalValidation.Result> testRareEvent() {
        List<EmpiricalValidation.Result> results = new ArrayList<>();
        RareEventEstimation rareEvent = new RareEventEstimation(5.0);
        double groundTruth = rareEvent.analyticalProbability();

        // Naive
        List<Double> naiveReps = new ArrayList<>();
        for (int rep = 0; rep < NUM_REPLICATIONS; rep++) {
            SeededRandomSource rng = new SeededRandomSource(globalSeed++);
            double[] estimate = rareEvent.estimate(SAMPLES_PER_REP, rng);
            naiveReps.add(estimate[0]);
        }

        results.add(EmpiricalValidation.computeResult(
            "Rare Event (P(Z > 5σ))",
            "Naive",
            naiveReps,
            groundTruth,
            0
        ));

        // Importance Sampling
        List<Double> isReps = new ArrayList<>();
        for (int rep = 0; rep < NUM_REPLICATIONS; rep++) {
            SeededRandomSource rng = new SeededRandomSource(globalSeed++);
            double[] estimate = rareEvent.estimate(SAMPLES_PER_REP, rng);
            isReps.add(estimate[1]);
        }

        results.add(EmpiricalValidation.computeResult(
            "Rare Event (P(Z > 5σ))",
            "Importance Sampling",
            isReps,
            groundTruth,
            0
        ));

        return results;
    }
}
