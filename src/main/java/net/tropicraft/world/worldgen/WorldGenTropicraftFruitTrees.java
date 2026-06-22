package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTropicraftFruitTrees extends TCGenBase {

    private static final Block WOOD_BLOCK;
    private static final Block FRUIT_LEAF_BLOCK;
    private static final Block REGULAR_LEAF_BLOCK;
    int treeType;

    public WorldGenTropicraftFruitTrees(final World world, final Random rand, final int i) {
        super(world, rand);
        this.treeType = i;
    }

    public boolean generate(final int i, final int j, final int k) {
        final int height = this.rand.nextInt(3) + 4;
        boolean canGenerate = true;
        if (j < 1 || j + height + 1 > this.worldObj.getHeight()) {
            return false;
        }
        for (int y = j; y <= j + 1 + height; ++y) {
            int size = 1;
            if (y == j) {
                size = 0;
            }
            if (y >= j + 1 + height - 2) {
                size = 2;
            }
            for (int x = i - size; x <= i + size && canGenerate; ++x) {
                for (int z = k - size; z <= k + size && canGenerate; ++z) {
                    if (y >= 0 && y < this.worldObj.getHeight()) {
                        final Block block = this.worldObj.getBlock(x, y, z);
                        if (block != Blocks.air && block != WorldGenTropicraftFruitTrees.FRUIT_LEAF_BLOCK) {
                            canGenerate = false;
                        }
                    } else {
                        canGenerate = false;
                    }
                }
            }
        }
        if (!canGenerate) {
            return false;
        }
        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if ((blockUnder != Blocks.grass && blockUnder != Blocks.dirt) || j >= this.worldObj.getHeight() - height - 1) {
            return false;
        }
        this.worldObj.setBlock(i, j - 1, k, Blocks.dirt, 0, WorldGenTropicraftFruitTrees.blockGenNotifyFlag);
        for (int y2 = j - 3 + height; y2 <= j + height; ++y2) {
            final int presizeMod = y2 - (j + height);
            for (int size2 = 1 - presizeMod / 2, x2 = i - size2; x2 <= i + size2; ++x2) {
                final int localX = x2 - i;
                for (int z2 = k - size2; z2 <= k + size2; ++z2) {
                    final int localZ = z2 - k;
                    if ((Math.abs(localX) != size2 || Math.abs(localZ) != size2
                        || (this.rand.nextInt(2) != 0 && presizeMod != 0))
                        && !this.worldObj.getBlock(x2, y2, z2)
                            .isOpaqueCube()) {
                        if (this.rand.nextBoolean()) {
                            this.worldObj.setBlock(
                                x2,
                                y2,
                                z2,
                                WorldGenTropicraftFruitTrees.FRUIT_LEAF_BLOCK,
                                this.treeType,
                                WorldGenTropicraftFruitTrees.blockGenNotifyFlag);
                        } else {
                            this.worldObj.setBlock(
                                x2,
                                y2,
                                z2,
                                WorldGenTropicraftFruitTrees.REGULAR_LEAF_BLOCK,
                                2,
                                WorldGenTropicraftFruitTrees.blockGenNotifyFlag);
                        }
                    }
                }
            }
        }
        for (int y2 = 0; y2 < height; ++y2) {
            final Block k2 = this.worldObj.getBlock(i, j + y2, k);
            if (k2 == Blocks.air || k2.getMaterial() == Material.leaves) {
                this.worldObj.setBlock(
                    i,
                    j + y2,
                    k,
                    WorldGenTropicraftFruitTrees.WOOD_BLOCK,
                    0,
                    WorldGenTropicraftFruitTrees.blockGenNotifyFlag);
            }
        }
        return true;
    }

    static {
        WOOD_BLOCK = Blocks.log;
        FRUIT_LEAF_BLOCK = (Block) TCBlockRegistry.fruitLeaves;
        REGULAR_LEAF_BLOCK = (Block) TCBlockRegistry.rainforestLeaves;
    }
}
