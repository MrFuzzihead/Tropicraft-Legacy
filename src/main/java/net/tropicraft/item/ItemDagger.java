package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDagger extends ItemTropicraft {

    private float weaponDamage;

    public ItemDagger(final Item.ToolMaterial enumtoolmaterial) {
        this.maxStackSize = 1;
        this.setMaxDamage(enumtoolmaterial.getMaxUses());
        this.weaponDamage = 4.0f + enumtoolmaterial.getDamageVsEntity();
    }

    public float getDigSpeed(final ItemStack itemstack, final Block block, final int metadata) {
        return (block != Blocks.web) ? 1.5f : 15.0f;
    }

    public boolean hitEntity(final ItemStack itemstack, final EntityLivingBase entityliving,
        final EntityLivingBase entityliving1) {
        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public boolean onBlockDestroyed(final ItemStack par1ItemStack, final World par2World, final Block block,
        final int par4, final int par5, final int par6, final EntityLivingBase par7EntityLiving) {
        return true;
    }

    public boolean isFull3D() {
        return true;
    }

    public EnumAction getItemUseAction(final ItemStack itemstack) {
        return EnumAction.block;
    }

    public int getMaxItemUseDuration(final ItemStack itemstack) {
        return 72000;
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    public boolean canHarvestBlock(final Block block, final ItemStack stack) {
        return block == Blocks.web;
    }
}
