package net.tropicraft.event;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCBlockEvents {

    @SubscribeEvent
    public void handlePineappleBreakEvent(final BlockEvent.HarvestDropsEvent event) {
        final World world = event.world;
        final Block block = event.block;
        final int meta = event.blockMetadata;
        final int x = event.x;
        final int y = event.y;
        final int z = event.z;
        final EntityPlayer player = event.harvester;
        if (world.isRemote) {
            return;
        }
        if (block != TCBlockRegistry.pineapple) {
            return;
        }
        final ItemStack handItemStack = this.getPlayerItem(world, player);
        Item inHand;
        if (handItemStack != null) {
            inHand = handItemStack.getItem();
        } else {
            inHand = null;
        }
        ItemStack drop = null;
        if (inHand != null
            && (inHand instanceof ItemSword || inHand.getItemUseAction(new ItemStack(inHand)) == EnumAction.block)) {
            drop = new ItemStack((Item) TCItemRegistry.pineappleCubes);
        } else {
            drop = new ItemStack(block, 1, 0);
        }
        final int numDrops = (meta == 8 && drop.getItem() == TCItemRegistry.pineappleCubes)
            ? (world.rand.nextInt(3) + 2)
            : 1;
        if (meta == 8) {
            System.out.println("meta 8" + drop.getDisplayName());
            world.setBlockMetadataWithNotify(x, y - 1, z, 0, 3);
            world.setBlockToAir(x, y, z);
            for (int i = 0; i < numDrops; ++i) {
                this.dropBlockAsItem(world, x, y, z, drop);
            }
        } else if (meta <= 7) {
            if (world.getBlockMetadata(x, y + 1, z) == 8) {
                world.setBlockToAir(x, y + 1, z);
                this.dropBlockAsItem(world, x, y + 1, z, drop);
            } else {
                world.setBlockToAir(x, y, z);
            }
        } else {
            world.setBlockToAir(x, y, z);
            world.setBlockToAir(x, y - 1, z);
        }
    }

    @SubscribeEvent
    public void handleCoconutBreakEvent(final BlockEvent.HarvestDropsEvent event) {
        final World world = event.world;
        final Block block = event.block;
        final int x = event.x;
        final int y = event.y;
        final int z = event.z;
        final EntityPlayer player = event.harvester;
        if (world.isRemote) {
            return;
        }
        if (block != TCBlockRegistry.coconut) {
            return;
        }
        final ItemStack handItemStack = this.getPlayerItem(world, player);
        Item inHand;
        if (handItemStack != null) {
            inHand = handItemStack.getItem();
        } else {
            inHand = null;
        }
        ItemStack drop = null;
        if (inHand != null
            && (inHand instanceof ItemSword || inHand.getItemUseAction(new ItemStack(inHand)) == EnumAction.block)) {
            drop = new ItemStack((Item) TCItemRegistry.coconutChunk);
        } else {
            drop = new ItemStack(block, 1, 0);
        }
        final int numDrops = (drop.getItem() == TCItemRegistry.coconutChunk) ? (world.rand.nextInt(3) + 2) : 1;
        world.setBlockToAir(x, y, z);
        for (int i = 0; i < numDrops; ++i) {
            this.dropBlockAsItem(world, x, y, z, drop);
        }
    }

    protected void dropBlockAsItem(final World p_149642_1_, final int p_149642_2_, final int p_149642_3_,
        final int p_149642_4_, final ItemStack p_149642_5_) {
        if (!p_149642_1_.isRemote && p_149642_1_.getGameRules()
            .getGameRuleBooleanValue("doTileDrops")) {
            final float f = 0.7f;
            final double d0 = p_149642_1_.rand.nextFloat() * f + (1.0f - f) * 0.5;
            final double d2 = p_149642_1_.rand.nextFloat() * f + (1.0f - f) * 0.5;
            final double d3 = p_149642_1_.rand.nextFloat() * f + (1.0f - f) * 0.5;
            final EntityItem entityitem = new EntityItem(
                p_149642_1_,
                p_149642_2_ + d0,
                p_149642_3_ + d2,
                p_149642_4_ + d3,
                p_149642_5_);
            entityitem.delayBeforeCanPickup = 10;
            p_149642_1_.spawnEntityInWorld((Entity) entityitem);
        }
    }

    private ItemStack getPlayerItem(final World world, final EntityPlayer player) {
        if (player == null) {
            return null;
        }
        final ItemStack inHand = player.inventory.getStackInSlot(player.inventory.currentItem);
        return inHand;
    }
}
