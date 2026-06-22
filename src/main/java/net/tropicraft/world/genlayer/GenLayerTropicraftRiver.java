package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;
import net.tropicraft.world.biomes.BiomeGenTropicraft;

public class GenLayerTropicraftRiver extends GenLayerTropicraft {

    private int riverID;

    public GenLayerTropicraftRiver(final long seed, final GenLayerTropicraft parent) {
        super(seed);
        this.riverID = BiomeGenTropicraft.tropicsRiver.biomeID;
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
                final int backX = parentMap[i + 0 + (j + 1) * width2];
                final int forwardX = parentMap[i + 2 + (j + 1) * width2];
                final int backY = parentMap[i + 1 + (j + 0) * width2];
                final int forwardY = parentMap[i + 1 + (j + 2) * width2];
                final int cur = parentMap[i + 1 + (j + 1) * width2];
                if (cur != backX || cur != forwardX || cur != backY || cur != forwardY) {
                    resultMap[i + j * width] = this.riverID;
                } else {
                    resultMap[i + j * width] = -1;
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
