package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.world.*;

public class WorldGenTallFlower extends TCGenBase {

    private final int FLOWER_TRIES;
    private final Block plantBlock;
    private final int plantBottomMeta;
    private final int plantTopMeta;

    public WorldGenTallFlower(final World world, final Random rand, final Block plantBlock, final int plantBottomMeta,
        final int plantTopMeta) {
        super(world, rand);
        this.FLOWER_TRIES = 12;
        this.plantBlock = plantBlock;
        this.plantBottomMeta = plantBottomMeta;
        this.plantTopMeta = plantTopMeta;
    }

    public boolean generate(final int i, final int j, final int k) {
        for (int l = 0; l < this.FLOWER_TRIES; ++l) {
            final int i2 = i + this.rand.nextInt(8) - this.rand.nextInt(8);
            final int j2 = j + this.rand.nextInt(3) - this.rand.nextInt(3);
            final int k2 = k + this.rand.nextInt(8) - this.rand.nextInt(8);
            if (this.worldObj.isAirBlock(i2, j2, k2) && this.worldObj.isAirBlock(i2, j2 + 1, k2)
                && this.plantBlock.canBlockStay(this.worldObj, i2, j2, k2)) {
                this.worldObj
                    .setBlock(i2, j2, k2, this.plantBlock, this.plantBottomMeta, WorldGenTallFlower.blockGenNotifyFlag);
                this.worldObj.setBlock(
                    i2,
                    j2 + 1,
                    k2,
                    this.plantBlock,
                    this.plantTopMeta,
                    WorldGenTallFlower.blockGenNotifyFlag);
            }
        }
        return true;
    }
}
