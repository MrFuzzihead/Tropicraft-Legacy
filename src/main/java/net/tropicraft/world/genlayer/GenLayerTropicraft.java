package net.tropicraft.world.genlayer;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.tropicraft.world.biomes.BiomeGenTropicraft;

public abstract class GenLayerTropicraft extends GenLayer {

    protected int zoom;
    protected GenLayerTropicraft parent;
    protected static int oceanID;
    protected static int landID;
    private static int[] landBiomeIDs;
    private static int[] rainforestBiomeIDs;

    public GenLayerTropicraft(final long par1) {
        super(par1);
    }

    public static GenLayer[] initializeAllBiomeGenerators(final long par0, final WorldType par2WorldType) {
        final GenLayerTropicraft layerIsland = new GenLayerTropicraftIsland(1L);
        final GenLayerTropicraft layerFuzzyZoom = new GenLayerTropicraftFuzzyZoom(2000L, layerIsland);
        GenLayerTropicraft layerExpand = new GenLayerTropicraftExpandIsland(2L, layerFuzzyZoom);
        GenLayerTropicraft layerAddIsland = new GenLayerTropicraftAddIsland(
            3L,
            layerExpand,
            10,
            GenLayerTropicraft.landID);
        GenLayerTropicraft layerZoom = new GenLayerTropicraftZoom(2000L, layerAddIsland);
        layerExpand = new GenLayerTropicraftExpandIsland(6L, layerZoom);
        final GenLayerTropicraft layerLake = new GenLayerTropicraftAddInland(
            9L,
            layerExpand,
            20,
            GenLayerTropicraft.landID);
        layerAddIsland = new GenLayerTropicraftAddIsland(5L, layerLake, 8, GenLayerTropicraft.landID);
        layerAddIsland = new GenLayerTropicraftAddIsland(6L, layerLake, 13, BiomeGenTropicraft.islandMountains.biomeID);
        layerZoom = new GenLayerTropicraftZoom(2001L, layerAddIsland);
        layerExpand = new GenLayerTropicraftExpandIsland(7L, layerZoom);
        layerAddIsland = new GenLayerTropicraftAddIsland(8L, layerExpand, 9, GenLayerTropicraft.landID);
        layerExpand = new GenLayerTropicraftExpandIsland(10L, layerAddIsland);
        final GenLayerTropicraft genLayerBiomes = new GenLayerTropicraftBiomes(
            15L,
            layerExpand,
            GenLayerTropicraft.landBiomeIDs);
        final GenLayerTropicraft genLayerHills = new GenLayerTropicraftAddSubBiomes(
            16L,
            genLayerBiomes,
            BiomeGenTropicraft.rainforestHills.biomeID,
            GenLayerTropicraft.rainforestBiomeIDs);
        layerZoom = new GenLayerTropicraftZoom(2002L, genLayerHills);
        layerExpand = new GenLayerTropicraftExpandIsland(10L, layerZoom);
        for (int i = 0; i < 4; ++i) {
            layerExpand = new GenLayerTropicraftExpandIsland(10L, layerExpand);
        }
        final GenLayerTropicraft layerRiverInit = new GenLayerTropicraftRiverInit(12L);
        final GenLayerTropicraft layerRiverMag = GenLayerTropicraftZoom.magnify(2007L, layerRiverInit, 5);
        final GenLayerTropicraft layerRiver = new GenLayerTropicraftRiver(13L, layerRiverMag);
        final GenLayerTropicraft layerRiverSmooth = new GenLayerTropicraftSmooth(2008L, layerRiver);
        final GenLayerTropicraft layerMagnify = GenLayerTropicraftZoom.magnify(2007L, layerExpand, 3);
        final GenLayerTropicraft layerBeach = new GenLayerTropicraftBeach(20L, layerMagnify);
        final GenLayerTropicraft layerBiomesSmooth = new GenLayerTropicraftSmooth(17L, layerBeach);
        final GenLayerTropicraft layerRiverMix = new GenLayerTropicraftRiverMix(
            5L,
            layerBiomesSmooth,
            layerRiverSmooth);
        final GenLayerTropicraft layerVoronoi = new GenLayerTropiVoronoiZoom(
            10L,
            layerRiverMix,
            GenLayerTropiVoronoiZoom.Mode.MANHATTAN);
        layerRiverMix.initWorldGenSeed(par0);
        layerVoronoi.initWorldGenSeed(par0);
        return new GenLayer[] { layerRiverMix, layerVoronoi };
    }

    public abstract void setZoom(final int p0);

    static {
        GenLayerTropicraft.oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
        GenLayerTropicraft.landID = BiomeGenTropicraft.tropics.biomeID;
        GenLayerTropicraft.landBiomeIDs = new int[] { BiomeGenTropicraft.tropics.biomeID,
            BiomeGenTropicraft.rainforestPlains.biomeID };
        GenLayerTropicraft.rainforestBiomeIDs = new int[] { BiomeGenTropicraft.rainforestPlains.biomeID,
            BiomeGenTropicraft.rainforestHills.biomeID, BiomeGenTropicraft.rainforestMountains.biomeID };
    }
}
