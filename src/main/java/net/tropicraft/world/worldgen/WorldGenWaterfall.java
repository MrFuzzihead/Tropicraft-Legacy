package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

public class WorldGenWaterfall extends TCGenBase {

    private static final Block WATER_BLOCK = TCBlockRegistry.tropicsWater;

    public WorldGenWaterfall(World world, Random random) {
        super(world, random);
    }

    public boolean generate(int i, int j, int k) {
        if (worldObj.getBlock(i, j, k) == Blocks.stone) {
            int size = rand.nextInt(4) + 3;
            sizeLoop: for (int[] direction : new int[][] { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 } }) {
                int dirX = direction[0];
                int dirY = direction[1];
                int dirZ = direction[2];

                if (worldObj.getBlock(i + dirX, j + dirY, k + dirZ) == Blocks.air) {
                    int dir = (worldObj.getBlock(i, j, k + 1) == Blocks.stone) ? 1 : -1;
                    size *= dir;

                    for (int x = 0; x < size; x += dir) {
                        worldObj.setBlock(
                            i + (dirX * x),
                            j + (dirY * x),
                            k + (dirZ * x),
                            WATER_BLOCK,
                            0,
                            WorldGenWaterfall.blockGenNotifyFlag);

                        if (worldObj.getBlock(i + (dirX * (x + dir)), j + (dirY * (x + dir)), k + dirZ) != Blocks.air
                            || worldObj.getBlock(i + (dirX * (x + dir)), j + (dirY * (x + dir)), k) != Blocks.stone) {
                            continue sizeLoop;
                        }
                    }
                }
            }
        }
        return true;
    }
}
