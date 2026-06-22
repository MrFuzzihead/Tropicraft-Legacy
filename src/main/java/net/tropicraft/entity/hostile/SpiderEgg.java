package net.tropicraft.entity.hostile;

import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import net.tropicraft.entity.ai.jobs.JobEggHatch;

import CoroUtil.componentAI.jobSystem.JobBase;

public class SpiderEgg extends EntityCoroAI implements IMob {

    public int motherID;
    public JobEggHatch job;

    public SpiderEgg(final World par1World) {
        super(par1World);
        this.motherID = -1;
        this.agent.jobMan.clearJobs();
        this.agent.jobMan.addPrimaryJob((JobBase) (this.job = new JobEggHatch(this.agent.jobMan)));
        this.agent.shouldAvoid = false;
    }

    protected boolean canDespawn() {
        return false;
    }

    public void readEntityFromNBT(final NBTTagCompound par1nbtTagCompound) {
        super.readEntityFromNBT(par1nbtTagCompound);
        this.motherID = par1nbtTagCompound.getInteger("motherID");
        this.job.motherID = this.motherID;
    }

    public void writeEntityToNBT(final NBTTagCompound par1nbtTagCompound) {
        super.writeEntityToNBT(par1nbtTagCompound);
        par1nbtTagCompound.setInteger("motherID", this.motherID);
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
}
