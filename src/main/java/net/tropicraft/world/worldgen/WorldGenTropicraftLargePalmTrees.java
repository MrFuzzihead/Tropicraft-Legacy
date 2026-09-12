package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.tropicraft.block.*;
import net.tropicraft.registry.*;

public class WorldGenTropicraftLargePalmTrees extends WorldGenerator {

    private final Block wood;
    private final Block tropicsLeaves;
    protected Random rand;

    public WorldGenTropicraftLargePalmTrees() {
        this.wood = TCBlockRegistry.logs;
        this.tropicsLeaves = TCBlockRegistry.palmLeaves;
    }

    public WorldGenTropicraftLargePalmTrees(final boolean flag) {
        super(flag);
        this.wood = TCBlockRegistry.logs;
        this.tropicsLeaves = TCBlockRegistry.palmLeaves;
    }

    public boolean generate(final World world, final Random random, final int i, int j, final int k) {
        final int b = random.nextInt(2);
        final byte height = (byte) (random.nextInt(4) + 7);
        boolean flag = true;
        if (j < 1 || j + height + 1 > 128) {
            return false;
        }
        for (int l = j; l <= j + 1 + height; ++l) {
            byte byte1 = 1;
            if (l == j) {
                byte1 = 0;
            }
            if (l >= j + 1 + height - 2) {
                byte1 = 2;
            }
            for (int j2 = i - byte1; j2 <= i + byte1 && flag; ++j2) {
                for (int k2 = k - byte1; k2 <= k + byte1 && flag; ++k2) {
                    if (l >= 0 && l < 128) {
                        final Block l2 = world.getBlock(j2, l, k2);
                        if (l2 != Blocks.air && l2 != TCBlockRegistry.palmLeaves) {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                }
            }
        }
        if (!flag) {
            return false;
        }
        Block i2 = world.getBlock(i, j - 1, k);
        if (i2 != Blocks.sand || j >= 128 - height - 1) {
            final int ground = world.getHeightValue(i, k);
            i2 = world.getBlock(i, ground - 1, k);
            if (i2 != Blocks.sand || j >= 128 - height - 1) {
                return false;
            }
            j = ground;
        }
        for (int y = 0; y <= height; ++y) {
            this.setBlockAndMetadata(world, i, j + y, k, this.wood, 0);
        }
        this.setBlockAndMetadata(world, i, j + height + 1, k - 7, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 1, k - 6, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 1, k - 6, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 1, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 1, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 6, j + height + 1, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k - 1, this.wood, 0);
        this.setBlockAndMetadata(world, i + 6, j + height + 1, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 7, j + height + 1, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 1, k, this.wood, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k, this.wood, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 1, k, this.wood, 0);
        this.setBlockAndMetadata(world, i + 7, j + height + 1, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 6, j + height + 1, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k + 1, this.wood, 0);
        this.setBlockAndMetadata(world, i + 6, j + height + 1, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 1, k + 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 1, k + 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 1, k + 6, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 1, k + 6, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k + 7, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 2, k - 6, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 2, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 2, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 2, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 2, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 2, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 2, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 2, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 2, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 2, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 2, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 2, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 2, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 2, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 6, j + height + 2, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 2, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 6, j + height + 2, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 2, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 2, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 2, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 2, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 2, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 2, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 2, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 2, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 2, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 2, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 2, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 2, k + 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 2, k + 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 2, k + 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 2, k + 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 2, k + 6, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k - 5, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 3, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 3, k - 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 3, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 3, k - 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 3, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 3, k - 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 3, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 3, k - 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 5, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 5, j + height + 3, k, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 3, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 3, k + 1, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 3, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 3, k + 2, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 3, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 3, k + 3, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height + 3, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height + 3, k + 4, this.tropicsLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 3, k + 5, this.tropicsLeaves, 0);
        for (int y = 0; y <= height; ++y) {
            BlockTropicraftLog.spawnCoconuts(world, i, j + y, k, random, 2);
        }
        return true;
    }

    public void setBlockAndMetadata(final World par1World, final int par2, final int par3, final int par4,
        final Block par5, final int par6) {
        par1World.setBlock(par2, par3, par4, par5, par6, 3);
    }
}
