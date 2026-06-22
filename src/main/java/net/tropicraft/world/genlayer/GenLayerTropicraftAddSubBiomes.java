package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftAddSubBiomes extends GenLayerTropicraft {

    private int baseID;
    private int[] subBiomeIDs;

    public GenLayerTropicraftAddSubBiomes(final long seed, final GenLayerTropicraft parent, final int baseID,
        final int[] subBiomeIDs) {
        super(seed);
        this.parent = parent;
        this.baseID = baseID;
        this.subBiomeIDs = subBiomeIDs;
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int[] parentMap = this.parent.getInts(x, y, width, length);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int j = 0; j < length; ++j) {
            for (int i = 0; i < width; ++i) {
                this.initChunkSeed((long) (i + x), (long) (j + y));
                final int cur = parentMap[i + j * width];
                if (cur == this.baseID) {
                    resultMap[i + j * width] = this.subBiomeIDs[this.nextInt(this.subBiomeIDs.length)];
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
