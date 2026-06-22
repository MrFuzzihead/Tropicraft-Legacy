package net.tropicraft.entity.passive;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityTropicraftAnimal;
import net.tropicraft.registry.TCItemRegistry;

public class EntityIguana extends EntityTropicraftAnimal implements IMob {

    private int angerLevel;

    public EntityIguana(final World world) {
        super(world);
        this.isImmuneToFire = true;
        this.setSize(1.0f, 0.4f);
    }

    public boolean attackEntityFrom(final DamageSource damagesource, final float range) {
        if (this.isEntityInvulnerable()) {
            return false;
        }
        final Entity entity = damagesource.getEntity();
        if (entity instanceof EntityPlayer) {
            final List list = this.worldObj
                .getEntitiesWithinAABBExcludingEntity((Entity) this, this.boundingBox.expand(32.0, 32.0, 32.0));
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = (Entity) list.get(i);
                if (entity2 instanceof EntityIguana) {
                    final EntityIguana iggy = (EntityIguana) entity2;
                    iggy.becomeAngryAt(entity);
                }
            }
            this.becomeAngryAt(entity);
        }
        return super.attackEntityFrom(damagesource, range);
    }

    private void becomeAngryAt(final Entity entity) {
        this.entityToAttack = entity;
        this.angerLevel = 400 + this.rand.nextInt(400);
    }

    public void attackEntity(final Entity entity, final float range) {
        if (range > 2.0f && range < 6.0f && this.rand.nextInt(10) == 0) {
            if (this.onGround) {
                final double d = entity.posX - this.posX;
                final double d2 = entity.posZ - this.posZ;
                final float f1 = MathHelper.sqrt_double(d * d + d2 * d2);
                this.motionX = d / f1 * 0.5 * 0.800000011920929 + this.motionX * 0.20000000298023224;
                this.motionZ = d2 / f1 * 0.5 * 0.800000011920929 + this.motionZ * 0.20000000298023224;
                this.motionY = 0.4000000059604645;
            }
        } else if (range < 1.5f && entity.boundingBox.maxY > this.boundingBox.minY
            && entity.boundingBox.minY < this.boundingBox.maxY) {
                this.attackTime = 20;
                final byte byte0 = 2;
                entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this), (float) byte0);
            }
    }

    public boolean isAIEnabled() {
        return false;
    }

    protected void fall(final float f) {}

    protected void dropFewItems(final boolean recentlyHit, final int looting) {
        for (int numDrops = 3 + this.rand.nextInt(1 + looting), i = 0; i < numDrops; ++i) {
            this.dropItem((Item) TCItemRegistry.scale, 1);
        }
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(1.0);
    }

    public Entity findPlayerToAttack() {
        if (this.angerLevel == 0) {
            return null;
        }
        final EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity((Entity) this, 16.0);
        return (Entity) ((entityplayer != null && this.canEntityBeSeen((Entity) entityplayer)) ? entityplayer : null);
    }

    public float getSoundVolume() {
        return 0.4f;
    }

    protected String getLivingSound() {
        return this.tcSound("iggyliving");
    }

    protected String getHurtSound() {
        return this.tcSound("iggyattack");
    }

    protected String getDeathSound() {
        return this.tcSound("iggydeath");
    }

    public void writeEntityToNBT(final NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setShort("Anger", (short) this.angerLevel);
    }

    public void readEntityFromNBT(final NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.angerLevel = nbt.getShort("Anger");
    }

    public EntityAgeable createChild(final EntityAgeable var1) {
        return (EntityAgeable) new EntityIguana(this.worldObj);
    }

    public int getMaxSpawnedInChunk() {
        return 6;
    }
}
