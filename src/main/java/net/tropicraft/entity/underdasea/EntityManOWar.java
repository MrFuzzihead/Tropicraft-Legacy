package net.tropicraft.entity.underdasea;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityManOWar extends EntityWaterMob {

    public float important1;
    protected float randomMotionSpeed;
    protected float important2;
    protected float randomMotionVecX;
    protected float randomMotionVecY;
    protected float randomMotionVecZ;

    public EntityManOWar(final World world) {
        super(world);
        this.important1 = 0.0f;
        this.randomMotionSpeed = 0.0f;
        this.important2 = 0.0f;
        this.randomMotionVecX = 0.0f;
        this.randomMotionVecY = 0.0f;
        this.randomMotionVecZ = 0.0f;
        this.important2 = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.setSize(0.6f, 0.6f);
        this.experienceValue = 7;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(5.0);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public byte getAttackStrength() {
        switch (this.worldObj.difficultySetting) {
            case EASY: {
                return 1;
            }
            case NORMAL: {
                return 2;
            }
            case HARD: {
                return 3;
            }
            default: {
                return 0;
            }
        }
    }

    protected Entity getTarget() {
        return null;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            return;
        }
        if (this.inWater) {
            if (this.attackTime == 0) {
                final List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                    (Entity) this,
                    this.boundingBox.expand(2.0, 8.0, 2.0)
                        .getOffsetBoundingBox(0.0, -8.0, 0.0));
                for (int i = 0; i < list.size(); ++i) {
                    final Entity ent = (Entity) list.get(i);
                    if (!(ent instanceof EntityManOWar) && ent instanceof EntityLiving
                        && ((EntityLiving) ent).isInWater()) {
                        final byte byte0 = this.getAttackStrength();
                        ((EntityLiving) ent).attackEntityFrom(DamageSource.drown, (float) byte0);
                        this.attackTime = 60;
                    }
                }
            }
            this.important1 += this.important2;
            if (this.important1 > 6.283185f) {
                this.important1 -= 6.283185f;
                if (this.rand.nextInt(10) == 0) {
                    this.important2 = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
                }
            }
            if (this.important1 < 3.141593f) {
                final float f = this.important1 / 3.141593f;
                if (f > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                }
            } else {
                this.randomMotionSpeed *= 0.95f;
            }
            if (!this.worldObj.isRemote) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-(float) Math.atan2(this.motionX, this.motionZ) * 180.0f / 3.141593f
                - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
        } else {
            if (!this.worldObj.isRemote) {
                this.motionX = 0.0;
                this.motionY *= 0.9800000190734863;
                this.motionZ = 0.0;
            }
            if (this.onGround && this.deathTime == 0) {
                this.attackEntityFrom(DamageSource.drown, 1.0f);
                int d = 1;
                int e = 1;
                if (this.rand.nextInt(2) == 0) {
                    d = -1;
                }
                if (this.rand.nextInt(2) == 0) {
                    e = -1;
                }
                this.motionZ = this.rand.nextFloat() * 0.2f * d;
                this.motionX = this.rand.nextFloat() * 0.2f * e;
            }
            if (!this.inWater) {
                this.motionY -= 0.08;
            }
        }
    }

    protected void updateEntityActionState() {
        if (this.rand.nextInt(150) == 0 || !this.inWater
            || (this.randomMotionVecX == 0.0f && this.randomMotionVecY == 0.0f && this.randomMotionVecZ == 0.0f)) {
            final float f = this.rand.nextFloat() * 3.141593f * 2.0f;
            this.randomMotionVecX = MathHelper.cos(f) * 0.025f;
            this.randomMotionVecZ = MathHelper.sin(f) * 0.025f;
        }
        if (this.inWater) {
            if (this.posY < 62.5) {
                this.randomMotionVecY = 0.05f;
            }
            if (this.posY >= 62.5) {
                this.randomMotionVecY = 0.0f;
            }
        }
    }

    public void onDeath(final DamageSource d) {
        super.onDeath(d);
        if (!this.worldObj.isRemote) {
            for (int numDrops = 3 + this.rand.nextInt(1), i = 0; i < numDrops; ++i) {
                this.dropItem(Items.slime_ball, 1);
            }
        }
    }

    public boolean getCanSpawnHere() {
        return !this.worldObj.checkBlockCollision(this.boundingBox);
    }

    public int getTalkInterval() {
        return 120;
    }

    protected boolean canDespawn() {
        return true;
    }

    protected int getExperiencePoints(final EntityPlayer entityplayer) {
        return 3 + this.worldObj.rand.nextInt(3);
    }
}
