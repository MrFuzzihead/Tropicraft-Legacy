package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftSmooth extends GenLayerTropicraft {

    public GenLayerTropicraftSmooth(final long seed, final GenLayerTropicraft parent) {
        super(seed);
        super.parent = parent;
        this.setZoom(1);
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int x2 = x - 1;
        final int y2 = y - 1;
        final int width2 = width + 2;
        final int length2 = length + 2;
        final int[] biomeMap = this.parent.getInts(x2, y2, width2, length2);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                final int backX = biomeMap[i + 0 + (j + 1) * width2];
                final int forwardX = biomeMap[i + 2 + (j + 1) * width2];
                final int backY = biomeMap[i + 1 + (j + 0) * width2];
                final int forwardY = biomeMap[i + 1 + (j + 2) * width2];
                int cur = biomeMap[i + 1 + (j + 1) * width2];
                if (backX == forwardX && backY == forwardY) {
                    this.initChunkSeed((long) (i + x), (long) (j + y));
                    if (this.nextInt(2) == 0) {
                        cur = backX;
                    } else {
                        cur = backY;
                    }
                } else {
                    if (backX == forwardX) {
                        cur = backX;
                    }
                    if (backY == forwardY) {
                        cur = backY;
                    }
                }
                resultMap[i + j * width] = cur;
            }
        }
        return resultMap;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
        this.parent.setZoom(zoom);
    }
}
