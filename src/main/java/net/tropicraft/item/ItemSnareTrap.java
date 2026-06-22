package net.tropicraft.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.entity.placeable.EntitySnareTrap;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemSnareTrap extends ItemTropicraft {

    public ItemSnareTrap() {
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World world, int x, final int y,
        int z, final int side, final float offsetX, final float offsetY, final float offsetZ) {
        final ForgeDirection dir = ForgeDirection.getOrientation(side);
        if (dir == ForgeDirection.DOWN || dir == ForgeDirection.UP) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        x += dir.offsetX;
        z += dir.offsetZ;
        final int height = this.getHeight(world, x, y, z);
        if (height < 3 || height > 6) {
            return false;
        }
        final List objs = world.getEntitiesWithinAABB(
            (Class) EntitySnareTrap.class,
            AxisAlignedBB.getBoundingBox(
                (double) x,
                (double) (y - height + 1),
                (double) z,
                (double) (x + 1),
                (double) (y - height + 1 + 6),
                (double) (z + 1)));
        if (!objs.isEmpty()) {
            return false;
        }
        final EntitySnareTrap trap = new EntitySnareTrap(world, x, y - height + 1, z, height, dir);
        world.spawnEntityInWorld((Entity) trap);
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return true;
    }

    private int getHeight(final World world, final int x, int y, final int z) {
        int height;
        for (height = 0; y >= 0 && world.getBlock(x, y, z) == Blocks.air; --y, ++height) {}
        return height;
    }
}
