package net.tropicraft.entity.underdasea;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

public class EntityTropicalFish extends EntityTropicraftWaterMob {

    public boolean isLeader;
    public boolean inSchool;
    public EntityTropicalFish leader;
    public boolean targetHook;
    public Entity hook;
    public boolean hasBeenPlaced;
    public static final String[] names;
    private static final int SHOULD_SPAWN_DATAWATCHER = 17;
    private static final int DATA_COLOR = 16;

    public EntityTropicalFish(final World world) {
        super(world);
        this.targetHook = false;
        this.isLeader = true;
        this.inSchool = false;
        this.leader = null;
        this.setSize(0.6f, 0.6f);
        this.setColor(this.worldObj.rand.nextInt(EntityTropicalFish.names.length));
        this.setShouldSpawnSchool(true);
        this.isCatchable = true;
        this.experienceValue = 3;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(5.0);
    }

    public void setColor(final int col) {
        this.dataWatcher.updateObject(DATA_COLOR, col);
    }

    public int getColor() {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setShouldSpawnSchool(final boolean spawnStatus) {
        this.dataWatcher.updateObject(SHOULD_SPAWN_DATAWATCHER, (spawnStatus ? 1 : -1));
    }

    public boolean getShouldSpawnSchool() {
        return this.dataWatcher.getWatchableObjectInt(17) == 1;
    }

    public EntityTropicalFish(final World world, final EntityLiving entityliving, final int i) {
        super(world);
        this.setShouldSpawnSchool(false);
        this.targetHook = false;
        this.isLeader = true;
        this.inSchool = false;
        this.leader = null;
        this.setColor(i);
        this.setSize(0.4f, 0.85f);
        this.setLocationAndAngles(
            entityliving.posX,
            entityliving.posY + entityliving.getEyeHeight(),
            entityliving.posZ,
            entityliving.rotationYaw,
            entityliving.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.141593f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.141593f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.141593f)
            * MathHelper.cos(this.rotationPitch / 180.0f * 3.141593f);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.141593f)
            * MathHelper.cos(this.rotationPitch / 180.0f * 3.141593f);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.141593f);
    }

    public EntityTropicalFish(final EntityTropicalFish original) {
        this(original.worldObj);
        this.setShouldSpawnSchool(false);
        this.targetHook = false;
        this.isLeader = false;
        this.inSchool = true;
        this.leader = original;
        this.setColor(original.getColor());
        this.yOffset = 0.0f;
        this.setSize(original.width, original.height);
        do {
            final double offsetX = new Random().nextDouble() * 3.0 - 1.5;
            final double offsetY = new Random().nextDouble() * 2.0 + 1.0;
            final double offsetZ = new Random().nextDouble() * 3.0 - 1.5;
            this.setLocationAndAngles(
                original.posX + offsetX,
                original.posY + offsetY,
                original.posZ + offsetZ,
                original.rotationYaw,
                original.rotationPitch);
        } while (!this.getCanSpawnHere());
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(DATA_COLOR, 0);
        this.dataWatcher.addObject(SHOULD_SPAWN_DATAWATCHER, -1);
    }

    public void checkForLeader() {
        final List list = this.worldObj
            .getEntitiesWithinAABB((Class) EntityTropicalFish.class, this.boundingBox.expand(10.0, 10.0, 10.0));
        for (final Object ent : list) {
            if (((EntityTropicalFish) ent).getColor() == this.getColor()) {
                if (this.getEntityId() > ((Entity) ent).getEntityId()) {
                    this.leader = (EntityTropicalFish) ent;
                    this.isLeader = false;
                } else {
                    this.isLeader = true;
                }
            }
        }
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Placed", this.hasBeenPlaced);
        nbttagcompound.setInteger("Color", (int) this.getColor());
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("Placed")) {
            this.hasBeenPlaced = nbttagcompound.getBoolean("Placed");
        } else {
            this.hasBeenPlaced = true;
        }
        this.setShouldSpawnSchool(false);
        this.setColor(nbttagcompound.getInteger("Color"));
    }

    public EntityLivingBase getAttackTarget() {
        if (this.leader != null && !this.inSchool && this.canEntityBeSeen((Entity) this.leader)) {
            return (EntityLivingBase) this.leader;
        }
        return null;
    }

    public void checkForHook() {
        final List list = this.worldObj
            .getEntitiesWithinAABB((Class) EntityFishHook.class, this.boundingBox.expand(10.0, 10.0, 10.0));
        if (list.isEmpty()) {
            this.targetHook = false;
            this.hook = null;
            return;
        }
        this.hook = (Entity) list.get(0);
        this.targetHook = true;
    }

    @Override
    public void applyEntityCollision(final Entity entity) {
        super.applyEntityCollision(entity);
        if (this.targetEntity != null && entity instanceof EntityTropicalFish) {
            this.targetEntity = null;
            this.inSchool = true;
        }
    }

    public boolean interact(final EntityPlayer entityplayer) {
        if (entityplayer.getCurrentEquippedItem() == null || entityplayer.getCurrentEquippedItem()
            .getItem() != TCItemRegistry.fishingNet) {
            return false;
        }
        if (!entityplayer.inventory.hasItem((Item) TCItemRegistry.bucketTropicsWater)) {
            return false;
        }
        for (int i = 0; i < entityplayer.inventory.mainInventory.length; ++i) {
            if (entityplayer.inventory.getStackInSlot(i) != null && entityplayer.inventory.getStackInSlot(i)
                .getItem() == TCItemRegistry.bucketTropicsWater) {
                entityplayer.inventory.mainInventory[i] = new ItemStack(
                    (Item) TCItemRegistry.fishBucket,
                    1,
                    this.getColor());
                this.setDead();
                entityplayer.swingItem();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void updateEntityActionState() {
        if (this.getShouldSpawnSchool()) {
            final int maxInSchool = 7;
            final int minInSchool = 4;
            for (int numToSpawn = new Random().nextInt(1 + maxInSchool - minInSchool) + minInSchool - 1, i = 0; i
                < numToSpawn; ++i) {
                if (!this.worldObj.isRemote) {}
            }
            this.setShouldSpawnSchool(false);
        }
        if (this.leader != null && this.getDistanceToEntity((Entity) this.leader) < 1.5f) {
            this.inSchool = true;
        }
        if (this.leader != null && this.leader.isDead) {
            this.leader = null;
        }
        if (this.leader == null || this.isLeader) {
            this.checkForLeader();
        }
        if (!this.inSchool || this.isLeader) {
            super.updateEntityActionState();
        } else if (this.inSchool && this.leader != null) {
            if (this.getDistanceToEntity((Entity) this.leader) >= 2.25f && this.ticksExisted % 40 == 0) {
                this.inSchool = false;
            }
            if (!this.leader.isLeader && this.leader.leader != null) {
                this.leader = this.leader.leader;
            }
            this.randomMotionVecX = this.leader.randomMotionVecX;
            this.randomMotionVecY = this.leader.randomMotionVecY;
            this.randomMotionVecZ = this.leader.randomMotionVecZ;
        }
    }

    @Override
    protected int attackStrength() {
        return 0;
    }

    public boolean canDespawn() {
        return !this.hasBeenPlaced;
    }

    public void disableDespawning() {
        this.hasBeenPlaced = true;
    }

    static {
        names = new String[] { "Clownfish", "Queen Angelfish", "Yellow Tang", "Butterflyfish", "Geophagus Surinamensis",
            "Betta Fish", "Regal Tang", "Royal Gamma" };
    }
}
