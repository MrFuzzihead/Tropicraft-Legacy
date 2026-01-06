package net.tropicraft.world;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;
import net.tropicraft.config.*;
import net.tropicraft.registry.*;
import net.tropicraft.world.biomes.*;
import net.tropicraft.world.worldgen.*;

import cpw.mods.fml.common.*;

public class TCWorldGenerator implements IWorldGenerator {

    public void generate(final Random random, final int chunkX, final int chunkZ, final World world,
        final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
        this.generateSurface(world, random, chunkX, chunkZ);
    }

    public void generateSurface(final World world, final Random random, int chunkX, int chunkZ) {
        final int cx = chunkX;
        final int cz = chunkZ;
        if (ConfigGenRates.genTropicraftInOverworld) {
            chunkX *= 16;
            chunkZ *= 16;
            if (world.provider.dimensionId == 0) {
                final WorldType terrainType = world.provider.terrainType;
                if (terrainType != WorldType.FLAT) {
                    final int k = chunkX + random.nextInt(16) + 8;
                    int l = random.nextInt(62) + 64;
                    final int i1 = chunkZ + random.nextInt(16) + 8;
                    if (ConfigGenRates.genTropicraftFlowersInOverworld) {
                        for (int j3 = 0; j3 < 10; ++j3) {
                            l = random.nextInt(62) + 64;
                            new WorldGenTropicraftFlowers(
                                world,
                                random,
                                TCBlockRegistry.flowers,
                                BiomeGenTropicraft.DEFAULT_FLOWER_META).generate(world, random, k, l, i1);
                        }
                    }
                    if (ConfigGenRates.genTropicraftEIHInOverworld && random.nextInt(27) == 0) {
                        l = random.nextInt(62) + 64;
                        new WorldGenEIH(world, random).generate(world, random, k, l, i1);
                    }
                    if (ConfigGenRates.genPalmsInOverworld && random.nextInt(12) == 0) {
                        final BiomeGenBase biome = world.getWorldChunkManager()
                            .getBiomeGenAt(cx, cz);
                        if ((!ConfigGenRates.genOverworldPalmsInBeachOnly || biome == BiomeGenBase.beach)
                            && (ConfigGenRates.palmChanceOfGenInOverworld < 0
                                || random.nextFloat() < ConfigGenRates.palmChanceOfGenInOverworld / 100.0f)) {
                            for (int j4 = 0; j4 < ConfigGenRates.palmPopulationFactorInOverworld; ++j4) {
                                l = random.nextInt(62) + 64;
                                if (random.nextInt(5) == 0) {
                                    new WorldGenTropicraftLargePalmTrees().generate(world, random, k, l, i1);
                                } else if (random.nextInt(5) < 3) {
                                    new WorldGenTropicraftCurvedPalm(world, random).generate(world, random, k, l, i1);
                                } else {
                                    new WorldGenTropicraftNormalPalms().generate(world, random, k, l, i1);
                                }
                            }
                        }
                    }
                    if (ConfigGenRates.genPineapplesInOverworld && random.nextInt(8) == 0) {
                        l = random.nextInt(62) + 64;
                        new WorldGenTallFlower(world, random, TCBlockRegistry.pineapple, 7, 8)
                            .generate(world, random, k, l, i1);
                    }
                    if (ConfigGenRates.genBambooInOverworld && random.nextInt(3) == 0) {
                        l = random.nextInt(62) + 64;
                        new WorldGenBamboo(world, random).generate(world, random, k, l, i1);
                    }
                }
            }
        }
    }
}
