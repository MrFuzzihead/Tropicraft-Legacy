package net.tropicraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.tropicraft.config.ConfigMisc;
import net.tropicraft.world.TeleporterTropics;
import net.tropicraft.world.WorldProviderTropicraft;

import cpw.mods.fml.common.FMLLog;

public class TropicraftWorldUtils {

    /**
     * The dimension id the Tropics live in, read from {@link net.tropicraft.config.ConfigMisc#tropicsDimensionId} at
     * startup. Deliberately not {@code final}: a {@code static final int} with a constant initialiser is inlined into
     * every use site at compile time, so a config value would never reach them.
     */
    public static int TROPICS_DIMENSION_ID = -127;

    /**
     * Registers the Tropics dimension. Logs - and falls back to the default id in the log message - when the
     * configured id cannot be used, so a collision with another mod is visible instead of a silently missing
     * dimension.
     */
    public static void initializeDimension() {
        TropicraftWorldUtils.TROPICS_DIMENSION_ID = ConfigMisc.tropicsDimensionId;
        final boolean providerRegistered = DimensionManager
            .registerProviderType(TROPICS_DIMENSION_ID, WorldProviderTropicraft.class, true);
        if (!providerRegistered) {
            FMLLog.severe(
                "[Tropicraft] Could not register the Tropics world provider for dimension id %d - another mod already "
                    + "uses it. Change tropicsDimensionId in the Tropicraft_Misc config.",
                TROPICS_DIMENSION_ID);
        }
        DimensionManager.registerDimension(TROPICS_DIMENSION_ID, TROPICS_DIMENSION_ID);
        if (!DimensionManager.isDimensionRegistered(TROPICS_DIMENSION_ID)) {
            FMLLog.severe(
                "[Tropicraft] Dimension %d was not registered; the Tropics will not be reachable. See the provider "
                    + "message above.",
                TROPICS_DIMENSION_ID);
        }
    }

    /**
     * The Tropicraft dimension, loading it if the server has not needed it until now.
     */
    public static WorldServer tropics() {
        return MinecraftServer.getServer()
            .worldServerForDimension(TROPICS_DIMENSION_ID);
    }

    public static void teleportPlayer(final EntityPlayerMP player) {
        final int destination = player.dimension == TROPICS_DIMENSION_ID ? 0 : TROPICS_DIMENSION_ID;
        final TeleporterTropics tropicsTeleporter = new TeleporterTropics(
            MinecraftServer.getServer()
                .worldServerForDimension(destination));
        final ServerConfigurationManager scm = MinecraftServer.getServer()
            .getConfigurationManager();
        scm.transferPlayerToDimension(player, destination, (Teleporter) tropicsTeleporter);
    }
}
