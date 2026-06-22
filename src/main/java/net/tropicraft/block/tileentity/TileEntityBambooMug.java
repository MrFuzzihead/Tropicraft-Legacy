package net.tropicraft.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBambooMug extends TileEntity {

    public ItemStack cocktail;

    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Cocktail")) {
            this.cocktail = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Cocktail"));
        } else {
            this.cocktail = null;
        }
    }

    public void writeToNBT(final NBTTagCompound nbt2) {
        super.writeToNBT(nbt2);
        if (this.cocktail != null) {
            final NBTTagCompound nbt3 = new NBTTagCompound();
            this.cocktail.writeToNBT(nbt3);
            nbt2.setTag("Cocktail", (NBTBase) nbt3);
        }
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

    public boolean isEmpty() {
        return this.cocktail == null;
    }

    public void setCocktail(final ItemStack cocktail) {
        this.cocktail = cocktail;
        this.sync();
    }

    public int getMetadata() {
        return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }

    public boolean canUpdate() {
        return false;
    }
}
