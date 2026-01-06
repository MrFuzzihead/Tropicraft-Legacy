package net.tropicraft.entity.hostile;

import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import CoroUtil.api.weather.*;

public class EntityLostMask extends Entity implements WindHandler {

    public int type;
    public float bobber;
    private double launchedSpeed;

    public EntityLostMask(final World par1World) {
        super(par1World);
        this.launchedSpeed = 1.0;
        this.setSize(1.0f, 1.0f);
    }

    public EntityLostMask(final World world, final int type, final double x, final double y, final double z,
        final double angle) {
        this(world);
        this.setPosition(x, y, z);
        this.setType(type);
        this.motionX = Math.cos(Math.toRadians(angle + 90.0)) * this.launchedSpeed;
        this.motionZ = Math.sin(Math.toRadians(angle + 90.0)) * this.launchedSpeed;
        final double subAngle = MathHelper.wrapAngleTo180_double(angle);
        final double subAngle2 = subAngle + (180.0 - subAngle) * 2.0;
        this.rotationYaw = (float) subAngle2;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean canBePushed() {
        return true;
    }

    public void onUpdate() {
        if (this.onGround) {
            this.motionX *= 0.5;
            this.motionZ *= 0.5;
            this.motionY = 0.0;
        }
        if (this.checkForWater(0)) {
            this.motionY = 0.019999999552965164;
            this.motionX *= 0.949999988079071;
            this.motionZ *= 0.949999988079071;
        } else {
            final int xMod = this.worldObj.rand.nextInt(10);
            final int yMod = this.worldObj.rand.nextInt(30);
            final int zMod = this.worldObj.rand.nextInt(10);
            final int[] rotator;
            final int[] a = rotator = this.getRotator();
            final int n = 0;
            rotator[n] += xMod;
            final int[] array = a;
            final int n2 = 1;
            array[n2] += yMod;
            final int[] array2 = a;
            final int n3 = 2;
            array2[n3] += zMod;
            this.setRotator(a);
            this.motionY -= 0.05000000074505806;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    protected void entityInit() {
        this.dataWatcher.addObject(17, 0);
        this.dataWatcher.addObject(18, 0);
        this.dataWatcher.addObject(19, 0);
        this.dataWatcher.addObject(20, 0);
    }

    protected void readEntityFromNBT(final NBTTagCompound nbt) {
        this.setType(nbt.getInteger("MaskType"));
    }

    protected void writeEntityToNBT(final NBTTagCompound nbt) {
        nbt.setInteger("MaskType", this.getColor());
    }

    public boolean attackEntityFrom(final DamageSource par1DamageSource, final float par2) {
        if (this.isEntityInvulnerable()) {
            return false;
        }
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            EntityPlayer entityplayer = null;
            if (par1DamageSource.getEntity() instanceof EntityPlayer) {
                entityplayer = (EntityPlayer) par1DamageSource.getEntity();
            }
            if (entityplayer != null && entityplayer.capabilities.isCreativeMode) {
                return true;
            }
            this.dropItemStack();
        }
        return true;
    }

    public void dropItemStack() {}

    private void setRotator(final int[] a) {
        this.dataWatcher.updateObject(18, a[0]);
        this.dataWatcher.updateObject(19, a[1]);
        this.dataWatcher.updateObject(20, a[2]);
    }

    public int[] getRotator() {
        return new int[] { this.dataWatcher.getWatchableObjectInt(18), this.dataWatcher.getWatchableObjectInt(19),
            this.dataWatcher.getWatchableObjectInt(20) };
    }

    private void setType(final int i) {
        this.dataWatcher.updateObject(17, i);
    }

    public boolean checkForWater(final int i) {
        return this.worldObj
            .getBlock((int) Math.floor(this.posX), (int) Math.floor(this.posY) + i, (int) Math.floor(this.posZ))
            .getMaterial() == Material.water;
    }

    public int getMode() {
        return 0;
    }

    public int getColor() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public int getDirection() {
        return 0;
    }

    public float getWindWeight() {
        return 999999.0f;
    }

    public int getParticleDecayExtra() {
        return 0;
    }
}
