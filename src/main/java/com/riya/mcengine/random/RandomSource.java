package com.riya.mcengine.random;

/**
 * Abstraction over randomness to allow deterministic replay
 * and testable simulations.
 */
public interface RandomSource {

    /**
     * @return random double in range [0.0, 1.0)
     */
    double nextDouble();

    /**
     * @param bound upper bound (exclusive)
     * @return random integer in range [0, bound)
     */
    int nextInt(int bound);
}
