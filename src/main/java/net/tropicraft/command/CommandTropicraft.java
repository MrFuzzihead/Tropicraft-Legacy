package net.tropicraft.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

/**
 * The one root command of the mod - everything Tropicraft adds hangs off {@code /tc}.
 * <p>
 * Each sub-command is an {@link ICommand} that owns its own parsing, tab completion and usage text;
 * the root only matches the first word and hands over the rest of the arguments.
 */
public class CommandTropicraft extends CommandBase {

    /** The sub-commands, in the order {@code /tc} lists and completes them. */
    private static final ICommand[] SUBCOMMANDS = { new CommandTCTime(), new CommandTCWeather(),
        new CommandTropicsTeleport() };

    public String getCommandName() {
        return "tc";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "/tc <" + String.join("|", names()) + ">";
    }

    public List<String> addTabCompletionOptions(final ICommandSender commandSender, final String[] args) {
        if (args.length <= 1) {
            return getListOfStringsMatchingLastWord(args, names());
        }
        final ICommand subcommand = find(args[0]);
        return subcommand == null ? null
            : subcommand.addTabCompletionOptions(commandSender, Arrays.copyOfRange(args, 1, args.length));
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        final ICommand subcommand = args.length > 0 ? find(args[0]) : null;
        if (subcommand == null) {
            throw new WrongUsageException(getCommandUsage(commandSender), new Object[0]);
        }
        subcommand.processCommand(commandSender, Arrays.copyOfRange(args, 1, args.length));
    }

    private static ICommand find(final String name) {
        for (final ICommand subcommand : SUBCOMMANDS) {
            if (subcommand.getCommandName()
                .equalsIgnoreCase(name)) {
                return subcommand;
            }
        }
        return null;
    }

    private static String[] names() {
        final String[] names = new String[SUBCOMMANDS.length];
        for (int i = 0; i < names.length; ++i) {
            names[i] = SUBCOMMANDS[i].getCommandName();
        }
        return names;
    }

    /*
     * Dead code, and unreachable: this command was never registered, so the village tools that used
     * to be its whole body have never been callable. They are unfinished rather than broken -
     * village_repopulate walks the villages and does nothing at all - and village_clear would remove
     * every ticking managed location in the dimension, which is not something to hand out until
     * somebody decides what these are meant to do.
     * To bring one back, add a case for it below processCommand's switch and restore the imports it
     * needs: net.minecraft.entity.player.EntityPlayerMP, net.minecraft.util.ChunkCoordinates,
     * net.minecraft.util.MathHelper, net.tropicraft.world.location.TownKoaVillage,
     * net.tropicraft.world.location.TownKoaVillageGenHelper, CoroUtil.world.WorldDirector,
     * CoroUtil.world.WorldDirectorManager, CoroUtil.world.location.ISimulationTickable and
     * CoroUtil.world.location.ManagedLocation. Note that they all work on a player, so they need
     * CommandTropicraft.getCommandSenderAsPlayer(commandSender) - which is what they started with,
     * and which meant the command could not be run from the server console.
     * case "village_new" -> {
     * final int x = MathHelper.floor_double(player.posX);
     * final int z = MathHelper.floor_double(player.posZ);
     * int y = player.worldObj.getHeightValue(x, z);
     * if (y < 63) {
     * y = 64;
     * }
     * final TownKoaVillage village = new TownKoaVillage();
     * final WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
     * final int newID = wd.lookupTickingManagedLocations.size();
     * village.initData(newID, player.worldObj.provider.dimensionId, new ChunkCoordinates(x, y, z));
     * village.initFirstTime();
     * wd.addTickingLocation(village);
     * }
     * case "village_try" -> {
     * final int x = MathHelper.floor_double(player.posX);
     * final int z = MathHelper.floor_double(player.posZ);
     * int y = player.worldObj.getTopSolidOrLiquidBlock(x, z);
     * if (y < 63) {
     * y = 64;
     * }
     * TownKoaVillageGenHelper.hookTryGenVillage(new ChunkCoordinates(x, y, z), player.worldObj);
     * }
     * case "village_clear" -> {
     * final WorldDirector wd2 = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
     * for (final ISimulationTickable location : wd2.lookupTickingManagedLocations.values()) {
     * location.cleanup();
     * wd2.removeTickingLocation(location);
     * }
     * }
     * case "village_regen" -> {
     * final WorldDirector wd2 = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
     * for (final ISimulationTickable location : wd2.lookupTickingManagedLocations.values()) {
     * if (location instanceof ManagedLocation) {
     * ((ManagedLocation) location).initFirstTime();
     * }
     * }
     * }
     * case "village_repopulate" -> {
     * final WorldDirector wd2 = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
     * for (final ISimulationTickable location : wd2.lookupTickingManagedLocations.values()) {
     * if (location instanceof TownKoaVillage) {}
     * }
     * }
     */
}
