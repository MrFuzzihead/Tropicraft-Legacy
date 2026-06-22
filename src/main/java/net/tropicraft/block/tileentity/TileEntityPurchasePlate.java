package net.tropicraft.block.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.entity.ai.jobs.JobTrade;

import CoroUtil.componentAI.ICoroAI;

public class TileEntityPurchasePlate extends TileEntity {

    public int itemIndex;
    public float startOffset;
    public float angle;
    public int watch_itemIndex;
    public int watch_delay_itemIndex;
    public int tradeState;
    public int watch_tradeState;
    public int credit;
    public ICoroAI tradeKoa;
    public EntityPlayer activeTrader;
    public long toolTipWaitStartTime;
    public boolean showToolTip;
    public boolean watch_showToolTip;

    public TileEntityPurchasePlate() {
        this.itemIndex = 0;
        this.angle = 0.0f;
        this.watch_itemIndex = 0;
        this.watch_delay_itemIndex = 0;
        this.tradeState = 0;
        this.activeTrader = null;
        this.toolTipWaitStartTime = 0L;
        this.showToolTip = false;
        this.watch_showToolTip = false;
        final Random rand = new Random();
        this.startOffset = (float) (Math.random() * 3.141592653589793 * 2.0);
        this.angle = (float) rand.nextInt(360);
    }

    public void onClicked(final boolean rightClick) {
        if (rightClick) {
            ++this.itemIndex;
            this.tradeState = 1;
            if (this.itemIndex >= ItemValues.itemsBuyable.size()) {
                this.itemIndex = 0;
            }
        } else if (this.tradeState == 1) {
            ++this.tradeState;
        } else if (this.tradeState == 2 && this.tradeKoa != null && !this.tradeKoa.getAIAgent().ent.isDead) {
            final JobTrade job = (JobTrade) this.tradeKoa.getAIAgent().jobMan
                .getFirstJobByClass((Class) JobTrade.class);
            if (job != null) {
                job.tradeConfirmCallback();
            }
            this.tradeState = 1;
        }
        this.sync();
    }

    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            final int waitTime = 100;
            final int endTime = waitTime + 400;
            if (this.activeTrader != null && !this.activeTrader.getEntityData()
                .hasKey("hasShownTradeToolTip")) {
                if (this.toolTipWaitStartTime == 0L) {
                    this.toolTipWaitStartTime = this.worldObj.getWorldTime();
                } else if (this.worldObj.getWorldTime() > this.toolTipWaitStartTime + waitTime) {
                    if (this.worldObj.getWorldTime() < this.toolTipWaitStartTime + endTime) {
                        this.showToolTip = true;
                    } else {
                        this.showToolTip = false;
                        this.toolTipWaitStartTime = 0L;
                        this.activeTrader.getEntityData()
                            .setBoolean("hasShownTradeToolTip", true);
                    }
                }
            } else {
                this.showToolTip = false;
                this.toolTipWaitStartTime = 0L;
            }
            this.watchVariables();
        }
        super.updateEntity();
    }

    public void watchVariables() {
        boolean update = false;
        if (this.showToolTip != this.watch_showToolTip) {
            this.watch_showToolTip = this.showToolTip;
            update = true;
        }
        if (this.itemIndex != this.watch_itemIndex || this.watch_delay_itemIndex == 0
            || this.watch_tradeState != this.tradeState) {
            this.watch_delay_itemIndex = 40;
            this.watch_tradeState = this.tradeState;
            update = true;
        }
        if (update) {
            this.sync();
        }
        if (this.watch_delay_itemIndex > 0) {
            --this.watch_delay_itemIndex;
        }
        this.watch_itemIndex = this.itemIndex;
    }

    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        try {
            super.readFromNBT(par1NBTTagCompound);
            this.itemIndex = par1NBTTagCompound.getInteger("itemIndex");
            this.tradeState = par1NBTTagCompound.getInteger("tradeState");
            this.credit = par1NBTTagCompound.getInteger("credit");
            this.showToolTip = par1NBTTagCompound.getBoolean("showToolTip");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        try {
            super.writeToNBT(par1NBTTagCompound);
            par1NBTTagCompound.setInteger("itemIndex", this.itemIndex);
            par1NBTTagCompound.setInteger("tradeState", this.tradeState);
            par1NBTTagCompound.setInteger("credit", this.credit);
            par1NBTTagCompound.setBoolean("showToolTip", this.showToolTip);
        } catch (Exception ex) {
            ex.printStackTrace();
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
}
