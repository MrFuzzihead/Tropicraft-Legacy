package net.tropicraft.world.mapgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class MapGenTropicsCaves extends MapGenBase {

    private static final int CHUNK_SIZE_Y = 256;

    public void generate(final IChunkProvider chunkProvider, final World world, final int x, final int z,
        final Block[] blocks) {
        final int range = this.range;
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        final long l = this.rand.nextLong();
        final long i1 = this.rand.nextLong();
        for (int j1 = x - range; j1 <= x + range; ++j1) {
            for (int k1 = z - range; k1 <= z + range; ++k1) {
                final long l2 = j1 * l;
                final long i2 = k1 * i1;
                this.rand.setSeed(l2 ^ i2 ^ world.getSeed());
                this.recursiveGenerate(world, j1, k1, x, z, blocks);
            }
        }
    }

    protected void generateLargeCaveNode(final long seed, final int chunkX, final int chunkZ, final Block[] blocks,
        final double i, final double j, final double k) {
        this.generateCaveNode(
            seed,
            chunkX,
            chunkZ,
            blocks,
            i,
            j,
            k,
            1.0f + this.rand.nextFloat() * 6.0f,
            0.0f,
            0.0f,
            -1,
            -1,
            0.5);
    }

    protected void generateCaveNode(final long seed, final int chunkX, final int chunkZ, final Block[] blocks, double i,
        double j, double k, final float sizeMod, float headingXZ, float headingY, int currentSection, int length,
        final double sizeModY) {
        final double d4 = chunkX * 16 + 8;
        final double d5 = chunkZ * 16 + 8;
        float f3 = 0.0f;
        float f4 = 0.0f;
        final Random random = new Random(seed);
        if (length <= 0) {
            final int j2 = this.range * 16 - 16;
            length = j2 - random.nextInt(j2 / 4);
        }
        boolean flag = false;
        if (currentSection == -1) {
            currentSection = length / 2;
            flag = true;
        }
        final int k2 = random.nextInt(length / 2) + length / 4;
        final boolean flag2 = random.nextInt(6) == 0;
        while (currentSection < length) {
            final double d6 = 1.5 + MathHelper.sin(currentSection * 3.1415927f / length) * sizeMod * 1.0f;
            final double d7 = d6 * sizeModY;
            final float f5 = MathHelper.cos(headingY);
            final float f6 = MathHelper.sin(headingY);
            i += MathHelper.cos(headingXZ) * f5;
            j += f6;
            k += MathHelper.sin(headingXZ) * f5;
            if (flag2) {
                headingY *= 0.92f;
            } else {
                headingY *= 0.7f;
            }
            headingY += f4 * 0.1f;
            headingXZ += f3 * 0.1f;
            f4 *= 0.9f;
            f3 *= 0.75f;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (!flag && currentSection == k2 && sizeMod > 1.0f && length > 0) {
                this.generateCaveNode(
                    random.nextLong(),
                    chunkX,
                    chunkZ,
                    blocks,
                    i,
                    j,
                    k,
                    random.nextFloat() * 0.5f + 0.5f,
                    headingXZ - 1.5707964f,
                    headingY / 3.0f,
                    currentSection,
                    length,
                    1.0);
                this.generateCaveNode(
                    random.nextLong(),
                    chunkX,
                    chunkZ,
                    blocks,
                    i,
                    j,
                    k,
                    random.nextFloat() * 0.5f + 0.5f,
                    headingXZ + 1.5707964f,
                    headingY / 3.0f,
                    currentSection,
                    length,
                    1.0);
                return;
            }
            if (flag || random.nextInt(4) != 0) {
                final double d8 = i - d4;
                final double d9 = k - d5;
                final double d10 = length - currentSection;
                final double d11 = sizeMod + 2.0f + 16.0f;
                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return;
                }
                if (i >= d4 - 16.0 - d6 * 2.0 && k >= d5 - 16.0 - d6 * 2.0
                    && i <= d4 + 16.0 + d6 * 2.0
                    && k <= d5 + 16.0 + d6 * 2.0) {
                    int l1 = MathHelper.floor_double(i - d6) - chunkX * 16 - 1;
                    int i2 = MathHelper.floor_double(i + d6) - chunkX * 16 + 1;
                    int j3 = MathHelper.floor_double(j - d7) - 1;
                    int k3 = MathHelper.floor_double(j + d7) + 1;
                    int l2 = MathHelper.floor_double(k - d6) - chunkZ * 16 - 1;
                    int i3 = MathHelper.floor_double(k + d6) - chunkZ * 16 + 1;
                    if (l1 < 0) {
                        l1 = 0;
                    }
                    if (i2 > 16) {
                        i2 = 16;
                    }
                    if (j3 < 1) {
                        j3 = 1;
                    }
                    if (k3 > 120) {
                        k3 = 120;
                    }
                    if (l2 < 0) {
                        l2 = 0;
                    }
                    if (i3 > 16) {
                        i3 = 16;
                    }
                    boolean flag3 = false;
                    for (int j4 = l1; !flag3 && j4 < i2; ++j4) {
                        for (int l3 = l2; !flag3 && l3 < i3; ++l3) {
                            for (int i4 = k3 + 1; !flag3 && i4 >= j3 - 1; --i4) {
                                final int k4 = j4 * 256 * 16 | l3 * 256 | i4;
                                if (i4 >= 0 && i4 < 128) {
                                    if (this.isOceanBlock(blocks, k4, j4, i4, l3, chunkX, chunkZ)) {
                                        flag3 = true;
                                    }
                                    if (i4 != j3 - 1 && j4 != l1 && j4 != i2 - 1 && l3 != l2 && l3 != i3 - 1) {
                                        i4 = j3;
                                    }
                                }
                            }
                        }
                    }
                    if (!flag3) {
                        for (int j4 = l1; j4 < i2; ++j4) {
                            final double d12 = (j4 + chunkX * 16 + 0.5 - i) / d6;
                            for (int k4 = l2; k4 < i3; ++k4) {
                                final double d13 = (k4 + chunkZ * 16 + 0.5 - k) / d6;
                                boolean flag4 = false;
                                if (d12 * d12 + d13 * d13 < 1.0) {
                                    for (int k5 = k3 - 1; k5 >= j3; --k5) {
                                        final int j5 = j4 * 256 * 16 | k4 * 256 | k5;
                                        final double d14 = (k5 + 0.5 - j) / d7;
                                        if (d14 > -0.7 && d12 * d12 + d14 * d14 + d13 * d13 < 1.0) {
                                            if (this.isTopBlock(blocks, j5, j4, k5, k4, chunkX, chunkZ)) {
                                                flag4 = true;
                                            }
                                            this.digBlock(blocks, j5, j4, k5, k4, chunkX, chunkZ, flag4);
                                        }
                                    }
                                }
                            }
                        }
                        if (flag) {
                            break;
                        }
                    }
                }
            }
            ++currentSection;
        }
    }

    protected void recursiveGenerate(final World world, final int i, final int k, final int chunkX, final int chunkZ,
        final Block[] blocks) {
        int i2 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
        if (this.rand.nextInt(15) != 0) {
            i2 = 0;
        }
        for (int j1 = 0; j1 < i2; ++j1) {
            final double x = i * 16 + this.rand.nextInt(16);
            final double y = this.rand.nextInt(this.rand.nextInt(120) + 8);
            final double z = k * 16 + this.rand.nextInt(16);
            int k2 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.generateLargeCaveNode(this.rand.nextLong(), chunkX, chunkZ, blocks, x, y, z);
                k2 += this.rand.nextInt(4);
            }
            for (int l1 = 0; l1 < k2; ++l1) {
                final float f = this.rand.nextFloat() * 3.1415927f * 2.0f;
                final float f2 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f3 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                if (this.rand.nextInt(10) == 0) {
                    f3 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0f + 1.0f;
                }
                this.generateCaveNode(this.rand.nextLong(), chunkX, chunkZ, blocks, x, y, z, f3, f, f2, 0, 0, 1.0);
            }
        }
    }

    protected boolean isOceanBlock(final Block[] data, final int index, final int x, final int y, final int z,
        final int chunkX, final int chunkZ) {
        return data[index] == Blocks.water;
    }

    private boolean isExceptionBiome(final BiomeGenBase biome) {
        return biome == BiomeGenBase.mushroomIsland || biome == BiomeGenBase.beach || biome == BiomeGenBase.desert;
    }

    private boolean isTopBlock(final Block[] data, final int index, final int x, final int y, final int z,
        final int chunkX, final int chunkZ) {
        final BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        return this.isExceptionBiome(biome) ? (data[index] == Blocks.grass) : (data[index] == biome.topBlock);
    }

    protected void digBlock(final Block[] data, final int index, final int x, final int y, final int z,
        final int chunkX, final int chunkZ, final boolean foundTop) {
        final BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        final Block top = (Block) (this.isExceptionBiome(biome) ? Blocks.grass : biome.topBlock);
        final Block filler = this.isExceptionBiome(biome) ? Blocks.dirt : biome.fillerBlock;
        final Block block = data[index];
        if (block == Blocks.stone || block == filler || block == top) {
            if (y < 10) {
                data[index] = Blocks.lava;
            } else {
                data[index] = Blocks.air;
                if (foundTop && data[index - 1] == filler) {
                    data[index - 1] = top;
                }
            }
        }
    }
}
