package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftIsland extends GenLayerTropicraft {

    private static final int CHANCE_OF_LAND = 10;

    public GenLayerTropicraftIsland(final long seed) {
        super(seed);
        this.setZoom(1);
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                this.initChunkSeed((long) (x + i), (long) (y + j));
                resultMap[i + j * width] = ((this.nextInt(10) == 0) ? GenLayerTropicraftIsland.landID
                    : GenLayerTropicraftIsland.oceanID);
            }
        }
        if (x > -width && x <= 0 && y > -length && y <= 0) {
            resultMap[-x + -y * width] = GenLayerTropicraftIsland.landID;
        }
        return resultMap;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
    }
}
