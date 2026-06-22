package net.tropicraft.world.mapgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.perlin.generator.RidgedMulti;

public class MapGenUndergroundGrove {

    private static final int CHUNK_SIZE_Y = 256;
    private World worldObj;
    public boolean isActive;
    public int centerX;
    public int centerZ;
    public double length;
    public double width;
    public double height;
    public int y;

    public MapGenUndergroundGrove(final World worldObj) {
        this.isActive = false;
        this.centerX = 0;
        this.centerZ = 0;
        this.worldObj = worldObj;
    }

    public Block[] generate(int x, int z, final Block[] blocks, final byte[] metas) {
        final ChunkCoordinates groveCoords = this.getGroveNear(this.worldObj, x, z);
        if (groveCoords != null) {
            this.centerX = groveCoords.posX;
            this.y = groveCoords.posY;
            this.centerZ = groveCoords.posZ;
            x *= 16;
            z *= 16;
            this.isActive = true;
            final Random rand = new Random(this.worldObj.getSeed() * this.centerX + this.centerZ * 57647382913L);
            final RidgedMulti ridged = new RidgedMulti(rand.nextLong(), 1);
            ridged.frequency = 0.0325;
            this.length = rand.nextInt(20) + 30;
            this.width = rand.nextInt(20) + 30;
            this.height = rand.nextInt(3) + 5;
            this.length *= this.length;
            this.width *= this.width;
            this.height *= this.height;
            for (int i = 0; i < 16; ++i) {
                for (int k = 0; k < 16; ++k) {
                    int relativeX = x + i - this.centerX;
                    int relativeZ = z + k - this.centerZ;
                    relativeX *= relativeX;
                    relativeZ *= relativeZ;
                    for (double j = -this.height; j < this.height; ++j) {
                        if (relativeX / this.length + j * j / this.height + relativeZ / this.width <= 1.0) {
                            this.placeBlock(i, this.y + (int) j, k, Blocks.air, blocks);
                        }
                    }
                    final double noise1 = ridged.getNoise(x + i, z + k);
                    final double noise2 = ridged.getNoise(x + i + 15432, z + k + 42314);
                    if (noise1 > 0.845 || noise2 > 0.855) {
                        final int l = (int) Math.sqrt(
                            this.height - this.height * relativeX / this.length - this.height * relativeZ / this.width);
                        this.placeBlock(i, this.y - l - 1, k, Blocks.dirt, blocks);
                        final double tunnelHeight = (5.0 - (relativeX / 2500.0 + relativeZ / 2500.0) * 2.0) / 3.0;
                        for (int j2 = 0; j2 < tunnelHeight; ++j2) {
                            this.placeBlock(i, this.y - l + j2, k, Blocks.air, blocks);
                        }
                    }
                    if ((i + x) % 16 == 0 && (k + z) % 16 == 0) {
                        final int l = (int) Math.sqrt(
                            this.height - this.height * relativeX / this.length - this.height * relativeZ / this.width);
                        rand.setSeed(i * k * 54325432 * this.worldObj.getSeed() * relativeX * this.centerX);
                        if (this.getBlock(i, this.y - l, k, blocks) == Blocks.air
                            && this.getBlock(i, this.y - l + 1, k, blocks) == Blocks.air
                            && this.getBlock(i, this.y - l + 2, k, blocks) == Blocks.air
                            && rand.nextInt(3) != 0) {
                            this.placeBlockAndMeta(
                                i,
                                this.y - l,
                                k,
                                (Block) TCBlockRegistry.tikiTorch,
                                1,
                                blocks,
                                metas);
                            this.placeBlockAndMeta(
                                i,
                                this.y - l + 1,
                                k,
                                (Block) TCBlockRegistry.tikiTorch,
                                1,
                                blocks,
                                metas);
                            this.placeBlockAndMeta(
                                i,
                                this.y - l + 2,
                                k,
                                (Block) TCBlockRegistry.tikiTorch,
                                0,
                                blocks,
                                metas);
                        }
                    }
                }
            }
            return blocks;
        }
        return blocks;
    }

    public int getHeightAt(final int x, final int z) {
        int relativeX = x - this.centerX;
        int relativeZ = z - this.centerZ;
        relativeX *= relativeX;
        relativeZ *= relativeZ;
        return this.y - (int) Math
            .sqrt(this.height - this.height * relativeX / this.length - this.height * relativeZ / this.width);
    }

    protected boolean canGenGroveAtCoords(final World worldObj, int i, int j) {
        final byte numChunks = 32;
        final byte offsetChunks = 8;
        final int oldi = i;
        final int oldj = j;
        if (i < 0) {
            i -= numChunks - 1;
        }
        if (j < 0) {
            j -= numChunks - 1;
        }
        int randX = i / numChunks;
        int randZ = j / numChunks;
        final long seed = randX * 341832132712L + randZ * 422843987541L
            + worldObj.getWorldInfo()
                .getSeed()
            + 42231726L;
        final Random rand = new Random(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);
        return oldi == randX && oldj == randZ;
    }

    public ChunkCoordinates getGroveNear(final World worldObj, final int i, final int j) {
        for (int range = 4, x = i - range; x <= i + range; ++x) {
            for (int z = j - range; z <= j + range; ++z) {
                final Random rand = new Random(worldObj.getSeed() * x + z * 57647382913L);
                if (this.canGenGroveAtCoords(worldObj, x, z)) {
                    return new ChunkCoordinates(x * 16 + 8, rand.nextInt(5) + 20, z * 16 + 8);
                }
            }
        }
        return null;
    }

    private void placeBlock(final int x, final int y, final int z, final Block block, final Block[] blocks) {
        blocks[x * 256 * 16 | z * 256 | y] = block;
    }

    private void placeBlockAndMeta(final int x, final int y, final int z, final Block block, final int meta,
        final Block[] blocks, final byte[] metas) {
        blocks[x * 256 * 16 | z * 256 | y] = block;
        metas[x * 256 * 16 | z * 256 | y] = (byte) (meta & 0xF);
    }

    private Block getBlock(final int x, final int y, final int z, final Block[] blocks) {
        return blocks[x * 256 * 16 | z * 256 | y];
    }
}
