package net.tropicraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.tropicraft.entity.placeable.EntityWallStarfish;

public class ItemShell extends ItemTropicraftMulti {

    public ItemShell(final String[] names) {
        super(names);
    }

    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World,
        final int par4, final int par5, final int par6, final int par7, final float par8, final float par9,
        final float par10) {
        if (par1ItemStack.getItemDamage() != 4) {
            return false;
        }
        if (par7 == 0) {
            return false;
        }
        if (par7 == 1) {
            return false;
        }
        final int i1 = Direction.facingToDirection[par7];
        final EntityWallStarfish entityhanging = new EntityWallStarfish(par3World);
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        }
        if (entityhanging != null && entityhanging.onValidSurface()) {
            if (!par3World.isRemote) {
                par3World.spawnEntityInWorld((Entity) entityhanging);
            }
            --par1ItemStack.stackSize;
        }
        return true;
    }
}
