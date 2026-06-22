package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemBambooDoor extends ItemTropicraft {

    public ItemBambooDoor() {
        this.maxStackSize = 1;
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
    }

    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World,
        final int par4, int par5, final int par6, final int par7, final float par8, final float par9,
        final float par10) {
        if (par7 != 1) {
            return false;
        }
        ++par5;
        final Block block = (Block) TCBlockRegistry.bambooDoor;
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)
            || !par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack)) {
            return false;
        }
        if (!block.canPlaceBlockAt(par3World, par4, par5, par6)) {
            return false;
        }
        final int i1 = MathHelper.floor_double((par2EntityPlayer.rotationYaw + 180.0f) * 4.0f / 360.0f - 0.5) & 0x3;
        ItemDoor.placeDoorBlock(par3World, par4, par5, par6, i1, block);
        --par1ItemStack.stackSize;
        return true;
    }
}
