package net.tropicraft.entity.ai.jobs;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.tropicraft.entity.hostile.*;

import CoroUtil.componentAI.jobSystem.*;
import CoroUtil.entity.*;
import CoroUtil.pathfinding.*;

public class JobHuntAshen extends JobBase {

    public long huntRange;
    public long keepDistantRange;
    public boolean xRay;
    public boolean useMelee;
    public int useMeleeCountdown;
    public int useMeleeCountdownMax;
    public Vec3 targetLastPos;
    public int targetNoMoveTicks;
    public int targetNoMoveTicksMax;
    public int panicTicks;

    public JobHuntAshen(final JobManager jm) {
        super(jm);
        this.huntRange = 24L;
        this.keepDistantRange = 14L;
        this.xRay = false;
        this.useMelee = false;
        this.useMeleeCountdown = 0;
        this.useMeleeCountdownMax = 80;
        this.targetLastPos = null;
        this.targetNoMoveTicks = 0;
        this.targetNoMoveTicksMax = 4;
        this.panicTicks = 0;
    }

    public boolean shouldExecute() {
        return true;
    }

    public boolean shouldContinue() {
        return this.ai.entityToAttack == null || this.ai.entityToAttack.getDistanceToEntity(this.ent) > this.huntRange;
    }

    public void onLowHealth() {
        if (this.ai.lastFleeEnt == null) {
            return;
        }
        super.onLowHealth();
        if (this.hitAndRunDelay == 0 && this.ent.getDistanceToEntity(this.ai.lastFleeEnt) > 3.0f) {
            this.hitAndRunDelay = this.entInt.getCooldownRanged() + 1;
            this.ai.entityToAttack = this.ai.lastFleeEnt;
            if (this.ai.entityToAttack != null) {
                this.ent.faceEntity(this.ai.entityToAttack, 180.0f, 180.0f);
                if (this.ai.useInv) {
                    this.ai.entInv.performRightClick();
                } else {
                    this.entInt.attackRanged(this.ai.entityToAttack, this.ent.getDistanceToEntity(this.ai.lastFleeEnt));
                }
            }
        }
    }

    public boolean shouldTickCloseCombat() {
        return this.useMelee && super.shouldTickCloseCombat();
    }

    public boolean hookHit(final DamageSource ds, final int damage) {
        if (this.ent.getDataWatcher()
            .getWatchableObjectInt(16) != -1) {
            this.panicTicks = 40;
            final EntityLostMask mask = new EntityLostMask(
                this.ent.worldObj,
                this.ent.getDataWatcher()
                    .getWatchableObjectInt(16),
                this.ent.posX,
                this.ent.posY + 1.0,
                this.ent.posZ,
                this.ent.rotationYaw);
            this.ent.getDataWatcher()
                .updateObject(16, -1);
            this.ent.worldObj.spawnEntityInWorld(mask);
        }
        return true;
    }

    public void setJobItems() {}

    public boolean checkDangers() {
        boolean returnVal = false;
        if (this.ent.getDataWatcher()
            .getWatchableObjectInt(16) == -1) {
            this.ai.entityToAttack = null;
            this.ent.getDataWatcher()
                .updateObject(17, 1);
            returnVal = true;
        } else if (this.ai.entityToAttack != null) {
            this.ent.getDataWatcher()
                .updateObject(17, 2);
        } else {
            this.ent.getDataWatcher()
                .updateObject(17, 0);
        }
        if (!returnVal && !this.useMelee
            && this.ai.entityToAttack != null
            && this.ai.entityToAttack.getDistanceToEntity(this.ai.ent) < this.keepDistantRange - 2L) {
            returnVal = true;
        }
        return returnVal || this.checkHealth();
    }

    public boolean avoid(final boolean actOnTrue) {
        final int range = 25;
        boolean seesMask = false;
        EntityLostMask clEnt = null;
        float closest = 9999.0f;
        if (this.panicTicks > 0) {
            --this.panicTicks;
        }
        if (this.ent.worldObj.getWorldTime() % 5L != 0L) {
            return true;
        }
        if (this.panicTicks <= 0) {
            final List<?> list = this.ent.worldObj
                .getEntitiesWithinAABB(EntityLostMask.class, this.ent.boundingBox.expand(range, range / 2.0, range));
            for (final Object o : list) {
                final EntityLostMask entity1 = (EntityLostMask) o;
                if (!entity1.isDead) {
                    seesMask = true;
                    final float dist = this.ent.getDistanceToEntity(entity1);
                    if (dist < closest) {
                        closest = dist;
                        clEnt = entity1;
                    }
                }
            }
        }
        if (seesMask) {
            if (closest < 2.0) {
                this.ent.getDataWatcher()
                    .updateObject(
                        16,
                        clEnt.getDataWatcher()
                            .getWatchableObjectInt(17));
                clEnt.setDead();
            } else if (this.ent.worldObj.getWorldTime() % 10L == 0L) {
                PFQueue.getPath(this.ent, clEnt, range + 16.0f);
            }
            return true;
        }
        return super.avoid(actOnTrue);
    }

    public void tick() {
        super.tick();
        this.jobHunter();
    }

    protected void jobHunter() {
        this.dontStrayFromHome = false;
        this.ai.maxDistanceFromHome = 48.0;
        if (this.ai.entityToAttack != null && this.targetLastPos != null) {
            if (this.ent.worldObj.getWorldTime() % 10L == 0L) {
                if (this.ai.entityToAttack
                    .getDistance(this.targetLastPos.xCoord, this.targetLastPos.yCoord, this.targetLastPos.zCoord)
                    < 0.5) {
                    ++this.targetNoMoveTicks;
                } else {
                    this.targetNoMoveTicks = 0;
                }
            }
            if (this.targetNoMoveTicks >= this.targetNoMoveTicksMax) {
                this.useMeleeCountdown = this.useMeleeCountdownMax;
            }
        } else {
            this.useMeleeCountdown = 0;
        }
        if (this.useMeleeCountdown > 0) {
            --this.useMeleeCountdown;
            this.useMelee = true;
        } else {
            this.useMelee = false;
        }
        this.setJobState(EnumJobState.IDLE);
        if (this.ent.getHealth() > this.ent.getHealth() * 0.9f
            && (this.ai.entityToAttack == null || this.ai.rand.nextInt(20) == 0)) {
            Entity clEnt = null;
            float closest = 9999.0f;
            final List<?> list = this.ent.worldObj.getEntitiesWithinAABBExcludingEntity(
                this.ent,
                this.ent.boundingBox.expand(this.huntRange, this.huntRange / 2.0, this.huntRange));
            for (final Object o : list) {
                final Entity entity1 = (Entity) o;
                if (this.isEnemy(entity1) && (this.xRay || ((EntityLivingBase) entity1).canEntityBeSeen(this.ent))
                    && this.sanityCheck(entity1)) {
                    final float dist = this.ent.getDistanceToEntity(entity1);
                    if (dist < closest) {
                        closest = dist;
                        clEnt = entity1;
                    }
                }
            }
            if (clEnt != null) {
                if (this.ai.entityToAttack != clEnt) {
                    this.ai.setTarget(clEnt);
                } else {
                    this.ai.setTarget(clEnt);
                }
            }
        } else if (this.ai.entityToAttack != null) {
            if (!this.useMelee && this.ai.entityToAttack != null
                && this.ai.entityToAttack.getDistanceToEntity(this.ai.ent) < this.keepDistantRange - 2L) {
                this.ent.getNavigator()
                    .clearPathEntity();
            }
            if (this.ent.getNavigator()
                .noPath()
                && (this.ent.getDistanceToEntity(this.ai.entityToAttack) > this.keepDistantRange + 1L
                    || this.useMelee)) {
                PFQueue.getPath(this.ent, this.ai.entityToAttack, (float) this.ai.maxPFRange);
            } else if (!this.useMelee && !this.ai.fleeing
                && this.ai.entityToAttack.getDistanceToEntity(this.ent) < this.keepDistantRange) {
                    this.ent.getNavigator()
                        .clearPathEntity();
                }
        }
        if (this.ent.worldObj.getWorldTime() % 10L == 0L && this.ai.entityToAttack != null) {
            this.targetLastPos = Vec3.createVectorHelper(
                this.ai.entityToAttack.posX,
                this.ai.entityToAttack.posY,
                this.ai.entityToAttack.posZ);
        }
        this.ent.prevHealth = this.ent.getHealth();
    }

    public boolean sanityCheckHelp(final Entity caller, final Entity target) {
        return this.ent.getHealth() >= 10.0f
            && (!this.dontStrayFromHome
                || target.getDistance(this.ai.homeX, this.ai.homeY, this.ai.homeZ) <= this.ai.maxDistanceFromHome * 1.5)
            && this.ai.rand.nextInt(2) == 0;
    }

    public boolean sanityCheck(final Entity target) {
        return this.ent.getHealth() >= 6.0f && (!this.dontStrayFromHome
            || target.getDistance(this.ai.homeX, this.ai.homeY, this.ai.homeZ) <= this.ai.maxDistanceFromHome);
    }
}
