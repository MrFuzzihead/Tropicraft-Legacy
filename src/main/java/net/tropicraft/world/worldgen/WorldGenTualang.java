package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

public class WorldGenTualang extends TCGenBase {

    private static final Block WOOD_BLOCK;
    private static final int WOOD_META = 1;
    private static final Block LEAF_BLOCK;
    private static final int LEAF_META = 1;
    private final int baseHeight;
    private final int maxHeight;

    public WorldGenTualang(final World world, final Random random, final int maxHeight, final int baseHeight) {
        super(world, random);
        this.baseHeight = baseHeight;
        this.maxHeight = maxHeight;
    }

    public boolean generate(final int i, final int j, final int k) {
        final int height = this.rand.nextInt(this.maxHeight - this.baseHeight) + this.baseHeight + j;
        final int branches = this.rand.nextInt(3) + 3;
        if (height + 6 > 256) {
            return false;
        }
        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        for (int x = i - 1; x <= i + 1; ++x) {
            for (int z = k - 1; z <= k + 1; ++z) {
                for (int y = j; y < j + height; ++y) {
                    final Block block = this.worldObj.getBlock(x, y, z);
                    if (block.isOpaqueCube()) {
                        return false;
                    }
                }
            }
        }
        for (int x = i - 9; x >= i + 9; ++x) {
            for (int z = k - 9; z >= k + 9; ++z) {
                for (int y = height; y < height + 6; ++y) {
                    final Block block = this.worldObj.getBlock(x, y, z);
                    if (block.isOpaqueCube()) {
                        return false;
                    }
                }
            }
        }
        this.worldObj.setBlock(i, j, k, Blocks.dirt, 0, WorldGenTualang.blockGenNotifyFlag);
        this.worldObj.setBlock(i - 1, j, k, Blocks.dirt, 0, WorldGenTualang.blockGenNotifyFlag);
        this.worldObj.setBlock(i + 1, j, k, Blocks.dirt, 0, WorldGenTualang.blockGenNotifyFlag);
        this.worldObj.setBlock(i, j, k - 1, Blocks.dirt, 0, WorldGenTualang.blockGenNotifyFlag);
        this.worldObj.setBlock(i, j, k + 1, Blocks.dirt, 0, WorldGenTualang.blockGenNotifyFlag);
        for (int y2 = j; y2 < height; ++y2) {
            this.worldObj.setBlock(i, y2, k, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
            this.worldObj.setBlock(i - 1, y2, k, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
            this.worldObj.setBlock(i + 1, y2, k, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
            this.worldObj.setBlock(i, y2, k - 1, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
            this.worldObj.setBlock(i, y2, k + 1, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
        }
        for (int x = 0; x < branches; ++x) {
            final int branchHeight = this.rand.nextInt(4) + 2 + height;
            final int bx = this.rand.nextInt(15) - 8 + i;
            final int bz = this.rand.nextInt(15) - 8 + k;
            this.placeBlockLine(
                new int[] { i + this.sign((bx - i) / 2), height, k + this.sign((bz - k) / 2) },
                new int[] { bx, branchHeight, bz },
                WorldGenTualang.WOOD_BLOCK,
                1);
            this.genCircle(bx, branchHeight, bz, 2.0, 1.0, WorldGenTualang.LEAF_BLOCK, 1, false);
            this.genCircle(bx, branchHeight + 1, bz, 3.0, 2.0, WorldGenTualang.LEAF_BLOCK, 1, false);
        }
        return true;
    }

    private int sign(final int i) {
        return (i == 0) ? 0 : ((i <= 0) ? -1 : 1);
    }

    static {
        WOOD_BLOCK = TCBlockRegistry.logs;
        LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
    }
}
