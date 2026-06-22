package net.tropicraft.entity.placeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.underdasea.StarfishType;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;

public class EntityWallStarfish extends EntityHanging implements IEntityAdditionalSpawnData {

    private StarfishType starfishType;

    public EntityWallStarfish(final World par1World) {
        super(par1World);
        this.setStarfishType(StarfishType.values()[0]);
    }

    public EntityWallStarfish(final World world, final int xCoord, final int yCoord, final int zCoord,
        final int direction, final StarfishType starfishType) {
        super(world, xCoord, yCoord, zCoord, direction);
        this.setDirection(direction);
        this.setStarfishType(starfishType);
    }

    public int getWidthPixels() {
        return 9;
    }

    public int getHeightPixels() {
        return 9;
    }

    public void dropItemStack() {}

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

    public StarfishType getStarfishType() {
        return this.starfishType;
    }

    public void setStarfishType(final StarfishType starfishType) {
        this.starfishType = starfishType;
    }

    public void onBroken(final Entity entity) {
        this.dropItemStack();
    }

    public void writeSpawnData(final ByteBuf data) {
        data.writeInt(this.field_146063_b);
        data.writeInt(this.field_146064_c);
        data.writeInt(this.field_146062_d);
        data.writeByte(this.starfishType.ordinal());
        data.writeByte(this.hangingDirection);
    }

    public void readSpawnData(final ByteBuf data) {
        this.field_146063_b = data.readInt();
        this.field_146064_c = data.readInt();
        this.field_146062_d = data.readInt();
        this.starfishType = StarfishType.values()[data.readByte()];
        this.setDirection((int) data.readByte());
    }
}
