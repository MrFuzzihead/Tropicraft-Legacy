package net.tropicraft.entity.hostile;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public abstract class EntityAshen extends EntityMob implements IRangedAttackMob {

    public static final int DATAWATCHER_MASK_TYPE = 16;
    public static final int DATAWATCHER_ACTION_STATE = 17;
    public float bobber;
    public int bobberHelper;
    public int actionPicker;
    public EntityLostMask maskToTrack;
    public Entity itemToTrack;

    public EntityAshen(final World par1World) {
        super(par1World);
        this.setSize(0.5f, 1.3f);
        this.setMaskType(new Random().nextInt(7));
        this.actionPicker = 0;
        this.tasks.addTask(1, (EntityAIBase) new EntityAISwimming((EntityLiving) this));
        this.tasks.addTask(2, (EntityAIBase) new AIAshenChaseAndPickupLostMask(this, 1.0));
        this.tasks.addTask(3, (EntityAIBase) new EntityAIMeleeAndRangedAttack((IRangedAttackMob) this, 1.0, 60, 5.0f));
        this.tasks.addTask(4, (EntityAIBase) new EntityAIWander((EntityCreature) this, 1.0));
        this.tasks
            .addTask(5, (EntityAIBase) new EntityAIWatchClosest((EntityLiving) this, (Class) EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, (EntityAIBase) new EntityAILookIdle((EntityLiving) this));
        this.targetTasks.addTask(1, (EntityAIBase) new EntityAIHurtByTarget((EntityCreature) this, false));
        this.targetTasks.addTask(
            2,
            (EntityAIBase) new EntityAINearestAttackableTarget(
                (EntityCreature) this,
                (Class) EntityPlayer.class,
                0,
                true));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(DATAWATCHER_MASK_TYPE, 0);
        this.dataWatcher.addObject(DATAWATCHER_ACTION_STATE, 0);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(20.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.4);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(this.getAttackStrength());
    }

    protected abstract double getAttackStrength();

    public boolean isAIEnabled() {
        return true;
    }

    public void setMaskType(final int type) {
        this.dataWatcher.updateObject(DATAWATCHER_MASK_TYPE, type);
    }

    public int getMaskType() {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setActionState(final int state) {
        this.dataWatcher.updateObject(DATAWATCHER_ACTION_STATE, state);
    }

    public int getActionState() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("MaskType", (short) this.getMaskType());
        nbttagcompound.setShort("ActionState", (short) this.getActionState());
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setMaskType(nbttagcompound.getShort("MaskType"));
        this.setActionState(nbttagcompound.getShort("ActionState"));
    }

    public boolean hasMask() {
        final int ac = this.getActionState();
        return this.getActionState() != 1;
    }

    public void dropMask() {
        this.setActionState(1);
        this.maskToTrack = new EntityLostMask(
            this.worldObj,
            this.getMaskType(),
            this.posX,
            this.posY,
            this.posZ,
            this.rotationYaw);
        this.worldObj.spawnEntityInWorld((Entity) this.maskToTrack);
    }

    public void pickupMask(final EntityLostMask mask) {
        this.setActionState(2);
        this.maskToTrack = null;
        this.setMaskType(mask.type);
        mask.setDead();
    }

    protected String tcSound(final String postfix) {
        return String.format("%s:%s", "tropicraft", postfix);
    }

    public boolean attackEntityFrom(final DamageSource p_70097_1_, final float p_70097_2_) {
        final boolean wasHit = super.attackEntityFrom(p_70097_1_, p_70097_2_);
        if (!this.worldObj.isRemote && this.hasMask() && wasHit) {
            this.dropMask();
        }
        return wasHit;
    }
}
