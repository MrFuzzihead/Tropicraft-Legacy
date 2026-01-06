package net.tropicraft.world.worldgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.*;

public abstract class TCGenBase extends WorldGenerator {

    World worldObj;
    Random rand;
    static final byte[] otherCoordPairs;
    List<Block> standardAllowedBlocks;
    public static final int BLOCK_GEN_NOTIFY_FLAG_DEFAULT = 2;
    public static int blockGenNotifyFlag;

    public TCGenBase(final World world, final Random random) {
        this.standardAllowedBlocks = Arrays.asList(Blocks.air, Blocks.leaves, Blocks.tallgrass, Blocks.snow_layer);
        this.worldObj = world;
        this.rand = random;
    }

    public boolean isLeafBlock(final Block block) {
        return block == Blocks.leaves;
    }

    public abstract boolean generate(final int p0, final int p1, final int p2);

    public boolean generate(final World world, final Random rand, final int i, final int j, final int k) {
        this.worldObj = world;
        this.rand = rand;
        return this.generate(i, j, k);
    }

    public List<ChunkCoordinates> genCircle(final int x, final int y, final int z, final double outerRadius,
        final double innerRadius, final Block block, final int meta, final boolean solid) {
        List<ChunkCoordinates> generatedPositions = new ArrayList<>();
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        Chunk chunk = worldObj.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (!chunk.isChunkLoaded) {
            return null;
        }
        for (int chunkI = chunkX - (int) outerRadius - 1; chunkI <= chunkX + (int) outerRadius + 1; ++chunkI) {
            for (int chunkK = chunkZ - (int) outerRadius - 1; chunkK <= chunkZ + (int) outerRadius + 1; ++chunkK) {
                for (int i = chunkI << 4; i < (chunkI << 4) + 16; ++i) {
                    for (int k = chunkK << 4; k < (chunkK << 4) + 16; ++k) {
                        final double d = (double) ((i - x) * (i - x)) + (k - z) * (k - z);
                        if (d <= outerRadius * outerRadius && d >= innerRadius * innerRadius
                            && (this.worldObj.isAirBlock(i, y, k) || solid)
                            && this.worldObj.setBlock(i, y, k, block, meta, TCGenBase.blockGenNotifyFlag)) {
                            generatedPositions.add(new ChunkCoordinates(i, y, k));
                        }
                    }
                }
            }
        }
        return generatedPositions;
    }

    public boolean checkCircle(final int i, final int j, final int k, final double outerRadius,
        final double innerRadius, final List allowedBlockList) {
        for (int x = (int) (-outerRadius - 2.0) + i; x < (int) (outerRadius + 2.0) + i; ++x) {
            for (int z = (int) (-outerRadius - 2.0) + k; z < (int) (outerRadius + 2.0) + k; ++z) {
                final double d = (i - x) * (i - x) + (k - z) * (k - z);
                if (d <= outerRadius * outerRadius && d >= innerRadius * innerRadius
                    && !allowedBlockList.contains(this.worldObj.getBlock(x, j, z))) {
                    System.out.println("t2");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkBlockLine(final int[] ai, final int[] ai1, final List<Block> allowedBlockList) {
        final int[] ai2 = { 0, 0, 0 };
        byte byte0 = 0;
        int j = 0;
        while (byte0 < 3) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
            ++byte0;
        }
        if (ai2[j] == 0) {
            return false;
        }
        final byte byte2 = TCGenBase.otherCoordPairs[j];
        final byte byte3 = TCGenBase.otherCoordPairs[j + 3];
        byte byte4;
        if (ai2[j] > 0) {
            byte4 = 1;
        } else {
            byte4 = -1;
        }
        final double d = ai2[byte2] / (double) ai2[j];
        final double d2 = ai2[byte3] / (double) ai2[j];
        final int[] ai3 = { 0, 0, 0 };

        World world = this.worldObj;

        for (int k = 0, l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);

            int chunkX = ai3[0] >> 4;
            int chunkZ = ai3[2] >> 4;

            Chunk chunk = world.getChunkFromChunkCoords(chunkX >> 4, chunkZ >> 4);
            if (!chunk.isChunkLoaded) {
                return false;
            }

            Block block = chunk.getBlock(ai3[0] & 15, ai3[1], ai3[2] & 15);

            if (!allowedBlockList.contains(block)) {
                return false;
            }
        }
        return true;
    }

    public void placeBlockLine(final int[] start, final int[] end, final Block block, final int meta) {
        int[] difference = new int[] { end[0] - start[0], end[1] - start[1], end[2] - start[2] };
        int largestDifferenceIndex = getLargestDifferenceIndex(difference);

        if (difference[largestDifferenceIndex] == 0) {
            return;
        }

        byte[] otherCoordPairs = TCGenBase.otherCoordPairs;
        byte otherCoord1 = otherCoordPairs[largestDifferenceIndex];
        byte otherCoord2 = otherCoordPairs[largestDifferenceIndex + 3];
        byte sign = (byte) (difference[largestDifferenceIndex] > 0 ? 1 : -1);
        double d1 = difference[otherCoord1] / (double) difference[largestDifferenceIndex];
        double d2 = difference[otherCoord2] / (double) difference[largestDifferenceIndex];

        int chunkX = start[0] >> 4;
        int chunkZ = start[2] >> 4;

        Set<String> placedBlocks = new HashSet<>();

        int[] currentPos = new int[3];
        for (int k = 0, l = difference[largestDifferenceIndex] + sign; k != l; k += sign) {
            currentPos[largestDifferenceIndex] = MathHelper.floor_double(start[largestDifferenceIndex] + k + 0.5);
            currentPos[otherCoord1] = MathHelper.floor_double(start[otherCoord1] + k * d1 + 0.5);
            currentPos[otherCoord2] = MathHelper.floor_double(start[otherCoord2] + k * d2 + 0.5);

            int blockChunkX = currentPos[0] >> 4;
            int blockChunkZ = currentPos[2] >> 4;

            if (blockChunkX != chunkX || blockChunkZ != chunkZ) {
                chunkX = blockChunkX;
                chunkZ = blockChunkZ;

                Chunk chunk = worldObj.getChunkFromChunkCoords(chunkX >> 4, chunkZ >> 4);
                if (!chunk.isChunkLoaded) {
                    return;
                }
            }

            String blockKey = currentPos[0] + "_" + currentPos[1] + "_" + currentPos[2];

            if (worldObj.getBlock(currentPos[0], currentPos[1], currentPos[2]) != block) {
                worldObj
                    .setBlock(currentPos[0], currentPos[1], currentPos[2], block, meta, TCGenBase.blockGenNotifyFlag);
                placedBlocks.add(blockKey);
            }
        }
    }

    private int getLargestDifferenceIndex(int[] differences) {
        int largestIndex = 0;
        for (byte i = 0; i < 3; i++) {
            if (Math.abs(differences[i]) > Math.abs(differences[largestIndex])) {
                largestIndex = i;
            }
        }
        return largestIndex;
    }

    public boolean checkBlockCircleLine(final int[] ai, final int[] ai1, final double outerRadius,
        final double innerRadius, final List allowedBlockList) {
        final ArrayList<int[]> places = new ArrayList<>();
        final int[] ai2 = { 0, 0, 0 };
        byte byte0 = 0;
        int j = 0;
        while (byte0 < 3) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
            ++byte0;
        }
        if (ai2[j] == 0) {
            return false;
        }
        final byte byte2 = TCGenBase.otherCoordPairs[j];
        final byte byte3 = TCGenBase.otherCoordPairs[j + 3];
        byte byte4;
        if (ai2[j] > 0) {
            byte4 = 1;
        } else {
            byte4 = -1;
        }
        final double d = ai2[byte2] / (double) ai2[j];
        final double d2 = ai2[byte3] / (double) ai2[j];
        final int[] ai3 = { 0, 0, 0 };
        for (int k = 0, l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            if (!this.checkCircle(ai3[0], ai3[1], ai3[2], outerRadius, innerRadius, allowedBlockList)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<int[]> checkAndPlaceBlockCircleLine(final int[] ai, final int[] ai1, final double outerRadius,
        final double innerRadius, final Block block, final int meta, final List allowedBlockList) {
        final ArrayList<int[]> places = new ArrayList<>();
        final int[] ai2 = { 0, 0, 0 };
        byte byte0 = 0;
        int j = 0;
        while (byte0 < 3) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
            ++byte0;
        }
        if (ai2[j] == 0) {
            return new ArrayList<>();
        }
        final byte byte2 = TCGenBase.otherCoordPairs[j];
        final byte byte3 = TCGenBase.otherCoordPairs[j + 3];
        byte byte4;
        if (ai2[j] > 0) {
            byte4 = 1;
        } else {
            byte4 = -1;
        }
        final double d = ai2[byte2] / (double) ai2[j];
        final double d2 = ai2[byte3] / (double) ai2[j];
        final int[] ai3 = { 0, 0, 0 };
        for (int k = 0, l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            if (!this.checkCircle(ai3[0], ai3[1], ai3[2], outerRadius, innerRadius, allowedBlockList)) {
                return new ArrayList<>();
            }
        }
        for (int k = 0, l = ai2[j] + byte4; k != l; k += byte4) {
            System.out.println("watwat");
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            this.genCircle(ai3[0], ai3[1], ai3[2], outerRadius, innerRadius, block, meta, true);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }

    public ArrayList<int[]> checkAndPlaceBlockLine(final int[] ai, final int[] ai1, final Block block, final int meta,
        final List allowedBlockList) {
        final ArrayList<int[]> places = new ArrayList<>();
        final int[] ai2 = { 0, 0, 0 };
        byte byte0 = 0;
        int j = 0;
        while (byte0 < 3) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
            ++byte0;
        }
        if (ai2[j] == 0) {
            return new ArrayList<>();
        }
        final byte byte2 = TCGenBase.otherCoordPairs[j];
        final byte byte3 = TCGenBase.otherCoordPairs[j + 3];
        byte byte4;
        if (ai2[j] > 0) {
            byte4 = 1;
        } else {
            byte4 = -1;
        }
        final double d = ai2[byte2] / (double) ai2[j];
        final double d2 = ai2[byte3] / (double) ai2[j];
        final int[] ai3 = { 0, 0, 0 };
        int k = 0;
        for (int l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            if (!allowedBlockList.contains(this.worldObj.getBlock(ai3[0], ai3[1], ai3[2]))) {
                return new ArrayList<>();
            }
        }
        for (int l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            this.worldObj.setBlock(ai3[0], ai3[1], ai3[2], block, meta, TCGenBase.blockGenNotifyFlag);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }

    public ArrayList<int[]> placeBlockCircleLine(final int[] ai, final int[] ai1, final double distance,
        final double distance2, final Block block, final int meta) {
        final ArrayList<int[]> places = new ArrayList<>();
        final int[] ai2 = { 0, 0, 0 };
        byte byte0 = 0;
        int j = 0;
        while (byte0 < 3) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
            ++byte0;
        }
        if (ai2[j] == 0) {
            return new ArrayList<>();
        }
        final byte byte2 = TCGenBase.otherCoordPairs[j];
        final byte byte3 = TCGenBase.otherCoordPairs[j + 3];
        byte byte4;
        if (ai2[j] > 0) {
            byte4 = 1;
        } else {
            byte4 = -1;
        }
        final double d = ai2[byte2] / (double) ai2[j];
        final double d2 = ai2[byte3] / (double) ai2[j];
        final int[] ai3 = { 0, 0, 0 };
        for (int k = 0, l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            this.genCircle(ai3[0], ai3[1], ai3[2], distance, distance2, block, meta, true);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }

    public void genSphere(final int x, final int y, final int z, final int outerRadius, final Block block,
        final int meta) {
        for (int i = x - outerRadius; i < x + outerRadius; ++i) {
            for (int j = y - outerRadius; j < y + outerRadius; ++j) {
                for (int k = z - outerRadius; k < z + outerRadius; ++k) {
                    if (this.worldObj.isAirBlock(i, j, k)) {
                        final int distance1 = (i - x) * (i - x) + (j - y) * (j - y) + (k - z) * (k - z);
                        if (distance1 <= outerRadius) {
                            this.worldObj.setBlock(i, j, k, block, meta, TCGenBase.blockGenNotifyFlag);
                        }
                    }
                }
            }
        }
    }

    public int getTerrainHeightAt(final int x, final int z) {
        for (int y = this.worldObj.getHeightValue(x, z) + 1; y > 0; --y) {
            final Block block = this.worldObj.getBlock(x, y, z);
            if (block == Blocks.dirt || block == Blocks.grass || block == Blocks.sand || block == Blocks.stone) {
                return y + 1;
            }
        }
        return 0;
    }

    public double randAngle() {
        return this.rand.nextDouble() * 3.141592653589793 * 2.0;
    }

    static {
        otherCoordPairs = new byte[] { 2, 0, 0, 1, 2, 1 };
        TCGenBase.blockGenNotifyFlag = 2;
    }
}
