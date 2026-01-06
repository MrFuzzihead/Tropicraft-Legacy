package net.tropicraft.entity.placeable;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;
import net.tropicraft.util.*;

import cpw.mods.fml.relauncher.*;

public class EntityChair extends Entity {

    private static final int DATAWATCHER_COLOR = 2;
    private static final int DATAWATCHER_DAMAGE = 3;
    private static final int DATAWATCHER_COMESAILAWAY = 4;
    private static final int DATAWATCHER_FORWARD_DIRECTION = 5;
    private static final int DATAWATCHER_TIME_SINCE_HIT = 6;
    public boolean isChairEmpty;
    private int chairPosRotationIncrements;
    private double chairX;
    private double chairY;
    private double chairZ;
    private double chairPitch;
    private double chairYaw;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;
    private double speedMultiplier;

    public EntityChair(final World world) {
        super(world);
        this.ignoreFrustumCheck = true;
        this.isChairEmpty = true;
        this.speedMultiplier = 0.1;
        this.preventEntitySpawning = true;
        this.entityCollisionReduction = 0.95f;
        this.setSize(1.0f, 1.0f);
    }

    public EntityChair(final World world, final double x, final double y, final double z, final int color,
        final EntityPlayer player) {
        this(world);
        this.setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setColor(color);
        this.rotationYaw = this.getAngleToPlayer(player);
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
        final byte b0 = 5;
        double d0 = 0.0;
        if (this.getComeSailAway()) {
            for (int i = 0; i < b0; ++i) {
                final double d2 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * i / b0
                    - 0.125;
                final double d3 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (i + 1) / b0
                    - 0.125;
                final AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                    this.boundingBox.minX,
                    d2,
                    this.boundingBox.minZ,
                    this.boundingBox.maxX,
                    d3,
                    this.boundingBox.maxZ);
                if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
                    d0 += 1.0 / b0;
                }
            }
        }
        final double d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (d4 > 0.26249999999999996) {
            final double d5 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0);
            final double d6 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0);
            if (this.getComeSailAway()) {
                for (int j = 0; j < 1.0 + d4 * 60.0; ++j) {
                    final double d7 = this.rand.nextFloat() * 2.0f - 1.0f;
                    final double d8 = (this.rand.nextInt(2) * 2 - 1) * 0.7;
                    if (this.rand.nextBoolean()) {
                        final double d9 = this.posX - d5 * d7 * 0.8 + d6 * d8;
                        final double d10 = this.posZ - d6 * d7 * 0.8 - d5 * d8;
                        this.worldObj.spawnParticle(
                            "splash",
                            d9,
                            this.posY - 0.125,
                            d10,
                            this.motionX,
                            this.motionY,
                            this.motionZ);
                    } else {
                        final double d9 = this.posX + d5 + d6 * d7 * 0.7;
                        final double d10 = this.posZ + d6 - d5 * d7 * 0.7;
                        this.worldObj.spawnParticle(
                            "splash",
                            d9,
                            this.posY - 0.125,
                            d10,
                            this.motionX,
                            this.motionY,
                            this.motionZ);
                    }
                }
            }
        }
        if (this.worldObj.isRemote && this.isChairEmpty) {
            if (this.chairPosRotationIncrements > 0) {
                final double d5 = this.posX + (this.chairX - this.posX) / this.chairPosRotationIncrements;
                final double d6 = this.posY + (this.chairY - this.posY) / this.chairPosRotationIncrements;
                final double d11 = this.posZ + (this.chairZ - this.posZ) / this.chairPosRotationIncrements;
                final double d12 = MathHelper.wrapAngleTo180_double(this.chairYaw - this.rotationYaw);
                this.rotationYaw += (float) (d12 / this.chairPosRotationIncrements);
                this.rotationPitch += (float) ((this.chairPitch - this.rotationPitch)
                    / this.chairPosRotationIncrements);
                --this.chairPosRotationIncrements;
                this.setPosition(d5, d6, d11);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                final double d5 = this.posX + this.motionX;
                final double d6 = this.posY + this.motionY;
                final double d11 = this.posZ + this.motionZ;
                this.setPosition(d5, d6, d11);
                if (this.onGround) {
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
        } else {
            if (d0 < 1.0) {
                final double d5 = d0 * 2.0 - 1.0;
                this.motionY += 0.03999999910593033 * d5;
            } else {
                if (this.motionY < 0.0) {
                    this.motionY /= 2.0;
                }
                this.motionY += 0.007000000216066837;
            }
            if (this.getComeSailAway() && this.riddenByEntity instanceof EntityLivingBase entitylivingbase) {
                final float f = entitylivingbase.rotationYaw + -entitylivingbase.moveStrafing * 90.0f;
                this.motionX += -Math.sin(f * 3.1415927f / 180.0f) * this.speedMultiplier
                    * entitylivingbase.moveForward
                    * 0.05000000074505806;
                this.motionZ += Math.cos(f * 3.1415927f / 180.0f) * this.speedMultiplier
                    * entitylivingbase.moveForward
                    * 0.05000000074505806;
            }
            double d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d5 > 0.45) {
                final double d6 = 0.45 / d5;
                this.motionX *= d6;
                this.motionZ *= d6;
                d5 = 0.45;
            }
            if (d5 > d4 && this.speedMultiplier < 0.45) {
                this.speedMultiplier += (0.45 - this.speedMultiplier) / 45.0;
                if (this.speedMultiplier > 0.45) {
                    this.speedMultiplier = 0.45;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.1) / 45.0;
                if (this.speedMultiplier < 0.1) {
                    this.speedMultiplier = 0.1;
                }
            }
            if (this.getComeSailAway()) {
                for (int l = 0; l < 4; ++l) {
                    final int i2 = MathHelper.floor_double(this.posX + (l % 2 - 0.5) * 0.8);
                    final int j = MathHelper.floor_double(this.posZ + (l / 2.0 - 0.5) * 0.8);
                    for (int j2 = 0; j2 < 2; ++j2) {
                        final int k = MathHelper.floor_double(this.posY) + j2;
                        final Block block = this.worldObj.getBlock(i2, k, j);
                        if (block == Blocks.snow_layer) {
                            this.worldObj.setBlockToAir(i2, k, j);
                            this.isCollidedHorizontally = false;
                        } else if (block == Blocks.waterlily) {
                            this.worldObj.setBlockToAir(i2, k, j);
                            this.worldObj.func_147480_a(i2, k, j, true);
                            this.isCollidedHorizontally = false;
                        }
                    }
                }
            }
            if (this.getComeSailAway() && this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            } else if (this.onGround) {
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.getComeSailAway()) {
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
            this.rotationPitch = 0.0f;
            double d6 = this.rotationYaw;
            final double d11 = this.prevPosX - this.posX;
            final double d12 = this.prevPosZ - this.posZ;
            if (d11 * d11 + d12 * d12 > 0.001) {
                d6 = (float) (Math.atan2(d12, d11) * 180.0 / 3.141592653589793);
            }
            double d13 = MathHelper.wrapAngleTo180_double(d6 - this.rotationYaw);
            if (d13 > 20.0) {
                d13 = 20.0;
            }
            if (d13 < -20.0) {
                d13 = -20.0;
            }
            this.setRotation(this.rotationYaw += (float) d13, this.rotationPitch);
            if (!this.worldObj.isRemote) {
                final List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                    this,
                    this.boundingBox.expand(0.20000000298023224, 0.0, 0.20000000298023224));
                if (list != null && !list.isEmpty()) {
                    for (final Object obj : list) {
                        final Entity entity = (Entity) obj;
                        if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityChair) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }
                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    protected void entityInit() {
        // Pass primitives and allow autoboxing to avoid unnecessary explicit boxing calls
        this.dataWatcher.addObject(DATAWATCHER_COLOR, ColorHelper.DEFAULT_VALUE);
        this.dataWatcher.addObject(DATAWATCHER_DAMAGE, 0.0f);
        this.dataWatcher.addObject(DATAWATCHER_COMESAILAWAY, (byte) 0);
        this.dataWatcher.addObject(DATAWATCHER_FORWARD_DIRECTION, 1);
        this.dataWatcher.addObject(DATAWATCHER_TIME_SINCE_HIT, 0);
    }

    protected void readEntityFromNBT(final NBTTagCompound nbt) {
        this.setColor(nbt.getInteger("COLOR"));
        this.setComeSailAway(nbt.getBoolean("COME_SAIL_AWAY"));
    }

    protected void writeEntityToNBT(final NBTTagCompound nbt) {
        nbt.setInteger("COLOR", this.getColor());
        nbt.setBoolean("COME_SAIL_AWAY", this.getComeSailAway());
    }

    public boolean attackEntityFrom(final DamageSource par1DamageSource, final float par2) {
        if (this.isEntityInvulnerable()) {
            return false;
        }
        if (!this.worldObj.isRemote && !this.isDead) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamage(this.getDamage() + par2 * 10.0f);
            this.setBeenAttacked();
            final boolean flag = par1DamageSource.getEntity() instanceof EntityPlayer
                && ((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode;
            if (flag || this.getDamage() > 40.0f) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity(this);
                }
                if (!flag) {
                    this.entityDropItem(new ItemStack(TCItemRegistry.chair, 1, this.getDamageFromColor()), 0.0f);
                }
                this.setDead();
            }
            return true;
        }
        return true;
    }

    public int getDamageFromColor() {
        return ColorHelper.getDamageFromColor(this.getColor());
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamage(this.getDamage() * 11.0f);
    }

    public boolean interactFirst(final EntityPlayer player) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer
            && this.riddenByEntity != player) {
            return true;
        }
        if (!this.worldObj.isRemote) {
            System.out.println("mount");
            player.mountEntity(this);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(final double par1, final double par3, final double par5, final float par7,
        final float par8, final int par9) {
        if (this.isChairEmpty) {
            this.chairPosRotationIncrements = par9 + 5;
        } else {
            final double d3 = par1 - this.posX;
            final double d4 = par3 - this.posY;
            final double d5 = par5 - this.posZ;
            final double d6 = d3 * d3 + d4 * d4 + d5 * d5;
            if (d6 <= 1.0) {
                return;
            }
            this.chairPosRotationIncrements = 3;
        }
        this.chairX = par1;
        this.chairY = par3;
        this.chairZ = par5;
        this.chairYaw = par7;
        this.chairPitch = par8;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(final double xVelocity, final double yVelocity, final double zVelocity) {
        this.motionX = xVelocity;
        this.velocityX = xVelocity;
        this.motionY = yVelocity;
        this.velocityY = yVelocity;
        this.motionZ = zVelocity;
        this.velocityZ = zVelocity;
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            final double xOffset = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            final double zOffset = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            this.riddenByEntity.setPosition(
                this.posX + xOffset,
                this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(),
                this.posZ + zOffset);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.5f;
    }

    public double getMountedYOffset() {
        return this.height - 0.65;
    }

    private float getAngleToPlayer(final EntityPlayer player) {
        return MathHelper.wrapAngleTo180_float(player.rotationYaw);
    }

    public AxisAlignedBB getCollisionBox(final Entity par1Entity) {
        return par1Entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void updateFallState(final double distanceFallenThisTick, final boolean onGround) {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.posY);
        final int k = MathHelper.floor_double(this.posZ);
        if (onGround) {
            if (this.fallDistance > 3.0f) {
                this.fall(this.fallDistance);
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    for (int l = 0; l < 3; ++l) {
                        this.entityDropItem(
                            new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getDamageFromColor()),
                            0.0f);
                    }
                    for (int l = 0; l < this.rand.nextInt(5) + 1; ++l) {
                        this.dropItem(TCItemRegistry.bambooStick, 1);
                    }
                }
                this.fallDistance = 0.0f;
            }
        } else if (this.worldObj.getBlock(i, j - 1, k)
            .getMaterial() != Material.water && distanceFallenThisTick < 0.0) {
                this.fallDistance -= (float) distanceFallenThisTick;
            }
    }

    public void setColor(final int color) {
        this.dataWatcher.updateObject(DATAWATCHER_COLOR, color);
    }

    @SuppressWarnings("unused")
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

    public void setComeSailAway(final boolean sail) {
        this.dataWatcher.updateObject(DATAWATCHER_COMESAILAWAY, (byte) (sail ? 1 : 0));
    }

    public boolean getComeSailAway() {
        return this.dataWatcher.getWatchableObjectByte(DATAWATCHER_COMESAILAWAY) == 1;
    }

    public void setForwardDirection(final int dir) {
        this.dataWatcher.updateObject(DATAWATCHER_FORWARD_DIRECTION, dir);
    }

    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_FORWARD_DIRECTION);
    }

    public void setTimeSinceHit(final int time) {
        this.dataWatcher.updateObject(DATAWATCHER_TIME_SINCE_HIT, time);
    }

    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_TIME_SINCE_HIT);
    }
}
