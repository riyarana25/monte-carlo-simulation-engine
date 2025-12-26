package com.riya.mcengine.core;

/**
 * Represents a Monte Carlo simulation definition.
 */
public interface Simulation<S extends State> {

    /**
     * @return the initial state of the simulation
     */
    S initialState();

    /**
     * @return the transition logic for the simulation
     */
    Transition<S> transition();
}