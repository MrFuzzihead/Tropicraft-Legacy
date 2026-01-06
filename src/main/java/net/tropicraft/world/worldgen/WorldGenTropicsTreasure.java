package net.tropicraft.world.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.world.storage.*;
import net.tropicraft.registry.*;

public class WorldGenTropicsTreasure extends TCGenBase {

    private static final List<Item> treasureList;
    private static final List<Block> sandBlocks;

    public WorldGenTropicsTreasure(final World worldObj, final Random rand) {
        super(worldObj, rand);
    }

    public boolean generate(final int startX, int startY, final int startZ) {
        final int depth = this.rand.nextInt(2) + 2;
        int tries = 0;
        final int maxTries = 10;
        while (tries < maxTries) {
            int x = startX + this.rand.nextInt(8) - this.rand.nextInt(8);
            int z = startZ + this.rand.nextInt(8) - this.rand.nextInt(8);
            int y;
            int sandArea;
            for (y = this.getTerrainHeightAt(x, z) - 1; y > y - depth; --y) {
                if (!WorldGenTropicsTreasure.sandBlocks.contains(this.worldObj.getBlock(x, y, z))) {
                    ++tries;
                    break;
                }
            }
            if (y <= startY - depth) {
                sandArea = 3;
                boolean shouldContinue = false;
                for (int surroundZ = z - sandArea; surroundZ <= z + sandArea; ++surroundZ) {
                    for (int surroundX = x - sandArea; surroundX <= x + sandArea; ++surroundX) {
                        if (!WorldGenTropicsTreasure.sandBlocks
                            .contains(this.worldObj.getBlock(surroundX, y, surroundZ))) {
                            shouldContinue = true;
                            break;
                        }
                    }
                    if (shouldContinue) {
                        break;
                    }
                }
                if (shouldContinue) {
                    continue;
                }
                for (int surroundZ = z - sandArea; surroundZ <= z + sandArea; ++surroundZ) {
                    for (int surroundX = x - sandArea; surroundX <= x + sandArea; ++surroundX) {
                        if (this.rand.nextFloat() < 0.2f) {
                            this.worldObj.setBlock(surroundX, y, surroundZ, TCBlockRegistry.purifiedSand);
                        }
                    }
                }
                this.worldObj.setBlock(x, y, z, TCBlockRegistry.bambooChest);
                final TileEntityChest tileEntityChest = (TileEntityChest) this.worldObj.getTileEntity(x, y, z);
                if (tileEntityChest == null) {
                    return false;
                }
                boolean hasAddedMap = false;
                for (int e = 0; e < 8; ++e) {
                    final ItemStack itemStack = this.pickCheckLootItem(this.worldObj, this.rand, x, y, z);
                    if (itemStack.getItem() != Items.map || !hasAddedMap) {
                        if (itemStack.getItem() == Items.map) {
                            hasAddedMap = true;
                            this.initializeMap(this.worldObj, itemStack, x, y, z);
                        }
                        tileEntityChest
                            .setInventorySlotContents(this.rand.nextInt(tileEntityChest.getSizeInventory()), itemStack);
                    }
                }
                return true;
            }
        }
        return true;
    }

    private ItemStack pickCheckLootItem(final World worldObj, final Random rand, final int x, final int y,
        final int z) {
        final Item loot = WorldGenTropicsTreasure.treasureList
            .get(rand.nextInt(WorldGenTropicsTreasure.treasureList.size()));
        if (loot == Items.iron_ingot) {
            return new ItemStack(loot, rand.nextInt(36) + 1);
        }
        if (loot == Items.gold_ingot) {
            return new ItemStack(loot, rand.nextInt(46) + 1);
        }
        if (loot == Items.diamond) {
            return new ItemStack(loot, rand.nextInt(24) + 6);
        }
        if (loot == Items.arrow) {
            return new ItemStack(loot, rand.nextInt(45) + 1);
        }
        if (loot == Items.gold_nugget) {
            return new ItemStack(loot, rand.nextInt(40) + 1);
        }
        if (loot == TCItemRegistry.ore) {
            return new ItemStack(loot, rand.nextInt(10) + 5, rand.nextInt(5));
        }
        if (loot == TCItemRegistry.shells) {
            return new ItemStack(loot, rand.nextInt(4) + 2, rand.nextInt(6));
        }
        return new ItemStack(loot);
    }

    private void initializeMap(final World worldObj, final ItemStack mapItem, final int x, final int y, final int z) {
        mapItem.setItemDamage(worldObj.getUniqueDataId("map"));
        final String mapName = "map_" + mapItem.getItemDamage();
        final MapData data = new MapData(mapName);
        worldObj.setItemData(mapName, data);
        data.xCenter = x;
        data.zCenter = z;
        data.scale = 3;
        data.markDirty();
    }

    static {
        treasureList = new ArrayList<>();
        sandBlocks = new ArrayList<>();
        WorldGenTropicsTreasure.treasureList.add(Items.iron_ingot);
        WorldGenTropicsTreasure.treasureList.add(Items.gold_ingot);
        WorldGenTropicsTreasure.treasureList.add(Items.diamond);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.ore);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.shells);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.scaleHelmet);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.scaleBoots);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.scaleChestplate);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.scaleLeggings);
        WorldGenTropicsTreasure.treasureList.add(Items.golden_apple);
        WorldGenTropicsTreasure.treasureList.add(Items.arrow);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.swordEudialyte);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.swordZircon);
        WorldGenTropicsTreasure.treasureList.add(Items.gold_nugget);
        WorldGenTropicsTreasure.treasureList.add(Items.map);
        WorldGenTropicsTreasure.treasureList.add(Items.spider_eye);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.recordTradeWinds);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.recordEasternIsles);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.recordBuriedTreasure);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.recordLowTide);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.recordSummering);
        WorldGenTropicsTreasure.treasureList.add(TCItemRegistry.recordTheTribe);
        WorldGenTropicsTreasure.sandBlocks.add(Blocks.sand);
        WorldGenTropicsTreasure.sandBlocks.add(Blocks.sandstone);
        WorldGenTropicsTreasure.sandBlocks.add(TCBlockRegistry.purifiedSand);
    }
}
