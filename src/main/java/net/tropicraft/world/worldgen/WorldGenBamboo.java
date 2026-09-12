package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

public class WorldGenBamboo extends TCGenBase {

    private static final int MIN_BAMBOO = 30;
    private static final int MAX_BAMBOO = 60;
    private static final int MIN_HEIGHT = 4;
    private static final int MAX_HEIGHT = 8;
    private static final Block BAMBOO_BLOCK;

    public WorldGenBamboo(final World world, final Random random) {
        super(world, random);
    }

    public boolean generate(final int i, int j, final int k) {
        j = this.getTerrainHeightAt(i, k);
        if (!this.worldObj.isAirBlock(i, j, k)) {
            return false;
        }
        if (this.worldObj.getBlock(i + 1, j - 1, k)
            .getMaterial() != Material.water
            && this.worldObj.getBlock(i - 1, j - 1, k)
                .getMaterial() != Material.water
            && this.worldObj.getBlock(i, j - 1, k + 1)
                .getMaterial() != Material.water
            && this.worldObj.getBlock(i, j - 1, k - 1)
                .getMaterial() != Material.water) {
            return false;
        }
        final int amount = this.rand.nextInt(30) + 30;
        final int spread = this.rand.nextInt(3) - 1 + (int) (Math.sqrt(amount) / 2.0);
        for (int l = 0; l < amount; ++l) {
            for (int x = i + this.rand.nextInt(spread) - this.rand.nextInt(spread),
                z = k + this.rand.nextInt(spread) - this.rand.nextInt(spread), y = this.getTerrainHeightAt(x, z),
                height = this.rand.nextInt(4) + 4, h = 0; h < height && this.worldObj.isAirBlock(x, y + h, z); ++h) {
                this.worldObj.setBlock(x, y + h, z, WorldGenBamboo.BAMBOO_BLOCK, 0, WorldGenBamboo.blockGenNotifyFlag);
            }
        }
        return true;
    }

    static {
        BAMBOO_BLOCK = TCBlockRegistry.bambooChute;
    }
}
