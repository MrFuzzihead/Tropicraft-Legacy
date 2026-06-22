package net.tropicraft.entity.ai.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.tropicraft.Tropicraft;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.util.CoroUtilBlock;

public class JobTrade extends JobBase {

    public float tradeDistTrigger;
    public int tradeLastItemOffer;
    public ChunkCoordinates tradeBlockPos;
    public TileEntityPurchasePlate tradePlate;
    public EntityPlayer activeTrader;
    public ArrayList<ItemStack> offeredItems;
    public Block idTradeBlock;

    public JobTrade(final JobManager jm) {
        super(jm);
        this.offeredItems = new ArrayList<ItemStack>();
    }

    public void convertOfferingsToCurrency(final int newCredit) {
        this.offeredItems.clear();
        int leftToConvert;
        for (leftToConvert = newCredit; leftToConvert > TCKoaCurrencyRegistry.currency
            .getMaxStackSize(); leftToConvert -= TCKoaCurrencyRegistry.currency.getMaxStackSize()) {
            this.offeredItems.add(
                new ItemStack(
                    TCKoaCurrencyRegistry.currency.getItem(),
                    TCKoaCurrencyRegistry.currency.getMaxStackSize()));
        }
        if (leftToConvert > 0) {
            this.offeredItems.add(new ItemStack(TCKoaCurrencyRegistry.currency.getItem(), leftToConvert));
        }
    }

    public int getOfferedItemsValue() {
        int value = 0;
        try {
            for (int i = 0; i < this.offeredItems.size(); ++i) {
                value += ItemValues.getItemEntry((ItemStack) this.offeredItems.get(i))
                    .getTotalValue((ItemStack) this.offeredItems.get(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public int returnCredit() {
        final int value = 0;
        try {
            for (int i = 0; i < this.offeredItems.size(); ++i) {
                this.activeTrader.inventory.addItemStackToInventory((ItemStack) this.offeredItems.get(i));
            }
            this.offeredItems.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public void tick() {
        if (this.tradeBlockPos != null
            && (this.ai.targX != this.tradeBlockPos.posX || this.ai.targY != this.tradeBlockPos.posY + 1
                || this.ai.targZ != this.tradeBlockPos.posZ)
            && this.ent.getDistance(
                (double) this.tradeBlockPos.posX,
                (double) this.tradeBlockPos.posY,
                (double) this.tradeBlockPos.posZ) > 15.0) {
            this.ent.getNavigator()
                .clearPathEntity();
            this.ai.walkTo(
                (Entity) this.ent,
                this.tradeBlockPos.posX,
                this.tradeBlockPos.posY + 1,
                this.tradeBlockPos.posZ,
                (float) this.ai.maxPFRange,
                600);
        }
        this.idTradeBlock = (Block) TCBlockRegistry.purchasePlate;
        this.ai.maxDistanceFromHome = 0.5;
        this.tradeDistTrigger = 4.0f;
        if (this.tradeBlockPos == null) {
            this.tradeBlockPos = this.tickFind(this.idTradeBlock, 25);
            if (this.tradeBlockPos != null) {
                final TileEntity tile = this.ent.worldObj
                    .getTileEntity(this.tradeBlockPos.posX, this.tradeBlockPos.posY, this.tradeBlockPos.posZ);
                if (tile != null) {
                    ((TileEntityPurchasePlate) tile).tradeKoa = this.entInt;
                    this.tradePlate = (TileEntityPurchasePlate) tile;
                }
                this.ai.homeX = this.tradeBlockPos.posX;
                this.ai.homeY = this.tradeBlockPos.posY;
                this.ai.homeZ = this.tradeBlockPos.posZ;
            }
        }
        if (this.activeTrader == null) {
            this.activeTrader = this.ent.worldObj
                .getClosestPlayerToEntity((Entity) this.ent, (double) this.tradeDistTrigger);
            if (this.activeTrader != null) {
                this.tradeStart();
            }
        } else if (this.ent.getDistanceToEntity((Entity) this.activeTrader) > this.tradeDistTrigger * 1.5) {
            this.tradeReset();
        } else {
            this.tradeTick();
        }
    }

    public void tradeStart() {
        if (this.tradePlate != null) {
            this.tradePlate.tradeState = 1;
            this.tradePlate.credit = this.getOfferedItemsValue();
        }
    }

    public void tradeConfirmCallback() {
        int credit = this.getOfferedItemsValue();
        final ItemEntry ie = ItemValues.itemsBuyable.get(this.tradePlate.itemIndex);
        final int cost = ie.value;
        if (credit >= cost) {
            credit -= cost;
            this.convertOfferingsToCurrency(credit);
            final ItemStack is = ie.item.copy();
            this.activeTrader.inventory.addItemStackToInventory(is);
        }
        this.tradeSuccess();
    }

    public void tradeSuccess() {
        if (this.tradePlate != null) {
            this.tradePlate.tradeState = 1;
            this.tradePlate.credit = this.getOfferedItemsValue();
        }
    }

    public void tradeReset() {
        if (this.offeredItems != null) {
            this.returnCredit();
        }
        this.activeTrader = null;
        if (this.tradePlate != null) {
            this.tradePlate.tradeState = 0;
            this.tradePlate.credit = 0;
            this.tradePlate.activeTrader = null;
        }
        Tropicraft.dbg("trade reset");
    }

    public void tradeTick() {
        TileEntity tEnt = null;
        if (this.tradeBlockPos != null) {
            tEnt = this.ent.worldObj
                .getTileEntity(this.tradeBlockPos.posX, this.tradeBlockPos.posY, this.tradeBlockPos.posZ);
        }
        if (this.tradeBlockPos != null && tEnt == null) {
            this.tradeBlockPos = null;
        } else if (tEnt instanceof TileEntityPurchasePlate) {
            ((TileEntityPurchasePlate) tEnt).activeTrader = this.activeTrader;
        }
    }

    public ChunkCoordinates tickFind(final Block id, final int range) {
        for (int i = 0; i < 30; ++i) {
            final int randX = (int) this.ent.posX + this.ent.worldObj.rand.nextInt(range) - range / 2;
            final int randY = (int) this.ent.posY + this.ent.worldObj.rand.nextInt(range) - range / 2;
            final int randZ = (int) this.ent.posZ + this.ent.worldObj.rand.nextInt(range) - range / 2;
            final Block foundID = this.ent.worldObj.getBlock(randX, randY, randZ);
            if (foundID == id) {
                Tropicraft.dbg("found trade block");
                return new ChunkCoordinates(randX, randY, randZ);
            }
        }
        return null;
    }

    public boolean shouldExecute() {
        return this.isAreaSecure();
    }

    public boolean shouldContinue() {
        return !this.isAreaSecure();
    }

    public boolean isAreaSecure() {
        if (this.tradeBlockPos != null) {
            final List list = this.ent.worldObj.getEntitiesWithinAABBExcludingEntity(
                (Entity) this.ent,
                AxisAlignedBB
                    .getBoundingBox(
                        (double) this.tradeBlockPos.posX,
                        (double) this.tradeBlockPos.posY,
                        (double) this.tradeBlockPos.posZ,
                        (double) this.tradeBlockPos.posX,
                        (double) this.tradeBlockPos.posY,
                        (double) this.tradeBlockPos.posZ)
                    .expand(6.0, 3.0, 6.0));
            for (int j = 0; j < list.size(); ++j) {
                final Entity entity1 = (Entity) list.get(j);
                if (this.entInt.isEnemy(entity1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onIdleTickAct() {
        if (this.activeTrader != null) {
            this.ent.faceEntity((Entity) this.activeTrader, 15.0f, 15.0f);
        } else if (this.tradeBlockPos != null) {}
        if (this.activeTrader == null && this.ent.getNavigator()
            .noPath()) {
            final Random rand = new Random();
            if (this.tradeBlockPos != null && this.ent.worldObj.rand.nextInt(100) == 0) {
                final int tryX = this.tradeBlockPos.posX;
                final int tryZ = this.tradeBlockPos.posZ;
                if (!CoroUtilBlock
                    .isAir(this.ent.worldObj.getBlock(tryX, MathHelper.floor_double(this.ent.posY - 1.0), tryZ))) {
                    this.ai.walkTo(
                        (Entity) this.ent,
                        this.tradeBlockPos.posX,
                        MathHelper.floor_double(this.ent.posY),
                        this.tradeBlockPos.posZ,
                        (float) this.ai.maxPFRange,
                        600);
                }
            }
        }
    }

    public boolean hookInteract(final EntityPlayer par1EntityPlayer) {
        if (!this.ent.worldObj.isRemote) {
            if (this.offeredItems == null) {
                this.offeredItems = new ArrayList<ItemStack>();
            }
            final ItemStack is = par1EntityPlayer.getCurrentEquippedItem();
            if (is != null && ItemValues.getItemEntry(is) != null) {
                par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = null;
                this.offeredItems.add(is);
                if (this.tradePlate != null) {
                    this.tradePlate.credit = this.getOfferedItemsValue();
                    System.out.println("VERIFY THIS CODE IN hookInteract()");
                    MinecraftServer.getServer()
                        .getConfigurationManager()
                        .sendPacketToAllPlayers(this.tradePlate.getDescriptionPacket());
                }
            }
            return true;
        }
        return super.hookInteract(par1EntityPlayer);
    }
}
