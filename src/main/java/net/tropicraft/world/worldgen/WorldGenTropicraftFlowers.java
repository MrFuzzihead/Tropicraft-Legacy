package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.tropicraft.registry.*;

public class WorldGenTropicraftFlowers extends TCGenBase {

    private static final int FLOWER_TRIES = 35;
    private final Block plantBlock;
    private final int[] metadata;

    public WorldGenTropicraftFlowers(final World world, final Random rand, final Block plantBlock,
        final int[] metadata) {
        super(world, rand);
        this.plantBlock = plantBlock;
        this.metadata = metadata;
    }

    public boolean generate(final int i, final int j, final int k) {
        for (int l = 0; l < 35; ++l) {
            final int x = i + this.rand.nextInt(8) - this.rand.nextInt(8);
            final int y = j + this.rand.nextInt(4) - this.rand.nextInt(4);
            final int z = k + this.rand.nextInt(8) - this.rand.nextInt(8);

            Chunk chunk = worldObj.getChunkFromChunkCoords(x >> 4, z >> 4);
            if (!chunk.isChunkLoaded) {
                return false;
            }

            Block blockAtPos = this.worldObj.getBlock(x, y, z);
            if (blockAtPos.isAir(worldObj, x, y, z) && TCBlockRegistry.flowers.canBlockStay(this.worldObj, x, y, z)
                && this.rand.nextInt(3) == 0) {
                this.worldObj.setBlock(
                    x,
                    y,
                    z,
                    this.plantBlock,
                    this.metadata[this.rand.nextInt(this.metadata.length)],
                    WorldGenTropicraftFlowers.blockGenNotifyFlag);
            }
        }
        return true;
    }
}
