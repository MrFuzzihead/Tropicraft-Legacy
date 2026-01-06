package net.tropicraft.entity.underdasea;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public abstract class EntityTropicraftWaterMob extends EntityWaterMob {

    protected WaterMobType type;
    public Entity targetEntity;
    protected float climbFactor;
    protected float horFactor;
    protected int hyperness;
    protected int fickleness;
    public boolean isSurfacing;
    public boolean reachedTarget;
    public float important1;
    public float randomMotionSpeed;
    public float important2;
    protected float randomMotionVecX;
    protected float randomMotionVecY;
    protected float randomMotionVecZ;
    public int targetHeight;
    public int surfaceTick;
    public int targetHeightTick;
    public boolean isCatchable;
    public int outOfWaterTick;
    public double fishingMaxLookDist;
    public int fishingImmediateDispatchOdds;
    public int fishingInterestOdds;
    public int fishingBreakLineOdds;
    public float fishingApproachSpeed;
    public float fishingEscapeSpeed;
    public static boolean fishingDebug;
    public List<String> fishingLog;
    public boolean fishingIsInterested;

    public EntityTropicraftWaterMob(final World world) {
        super(world);
        this.targetHeightTick = 120;
        this.outOfWaterTick = 0;
        this.fishingMaxLookDist = 10.0;
        this.fishingImmediateDispatchOdds = 10;
        this.fishingInterestOdds = 10;
        this.fishingBreakLineOdds = 500;
        this.fishingApproachSpeed = 1.4f;
        this.fishingEscapeSpeed = 2.4f;
        this.fishingLog = new ArrayList<String>();
        this.fishingIsInterested = false;
        this.important1 = 0.0f;
        this.randomMotionSpeed = 0.0f;
        this.important2 = 0.0f;
        this.important2 = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.randomMotionVecX = 0.0f;
        this.randomMotionVecY = 0.0f;
        this.randomMotionVecZ = 0.0f;
        this.reachedTarget = true;
        this.targetHeight = 62;
        this.isSurfacing = false;
        this.surfaceTick = 0;
        this.hyperness = 30;
        this.fickleness = 150;
        this.horFactor = 0.1f;
        this.climbFactor = 0.1f;
        this.experienceValue = 5;
        this.type = WaterMobType.OCEAN_DWELLER;
    }

    public EntityTropicraftWaterMob(final World par1World, final WaterMobType type) {
        this(par1World);
        this.type = type;
    }

    protected abstract int attackStrength();

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            return;
        }
        this.important1 += this.important2;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            final float n = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.1415927410125732);
            this.renderYawOffset = n;
            this.prevRenderYawOffset = n;
            final float n2 = (float) (Math.atan2(this.motionY, f) * 180.0 / 3.1415927410125732);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
        }
        final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationPitch = (float) (Math.atan2(this.motionY, f2) * 180.0 / 3.1415927410125732);
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
        if (this.important1 > 6.283185f) {
            this.important1 -= 6.283185f;
            if (this.rand.nextInt(10) == 0) {
                this.important2 = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
            }
        }
        if (this.surfaceTick != 0) {
            --this.surfaceTick;
        }
        if (!this.isSurfacing) {
            if (this.important1 < 3.141593f) {
                final float f3 = this.important1 / 3.141593f;
                if (f3 > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                }
            } else {
                this.randomMotionSpeed *= 0.95f;
            }
            if (!this.worldObj.isRemote && this.targetEntity == null) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            if (this.targetEntity == null) {
                this.renderYawOffset += (-(float) Math.atan2(this.motionX, this.motionZ) * 180.0f / 3.141593f
                    - this.renderYawOffset) * 0.1f;
                this.rotationYaw = this.renderYawOffset;
            }
        }
        if (!this.isInWater()) {
            if (!this.worldObj.isRemote) {
                this.motionX = 0.0;
                this.motionY *= 0.9800000190734863;
                this.motionZ = 0.0;
            }
            if (this.surfaceTick == 0) {
                this.isSurfacing = false;
            }
            this.setAir(400);
            if (this.isInWater() || this.deathTime == 0) {}
            final Block blockUnder = this.worldObj.getBlock(
                MathHelper.floor_double(this.posX),
                MathHelper.floor_double(this.posY - 1.0),
                MathHelper.floor_double(this.posZ));
            final Block blockAt = this.worldObj.getBlock(
                MathHelper.floor_double(this.posX),
                MathHelper.floor_double(this.posY),
                MathHelper.floor_double(this.posZ));
            if (!blockUnder.getMaterial()
                .isSolid()) {
                if (blockAt == Blocks.air) {
                    this.motionY -= 0.08;
                }
            } else {
                this.motionY += 0.2;
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
            }
        }
    }

    public boolean isAIEnabled() {
        return false;
    }

    protected void updateEntityActionState() {
        ++this.entityAge;
        if (this.targetEntity != null) {
            if (this.targetEntity.isDead || !this.targetEntity.isInWater()
                || this.getDistanceToEntity(this.targetEntity) > 10.0f) {
                this.targetEntity = null;
            } else if (this.inWater) {
                if (this.targetEntity.posY > this.type.shallowDepth || !this.targetEntity.isInWater()) {
                    this.targetEntity = null;
                }
                if (this.attackTime == 0 && this.targetEntity != null) {
                    this.attackEntity(this.targetEntity);
                }
                if (this.attackTime > 0) {
                    this.motionZ = Math.cos(this.renderYawOffset / 57.26) * this.horFactor;
                    this.motionX = Math.sin(this.renderYawOffset / 57.26) * this.horFactor;
                    this.motionY = 0.0;
                }
            }
        }
        if (this.targetEntity == null) {
            this.targetEntity = (Entity) this.getAttackTarget();
            if (this.rand.nextInt(this.hyperness) == 0 || !this.inWater
                || (this.randomMotionVecX == 0.0f && this.randomMotionVecY == 0.0f && this.randomMotionVecZ == 0.0f)) {
                final float f = this.rand.nextFloat() * 3.141593f * 2.0f;
                this.randomMotionVecX = MathHelper.cos(f) * this.horFactor;
                this.randomMotionVecZ = MathHelper.sin(f) * this.horFactor;
            }
            if (!this.isSurfacing && this.isInWater()) {
                if (!this.reachedTarget) {
                    --this.targetHeightTick;
                }
                if (this.targetHeightTick == 0) {
                    this.targetHeightTick = 120;
                    this.reachedTarget = true;
                }
                if ((this.posY <= this.targetHeight + 0.15 && this.posY >= this.targetHeight - 0.15)
                    || this.reachedTarget) {
                    this.reachedTarget = true;
                    this.targetHeightTick = 120;
                    this.randomMotionVecY = 0.0f;
                    if (this.rand.nextInt(this.fickleness) == 0) {
                        this.reachedTarget = this.getTargetHeight();
                    }
                } else if (this.posY > this.targetHeight && !this.reachedTarget) {
                    this.randomMotionVecY = -this.climbFactor;
                } else if (this.posY < this.targetHeight && !this.reachedTarget) {
                    this.randomMotionVecY = this.climbFactor;
                }
            }
        }
    }

    protected int getDistanceToBase(final int i, final int height) {
        if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), height - i, MathHelper.floor_double(this.posZ))
            .getMaterial()
            .isLiquid()) {
            return this.getDistanceToBase(i + 1, height);
        }
        return i;
    }

    protected int getDistanceToSurface(final int i) {
        if (this.worldObj
            .getBlock(MathHelper.floor_double(this.posX), (int) this.posY + i, MathHelper.floor_double(this.posZ))
            .getMaterial()
            .isLiquid()) {
            return this.getDistanceToSurface(i + 1);
        }
        return i;
    }

    protected boolean getTargetHeight() {
        if (!this.isWet() || this.isSurfacing) {
            return true;
        }
        if (this.posY < this.type.shallowDepth + 1) {
            int depth = this.getDistanceToBase(0, 62);
            if (depth < 1) {
                this.targetHeight = (int) this.posY;
                return false;
            }
            if (depth < 63 - this.type.shallowDepth) {
                this.targetHeight = 63 - this.rand.nextInt(depth + 1);
                if (this.targetHeight == 63) {
                    --this.targetHeight;
                }
                return false;
            }
            depth -= 63 - this.type.shallowDepth;
            if (depth > this.type.deepDepth) {
                depth = this.type.deepDepth;
            }
            this.targetHeight = this.type.shallowDepth - this.rand.nextInt(depth + 1);
            return false;
        } else {
            int height = this.getDistanceToSurface(0);
            height += (int) this.posY;
            final int depth2 = this.getDistanceToBase(0, height - 1);
            if (depth2 < 1) {
                this.targetHeight = (int) this.posY;
                return false;
            }
            int i1 = this.rand.nextInt(depth2);
            if (i1 == 0) {
                i1 = 1;
            }
            this.targetHeight = height - i1;
            return false;
        }
    }

    public void setAttackHeading(double d, double d1, double d2, final float f, final float f1) {
        final float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
    }

    protected void attackEntity(final Entity entity) {
        final double d = entity.posX - this.posX;
        final double d2 = entity.posZ - this.posZ;
        this.faceEntity(this.targetEntity, 360.0f, 360.0f);
        final double d3 = entity.posY - this.posY;
        final float f1 = MathHelper.sqrt_double(d * d + d2 * d2) * 0.2f;
        this.setAttackHeading(d, d3, d2, this.horFactor, 12.0f);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean isInWater() {
        return super.isInWater();
    }

    public boolean getCanSpawnHere() {
        return this.posY > 45.0 && this.posY < 63.0 && super.getCanSpawnHere();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10.0);
    }

    public void applyEntityCollision(final Entity entity) {
        super.applyEntityCollision(entity);
        if (this.targetEntity != null && entity.equals((Object) this.targetEntity) && this.attackStrength() != 0) {
            this.targetEntity
                .attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this), (float) this.attackStrength());
            this.attackTime = 60;
        }
    }

    public WaterMobType getType() {
        return this.type;
    }

    public int getTalkInterval() {
        return 120;
    }

    protected boolean canDespawn() {
        return false;
    }

    protected int getExperiencePoints(final EntityPlayer entityplayer) {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    public void moveEntityWithHeading(final float f, final float f1) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    protected String getLivingSound() {
        return null;
    }

    protected String getHurtSound() {
        return null;
    }

    protected String getDeathSound() {
        return null;
    }

    protected float getSoundVolume() {
        return 0.4f;
    }

    public void entityInit() {
        this.dataWatcher.addObject(26, -1);
        super.entityInit();
    }

    public int getHookID() {
        return this.dataWatcher.getWatchableObjectInt(26);
    }

    public void setHookID(final int i) {
        this.dataWatcher.updateObject(26, i);
    }

    public void onFishingUpdate() {
        if (this.getHookID() != -1) {
            final List<Entity> l = (List<Entity>) this.worldObj.loadedEntityList;
            for (final Entity e : l) {
                if (e.getEntityId() == this.getHookID()) {
                    if (e.isDead) {
                        this.setHookID(-1);
                    }
                    if (this.rand.nextInt(this.fishingBreakLineOdds) != 0 || !this.worldObj.isRemote) {}
                }
            }
        }
    }

    public void fishDebug(final String s) {
        try {
            if (EntityTropicraftWaterMob.fishingDebug && !this.worldObj.isRemote) {
                final String out = "<" + this.getEntityString()
                    .split("\\.")[1] + this.getEntityId() + ">: " + s;
                if (this.fishingLog.contains(out)) {
                    return;
                }
                System.out.println(out);
                this.fishingLog.add(out);
            }
        } catch (Exception ex) {}
    }

    public void faceEntity(final Entity par1Entity, final float par2, final float par3) {
        final double d0 = par1Entity.posX - this.posX;
        final double d2 = par1Entity.posZ - this.posZ;
        double d3;
        if (par1Entity instanceof EntityLiving) {
            final EntityLiving entityliving = (EntityLiving) par1Entity;
            d3 = entityliving.posY + entityliving.getEyeHeight() - (this.posY + this.getEyeHeight());
        } else {
            d3 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0 - (this.posY + this.getEyeHeight());
        }
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final float f2 = (float) (Math.atan2(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f3 = (float) (-(Math.atan2(d3, d4) * 180.0 / 3.141592653589793));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f3, par3);
        this.renderYawOffset = this.updateRotation(this.renderYawOffset, f2, par2);
        this.rotationYaw = this.renderYawOffset;
    }

    public void faceEntity(final EntityLivingBase entSource, final Entity par1Entity, final float par2,
        final float par3) {
        final double d0 = par1Entity.posX - entSource.posX;
        final double d2 = par1Entity.posZ - entSource.posZ;
        double d3;
        if (par1Entity instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase) par1Entity;
            d3 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (entSource.posY + entSource.getEyeHeight());
        } else {
            d3 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0
                - (entSource.posY + entSource.getEyeHeight());
        }
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final float f2 = (float) (Math.atan2(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f3 = (float) (-(Math.atan2(d3, d4) * 180.0 / 3.141592653589793));
        entSource.rotationPitch = this.updateRotation(entSource.rotationPitch, f3, par3);
        entSource.rotationYaw = this.updateRotation(entSource.rotationYaw, f2, par2);
    }

    public float updateRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float f3 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (f3 > p_70663_3_) {
            f3 = p_70663_3_;
        }
        if (f3 < -p_70663_3_) {
            f3 = -p_70663_3_;
        }
        return p_70663_1_ + f3;
    }

    static {
        EntityTropicraftWaterMob.fishingDebug = false;
    }

    public enum WaterMobType {

        SURFACE_TROPICS(90, 88),
        SURFACE_OVERWORLD(63, 62),
        OCEAN_DWELLER(62, 32);

        final int shallowDepth;
        final int deepDepth;

        private WaterMobType(final int shallowDepth, final int deepDepth) {
            this.shallowDepth = shallowDepth;
            this.deepDepth = deepDepth;
        }

        public int getShallowDepth() {
            return this.shallowDepth;
        }

        public int getDeepDepth() {
            return this.deepDepth;
        }
    }
}
