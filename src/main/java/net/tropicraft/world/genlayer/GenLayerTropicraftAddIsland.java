package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;
import net.tropicraft.world.biomes.BiomeGenTropicraft;

public class GenLayerTropicraftAddIsland extends GenLayerTropicraft {

    private int oceanID;
    private int landID;
    private int chance;

    public GenLayerTropicraftAddIsland(final long seed, final GenLayerTropicraft parent, final int chance,
        final int landID) {
        super(seed);
        this.oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
        this.landID = BiomeGenTropicraft.tropics.biomeID;
        this.parent = parent;
        this.chance = chance;
        this.landID = landID;
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int x2 = x - 1;
        final int y2 = y - 1;
        final int width2 = width + 2;
        final int length2 = length + 2;
        final int[] parentMap = this.parent.getInts(x2, y2, width2, length2);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int i2 = 0; i2 < length; ++i2) {
            for (int j2 = 0; j2 < width; ++j2) {
                final int backX = parentMap[j2 + 0 + (i2 + 1) * width2];
                final int forwardX = parentMap[j2 + 2 + (i2 + 1) * width2];
                final int backY = parentMap[j2 + 1 + (i2 + 0) * width2];
                final int forwardY = parentMap[j2 + 1 + (i2 + 2) * width2];
                final int cur = parentMap[j2 + 1 + (i2 + 1) * width2];
                this.initChunkSeed((long) (j2 + x), (long) (i2 + y));
                if (backX != this.landID && forwardX != this.landID
                    && backY != this.landID
                    && forwardY != this.landID
                    && cur != this.landID
                    && this.nextInt(this.chance) == 0) {
                    resultMap[j2 + i2 * width] = this.landID;
                } else {
                    resultMap[j2 + i2 * width] = cur;
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
