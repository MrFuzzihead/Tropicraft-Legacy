package net.tropicraft.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.block.tileentity.*;
import net.tropicraft.registry.*;

public class TeleporterTropics extends Teleporter {

    private static Block PORTAL_WALL_BLOCK;
    private static Block PORTAL_BLOCK;
    private final WorldServer world;
    private final Random random;
    private final LongHashMap destinationCoordinateCache;
    private final List<Long> destinationCoordinateKeys;

    public TeleporterTropics(final WorldServer world) {
        super(world);
        this.destinationCoordinateCache = new LongHashMap();
        this.destinationCoordinateKeys = new ArrayList<Long>();
        TeleporterTropics.PORTAL_BLOCK = TCBlockRegistry.tropicsPortal;
        TeleporterTropics.PORTAL_WALL_BLOCK = TCBlockRegistry.tropicsPortalWall;
        this.world = world;
        this.random = new Random(world.getSeed());
    }

    public void placeInPortal(final Entity entity, final double d, final double d2, final double d3, final float f) {
        final long startTime = System.currentTimeMillis();
        if (!this.placeInExistingPortal(entity, d, d2, d3, f)) {
            this.makePortal(entity);
            this.placeInExistingPortal(entity, d, d2, d3, f);
        }
        final long finishTime = System.currentTimeMillis();
        System.out.printf(
            "It took %f seconds for TeleporterTropics.placeInPortal to complete\n",
            (finishTime - startTime) / 1000.0f);
    }

    public boolean placeInExistingPortal(final Entity entity, final double d, final double d2, final double d3,
        final float f) {
        final int searchArea = 148;
        double closestPortal = -1.0;
        int foundX = 0;
        int foundY = 0;
        int foundZ = 0;
        final int entityX = MathHelper.floor_double(entity.posX);
        final int entityZ = MathHelper.floor_double(entity.posZ);
        boolean notInCache = true;
        final long j1 = ChunkCoordIntPair.chunkXZ2Int(entityX, entityZ);
        if (this.destinationCoordinateCache.containsItem(j1)) {
            final Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache
                .getValueByKey(j1);
            closestPortal = 0.0;
            foundX = portalposition.posX;
            foundY = portalposition.posY;
            foundZ = portalposition.posZ;
            portalposition.lastUpdateTime = this.world.getTotalWorldTime();
            notInCache = false;
        } else {
            for (int x = entityX - searchArea; x <= entityX + searchArea; ++x) {
                final double distX = x + 0.5 - entity.posX;
                for (int z = entityZ - searchArea; z <= entityZ + searchArea; ++z) {
                    final double distZ = z + 0.5 - entity.posZ;
                    for (int y = this.world.getActualHeight() - 1; y >= 0; --y) {
                        if (this.world.getBlock(x, y, z) == TeleporterTropics.PORTAL_BLOCK) {
                            while (this.world.getBlock(x, y - 1, z) == TeleporterTropics.PORTAL_BLOCK) {
                                --y;
                            }
                            final double distY = y + 0.5 - entity.posY;
                            final double distance = distX * distX + distY * distY + distZ * distZ;
                            if (closestPortal < 0.0 || distance < closestPortal) {
                                closestPortal = distance;
                                foundX = x;
                                foundY = y;
                                foundZ = z;
                            }
                        }
                    }
                }
            }
        }
        if (closestPortal >= 0.0) {
            if (notInCache) {
                this.destinationCoordinateCache
                    .add(j1, new PortalPosition(foundX, foundY, foundZ, this.world.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(j1);
            }
            final int x = foundX;
            final int y2 = foundY;
            final int z2 = foundZ;
            double newLocX = x + 0.5;
            final double newLocY = y2 + 0.5;
            double newLocZ = z2 + 0.5;
            if (this.world.getBlock(x - 1, y2, z2) == TeleporterTropics.PORTAL_BLOCK) {
                newLocX -= 0.5;
            }
            if (this.world.getBlock(x + 1, y2, z2) == TeleporterTropics.PORTAL_BLOCK) {
                newLocX += 0.5;
            }
            if (this.world.getBlock(x, y2, z2 - 1) == TeleporterTropics.PORTAL_BLOCK) {
                newLocZ -= 0.5;
            }
            if (this.world.getBlock(x, y2, z2 + 1) == TeleporterTropics.PORTAL_BLOCK) {
                newLocZ += 0.5;
            }
            entity.setLocationAndAngles(newLocX, newLocY + 2.0, newLocZ, entity.rotationYaw, 0.0f);
            final int worldSpawnX = MathHelper.floor_double(newLocX);
            final int worldSpawnZ = MathHelper.floor_double(newLocZ);
            final int worldSpawnY = this.world.getHeightValue(worldSpawnX, worldSpawnZ) + 3;
            final double motionX = 0.0;
            entity.motionZ = motionX;
            entity.motionY = motionX;
            entity.motionX = motionX;
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                if (this.world.provider instanceof WorldProviderTropicraft
                    && !player.inventory.hasItem(TCItemRegistry.encTropica)) {
                    TileEntityBambooChest chest = null;
                    final int chestX = MathHelper.floor_double(newLocX);
                    final int chestZ = MathHelper.floor_double(newLocZ);
                    Label_0774: for (int searchX = -3; searchX < 4; ++searchX) {
                        for (int searchZ = -3; searchZ < 4; ++searchZ) {
                            for (int searchY = -4; searchY < 5; ++searchY) {
                                if (this.world.getBlock(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ)
                                    == TCBlockRegistry.bambooChest) {
                                    chest = (TileEntityBambooChest) this.world
                                        .getTileEntity(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ);
                                    if (chest != null && chest.isUnbreakable()) {
                                        break Label_0774;
                                    }
                                }
                            }
                        }
                    }
                    if (chest != null && chest.isUnbreakable()) {
                        boolean hasEncyclopedia = false;
                        for (int inv = 0; inv < chest.getSizeInventory(); ++inv) {
                            final ItemStack stack = chest.getStackInSlot(inv);
                            if (stack != null && stack.getItem() == TCItemRegistry.encTropica) {
                                hasEncyclopedia = true;
                            }
                        }
                        if (!hasEncyclopedia) {
                            for (int inv = 0; inv < chest.getSizeInventory(); ++inv) {
                                final ItemStack stack = chest.getStackInSlot(inv);
                                if (stack == null) {
                                    chest.setInventorySlotContents(inv, new ItemStack(TCItemRegistry.encTropica, 1));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean makePortal(final Entity entity) {
        final int searchArea = 16;
        double closestSpot = -1.0;
        final int entityX = MathHelper.floor_double(entity.posX);
        final int entityY = MathHelper.floor_double(entity.posY);
        final int entityZ = MathHelper.floor_double(entity.posZ);
        int foundX = entityX;
        int foundY = entityY;
        int foundZ = entityZ;
        for (int x = entityX - searchArea; x <= entityX + searchArea; ++x) {
            final double distX = x + 0.5 - entity.posX;
            Label_0418: for (int z = entityZ - searchArea; z <= entityZ + searchArea; ++z) {
                final double distZ = z + 0.5 - entity.posZ;
                int y;
                for (y = this.world.getHeight() - 1; y >= 62
                    && (this.world.getBlock(x, y, z) == Blocks.air || !this.world.getBlock(x, y, z)
                        .isOpaqueCube()); --y) {}
                if (y <= 83) {
                    if (y >= 63) {
                        if (this.getValidBuildBlocks()
                            .contains(this.world.getBlock(x, y, z))) {
                            for (int xOffset = -2; xOffset <= 2; ++xOffset) {
                                for (int zOffset = -2; zOffset <= 2; ++zOffset) {
                                    int otherY;
                                    for (otherY = this.world.getHeight() - 1; otherY >= 63
                                        && (this.world.getBlock(x + xOffset, otherY, z + zOffset) == Blocks.air
                                            || !this.world.getBlock(x, y, z)
                                                .isOpaqueCube()); --otherY) {}
                                    if (Math.abs(y - otherY) >= 3) {
                                        continue Label_0418;
                                    }
                                    if (!this.getValidBuildBlocks()
                                        .contains(this.world.getBlock(x + xOffset, otherY, z + zOffset))) {
                                        continue Label_0418;
                                    }
                                }
                            }
                            final double distY = y + 0.5 - entity.posY;
                            final double distance = distX * distX + distY * distY + distZ * distZ;
                            if (closestSpot < 0.0 || distance < closestSpot) {
                                closestSpot = distance;
                                foundX = x;
                                foundY = y;
                                foundZ = z;
                            }
                        }
                    }
                }
            }
        }
        final int worldSpawnX = MathHelper.floor_double((double) foundX);
        final int worldSpawnZ = MathHelper.floor_double((double) foundZ);
        final int worldSpawnY = this.getTerrainHeightAt(worldSpawnX, worldSpawnZ);
        if (closestSpot < 0.0) {
            foundY = worldSpawnY - 2;
        }
        entity.setLocationAndAngles((double) foundX, (double) (foundY + 2), (double) foundZ, entity.rotationYaw, 0.0f);
        this.buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ, entity);
        return true;
    }

    public int getTerrainHeightAt(final int x, final int z) {
        for (int y = 100; y > 0; --y) {
            final Block block = this.world.getBlock(x, y, z);
            if (block == Blocks.dirt || block == Blocks.grass
                || block == Blocks.sand
                || block == Blocks.stone
                || block == TCBlockRegistry.tropicsWater
                || block == TCBlockRegistry.purifiedSand) {
                return y;
            }
        }
        return 0;
    }

    public void buildTeleporterAt(final int x, int y, final int z, final Entity entity) {
        y = ((y < 9) ? 9 : y);
        for (int yOffset = 4; yOffset >= -7; --yOffset) {
            for (int zOffset = -2; zOffset <= 2; ++zOffset) {
                for (int xOffset = -2; xOffset <= 2; ++xOffset) {
                    final int blockX = x + xOffset;
                    final int blockY = y + yOffset;
                    final int blockZ = z + zOffset;
                    if (yOffset == -7) {
                        this.world.setBlock(blockX, blockY, blockZ, TeleporterTropics.PORTAL_WALL_BLOCK);
                    } else if (yOffset > 0) {
                        this.world.setBlock(blockX, blockY, blockZ, Blocks.air);
                    } else {
                        final boolean isWall = xOffset == -2 || xOffset == 2 || zOffset == -2 || zOffset == 2;
                        if (isWall) {
                            this.world.setBlock(blockX, blockY, blockZ, TeleporterTropics.PORTAL_WALL_BLOCK);
                        } else {
                            final int metadata = (yOffset <= -5) ? 8 : 0;
                            this.world.setBlock(blockX, blockY, blockZ, TeleporterTropics.PORTAL_BLOCK, metadata, 3);
                        }
                    }
                    final boolean isCorner = (xOffset == -2 || xOffset == 2) && (zOffset == -2 || zOffset == 2);
                    if (yOffset == 0 && isCorner) {
                        this.world.setBlock(blockX, blockY + 1, blockZ, (Block) TCBlockRegistry.tikiTorch, 1, 3);
                        this.world.setBlock(blockX, blockY + 2, blockZ, (Block) TCBlockRegistry.tikiTorch, 1, 3);
                        this.world.setBlock(blockX, blockY + 3, blockZ, (Block) TCBlockRegistry.tikiTorch, 0, 3);
                    }
                }
            }
        }
        if (this.world.provider instanceof WorldProviderTropicraft) {
            this.world.setBlock(x + 2, y + 1, z, (Block) TCBlockRegistry.bambooChest, 1, 3);
            final TileEntityBambooChest tile = (TileEntityBambooChest) this.world.getTileEntity(x + 2, y + 1, z);
            if (tile != null) {
                tile.setIsUnbreakable(true);
            }
        }
        for (int yOffset = 5; yOffset >= -7; --yOffset) {
            for (int zOffset = -2; zOffset <= 2; ++zOffset) {
                for (int xOffset = -2; xOffset <= 2; ++xOffset) {
                    final int blockX = x + xOffset;
                    final int blockY = y + yOffset;
                    final int blockZ = z + zOffset;
                    this.world.notifyBlocksOfNeighborChange(
                        blockX,
                        blockY,
                        blockZ,
                        this.world.getBlock(blockX, blockY, blockZ));
                }
            }
        }
    }

    public void removeStalePortalLocations(final long par1) {
        if (par1 % 100L == 0L) {
            final Iterator iterator = this.destinationCoordinateKeys.iterator();
            final long j = par1 - 600L;
            while (iterator.hasNext()) {
                final Long olong = (Long) iterator.next();
                final Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache
                    .getValueByKey((long) olong);
                if (portalposition == null || portalposition.lastUpdateTime < j) {
                    iterator.remove();
                    this.destinationCoordinateCache.remove((long) olong);
                }
            }
        }
    }

    private List<Block> getValidBuildBlocks() {
        return Arrays
            .asList((Block) Blocks.sand, (Block) Blocks.grass, Blocks.dirt, (Block) TCBlockRegistry.purifiedSand);
    }
}
