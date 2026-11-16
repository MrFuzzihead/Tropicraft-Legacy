package net.tropicraft.block.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
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

    /**
     * Tops the chest's household up to {@link #housePop}.
     * <p>
     * The quota is decided from the registered slots, not from a live scan of the world. A member that is standing in
     * an unloaded chunk still belongs to this chest, but {@link #getHomeKoaCount()} cannot see it - and
     * {@link #addToList} used to silently drop the ids of anything spawned while both slots were taken. Visiting a
     * village whose Koa had wandered out of the 160 block scan window therefore spawned a fresh pair every time
     * while the old pair was still alive, untracked and never cleaned up.
     * </p>
     */
    public void spawnKoa(final World world) {
        this.updateList();
        int free = this.housePop - this.occupiedSlots();
        if (free <= 0) {
            return;
        }
        // The live counts only pick which type to spawn next; what bounds the household is `free` above.
        this.getHomeKoaCount();
        for (int spawnTry = 0; free > 0 && spawnTry < this.housePop * 4; ++spawnTry) {
            final boolean hunter = this.hunters < this.housePop_hunters;
            final EntityKoaBase koa = hunter ? new EntityKoaHunter(this.worldObj) : new EntityKoaFisher(this.worldObj);
            koa.setLocationAndAngles(
                this.xCoord + 0.5,
                this.yCoord + 1,
                this.zCoord + 0.5,
                this.worldObj.rand.nextFloat() * 360.0f,
                0.0f);
            koa.getAIAgent().homeX = this.xCoord;
            koa.getAIAgent().homeY = this.yCoord;
            koa.getAIAgent().homeZ = this.zCoord;
            if (!this.addToList(koa)) {
                // The household filled up between the check and here: drop this one rather than spawn a Koa that
                // nothing tracks.
                return;
            }
            this.worldObj.spawnEntityInWorld((Entity) koa);
            koa.onSpawnWithEgg(null);
            koa.getAIAgent()
                .spawnedOrNBTReloadedInit();
            koa.spawnExplosionParticle();
            if (hunter) {
                ++this.hunters;
            } else {
                ++this.fishers;
            }
            --free;
        }
    }

    /**
     * @return true if the entity got a slot. False means the household is full, and the caller must not spawn it.
     */
    public boolean addToList(final EntityKoaBase ent) {
        for (int j = 0; j < this.entIDs.length; ++j) {
            if (this.entIDs[j] == -1) {
                this.entIDs[j] = ent.getAIAgent().entID;
                this.entRefs[j] = ent;
                return true;
            }
        }
        return false;
    }

    private int occupiedSlots() {
        int occupied = 0;
        for (final int entID : this.entIDs) {
            if (entID != -1) {
                ++occupied;
            }
        }
        return occupied;
    }

    /**
     * Refreshes {@link #entRefs} from {@link #entIDs}. A member that is simply not loaded keeps its slot: the only
     * thing this can prove is that a <em>loaded</em> member is dead, and clearing slots for unloaded members is what
     * made the chest spawn replacement pairs forever.
     */
    public void updateList() {
        for (int j = 0; j < this.entIDs.length; ++j) {
            if (this.entIDs[j] == -1) {
                this.entRefs[j] = null;
                continue;
            }
            final Entity ent = OldUtil.getEntByPersistantID(this.worldObj, this.entIDs[j]);
            if (ent == null) {
                this.entRefs[j] = null;
            } else if (ent.isDead) {
                this.entIDs[j] = -1;
                this.entRefs[j] = null;
            } else if (ent instanceof EntityKoaBase) {
                this.entRefs[j] = (EntityKoaBase) ent;
            } else {
                // A recycled id belonging to something else: not our member.
                this.entIDs[j] = -1;
                this.entRefs[j] = null;
            }
        }
    }

    /**
     * Counts the members of this chest that are currently loaded and nearby, and refreshes {@link #hunters} and
     * {@link #fishers}. Note this is a <em>live</em> count: use {@link #occupiedSlots()} for the quota.
     */
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
            final Object candidate = ents.get(i);
            if (!(candidate instanceof EntityKoaBase)) {
                continue;
            }
            final EntityKoaBase ent = (EntityKoaBase) candidate;
            if (!this.ownsMember(ent)) {
                continue;
            }
            ++existing;
            if (ent instanceof EntityKoaHunter) {
                ++this.hunters;
            }
            if (ent instanceof EntityKoaFisher) {
                ++this.fishers;
            }
        }
        return existing;
    }

    private boolean ownsMember(final EntityKoaBase ent) {
        final int entID = ent.getAIAgent().entID;
        if (entID == -1) {
            return false;
        }
        for (final int slot : this.entIDs) {
            if (slot == entID) {
                return true;
            }
        }
        return false;
    }

    public boolean getCanSpawnHere(final Entity ent) {
        final boolean b1 = !this.worldObj.checkBlockCollision(ent.boundingBox);
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
                this.updateList();
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
        for (int i = 0; i < this.entIDs.length; ++i) {
            this.entIDs[i] = nbttagcompound.getInteger("entID_" + i);
        }
        this.needListUpdate = true;
    }

    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("unbreakable", this.unbreakable);
        for (int i = 0; i < this.entIDs.length; ++i) {
            nbttagcompound.setInteger("entID_" + i, this.entIDs[i]);
        }
    }

    public Block getBlockType() {
        return (Block) TCBlockRegistry.koaChest;
    }
}
