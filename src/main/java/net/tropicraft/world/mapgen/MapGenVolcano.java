package net.tropicraft.world.mapgen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.tropicraft.registry.*;
import net.tropicraft.world.biomes.*;
import net.tropicraft.world.perlin.*;
import net.tropicraft.world.perlin.generator.*;

public class MapGenVolcano {

    protected HashMap<Long, ChunkCoordinates> coordMap;
    public static List<BiomeGenBase> volcanoSpawnBiomesLand;
    public static List<BiomeGenBase> volcanoSpawnBiomesOcean;
    private World worldObj;
    private boolean useArrays;
    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Z = 16;
    private static final int CHUNK_SIZE_Y = 256;
    private static final int MAX_RADIUS = 65;
    private static final int MIN_RADIUS = 45;
    private static final int LAND_STEEPNESS_MOD = 4;
    private static final int OCEAN_STEEPNESS_MOD = 8;
    private static final int CALDERA_CUTOFF = 110;
    private static final int VOLCANO_TOP = 103;
    private static final int VOLCANO_CRUST = 100;
    private static final int LAVA_LEVEL = 95;
    private static final int CRUST_HOLE_CHANCE = 15;
    private static final Block VOLCANO_BLOCK;
    private static final Block LAVA_BLOCK;

    public MapGenVolcano(final World worldObj, final boolean useArrays) {
        this.coordMap = new HashMap<Long, ChunkCoordinates>();
        this.worldObj = worldObj;
        this.useArrays = useArrays;
    }

    public Block[] generate(int i, int k, final Block[] blocks, final byte[] metas) {
        final ChunkCoordinates volcanoCoords = this.getVolcanoNear(this.worldObj, i, k);
        if (volcanoCoords == null) {
            return blocks;
        }
        final int[] heightmap = new int[256];
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < 127; ++y) {
                    final Block blockID = this.getBlock(x, y, z, blocks);
                    if (blockID == Blocks.air || blockID == TCBlockRegistry.tropicsWater) {
                        heightmap[x * 16 + z] = y;
                        break;
                    }
                    if (y > 75) {
                        heightmap[x * 16 + z] = y;
                        break;
                    }
                }
            }
        }
        i *= 16;
        k *= 16;
        final int volcCenterX = volcanoCoords.posX;
        final int volcCenterZ = volcanoCoords.posZ;
        final int steepnessMod = (volcanoCoords.posY == 1) ? 4 : 8;
        final long seed = volcCenterX * 341873128712L + volcCenterZ * 132897987541L
            + this.worldObj.getWorldInfo()
                .getSeed()
            + 4291726L;
        final Random rand = new Random(seed);
        final int radiusX = rand.nextInt(20) + 45;
        final int radiusZ = rand.nextInt(20) + 45;
        final NoiseModule volcNoise = new Billowed(seed, 1, 1.0);
        volcNoise.amplitude = 0.45;
        for (int x2 = 0; x2 < 16; ++x2) {
            for (int z2 = 0; z2 < 16; ++z2) {
                final float relativeX = (float) (x2 + i - volcCenterX);
                final float relativeZ = (float) (z2 + k - volcCenterZ);
                final float distanceSquared = relativeX / radiusX * (relativeX / radiusX)
                    + relativeZ / radiusZ * (relativeZ / radiusZ);
                final float perlin = (float) volcNoise.getNoise(relativeX * 0.05 + 1.0E-4, relativeZ * 0.05 + 1.0E-4)
                    + 1.0f;
                final double volcanoHeight = steepnessMod / distanceSquared * perlin - steepnessMod - 2.0f;
                final int groundHeight = heightmap[x2 * 16 + z2];
                if (distanceSquared < 1.0f) {
                    for (int y2 = 256; y2 > 0; --y2) {
                        if (volcanoHeight + groundHeight < 110.0) {
                            if (volcanoHeight + groundHeight <= 103.0) {
                                if (y2 <= volcanoHeight + groundHeight && y2 >= groundHeight) {
                                    this.placeBlock(x2, y2, z2, MapGenVolcano.VOLCANO_BLOCK, blocks);
                                }
                            } else if (y2 <= 103) {
                                this.placeBlock(x2, y2, z2, MapGenVolcano.VOLCANO_BLOCK, blocks);
                            }
                        } else {
                            if (y2 == 100 && rand.nextInt(15) != 0) {
                                this.placeBlock(x2, y2, z2, MapGenVolcano.VOLCANO_BLOCK, blocks);
                            }
                            if (y2 <= 95) {
                                this.placeBlock(x2, y2, z2, MapGenVolcano.LAVA_BLOCK, blocks);
                            }
                        }
                    }
                }
            }
        }
        return blocks;
    }

    public void placeBlock(final int x, final int y, final int z, final Block block, final Block[] blocks) {
        blocks[x * 256 * 16 | z * 256 | y] = block;
    }

    public Block getBlock(final int x, final int y, final int z, final Block[] blocks) {
        return blocks[x * 256 * 16 | z * 256 | y];
    }

    protected int canGenVolcanoAtCoords(final World worldObj, int i, int j) {
        final byte numChunks = 32;
        final byte offsetChunks = 8;
        final int oldi = i;
        final int oldj = j;
        if (i < 0) {
            i -= numChunks - 1;
        }
        if (j < 0) {
            j -= numChunks - 1;
        }
        int randX = i / numChunks;
        int randZ = j / numChunks;
        final long seed = randX * 341873128712L + randZ * 132897987541L
            + worldObj.getWorldInfo()
                .getSeed()
            + 4291726L;
        final Random rand = new Random(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);
        if (oldi == randX && oldj == randZ) {
            if (worldObj.getWorldChunkManager()
                .areBiomesViable(oldi * 16 + 8, oldj * 16 + 8, 0, MapGenVolcano.volcanoSpawnBiomesLand)) {
                return 1;
            }
            if (worldObj.getWorldChunkManager()
                .areBiomesViable(oldi * 16 + 8, oldj * 16 + 8, 0, MapGenVolcano.volcanoSpawnBiomesOcean)) {
                return 2;
            }
        }
        return 0;
    }

    public ChunkCoordinates getVolcanoNear(final World worldObj, final int i, final int j) {
        for (int range = 4, x = i - range; x <= i + range; ++x) {
            for (int z = j - range; z <= j + range; ++z) {
                final int biome = this.canGenVolcanoAtCoords(worldObj, x, z);
                if (biome != 0) {
                    return new ChunkCoordinates(x * 16 + 8, biome, z * 16 + 8);
                }
            }
        }
        return null;
    }

    static {
        MapGenVolcano.volcanoSpawnBiomesLand = Arrays
            .asList(BiomeGenTropicraft.tropics, BiomeGenTropicraft.rainforestPlains);
        MapGenVolcano.volcanoSpawnBiomesOcean = Arrays.asList(BiomeGenTropicraft.tropicsOcean);
        VOLCANO_BLOCK = (Block) TCBlockRegistry.chunkOHead;
        LAVA_BLOCK = Blocks.lava;
    }
}
