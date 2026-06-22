package net.tropicraft.world.location;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import CoroUtil.world.location.ManagedLocation;
import build.world.BuildDirectionHelper;

public class TownKoaVillageGenHelper {

    public static int areaLength;
    public static int areaWidth;
    public static int areaHeight;

    public static boolean hookTryGenVillage(final ChunkCoordinates parCoords, final World parWorld) {
        final int directionTry = getBestGenDirection(parCoords, parWorld);
        if (directionTry != -1) {
            System.out.println("test success! dir: " + directionTry);
            final ChunkCoordinates centerCoords = getCoordsFromAdjustedDirection(parCoords, directionTry);
            System.out.println("centerCoords: " + centerCoords);
            final TownKoaVillage village = new TownKoaVillage();
            final WorldDirector wd = WorldDirectorManager.instance()
                .getCoroUtilWorldDirector(parWorld);
            final int minDistBetweenVillages = 128;
            for (final ManagedLocation town : wd.lookupTickingManagedLocations.values()) {
                if (Math.sqrt(town.spawn.getDistanceSquaredToChunkCoordinates(parCoords)) < minDistBetweenVillages) {
                    return false;
                }
            }
            final int newID = wd.lookupTickingManagedLocations.size();
            village.initData(newID, parWorld.provider.dimensionId, centerCoords);
            village.direction = directionTry;
            village.initFirstTime();
            wd.addTickingLocation((ManagedLocation) village);
            return true;
        }
        return false;
    }

    public static int getBestGenDirection(final ChunkCoordinates parCoords, final World parWorld) {
        if (isClear(parCoords, parWorld, 1, 0)) {
            return 0;
        }
        if (isClear(parCoords, parWorld, 0, -1)) {
            return 1;
        }
        if (isClear(parCoords, parWorld, -1, 0)) {
            return 2;
        }
        if (isClear(parCoords, parWorld, 0, 1)) {
            return 3;
        }
        return -1;
    }

    public static boolean isClear(final ChunkCoordinates parCoords, final World parWorld, final int scanX,
        final int scanZ) {
        final int sizeHorizMax = TownKoaVillageGenHelper.areaWidth;
        final int sizeMiddle = TownKoaVillageGenHelper.areaWidth / 2;
        final int topYBeach = 62;
        final Block blockScanBeach = parWorld.getBlock(parCoords.posX, topYBeach, parCoords.posZ);
        if (blockScanBeach.getMaterial() == Material.sand) {
            final int topYMiddle = 62;
            final Block blockScanMiddle = parWorld
                .getBlock(parCoords.posX + sizeMiddle * scanX, topYMiddle, parCoords.posZ + sizeMiddle * scanZ);
            System.out.println("testing scanX: " + scanX + " scanZ: " + scanZ);
            if (blockScanMiddle.getMaterial() == Material.water) {
                final Block blockScanEnd = parWorld
                    .getBlock(parCoords.posX + sizeHorizMax * scanX, topYMiddle, parCoords.posZ + sizeHorizMax * scanZ);
                System.out.println(
                    "testing blockScanEnd x: " + (parCoords.posX + sizeHorizMax * scanX)
                        + " z: "
                        + (parCoords.posZ + sizeHorizMax * scanZ));
                if (blockScanEnd.getMaterial() == Material.water) {
                    for (int i = 1; i <= 4; ++i) {
                        final int sizeStep = sizeHorizMax / 4 * i;
                        final Block blockScanFrontLeft = parWorld
                            .getBlock(parCoords.posX + sizeStep * scanZ, topYMiddle, parCoords.posZ + sizeStep * scanX);
                        final Block blockScanFrontRight = parWorld.getBlock(
                            parCoords.posX + sizeStep * scanZ * -1,
                            topYMiddle,
                            parCoords.posZ + sizeStep * scanX * -1);
                        System.out.println(
                            "testing blockScanFrontLeft x: " + (parCoords.posX + sizeStep * scanZ)
                                + " z: "
                                + (parCoords.posZ + sizeStep * scanX));
                        System.out.println(
                            "testing blockScanFrontRight x: " + (parCoords.posX + sizeStep * scanZ * -1)
                                + " z: "
                                + (parCoords.posZ + sizeStep * scanX * -1));
                        if (blockScanFrontLeft.getMaterial() != Material.water
                            || blockScanFrontRight.getMaterial() != Material.water) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static ChunkCoordinates getCoordsFromAdjustedDirection(final ChunkCoordinates parCoords,
        final int parDirection) {
        return new ChunkCoordinates(
            parCoords.posX
                + TownKoaVillageGenHelper.areaWidth / 2 * BuildDirectionHelper.getDirectionToCoords(parDirection).posX,
            parCoords.posY,
            parCoords.posZ
                + TownKoaVillageGenHelper.areaWidth / 2 * BuildDirectionHelper.getDirectionToCoords(parDirection).posZ);
    }

    static {
        TownKoaVillageGenHelper.areaLength = 76;
        TownKoaVillageGenHelper.areaWidth = 86;
        TownKoaVillageGenHelper.areaHeight = 16;
    }
}
