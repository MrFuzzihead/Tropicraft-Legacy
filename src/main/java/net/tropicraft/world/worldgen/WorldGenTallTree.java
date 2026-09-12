package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

public class WorldGenTallTree extends TCGenBase {

    private static final int VINE_CHANCE = 5;
    private static final int SMALL_LEAF_CHANCE = 3;
    private static final int SECOND_CANOPY_CHANCE = 3;
    private static final Block WOOD_BLOCK;
    private static final int WOOD_META = 1;
    private static final Block LEAF_BLOCK;
    private static final int LEAF_META = 1;

    public WorldGenTallTree(final World world, final Random random) {
        super(world, random);
    }

    public boolean generate(final World world, final Random random, final int i, final int j, final int k) {
        return this.generate(i, j, k);
    }

    public boolean generate(final int i, final int j, final int k) {
        Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i + 1, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i - 1, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i, j - 1, k + 1);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i, j - 1, k - 1);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        final int height = this.rand.nextInt(15) + 15;
        for (int y = j; y < j + height + 6; ++y) {
            for (int x = i - 1; x <= i + 1; ++x) {
                for (int z = k - 1; z <= k + 1; ++z) {
                    final Block block = this.worldObj.getBlock(x, y, z);
                    if (block != Blocks.air && block != Blocks.tallgrass && block != WorldGenTallTree.LEAF_BLOCK) {
                        return false;
                    }
                }
            }
        }
        this.worldObj.setBlock(i, j, k, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i - 1, j, k, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i + 1, j, k, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i, j, k - 1, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i, j, k + 1, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        for (int y = j; y < j + height; ++y) {
            this.worldObj.setBlock(i, y, k, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i - 1, y, k, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i + 1, y, k, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i, y, k - 1, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i, y, k + 1, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            if (y - j > height / 2 && this.rand.nextInt(3) == 0) {
                final int nx = this.rand.nextInt(3) - 1 + i;
                final int nz = this.rand.nextInt(3) - 1 + k;
                this.genCircle(nx, y + 1, nz, 1.0, 0.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.genCircle(nx, y, nz, 2.0, 1.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
                for (int x2 = nx - 3; x2 <= nx + 3; ++x2) {
                    for (int z2 = nz - 3; z2 <= nz + 3; ++z2) {
                        for (int y2 = y - 1; y2 <= y; ++y2) {
                            if (this.rand.nextInt(5) == 0) {
                                this.genVines(x2, y2, z2);
                            }
                        }
                    }
                }
            }
            if (y - j > height - height / 4 && y - j < height - 3 && this.rand.nextInt(3) == 0) {
                final int nx = i + this.rand.nextInt(9) - 4;
                final int nz = k + this.rand.nextInt(9) - 4;
                final int leafSize = this.rand.nextInt(3) + 5;
                this.genCircle(nx, y + 3, nz, (leafSize - 2), 0.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.genCircle(nx, y + 2, nz, (leafSize - 1), (leafSize - 3), WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.genCircle(nx, y + 1, nz, leafSize, (leafSize - 1), WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.placeBlockLine(
                    new int[] { i, y - 2, k },
                    new int[] { nx, y + 2, nz },
                    WorldGenTallTree.WOOD_BLOCK,
                    1);
                for (int x3 = nx - leafSize; x3 <= nx + leafSize; ++x3) {
                    for (int z3 = nz - leafSize; z3 <= nz + leafSize; ++z3) {
                        for (int y3 = y; y3 <= y + 2; ++y3) {
                            if (this.rand.nextInt(5) == 0) {
                                this.genVines(x3, y3, z3);
                            }
                        }
                    }
                }
            }
        }
        final int leafSize2 = this.rand.nextInt(5) + 9;
        this.genCircle(i, j + height, k, leafSize2 - 2, 0.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
        this.genCircle(i, j + height - 1, k, (leafSize2 - 1), (leafSize2 - 4), WorldGenTallTree.LEAF_BLOCK, 1, false);
        this.genCircle(i, j + height - 2, k, leafSize2, (leafSize2 - 1), WorldGenTallTree.LEAF_BLOCK, 1, false);
        for (int x = i - leafSize2; x <= i + leafSize2; ++x) {
            for (int z = k - leafSize2; z <= k + leafSize2; ++z) {
                for (int y4 = j + height + 3; y4 <= j + height + 6; ++y4) {
                    if (this.rand.nextInt(5) == 0) {
                        this.genVines(x, y4, z);
                    }
                }
            }
        }
        return true;
    }

    private boolean genVines(final int i, final int j, final int k) {
        for (int m = 2; m <= 5; ++m) {
            if (Blocks.vine.canPlaceBlockOnSide(this.worldObj, i, j, k, m)
                && this.worldObj.getBlock(i, j, k) == Blocks.air) {
                this.worldObj.setBlock(
                    i,
                    j,
                    k,
                    Blocks.vine,
                    1 << Direction.facingToDirection[Facing.oppositeSide[m]],
                    WorldGenTallTree.blockGenNotifyFlag);
                for (int length = this.rand.nextInt(4) + 4, y = j - 1; y > j - length; --y) {
                    if (this.worldObj.getBlock(i, y, k) != Blocks.air) {
                        return true;
                    }
                    this.worldObj.setBlock(
                        i,
                        y,
                        k,
                        Blocks.vine,
                        1 << Direction.facingToDirection[Facing.oppositeSide[m]],
                        WorldGenTallTree.blockGenNotifyFlag);
                }
                return true;
            }
        }
        return false;
    }

    static {
        WOOD_BLOCK = TCBlockRegistry.logs;
        LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
    }
}
