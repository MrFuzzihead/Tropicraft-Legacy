package net.tropicraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.world.location.TownKoaVillage;
import net.tropicraft.world.location.TownKoaVillageGenHelper;

import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import CoroUtil.world.location.ISimulationTickable;

/**
 * {@code /tc village} - look at, and place, Koa villages.
 * <p>
 * These are the tools issue #49 asked for after the original {@code village_*} commands were removed. They are
 * server-side and destructive enough to need level 4, and the command checks that itself: {@code /tc} only requires
 * level 2, so a sub-command cannot rely on the root's permission.
 * </p>
 * <p>
 * Deliberately not restored: {@code village_clear} (it removed every ticking location in the dimension) and
 * {@code village_repopulate} (it was an empty loop).
 * </p>
 */
public class CommandTCVillage extends CommandBase {

    public String getCommandName() {
        return "village";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "/tc village <locate|list|try|regen>";
    }

    public int getRequiredPermissionLevel() {
        return 4;
    }

    public List<String> addTabCompletionOptions(final ICommandSender commandSender, final String[] args) {
        return args.length == 1
            ? getListOfStringsMatchingLastWord(args, new String[] { "locate", "list", "try", "regen" })
            : null;
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        if (!commandSender.canCommandSenderUseCommand(4, "tc")) {
            throw new CommandException("You need to be an operator to change villages.");
        }
        if (args.length == 0) {
            throw new WrongUsageException(getCommandUsage(commandSender), new Object[0]);
        }
        final World world = commandSender.getEntityWorld();
        final List<TownKoaVillage> villages = villagesIn(world);
        final String subcommand = args[0].toLowerCase();
        if (subcommand.equals("list")) {
            if (villages.isEmpty()) {
                commandSender
                    .addChatMessage(new ChatComponentText("No Koa villages are registered in this dimension."));
                return;
            }
            for (final TownKoaVillage village : villages) {
                commandSender.addChatMessage(new ChatComponentText(describe(village)));
            }
            return;
        }
        if (subcommand.equals("locate")) {
            final TownKoaVillage nearest = nearest(villages, commandSender);
            commandSender.addChatMessage(
                new ChatComponentText(
                    nearest == null ? "No Koa village is registered in this dimension."
                        : "Nearest: " + describe(nearest)));
            return;
        }
        if (subcommand.equals("try")) {
            final int x = MathHelper.floor_double(commandSender.getPlayerCoordinates().posX);
            final int y = MathHelper.floor_double(commandSender.getPlayerCoordinates().posY);
            final int z = MathHelper.floor_double(commandSender.getPlayerCoordinates().posZ);
            final boolean generated = TownKoaVillageGenHelper.hookTryGenVillage(new ChunkCoordinates(x, y, z), world);
            commandSender.addChatMessage(
                new ChatComponentText(
                    generated ? "Village placed near " + x + ", " + y + ", " + z + "."
                        : "No village fits here: the site needs sand at beach height with open water on three sides, "
                            + "at least 128 blocks from any other village (the log says which check failed)."));
            return;
        }
        if (subcommand.equals("regen")) {
            final TownKoaVillage nearest = nearest(villages, commandSender);
            if (nearest == null) {
                commandSender.addChatMessage(
                    new ChatComponentText("No Koa village is registered in this dimension to rebuild."));
                return;
            }
            nearest.genStructure();
            commandSender.addChatMessage(
                new ChatComponentText(
                    "Rebuilding the village at " + nearest.spawn.posX
                        + ", "
                        + nearest.spawn.posY
                        + ", "
                        + nearest.spawn.posZ
                        + (nearest.isStructureQueued() ? "."
                            : " - but the schematic could not be read, see the log.")));
            return;
        }
        throw new WrongUsageException(getCommandUsage(commandSender), new Object[0]);
    }

    private static String describe(final TownKoaVillage village) {
        return "village at " + village.spawn.posX
            + ", "
            + village.spawn.posY
            + ", "
            + village.spawn.posZ
            + " (direction "
            + village.direction
            + ", structure queued: "
            + village.isStructureQueued()
            + ")";
    }

    /** The Koa villages the world director is currently simulating for this world. */
    private static List<TownKoaVillage> villagesIn(final World world) {
        final List<TownKoaVillage> villages = new ArrayList<TownKoaVillage>();
        if (world == null) {
            return villages;
        }
        final WorldDirector director = WorldDirectorManager.instance()
            .getCoroUtilWorldDirector(world);
        if (director == null) {
            return villages;
        }
        for (final ISimulationTickable location : director.lookupTickingManagedLocations.values()) {
            if (location instanceof TownKoaVillage) {
                villages.add((TownKoaVillage) location);
            }
        }
        return villages;
    }

    private static TownKoaVillage nearest(final List<TownKoaVillage> villages, final ICommandSender sender) {
        final ChunkCoordinates from = sender.getPlayerCoordinates();
        TownKoaVillage nearest = null;
        double best = Double.MAX_VALUE;
        for (final TownKoaVillage village : villages) {
            final double distance = village.spawn.getDistanceSquaredToChunkCoordinates(from);
            if (distance < best) {
                best = distance;
                nearest = village;
            }
        }
        return nearest;
    }
}
