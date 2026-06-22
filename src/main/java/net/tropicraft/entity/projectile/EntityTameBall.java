package net.tropicraft.entity.projectile;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import CoroUtil.componentAI.AITamable;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.entity.EntityThrowableUsefull;
import CoroUtil.util.CoroUtilEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.ParticleRegistry;
import extendedrenderer.particle.behavior.ParticleBehaviors;
import extendedrenderer.particle.entity.EntityRotFX;

public class EntityTameBall extends EntityThrowableUsefull {

    public int ticksInAir;
    @SideOnly(Side.CLIENT)
    public boolean hasDeathTicked;
    @SideOnly(Side.CLIENT)
    public ParticleBehaviors pm;

    public EntityTameBall(final World world) {
        super(world);
    }

    @Override
    protected void entityInit() {

    }

    public EntityTameBall(final World world, final EntityLivingBase entityliving) {
        super(world, entityliving);
        final float speed = 0.7f;
        final float f = 0.4f;
        this.motionX = -MathHelper.sin(-this.rotationYaw / 180.0f * 3.1415927f)
            * MathHelper.cos(-this.rotationPitch / 180.0f * 3.1415927f)
            * f;
        this.motionZ = MathHelper.cos(-this.rotationYaw / 180.0f * 3.1415927f)
            * MathHelper.cos(-this.rotationPitch / 180.0f * 3.1415927f)
            * f;
        this.motionY = -MathHelper.sin((-this.rotationPitch + this.func_70183_g()) / 180.0f * 3.1415927f) * f;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed, 1.0f);
    }

    public EntityTameBall(final World world, final double d, final double d1, final double d2) {
        super(world, d, d1, d2);
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            ++this.ticksInAir;
            if (this.ticksInAir > 80) {
                this.setDead();
            }
        } else {
            if (this.pm == null) {
                this.pm = new ParticleBehaviors(Vec3.createVectorHelper(this.posX, this.posY, this.posZ));
                this.pm.rateAlpha = 0.02f;
                this.pm.rateBrighten = 0.02f;
                this.pm.tickSmokifyTrigger = 20;
            }
            this.tickAnimate();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }

    protected float getGravityVelocity() {
        return 0.0f;
    }

    public MovingObjectPosition tickEntityCollision(final Vec3 vec3, final Vec3 vec31) {
        MovingObjectPosition movingobjectposition = null;
        Entity entity = null;
        final List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
            (Entity) this,
            this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ)
                .expand(0.5, 1.0, 0.5));
        final double d0 = 0.0;
        final EntityLivingBase entityliving = this.getThrower();
        for (int j = 0; j < list.size(); ++j) {
            final Entity entity2 = (Entity) list.get(j);
            if (entity2.canBeCollidedWith() && entity2 != entityliving && this.ticksInAir >= 4) {
                entity = entity2;
                break;
            }
        }
        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        return movingobjectposition;
    }

    protected void onImpact(final MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entityHit != null && !this.worldObj.isRemote
            && movingobjectposition.entityHit instanceof ICoroAI
            && this.getThrower() instanceof EntityPlayer) {
            final AITamable tamable = ((ICoroAI) movingobjectposition.entityHit).getAIAgent().jobMan
                .getPrimaryJob().tamable;
            if (!tamable.isTame()) {
                tamable.tameBy(CoroUtilEntity.getName((Entity) this.getThrower()));
                this.setDead();
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
    public void tickAnimate() {
        for (int amount = 3 / (Minecraft.getMinecraft().gameSettings.particleSetting + 1), i = 0; i < amount; ++i) {
            final double speed = 0.01;
            final EntityRotFX entityfx = this.pm.spawnNewParticleIconFX(
                this.worldObj,
                ParticleRegistry.smoke,
                this.posX + this.rand.nextDouble(),
                this.posY + 0.2 + this.rand.nextDouble() * 0.2,
                this.posZ + this.rand.nextDouble(),
                (this.rand.nextDouble() - this.rand.nextDouble()) * speed,
                0.03,
                (this.rand.nextDouble() - this.rand.nextDouble()) * speed);
            final ParticleBehaviors pm = this.pm;
            ParticleBehaviors.setParticleRandoms(entityfx, true, true);
            final ParticleBehaviors pm2 = this.pm;
            ParticleBehaviors.setParticleFire(entityfx);
            entityfx.setMaxAge(120 + this.rand.nextInt(90));
            entityfx.setScale(0.55f + 0.2f * this.rand.nextFloat());
            entityfx.spawnAsWeatherEffect();
            this.pm.particles.add(entityfx);
        }
    }

    @SideOnly(Side.CLIENT)
    public void tickDeath() {
        if (!this.hasDeathTicked) {
            this.hasDeathTicked = true;
            if (this.pm == null) {
                return;
            }
            for (int amount = 20 / (Minecraft.getMinecraft().gameSettings.particleSetting + 1), i = 0; i
                < amount; ++i) {
                final double speed = 0.01;
                final EntityRotFX entityfx = this.pm.spawnNewParticleIconFX(
                    this.worldObj,
                    ParticleRegistry.smoke,
                    this.posX + this.rand.nextDouble(),
                    this.posY + 0.2 + this.rand.nextDouble() * 0.2,
                    this.posZ + this.rand.nextDouble(),
                    (this.rand.nextDouble() - this.rand.nextDouble()) * speed,
                    0.03,
                    (this.rand.nextDouble() - this.rand.nextDouble()) * speed);
                final ParticleBehaviors pm = this.pm;
                ParticleBehaviors.setParticleRandoms(entityfx, true, true);
                final ParticleBehaviors pm2 = this.pm;
                ParticleBehaviors.setParticleFire(entityfx);
                entityfx.setMaxAge(220 + this.rand.nextInt(90));
                entityfx.spawnAsWeatherEffect();
                this.pm.particles.add(entityfx);
            }
        }
    }

    @Override
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_,
        float p_70186_8_) {

    }
}
