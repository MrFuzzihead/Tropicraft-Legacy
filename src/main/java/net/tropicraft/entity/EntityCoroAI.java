package net.tropicraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import CoroUtil.componentAI.AIAgent;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobHunt;
import CoroUtil.diplomacy.DiplomacyHelper;
import CoroUtil.diplomacy.TeamTypes;

public class EntityCoroAI extends EntityLand implements ICoroAI, IMob {

    public AIAgent agent;

    public EntityCoroAI(final World par1World) {
        super(par1World);
        this.setSize(0.7f, 1.95f);
        this.agent.jobMan.addPrimaryJob((JobBase) new JobHunt(this.agent.jobMan));
        this.agent.shouldFixBadYPathing = true;
        this.agent.dipl_info = TeamTypes.getType("hostile");
    }

    public boolean interact(final EntityPlayer par1EntityPlayer) {
        this.checkNewAgent();
        return this.agent.hookInteract(par1EntityPlayer);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.agent.setSpeedFleeAdditive(0.1f);
        this.agent.setSpeedNormalBase(0.5f);
        this.agent.applyEntityAttributes();
    }

    public void setDead() {
        super.setDead();
        this.agent.hookSetDead();
    }

    public void cleanup() {
        this.agent = null;
    }

    protected boolean isValidLightLevel() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.boundingBox.minY);
        final int k = MathHelper.floor_double(this.posZ);
        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32)) {
            return false;
        }
        int l = this.worldObj.getBlockLightValue(i, j, k);
        if (this.worldObj.isThundering()) {
            final int i2 = this.worldObj.skylightSubtracted;
            this.worldObj.skylightSubtracted = 10;
            l = this.worldObj.getBlockLightValue(i, j, k);
            this.worldObj.skylightSubtracted = i2;
        }
        return l <= this.rand.nextInt(8);
    }

    public IEntityLivingData onSpawnWithEgg(final IEntityLivingData par1EntityLivingData) {
        this.checkNewAgent();
        this.agent.spawnedOrNBTReloadedInit();
        return super.onSpawnWithEgg(par1EntityLivingData);
    }

    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere();
    }

    public boolean attackEntityFrom(final DamageSource par1DamageSource, final float par2) {
        if (!this.worldObj.isRemote) {
            if (par1DamageSource == DamageSource.inWall) {
                this.motionY += 1.5;
            }
            if (this.agent != null && this.agent.jobMan != null) {
                this.agent.jobMan.hookHit(par1DamageSource, (int) par2);
            }
        }
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public void checkNewAgent() {
        if (this.agent == null) {
            this.agent = new AIAgent((ICoroAI) this, false);
        }
    }

    public void updateAITasks() {
        this.agent.updateAITasks();
    }

    protected void entityInit() {
        super.entityInit();
        this.checkNewAgent();
        this.agent.entityInit();
    }

    public void readEntityFromNBT(final NBTTagCompound par1nbtTagCompound) {
        super.readEntityFromNBT(par1nbtTagCompound);
        this.checkNewAgent();
        this.agent.readEntityFromNBT(par1nbtTagCompound);
    }

    public void writeEntityToNBT(final NBTTagCompound par1nbtTagCompound) {
        super.writeEntityToNBT(par1nbtTagCompound);
        this.agent.writeEntityToNBT(par1nbtTagCompound);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    public AIAgent getAIAgent() {
        return this.agent;
    }

    public void setPathResultToEntity(final PathEntity pathentity) {
        if (this.agent != null) {
            this.agent.setPathToEntity(pathentity);
        }
    }

    public int getCooldownMelee() {
        return 20;
    }

    public int getCooldownRanged() {
        return 20;
    }

    public void attackMelee(final Entity ent, final float dist) {
        ent.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this), 2.0f);
    }

    public void attackRanged(final Entity ent, final float dist) {}

    public boolean isBreaking() {
        return false;
    }

    public boolean isEnemy(final Entity ent) {
        return (ent instanceof EntityPlayer && !((EntityPlayer) ent).capabilities.isCreativeMode)
            || DiplomacyHelper.shouldTargetEnt((ICoroAI) this, ent);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.agent.onLivingUpdateTick();
        if (this.getAIAgent().activeFormation != null && this == this.getAIAgent().activeFormation.leader) {
            this.motionX *= 0.8;
            this.motionZ *= 0.8;
        }
        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
        if (this.worldObj.isRemote && this.isInWater()) {
            this.motionY += 0.03;
        }
    }

    protected String getLivingSound() {
        return null;
    }

    public boolean allowLeashing() {
        return (this.agent != null && this.agent.jobMan != null
            && this.agent.jobMan.getPrimaryJob() != null
            && this.agent.jobMan.getPrimaryJob().tamable.isTame()) || super.allowLeashing();
    }
}
