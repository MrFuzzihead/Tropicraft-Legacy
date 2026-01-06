package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.tropicraft.registry.*;

public class WorldGenTualang extends TCGenBase {

    private static final Block WOOD_BLOCK;
    private static final Block LEAF_BLOCK;
    private final int baseHeight;
    private final int maxHeight;

    public WorldGenTualang(final World world, final Random random, final int maxHeight, final int baseHeight) {
        super(world, random);
        this.baseHeight = baseHeight;
        this.maxHeight = maxHeight;
    }

    public boolean generate(final int i, final int j, final int k) {
        Chunk chunk = worldObj.getChunkFromChunkCoords(i >> 4, k >> 4);
        if (!chunk.isChunkLoaded) {
            return false;
        }
        final int height = this.rand.nextInt(this.maxHeight - this.baseHeight) + this.baseHeight + j;
        final int branches = this.rand.nextInt(3) + 3;

        if (height + 6 > 256) {
            return false;
        }

        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }

        if (checkBlocks(i, j, k, height)) {
            return false;
        }

        buildTrunk(i, j, k, height);

        generateBranches(i, k, height, branches);

        return true;
    }

    private boolean checkBlocks(int i, int j, int k, int height) {
        for (int y = j; y < j + height; ++y) {
            Block block = this.worldObj.getBlock(i, y, k);
            if (block != null && block.getMaterial()
                .isOpaque()) {
                return true;
            }
        }
        return false;
    }

    private void buildTrunk(int i, int j, int k, int height) {
        for (int y = j; y < height; ++y) {
            this.worldObj.setBlock(i, y, k, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
        }
    }

    private void generateBranches(int i, int k, int height, int branches) {
        for (int x = 0; x < branches; ++x) {
            final int branchHeight = this.rand.nextInt(4) + 2 + height;
            final int bx = this.rand.nextInt(15) - 8 + i;
            final int bz = this.rand.nextInt(15) - 8 + k;

            int midX = i + this.sign((bx - i) / 2);
            int midZ = k + this.sign((bz - k) / 2);

            for (int y = height; y <= branchHeight; y++) {
                this.worldObj
                    .setBlock(midX, y, midZ, WorldGenTualang.WOOD_BLOCK, 1, WorldGenTualang.blockGenNotifyFlag);
            }

            this.genCircle(bx, branchHeight, bz, 2.0, 1.0, WorldGenTualang.LEAF_BLOCK, 1, false);
            this.genCircle(bx, branchHeight + 1, bz, 3.0, 2.0, WorldGenTualang.LEAF_BLOCK, 1, false);
        }
    }

    private int sign(final int i) {
        return (i == 0) ? 0 : ((i <= 0) ? -1 : 1);
    }

    static {
        WOOD_BLOCK = TCBlockRegistry.logs;
        LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
    }
}
