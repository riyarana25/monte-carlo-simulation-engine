package com.riya.mcengine.core;

import com.riya.mcengine.random.RandomSource;

/**
 * Defines how a simulation transitions from one state to the next.
 */
public interface Transition<S extends State> {

    /**
     * Computes the next state from the current state using randomness.
     *
     * @param currentState current simulation state
     * @param randomSource controlled source of randomness
     * @return next simulation state
     */
    S next(S currentState, RandomSource randomSource);
}