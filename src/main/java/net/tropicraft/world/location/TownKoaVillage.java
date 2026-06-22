package net.tropicraft.world.location;

import java.io.File;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.tropicraft.entity.koa.EntityKoaBase;
import net.tropicraft.entity.koa.EntityKoaFisher;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.entity.koa.EntityKoaShaman;
import net.tropicraft.entity.koa.EntityKoaTrader;

import CoroUtil.util.CoroUtilFile;
import CoroUtil.world.location.ManagedLocation;
import CoroUtil.world.location.SpawnLocationData;
import CoroUtil.world.location.town.TownObject;
import build.BuildServerTicks;
import build.ICustomGen;
import build.world.Build;
import build.world.BuildJob;
import build.world.BuildManager;

public class TownKoaVillage extends TownObject implements ICustomGen {

    public int areaLength;
    public int areaWidth;
    public int areaHeight;
    public int direction;

    public TownKoaVillage() {
        this.areaLength = TownKoaVillageGenHelper.areaLength;
        this.areaWidth = TownKoaVillageGenHelper.areaWidth;
        this.areaHeight = TownKoaVillageGenHelper.areaHeight;
        this.direction = 1;
    }

    public void tickUpdate() {
        super.tickUpdate();
        if (this.getWorld()
            .getTotalWorldTime() % 20L == 0L) {}
    }

    public void initFirstTime() {
        super.initFirstTime();
        this.generateSpawnCoords();
        this.genStructure();
    }

    public void genStructure() {
        this.genSchematic();
    }

    public void genSchematic() {
        final int yOffset = 0;
        final Build mainStructureData = new Build(
            this.spawn.posX,
            this.spawn.posY + yOffset,
            this.spawn.posZ,
            CoroUtilFile.getSaveFolderPath() + "TCSchematics" + File.separator + "koavillage");
        final BuildJob bj = new BuildJob(
            -99,
            this.spawn.posX,
            this.spawn.posY + yOffset,
            this.spawn.posZ,
            mainStructureData);
        bj.build.dim = this.getWorld().provider.dimensionId;
        bj.useFirstPass = false;
        bj.useRotationBuild = true;
        bj.build_rate = 100;
        bj.notifyFlag = 0;
        bj.setDirection(this.direction);
        bj.customGenCallback = (ICustomGen) this;
        this.areaHeight = mainStructureData.map_sizeY;
        BuildServerTicks.buildMan.addBuild(bj);
    }

    public void spawnEntitiesForce() {
        System.out.println("Spawning koa village population for village: " + this.spawn);
        this.tickMonitorPersistantMembers();
    }

    public void generateSpawnCoords() {
        final int y = 2;
        this.registerSpawnLocation(
            new SpawnLocationData(this.getRotatedCoordsWithRelFromCorner(77, 2 + y, 37), "shaman"));
        this.registerSpawnLocation(new SpawnLocationData(this.getRotatedCoordsWithRelFromCorner(25, y, 37), "trader"));
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(23, 1 + y, 20),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(38, 1 + y, 14),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(57, 1 + y, 3),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(63, 1 + y, 17),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(69, 1 + y, 3),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(23, 1 + y, 54),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(38, 1 + y, 60),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(57, 1 + y, 71),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(63, 1 + y, 57),
            new String[] { "fisher", "hunter" });
        this.registerSpawnLocation(
            this.getRotatedCoordsWithRelFromCorner(69, 1 + y, 71),
            new String[] { "fisher", "hunter" });
    }

    public ChunkCoordinates getRotatedCoordsWithRelFromCorner(final int x, final int y, final int z) {
        final ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
        return BuildManager.rotateNew(
            coords,
            this.direction,
            Vec3.createVectorHelper(0.0, 0.0, 0.0),
            Vec3.createVectorHelper((double) this.areaWidth, (double) this.areaHeight, (double) this.areaLength));
    }

    public void addEntity(final String unitType, final EntityLivingBase ent, final int parMemberID) {
        super.addEntity(unitType, ent);
    }

    public void spawnMemberAtSpawnLocation(final SpawnLocationData parData) {
        super.spawnMemberAtSpawnLocation(parData);
        EntityKoaBase ent = null;
        if (parData.type.equals("fisher")) {
            ent = (EntityKoaBase) new EntityKoaFisher(this.getWorld());
        } else if (parData.type.equals("hunter")) {
            ent = (EntityKoaBase) new EntityKoaHunter(this.getWorld());
        } else if (parData.type.equals("trader")) {
            ent = (EntityKoaBase) new EntityKoaTrader(this.getWorld());
        } else if (parData.type.equals("shaman")) {
            ent = (EntityKoaBase) new EntityKoaShaman(this.getWorld());
        }
        if (ent != null) {
            ent.getAIAgent()
                .setManagedLocation((ManagedLocation) this);
            ent.setPosition(
                (double) (this.spawn.posX + parData.coords.posX + 0.5f),
                (double) (this.spawn.posY + parData.coords.posY),
                (double) (this.spawn.posZ + parData.coords.posZ + 0.5f));
            this.getWorld()
                .spawnEntityInWorld((Entity) ent);
            this.addEntity(parData.type, (EntityLivingBase) ent);
            parData.entityUUID = ent.getPersistentID();
            ent.onSpawnWithEgg((IEntityLivingData) null);
        }
    }

    public void genPassPre(final World world, final BuildJob parBuildJob, final int parPass) {
        if (parPass == -1) {
            this.spawnEntitiesForce();
        }
    }

    public NBTTagCompound getInitNBTTileEntity() {
        return null;
    }

    public void readFromNBT(final NBTTagCompound var1) {
        super.readFromNBT(var1);
        this.direction = var1.getInteger("direction");
    }

    public void writeToNBT(final NBTTagCompound var1) {
        super.writeToNBT(var1);
        var1.setInteger("direction", this.direction);
    }
}
