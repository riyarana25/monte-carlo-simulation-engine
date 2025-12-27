package com.riya.mcengine.random;

/**
 * Generic probability distribution.
 */
public interface Distribution<T> {

    /**
     * Draws a sample using the given RandomSource.
     */
    T sample(RandomSource randomSource);
}
