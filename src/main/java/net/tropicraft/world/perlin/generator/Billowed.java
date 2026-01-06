package net.tropicraft.world.perlin.generator;

import java.util.Random;

import net.tropicraft.world.perlin.*;

public class Billowed extends NoiseModule {

    private final FishyNoise noiseGen;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final int numOctaves;
    private final double persistance;

    public Billowed(final long seed, final int nOctaves, final double p) {
        this.numOctaves = nOctaves;
        this.persistance = p;
        final Random rand = new Random(seed);
        this.offsetX = rand.nextDouble() / 2.0 + 0.01;
        this.offsetY = rand.nextDouble() / 2.0 + 0.01;
        this.offsetZ = rand.nextDouble() / 2.0 + 0.01;
        this.noiseGen = new FishyNoise(seed);
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
        double curAmplitude = this.amplitude;
        for (int n = 0; n < this.numOctaves; ++n) {
            val += Math.abs(this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY) * curAmplitude);
            i *= 2.0;
            j *= 2.0;
            curAmplitude *= this.persistance;
        }
        return val;
    }

    @Override
    public double getNoise(double i, double j, double k) {
        i *= this.frequency;
        j *= this.frequency;
        k *= this.frequency;
        double val = 0.0;
        for (int n = 0; n < this.numOctaves; ++n) {
            val += Math
                .abs(this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ) * this.amplitude);
            i *= 2.0;
            j *= 2.0;
            k *= 2.0;
        }
        return val;
    }
}
