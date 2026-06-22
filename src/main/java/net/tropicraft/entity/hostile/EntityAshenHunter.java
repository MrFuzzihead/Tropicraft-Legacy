package net.tropicraft.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityDart;

public class EntityAshenHunter extends EntityAshen {

    public boolean hasGTFO;

    public EntityAshenHunter(final World par1World) {
        super(par1World);
        this.setActionState(2);
        this.actionPicker = 2;
        this.hasGTFO = false;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL && this.getActionState() != 1) {
            this.setActionState(0);
        } else if (this.getActionState() != 1) {
            this.setActionState(this.actionPicker);
        }
    }

    protected String getLivingSound() {
        return (this.getAttackTarget() == null) ? null : this.tcSound("ashenLaugh");
    }

    public void attackEntityWithRangedAttack(final EntityLivingBase entity, final float range) {
        if (this.getAttackTarget() != null) {
            this.faceEntity((Entity) this.getAttackTarget(), 180.0f, 180.0f);
            final EntityDart entitydart = new EntityDart(this.worldObj, (Entity) this, 3.0f, (short) 0);
            this.worldObj.spawnEntityInWorld((Entity) entitydart);
        }
    }

    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(this.getAttackStrength());
        return super.attackEntityAsMob(p_70652_1_);
    }

    protected double getAttackStrength() {
        if (this.worldObj == null) {
            return 0.0;
        }
        switch (this.worldObj.difficultySetting) {
            case EASY: {
                return 1.0;
            }
            case NORMAL: {
                return 2.0;
            }
            case HARD: {
                return 3.0;
            }
            default: {
                return 0.0;
            }
        }
    }
}
