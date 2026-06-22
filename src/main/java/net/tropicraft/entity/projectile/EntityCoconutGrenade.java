package net.tropicraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCoconutGrenade extends EntityThrowable implements IProjectile {

    public EntityCoconutGrenade(final World world) {
        super(world);
    }

    public EntityCoconutGrenade(final World world, final EntityPlayer player) {
        super(world, (EntityLivingBase) player);
    }

    protected void onImpact(final MovingObjectPosition mop) {
        if (!this.worldObj.isRemote) {
            this.worldObj.createExplosion((Entity) this, this.posX, this.posY, this.posZ, 2.4f, true);
        }
    }
}
