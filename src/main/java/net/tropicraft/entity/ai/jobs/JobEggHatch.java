package net.tropicraft.entity.ai.jobs;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.tropicraft.entity.hostile.SpiderChild;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobEggHatch extends JobBase {

    public boolean inStasis;
    public int countdownHatch;
    public int countdownHatchMax;
    public Vec3 lockPos;
    public int motherID;

    public JobEggHatch(final JobManager jm) {
        super(jm);
        this.inStasis = true;
        this.countdownHatch = 0;
        this.countdownHatchMax = 300;
        this.lockPos = null;
        this.motherID = -1;
    }

    public boolean shouldExecute() {
        return true;
    }

    public boolean shouldContinue() {
        return !this.inStasis;
    }

    public boolean hookHit(final DamageSource ds, final int damage) {
        if (ds.getEntity() instanceof EntityPlayer && this.inStasis) {
            this.startHatching();
        }
        return true;
    }

    public void onIdleTickAct() {}

    public void tick() {
        super.tick();
        this.ai.entityToAttack = null;
        if (this.lockPos == null) {
            this.lockPos = Vec3.createVectorHelper(this.ent.posX, this.ent.posY, this.ent.posZ);
        } else {
            this.ent.getNavigator()
                .clearPathEntity();
        }
        if (!this.inStasis) {
            if (this.countdownHatch > 0) {
                --this.countdownHatch;
                if (this.countdownHatch == 0) {
                    final Random rand = new Random();
                    for (int spawnCount = rand.nextInt(3) + 2, i = 0; i < spawnCount; ++i) {
                        final SpiderChild spider = new SpiderChild(this.ent.worldObj);
                        spider.setPosition(this.ent.posX, this.ent.posY, this.ent.posZ);
                        spider.motionX = (rand.nextDouble() - rand.nextDouble()) * 0.5;
                        spider.motionY = (rand.nextDouble() - rand.nextDouble()) * 0.5;
                        spider.motionZ = (rand.nextDouble() - rand.nextDouble()) * 0.5;
                        this.ent.worldObj.spawnEntityInWorld((Entity) spider);
                        this.ent.setDead();
                    }
                }
            }
        } else {
            final EntityPlayer player = this.ent.worldObj.getClosestVulnerablePlayerToEntity((Entity) this.ent, 48.0);
            if (player != null && player.canEntityBeSeen((Entity) this.ent)) {
                this.startHatching();
            }
        }
    }

    public void startHatching() {
        this.inStasis = false;
        this.countdownHatch = this.countdownHatchMax;
    }
}
