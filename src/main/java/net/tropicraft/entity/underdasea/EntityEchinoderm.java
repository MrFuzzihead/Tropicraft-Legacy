package net.tropicraft.entity.underdasea;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public abstract class EntityEchinoderm extends EntityWaterMob {

    public static final int DW_GROWING_AGE = 16;
    public static final int GROWTH_TICKS = 12000;
    public static final int BREEDING_COOLDOWN = 12000;
    public static final int BREEDING_PROXIMITY = 4;
    public static final int MAX_NEIGHBORS = 6;
    public static final int NEIGHBORHOOD_SIZE = 8;
    public static final int MATE_SCAN_INTERVAL = 100;
    private int prevGrowingAge;
    private int mateScanCooldown;

    public EntityEchinoderm(final World world) {
        super(world);
        this.setEchinodermSize();
    }

    public EntityEchinoderm(final World world, final boolean baby) {
        super(world);
        this.setGrowingAge(baby ? -12000 : 0);
        this.setEchinodermSize();
    }

    public void updateEntityActionState() {
        super.updateEntityActionState();
        this.isJumping = false;
    }

    public boolean attackEntityFrom(final DamageSource source, final float amt) {
        return source != DamageSource.inWall && super.attackEntityFrom(source, amt);
    }

    public boolean isMovementCeased() {
        return true;
    }

    public void knockBack(final Entity ent, final float par2, final double par3, final double par5) {}

    public void onEntityUpdate() {
        super.onEntityUpdate();
        final int growingAge = this.getGrowingAge();
        if (this.worldObj.isRemote) {
            this.motionY = 0.0;
            if (growingAge != this.prevGrowingAge) {
                this.setEchinodermSize();
                this.prevGrowingAge = growingAge;
            }
        } else {
            this.noClip = this
                .func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0, this.posZ);
            if (growingAge < 0) {
                this.setGrowingAge(growingAge + 1);
                this.setEchinodermSize();
            } else if (growingAge > 0) {
                this.setGrowingAge(growingAge - 1);
            }
            if (this.isHorny()) {
                if (this.mateScanCooldown > 0) {
                    --this.mateScanCooldown;
                } else {
                    this.mateScanCooldown = 100;
                    final EntityEchinoderm mate = this.findMate();
                    if (mate != null) {
                        this.setGrowingAge(12000);
                        mate.setGrowingAge(12000);
                        final EntityEchinodermEgg egg = this.createEgg();
                        final double newX = this.posX + 0.5 * (mate.posX - this.posX);
                        final double newY = this.posY + 1.0;
                        final double newZ = this.posZ + 0.5 * (mate.posZ - this.posZ);
                        egg.setLocationAndAngles(newX, newY, newZ, 0.0f, 0.0f);
                        this.worldObj.spawnEntityInWorld((Entity) egg);
                    }
                }
            }
        }
    }

    public abstract EntityEchinodermEgg createEgg();

    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }

    public boolean isHorny() {
        return this.getGrowingAge() == 0;
    }

    private EntityEchinoderm findMate() {
        int neighbors = 0;
        EntityEchinoderm closestMate = null;
        double closestSqDist = -1.0;
        final AxisAlignedBB aabb = this.boundingBox.expand(8.0, 8.0, 8.0);
        for (final Object obj : this.worldObj.getEntitiesWithinAABB((Class) this.getClass(), aabb)) {
            if (obj == this) {
                continue;
            }
            ++neighbors;
            final EntityEchinoderm other = (EntityEchinoderm) obj;
            if (!this.isPotentialMate(other)) {
                continue;
            }
            final double sqDist = this.getDistanceSqToEntity((Entity) other);
            if (sqDist >= 4.0 || (closestSqDist != -1.0 && sqDist >= closestSqDist)) {
                continue;
            }
            closestMate = other;
            closestSqDist = sqDist;
        }
        if (neighbors > 6) {
            return null;
        }
        return closestMate;
    }

    public boolean isPotentialMate(final EntityEchinoderm other) {
        return !other.isChild() && other.isHorny();
    }

    public void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
    }

    public int getGrowingAge() {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setGrowingAge(final int age) {
        this.dataWatcher.updateObject(16, age);
    }

    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Age", this.getGrowingAge());
    }

    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setGrowingAge(compound.getInteger("Age"));
    }

    public float getGrowthProgress() {
        final int growingAge = this.getGrowingAge();
        final float growthProgress = (growingAge < 0) ? (1.0f + growingAge / 12000.0f) : 1.0f;
        return growthProgress;
    }

    private void setEchinodermSize() {
        final float growthProgress = this.getGrowthProgress();
        final float width = this.getBabyWidth() + growthProgress * (this.getAdultWidth() - this.getBabyWidth());
        final float height = this.getBabyHeight() + growthProgress * (this.getAdultHeight() - this.getBabyHeight());
        final float yO = this.getBabyYOffset() + growthProgress * (this.getAdultYOffset() - this.getBabyYOffset());
        this.setSize(width, height);
        this.yOffset = yO;
    }

    public abstract float getBabyWidth();

    public abstract float getAdultWidth();

    public abstract float getBabyHeight();

    public abstract float getAdultHeight();

    public abstract float getBabyYOffset();

    public abstract float getAdultYOffset();
}
