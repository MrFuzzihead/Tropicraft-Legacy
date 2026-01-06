package net.tropicraft.world.perlin;

import java.util.Random;

public class FishyNoise {

    int[] perm;
    public double[][] grad2d;
    public int[][] grad3d;

    public FishyNoise(final long seed) {
        this.perm = new int[512];
        this.grad2d = new double[][] { { 1.0, 0.0 }, { 0.9239, 0.3827 }, { 0.707107, 0.707107 }, { 0.3827, 0.9239 },
            { 0.0, 1.0 }, { -0.3827, 0.9239 }, { -0.707107, 0.707107 }, { -0.9239, 0.3827 }, { -1.0, 0.0 },
            { -0.9239, -0.3827 }, { -0.707107, -0.707107 }, { -0.3827, -0.9239 }, { 0.0, -1.0 }, { 0.3827, -0.9239 },
            { 0.707107, -0.707107 }, { 0.9239, -0.3827 } };
        this.grad3d = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 },
            { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 },
            { -1, 1, 0 }, { 0, -1, 1 }, { 0, -1, -1 } };
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

    private static double dot2(final double[] grad2, final double x, final double y) {
        return grad2[0] * x + grad2[1] * y;
    }

    private static double dot3(final int[] grad3, final double x, final double y, final double z) {
        return grad3[0] * x + grad3[1] * y + grad3[2] * z;
    }

    public double noise2d(double x, double y) {
        int largeX = fastFloor(x);
        int largeY = fastFloor(y);
        x -= largeX;
        y -= largeY;
        largeX &= 0xFF;
        largeY &= 0xFF;
        final double u = fade(x);
        final double v = fade(y);
        final double grad00 = dot2(this.grad2d[this.perm[largeX + this.perm[largeY]] & 0xF], x, y);
        final double grad2 = dot2(this.grad2d[this.perm[largeX + this.perm[largeY + 1]] & 0xF], x, y - 1.0);
        final double grad3 = dot2(this.grad2d[this.perm[largeX + 1 + this.perm[largeY + 1]] & 0xF], x - 1.0, y - 1.0);
        final double grad4 = dot2(this.grad2d[this.perm[largeX + 1 + this.perm[largeY]] & 0xF], x - 1.0, y);
        final double lerpX0 = lerp(grad00, grad4, u);
        final double lerpX2 = lerp(grad2, grad3, u);
        return lerp(lerpX0, lerpX2, v);
    }

    public double noise3d(double x, double y, double z) {
        int unitX = fastFloor(x);
        int unitY = fastFloor(y);
        int unitZ = fastFloor(z);
        x -= unitX;
        y -= unitY;
        z -= unitZ;
        unitX &= 0xFF;
        unitY &= 0xFF;
        unitZ &= 0xFF;
        final double u = fade(x);
        final double v = fade(y);
        final double w = fade(z);
        final double grad000 = dot3(this.grad3d[this.perm[unitX + this.perm[unitY + this.perm[unitZ]]] & 0xF], x, y, z);
        final double grad2 = dot3(
            this.grad3d[this.perm[unitX + 1 + this.perm[unitY + this.perm[unitZ]]] & 0xF],
            x - 1.0,
            y,
            z);
        final double grad3 = dot3(
            this.grad3d[this.perm[unitX + this.perm[unitY + 1 + this.perm[unitZ]]] & 0xF],
            x,
            y - 1.0,
            z);
        final double grad4 = dot3(
            this.grad3d[this.perm[unitX + 1 + this.perm[unitY + 1 + this.perm[unitZ]]] & 0xF],
            x - 1.0,
            y - 1.0,
            z);
        final double grad5 = dot3(
            this.grad3d[this.perm[unitX + this.perm[unitY + this.perm[unitZ + 1]]] & 0xF],
            x,
            y,
            z - 1.0);
        final double grad6 = dot3(
            this.grad3d[this.perm[unitX + 1 + this.perm[unitY + this.perm[unitZ + 1]]] & 0xF],
            x - 1.0,
            y,
            z - 1.0);
        final double grad7 = dot3(
            this.grad3d[this.perm[unitX + this.perm[unitY + 1 + this.perm[unitZ + 1]]] & 0xF],
            x,
            y - 1.0,
            z - 1.0);
        final double grad8 = dot3(
            this.grad3d[this.perm[unitX + 1 + this.perm[unitY + 1 + this.perm[unitZ + 1]]] & 0xF],
            x - 1.0,
            y - 1.0,
            z - 1.0);
        return lerp(
            lerp(lerp(grad000, grad2, u), lerp(grad3, grad4, u), v),
            lerp(lerp(grad5, grad6, u), lerp(grad7, grad8, u), v),
            w);
    }
}
