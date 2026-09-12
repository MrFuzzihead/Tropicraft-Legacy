package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
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
        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        this.worldObj.setBlock(i, j, k, WorldGenTCUndergrowth.WOOD_BLOCK, 1, WorldGenTCUndergrowth.blockGenNotifyFlag);
        int size = 2;
        if (this.rand.nextInt(10) == 0) {
            size = 3;
        }
        for (int y = j; y < j + size; ++y) {
            for (int bushWidth = size - (y - j), x = i - bushWidth; x < i + bushWidth; ++x) {
                final int xVariance = x - i;
                for (int z = k - bushWidth; z < k + bushWidth; ++z) {
                    final int zVariance = z - k;
                    if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth
                        || this.rand.nextInt(2) != 0)
                        && !this.worldObj.getBlock(x, y, z)
                            .isOpaqueCube()) {
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

    static {
        WOOD_BLOCK = (Block) TCBlockRegistry.logs;
        LEAF_BLOCK = (Block) TCBlockRegistry.rainforestLeaves;
    }
}
