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

    /**
     * Level 2, the same as vanilla gives /time, /toggledownfall and /tp, so a server operator does not
     * need to be a full level 4 admin to look at the clocks or change the island's weather.
     */
    public int getRequiredPermissionLevel() {
        return 2;
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
}
