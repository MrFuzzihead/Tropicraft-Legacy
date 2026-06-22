package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftRiverInit extends GenLayerTropicraft {

    private static final int AREAS = 4;

    public GenLayerTropicraftRiverInit(final long seed) {
        super(seed);
        this.setZoom(1);
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                this.initChunkSeed((long) (i + x), (long) (j + y));
                resultMap[i + j * width] = this.nextInt(4) + 1;
            }
        }
        return resultMap;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
    }
}
