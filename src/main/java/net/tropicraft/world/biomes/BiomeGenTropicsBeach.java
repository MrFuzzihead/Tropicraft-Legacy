package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.tropicraft.world.location.TownKoaVillageGenHelper;
import net.tropicraft.world.worldgen.WorldGenTropicsTreasure;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

    private static final int TREASURE_CHANCE = 25;
    private static final int VILLAGE_CHANCE = 10;
    private static final int VILLAGE_ATTEMPTS = 3;

    /** Set once a village attempt throws, so a persistently broken world logs one trace instead of thousands. */
    private static boolean villageFailureLogged;

    public BiomeGenTropicsBeach(final int biomeID) {
        super(biomeID);
    }

    public void decorate(final World world, final Random rand, final int x, final int z) {
        if (rand.nextInt(TREASURE_CHANCE) == 0) {
            final int i = this.randCoord(rand, x, 16);
            final int k = this.randCoord(rand, z, 16);
            new WorldGenTropicsTreasure(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }
        if (rand.nextInt(VILLAGE_CHANCE) == 0) {
            boolean success = false;
            for (int attempt = 0; attempt < VILLAGE_ATTEMPTS && !success; ++attempt) {
                final int j = this.randCoord(rand, x, 16);
                final int l = this.randCoord(rand, z, 16);
                try {
                    success = TownKoaVillageGenHelper
                        .hookTryGenVillage(new ChunkCoordinates(j, this.getTerrainHeightAt(world, j, l), l), world);
                } catch (final Throwable t) {
                    // Deliberately Throwable and not Exception: a failure escaping into chunk population leaves the
                    // chunk broken on every subsequent load, which is the crash #30 reported. Containment is the
                    // point; what was wrong was the silence and the per-chunk log spam.
                    reportVillageFailure(j, l, t);
                }
            }
        }
        super.decorate(world, rand, x, z);
    }

    private static void reportVillageFailure(final int x, final int z, final Throwable t) {
        if (villageFailureLogged) {
            return;
        }
        villageFailureLogged = true;
        FMLLog.log(
            Level.ERROR,
            t,
            "[Tropicraft] Failed to generate a Koa village near %d, %d. This is logged once per session; "
                + "beach chunks will keep generating without villages.",
            x,
            z);
    }
}
