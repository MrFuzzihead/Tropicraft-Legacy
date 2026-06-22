package net.tropicraft.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class AIAshenChaseAndPickupLostMask extends EntityAIBase {

    public EntityAshen ashen;
    public EntityLivingBase target;
    public double speed;
    public double maskGrabDistance;
    public int panicTime;

    public AIAshenChaseAndPickupLostMask(final EntityAshen ashen, final double speed) {
        this.speed = 1.0;
        this.maskGrabDistance = 3.0;
        this.panicTime = 0;
        this.ashen = ashen;
        this.speed = speed;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return !this.ashen.hasMask();
    }

    public boolean continueExecuting() {
        if (this.ashen.maskToTrack == null) {
            return false;
        }
        if (this.panicTime > 0) {
            --this.panicTime;
            if (this.ashen.worldObj.getTotalWorldTime() % 10L == 0L) {
                final Vec3 vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature) this.ashen, 10, 7);
                if (vec3 == null) {
                    return false;
                }
                this.ashen.getNavigator()
                    .tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
                return true;
            }
        } else if (this.ashen.getDistanceSqToEntity((Entity) this.ashen.maskToTrack) <= this.maskGrabDistance) {
            this.ashen.pickupMask(this.ashen.maskToTrack);
        } else if (this.ashen.worldObj.getTotalWorldTime() % 40L == 0L) {
            this.ashen.getNavigator()
                .tryMoveToXYZ(
                    this.ashen.maskToTrack.posX,
                    this.ashen.maskToTrack.posY,
                    this.ashen.maskToTrack.posZ,
                    this.speed);
        }
        return this.shouldExecute() || !this.ashen.getNavigator()
            .noPath();
    }

    public void startExecuting() {
        super.startExecuting();
        this.panicTime = 120;
    }

    public void resetTask() {
        this.target = null;
    }
}
