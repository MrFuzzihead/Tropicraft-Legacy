package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.tropicraft.block.*;
import net.tropicraft.registry.*;

public class WorldGenTropicraftNormalPalms extends WorldGenerator {

    boolean notify;
    private final Block wood;
    private final Block palmLeaves;

    public WorldGenTropicraftNormalPalms() {
        this.wood = TCBlockRegistry.logs;
        this.palmLeaves = TCBlockRegistry.palmLeaves;
        this.notify = false;
    }

    public WorldGenTropicraftNormalPalms(final boolean flag) {
        super(flag);
        this.wood = TCBlockRegistry.logs;
        this.palmLeaves = TCBlockRegistry.palmLeaves;
        this.notify = flag;
    }

    public boolean generate(final World world, final Random random, final int i, int j, final int k) {
        random.nextInt(2);
        final byte height = (byte) (random.nextInt(4) + 6);
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
            for (int k2 = i - byte1; k2 <= i + byte1 && flag; ++k2) {
                for (int i2 = k - byte1; i2 <= k + byte1 && flag; ++i2) {
                    if (l >= 0 && l < 128) {
                        final Block j2 = world.getBlock(k2, l, i2);
                        if (!world.isAirBlock(k2, l, i2) && j2 != this.palmLeaves) {
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
        Block i3 = world.getBlock(i, j - 1, k);
        if (i3 != Blocks.sand || j >= 128 - height - 1) {
            final int ground = world.getHeightValue(i, k);
            i3 = world.getBlock(i, ground - 1, k);
            if (i3 != Blocks.sand || j >= 128 - height - 1) {
                return false;
            }
            j = ground;
        }
        this.setBlockAndMetadata(world, i, j - 1, k, (Block) Blocks.sand, 0);
        this.setBlockAndMetadata(world, i, j + height + 2, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k + 1, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k + 2, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k + 3, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height, k + 4, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 1, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 1, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height + 1, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 4, j + height, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k - 1, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k - 2, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height + 1, k - 3, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i, j + height, k - 4, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 1, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 1, k - 1, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 1, j + height + 1, k + 1, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 1, k - 1, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 1, j + height + 1, k + 1, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 1, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height + 1, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 4, j + height, k, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 1, k + 2, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 2, j + height + 1, k - 2, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 1, k + 2, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 2, j + height + 1, k - 2, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height, k + 3, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i + 3, j + height, k - 3, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height, k + 3, this.palmLeaves, 0);
        this.setBlockAndMetadata(world, i - 3, j + height, k - 3, this.palmLeaves, 0);
        for (int j3 = 0; j3 < height + 4; ++j3) {
            final Block l2 = world.getBlock(i, j + j3, k);
            if (world.isAirBlock(i, j + j3, k) || l2 == this.palmLeaves) {
                this.setBlockAndMetadata(world, i, j + j3 - 2, k, this.wood, 0);
                BlockTropicraftLog.spawnCoconuts(world, i, j + j3 - 2, k, random, 2);
                if (j3 <= height - 1 || j3 >= height + 2) {}
            }
        }
        return true;
    }

    protected void setBlockAndMetadata(final World par1World, final int par2, final int par3, final int par4,
        final Block par5, final int par6) {
        par1World.setBlock(par2, par3, par4, par5, par6, 3);
    }
}
