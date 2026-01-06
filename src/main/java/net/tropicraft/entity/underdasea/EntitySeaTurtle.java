package net.tropicraft.entity.underdasea;

import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class EntitySeaTurtle extends EntityAmphibian {

    public static final int DATAWATCHER_USERRIDING = 17;

    public EntitySeaTurtle(final World par1World) {
        super(par1World);
        this.setSize(0.3f, 0.3f);
    }

    public EntitySeaTurtle(final World world, final float age) {
        super(world, age);
        this.setSize(0.3f, 0.3f);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, (byte) 0);
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("UserRiding", this.isUserRiding());
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setUserRiding(nbttagcompound.getBoolean("UserRiding"));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.onGround && this.getAmphibianAge() >= 1.0f
            && this.returnToLand
            && this.worldObj.getBlock((int) this.posX, (int) this.posY - 1, (int) this.posZ)
                .getMaterial() == Material.sand
            && this.rand.nextInt(600) == 0) {
            final EntityTurtleEgg entityEgg = new EntityTurtleEgg(this.worldObj);
            final double d3 = this.posX;
            final double d4 = this.posY;
            final double d5 = this.posZ;
            entityEgg.setLocationAndAngles(d3, d4, d5, 0.0f, 0.0f);
            this.worldObj.spawnEntityInWorld(entityEgg);
            this.returnToLand = false;
        }
    }

    public boolean interact(final EntityPlayer entityplayer) {
        if (super.interact(entityplayer)) {
            return true;
        }
        if (this.worldObj.isRemote) {
            return false;
        }
        if (this.getAmphibianAge() >= 1.0f && !this.isUserRiding()
            && !this.worldObj.isRemote
            && (this.riddenByEntity == null || this.riddenByEntity == entityplayer)) {
            entityplayer.mountEntity(this);
            return true;
        }
        return false;
    }

    public double getMountedYOffset() {
        return this.height * 0.75 - 1.0 + 0.699999988079071;
    }

    public boolean isUserRiding() {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setUserRiding(final boolean flag) {
        this.dataWatcher.updateObject(17, (byte) (flag ? 1 : 0));
    }
}
