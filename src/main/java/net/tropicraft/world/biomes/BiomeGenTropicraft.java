package net.tropicraft.world.biomes;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.tropicraft.config.*;
import net.tropicraft.entity.hostile.*;
import net.tropicraft.entity.passive.*;
import net.tropicraft.entity.underdasea.*;
import net.tropicraft.registry.*;
import net.tropicraft.world.worldgen.*;

public class BiomeGenTropicraft extends BiomeGenBase {

    public static final int[] DEFAULT_FLOWER_META;
    public static BiomeGenBase tropicsOcean;
    public static BiomeGenBase tropics;
    public static BiomeGenBase rainforestPlains;
    public static BiomeGenBase rainforestHills;
    public static BiomeGenBase rainforestMountains;
    public static BiomeGenBase islandMountains;
    public static BiomeGenBase tropicsRiver;
    public static BiomeGenBase tropicsBeach;
    public static BiomeGenBase tropicsLake;
    public Block sandBlock;
    public short sandMetadata;
    public static boolean DISABLEDECORATION;

    public BiomeGenTropicraft(final int biomeID) {
        super(biomeID);
        this.sandBlock = (Block) Blocks.sand;
        this.sandMetadata = 0;
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        if (biomeID == ConfigBiomes.rainforestMountainsID || biomeID == ConfigBiomes.rainforestHillsID
            || biomeID == ConfigBiomes.rainforestPlainsID) {
            this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTreeFrogBlue.class, 25, 1, 2));
            this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTreeFrogGreen.class, 25, 1, 2));
            this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTreeFrogRed.class, 25, 1, 2));
            this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTreeFrogYellow.class, 25, 1, 2));
        }
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(VMonkey.class, 20, 1, 3));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityIguana.class, 20, 1, 1));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTropiCreeper.class, 2, 1, 2));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEIH.class, 10, 1, 1));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTropiSkeleton.class, 25, 1, 8));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityAshenHunter.class, 2, 3, 12));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(Failgull.class, 30, 5, 15));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityTropicalFish.class, 10, 1, 12));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityEagleRay.class, 6, 1, 3));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySeaTurtle.class, 6, 1, 3));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySeahorse.class, 6, 1, 3));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityMarlin.class, 10, 1, 3));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityManOWar.class, 4, 1, 2));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityStarfish.class, 4, 1, 4));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySeaUrchin.class, 4, 1, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(SpiderAdult.class, 50, 1, 3));
    }

    public void decorate(final World world, final Random rand, final int x, final int z) {
        final BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        int i;
        int k;

        if (biome == BiomeGenTropicraft.tropicsOcean && rand.nextInt(5) == 0) {
            new WorldGenCoral().generate(world, rand, x + 6 + rand.nextInt(4), 64, z + 6 + rand.nextInt(4));
        }

        i = this.randCoord(rand, x, 16);
        k = this.randCoord(rand, z, 16);

        if (rand.nextInt(2) == 0) {
            new WorldGenBamboo(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(50) == 0) {
            new WorldGenEIH(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(3) == 0) {
            int terrainHeight = this.getTerrainHeightAt(world, i, k);
            if (terrainHeight > 0 && world.isAirBlock(i, terrainHeight, k)) {
                new WorldGenTallFlower(world, rand, TCBlockRegistry.tallFlowers, 0, 1).generate(i, terrainHeight, k);
            }
        }

        if (rand.nextInt(3) == 0) {
            int terrainHeight = this.getTerrainHeightAt(world, i, k);
            if (terrainHeight > 0 && world.isAirBlock(i, terrainHeight, k)) {
                new WorldGenTallFlower(world, rand, TCBlockRegistry.pineapple, 7, 8).generate(i, terrainHeight, k);
            }
        }

        int terrainHeight = this.getTerrainHeightAt(world, i, k);
        if (terrainHeight > 0 && world.isAirBlock(i, terrainHeight, k)) {
            new WorldGenTropicraftFlowers(world, rand, TCBlockRegistry.flowers, BiomeGenTropicraft.DEFAULT_FLOWER_META)
                .generate(i, terrainHeight, k);
        }
        if (rand.nextInt(300) == 0) {
            new WorldGenSunkenShip(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(3) == 0) {
            new WorldGenTropicraftCurvedPalm(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(3) == 0) {
            new WorldGenTropicraftLargePalmTrees(false)
                .generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(3) == 0) {
            new WorldGenTropicraftNormalPalms(false).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(4) == 0) {
            terrainHeight = this.getTerrainHeightAt(world, i, k);
            if (terrainHeight > 0 && world.isAirBlock(i, terrainHeight, k)) {
                if (world.getBlock(i, terrainHeight - 1, k) == Blocks.grass) {
                    world.setBlock(i, terrainHeight, k, Blocks.tallgrass, 1, 2);
                }
            }
        }

        for (int a = 0; a < 25; ++a) {
            new WorldGenWaterfall(world, rand)
                .generate(this.randCoord(rand, x, 16), 63 + rand.nextInt(193), this.randCoord(rand, z, 16));
        }
    }

    public int getTerrainHeightAt(final World world, final int x, final int z) {
        int height = world.getHeightValue(x, z);
        for (int y = height + 1; y > 0; --y) {
            Block id = world.getBlock(x, y, z);
            if (id == Blocks.grass || id == Blocks.dirt || id == Blocks.sand) {
                return y + 1;
            }
            if (id.getMaterial()
                .isSolid()) {
                break;
            }
        }
        return height;
    }

    public final int randCoord(final Random rand, final int base, final int variance) {
        return base + rand.nextInt(variance);
    }

    static {
        DEFAULT_FLOWER_META = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
        BiomeGenTropicraft.tropicsOcean = new BiomeGenTropicraft(ConfigBiomes.tropicsOceanID)
            .setHeight(new BiomeGenBase.Height(-1.0f, 0.4f))
            .setTemperatureRainfall(1.5f, 1.25f)
            .setBiomeName("Tropical Ocean");
        BiomeGenTropicraft.tropics = new BiomeGenTropics(ConfigBiomes.tropicsID)
            .setHeight(new BiomeGenBase.Height(0.15f, 0.15f))
            .setTemperatureRainfall(2.0f, 1.5f)
            .setBiomeName("Tropics");
        BiomeGenTropicraft.rainforestPlains = new BiomeGenRainforest(ConfigBiomes.rainforestPlainsID)
            .setHeight(new BiomeGenBase.Height(0.25f, 0.1f))
            .setColor(1148975)
            .setTemperatureRainfall(1.5f, 2.0f)
            .setBiomeName("Rainforest Plains");
        BiomeGenTropicraft.rainforestHills = new BiomeGenRainforest(ConfigBiomes.rainforestHillsID)
            .setHeight(new BiomeGenBase.Height(0.45f, 0.425f))
            .setColor(1148975)
            .setTemperatureRainfall(1.5f, 2.0f)
            .setBiomeName("Rainforest Hills");
        BiomeGenTropicraft.rainforestMountains = new BiomeGenRainforest(ConfigBiomes.rainforestMountainsID)
            .setHeight(new BiomeGenBase.Height(1.0f, 1.2f))
            .setTemperatureRainfall(1.5f, 2.0f)
            .setBiomeName("Rainforest Mountains");
        BiomeGenTropicraft.islandMountains = new BiomeGenRainforest(ConfigBiomes.islandMountainsID)
            .setHeight(new BiomeGenBase.Height(0.1f, 2.5f))
            .setTemperatureRainfall(1.5f, 2.0f)
            .setBiomeName("Extreme Rainforest Mountains");
        BiomeGenTropicraft.tropicsRiver = new BiomeGenTropicsRiver(ConfigBiomes.tropicsRiverID)
            .setHeight(new BiomeGenBase.Height(-0.7f, 0.05f))
            .setTemperatureRainfall(1.5f, 1.25f)
            .setBiomeName("Tropical River");
        BiomeGenTropicraft.tropicsBeach = new BiomeGenTropicsBeach(ConfigBiomes.tropicsBeachID)
            .setHeight(new BiomeGenBase.Height(-0.1f, 0.1f))
            .setTemperatureRainfall(1.5f, 1.25f)
            .setBiomeName("Tropical Beach");
        BiomeGenTropicraft.tropicsLake = new BiomeGenTropicsOcean(ConfigBiomes.tropicsLakeID)
            .setHeight(new BiomeGenBase.Height(-0.6f, 0.1f))
            .setTemperatureRainfall(1.5f, 1.5f)
            .setBiomeName("Tropical Lake");
        BiomeGenTropicraft.DISABLEDECORATION = false;
    }
}
