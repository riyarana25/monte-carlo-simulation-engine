package com.riya.mcengine.random;

import java.util.Random;

/**
 * Deterministic RandomSource backed by java.util.Random.
 * Supports stream-based partitioning for reproducible parallel simulation.
 */
public class SeededRandomSource implements RandomSource {

    private final Random random;
    private final long baseSeed;
    private final int streamId;

    public SeededRandomSource(long seed) {
        this(seed, 0);
    }

    /**
     * Create RNG for a specific stream within a partition.
     * Stream ID is hashed with base seed to ensure independence.
     */
    public SeededRandomSource(long seed, int streamId) {
        this.baseSeed = seed;
        this.streamId = streamId;
        long streamSeed = hashStreamSeed(seed, streamId);
        this.random = new Random(streamSeed);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * Deterministic hash: seed, stream_id → unique seed for this stream.
     * Uses aggressive bit mixing to ensure independent streams.
     * Based on MurmurHash-inspired mixing.
     */
    private static long hashStreamSeed(long baseSeed, int streamId) {
        long h = baseSeed;
        h ^= (long) streamId;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        return h;
    }

    public long getBaseSeed() {
        return baseSeed;
    }

    public int getStreamId() {
        return streamId;
    }
}
