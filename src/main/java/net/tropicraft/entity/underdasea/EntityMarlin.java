package net.tropicraft.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntityMarlin extends EntityTropicraftWaterMob {

    public String texture;

    public EntityMarlin(final World world) {
        super(world);
        this.texture = "";
        this.hyperness = 30;
        this.fickleness = 150;
        this.horFactor = 0.3f;
        this.climbFactor = 0.225f;
        this.type = WaterMobType.OCEAN_DWELLER;
        this.setSize(1.4f, 0.95f);
        this.isCatchable = true;
        this.fishingMaxLookDist = 40.0;
        this.fishingInterestOdds = 6;
        this.experienceValue = 5;
    }

    public void writeEntityToNBT(final NBTTagCompound n) {
        n.setString("texture", this.texture);
        super.writeEntityToNBT(n);
    }

    public void readEntityFromNBT(final NBTTagCompound n) {
        this.texture = n.getString("texture");
        super.readEntityFromNBT(n);
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    protected void dropFewItems(final boolean flag) {
        for (int i = this.rand.nextInt(3) + 1, j = 0; j < i; ++j) {
            if (!this.worldObj.isRemote) {
                this.entityDropItem(new ItemStack((Item) TCItemRegistry.freshMarlin), 0.0f);
            }
        }
    }

    public void onDeath(final DamageSource damagesource) {
        super.onDeath(damagesource);
        this.dropFewItems(true);
    }

    public boolean interact(final EntityPlayer entityplayer) {
        return false;
    }

    @Override
    public boolean isInWater() {
        return super.isInWater();
    }

    @Override
    public void applyEntityCollision(final Entity entity) {
        super.applyEntityCollision(entity);
        if (this.isSurfacing) {}
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    protected void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.isInWater() && this.worldObj.rand.nextInt(500) == 0
            && !this.isSurfacing
            && Math.floor(this.posY) >= 60.0) {
            this.isSurfacing = true;
            this.motionX *= 1.5;
            this.motionZ *= 1.5;
            this.addVelocity(0.0, 0.75, 0.0);
            this.surfaceTick = 20;
            this.reachedTarget = false;
        }
    }

    @Override
    protected int attackStrength() {
        switch (this.worldObj.difficultySetting) {
            case EASY: {
                return 3;
            }
            case NORMAL: {
                return 5;
            }
            case HARD: {
                return 7;
            }
            default: {
                return 0;
            }
        }
    }

    @Override
    protected boolean canDespawn() {
        return true;
    }
}
