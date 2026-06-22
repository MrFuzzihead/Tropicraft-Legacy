package net.tropicraft.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityTropicraftAnimal;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;

public abstract class EntityTreeFrog extends EntityTropicraftAnimal
    implements IEntityAdditionalSpawnData, IRangedAttackMob {

    public int type;
    public int jumpDelay;
    public int jumpDelayMax;
    public boolean wasOnGround;
    public double leapVecX;
    public double leapVecZ;

    public EntityTreeFrog(final World world) {
        super(world);
        this.type = 0;
        this.jumpDelay = 0;
        this.jumpDelayMax = 10;
        this.wasOnGround = false;
        this.setSize(0.8f, 0.8f);
        this.entityCollisionReduction = 0.8f;
        this.experienceValue = 5;
        this.getNavigator()
            .setAvoidsWater(true);
        this.tasks.addTask(0, (EntityAIBase) new EntityAISwimming((EntityLiving) this));
        this.tasks.addTask(1, (EntityAIBase) new EntityAIArrowAttack((IRangedAttackMob) this, 1.0, 60, 10.0f));
        this.tasks.addTask(2, (EntityAIBase) new EntityAIWander((EntityCreature) this, 1.0));
        this.targetTasks.addTask(
            1,
            (EntityAIBase) new EntityAINearestAttackableTarget(
                (EntityCreature) this,
                (Class) EntityPlayer.class,
                0,
                true));
    }

    public EntityTreeFrog(final World world, final int parType) {
        this(world);
        this.type = parType;
    }

    public boolean isAIEnabled() {
        return true;
    }

    public void updateAITasks() {
        super.updateAITasks();
        if (!this.getNavigator()
            .noPath() || this.getAttackTarget() != null) {
            if (this.onGround || this.isInWater()) {
                if (this.jumpDelay > 0) {
                    --this.jumpDelay;
                }
                if (this.jumpDelay <= 0) {
                    this.jumpDelay = this.rand.nextInt(4);
                    this.getNavigator()
                        .onUpdateNavigation();
                    this.jump();
                    this.motionY += -0.01 + this.rand.nextDouble() * 0.1;
                    double speed = 0.4;
                    Label_0237: {
                        if (this.getNavigator()
                            .getPath() != null) {
                            try {
                                if (this.getNavigator()
                                    .getPath()
                                    .getCurrentPathIndex()
                                    > this.getNavigator()
                                        .getPath()
                                        .getCurrentPathLength()) {
                                    this.getNavigator()
                                        .clearPathEntity();
                                    return;
                                }
                                final Vec3 pos = this.getNavigator()
                                    .getPath()
                                    .getPosition((Entity) this);
                                this.leapVecX = pos.xCoord - this.posX;
                                this.leapVecZ = pos.zCoord - this.posZ;
                                speed = 0.2;
                                break Label_0237;
                            } catch (Exception ex) {
                                return;
                            }
                        }
                        if (this.getAttackTarget() != null) {
                            this.leapVecX = this.getAttackTarget().posX - this.posX;
                            this.leapVecZ = this.getAttackTarget().posZ - this.posZ;
                        }
                    }
                    if (this.leapVecX != 0.0) {
                        final double dist2 = Math.sqrt(this.leapVecX * this.leapVecX + this.leapVecZ * this.leapVecZ);
                        this.motionX += this.leapVecX / dist2 * speed;
                        this.motionZ += this.leapVecZ / dist2 * speed;
                    }
                }
            } else if (this.leapVecX != 0.0) {
                double speed = 0.1;
                if (this.isInWater()) {
                    speed = 0.2;
                }
                final double dist2 = Math.sqrt(this.leapVecX * this.leapVecX + this.leapVecZ * this.leapVecZ);
                this.motionX += this.leapVecX / dist2 * speed;
                this.motionZ += this.leapVecZ / dist2 * speed;
            }
            if (this.isInWater()) {
                this.motionY += 0.07;
            }
        }
        this.wasOnGround = this.onGround;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(5.0);
    }

    protected void dropFewItems(final boolean par1, final int par2) {
        for (int j = 2, k = 0; k < j; ++k) {
            this.dropItem((Item) TCItemRegistry.frogLeg, 1);
        }
    }

    public EntityAgeable createChild(final EntityAgeable var1) {
        return null;
    }

    public void writeSpawnData(final ByteBuf buf) {
        buf.writeInt(this.type);
    }

    public void readSpawnData(final ByteBuf buf) {
        this.type = buf.readInt();
    }

    public void attackEntityWithRangedAttack(final EntityLivingBase entity, final float f) {
        if (this.type == 0) {
            return;
        }
        if (f < 4.0f && !this.worldObj.isRemote
            && this.attackTime == 0
            && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
            final double d = entity.posX - this.posX;
            final double d2 = entity.posZ - this.posZ;
            final EntityPoisonBlot entityPoisonBlot;
            final EntityPoisonBlot entitypoisonblot = entityPoisonBlot = new EntityPoisonBlot(
                this.worldObj,
                (EntityLivingBase) this);
            entityPoisonBlot.posY += 1.399999976158142;
            final double d3 = entity.posY + entity.getEyeHeight() - 0.20000000298023224 - entitypoisonblot.posY;
            final float f2 = MathHelper.sqrt_double(d * d + d2 * d2) * 0.2f;
            this.worldObj
                .playSoundAtEntity((Entity) this, "frogspit", 1.0f, 1.0f / (this.rand.nextFloat() * 0.4f + 0.8f));
            this.worldObj.spawnEntityInWorld((Entity) entitypoisonblot);
            entitypoisonblot.setThrowableHeading(d, d3 + f2, d2, 0.6f, 12.0f);
            this.attackTime = 50;
            this.rotationYaw = (float) (Math.atan2(d2, d) * 180.0 / 3.1415927410125732) - 90.0f;
            this.hasAttacked = true;
        }
    }

    public float getShadowSize() {
        return 0.0f;
    }
}
