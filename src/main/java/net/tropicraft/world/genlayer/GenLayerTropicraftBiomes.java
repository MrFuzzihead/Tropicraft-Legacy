package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftBiomes extends GenLayerTropicraft {

    private int[] biomeIDs;

    public GenLayerTropicraftBiomes(final long seed, final GenLayerTropicraft parent, final int[] biomeIDs) {
        super(seed);
        this.parent = parent;
        this.biomeIDs = biomeIDs;
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int[] parentMap = this.parent.getInts(x, y, width, length);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                this.initChunkSeed((long) (i + x), (long) (j + y));
                final int cur = parentMap[i + j * width];
                if (cur == GenLayerTropicraftBiomes.landID) {
                    resultMap[i + j * width] = this.biomeIDs[this.nextInt(this.biomeIDs.length)];
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
