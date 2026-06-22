package net.tropicraft.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityEchinodermEgg extends EntityLiving {

    public static final int HATCH_TICKS = 2400;
    private int hatchTime;

    public EntityEchinodermEgg(final World par1World) {
        super(par1World);
        this.hatchTime = 2400;
        this.setSize(0.25f, 0.25f);
        this.yOffset = 0.25f;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(5.0);
    }

    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.hatchTime = compound.getInteger("HatchTime");
    }

    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("HatchTime", this.hatchTime);
    }

    public void updateEntityActionState() {
        super.updateEntityActionState();
        this.isJumping = false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean attackEntityFrom(final DamageSource source, final float amt) {
        return source != DamageSource.inWall && super.attackEntityFrom(source, amt);
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.worldObj.isRemote) {
            this.motionY = 0.0;
            return;
        }
        this.noClip = this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0, this.posZ);
        if (this.hatchTime > 0) {
            --this.hatchTime;
        } else {
            this.setDead();
            final EntityEchinoderm baby = this.createBaby();
            final double newX = this.posX;
            final double newY = this.posY;
            final double newZ = this.posZ;
            baby.setLocationAndAngles(newX, newY, newZ, 0.0f, 0.0f);
            this.worldObj.spawnEntityInWorld((Entity) baby);
        }
    }

    public abstract EntityEchinoderm createBaby();
}
