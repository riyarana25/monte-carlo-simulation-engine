package com.riya.mcengine.random;

import java.util.Random;

/**
 * Deterministic RandomSource backed by java.util.Random.
 */
public class SeededRandomSource implements RandomSource {

    private final Random random;

    public SeededRandomSource(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
