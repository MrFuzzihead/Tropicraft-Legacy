package net.tropicraft.entity.passive;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Failgull extends EntityFlying {

    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    public boolean inFlock;
    public Failgull leader;
    public int flockCount;
    public int flockPosition;

    public Failgull(final World par1World) {
        super(par1World);
        this.courseChangeCooldown = 0;
        this.flockCount = 0;
        this.flockPosition = 0;
        this.setSize(0.4f, 0.6f);
        this.experienceValue = 1;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(3.0);
    }

    public void entityInit() {
        super.entityInit();
    }

    protected void updateEntityActionState() {
        ++this.entityAge;
        this.despawnEntity();
        final double d0 = this.waypointX - this.posX;
        final double d2 = this.waypointY - this.posY;
        final double d3 = this.waypointZ - this.posZ;
        double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        if (d4 < 1.0 || d4 > 3600.0) {
            this.waypointX = this.posX + (this.rand.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.waypointY = this.posY + (this.rand.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.waypointZ = this.posZ + (this.rand.nextFloat() * 2.0f - 1.0f) * 16.0f;
        }
        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            d4 = MathHelper.sqrt_double(d4);
            if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d4)) {
                this.motionX += d0 / d4 * 0.1;
                this.motionY += d2 / d4 * 0.1;
                this.motionZ += d3 / d4 * 0.1;
            } else {
                this.waypointX = this.posX;
                this.waypointY = this.posY;
                this.waypointZ = this.posZ;
            }
        }
        if (this.leader != null) {
            if (this.flockPosition % 2 == 0) {
                this.waypointX = this.leader.waypointX;
                this.waypointY = this.leader.waypointY;
                this.waypointZ = this.leader.waypointZ;
            } else {
                this.waypointX = this.leader.waypointX;
                this.waypointY = this.leader.waypointY;
                this.waypointZ = this.leader.waypointZ;
            }
        }
        if (!this.inFlock) {
            final List list = this.worldObj
                .getEntitiesWithinAABB((Class) Failgull.class, this.boundingBox.expand(10.0, 10.0, 10.0));
            final int lowest = this.getEntityId();
            Failgull f = null;
            for (final Object o : list) {
                f = (Failgull) o;
                if (f.leader != null) {
                    this.flockPosition = ++f.leader.flockCount;
                    f.inFlock = true;
                    this.leader = f.leader;
                    break;
                }
            }
        }
        if (!this.inFlock && this.leader == null) {
            this.leader = this;
            this.inFlock = true;
        }
    }

    private void poop() {
        if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(20) == 0) {
            final EntitySnowball s = new EntitySnowball(this.worldObj, this.posX, this.posY, this.posZ);
            s.setThrowableHeading(0.0, 0.0, 0.0, 0.0f, 0.0f);
            this.worldObj.spawnEntityInWorld((Entity) s);
        }
    }

    private boolean isCourseTraversable(final double par1, final double par3, final double par5, final double par7) {
        final double d4 = (this.waypointX - this.posX) / par7;
        final double d5 = (this.waypointY - this.posY) / par7;
        final double d6 = (this.waypointZ - this.posZ) / par7;
        final AxisAlignedBB axisalignedbb = this.boundingBox.copy();
        for (int i = 1; i < par7; ++i) {
            axisalignedbb.offset(d4, d5, d6);
            if (!this.worldObj.getCollidingBoundingBoxes((Entity) this, axisalignedbb)
                .isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere();
    }

    protected String getLivingSound() {
        return null;
    }
}
