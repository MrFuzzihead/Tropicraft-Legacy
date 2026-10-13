package net.tropicraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.tropicraft.world.TeleporterTropics;
import net.tropicraft.world.WorldProviderTropicraft;

public class TropicraftWorldUtils {

    public static final int TROPICS_DIMENSION_ID = -127;

    public static void initializeDimension() {
        DimensionManager.registerProviderType(TROPICS_DIMENSION_ID, (Class) WorldProviderTropicraft.class, true);
        DimensionManager.registerDimension(TROPICS_DIMENSION_ID, TROPICS_DIMENSION_ID);
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
