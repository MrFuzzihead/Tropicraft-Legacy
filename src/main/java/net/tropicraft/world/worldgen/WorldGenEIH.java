package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenEIH extends TCGenBase {

    private static final int CHUNK_SIZE_Y = 256;
    /** generate() raises the origin by one block, the head then reaches this far above / below that raised origin. */
    private static final int HEAD_ABOVE_ORIGIN = 6;
    private static final int HEAD_BELOW_ORIGIN = 3;
    /**
     * Number of eye materials the shared roll can pick, see placeEye. The redstone fallback that case 7 would need
     * is unreachable on purpose, it has been there since 1.6.2 as a "should never get called" guard.
     */
    private static final int EYE_MATERIAL_COUNT = 7;
    /**
     * Number of ore eye variants that can be rolled, see placeEye. {@code oreBlocks} is a four way multi block -
     * Eudialyte, Zircon, Azurite and Zirconium - but the roll upstream used nextInt(3) on, so Zirconium eyes were
     * unobtainable. This covers all four.
     */
    private static final int ORE_META_COUNT = 4;
    /** One in this many heads gets two different ore eyes (heterochromia) instead of a matched pair. */
    private static final int HETEROCHROMIA_CHANCE = 8;
    /** One in this many eyes rejects the head's shared material and picks its own instead. */
    private static final int ODD_EYE_OUT_CHANCE = 1000;
    private static final Block EIH_BLOCK;

    public WorldGenEIH(final World worldObj, final Random rand) {
        super(worldObj, rand);
    }

    public boolean generate(final int i, int j, final int k) {
        final Block ground = this.worldObj.getBlock(i, j - 1, k);
        if ((ground == Blocks.dirt || ground == Blocks.grass) && this.worldObj.getBlock(i, j, k) == Blocks.air) {
            ++j;
            // Keep the whole head inside the vertical world bounds, it is built from j - HEAD_BELOW_ORIGIN to
            // j + HEAD_ABOVE_ORIGIN.
            if (j - HEAD_BELOW_ORIGIN < 0 || j + HEAD_ABOVE_ORIGIN >= CHUNK_SIZE_Y) {
                return false;
            }
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
            // Both eyes share one roll so a head always gets a matched pair. The ore eye is the only kind that has
            // a meaningful meta, and this used to roll it per eye, which is how heads ended up with one dark red
            // Zircon eye and one pink Eudialyte eye. Every one in HETEROCHROMIA_CHANCE heads asks for that on
            // purpose now, and for a guaranteed colour clash rather than by luck.
            final int k2 = this.rand.nextInt(EYE_MATERIAL_COUNT);
            final int eyeMetaOne = this.rand.nextInt(ORE_META_COUNT);
            int eyeMetaTwo = eyeMetaOne;
            if (this.rand.nextInt(HETEROCHROMIA_CHANCE) == 0) {
                eyeMetaTwo = (eyeMetaOne + 1 + this.rand.nextInt(ORE_META_COUNT - 1)) % ORE_META_COUNT;
            }
            // On top of the metadata, an eye sometimes refuses the shared material outright and takes one of the
            // other six, which is how a head ends up with glowstone in one socket and obsidian in the other. Modern
            // Tropicraft does the same at one in a thousand per eye and keeps it that rare deliberately. It re rolls
            // blindly though, so a seventh of those tries land straight back on the material they just turned down;
            // asking for a different one here keeps the odd eye actually odd.
            int eyeOneRand = k2;
            if (this.rand.nextInt(ODD_EYE_OUT_CHANCE) == 0) {
                eyeOneRand = (k2 + 1 + this.rand.nextInt(EYE_MATERIAL_COUNT - 1)) % EYE_MATERIAL_COUNT;
            }
            int eyeTwoRand = k2;
            if (this.rand.nextInt(ODD_EYE_OUT_CHANCE) == 0) {
                eyeTwoRand = (k2 + 1 + this.rand.nextInt(EYE_MATERIAL_COUNT - 1)) % EYE_MATERIAL_COUNT;
            }
            final int eyeOneX = i;
            final int eyeOneY = j + 5;
            final int eyeOneZ = k + 1;
            final int eyeTwoX = i - 3;
            final int eyeTwoY = j + 5;
            final int eyeTwoZ = k + 1;
            this.placeEye(this.worldObj, eyeOneX, eyeOneY, eyeOneZ, eyeOneRand, eyeMetaOne);
            this.placeEye(this.worldObj, eyeTwoX, eyeTwoY, eyeTwoZ, eyeTwoRand, eyeMetaTwo);
            return true;
        }
        return false;
    }

    private void placeEye(final World worldObj, final int x, final int y, final int z, final int eye_rand,
        final int oreMeta) {
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
                meta = oreMeta;
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
