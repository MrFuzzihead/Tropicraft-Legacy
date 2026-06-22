package net.tropicraft.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import net.tropicraft.entity.ai.jobs.JobAttackTargetShare;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobHunt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpiderBase extends EntityCoroAI implements IMob {

    public SpiderBase(final World par1World) {
        super(par1World);
        this.agent.jobMan.clearJobs();
        this.agent.jobMan.addPrimaryJob((JobBase) new JobHunt(this.agent.jobMan) {

            public boolean shouldContinue() {
                return true;
            }
        });
        this.agent.jobMan.addJob((JobBase) new JobAttackTargetShare(this.agent.jobMan, (Class) SpiderBase.class));
        this.agent.shouldAvoid = false;
        this.setSize(1.4f, 0.9f);
    }

    public boolean getCanSpawnHere() {
        return this.isValidLightLevel() && super.getCanSpawnHere();
    }

    protected void dropFewItems(final boolean par1, final int par2) {
        for (int j = this.rand.nextInt(2) + this.rand.nextInt(1 + par2), k = 0; k < j; ++k) {
            this.dropItem(Items.string, 1);
        }
        if (this.rand.nextInt(10) == 0) {
            this.dropItem(Items.string, 1);
        }
    }

    public boolean isOnLadder() {
        return this.agent != null && this.agent.entityToAttack == null
            && !this.getNavigator()
                .noPath()
            && this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return this.isCollidedHorizontally;
    }

    public void onLivingUpdate() {
        this.fallDistance = 0.0f;
        super.onLivingUpdate();
        if (!this.worldObj.isRemote && this.agent != null
            && this.agent.entityToAttack != null
            && this.onGround
            && this.worldObj.rand.nextInt(3) == 0
            && this.agent.entityToAttack.getDistanceToEntity((Entity) this) < 5.0f) {
            this.jump();
            this.motionX *= 2.4000000953674316;
            this.motionZ *= 2.4000000953674316;
        }
    }

    public void setInWeb() {}

    @SideOnly(Side.CLIENT)
    public float spiderScaleAmount() {
        return 1.2f;
    }

    protected String getLivingSound() {
        return "mob.spider.say";
    }

    protected String getHurtSound() {
        return "mob.spider.say";
    }

    protected String getDeathSound() {
        return "mob.spider.death";
    }

    protected void playStepSound(final int par1, final int par2, final int par3, final int par4) {
        this.playSound("mob.spider.step", 0.15f, 1.0f);
    }
}
