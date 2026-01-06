package net.tropicraft.entity.projectile;

import java.util.List;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.*;
import net.tropicraft.entity.damage.*;
import net.tropicraft.util.*;

import CoroUtil.packet.*;
import cpw.mods.fml.relauncher.*;

public class EntityDart extends Entity implements IProjectile {

    private static final short MAX_HIT_TIME = 200;
    private int ticksInGround;
    private int ticksInAir;
    public Entity shootingEntity;
    private short dartType;
    private boolean inGround;
    public boolean doesDartBelongToPlayer;
    public int dartShake;
    private boolean hasRidden;
    public static int[] potions;

    public EntityDart(final World world) {
        super(world);
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
    }

    public EntityDart(final World world, final double x, final double y, final double z) {
        super(world);
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
        this.setPosition(x, y, z);
        this.yOffset = 0.0f;
        this.ticksInAir = 0;
        this.ticksInGround = 0;
    }

    public EntityDart(final World world, final Entity shooter, final float derp, final short damage) {
        this(world);
        this.shootingEntity = shooter;
        this.dartType = damage;
        this.setLocationAndAngles(
            shooter.posX,
            shooter.posY + shooter.getEyeHeight(),
            shooter.posZ,
            shooter.rotationYaw,
            shooter.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f)
            * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f)
            * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, derp * 1.5f, 1.0f);
    }

    public void setThrowableHeading(double x, double y, double z, final float rotation1, final float rotation2) {
        final float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= f2;
        y /= f2;
        z /= f2;
        x += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * rotation2;
        y += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * rotation2;
        z += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * rotation2;
        x *= rotation1;
        y *= rotation1;
        z *= rotation1;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        final float f3 = MathHelper.sqrt_double(x * x + z * z);
        final float n = (float) (Math.atan2(x, z) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float) (Math.atan2(y, f3) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            final float n = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float) (Math.atan2(this.motionY, f) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
        }
        if (this.dartShake > 0) {
            --this.dartShake;
        }
        if (!this.worldObj.isRemote) {}
        if (this.inGround) {
            this.setDead();
        } else {
            ++this.ticksInAir;
            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec32 = Vec3
                .createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec32, false, true, false);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec32 = Vec3
                .createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec32 = Vec3.createVectorHelper(
                    movingobjectposition.hitVec.xCoord,
                    movingobjectposition.hitVec.yCoord,
                    movingobjectposition.hitVec.zCoord);
            }
            Entity entity = null;
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                this,
                this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ)
                    .expand(1.0, 1.0, 1.0));
            double d0 = 0.0;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (entity2.canBeCollidedWith() && (entity2 != this.shootingEntity || this.ticksInAir >= 5)) {
                    final float f2 = 0.3f;
                    final AxisAlignedBB axisalignedbb1 = entity2.boundingBox.expand(f2, f2, f2);
                    final MovingObjectPosition movingobjectposition2 = axisalignedbb1.calculateIntercept(vec31, vec32);
                    if (movingobjectposition2 != null) {
                        final double d2 = vec31.distanceTo(movingobjectposition2.hitVec);
                        if (d2 < d0 || d0 == 0.0) {
                            entity = entity2;
                            d0 = d2;
                        }
                    }
                }
            }
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
            if (movingobjectposition != null && movingobjectposition.entityHit != null
                && movingobjectposition.entityHit instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
                if (entityplayer.capabilities.disableDamage || (this.shootingEntity instanceof EntityPlayer
                    && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer))) {
                    movingobjectposition = null;
                }
            }
            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    final float f3 = MathHelper.sqrt_double(
                        this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    DamageSource damagesource = null;
                    if (this.shootingEntity == null) {
                        damagesource = TCDamageSource.causeDartDamage(this, this);
                    } else {
                        damagesource = TCDamageSource.causeDartDamage(this, this.shootingEntity);
                    }
                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, 2.0f)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            final EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;
                            if (!this.worldObj.isRemote) {
                                if (this.dartType == 0) {
                                    System.out.println("client?: " + this.worldObj.isRemote);
                                    if (entitylivingbase instanceof EntityPlayerMP) {
                                        final NBTTagCompound nbt = new NBTTagCompound();
                                        nbt.setString("packetCommand", "effectAdd");
                                        nbt.setInteger("effectID", this.dartType);
                                        nbt.setInteger("effectTime", 100);
                                        Tropicraft.eventChannel.sendTo(
                                            PacketHelper.getNBTPacket(nbt, Tropicraft.eventChannelName),
                                            (EntityPlayerMP) entitylivingbase);
                                    } else {
                                        EffectHelper.addEntry(entitylivingbase, 100);
                                    }
                                } else {
                                    entitylivingbase
                                        .addPotionEffect(new PotionEffect(EntityDart.potions[this.dartType], 200, 1));
                                }
                            }
                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity
                                && movingobjectposition.entityHit instanceof EntityPlayer
                                && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler
                                    .sendPacket(new S2BPacketChangeGameState(6, 0.0f));
                            }
                        }
                        this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                    } else {
                        this.motionX *= -0.10000000149011612;
                        this.motionY *= -0.10000000149011612;
                        this.motionZ *= -0.10000000149011612;
                        this.rotationYaw += 180.0f;
                        this.prevRotationYaw += 180.0f;
                        this.ticksInAir = 0;
                    }
                } else {
                    final Block block = this.worldObj.getBlock(
                        movingobjectposition.blockX,
                        movingobjectposition.blockY,
                        movingobjectposition.blockZ);
                    if (block.getMaterial() != Material.air) {
                        this.setDead();
                    }
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
            this.rotationPitch = (float) (Math.atan2(this.motionY, f3) * 180.0 / 3.141592653589793);
            while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
                this.prevRotationPitch -= 360.0f;
            }
            while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
                this.prevRotationPitch += 360.0f;
            }
            while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
                this.prevRotationYaw -= 360.0f;
            }
            while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
                this.prevRotationYaw += 360.0f;
            }
            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
            float f4 = 0.99f;
            final float f2 = 0.05f;
            if (this.isInWater()) {
                for (int l = 0; l < 4; ++l) {
                    final float f5 = 0.25f;
                    this.worldObj.spawnParticle(
                        "bubble",
                        this.posX - this.motionX * f5,
                        this.posY - this.motionY * f5,
                        this.posZ - this.motionZ * f5,
                        this.motionX,
                        this.motionY,
                        this.motionZ);
                }
                f4 = 0.8f;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            this.motionX *= f4;
            this.motionY *= f4;
            this.motionZ *= f4;
            this.motionY -= f2;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }
    }

    public void setDead() {
        super.setDead();
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(final double par1, final double par3, final double par5, final float par7,
        final float par8, final int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(final double par1, final double par3, final double par5) {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            final float n = (float) (Math.atan2(par1, par5) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float) (Math.atan2(par3, f) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    public void onCollideWithPlayer(final EntityPlayer par1EntityPlayer) {}

    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0f;
    }

    protected void entityInit() {
        EntityDart.potions = new int[] { Potion.blindness.id, Potion.poison.id, Potion.moveSlowdown.id, Potion.harm.id,
            Potion.confusion.id, Potion.hunger.id, Potion.weakness.id };
        this.dataWatcher.addObject(17, (short) 200);
        this.dataWatcher.addObject(18, (byte) 0);
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("shake", (byte) this.dartShake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbttagcompound.setBoolean("player", this.doesDartBelongToPlayer);
        nbttagcompound.setBoolean("hit", this.getIsHit());
        nbttagcompound.setShort("hitTime", (short) this.getHitTimer());
        nbttagcompound.setShort("dartType", this.dartType);
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        this.dartShake = (nbttagcompound.getByte("shake") & 0xFF);
        this.inGround = (nbttagcompound.getByte("inGround") == 1);
        this.doesDartBelongToPlayer = nbttagcompound.getBoolean("player");
        this.setIsHit(nbttagcompound.getBoolean("hit"));
        this.setHitTimer(nbttagcompound.getShort("hitTime"));
        this.dartType = nbttagcompound.getShort("dartType");
    }

    public void setIsHit(final boolean set) {
        this.dataWatcher.updateObject(18, (byte) (set ? 1 : 0));
    }

    public boolean getIsHit() {
        return this.dataWatcher.getWatchableObjectByte(18) == 1;
    }

    public void setHitTimer(final short hitTime) {
        this.dataWatcher.updateObject(17, (short) hitTime);
    }

    public int getHitTimer() {
        return this.dataWatcher.getWatchableObjectShort(17);
    }
}
