package net.tropicraft.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.tropicraft.config.ConfigGenRates;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.worldgen.WorldGenBamboo;
import net.tropicraft.world.worldgen.WorldGenEIH;
import net.tropicraft.world.worldgen.WorldGenTallFlower;
import net.tropicraft.world.worldgen.WorldGenTropicraftCurvedPalm;
import net.tropicraft.world.worldgen.WorldGenTropicraftFlowers;
import net.tropicraft.world.worldgen.WorldGenTropicraftLargePalmTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftNormalPalms;

import cpw.mods.fml.common.IWorldGenerator;

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
                    if (ConfigGenRates.genTropicraftEIHInOverworld && ConfigGenRates.eihChanceInOverworld > 0
                        && random.nextInt(ConfigGenRates.eihChanceInOverworld) == 0) {
                        generateOverworldEIH(world, random, chunkX, chunkZ);
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

    /**
     * Places an Eastern Island Head on solid overworld ground.
     * <p>
     * This used to hand the generator a random Y between 64 and 125, but {@link WorldGenEIH} only builds when the
     * block directly below the origin is dirt or grass and the origin itself is air. Only the single Y sitting on
     * top of the surface of that exact column satisfies that, so the head spawned about once in every few thousand
     * chunks and nobody ever saw one. Snapping to the terrain height, the way
     * {@link net.tropicraft.world.biomes.BiomeGenTropicraft} does for the tropics, is what makes it work again.
     * See <a href="https://github.com/MrFuzzihead/Tropicraft-Legacy/issues/41">issue #41</a>.
     */
    private static void generateOverworldEIH(final World world, final Random random, final int chunkMinX,
        final int chunkMinZ) {
        final WorldGenEIH generator = new WorldGenEIH(world, random);
        // The head occupies originX - 4 .. originX + 1 and originZ - 1 .. originZ + 4. Keeping the origin inside
        // those margins means every block lands in the chunk that is currently being populated instead of dragging
        // a neighbouring chunk into existence (the cascading worldgen lag from issue #36).
        final int originX = chunkMinX + 4 + random.nextInt(11);
        final int originZ = chunkMinZ + 1 + random.nextInt(11);
        final int originY = generator.getTerrainHeightAt(originX, originZ);
        if (originY > 0) {
            generator.generate(world, random, originX, originY, originZ);
        }
    }
}
