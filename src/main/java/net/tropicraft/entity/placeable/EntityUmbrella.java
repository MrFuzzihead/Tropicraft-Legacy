package net.tropicraft.entity.placeable;

import java.util.*;

import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;
import net.tropicraft.util.*;

import cpw.mods.fml.relauncher.*;

public class EntityUmbrella extends Entity {

    private static final int DATAWATCHER_COLOR = 2;
    private static final int DATAWATCHER_DAMAGE = 3;
    private static final int DATAWATCHER_TIME_SINCE_HIT = 4;
    private static final int DATAWATCHER_ROCK_DIRECTION = 5;
    private static final int DAMAGE_THRESHOLD = 40;
    private double umbrellaX;
    private double umbrellaY;
    private double umbrellaZ;
    private double umbrellaYaw;
    private double umbrellaPitch;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;
    private int idk;

    public EntityUmbrella(final World world) {
        super(world);
        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
        this.entityCollisionReduction = 0.95f;
        this.setSize(1.0f, 4.0f);
    }

    public EntityUmbrella(final World world, final double x, final double y, final double z, final int color) {
        this(world);
        this.spawnX = MathHelper.floor_double(x);
        this.spawnY = MathHelper.floor_double(y - 1.01);
        this.spawnZ = MathHelper.floor_double(z);
        this.setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setColor(color);
        this.idk = 4;
    }

    public void setDead() {
        this.isDead = true;
        final int i = (int) this.posX;
        final int j = (int) this.posY;
        final int k = (int) this.posZ;
        final int y = j + 4;
        for (int x = i - 3; x <= i + 2; ++x) {
            for (int z = k - 3; z <= k + 2; ++z) {
                this.worldObj.setBlockToAir(i, y, k);
            }
        }
    }

    protected void entityInit() {
        this.dataWatcher.addObject(DATAWATCHER_COLOR, ColorHelper.DEFAULT_VALUE);
        this.dataWatcher.addObject(DATAWATCHER_DAMAGE, 0.0f);
        this.dataWatcher.addObject(DATAWATCHER_TIME_SINCE_HIT, 0);
        this.dataWatcher.addObject(DATAWATCHER_ROCK_DIRECTION, 0);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(final double d, final double d1, final double d2) {
        this.motionX = d;
        this.velocityX = d;
        this.motionY = d1;
        this.velocityY = d1;
        this.motionZ = d2;
        this.velocityZ = d2;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        final int i = 5;
        double d = 0.0;
        for (int j = 0; j < i; ++j) {
            final double d2 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (j + 0) / i
                - 0.125;
            final double d3 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (j + 1) / i
                - 0.125;
            final AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                this.boundingBox.minX,
                d2,
                this.boundingBox.minZ,
                this.boundingBox.maxX,
                d3,
                this.boundingBox.maxZ);
            if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
                d += 1.0 / i;
            }
        }
        if (this.worldObj.isRemote) {
            if (this.idk > 0) {
                final double d4 = this.posX + (this.umbrellaX - this.posX) / this.idk;
                final double d5 = this.posY + (this.umbrellaY - this.posY) / this.idk;
                final double d6 = this.posZ + (this.umbrellaZ - this.posZ) / this.idk;
                double d7;
                for (d7 = this.umbrellaYaw - this.rotationYaw; d7 < -180.0; d7 += 360.0) {}
                while (d7 >= 180.0) {
                    d7 -= 360.0;
                }
                this.rotationYaw += (float) (d7 / this.idk);
                this.rotationPitch += (float) ((this.umbrellaPitch - this.rotationPitch) / this.idk);
                --this.idk;
                this.setPosition(d4, d5, d6);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        } else {
            final double motionX = 0.0;
            this.motionZ = motionX;
            this.motionY = motionX;
            this.motionX = motionX;
        }
        if (d < 1.0) {
            final double d8 = d * 2.0 - 1.0;
            this.motionY += 0.03999999910593033 * d8;
        } else {
            if (this.motionY < 0.0) {
                this.motionY /= 2.0;
            }
            this.motionY += 0.007000000216066837;
        }
        if (this.onGround) {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.rotationPitch = 0.0f;
        double d9 = this.rotationYaw;
        final double d10 = this.prevPosX - this.posX;
        final double d11 = this.prevPosZ - this.posZ;
        if (d10 * d10 + d11 * d11 > 0.001) {
            d9 = (float) (Math.atan2(d11, d10) * 180.0 / 3.141592653589793);
        }
        double d12;
        for (d12 = d9 - this.rotationYaw; d12 >= 180.0; d12 -= 360.0) {}
        while (d12 < -180.0) {
            d12 += 360.0;
        }
        if (d12 > 20.0) {
            d12 = 20.0;
        }
        if (d12 < -20.0) {
            d12 = -20.0;
        }
        this.setRotation(this.rotationYaw += (float) d12, this.rotationPitch);
        final List<?> list = (List<?>) this.worldObj.getEntitiesWithinAABBExcludingEntity(
            (Entity) this,
            this.boundingBox.expand(0.20000000298023224, 0.2, 0.20000000298023224));
        if (list != null && list.size() > 0) {
            for (int j2 = 0; j2 < list.size(); ++j2) {
                final Entity entity = (Entity) list.get(j2);
                if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityUmbrella) {
                    entity.applyEntityCollision((Entity) this);
                }
            }
        }
    }

    public boolean attackEntityFrom(final DamageSource damagesource, final float i) {
        if (this.isEntityInvulnerable()) {
            return false;
        }
        if (!this.worldObj.isRemote && !this.isDead) {
            this.setRockDirection(-this.getRockDirection());
            this.setTimeSinceHit(10);
            this.setDamage(this.getDamage() + i * 10.0f);
            this.setBeenAttacked();
            final boolean flag = damagesource.getEntity() instanceof EntityPlayer
                && ((EntityPlayer) damagesource.getEntity()).capabilities.isCreativeMode;
            if (flag || this.getDamage() > 40.0f) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity((Entity) this);
                }
                if (!flag) {
                    this.entityDropItem(
                        new ItemStack((Item) TCItemRegistry.umbrella, 1, this.getDamageFromColor()),
                        0.0f);
                }
                this.setDead();
                final int y = this.spawnY + 4;
                for (int x = this.spawnX - 3; x <= this.spawnX + 2; ++x) {
                    for (int z = this.spawnZ - 3; z <= this.spawnZ + 2; ++z) {
                        this.worldObj.setBlockToAir(x, y, z);
                    }
                }
            }
            return true;
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(final double d, final double d1, final double d2, final float f, final float f1,
        final int i) {
        this.umbrellaX = d;
        this.umbrellaY = d1;
        this.umbrellaZ = d2;
        this.umbrellaYaw = f;
        this.umbrellaPitch = f1;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.idk = i + 1;
    }

    public int getDamageFromColor() {
        return ColorHelper.getDamageFromColor(this.getColor());
    }

    public AxisAlignedBB getCollisionBox(final Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public double getMountedYOffset() {
        return 0.0;
    }

    public void performHurtAnimation() {
        this.setRockDirection(-1 * this.getRockDirection());
        this.setTimeSinceHit(10);
        this.setDamage(this.getDamage() * 10.0f);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean canBePushed() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 2.0f;
    }

    protected void readEntityFromNBT(final NBTTagCompound nbt) {
        this.setColor(nbt.getInteger("COLOR"));
        this.spawnX = nbt.getInteger("spawnX");
        this.spawnY = nbt.getInteger("spawnY");
        this.spawnZ = nbt.getInteger("spawnZ");
    }

    protected void writeEntityToNBT(final NBTTagCompound nbt) {
        nbt.setInteger("COLOR", (int) this.getColor());
        nbt.setInteger("spawnX", (int) this.spawnX);
        nbt.setInteger("spawnY", (int) this.spawnY);
        nbt.setInteger("spawnZ", (int) this.spawnZ);
    }

    public void setColor(final int color) {
        this.dataWatcher.updateObject(DATAWATCHER_COLOR, color);
    }

    public void setColor(final float red, final float green, final float blue) {
        this.dataWatcher.updateObject(DATAWATCHER_COLOR, ColorHelper.getColor(red, green, blue));
    }

    public int getColor() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_COLOR);
    }

    public void setDamage(final float damage) {
        this.dataWatcher.updateObject(DATAWATCHER_DAMAGE, damage);
    }

    public float getDamage() {
        return this.dataWatcher.getWatchableObjectFloat(DATAWATCHER_DAMAGE);
    }

    public void setTimeSinceHit(final int time) {
        this.dataWatcher.updateObject(DATAWATCHER_TIME_SINCE_HIT, time);
    }

    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_TIME_SINCE_HIT);
    }

    public void setRockDirection(final int direction) {
        this.dataWatcher.updateObject(DATAWATCHER_ROCK_DIRECTION, direction);
    }

    public int getRockDirection() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_ROCK_DIRECTION);
    }
}
