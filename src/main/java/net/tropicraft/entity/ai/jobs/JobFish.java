package net.tropicraft.entity.ai.jobs;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.tropicraft.entity.underdasea.EntityTropicraftWaterMob;
import net.tropicraft.registry.TCItemRegistry;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.entity.EntityTropicalFishHook;
import CoroUtil.entity.EnumActState;
import CoroUtil.entity.EnumJobState;
import CoroUtil.inventory.AIInventory;
import CoroUtil.util.CoroUtilEntity;
import CoroUtil.util.CoroUtilInventory;
import CoroUtil.util.CoroUtilItem;

public class JobFish extends JobBase {

    public float maxCastStr;
    public int fishingTimeout;
    public int dryCastX;
    public int dryCastY;
    public int dryCastZ;
    public int pfRangeHome;
    public float castingStrength;

    public JobFish(final JobManager jm) {
        super(jm);
        this.maxCastStr = 1.0f;
        this.pfRangeHome = 128;
        this.castingStrength = 1.0f;
    }

    public boolean shouldExecute() {
        return this.ai.entityToAttack == null;
    }

    public boolean shouldContinue() {
        return this.state == EnumJobState.IDLE;
    }

    public void onIdleTick() {
        super.onIdleTick();
    }

    public void setJobState(final EnumJobState ekos) {
        super.setJobState(ekos);
    }

    public void onLowHealth() {
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

    public void tick() {
        super.tick();
        if (this.ai.entInv.inventory == null) {
            return;
        }
        if (this.ai.entInv != null && this.getFishEntity() != null
            && (this.state == EnumJobState.IDLE || this.getFishEntity().isDead)) {
            this.getFishEntity()
                .setDead();
            this.getFishEntity()
                .catchFish();
            this.setFishEntity(null);
        }
        final Random rand = new Random();
        this.ai.maxDistanceFromHome = 16.0;
        if (this.state == EnumJobState.IDLE) {
            if (this.findWater()) {
                this.setJobState(EnumJobState.W1);
            } else if (rand.nextInt(150) == 0 && this.ent.getNavigator()
                .noPath()) {
                    this.ai.updateWanderPath();
                }
        } else if (this.state == EnumJobState.W1) {
            if (this.ai.targY < 0) {
                this.setJobState(EnumJobState.IDLE);
                return;
            }
            if (!this.ent.isInWater()) {
                if (this.walkingTimeout <= 0 || this.ent.getNavigator()
                    .noPath()) {
                    final float tdist = (float) this.ent
                        .getDistance((double) this.ai.targX, (double) this.ai.targY, (double) this.ai.targZ);
                    this.ai.walkTo(
                        (Entity) this.ent,
                        this.ai.targX,
                        this.ai.targY,
                        this.ai.targZ,
                        (float) this.pfRangeHome,
                        600);
                }
            } else if (this.findLandClose()) {
                this.setJobState(EnumJobState.W4);
            }
            final double distStart = 0.5;
            final double distEnd = 1.0;
            final double distStep = 0.5;
            final double lookStartStop = 0.0;
            final double lookStep = 30.0;
            final boolean test = this.isFacingWater(distStart, distEnd, distStep, lookStartStop, lookStep);
            if (this.ent.getDistance((double) this.ai.targX, (double) this.ai.targY, (double) this.ai.targZ) < 8.0
                || this.ent.isInWater()
                || test) {
                if (CoroUtilEntity
                    .canCoordBeSeenFromFeet((EntityLivingBase) this.ent, this.ai.targX, this.ai.targY, this.ai.targZ)
                    || test) {
                    this.ai.setState(EnumActState.IDLE);
                    this.setJobState(EnumJobState.W2);
                    this.ent.getNavigator()
                        .clearPathEntity();
                    this.castLine();
                } else {
                    this.setJobState(EnumJobState.IDLE);
                }
            }
        } else if (this.state == EnumJobState.W2) {
            if (!this.ent.isInWater()) {
                this.ent.getNavigator()
                    .clearPathEntity();
                this.ai.faceCoord(this.ai.targX, this.ai.targY, this.ai.targZ, 90.0f, 90.0f);
            } else if (this.findLandClose()) {
                if (this.getFishEntity() != null) {
                    this.catchFish();
                }
                this.setJobState(EnumJobState.W4);
                return;
            }
            if (this.getFishEntity() != null && this.getFishEntity().bobber instanceof EntityTropicraftWaterMob) {
                final EntityTropicraftWaterMob fish = (EntityTropicraftWaterMob) this.getFishEntity().bobber;
                if (fish.outOfWaterTick > 10) {
                    final AIInventory entInv = this.ai.entInv;
                    final AIInventory entInv2 = this.ai.entInv;
                    entInv.setSlotActive(AIInventory.slot_Melee);
                    this.ai.entInv.performLeftClick((Entity) fish, 0.0f);
                    if (fish.getHealth() < 0.0f) {
                        this.catchFish();
                    }
                }
            }
            if (this.getFishEntity() == null || this.fishingTimeout <= 0
                || (this.getFishEntity() != null && (this.getFishEntity().inGround
                    || (this.getFishEntity().ticksCatchable > 0 && this.getFishEntity().ticksCatchable < 10)))) {
                if (this.getFishEntity() != null) {
                    this.catchFish();
                }
                if (this.getFishCount() > 4 || (rand.nextInt(1) == 0 && this.getFishCount() >= 2)) {
                    this.ai.walkTo(
                        (Entity) this.ent,
                        this.ai.homeX,
                        this.ai.homeY,
                        this.ai.homeZ,
                        (float) this.pfRangeHome,
                        600);
                    this.setJobState(EnumJobState.W3);
                } else if (rand.nextInt(2) == 0) {
                    this.setJobState(EnumJobState.IDLE);
                } else {
                    this.castLine();
                }
                this.ai.setState(EnumActState.IDLE);
            } else {
                --this.fishingTimeout;
            }
        } else if (this.state == EnumJobState.W3) {
            if (this.getFishEntity() != null) {
                this.catchFish();
            }
            if (this.walkingTimeout <= 0 || (this.ent.getNavigator()
                .noPath() && this.ent.worldObj.getWorldTime() % 20L == 0L)) {
                this.ai.walkTo(
                    (Entity) this.ent,
                    this.ai.homeX,
                    this.ai.homeY,
                    this.ai.homeZ,
                    (float) this.pfRangeHome,
                    600);
            }
            if (this.ent.getDistance((double) this.ai.homeX, (double) this.ai.homeY, (double) this.ai.homeZ) < 2.0) {
                this.ai.faceCoord(
                    (int) (this.ai.homeX - 0.5f),
                    this.ai.homeY,
                    (int) (this.ai.homeZ - 0.5f),
                    180.0f,
                    180.0f);
                this.transferJobItems(this.ai.homeX, this.ai.homeY, this.ai.homeZ);
                this.setJobState(EnumJobState.IDLE);
            }
        } else if (this.state == EnumJobState.W4) {
            if (this.ent.getDistance((double) this.dryCastX, this.ent.posY, (double) this.dryCastZ) < 5.0
                || this.ent.onGround) {
                this.dryCastX = (int) Math.floor(this.ent.posX + 0.5);
                this.dryCastY = (int) this.ent.boundingBox.minY;
                this.dryCastZ = (int) Math.floor(this.ent.posZ + 0.5);
                this.ai.setState(EnumActState.IDLE);
                this.ent.getNavigator()
                    .clearPathEntity();
                this.castLine();
                this.setJobState(EnumJobState.W2);
                return;
            }
            if (this.walkingTimeout <= 0 || (this.ent.getNavigator()
                .noPath() && this.ent.worldObj.getWorldTime() % 20L == 0L)) {
                if (this.ent.getDistance((double) this.dryCastX, (double) this.dryCastY, (double) this.dryCastZ)
                    < 64.0) {
                    this.ai.walkTo(
                        (Entity) this.ent,
                        this.dryCastX,
                        this.dryCastY,
                        this.dryCastZ,
                        (float) this.ai.maxPFRange,
                        600);
                } else {
                    this.ai.walkTo(
                        (Entity) this.ent,
                        this.ai.targX,
                        this.ai.targY,
                        this.ai.targZ,
                        (float) this.ai.maxPFRange,
                        600);
                }
            }
        }
    }

    public void catchFish() {
        if (this.getFishEntity() == null) {
            return;
        }
        this.getFishEntity()
            .setDead();
        this.getFishEntity()
            .catchFish();
        this.setFishEntity(null);
    }

    public boolean hookHit(final DamageSource ds, final int damage) {
        if (this.getFishEntity() != null) {
            this.catchFish();
            this.ai.setState(EnumActState.IDLE);
        }
        return super.hookHit(ds, damage);
    }

    protected boolean findLandClose() {
        if (this.findLand()) {
            this.catchFish();
            this.dryCastX = this.ai.targX;
            this.dryCastY = this.ai.targY;
            this.dryCastZ = this.ai.targZ;
            return true;
        }
        return false;
    }

    protected void castLine() {
        if (!this.ent.isInWater()) {
            this.dryCastX = (int) Math.floor(this.ent.posX + 0.5);
            this.dryCastY = (int) this.ent.boundingBox.minY;
            this.dryCastZ = (int) Math.floor(this.ent.posZ + 0.5);
        }
        final double dist = this.ent
            .getDistance((double) this.ai.targX, (double) this.ai.targY, (double) this.ai.targZ);
        this.ai.faceCoord(this.ai.targX, this.ai.targY, this.ai.targZ, 180.0f, 180.0f);
        this.castingStrength = (float) dist / 17.0f;
        if (this.castingStrength < 0.25) {
            this.castingStrength = 0.25f;
        }
        if (this.castingStrength > this.maxCastStr) {
            this.castingStrength = this.maxCastStr;
        }
        final EntityLiving ent = this.ent;
        ent.rotationPitch -= 25.0f;
        final AIInventory entInv = this.ai.entInv;
        final AIInventory entInv2 = this.ai.entInv;
        entInv.setSlotActive(AIInventory.slot_Tool);
        this.fishingTimeout = 400;
        if (this.getFishEntity() != null) {
            this.getFishEntity()
                .catchFish();
        }
        if (this.getFishEntity() != null) {
            this.getFishEntity()
                .setDead();
        }
        this.setFishEntity(null);
        this.ai.entInv.performRightClick();
    }

    public EntityTropicalFishHook getFishEntity() {
        return this.ai.entInv.fishEntity;
    }

    public void setFishEntity(final EntityTropicalFishHook parEnt) {
        this.ai.entInv.fishEntity = parEnt;
    }

    protected int getFishCount() {
        int count = 0;
        count += CoroUtilInventory.getItemCount(
            (IInventory) this.ai.entInv.inventory,
            Item.itemRegistry.getNameForObject((Object) Items.fish));
        count += CoroUtilInventory.getItemCount(
            (IInventory) this.ai.entInv.inventory,
            Item.itemRegistry.getNameForObject((Object) TCItemRegistry.freshMarlin));
        return count;
    }

    protected boolean isFish(final Item id) {
        return id == Items.fish || id == TCItemRegistry.freshMarlin;
    }

    protected void transferJobItems(final int x, int y, final int z) {
        if (CoroUtilInventory.isChest(this.ent.worldObj.getBlock(x, y - 1, z))) {
            --y;
        }
        final boolean transferred = false;
        if (!CoroUtilInventory.chestTryTransfer(this.ent.worldObj, this.ai.entInt, x, y, z)) {
            this.tossItems();
        }
    }

    public void tossItems() {
        for (int j = 0; j < this.ai.entInv.inventory.invList.length; ++j) {
            if (this.ai.entInv.inventory.invList[j] != null
                && this.isFish(this.ai.entInv.inventory.invList[j].getItem())) {
                CoroUtilItem.tossItemFromEntity((Entity) this.ent, this.ai.entInv.inventory.invList[j], false, false);
            }
        }
    }
}
