package net.tropicraft.world.worldgen;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

public class WorldGenHomeTree extends TCGenBase {

    private final Block woodID;
    private final int woodMeta = 1;
    private final Block leafID;
    private final int leafMeta = 0;
    private ArrayList<BranchNode> branchList;
    private int trunkRadius;

    public WorldGenHomeTree(final World world, final Random random) {
        super(world, random);
        this.woodID = TCBlockRegistry.logs;
        this.leafID = TCBlockRegistry.rainforestLeaves;
        this.branchList = new ArrayList<>();
    }

    public boolean generate(final int i, int j, final int k) {
        j = 127;
        this.trunkRadius = this.rand.nextInt(3) + 7;
        for (int x = i - this.trunkRadius; x < i + this.trunkRadius; ++x) {
            for (int z = k - this.trunkRadius; z < k + this.trunkRadius; ++z) {
                final int tHeight = this.getTerrainHeightAt(x, z);
                if (tHeight < j) {
                    j = tHeight;
                }
            }
        }
        final int height = this.rand.nextInt(20) + 30;
        if (height + j + 12 > 255) {
            return false;
        }
        final int[] top = this.generateTrunk(i, j, k, height);
        this.generateBranches(top[0], top[1], height + j);
        return true;
    }

    public int[] generateTrunk(final int i, final int j, final int k, final int height) {
        int bn = 0;
        int chanceToDisplace = 0;
        final int xDisplace = this.rand.nextBoolean() ? 1 : -1;
        final int zDisplace = this.rand.nextBoolean() ? 1 : -1;
        int trunkX = i;
        int trunkZ = k;
        for (int y = j; y < height + j; ++y) {
            ++chanceToDisplace;
            this.genCircle(trunkX, y, trunkZ, this.trunkRadius, this.trunkRadius - 3, this.woodID, 1, false);
            if (y == height + j - 1 || (y - j) % 6 == 0) {
                this.genCircle(trunkX, y, trunkZ, this.trunkRadius, 2.0, this.woodID, 1, false);
                this.placeBlock(trunkX + 1, y, trunkZ + 1, this.woodID, 1, false);
                this.placeBlock(trunkX + 1, y, trunkZ - 1, this.woodID, 1, false);
                this.placeBlock(trunkX - 1, y, trunkZ + 1, this.woodID, 1, false);
                this.placeBlock(trunkX - 1, y, trunkZ - 1, this.woodID, 1, false);
                final double angle = this.rand.nextDouble() * 3.141592 * 2.0;
                if (this.rand.nextInt(3) == 0) {
                    final double length = this.rand.nextDouble() * this.trunkRadius - 4.0;
                    final int spawnerX = trunkX - 4 + this.rand.nextInt(9);
                    final int spawnerZ = trunkZ - 4 + this.rand.nextInt(9);
                    this.worldObj.setBlock(spawnerX, y + 1, spawnerZ, Blocks.mob_spawner);
                    final StringBuilder sb = new StringBuilder(String.format("%s.", "tropicraft"));
                    sb.append(this.rand.nextBoolean() ? "AshenHunter" : "Iguana");
                    final TileEntityMobSpawner spawner = (TileEntityMobSpawner) this.worldObj
                        .getTileEntity(spawnerX, y + 1, spawnerZ);
                    if (spawner != null) {
                        spawner.func_145881_a()
                            .setEntityName(sb.toString());
                    }
                }
            }
            this.placeBlock(trunkX, y, trunkZ, this.woodID, 1, false);
            this.placeBlock(trunkX + 1, y, trunkZ, Blocks.vine, 2, false);
            this.placeBlock(trunkX - 1, y, trunkZ, Blocks.vine, 2, false);
            this.placeBlock(trunkX, y, trunkZ + 1, Blocks.vine, 2, false);
            this.placeBlock(trunkX, y, trunkZ - 1, Blocks.vine, 2, false);
            if (this.rand.nextInt(6) == 0) {}
            if (y >= j + height - 6) {
                for (int branches = this.rand.nextInt(4) + 2, x = 0; x < branches; ++x) {
                    final int branchLength = this.rand.nextInt(10) + 15;
                    int branchX1 = trunkX;
                    int branchZ1 = trunkZ;
                    final double angle2 = this.rand.nextDouble() * 3.141592 * 2.0;
                    branchX1 += (int) (this.trunkRadius * Math.sin(angle2));
                    branchZ1 += (int) (this.trunkRadius * Math.cos(angle2));
                    final int branchX2 = (int) (branchLength * Math.sin(angle2) + branchX1);
                    final int branchZ2 = (int) (branchLength * Math.cos(angle2) + branchZ1);
                    final int branchY2 = this.rand.nextInt(4) + 4;
                    this.branchList.add(new BranchNode(branchX1, y, branchZ1, branchX2, y + branchY2, branchZ2));
                    ++bn;
                }
            }
            if (this.rand.nextInt(6) + 4 <= chanceToDisplace && chanceToDisplace * 9 > y) {
                if (this.rand.nextBoolean()) {
                    trunkX += xDisplace;
                    if (this.rand.nextBoolean()) {
                        trunkZ += zDisplace;
                    }
                } else if (this.rand.nextBoolean()) {
                    trunkZ += zDisplace;
                    if (this.rand.nextBoolean()) {
                        trunkZ += xDisplace;
                    }
                }
                chanceToDisplace = 0;
            }
            this.placeBlock(trunkX, y, trunkZ, Blocks.log, 0, false);
        }
        this.worldObj.setBlock(trunkX - 1, height + j, trunkZ - 1, (Block) TCBlockRegistry.bambooChest);
        final TileEntityChest chest = (TileEntityChest) this.worldObj.getTileEntity(trunkX - 1, height + j, trunkZ - 1);
        if (chest != null) {
            for (int treasure = this.rand.nextInt(6) + 4, x = 0; x < treasure; ++x) {
                chest.setInventorySlotContents(this.rand.nextInt(chest.getSizeInventory()), this.randLoot());
            }
        }
        return new int[] { trunkX, trunkZ };
    }

    public void generateBranches(final int topX, final int topZ, final int height) {
        final int lSize = 3;
        int[][] coords = new int[6][3];

        for (final BranchNode bnode : this.branchList) {
            coords[0] = new int[] { bnode.x1, bnode.y1, bnode.z1 };
            coords[1] = new int[] { bnode.x2, bnode.y2, bnode.z2 };
            coords[2] = new int[] { bnode.x1 + 1, bnode.y1, bnode.z1 };
            coords[3] = new int[] { bnode.x2 + 1, bnode.y2, bnode.z2 };
            coords[4] = new int[] { bnode.x1 - 1, bnode.y1, bnode.z1 };
            coords[5] = new int[] { bnode.x2 - 1, bnode.y2, bnode.z2 };

            boolean shouldPlaceBlock = false;
            for (int i = 0; i < 6; i += 2) {
                shouldPlaceBlock = shouldPlaceBlock
                    || this.checkBlockLine(coords[i], coords[i + 1], this.standardAllowedBlocks);
            }

            if (shouldPlaceBlock) {
                for (int i = 0; i < 6; i += 2) {
                    this.placeBlockLine(coords[i], coords[i + 1], this.woodID, 1);
                }

                if (bnode.y2 + 1 <= height) {
                    this.placeBlockLine(
                        new int[] { bnode.x1, bnode.y1 + 1, bnode.z1 },
                        new int[] { bnode.x2, bnode.y2 + 1, bnode.z2 },
                        this.woodID,
                        1);
                }

                this.genLeafCircle(bnode.x2, bnode.y2 - 1, bnode.z2, lSize + 5, lSize + 3, this.leafID, 0, true);
                this.genLeafCircle(bnode.x2, bnode.y2, bnode.z2, lSize + 6, 0, this.leafID, 0, true);
                this.genLeafCircle(bnode.x2, bnode.y2 + 1, bnode.z2, lSize + 10, 0, this.leafID, 0, true);
                this.genLeafCircle(bnode.x2, bnode.y2 + 2, bnode.z2, lSize + 9, 0, this.leafID, 0, true);
            }
        }

        final int topBranches = this.rand.nextInt(6) + 6;
    }

    public boolean genTopBranch(final int i, final int j, final int k, final int sX, final int sY, final int sZ,
        final int topX, final int topZ) {
        final ArrayList<Block> allowedBlocks = new ArrayList<Block>(this.standardAllowedBlocks);
        allowedBlocks.add(this.woodID);
        allowedBlocks.add(this.leafID);
        allowedBlocks.add(Blocks.vine);
        final int branchSize = this.rand.nextInt(2) + 4;
        final ArrayList<int[]> lines = new ArrayList<int[]>();
        for (int x = i - branchSize; x < i + branchSize; ++x) {
            for (int z = k - branchSize; z < k + branchSize; ++z) {
                if ((x - i) * (x - i) + (z - k) * (z - k) < branchSize * branchSize
                    && (x - topX) * (x - topX) + (z - topZ) * (z - topZ) < this.trunkRadius * this.trunkRadius
                    && !this.checkBlockLine(
                        new int[] { x, j, z },
                        new int[] { sX + (i - x), sY, sZ + (k - z) },
                        (List) allowedBlocks)) {
                    return false;
                }
            }
        }
        for (int x = i - branchSize; x < i + branchSize; ++x) {
            for (int z = k - branchSize; z < k + branchSize; ++z) {
                if ((x - i) * (x - i) + (z - k) * (z - k) < branchSize * branchSize
                    && (x - topX) * (x - topX) + (z - topZ) * (z - topZ) < this.trunkRadius * this.trunkRadius) {
                    this.placeBlockLine(
                        new int[] { x, j, z },
                        new int[] { sX + (i - x), sY, sZ + (k - z) },
                        this.woodID,
                        1);
                }
            }
        }
        return true;
    }

    public void genLeafCircle(final int x, final int y, final int z, final int outerRadius, final int innerRadius,
        final Block leafID2, final int meta, final boolean vines) {
        final int outerRadiusSquared = outerRadius * outerRadius;
        final int innerRadiusSquared = innerRadius * innerRadius;

        for (int i = -outerRadius + x; i < outerRadius + x; ++i) {
            for (int k = -outerRadius + z; k < outerRadius + z; ++k) {
                final double d = (x - i) * (x - i) + (z - k) * (z - k);
                if (d <= outerRadiusSquared && d >= innerRadiusSquared) {
                    if (this.worldObj.isAirBlock(i, y, k) || this.worldObj.getBlock(i, y, k) == leafID2) {
                        this.placeBlock(i, y, k, leafID2, meta, false);
                    }
                    if (this.rand.nextInt(20) == 0 && vines) {
                        this.genVines(i, y - 1, k);
                    }
                }
            }
        }
    }

    public void genVines(final int i, final int j, final int k) {
        final int length = this.rand.nextInt(15) + 8;
        final int dir = this.rand.nextInt(4);
        for (int y = j; y > j - length && this.worldObj.isAirBlock(i, y, k); --y) {
            this.placeBlock(i, y, k, Blocks.vine, dir, false);
        }
    }

    public boolean placeBlock(final int i, final int j, final int k, final Block woodID2, final int meta,
        final boolean force) {
        final Block bID = this.worldObj.getBlock(i, j, k);
        if (!force && !canReplaceBlock(bID)) {
            return false;
        }

        return this.worldObj.setBlock(i, j, k, woodID2, meta, 0);
    }

    private boolean canReplaceBlock(Block block) {
        return block == Blocks.water || block == Blocks.flowing_water
            || block == TCBlockRegistry.tropicsWater
            || block == Blocks.air;
    }

    public List<ChunkCoordinates> genCircle(final int i, final int j, final int k, final double outerRadius,
        final double innerRadius, final Block id, final int meta, final boolean solid) {
        List<ChunkCoordinates> generatedCoordinates = new ArrayList<>();
        final double outerRadiusSquared = outerRadius * outerRadius;
        final double innerRadiusSquared = innerRadius * innerRadius;
        for (int x = (int) (-outerRadius) + i; x < (int) outerRadius + i; ++x) {
            for (int z = (int) (-outerRadius) + k; z < (int) outerRadius + k; ++z) {
                final double d = (x - i) * (x - i) + (z - k) * (z - k);
                if (d <= outerRadiusSquared && d >= innerRadiusSquared) {
                    final Block bID = this.worldObj.getBlock(x, j, z);
                    if ((bID == Blocks.air || bID == Blocks.water
                        || bID == Blocks.flowing_water
                        || bID == TCBlockRegistry.tropicsWater
                        || solid) && this.placeBlock(x, j, z, id, meta, solid)) {
                        generatedCoordinates.add(new ChunkCoordinates(x, j, z));
                    }
                }
            }
        }
        return generatedCoordinates;
    }

    public void placeBlockLine(final int[] ai, final int[] ai1, final Block i, final int meta) {
        final ArrayList<int[]> places = new ArrayList<int[]>();
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
            return;
        }
        final byte byte2 = WorldGenHomeTree.otherCoordPairs[j];
        final byte byte3 = WorldGenHomeTree.otherCoordPairs[j + 3];
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
            this.placeBlock(ai3[0], ai3[1], ai3[2], i, meta, true);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
    }

    public ArrayList<int[]> checkAndPlaceBlockLine(final int[] ai, final int[] ai1, final Block i, final int meta,
        final List a) {
        final ArrayList<int[]> places = new ArrayList<int[]>();
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
            return null;
        }
        final byte byte2 = WorldGenHomeTree.otherCoordPairs[j];
        final byte byte3 = WorldGenHomeTree.otherCoordPairs[j + 3];
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
            final Block bId = this.worldObj.getBlock(ai3[0], ai3[1], ai3[2]);
            if (!a.contains(bId)) {
                return null;
            }
        }
        for (int l = ai2[j] + byte4; k != l; k += byte4) {
            ai3[j] = MathHelper.floor_double(ai[j] + k + 0.5);
            ai3[byte2] = MathHelper.floor_double(ai[byte2] + k * d + 0.5);
            ai3[byte3] = MathHelper.floor_double(ai[byte3] + k * d2 + 0.5);
            this.placeBlock(ai3[0], ai3[1], ai3[2], i, meta, true);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }

    public ItemStack randLoot() {
        final int picker = this.rand.nextInt(18);
        if (picker < 6) {
            return new ItemStack(TCItemRegistry.bambooChute, this.rand.nextInt(20) + 1);
        }
        if (picker < 8) {
            return new ItemStack((Item) TCItemRegistry.coconutBomb, this.rand.nextInt(3) + 1);
        }
        if (picker < 10) {
            return new ItemStack((Item) TCItemRegistry.scale, this.rand.nextInt(3) + 1);
        }
        if (picker < 14) {
            return new ItemStack((Item) TCItemRegistry.cookedFrogLeg, this.rand.nextInt(4) + 1);
        }
        if (picker <= 15) {
            return new ItemStack(TCItemRegistry.recordTradeWinds, 1);
        }
        if (picker == 16) {
            return new ItemStack(TCItemRegistry.recordEasternIsles, 1);
        }
        return new ItemStack((Item) TCItemRegistry.ore, 1, 3);
    }

    private class BranchNode {

        public int x1;
        public int y1;
        public int z1;
        public int x2;
        public int y2;
        public int z2;

        public BranchNode(final int i, final int j, final int k, final int x, final int y, final int z) {
            this.x1 = i;
            this.y1 = j;
            this.z1 = k;
            this.x2 = x;
            this.y2 = y;
            this.z2 = z;
        }
    }
}
