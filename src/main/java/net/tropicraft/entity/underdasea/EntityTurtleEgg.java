package net.tropicraft.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityTurtleEgg extends EntityLiving {

    public int hatchingTime;
    public double rotationRand;

    public EntityTurtleEgg(final World par1World) {
        super(par1World);
        this.setSize(0.1f, 0.1f);
        this.hatchingTime = 0;
        this.rotationRand = 10.0;
        this.ignoreFrustumCheck = true;
    }

    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0.0f;
        if (this.ticksExisted % 400 == 0) {
            this.hatchingTime = 360;
        }
        if (this.hatchingTime != 0) {
            this.rotationRand += 1.5707f * this.worldObj.rand.nextFloat();
            --this.hatchingTime;
            if (this.hatchingTime == 1) {
                if (!this.worldObj.isRemote) {
                    final EntitySeaTurtle babyturtle = new EntitySeaTurtle(this.worldObj, 0.2f);
                    final double d3 = this.posX;
                    final double d4 = this.posY;
                    final double d5 = this.posZ;
                    babyturtle.setLocationAndAngles(d3, d4, d5, 0.0f, 0.0f);
                    this.worldObj.spawnEntityInWorld((Entity) babyturtle);
                    this.setDead();
                }
                for (int i = 0; i < 8; ++i) {
                    this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
                }
            }
            if (this.hatchingTime == 0) {
                this.rotationRand = 0.0;
            }
        }
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(2.0);
    }
}
