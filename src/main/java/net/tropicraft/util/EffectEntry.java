package net.tropicraft.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EffectEntry {

    private Vec3 startPos;
    private EntityLivingBase entity;
    private int effectID;
    private int effectTime;

    public EffectEntry(final EntityLivingBase entity) {
        this.effectTime = 100;
        this.entity = entity;
        this.startPos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
        this.init();
    }

    public int getEffectTime() {
        return this.effectTime;
    }

    public void setEffectTime(final int effectTime) {
        this.effectTime = effectTime;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public void setEntity(final EntityLivingBase entity) {
        this.entity = entity;
    }

    public void tick() {
        if (this.entity instanceof EntityPlayer) {
            if (this.entity.worldObj.isRemote) {
                this.tickClientPlayer();
            }
        } else if (!this.entity.worldObj.isRemote) {
            this.tickServerAI();
        }
        if (this.isFinished()) {
            this.deinit();
        }
    }

    public void init() {}

    public void deinit() {}

    public boolean isFinished() {
        return this.effectTime == 0;
    }

    public void cleanup() {
        this.entity = null;
    }

    public void tickServerAI() {
        if (this.effectTime > 0) {
            --this.effectTime;
        }
        final EntityLiving entityLiving2;
        final EntityLiving entityLiving;
        final EntityLiving ent = entityLiving = (entityLiving2 = (EntityLiving) this.entity);
        final double n = 0.0;
        entityLiving.motionZ = n;
        entityLiving2.motionX = n;
        if (ent.motionY > 0.0) {
            ent.motionY = 0.0;
        }
        ent.setPosition(this.startPos.xCoord, this.startPos.yCoord, this.startPos.zCoord);
    }

    public void tickClientPlayer() {
        if (this.effectTime > 0) {
            --this.effectTime;
        }
        final EntityPlayer entityPlayer2;
        final EntityPlayer entityPlayer;
        final EntityPlayer ent = entityPlayer = (entityPlayer2 = (EntityPlayer) this.entity);
        final double n = 0.0;
        entityPlayer.motionZ = n;
        entityPlayer2.motionX = n;
        if (ent.motionY > 0.0) {
            ent.motionY = 0.0;
        }
        ent.setPosition(this.startPos.xCoord, this.startPos.yCoord, this.startPos.zCoord);
    }
}
