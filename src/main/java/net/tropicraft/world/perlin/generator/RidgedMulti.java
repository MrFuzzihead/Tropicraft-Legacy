package net.tropicraft.world.perlin.generator;

import java.util.Random;

import net.tropicraft.world.perlin.*;

public class RidgedMulti extends NoiseModule {

    private final FishyNoise noiseGen;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final int numOctaves;
    private final double[] spectralWeights;

    public RidgedMulti(final long seed, final int nOctaves) {
        this.spectralWeights = new double[32];
        this.numOctaves = nOctaves;
        final Random rand = new Random(seed);
        this.offsetX = rand.nextDouble() / 2.0 + 0.01;
        this.offsetY = rand.nextDouble() / 2.0 + 0.01;
        this.offsetZ = rand.nextDouble() / 2.0 + 0.01;
        this.noiseGen = new FishyNoise(seed);
        final double h = 1.0;
        for (int i = 0; i < 32; ++i) {
            this.spectralWeights[i] = Math.pow(this.frequency, -1.0);
            this.frequency *= 2.0;
        }
        this.frequency = 1.0;
    }

    @Override
    public double getNoise(final double i) {
        return this.getNoise(i, 0.0);
    }

    @Override
    public double getNoise(double i, double j) {
        i *= this.frequency;
        j *= this.frequency;
        double val = 0.0;
        double weight = 1.0;
        final double offset = 1.0;
        final double gain = 2.0;
        for (int n = 0; n < this.numOctaves; ++n) {
            double noise = this.absolute(this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY));
            noise = 1.0 - noise;
            noise *= noise;
            noise *= weight;
            weight = noise * 2.0;
            if (weight > 1.0) {
                weight = 1.0;
            }
            if (weight < 0.0) {
                weight = 0.0;
            }
            val += noise * this.spectralWeights[n];
            i *= 2.0;
            j *= 2.0;
        }
        return val;
    }

    @Override
    public double getNoise(double i, double j, double k) {
        i *= this.frequency;
        j *= this.frequency;
        k *= this.frequency;
        double val = 0.0;
        double weight = 1.0;
        final double offset = 1.0;
        final double gain = 2.0;
        for (int n = 0; n < this.numOctaves; ++n) {
            double noise = this.absolute(this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ));
            noise = 1.0 - noise;
            noise *= noise;
            noise *= weight;
            weight = noise * 2.0;
            if (weight > 1.0) {
                weight = 1.0;
            }
            if (weight < 0.0) {
                weight = 0.0;
            }
            val += noise * this.spectralWeights[n];
            i *= 2.0;
            j *= 2.0;
            k *= 2.0;
        }
        return val;
    }

    private double absolute(double d) {
        if (d < 0.0) {
            d = -d;
        }
        return d;
    }
}
