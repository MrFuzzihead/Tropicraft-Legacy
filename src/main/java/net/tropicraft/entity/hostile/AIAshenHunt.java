package net.tropicraft.entity.hostile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;

public class AIAshenHunt extends EntityAIBase {

    public EntityAshen ashen;
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
    public EntityLivingBase target;

    public AIAshenHunt(final EntityAshen ashen) {
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
        this.ashen = ashen;
    }

    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.ashen.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        this.target = entitylivingbase;
        return true;
    }

    public boolean continueExecuting() {
        return this.shouldExecute() || !this.ashen.getNavigator()
            .noPath();
    }

    public void resetTask() {
        this.target = null;
    }
}
