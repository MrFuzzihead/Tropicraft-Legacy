package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntitySifter;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class BlockSifter extends BlockTropicraft implements ITileEntityProvider {

    public BlockSifter() {
        super(Material.wood);
    }

    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return TileEntityFactory.getSifterTE();
    }

    public int getRenderBlockPass() {
        return 0;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return true;
    }

    public Item getItemDropped(final int id, final Random random, final int j) {
        return Item.getItemFromBlock((Block) TCBlockRegistry.sifter);
    }

    public boolean onBlockActivated(final World world, final int i, final int j, final int k,
        final EntityPlayer entityplayer, final int d, final float f1, final float f2, final float f3) {
        if (world.isRemote) {
            return true;
        }
        final ItemStack stack = entityplayer.inventory.getCurrentItem();
        final TileEntitySifter tileentitysifta = (TileEntitySifter) world.getTileEntity(i, j, k);
        if (tileentitysifta != null && stack != null && !tileentitysifta.isSifting()) {
            final Item helditem = stack.getItem();
            if (helditem == Item.getItemFromBlock((Block) Blocks.sand)
                || (helditem == TCItemRegistry.ore && stack.getItemDamage() == 5)
                || (helditem == Item.getItemFromBlock((Block) TCBlockRegistry.mineralSands)
                    && stack.getItemDamage() == 3)) {
                final ItemStack getCurrentEquippedItem = entityplayer.getCurrentEquippedItem();
                --getCurrentEquippedItem.stackSize;
                if (helditem == TCItemRegistry.ore) {
                    final float percent = this.getTagCompound(stack)
                        .getFloat("AmtRefined");
                    tileentitysifta.setSifting(
                        true,
                        (helditem == Item.getItemFromBlock((Block) Blocks.sand)) ? 1
                            : ((helditem == Item.getItemFromBlock((Block) TCBlockRegistry.mineralSands)) ? 2 : 3),
                        percent);
                } else {
                    tileentitysifta.setSifting(
                        true,
                        (helditem == Item.getItemFromBlock((Block) Blocks.sand)) ? 1
                            : ((helditem == Item.getItemFromBlock((Block) TCBlockRegistry.mineralSands)) ? 2 : 3),
                        -1.0f);
                }
            }
        }
        return true;
    }

    public NBTTagCompound getTagCompound(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }
}
