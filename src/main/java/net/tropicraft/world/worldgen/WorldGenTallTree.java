package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.tropicraft.registry.*;

public class WorldGenTallTree extends TCGenBase {

    private static final int VINE_CHANCE = 5;
    private static final int SMALL_LEAF_CHANCE = 3;
    private static final int SECOND_CANOPY_CHANCE = 3;
    private static final Block WOOD_BLOCK;
    private static final int WOOD_META = 1;
    private static final Block LEAF_BLOCK;
    private static final int LEAF_META = 1;

    public WorldGenTallTree(final World world, final Random random) {
        super(world, random);
    }

    public boolean generate(final World world, final Random random, final int i, final int j, final int k) {
        return this.generate(i, j, k);
    }

    public boolean generate(final int i, final int j, final int k) {
        Chunk chunk = worldObj.getChunkFromChunkCoords(i >> 4, k >> 4);
        if (!chunk.isChunkLoaded) {
            return false;
        }
        Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i + 1, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i - 1, j - 1, k);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i, j - 1, k + 1);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        blockUnder = this.worldObj.getBlock(i, j - 1, k - 1);
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
            return false;
        }
        final int height = this.rand.nextInt(15) + 15;
        for (int y = j; y < j + height + 6; ++y) {
            for (int x = i - 1; x <= i + 1; ++x) {
                for (int z = k - 1; z <= k + 1; ++z) {
                    final Block block = this.worldObj.getBlock(x, y, z);
                    if (block != Blocks.air && block != Blocks.tallgrass && block != WorldGenTallTree.LEAF_BLOCK) {
                        return false;
                    }
                }
            }
        }
        this.worldObj.setBlock(i, j, k, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i - 1, j, k, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i + 1, j, k, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i, j, k - 1, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        this.worldObj.setBlock(i, j, k + 1, Blocks.dirt, 0, WorldGenTallTree.blockGenNotifyFlag);
        for (int y = j; y < j + height; ++y) {
            this.worldObj.setBlock(i, y, k, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i - 1, y, k, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i + 1, y, k, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i, y, k - 1, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            this.worldObj.setBlock(i, y, k + 1, WorldGenTallTree.WOOD_BLOCK, 1, WorldGenTallTree.blockGenNotifyFlag);
            if (y - j > height / 2 && this.rand.nextInt(3) == 0) {
                final int nx = this.rand.nextInt(3) - 1 + i;
                final int nz = this.rand.nextInt(3) - 1 + k;
                this.genCircle(nx, y + 1, nz, 1.0, 0.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.genCircle(nx, y, nz, 2.0, 1.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
                for (int x2 = nx - 3; x2 <= nx + 3; ++x2) {
                    for (int z2 = nz - 3; z2 <= nz + 3; ++z2) {
                        for (int y2 = y - 1; y2 <= y; ++y2) {
                            if (this.rand.nextInt(5) == 0) {
                                this.generateVinesAt(x2, y2, z2);
                            }
                        }
                    }
                }
            }
            if (y - j > height - height / 4 && y - j < height - 3 && this.rand.nextInt(3) == 0) {
                final int nx = i + this.rand.nextInt(9) - 4;
                final int nz = k + this.rand.nextInt(9) - 4;
                final int leafSize = this.rand.nextInt(3) + 5;
                this.genCircle(nx, y + 3, nz, (leafSize - 2), 0.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.genCircle(nx, y + 2, nz, (leafSize - 1), (leafSize - 3), WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.genCircle(nx, y + 1, nz, leafSize, (leafSize - 1), WorldGenTallTree.LEAF_BLOCK, 1, false);
                this.placeBlockLine(
                    new int[] { i, y - 2, k },
                    new int[] { nx, y + 2, nz },
                    WorldGenTallTree.WOOD_BLOCK,
                    1);
                for (int x3 = nx - leafSize; x3 <= nx + leafSize; ++x3) {
                    for (int z3 = nz - leafSize; z3 <= nz + leafSize; ++z3) {
                        for (int y3 = y; y3 <= y + 2; ++y3) {
                            if (this.rand.nextInt(5) == 0) {
                                this.generateVinesAt(x3, y3, z3);
                            }
                        }
                    }
                }
            }
        }
        final int leafSize2 = this.rand.nextInt(5) + 9;
        this.genCircle(i, j + height, k, leafSize2 - 2, 0.0, WorldGenTallTree.LEAF_BLOCK, 1, false);
        this.genCircle(i, j + height - 1, k, (leafSize2 - 1), (leafSize2 - 4), WorldGenTallTree.LEAF_BLOCK, 1, false);
        this.genCircle(i, j + height - 2, k, leafSize2, (leafSize2 - 1), WorldGenTallTree.LEAF_BLOCK, 1, false);

        generateVineCluster(i, j, k, leafSize2, height);
        return true;
    }

    private void generateVineCluster(int i, int j, int k, int leafSize2, int height) {
        List<ChunkCoordinates> vineCoordinates = new ArrayList<>();

        for (int x = i - leafSize2; x <= i + leafSize2; ++x) {
            for (int z = k - leafSize2; z <= k + leafSize2; ++z) {
                for (int y4 = j + height + 3; y4 <= j + height + 6; ++y4) {
                    if (this.rand.nextInt(5) == 0) {
                        vineCoordinates.add(new ChunkCoordinates(x, y4, z));
                    }
                }
            }
        }

        placeVines(vineCoordinates);
    }

    private void placeVines(List<ChunkCoordinates> vineCoordinates) {
        int vineChance = 5;

        for (ChunkCoordinates pos : vineCoordinates) {
            int posX = pos.posX;
            int posY = pos.posY;
            int posZ = pos.posZ;

            if (this.rand.nextInt(vineChance) == 0 && canPlaceVines(posX, posY, posZ)) {
                generateVinesAt(posX, posY, posZ);
            }
        }
    }

    private boolean canPlaceVines(int x, int y, int z) {
        Block vineBlock = Blocks.vine;

        World world = this.worldObj;

        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (!chunk.isChunkLoaded) {
            return false;
        }

        Block blockAtPos = world.getBlock(x, y, z);
        Block blockAbove = world.getBlock(x, y + 1, z);
        Block blockBelow = world.getBlock(x, y - 1, z);

        boolean isAir = blockAtPos.isAir(world, x, y, z);
        boolean isAirAbove = blockAbove.isAir(world, x, y + 1, z);
        boolean isAirBelow = blockBelow.isAir(world, x, y - 1, z);

        return isAir && isAirAbove && isAirBelow && blockAtPos == vineBlock && !vineExistsNearby(x, y, z);
    }

    private boolean vineExistsNearby(int x, int y, int z) {
        int range = 2;
        for (int i = x - range; i <= x + range; i++) {
            for (int j = y - range; j <= y + range; j++) {
                for (int k = z - range; k <= z + range; k++) {
                    if (this.worldObj.getBlock(i, j, k) == Blocks.vine) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void generateVinesAt(int x, int y, int z) {
        Block vineBlock = Blocks.vine;
        Random rand = this.rand;

        List<Integer> validSides = new ArrayList<>();

        for (int m = 2; m <= 5; ++m) {
            int blockX = x + Facing.offsetsXForSide[m];
            int blockY = y + Facing.offsetsYForSide[m];
            int blockZ = z + Facing.offsetsZForSide[m];

            if (this.worldObj.isAirBlock(blockX, blockY, blockZ)) {
                validSides.add(1 << Direction.facingToDirection[Facing.oppositeSide[m]]);
            } else {}
        }

        if (!validSides.isEmpty()) {
            int maxLength = 8;
            int length = rand.nextInt(maxLength) + 1;
            int startY = y - length;
            startY = Math.max(y - 8, startY);

            batchPlaceVines(validSides, x, y, z, vineBlock, startY, y);
        }
    }

    private void batchPlaceVines(List<Integer> validSides, int x, int y, int z, Block vineBlock, int startY, int maxY) {
        for (int vineSide : validSides) {
            placeVinesBottomUp(x, y, z, vineBlock, vineSide, startY, maxY);
        }
    }

    private void placeVinesBottomUp(int x, int y, int z, Block vineBlock, int vineSide, int startY, int endY) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkX, chunkZ);

        int adjustedJ = Math.min(y, endY);

        for (int yCoord = adjustedJ; yCoord >= startY; --yCoord) {
            int localX = x & 15;
            int localZ = z & 15;

            if (chunk.getBlock(localX, yCoord, localZ)
                .isAir(this.worldObj, localX, yCoord, localZ)) {
                chunk.func_150807_a(localX, yCoord, localZ, vineBlock, vineSide);
            }
        }
    }

    static {
        WOOD_BLOCK = TCBlockRegistry.logs;
        LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
    }
}
