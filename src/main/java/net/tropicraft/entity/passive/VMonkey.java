package net.tropicraft.entity.passive;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.Vec3;
import net.minecraft.world.*;
import net.tropicraft.entity.*;
import net.tropicraft.registry.*;

import CoroUtil.componentAI.*;
import CoroUtil.componentAI.jobSystem.*;
import CoroUtil.diplomacy.*;
import CoroUtil.util.*;

public class VMonkey extends EntityCoroAI {

    public boolean isSitting;
    public boolean isClimbing;
    public int noMoveTicks;
    public Vec3 prevPos;

    public VMonkey(final World par1World) {
        super(par1World);
        this.noMoveTicks = 0;
        this.prevPos = null;
        this.setSize(0.8f, 0.8f);
        this.agent.jobMan.addJob((JobBase) new JobTamable(this.agent.jobMan, new ItemStack(TCItemRegistry.cocktail)));
        this.agent.jobMan.addJob((JobBase) new JobPlay(this.agent.jobMan));
        this.agent.dipl_info = TeamTypes.getType("animal");
        this.experienceValue = 4;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(30.0);
    }

    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.boundingBox.minY);
        final int k = MathHelper.floor_double(this.posZ);
        return CoroUtilBlock.isAir(this.worldObj.getBlock(i, j - 1, k))
            && this.worldObj.getFullBlockLightValue(i, j, k) > 8
            && super.getCanSpawnHere();
    }

    public void entityInit() {
        super.entityInit();
        this.getDataWatcher()
            .addObject(16, 0);
    }

    public void checkNewAgent() {
        if (this.agent == null) {
            this.agent = new AIAgent((ICoroAI) this, true);
        }
    }

    public boolean isEnemy(final Entity ent) {
        return false;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            final Vec3 curPos = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            if (this.prevPos == null) {
                this.prevPos = curPos;
            }
            final double speed = Math
                .sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            if (curPos.distanceTo(this.prevPos) < 0.01 && this.onGround) {
                ++this.noMoveTicks;
            } else {
                this.noMoveTicks = 0;
            }
            this.isSitting = (this.noMoveTicks > 5);
            this.isClimbing = false;
            this.prevPos = curPos;
        }
    }

    public boolean attackEntityFrom(final DamageSource par1DamageSource, final float par2) {
        this.worldObj.playSoundAtEntity((Entity) this, "monkeyhurt", 1.0f, 1.0f);
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public void attackMelee(final Entity ent, final float dist) {
        super.attackMelee(ent, dist);
        this.worldObj.playSoundAtEntity(
            (Entity) this,
            (this.worldObj.rand.nextInt(3) == 0) ? "monkeyangry" : "monkeyhiccup",
            1.0f,
            1.0f);
    }

    protected String getLivingSound() {
        return "tropicraft:" + (this.isPotionActive(Potion.confusion.id) ? "monkeyhiccup" : "monkeyliving");
    }

    protected String getHurtSound() {
        return "tropicraft:monkeyhurt";
    }

    protected String getDeathSound() {
        return null;
    }

    public boolean allowLeashing() {
        return true;
    }
}
