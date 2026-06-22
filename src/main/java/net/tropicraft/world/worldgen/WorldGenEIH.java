package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenEIH extends TCGenBase {

    private static final int CHUNK_SIZE_Y = 256;
    private static final Block EIH_BLOCK;

    public WorldGenEIH(final World worldObj, final Random rand) {
        super(worldObj, rand);
    }

    public boolean generate(final int i, int j, final int k) {
        final byte height = 5;
        if (j < 1 || j + height + 1 > 256) {
            return false;
        }
        if ((this.worldObj.getBlock(i, j - 1, k) == Blocks.dirt || this.worldObj.getBlock(i, j - 1, k) == Blocks.grass)
            && this.worldObj.getBlock(i, j, k) == Blocks.air) {
            ++j;
            this.worldObj.setBlock(i + 0, j + 0, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 0, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 0, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 0, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 0, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 0, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 1, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 1, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 1, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 1, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 1, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 1, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 2, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 2, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 2, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 3, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 3, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 3, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 3, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 4, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 3, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 3, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 2, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 4, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 4, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 4, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 5, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 5, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 5, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 5, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 3, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 4, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 6, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 6, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 6, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 6, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 6, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 1, j + 5, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 1, j + 5, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 1, j + 4, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 1, j + 4, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 2, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 0, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 0, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 6, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 5, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 4, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 5, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 3, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 4, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 3, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 2, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 3, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 2, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 1, j + 1, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 3, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 3, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 2, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 1, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 1, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 0, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 0, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 0, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 0, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 1, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 2, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 3, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 4, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 5, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 6, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 6, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 6, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 5, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 5, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 4, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 4, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 5, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 4, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 3, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 4, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 3, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 2, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 3, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 2, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 1, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 3, j + 0, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 0, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 0, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 0, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 1, k + 4, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 1, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 2, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 1, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 2, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 2, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 3, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 4, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 3, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 3, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 3, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 4, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 5, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 6, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 6, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j + 6, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 4, j + 5, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 4, j + 4, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 4, j + 4, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j + 4, k + 0, Blocks.lava);
            this.worldObj.setBlock(i + 0, j + 4, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 3, j + 4, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 3, j + 4, k + 1, Blocks.lava);
            this.worldObj.setBlock(i - 3, j + 5, k + 0, Blocks.lava);
            this.worldObj.setBlock(i - 4, j + 5, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 1, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 1, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j + 0, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 0, k - 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j - 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j - 1, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 1, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 1, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j - 1, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j - 1, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 1, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 1, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j - 2, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 2, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 2, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j - 2, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 2, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 2, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j - 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j - 2, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j - 3, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 3, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j + 0, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j + 0, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 1, j - 1, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j - 1, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j - 2, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 1, j - 2, k + 2, Blocks.lava);
            this.worldObj.setBlock(i - 2, j - 3, k + 3, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 3, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 3, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j - 3, k + 2, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 3, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 3, k + 1, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 3, j - 3, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 2, j - 3, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i - 1, j - 3, k + 0, WorldGenEIH.EIH_BLOCK);
            this.worldObj.setBlock(i + 0, j - 3, k + 0, WorldGenEIH.EIH_BLOCK);
            final int k2 = this.rand.nextInt(7);
            final int eyeOneX = i;
            final int eyeOneY = j + 5;
            final int eyeOneZ = k + 1;
            final int eyeTwoX = i - 3;
            final int eyeTwoY = j + 5;
            final int eyeTwoZ = k + 1;
            this.placeEye(this.worldObj, eyeOneX, eyeOneY, eyeOneZ, k2, this.rand);
            this.placeEye(this.worldObj, eyeTwoX, eyeTwoY, eyeTwoZ, k2, this.rand);
        }
        return true;
    }

    private void placeEye(final World worldObj, final int x, final int y, final int z, final int eye_rand,
        final Random rand) {
        int meta = 0;
        Block block = null;
        switch (eye_rand) {
            case 0:
            case 5: {
                block = Blocks.glowstone;
                break;
            }
            case 1: {
                block = Blocks.obsidian;
                break;
            }
            case 2: {
                block = Blocks.diamond_block;
                break;
            }
            case 3: {
                block = Blocks.iron_block;
                break;
            }
            case 4: {
                block = Blocks.gold_block;
                break;
            }
            case 6: {
                block = (Block) TCBlockRegistry.oreBlocks;
                meta = rand.nextInt(3);
                break;
            }
            default: {
                block = Blocks.redstone_block;
                break;
            }
        }
        worldObj.setBlock(x, y, z, block, meta, WorldGenEIH.blockGenNotifyFlag);
    }

    static {
        EIH_BLOCK = (Block) TCBlockRegistry.chunkOHead;
    }
}
