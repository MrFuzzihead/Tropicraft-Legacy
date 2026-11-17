package net.tropicraft.entity.ai.jobs;

import net.minecraft.entity.Entity;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobExtraTargetting extends JobBase {

    public int rangeTarget;

    public JobExtraTargetting(final JobManager jm) {
        super(jm);
        this.rangeTarget = 48;
    }

    public boolean shouldExecute() {
        return true;
    }

    public boolean shouldContinue() {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.ai.entityToAttack == null) {
            final Entity candidate = this.ent.worldObj
                .getClosestVulnerablePlayerToEntity(this.ent, (double) this.rangeTarget);
            // Acquiring a target has to go through isEnemy, or this job re-invents the creative-player problem the
            // rest of the job system was just fixed for.
            if (candidate != null && this.isEnemy(candidate)) {
                this.ai.entityToAttack = candidate;
            }
        }
    }
}
