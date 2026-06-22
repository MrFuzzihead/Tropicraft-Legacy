package net.tropicraft.entity.projectile;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

import CoroUtil.componentAI.ICoroAI;
import CoroUtil.entity.EntityThrowableUsefull;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.entity.EntityIconFX;
import extendedrenderer.particle.entity.EntityRotFX;

public class EntityTropicraftLeafballNew extends EntityThrowableUsefull {

    public int ticksInAir;
    @SideOnly(Side.CLIENT)
    public boolean hasDeathTicked;

    public EntityTropicraftLeafballNew(final World world) {
        super(world);
    }

    @Override
    protected void entityInit() {

    }

    public EntityTropicraftLeafballNew(final World world, final EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public EntityTropicraftLeafballNew(final World world, final double d, final double d1, final double d2) {
        super(world, d, d1, d2);
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            ++this.ticksInAir;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }

    public MovingObjectPosition tickEntityCollision(final Vec3 vec3, final Vec3 vec31) {
        MovingObjectPosition movingobjectposition = null;
        Entity entity = null;
        final List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ)
                .expand(1.0, 1.0, 1.0));
        final double d0 = 0.0;
        final EntityLivingBase entityliving = this.getThrower();
        for (Object o : list) {
            final Entity entity2 = (Entity) o;
            if (entity2.canBeCollidedWith() && this.ticksInAir >= 2) {
                final float f = 0.3f;
                entity = entity2;
            }
        }
        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        return movingobjectposition;
    }

    protected void onImpact(final MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entityHit != null && !this.worldObj.isRemote) {
            final byte byte0 = 2;
            if (movingobjectposition.entityHit instanceof ICoroAI
                && (this.getThrower() == null || this.getThrower() instanceof ICoroAI)) {
                if (this.getThrower() != null && ((ICoroAI) this.getThrower()).getAIAgent().dipl_info
                    .isEnemy(((ICoroAI) movingobjectposition.entityHit).getAIAgent().dipl_info)) {
                    movingobjectposition.entityHit.attackEntityFrom(
                        DamageSource.causeThrownDamage((Entity) this, (Entity) this.getThrower()),
                        (float) byte0);
                }
            } else {
                movingobjectposition.entityHit.attackEntityFrom(
                    DamageSource.causeThrownDamage((Entity) this, (Entity) this.getThrower()),
                    (float) byte0);
            }
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        } else {
            this.tickDeath();
        }
    }

    public void setDead() {
        if (this.worldObj.isRemote) {
            this.tickDeath();
        }
        super.setDead();
    }

    @SideOnly(Side.CLIENT)
    public void tickDeath() {
        if (!this.hasDeathTicked) {
            this.hasDeathTicked = true;
            for (int amount = 20 / (Minecraft.getMinecraft().gameSettings.particleSetting + 1), i = 0; i
                < amount; ++i) {
                final double speed = 0.01;
                final double speedInheritFactor = 0.5;
                final EntityIconFX entityIconFX;
                final EntityRotFX entityfx = entityIconFX = new EntityIconFX(
                    this.worldObj,
                    this.posX,
                    this.posY,
                    this.posZ,
                    TCItemRegistry.leafBall.getIconFromDamage(0));
                entityIconFX.motionX += this.motionX * speedInheritFactor;
                final EntityRotFX entityRotFX = entityfx;
                entityRotFX.motionZ += this.motionZ * speedInheritFactor;
                entityfx.setGravity(0.5f);
                entityfx.rotationYaw = (float) this.rand.nextInt(360);
                entityfx.setMaxAge(30 + this.rand.nextInt(30));
                entityfx.spawnAsWeatherEffect();
            }
        }
    }

    @Override
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_,
        float p_70186_8_) {

    }
}
