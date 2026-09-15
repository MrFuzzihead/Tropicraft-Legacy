package net.tropicraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.tropicraft.world.location.TownKoaVillage;
import net.tropicraft.world.location.TownKoaVillageGenHelper;

import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import CoroUtil.world.location.ISimulationTickable;
import CoroUtil.world.location.ManagedLocation;

public class CommandTropicraft extends CommandBase {

    public String getCommandName() {
        return "tc";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "";
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        final EntityPlayerMP player = getCommandSenderAsPlayer(commandSender);
        if (args.length > 0) {
            switch (args[0]) {
                case "village_new" -> {
                    final int x = MathHelper.floor_double(player.posX);
                    final int z = MathHelper.floor_double(player.posZ);
                    int y = player.worldObj.getHeightValue(x, z);
                    if (y < 63) {
                        y = 64;
                    }
                    final TownKoaVillage village = new TownKoaVillage();
                    final WorldDirector wd = WorldDirectorManager.instance()
                        .getCoroUtilWorldDirector(player.worldObj);
                    final int newID = wd.lookupTickingManagedLocations.size();
                    village.initData(newID, player.worldObj.provider.dimensionId, new ChunkCoordinates(x, y, z));
                    village.initFirstTime();
                    wd.addTickingLocation(village);
                }
                case "village_try" -> {
                    final int x = MathHelper.floor_double(player.posX);
                    final int z = MathHelper.floor_double(player.posZ);
                    int y = player.worldObj.getTopSolidOrLiquidBlock(x, z);
                    if (y < 63) {
                        y = 64;
                    }
                    TownKoaVillageGenHelper.hookTryGenVillage(new ChunkCoordinates(x, y, z), player.worldObj);
                }
                case "village_clear" -> {
                    final WorldDirector wd2 = WorldDirectorManager.instance()
                        .getCoroUtilWorldDirector(player.worldObj);
                    for (final ISimulationTickable location : wd2.lookupTickingManagedLocations.values()) {
                        location.cleanup();
                        wd2.removeTickingLocation(location);
                    }
                }
                case "village_regen" -> {
                    final WorldDirector wd2 = WorldDirectorManager.instance()
                        .getCoroUtilWorldDirector(player.worldObj);
                    for (final ISimulationTickable location : wd2.lookupTickingManagedLocations.values()) {
                        if (location instanceof ManagedLocation) {
                            ((ManagedLocation) location).initFirstTime();
                        }
                    }
                }
                case "village_repopulate" -> {
                    final WorldDirector wd2 = WorldDirectorManager.instance()
                        .getCoroUtilWorldDirector(player.worldObj);
                    for (final ISimulationTickable location : wd2.lookupTickingManagedLocations.values()) {
                        if (location instanceof TownKoaVillage) {}
                    }
                }
            }
        }
    }
}
