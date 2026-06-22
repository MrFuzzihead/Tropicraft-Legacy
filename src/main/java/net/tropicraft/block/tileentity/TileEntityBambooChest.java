package net.tropicraft.block.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.tropicraft.block.BlockBambooChest;

public class TileEntityBambooChest extends TileEntityChest {

    private boolean unbreakable;

    public TileEntityBambooChest() {
        this.unbreakable = false;
    }

    public String getInventoryName() {
        return "Bamboo chest";
    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.unbreakable = nbttagcompound.getBoolean("unbreakable");
    }

    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("unbreakable", this.unbreakable);
    }

    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestZNeg = null;
            this.adjacentChestXPos = null;
            this.adjacentChestXNeg = null;
            this.adjacentChestZPos = null;
            if (this.func_94044_a(this.xCoord - 1, this.yCoord, this.zCoord)) {
                this.adjacentChestXNeg = (TileEntityBambooChest) this.worldObj
                    .getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
            }
            if (this.func_94044_a(this.xCoord + 1, this.yCoord, this.zCoord)) {
                this.adjacentChestXPos = (TileEntityBambooChest) this.worldObj
                    .getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
            }
            if (this.func_94044_a(this.xCoord, this.yCoord, this.zCoord - 1)) {
                this.adjacentChestZNeg = (TileEntityBambooChest) this.worldObj
                    .getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
            }
            if (this.func_94044_a(this.xCoord, this.yCoord, this.zCoord + 1)) {
                this.adjacentChestZPos = (TileEntityBambooChest) this.worldObj
                    .getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
            }
            if (this.adjacentChestZNeg != null) {
                this.adjacentChestZNeg.updateContainingBlockInfo();
            }
            if (this.adjacentChestZPos != null) {
                this.adjacentChestZPos.updateContainingBlockInfo();
            }
            if (this.adjacentChestXPos != null) {
                this.adjacentChestXPos.updateContainingBlockInfo();
            }
            if (this.adjacentChestXNeg != null) {
                this.adjacentChestXNeg.updateContainingBlockInfo();
            }
        }
    }

    private void func_90009_a(final TileEntityBambooChest par1TileEntityChest, final int par2) {
        if (par1TileEntityChest.isInvalid()) {
            this.adjacentChestChecked = false;
        } else if (this.adjacentChestChecked) {
            switch (par2) {
                case 0: {
                    if (this.adjacentChestZPos != par1TileEntityChest) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case 1: {
                    if (this.adjacentChestXNeg != par1TileEntityChest) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case 2: {
                    if (this.adjacentChestZNeg != par1TileEntityChest) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case 3: {
                    if (this.adjacentChestXPos != par1TileEntityChest) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
            }
        }
    }

    private boolean func_94044_a(final int par1, final int par2, final int par3) {
        final Block block = this.worldObj.getBlock(par1, par2, par3);
        return block != null && block instanceof BlockBambooChest
            && ((BlockBambooChest) block).field_149956_a == this.func_145980_j();
    }

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public void setIsUnbreakable(final boolean flag) {
        this.unbreakable = flag;
    }
}
