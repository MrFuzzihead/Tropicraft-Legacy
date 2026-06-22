package net.tropicraft.entity.ai.jobs;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import CoroUtil.componentAI.ICoroAI;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobAttackTargetShare extends JobBase {

    public int rangeTargetShare;
    public Class classToShareWith;
    public int shareTriggerCooldown;
    public int shareTriggerCooldownMax;

    public JobAttackTargetShare(final JobManager jm, final Class parClass) {
        super(jm);
        this.rangeTargetShare = 24;
        this.classToShareWith = null;
        this.shareTriggerCooldown = 0;
        this.shareTriggerCooldownMax = 60;
        this.classToShareWith = parClass;
    }

    public boolean shouldExecute() {
        return true;
    }

    public boolean shouldContinue() {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.shareTriggerCooldown > 0) {
            --this.shareTriggerCooldown;
        }
    }

    public boolean hookHit(final DamageSource ds, final int damage) {
        final Entity target = ds.getEntity();
        if (this.isEnemy(target) && this.shareTriggerCooldown == 0) {
            this.shareTriggerCooldown = this.shareTriggerCooldownMax;
            final List<EntityLivingBase> notifyList = (List<EntityLivingBase>) this.ent.worldObj.getEntitiesWithinAABB(
                this.classToShareWith,
                this.ent.boundingBox.expand(
                    (double) this.rangeTargetShare,
                    (double) this.rangeTargetShare,
                    (double) this.rangeTargetShare));
            for (int i = 0; i < notifyList.size(); ++i) {
                final EntityLivingBase entN = notifyList.get(i);
                if (!this.ent.isDead && this.ent instanceof ICoroAI
                    && ((ICoroAI) this.ent).getAIAgent().entityToAttack == null) {
                    System.out.println("sharing targetting");
                    ((ICoroAI) this.ent).getAIAgent().entityToAttack = target;
                }
            }
        }
        return super.hookHit(ds, damage);
    }
}
