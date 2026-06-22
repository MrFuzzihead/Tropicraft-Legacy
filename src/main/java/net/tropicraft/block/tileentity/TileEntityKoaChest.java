package net.tropicraft.block.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.tropicraft.entity.koa.EntityKoaBase;
import net.tropicraft.entity.koa.EntityKoaFisher;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.registry.TCBlockRegistry;

import CoroUtil.OldUtil;
import CoroUtil.componentAI.ICoroAI;

public class TileEntityKoaChest extends TileEntityChest implements IInventory {

    private boolean unbreakable;
    private int delay;
    public int housePop;
    public int housePop_hunters;
    public int housePop_fishers;
    public int[] entIDs;
    public EntityKoaBase[] entRefs;
    private int hunters;
    private int fishers;
    private boolean needListUpdate;

    public TileEntityKoaChest() {
        this.unbreakable = false;
        this.housePop = 2;
        this.housePop_hunters = 1;
        this.housePop_fishers = 1;
        this.needListUpdate = false;
        this.entIDs = new int[this.housePop];
        this.entRefs = new EntityKoaBase[this.housePop];
        for (int i = 0; i < this.entIDs.length; ++i) {
            this.entIDs[i] = -1;
            this.entRefs[i] = null;
        }
        this.delay = 80;
    }

    public void spawnKoa(final World world) {
        this.updateList();
        final int koaCount = this.getHomeKoaCount();
        final int spawnCount = this.housePop - koaCount;
        int spawnTry = 0;
        while ((this.hunters < this.housePop_hunters || this.fishers < this.housePop_fishers) && spawnTry < 40) {
            ++spawnTry;
            EntityKoaBase var2;
            if (this.hunters < this.housePop_hunters) {
                ++this.hunters;
                var2 = new EntityKoaHunter(this.worldObj);
            } else {
                ++this.fishers;
                var2 = new EntityKoaFisher(this.worldObj);
            }
            if (var2 == null) {
                return;
            }
            final double var3 = this.xCoord + 0.5;
            final double var4 = this.yCoord + 1;
            final double var5 = this.zCoord + 0.5;
            final EntityLiving var6 = (var2 instanceof EntityLiving) ? var2 : null;
            var2.setLocationAndAngles(var3, var4, var5, this.worldObj.rand.nextFloat() * 360.0f, 0.0f);
            var2.getAIAgent().homeX = this.xCoord;
            var2.getAIAgent().homeY = this.yCoord;
            var2.getAIAgent().homeZ = this.zCoord;
            this.addToList(var2);
            this.worldObj.spawnEntityInWorld((Entity) var2);
            var2.onSpawnWithEgg(null);
            var2.getAIAgent()
                .spawnedOrNBTReloadedInit();
            if (var6 == null) {
                continue;
            }
            var6.spawnExplosionParticle();
        }
    }

    public void addToList(final EntityKoaBase ent) {
        for (int j = 0; j < this.entIDs.length; ++j) {
            if (this.entIDs[j] == -1) {
                this.entIDs[j] = ent.getAIAgent().entID;
                this.entRefs[j] = ent;
                break;
            }
        }
    }

    public void updateList() {
        for (int j = 0; j < this.entIDs.length; ++j) {
            final Entity ent = OldUtil.getEntByPersistantID(this.worldObj, this.entIDs[j]);
            if (ent == null) {
                this.entIDs[j] = -1;
                this.entRefs[j] = null;
            } else if (ent.isDead) {
                this.entIDs[j] = -1;
                this.entRefs[j] = null;
            }
        }
    }

    public int getHomeKoaCount() {
        final float dist = 160.0f;
        final List<ICoroAI> ents = (List<ICoroAI>) this.worldObj.getEntitiesWithinAABB(
            (Class) ICoroAI.class,
            AxisAlignedBB
                .getBoundingBox(
                    (double) this.xCoord,
                    (double) this.yCoord,
                    (double) this.zCoord,
                    (double) (this.xCoord + 1),
                    (double) (this.yCoord + 1),
                    (double) (this.zCoord + 1))
                .expand((double) dist, (double) (dist / 2.0f), (double) dist));
        this.hunters = 0;
        this.fishers = 0;
        int existing = 0;
        for (int i = 0; i < ents.size(); ++i) {
            int j = 0;
            j = 0;
            while (j < this.entIDs.length) {
                final Entity ent = (Entity) ents.get(i);
                if (ent instanceof EntityKoaBase && ent != null
                    && this.entIDs[j] == ((EntityKoaBase) ent).getAIAgent().entID
                    && ((EntityKoaBase) ent).getAIAgent().entID != -1) {
                    ++existing;
                    if (ent instanceof EntityKoaHunter) {
                        ++this.hunters;
                    }
                    if (ent instanceof EntityKoaFisher) {
                        ++this.fishers;
                        break;
                    }
                    final int dsdf = 0;
                    break;
                } else {
                    ++j;
                }
            }
            if (j < this.entIDs.length) {}
        }
        return existing;
    }

    public boolean getCanSpawnHere(final Entity ent) {
        final boolean b1 = !this.worldObj.checkBlockCollision(ent.boundingBox);
        final boolean b2 = ent.worldObj.getCollidingBoundingBoxes(ent, ent.boundingBox)
            .isEmpty();
        final boolean b3 = !ent.worldObj.isAnyLiquid(ent.boundingBox);
        return b1 && b3;
    }

    public boolean anyPlayerInRange() {
        return this.worldObj.getClosestPlayer(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, 80.0) != null;
    }

    public void updateEntity() {
        super.updateEntity();
        if (!this.anyPlayerInRange()) {
            return;
        }
        if (this.delay == 0) {
            this.delay = 6000;
            if (!this.worldObj.isRemote && !this.needListUpdate) {
                this.spawnKoa(this.worldObj);
            }
        }
        if (this.delay > 0) {
            if (this.needListUpdate && this.delay < 20) {
                for (int i = 0; i < this.entIDs.length; ++i) {
                    final Entity ent = OldUtil.getEntByPersistantID(this.worldObj, this.entIDs[i]);
                    if (ent instanceof EntityKoaBase) {
                        this.entRefs[i] = (EntityKoaBase) ent;
                    } else {
                        this.entIDs[i] = -1;
                        this.entRefs[i] = null;
                    }
                }
                this.needListUpdate = false;
            }
            --this.delay;
        }
    }

    public String getInvName() {
        return "Koa chest";
    }

    public void checkForAdjacentChests() {
        if (this.adjacentChestChecked) {
            return;
        }
        this.adjacentChestChecked = true;
        this.adjacentChestZNeg = null;
        this.adjacentChestXPos = null;
        this.adjacentChestXNeg = null;
        this.adjacentChestZPos = null;
        if (this.worldObj.getBlock(this.xCoord - 1, this.yCoord, this.zCoord) == TCBlockRegistry.koaChest) {
            this.adjacentChestXNeg = (TileEntityKoaChest) this.worldObj
                .getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
        }
        if (this.worldObj.getBlock(this.xCoord + 1, this.yCoord, this.zCoord) == TCBlockRegistry.koaChest) {
            this.adjacentChestXPos = (TileEntityKoaChest) this.worldObj
                .getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
        }
        if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord - 1) == TCBlockRegistry.koaChest) {
            this.adjacentChestZNeg = (TileEntityKoaChest) this.worldObj
                .getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
        }
        if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord + 1) == TCBlockRegistry.koaChest) {
            this.adjacentChestZPos = (TileEntityKoaChest) this.worldObj
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

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public void setIsUnbreakable(final boolean flag) {
        this.unbreakable = flag;
    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.unbreakable = nbttagcompound.getBoolean("unbreakable");
        try {
            for (int i = 0; i < this.entIDs.length; ++i) {
                this.entIDs[i] = nbttagcompound.getInteger("entID_" + i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.needListUpdate = true;
    }

    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("unbreakable", this.unbreakable);
        try {
            for (int i = 0; i < this.entIDs.length; ++i) {
                nbttagcompound.setInteger("entID_" + i, this.entIDs[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Block getBlockType() {
        return (Block) TCBlockRegistry.koaChest;
    }
}
