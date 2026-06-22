package net.tropicraft.item.placeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.item.ItemTropicraftColored;
import net.tropicraft.util.ColorHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChair extends ItemTropicraftColored {

    @SideOnly(Side.CLIENT)
    private IIcon overlayIcon;

    public ItemChair() {
        this.setTextureName("chair");
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer entityplayer) {
        final int color = ColorHelper.getColorFromDamage(itemstack.getItemDamage());
        final float f = 1.0f;
        final float f2 = entityplayer.prevRotationPitch
            + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
        final float f3 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
        final double d = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * f;
        final double d2 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * f
            + 1.62
            - entityplayer.yOffset;
        final double d3 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * f;
        final Vec3 vec3d = Vec3.createVectorHelper(d, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.01745329f - 3.141593f);
        final float f5 = MathHelper.sin(-f3 * 0.01745329f - 3.141593f);
        final float f6 = -MathHelper.cos(-f2 * 0.01745329f);
        final float f7 = MathHelper.sin(-f2 * 0.01745329f);
        final float f8 = f5 * f6;
        final float f9 = f7;
        final float f10 = f4 * f6;
        final double d4 = 5.0;
        final Vec3 vec3d2 = vec3d.addVector(f8 * d4, f9 * d4, f10 * d4);
        final MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3d, vec3d2, true);
        if (movingobjectposition == null) {
            return itemstack;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
            && movingobjectposition.sideHit == 1) {
            final int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            final int k = movingobjectposition.blockZ;
            if (!world.isRemote) {
                if (world.getBlock(i, j, k) == Blocks.snow) {
                    --j;
                }
                world.spawnEntityInWorld(
                    (Entity) new EntityChair(world, (double) i, j + 1.01, (double) k, color, entityplayer));
            }
            if (!entityplayer.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }
        }
        return itemstack;
    }
}
