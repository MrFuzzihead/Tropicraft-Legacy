package net.tropicraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityFireBall;

public class ItemStaffFireball extends Item {

    public ItemStaffFireball() {
        this.maxStackSize = 1;
        this.setMaxDamage(100);
    }

    public void registerIcons(final IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("tropicraft:staff_fire");
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer entityplayer) {
        if (!entityplayer.capabilities.isCreativeMode) {
            itemstack.damageItem(1, (EntityLivingBase) entityplayer);
        }
        world.playSoundAtEntity(
            (Entity) entityplayer,
            "random.bow",
            0.5f,
            0.4f / (ItemStaffFireball.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!world.isRemote) {
            world.spawnEntityInWorld((Entity) new EntityFireBall(world, (EntityLivingBase) entityplayer));
        }
        return itemstack;
    }
}
