package net.tropicraft.entity.underdasea;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;

public class EntityStarfish extends EntityEchinoderm implements IEntityAdditionalSpawnData {

    public static final float BABY_WIDTH = 0.25f;
    public static final float ADULT_WIDTH = 1.0f;
    public static final float BABY_HEIGHT = 0.1f;
    public static final float ADULT_HEIGHT = 0.2f;
    public static final float BABY_YOFFSET = 0.03125f;
    public static final float ADULT_YOFFSET = 0.03125f;
    private StarfishType starfishType;

    public EntityStarfish(final World world) {
        super(world);
        this.setStarfishType(StarfishType.values()[this.rand.nextInt(StarfishType.values().length)]);
        this.experienceValue = 5;
    }

    public EntityStarfish(final World world, final boolean baby) {
        super(world, baby);
        this.setStarfishType(StarfishType.values()[this.rand.nextInt(StarfishType.values().length)]);
        this.experienceValue = 5;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10.0);
    }

    public StarfishType getStarfishType() {
        return this.starfishType;
    }

    public void setStarfishType(final StarfishType starfishType) {
        this.starfishType = starfishType;
    }

    public void writeEntityToNBT(final NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte(
            "StarfishType",
            (byte) this.getStarfishType()
                .ordinal());
    }

    public void readEntityFromNBT(final NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
    }

    public void writeSpawnData(final ByteBuf buffer) {
        buffer.writeByte(this.starfishType.ordinal());
    }

    public void readSpawnData(final ByteBuf additionalData) {
        this.starfishType = StarfishType.values()[additionalData.readByte()];
    }

    public EntityEchinodermEgg createEgg() {
        return new EntityStarfishEgg(this.worldObj, this.starfishType);
    }

    public float getBabyWidth() {
        return 0.25f;
    }

    public float getAdultWidth() {
        return 1.0f;
    }

    public float getBabyHeight() {
        return 0.1f;
    }

    public float getAdultHeight() {
        return 0.2f;
    }

    public float getBabyYOffset() {
        return 0.03125f;
    }

    public float getAdultYOffset() {
        return 0.03125f;
    }

    public boolean isPotentialMate(final EntityEchinoderm other) {
        return super.isPotentialMate(other) && ((EntityStarfish) other).getStarfishType() == this.getStarfishType();
    }

    public void onDeath(final DamageSource d) {
        super.onDeath(d);
        if (!this.worldObj.isRemote) {
            this.entityDropItem(new ItemStack(TCItemRegistry.shells, 1, 4), 0.0f);
        }
    }
}
