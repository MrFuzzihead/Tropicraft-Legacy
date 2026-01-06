package net.tropicraft.world.perlin;

import java.util.Random;

public class SimplexNoise {

    int[] perm;
    public int[][] grad2d;

    public SimplexNoise(final long seed) {
        this.perm = new int[512];
        this.grad2d = new int[][] { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 0 } };
        final Random rand = new Random(seed);
        for (int i = 0; i < 256; ++i) {
            this.perm[i] = i;
        }
        for (int i = 0; i < 256; ++i) {
            final int j = rand.nextInt(256);
            this.perm[i] ^= this.perm[j];
            this.perm[j] ^= this.perm[i];
            this.perm[i] ^= this.perm[j];
        }
        for (int i = 0; i < 256; ++i) {
            this.perm[i + 256] = this.perm[i];
        }
    }

    private static double lerp(final double x, final double y, final double n) {
        return x + n * (y - x);
    }

    private static int fastFloor(final double x) {
        return (x > 0.0) ? ((int) x) : ((int) x - 1);
    }

    private static double fade(final double n) {
        return n * n * n * (n * (n * 6.0 - 15.0) + 10.0);
    }

    private static double dot2(final int[] grad2, final double x, final double y) {
        return grad2[0] * x + grad2[1] * y;
    }
}
