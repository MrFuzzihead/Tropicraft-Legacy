package net.tropicraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityTropicraftLeafballNew;
import net.tropicraft.util.TropicraftNames;

import CoroUtil.entity.ItemTropicraftLeafball;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftLeafballNew extends ItemTropicraftLeafball {

    public ItemTropicraftLeafballNew() {
        this.maxStackSize = 16;
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer entityplayer) {
        if (!entityplayer.capabilities.isCreativeMode) {
            --itemstack.stackSize;
        }
        world.playSoundAtEntity(
            entityplayer,
            "random.bow",
            0.5f,
            0.4f / (ItemTropicraftLeafballNew.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityTropicraftLeafballNew(world, entityplayer));
        }
        return itemstack;
    }

    public String getUnlocalizedName() {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    public String getUnlocalizedName(final ItemStack itemStack) {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1));
    }
}
