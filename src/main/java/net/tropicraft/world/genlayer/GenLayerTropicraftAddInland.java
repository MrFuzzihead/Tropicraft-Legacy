package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftAddInland extends GenLayerTropicraft {

    private int inlandID;
    private int chance;

    public GenLayerTropicraftAddInland(final long seed, final GenLayerTropicraft parent, final int chance,
        final int inlandID) {
        super(seed);
        this.parent = parent;
        this.chance = chance;
        this.inlandID = inlandID;
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int x2 = x - 1;
        final int y2 = y - 1;
        final int width2 = width + 2;
        final int height2 = length + 2;
        final int[] parentMap = this.parent.getInts(x2, y2, width2, height2);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                final int backX = parentMap[i + 0 + (j + 1) * width2];
                final int forwardX = parentMap[i + 2 + (j + 1) * width2];
                final int backY = parentMap[i + 1 + (j + 0) * width2];
                final int forwardY = parentMap[i + 1 + (j + 2) * width2];
                final int cur = parentMap[i + 1 + (j + 1) * width2];
                this.initChunkSeed((long) (i + x), (long) (j + y));
                if (backX == GenLayerTropicraftAddInland.landID && forwardX == GenLayerTropicraftAddInland.landID
                    && backY == GenLayerTropicraftAddInland.landID
                    && forwardY == GenLayerTropicraftAddInland.landID
                    && cur == GenLayerTropicraftAddInland.landID
                    && this.nextInt(this.chance) == 0) {
                    resultMap[i + j * width] = this.inlandID;
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
