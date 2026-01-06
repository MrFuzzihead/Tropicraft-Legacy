package net.tropicraft.entity.hostile;

import java.util.Objects;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.entity.*;
import net.tropicraft.registry.*;

public class EntityEIH extends EntityLand implements IMob {

    public static final int DATAWATCHER_ANGER_STATE = 16;

    public EntityEIH(final World world) {
        super(world);
        this.isImmuneToFire = true;
        this.setSize(1.4f, 4.0f);
        this.experienceValue = 10;
    }

    public void entityInit() {
        super.entityInit();
        this.getDataWatcher()
            .addObject(16, 0);
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
            .setBaseValue(100.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange)
            .setBaseValue(16.0);
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Sitting", this.isImmobile());
        nbttagcompound.setBoolean("Angry", this.isAngry());
        nbttagcompound.setBoolean("Awake", this.isAwake());
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setImmobile(nbttagcompound.getBoolean("Sitting"));
        this.setAngry(nbttagcompound.getBoolean("Angry"));
        this.setAwake(nbttagcompound.getBoolean("Awake"));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.isImmobile()) {
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
        this.getPlayerToHurt();
        if (this.getEntityToAttack() != null && !this.hasPath() && !this.isAngry()) {
            final Entity entity = this.getEntityToAttack();
            if (entity instanceof EntityPlayer) {
                if (!((EntityPlayer) entity).capabilities.isCreativeMode) {
                    final EntityPlayer entityplayer = (EntityPlayer) entity;
                    if (this.getDistanceToEntity(entityplayer) < 10.0f) {
                        this.setAwake(true);
                        final ItemStack itemstack = entityplayer.inventory.getCurrentItem();
                        if (itemstack != null && this.isAwake()
                            && itemstack.getItem() == Item.getItemFromBlock(TCBlockRegistry.chunkOHead)) {
                            this.setAngry(true);
                            this.setImmobile(false);
                        }
                    }
                    if (this.getDistanceToEntity(entityplayer) < 3.0f
                        && this.worldObj.difficultySetting.getDifficultyId()
                            > EnumDifficulty.PEACEFUL.getDifficultyId()) {
                        this.setAwake(false);
                        this.setImmobile(false);
                        this.setAngry(true);
                    }
                } else {
                    this.setImmobile(true);
                    this.setAwake(false);
                    this.setAngry(false);
                    this.moveEntity(0.0, -0.1, 0.0);
                    this.setRotation(this.prevRotationYaw, this.prevRotationPitch);
                }
            }
        }
        if (this.isImmobile()) {
            this.setRotation(this.prevRotationYaw, this.prevRotationPitch);
        } else {
            this.setAwake(false);
        }
    }

    private void getPlayerToHurt() {
        if (this.entityToAttack == null) {
            this.entityToAttack = this.worldObj.getClosestPlayerToEntity(this, 10.0);
        } else if (this.getDistanceToEntity(this.entityToAttack) > 16.0f) {
            this.entityToAttack = null;
        }
    }

    protected Entity findPlayerToAttack() {
        if (this.isAngry()) {
            return this.worldObj.getClosestPlayerToEntity(this, 16.0);
        }
        return null;
    }

    public boolean attackEntityFrom(final DamageSource damagesource, final float f) {
        if (damagesource.getSourceOfDamage() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer) damagesource.getSourceOfDamage();
            final ItemStack itemstack = entityplayer.inventory.getCurrentItem();
            if (itemstack != null) {
                if (Objects.requireNonNull(itemstack.getItem())
                    .canHarvestBlock(Blocks.iron_ore, itemstack)) {
                    super.attackEntityFrom(damagesource, f);
                } else {
                    final int b = 0;
                    if (b == 0) {
                        this.worldObj.playSoundAtEntity(
                            this,
                            this.tcSound("headlaughing"),
                            1.0f,
                            1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                    }
                    if (b == 1) {
                        this.worldObj.playSoundAtEntity(
                            this,
                            this.tcSound("headlaughing2"),
                            1.0f,
                            1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                    }
                }
            }
        }
        this.setImmobile(false);
        this.setAngry(true);
        this.entityToAttack = damagesource.getSourceOfDamage();
        return true;
    }

    protected void attackEntity(final Entity entity, final float f) {
        if (this.isAngry()) {
            if (f > 2.0f && f < 6.0f && this.rand.nextInt(10) == 0) {
                if (this.onGround) {
                    final double d = entity.posX - this.posX;
                    final double d2 = entity.posZ - this.posZ;
                    final float f2 = MathHelper.sqrt_double(d * d + d2 * d2);
                    this.motionX = d / f2 * 0.75 * 0.800000011920929 + this.motionX * 0.20000000298023224;
                    this.motionZ = d2 / f2 * 0.75 * 0.800000011920929 + this.motionZ * 0.20000000298023224;
                    this.motionY = 0.4000000059604645;
                }
            } else if (f < 1.5 && entity.boundingBox.maxY > this.boundingBox.minY
                && entity.boundingBox.minY < this.boundingBox.maxY) {
                    this.attackTime = 120;
                    final byte damage = 7;
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
                }
            if (this.getDistanceToEntity(entity) > 10.0) {
                this.setImmobile(true);
                this.setAngry(false);
                this.isJumping = false;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                this.entityToAttack = null;
            }
        }
    }

    public boolean interact(final EntityPlayer entityplayer) {
        return false;
    }

    protected void dropFewItems(final boolean recentlyHit, final int looting) {
        for (int j = 2 + this.rand.nextInt(1 + looting), k = 0; k < j; ++k) {
            this.dropItem(Item.getItemFromBlock(TCBlockRegistry.chunkOHead), 1);
        }
    }

    public boolean isAIEnabled() {
        return false;
    }

    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.boundingBox.minY);
        final int k = MathHelper.floor_double(this.posZ);
        return !this.worldObj.isAirBlock(i, j - 1, k) && this.worldObj.getFullBlockLightValue(i, j, k) > 8
            && super.getCanSpawnHere();
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    protected String getHurtSound() {
        return this.tcSound("headpain");
    }

    protected String getDeathSound() {
        return this.tcSound("headdeath");
    }

    protected String getLivingSound() {
        if (this.isAngry()) {
            if (this.rand.nextInt(10) == 0) {
                return this.tcSound("headmed");
            }
            return null;
        } else {
            if (!this.isAwake()) {
                return null;
            }
            if (this.rand.nextInt(10) == 0) {
                return this.tcSound("headshort");
            }
            return null;
        }
    }

    public float getSoundVolume() {
        return 0.4f;
    }

    protected boolean isMovementCeased() {
        return this.isImmobile();
    }

    public boolean isImmobile() {
        return (this.dataWatcher.getWatchableObjectInt(16) & 0x1) != 0x0;
    }

    public void setImmobile(final boolean flag) {
        final int int0 = this.dataWatcher.getWatchableObjectInt(16);
        if (flag) {
            this.dataWatcher.updateObject(16, (int0 | 0x1));
        } else {
            this.dataWatcher.updateObject(16, (int0 & 0xFFFFFFFE));
        }
    }

    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectInt(16) & 0x2) != 0x0;
    }

    public void setAngry(final boolean flag) {
        final int int0 = this.dataWatcher.getWatchableObjectInt(16);
        if (flag) {
            this.dataWatcher.updateObject(16, (int0 | 0x2));
        } else {
            this.dataWatcher.updateObject(16, (int0 & 0xFFFFFFFD));
        }
    }

    public boolean isAwake() {
        return (this.dataWatcher.getWatchableObjectInt(16) & 0x4) != 0x0;
    }

    public void setAwake(final boolean flag) {
        final int int0 = this.dataWatcher.getWatchableObjectInt(16);
        if (flag) {
            this.dataWatcher.updateObject(16, (int0 | 0x4));
        } else {
            this.dataWatcher.updateObject(16, (int0 & 0xFFFFFFFB));
        }
    }
}
