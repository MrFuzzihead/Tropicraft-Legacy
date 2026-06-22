package net.tropicraft.entity.ai.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.tropicraft.entity.hostile.SpiderEgg;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobEggManage extends JobBase {

    public int eggScanRangeMotherToEgg;
    public int eggScanRangeEggToEgg;
    public int eggSpawnCountdown;
    public int eggSpawnCountdownMax;
    public int eggSpawnMax;
    public int eggMaxDistGuard;
    public List<SpiderEgg> eggs;

    public JobEggManage(final JobManager jm) {
        super(jm);
        this.eggScanRangeMotherToEgg = 24;
        this.eggScanRangeEggToEgg = 6;
        this.eggSpawnCountdown = -1;
        this.eggSpawnCountdownMax = 60;
        this.eggSpawnMax = -1;
        this.eggMaxDistGuard = 8;
        this.eggs = new ArrayList<SpiderEgg>();
    }

    public boolean shouldExecute() {
        return true;
    }

    public boolean shouldContinue() {
        return true;
    }

    public boolean hookHit(final DamageSource ds, final int damage) {
        if (ds.getEntity() instanceof EntityLivingBase) {
            this.startEggHatchTimer();
        }
        return true;
    }

    public void tick() {
        super.tick();
        if (this.eggSpawnCountdown > 0) {
            --this.eggSpawnCountdown;
        }
        if (this.eggSpawnCountdown == -1 && this.ent.worldObj.getWorldTime() % 80L == 0L) {
            this.eggSpawnCountdown = this.eggSpawnCountdownMax;
            final List<SpiderEgg> foundEggs = (List<SpiderEgg>) this.ent.worldObj.getEntitiesWithinAABB(
                (Class) SpiderEgg.class,
                this.ent.boundingBox.expand(
                    (double) this.eggScanRangeMotherToEgg,
                    (double) this.eggScanRangeMotherToEgg,
                    (double) this.eggScanRangeMotherToEgg));
            for (int i = 0; i < foundEggs.size(); ++i) {
                if (foundEggs.get(i).motherID == this.ai.entID) {
                    this.eggs.add(foundEggs.get(i));
                }
            }
        }
        if (this.ent.onGround && this.ent.getNavigator()
            .noPath() && this.eggs.size() < this.eggSpawnMax && this.eggSpawnCountdown == 0) {
            this.eggSpawnCountdown = this.eggSpawnCountdownMax;
            final Random rand = new Random();
            Vec3 spawnCoords = null;
            if (this.eggs.size() == 0) {
                spawnCoords = Vec3.createVectorHelper(this.ent.posX, this.ent.posY, this.ent.posZ);
            } else {
                final SpiderEgg spawnBesideEgg = this.eggs.get(rand.nextInt(this.eggs.size()));
                final double range = 1.0;
                spawnCoords = Vec3.createVectorHelper(spawnBesideEgg.posX, spawnBesideEgg.posY, spawnBesideEgg.posZ);
            }
            final SpiderEgg eggSpawn = new SpiderEgg(this.ent.worldObj);
            eggSpawn.setPosition(spawnCoords.xCoord, spawnCoords.yCoord, spawnCoords.zCoord);
            eggSpawn.onSpawnWithEgg(null);
            eggSpawn.motherID = this.ai.entID;
            this.ent.worldObj.spawnEntityInWorld((Entity) eggSpawn);
            this.eggs.add(eggSpawn);
        }
        if (this.eggs.size() > 0 && this.ent.worldObj.getWorldTime() % 80L == 0L) {
            for (int j = 0; j < this.eggs.size(); ++j) {
                if (this.eggs.get(j).isDead) {
                    this.eggs.remove(j);
                    --j;
                }
            }
            if (this.eggs.size() > 0 && this.ent.getNavigator()
                .noPath() && this.ent.getDistanceToEntity((Entity) this.eggs.get(0)) > this.eggMaxDistGuard) {
                this.ai.walkTo(
                    (Entity) this.ent,
                    MathHelper.floor_double(this.eggs.get(0).posX),
                    MathHelper.floor_double(this.eggs.get(0).posY),
                    MathHelper.floor_double(this.eggs.get(0).posZ),
                    (float) this.ai.maxPFRange,
                    600);
            }
            if (this.ai.entityToAttack instanceof EntityLivingBase) {
                this.startEggHatchTimer();
            }
        }
    }

    public void startEggHatchTimer() {
        for (int i = 0; i < this.eggs.size(); ++i) {
            if (this.eggs.get(i).job.inStasis) {
                this.eggs.get(i).job.startHatching();
            }
        }
    }
}
