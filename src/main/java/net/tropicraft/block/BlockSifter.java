package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.tropicraft.block.tileentity.*;
import net.tropicraft.factory.*;
import net.tropicraft.registry.*;

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
