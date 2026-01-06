package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.*;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenUpTree extends TCGenBase {

    private static final Block WOOD_BLOCK;
    private static final int WOOD_META = 1;
    private static final Block LEAF_BLOCK;
    private static final int LEAF_META = 1;

    public WorldGenUpTree(final World world, final Random random) {
        super(world, random);
    }

    public boolean generate(final int i, final int j, final int k) {
        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.grass && blockUnder != Blocks.dirt) {
            return false;
        }
        final int height = this.rand.nextInt(4) + 6;
        for (int y = j; y < j + height; ++y) {
            final Block block = this.worldObj.getBlock(i, y, k);
            if (block.getMaterial() == Material.leaves && !block.isOpaqueCube()) {
                return false;
            }
        }
        for (int y = j; y < j + height; ++y) {
            this.worldObj.setBlock(i, y, k, WorldGenUpTree.WOOD_BLOCK, 1, WorldGenUpTree.blockGenNotifyFlag);
            if (this.rand.nextInt(5) == 0) {
                final int x = this.rand.nextInt(3) - 1 + i;
                int z = k;
                if (x - i == 0) {
                    z += (this.rand.nextBoolean() ? 1 : -1);
                }
                this.worldObj.setBlock(x, y, z, WorldGenUpTree.LEAF_BLOCK, 1, WorldGenUpTree.blockGenNotifyFlag);
            }
            if (y == j + height - 1) {
                this.worldObj.setBlock(i + 1, y, k, WorldGenUpTree.WOOD_BLOCK, 1, WorldGenUpTree.blockGenNotifyFlag);
                this.worldObj.setBlock(i - 1, y, k, WorldGenUpTree.WOOD_BLOCK, 1, WorldGenUpTree.blockGenNotifyFlag);
                this.worldObj.setBlock(i, y, k + 1, WorldGenUpTree.WOOD_BLOCK, 1, WorldGenUpTree.blockGenNotifyFlag);
                this.worldObj.setBlock(i, y, k - 1, WorldGenUpTree.WOOD_BLOCK, 1, WorldGenUpTree.blockGenNotifyFlag);
            }
        }
        final int radius = this.rand.nextInt(2) + 3;
        this.genCircle(i, j + height, k, radius, 0.0, WorldGenUpTree.LEAF_BLOCK, 1, false);
        this.genCircle(i, j + height + 1, k, (radius + 2), radius, WorldGenUpTree.LEAF_BLOCK, 1, false);
        this.genCircle(i, j + height + 2, k, (radius + 3), (radius + 2), WorldGenUpTree.LEAF_BLOCK, 1, false);
        return true;
    }

    static {
        WOOD_BLOCK = TCBlockRegistry.logs;
        LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
    }
}
