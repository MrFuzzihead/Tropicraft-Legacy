package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.tropicraft.registry.*;

public class WorldGenTCUndergrowth extends TCGenBase {

    private static final int LARGE_BUSH_CHANCE = 10;
    private static final Block WOOD_BLOCK;
    private static final int WOOD_META = 1;
    private static final Block LEAF_BLOCK;
    private static final int LEAF_META = 1;

    public WorldGenTCUndergrowth(final World world, final Random rand) {
        super(world, rand);
    }

    public boolean generate(final int i, final int j, final int k) {
        int chunkX = i >> 4;
        int chunkZ = k >> 4;

        Chunk chunk = worldObj.getChunkFromChunkCoords(chunkX >> 4, chunkZ >> 4);
        if (!chunk.isChunkLoaded) {
            return false;
        }

        if (!isValidUnderBlock(i, j, k)) {
            return false;
        }

        placeWoodBlock(i, j, k);

        int size = (this.rand.nextInt(10) == 0) ? 3 : 2;

        int chunkStartX = (i - size) >> 4;
        int chunkStartZ = (k - size) >> 4;
        int chunkEndX = (i + size) >> 4;
        int chunkEndZ = (k + size) >> 4;
        for (int y = j; y < j + size; ++y) {
            for (int x = i - size; x <= i + size; ++x) {
                final int xVariance = x - i;
                for (int z = k - size; z <= k + size; ++z) {
                    final int zVariance = z - k;
                    int blockChunkX = x >> 4;
                    int blockChunkZ = z >> 4;
                    if (shouldPlaceLeafBlock(
                        xVariance,
                        zVariance,
                        blockChunkX,
                        blockChunkZ,
                        chunkStartX,
                        chunkStartZ,
                        chunkEndX,
                        chunkEndZ)) {
                        this.worldObj.setBlock(
                            x,
                            y,
                            z,
                            WorldGenTCUndergrowth.LEAF_BLOCK,
                            1,
                            WorldGenTCUndergrowth.blockGenNotifyFlag);
                    }
                }
            }
        }

        return true;
    }

    private boolean isValidUnderBlock(int i, int j, int k) {
        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        return blockUnder == Blocks.dirt || blockUnder == Blocks.grass;
    }

    private void placeWoodBlock(int i, int j, int k) {
        this.worldObj.setBlock(i, j, k, WorldGenTCUndergrowth.WOOD_BLOCK, 1, WorldGenTCUndergrowth.blockGenNotifyFlag);
    }

    private boolean shouldPlaceLeafBlock(int xVariance, int zVariance, int blockChunkX, int blockChunkZ,
        int chunkStartX, int chunkStartZ, int chunkEndX, int chunkEndZ) {
        return (Math.abs(xVariance) != 2 || Math.abs(zVariance) != 2 || this.rand.nextInt(2) != 0)
            && (blockChunkX >= chunkStartX && blockChunkX <= chunkEndX
                && blockChunkZ >= chunkStartZ
                && blockChunkZ <= chunkEndZ);
    }

    static {
        WOOD_BLOCK = TCBlockRegistry.logs;
        LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
    }
}
