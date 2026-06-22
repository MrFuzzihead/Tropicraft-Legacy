package net.tropicraft.block.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.item.scuba.ItemScubaGear;
import net.tropicraft.item.scuba.ItemScubaTank;

public class TileEntityAirCompressor extends TileEntity {

    public boolean compressing;
    private int ticks;
    private static final float fillRate = 0.1f;
    private ItemStack tank;
    private float maxCapacity;

    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.compressing = nbt.getBoolean("Compressing");
        this.ticks = nbt.getInteger("Ticks");
        if (nbt.hasKey("Tank")) {
            this.tank = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Tank"));
            this.maxCapacity = ((this.tank.getItemDamage() == 1)
                ? ((float) ItemScubaGear.AirType.TRIMIX.getMaxCapacity())
                : ((float) ItemScubaGear.AirType.REGULAR.getMaxCapacity()));
        } else {
            this.tank = null;
        }
    }

    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("Compressing", this.compressing);
        nbt.setInteger("Ticks", this.ticks);
        if (this.tank != null) {
            final NBTTagCompound var4 = new NBTTagCompound();
            this.tank.writeToNBT(var4);
            nbt.setTag("Tank", (NBTBase) var4);
        }
    }

    public void updateEntity() {
        if (this.tank == null) {
            return;
        }
        final float airContained = this.getTagCompound(this.tank)
            .getFloat("AirContained");
        if (this.compressing && airContained < ItemScubaGear.AirType.REGULAR.getMaxCapacity()) {
            if (airContained + 0.1f >= ItemScubaGear.AirType.REGULAR.getMaxCapacity()) {
                this.tank.getTagCompound()
                    .setFloat("AirContained", (float) ItemScubaGear.AirType.REGULAR.getMaxCapacity());
                ++this.ticks;
                this.finishCompressing();
            } else {
                this.tank.getTagCompound()
                    .setFloat("AirContained", airContained + 0.1f);
                ++this.ticks;
            }
        }
    }

    public NBTTagCompound getTagCompound(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public void sync() {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return (Packet) new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }

    public boolean addTank(final ItemStack stack) {
        if (this.tank == null && stack.getItem() != null && stack.getItem() instanceof ItemScubaTank) {
            this.tank = stack;
            this.compressing = true;
            this.sync();
            return true;
        }
        return false;
    }

    public void ejectTank() {
        if (this.tank != null) {
            final EntityItem tankItem = new EntityItem(
                this.worldObj,
                (double) this.xCoord,
                (double) this.yCoord,
                (double) this.zCoord,
                this.tank);
            this.worldObj.spawnEntityInWorld((Entity) tankItem);
            this.tank = null;
        }
        this.ticks = 0;
        this.compressing = false;
        this.sync();
    }

    public boolean isDoneCompressing() {
        return this.ticks > 0 && !this.compressing;
    }

    public float getTickRatio() {
        return this.ticks / (ItemScubaGear.AirType.REGULAR.getMaxCapacity() * 0.1f);
    }

    public boolean isCompressing() {
        return this.compressing;
    }

    public void startCompressing() {
        this.compressing = true;
        this.sync();
    }

    public void finishCompressing() {
        this.compressing = false;
        this.sync();
    }
}
