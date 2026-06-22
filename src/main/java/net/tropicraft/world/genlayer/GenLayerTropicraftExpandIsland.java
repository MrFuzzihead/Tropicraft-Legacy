package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftExpandIsland extends GenLayerTropicraft {

    public GenLayerTropicraftExpandIsland(final long seed, final GenLayerTropicraft parent) {
        super(seed);
        this.parent = parent;
        this.setZoom(1);
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int x2 = x - 1;
        final int y2 = y - 1;
        final int width2 = width + 2;
        final int length2 = length + 2;
        final int[] parentMap = this.parent.getInts(x2, y2, width2, length2);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                final int backXY = parentMap[i + 0 + (j + 0) * width2];
                final int forwardX = parentMap[i + 2 + (j + 0) * width2];
                final int forwardY = parentMap[i + 0 + (j + 2) * width2];
                final int forwardXY = parentMap[i + 2 + (j + 2) * width2];
                final int cur = parentMap[i + 1 + (j + 1) * width2];
                this.initChunkSeed((long) (i + x), (long) (j + y));
                if (cur == 0 && (backXY != 0 || forwardX != 0 || forwardY != 0 || forwardXY != 0)) {
                    int chance = 1;
                    int result = GenLayerTropicraftExpandIsland.landID;
                    if (backXY != 0 && this.nextInt(chance++) == 0) {
                        result = backXY;
                    }
                    if (forwardX != 0 && this.nextInt(chance++) == 0) {
                        result = forwardX;
                    }
                    if (forwardY != 0 && this.nextInt(chance++) == 0) {
                        result = forwardY;
                    }
                    if (forwardXY != 0 && this.nextInt(chance++) == 0) {
                        result = forwardXY;
                    }
                    if (this.nextInt(3) == 0) {
                        resultMap[i + j * width] = result;
                    } else {
                        resultMap[i + j * width] = GenLayerTropicraftExpandIsland.oceanID;
                    }
                } else {
                    resultMap[i + j * width] = cur;
                }
            }
        }
        return resultMap;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
        this.parent.setZoom(zoom);
    }
}
