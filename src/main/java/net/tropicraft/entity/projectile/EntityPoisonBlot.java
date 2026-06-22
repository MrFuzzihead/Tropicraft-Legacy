package net.tropicraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPoisonBlot extends EntityThrowable {

    public EntityPoisonBlot(final World par1World) {
        super(par1World);
    }

    public EntityPoisonBlot(final World par1World, final EntityLivingBase thrower) {
        super(par1World, thrower);
    }

    protected void onImpact(final MovingObjectPosition mop) {
        if (mop.entityHit != null && mop.entityHit instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) mop.entityHit;
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 240, 0));
            this.setDead();
        }
    }
}
