package com.riya.mcengine.executor;

/**
 * Functional interface for a function that takes 3 parameters and returns a result.
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
