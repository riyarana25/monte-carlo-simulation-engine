package com.riya.mcengine.random;

/**
 * Box-Muller normal distribution: transform uniform [0,1] to N(0,1).
 * Two independent uniforms → two independent normals per call pair.
 */
public class NormalDistribution implements Distribution<Double> {

    private double cachedGaussian = Double.NaN;

    @Override
    public Double sample(RandomSource randomSource) {
        if (!Double.isNaN(cachedGaussian)) {
            double result = cachedGaussian;
            cachedGaussian = Double.NaN;
            return result;
        }

        double u1 = randomSource.nextDouble();
        double u2 = randomSource.nextDouble();

        double mag = Math.sqrt(-2.0 * Math.log(u1));
        double z0 = mag * Math.cos(2.0 * Math.PI * u2);
        cachedGaussian = mag * Math.sin(2.0 * Math.PI * u2);

        return z0;
    }
}
