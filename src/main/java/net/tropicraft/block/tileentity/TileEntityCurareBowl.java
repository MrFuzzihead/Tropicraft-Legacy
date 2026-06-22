package net.tropicraft.block.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.curare.CurareMixRegistry;
import net.tropicraft.curare.CurareType;
import net.tropicraft.registry.TCItemRegistry;

public class TileEntityCurareBowl extends TileEntity {

    public List<ItemStack> ingredients;
    private static final int REQUIRED_NUM_CLICKS = 30;
    public static final int TICKS_PER_PESTLE_CLICK = 5;
    public int pestleTicks;
    public int numClicks;

    public void readFromNBT(final NBTTagCompound par1nbtTagCompound) {
        super.readFromNBT(par1nbtTagCompound);
        this.ingredients = new ArrayList<ItemStack>();
        for (int length = par1nbtTagCompound.getInteger("ContentLength"), i = 0; i < length; ++i) {
            final ItemStack stack = ItemStack.loadItemStackFromNBT(par1nbtTagCompound.getCompoundTag("Contents" + i));
            if (stack != null) {
                this.ingredients.add(stack);
            }
        }
        this.numClicks = par1nbtTagCompound.getInteger("Clicks");
        this.pestleTicks = par1nbtTagCompound.getShort("PestleTicks");
    }

    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (this.ingredients != null) {
            for (int i = 0; i < this.ingredients.size(); ++i) {
                if (this.ingredients.get(i) != null) {
                    final NBTTagCompound var4 = new NBTTagCompound();
                    this.ingredients.get(i)
                        .writeToNBT(var4);
                    nbt.setTag("Contents" + i, (NBTBase) var4);
                }
            }
            nbt.setInteger("ContentLength", this.ingredients.size());
        } else {
            this.ingredients = new ArrayList<ItemStack>();
        }
        nbt.setInteger("Clicks", this.numClicks);
        nbt.setShort("PestleTicks", (short) this.pestleTicks);
    }

    public void incrementNumClicks() {
        ++this.numClicks;
        this.sync();
    }

    public void updateEntity() {
        if (this.pestleTicks < this.numClicks * 5) {
            ++this.pestleTicks;
        }
    }

    public void resetClicks() {
        this.numClicks = 0;
        this.pestleTicks = 0;
        this.sync();
    }

    public void addIngredient(final ItemStack stack) {
        if (this.ingredients == null) {
            this.ingredients = new ArrayList<ItemStack>();
        }
        this.ingredients.add(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
        this.sync();
    }

    public void dropResult() {
        final ItemStack[] stacks = new ItemStack[this.ingredients.size()];
        int c = 0;
        for (final ItemStack i : this.ingredients) {
            stacks[c] = i;
            ++c;
        }
        final ItemStack stack = this.getCurareResult(this.getResult(stacks));
        final EntityItem result = new EntityItem(
            this.worldObj,
            (double) this.xCoord,
            this.yCoord + (new Random().nextInt(1) + 0.3),
            (double) this.zCoord,
            stack);
        if (!this.worldObj.isRemote) {
            this.worldObj.spawnEntityInWorld((Entity) result);
        }
        this.ingredients.clear();
        this.sync();
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

    public boolean isBowlFull() {
        if (this.ingredients == null) {
            return false;
        }
        for (final ItemStack i : this.ingredients) {
            if (i.getItem() == null) {
                return false;
            }
        }
        return this.ingredients.size() >= 1;
    }

    public ItemStack[] getIngredients() {
        if (this.ingredients == null) {
            return new ItemStack[0];
        }
        final ItemStack[] temp = new ItemStack[this.ingredients.size()];
        for (int i = 0; i < this.ingredients.size(); ++i) {
            temp[i] = this.ingredients.get(i);
        }
        return temp;
    }

    public List<ItemStack> getIngredientList() {
        return this.ingredients;
    }

    public boolean hasMetMaxNumClicks() {
        return this.numClicks >= 30;
    }

    private ItemStack getCurareResult(final CurareType curare) {
        return new ItemStack((Item) TCItemRegistry.curare, 1, curare.curareId);
    }

    private CurareType getResult(final ItemStack[] ingredients1) {
        return CurareMixRegistry.getInstance()
            .getCurareFromIngredients(ingredients1);
    }
}
