package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;
import net.tropicraft.world.biomes.BiomeGenTropicraft;

public class GenLayerTropicraftRiverMix extends GenLayerTropicraft {

    private int lakeID;
    private GenLayerTropicraft parentBiome;
    private GenLayerTropicraft parentRiver;

    public GenLayerTropicraftRiverMix(final long seed, final GenLayerTropicraft parentBiome,
        final GenLayerTropicraft parentRiver) {
        super(seed);
        this.lakeID = BiomeGenTropicraft.tropicsLake.biomeID;
        this.parentBiome = parentBiome;
        this.parentRiver = parentRiver;
        this.setZoom(1);
    }

    public void initWorldGenSeed(final long par1) {
        this.parentBiome.initWorldGenSeed(par1);
        this.parentRiver.initWorldGenSeed(par1);
        super.initWorldGenSeed(par1);
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int[] biomeMap = this.parentBiome.getInts(x, y, width, length);
        final int[] riverMap = this.parentRiver.getInts(x, y, width, length);
        final int[] resultMap = IntCache.getIntCache(width * length);
        for (int index = 0; index < width * length; ++index) {
            if (riverMap[index] != -1 && biomeMap[index] != GenLayerTropicraftRiverMix.oceanID
                && biomeMap[index] != this.lakeID) {
                resultMap[index] = riverMap[index];
            } else {
                resultMap[index] = biomeMap[index];
            }
        }
        return resultMap;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
        this.parentBiome.setZoom(zoom);
        this.parentRiver.setZoom(zoom);
    }
}
