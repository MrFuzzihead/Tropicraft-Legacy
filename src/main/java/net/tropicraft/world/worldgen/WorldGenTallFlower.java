package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldGenTallFlower extends TCGenBase {

    private final int FLOWER_TRIES;
    private final Block plantBlock;
    private final int plantBottomMeta;
    private final int plantTopMeta;

    public WorldGenTallFlower(final World world, final Random rand, final Block plantBlock, final int plantBottomMeta,
        final int plantTopMeta) {
        super(world, rand);
        this.FLOWER_TRIES = 4;
        this.plantBlock = plantBlock;
        this.plantBottomMeta = plantBottomMeta;
        this.plantTopMeta = plantTopMeta;
    }

    public boolean generate(final int i, final int j, final int k) {
        int chunkX = i >> 4;
        int chunkZ = k >> 4;

        Chunk chunk = worldObj.getChunkFromChunkCoords(chunkX >> 4, chunkZ >> 4);
        if (!chunk.isChunkLoaded) {
            return false;
        }

        int radius = 5;

        for (int l = 0; l < this.FLOWER_TRIES; ++l) {
            final int i2 = i + this.rand.nextInt(radius * 2 + 1) - radius;
            final int j2 = j + this.rand.nextInt(7) - 3;
            final int k2 = k + this.rand.nextInt(radius * 2 + 1) - radius;

            if (isWithinChunkBounds(i2, k2, chunkX, chunkZ)) {
                Block block = this.worldObj.getBlock(i2, j2, k2);
                Block blockAbove = this.worldObj.getBlock(i2, j2 + 1, k2);

                if (block == Blocks.grass && (blockAbove == Blocks.air || blockAbove == Blocks.tallgrass)
                    && this.plantBlock.canBlockStay(this.worldObj, i2, j2, k2)) {
                    placeFlower(i2, j2, k2);
                }
            }
        }

        return true;
    }

    private boolean isWithinChunkBounds(int i, int k, int chunkX, int chunkZ) {
        int chunkStartX = chunkX << 4;
        int chunkStartZ = chunkZ << 4;
        int chunkEndX = chunkStartX + 15;
        int chunkEndZ = chunkStartZ + 15;

        return i >= chunkStartX && i <= chunkEndX && k >= chunkStartZ && k <= chunkEndZ;
    }

    private void placeFlower(int i, int j, int k) {
        this.worldObj.setBlock(i, j, k, this.plantBlock, this.plantBottomMeta, 2);
        this.worldObj.setBlock(i, j + 1, k, this.plantBlock, this.plantTopMeta, 2);
    }
}
