package net.tropicraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWaterWand extends ItemTropicraft {

    public ItemWaterWand() {
        this.setMaxStackSize(1);
        this.setMaxDamage(2000);
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer player) {
        final double inc = 0.2617993877991494;
        if (!world.isRemote) {
            for (double lat = 0.0; lat < 6.283185307179586; lat += inc) {
                for (double lng = 0.0; lng < 6.283185307179586; lng += inc) {
                    for (double len = 1.0; len < 3.0; len += 0.5) {
                        final int x1 = (int) (Math.cos(lat) * len);
                        final int z1 = (int) (Math.sin(lat) * len);
                        final int y1 = (int) (Math.sin(lng) * len);
                        if (!this.removeWater(
                            world,
                            itemstack,
                            player,
                            (int) player.posX + x1,
                            (int) player.posY + y1,
                            (int) player.posZ + z1)) {
                            break;
                        }
                    }
                }
            }
        }
        return itemstack;
    }

    private boolean removeWater(final World world, final ItemStack itemstack, final EntityPlayer player, final int x,
        final int y, final int z) {
        if (!world.isRemote) {
            if (world.getBlock(x, y, z)
                .getMaterial() == Material.water) {
                itemstack.damageItem(1, (EntityLivingBase) player);
                world.setBlockToAir(x, y, z);
                return true;
            }
            if (world.isAirBlock(x, y, z)) {
                return true;
            }
        }
        return false;
    }
}
