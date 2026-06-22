package net.tropicraft.block.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class TileEntitySifter extends TileEntity {

    public static final int SIFT_TIME = 80;
    public boolean isSifting;
    public int currentSiftTime;
    private Random rand;
    public double yaw;
    public double yaw2;
    public ItemStack siftItem;

    public TileEntitySifter() {
        this.yaw2 = 0.0;
        this.rand = new Random();
        this.currentSiftTime = 80;
    }

    public void updateEntity() {
        if (this.currentSiftTime > 0 && this.isSifting) {
            --this.currentSiftTime;
        }
        if (this.worldObj.isRemote) {
            this.yaw2 = this.yaw % 360.0;
            this.yaw += 4.545454502105713;
        }
        if (!this.worldObj.isRemote && this.isSifting && this.currentSiftTime <= 0) {
            final double x = this.xCoord + this.worldObj.rand.nextDouble() * 1.4;
            final double y = this.yCoord + this.worldObj.rand.nextDouble() * 1.4;
            final double z = this.zCoord + this.worldObj.rand.nextDouble() * 1.4;
            this.dumpResults(x, y, z, this.isHeatedSifter() ? SiftType.HEATED : SiftType.REGULAR);
            this.currentSiftTime = 80;
            this.setSifting(false, 0, -1.0f);
        }
    }

    public void dumpResults(final double x, final double y, final double z, final SiftType type) {
        if (type == SiftType.HEATED) {
            float refinedAmt = this.getTagCompound(this.siftItem)
                .getFloat("AmtRefined");
            if (this.siftItem.getItem() == TCItemRegistry.ore) {
                refinedAmt = this.refine(refinedAmt);
            }
            if (this.siftItem.getItem() == Item.getItemFromBlock((Block) TCBlockRegistry.mineralSands)
                && this.siftItem.getItemDamage() == 3) {
                this.siftItem = new ItemStack((Item) TCItemRegistry.ore, 1, 5);
            }
            if (refinedAmt == 100.0f) {
                this.siftItem = new ItemStack((Item) TCItemRegistry.ore, 1, 6);
            }
            this.getTagCompound(this.siftItem)
                .setFloat("AmtRefined", refinedAmt);
            this.spawn(this.siftItem, x, y, z);
            this.spawn(new ItemStack((Block) TCBlockRegistry.purifiedSand), x, y, z);
        } else {
            this.dumpBeachResults(x, y, z);
        }
    }

    private void dumpBeachResults(final double x, final double y, final double z) {
        int dumpCount = this.rand.nextInt(3) + 1;
        this.spawn(new ItemStack((Block) TCBlockRegistry.purifiedSand), x, y, z);
        while (dumpCount > 0) {
            --dumpCount;
            ItemStack stack;
            if (this.rand.nextInt(10) == 0) {
                stack = this.getRareItem();
            } else {
                stack = this.getCommonItem();
            }
            this.spawn(stack, x, y, z);
        }
    }

    private void spawn(final ItemStack stack, final double x, final double y, final double z) {
        if (this.worldObj.isRemote) {
            return;
        }
        final EntityItem eitem = new EntityItem(this.worldObj, x, y, z, stack);
        eitem.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
        this.worldObj.spawnEntityInWorld((Entity) eitem);
    }

    private ItemStack getCommonItem() {
        final int dmg = this.rand.nextInt(8);
        switch (dmg) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 5: {
                return new ItemStack(TCItemRegistry.shells, 1, dmg);
            }
            default: {
                return this.getRareItem();
            }
        }
    }

    private ItemStack getRareItem() {
        final int dmg = this.rand.nextInt(8);
        switch (dmg) {
            case 0: {
                return new ItemStack(TCItemRegistry.shells, 1, 3);
            }
            case 1: {
                return new ItemStack(Items.gold_nugget, 1);
            }
            case 2: {
                return new ItemStack(Items.bucket, 1);
            }
            case 3: {
                return new ItemStack(Items.wooden_shovel, 1);
            }
            case 4: {
                return new ItemStack(Items.glass_bottle, 1);
            }
            case 5: {
                return new ItemStack((Item) TCItemRegistry.pearl, 1, 0);
            }
            case 6: {
                return new ItemStack((Item) TCItemRegistry.pearl, 1, 1);
            }
            default: {
                return new ItemStack(TCItemRegistry.shells, 1, 3);
            }
        }
    }

    private float refine(final float refinedAmt) {
        if (refinedAmt == 0.0f) {
            return 33.333f;
        }
        if (refinedAmt > 32.0f && refinedAmt < 34.0f) {
            return 66.667f;
        }
        if (refinedAmt > 65.0f && refinedAmt < 67.0f) {
            return 100.0f;
        }
        return 0.0f;
    }

    public boolean isHeatedSifter() {
        final Block block = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord);
        return block.getMaterial() == Material.fire || block.getMaterial() == Material.lava;
    }

    public void setSifting(final boolean flag, final int type, final float refined) {
        this.isSifting = flag;
        this.siftItem = ((type == -1) ? null
            : ((type == 1) ? new ItemStack((Block) Blocks.sand)
                : ((type == 2) ? new ItemStack((Block) TCBlockRegistry.mineralSands, 1, 3)
                    : new ItemStack((Item) TCItemRegistry.ore, 1, 5))));
        if (this.siftItem.getItem() == TCItemRegistry.ore) {
            this.getTagCompound(this.siftItem)
                .setFloat("AmtRefined", refined);
        } else {
            System.err.println("stack is null : (");
        }
        this.sync();
    }

    public boolean isSifting() {
        return this.isSifting;
    }

    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isSifting = nbt.getBoolean("isSifting");
        this.currentSiftTime = nbt.getInteger("currentSiftTime");
        final NBTTagList itemtaglist = nbt.getTagList("Item", 10);
        final NBTTagCompound itemtagcompound = itemtaglist.getCompoundTagAt(0);
        this.siftItem = ItemStack.loadItemStackFromNBT(itemtagcompound);
    }

    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isSifting", this.isSifting);
        nbt.setInteger("currentSiftTime", this.currentSiftTime);
        final NBTTagList nbttaglist = new NBTTagList();
        if (this.siftItem != null) {
            final NBTTagCompound siftItemTagCompound = new NBTTagCompound();
            this.siftItem.writeToNBT(siftItemTagCompound);
            nbttaglist.appendTag((NBTBase) siftItemTagCompound);
        }
        nbt.setTag("Item", (NBTBase) nbttaglist);
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

    public enum SiftType {
        REGULAR,
        HEATED;
    }
}
