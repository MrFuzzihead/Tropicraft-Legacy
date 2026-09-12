package net.tropicraft.world.location;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import CoroUtil.world.location.ISimulationTickable;
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
            for (final ISimulationTickable town : wd.lookupTickingManagedLocations.values()) {
                if (town instanceof ManagedLocation
                    && Math.sqrt(((ManagedLocation) town).spawn.getDistanceSquaredToChunkCoordinates(parCoords))
                        < minDistBetweenVillages) {
                    return false;
                }
            }
            final int newID = wd.lookupTickingManagedLocations.size();
            village.initData(newID, parWorld.provider.dimensionId, centerCoords);
            village.direction = directionTry;
            village.initFirstTime();
            wd.addTickingLocation(village);
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
        final Block blockScanBeach = getBlockIfLoaded(parWorld, parCoords.posX, topYBeach, parCoords.posZ);
        if (blockScanBeach != null && blockScanBeach.getMaterial() == Material.sand) {
            final int topYMiddle = 62;
            final Block blockScanMiddle = getBlockIfLoaded(
                parWorld,
                parCoords.posX + sizeMiddle * scanX,
                topYMiddle,
                parCoords.posZ + sizeMiddle * scanZ);
            if (blockScanMiddle != null && blockScanMiddle.getMaterial() == Material.water) {
                final Block blockScanEnd = getBlockIfLoaded(
                    parWorld,
                    parCoords.posX + sizeHorizMax * scanX,
                    topYMiddle,
                    parCoords.posZ + sizeHorizMax * scanZ);
                if (blockScanEnd != null && blockScanEnd.getMaterial() == Material.water) {
                    for (int i = 1; i <= 4; ++i) {
                        final int sizeStep = sizeHorizMax / 4 * i;
                        final Block blockScanFrontLeft = getBlockIfLoaded(
                            parWorld,
                            parCoords.posX + sizeStep * scanZ,
                            topYMiddle,
                            parCoords.posZ + sizeStep * scanX);
                        final Block blockScanFrontRight = getBlockIfLoaded(
                            parWorld,
                            parCoords.posX + sizeStep * scanZ * -1,
                            topYMiddle,
                            parCoords.posZ + sizeStep * scanX * -1);
                        if (blockScanFrontLeft == null || blockScanFrontRight == null
                            || blockScanFrontLeft.getMaterial() != Material.water
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

    /**
     * Reads a block only if its chunk is already loaded, returning null otherwise. Village scanning can
     * probe up to {@link #areaWidth} blocks away from the chunk being populated; reading an unloaded chunk
     * would synchronously generate it (cascading worldgen), so unloaded probes are treated as "not clear".
     * This method consumes no random state, so other decoration in the chunk is unaffected.
     */
    private static Block getBlockIfLoaded(final World parWorld, final int x, final int y, final int z) {
        if (!parWorld.getChunkProvider()
            .chunkExists(x >> 4, z >> 4)) {
            return null;
        }
        return parWorld.getBlock(x, y, z);
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
