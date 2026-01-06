package net.tropicraft.entity.underdasea;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class EntityAmphibian extends EntityCreature {

    public static final int DATAWATCHER_AGE = 16;
    public boolean desireToReturn;
    public boolean returnToLand;
    public boolean reachedTarget;
    public float important1;
    protected float randomMotionSpeed;
    protected float important2;
    protected float randomMotionVecX;
    protected float randomMotionVecY;
    protected float randomMotionVecZ;
    public int targetHeight;
    protected int growthRate;
    protected int fickleness;
    protected float landSpeed;
    public float moveSpeed;
    public static AttributeModifier speedBoostReturnToLand;

    public EntityAmphibian(final World par1World) {
        super(par1World);
        this.important1 = 0.0f;
        this.randomMotionSpeed = 0.0f;
        this.important2 = 0.0f;
        this.randomMotionVecX = 0.0f;
        this.randomMotionVecY = 0.0f;
        this.randomMotionVecZ = 0.0f;
        this.important2 = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.reachedTarget = false;
        this.returnToLand = false;
        this.desireToReturn = true;
        this.targetHeight = 61;
        this.setAmphibianAge(1.0f);
        this.growthRate = 12000;
        this.fickleness = 1200;
        this.landSpeed = 0.25f;
    }

    public EntityAmphibian(final World world, final float age) {
        this(world);
        this.setAmphibianAge(age);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(DATAWATCHER_AGE, 10000);
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("Age", this.getAmphibianAge());
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setAmphibianAge(nbttagcompound.getFloat("Age"));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.ticksExisted % this.growthRate == 0) {
            this.setAmphibianAge(this.getAmphibianAge() + 0.05f);
        }
        if (this.rand.nextInt(this.fickleness / 2) == 0 && this.returnToLand) {
            this.returnToLand = false;
        }
        if (this.isInWater() && this.returnToLand) {
            this.moveSpeed = 1.5f;
        } else if (!this.isInWater()) {
            this.moveSpeed = this.landSpeed;
        } else if (this.isInWater() && !this.returnToLand) {
            if (this.rand.nextInt(this.fickleness) == 0) {
                this.returnToLand = true;
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
            if (this.important1 < 3.141593f) {
                final float f3 = this.important1 / 3.141593f;
                if (f3 > 0.75) {
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
            final float f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-(float) Math.atan2(this.motionX, this.motionZ) * 180.0f / 3.141593f
                - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
        }
    }

    public float getBlockPathWeight(final int i, final int j, final int k) {
        final Block block = this.worldObj.getBlock(i, j - 1, k);
        if (this.returnToLand) {
            if (block instanceof BlockSand) {
                return 10.0f;
            }
            if (block.getMaterial() == Material.grass) {
                return 20.0f;
            }
        } else if (block.getMaterial()
            .isLiquid()) {
                return 20.0f;
            }
        return 0.0f;
    }

    protected void updateEntityActionState() {
        if (this.returnToLand || (!this.returnToLand && !this.isInWater())) {
            super.updateEntityActionState();
        }
        if (!this.returnToLand) {
            if (this.rand.nextInt(70) == 0 || !this.inWater
                || (this.randomMotionVecX == 0.0f && this.randomMotionVecY == 0.0f && this.randomMotionVecZ == 0.0f)) {
                final float f = this.rand.nextFloat() * 3.141593f * 2.0f;
                this.randomMotionVecX = MathHelper.cos(f) * 0.15f;
                this.randomMotionVecZ = MathHelper.sin(f) * 0.15f;
            }
            if (this.isInWater()) {
                if ((this.posY <= this.targetHeight + 0.15 && this.posY >= this.targetHeight - 0.15)
                    || this.reachedTarget) {
                    this.reachedTarget = true;
                    this.randomMotionVecY = 0.0f;
                    if (this.rand.nextInt(300) == 0) {
                        this.getTargetHeight();
                    }
                } else if (this.posY > this.targetHeight && !this.reachedTarget) {
                    this.randomMotionVecY = -0.15f;
                } else if (this.posY < this.targetHeight && !this.reachedTarget) {
                    this.randomMotionVecY = 0.15f;
                }
            }
        }
    }

    protected void getTargetHeight() {
        if (this.isWet()) {
            final int range = this.getDistanceToBase(0, this.worldObj.getActualHeight());
            if (range > 1) {
                this.targetHeight = (int) this.posY;
            }
            this.targetHeight = this.worldObj.getActualHeight() - this.rand.nextInt(range + 1);
            if (this.targetHeight == this.worldObj.getActualHeight()) {
                --this.targetHeight;
            }
            this.reachedTarget = false;
        }
    }

    protected boolean isAIEnabled() {
        return false;
    }

    protected int getDistanceToBase(final int i, final int height) {
        if (i == 5) {
            return i;
        }
        if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), height - i, MathHelper.floor_double(this.posZ))
            .getMaterial()
            .isLiquid()) {
            return this.getDistanceToBase(i + 1, height);
        }
        return i;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean getCanSpawnHere() {
        return this.worldObj.checkNoEntityCollision(this.boundingBox);
    }

    public int getTalkInterval() {
        return 120;
    }

    protected boolean canDespawn() {
        return true;
    }

    protected int getExperiencePoints(final EntityPlayer entityplayer) {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    public void setAmphibianAge(final float age) {
        this.dataWatcher.updateObject(DATAWATCHER_AGE, (int) (age * 10000.0f));
    }

    public float getAmphibianAge() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_AGE) / 10000.0f;
    }

    static {
        EntityAmphibian.speedBoostReturnToLand = new AttributeModifier(
            UUID.randomUUID(),
            "Speed boost return to land",
            0.25,
            0).setSaved(false);
    }
}
