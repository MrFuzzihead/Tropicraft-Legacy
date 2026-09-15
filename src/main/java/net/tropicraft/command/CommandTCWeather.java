package net.tropicraft.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

/**
 * {@code /tc weather} - starts or stops rain and thunder in the dimension the sender is stood in,
 * the way modern {@code /weather} does.
 * <p>
 * Vanilla 1.7.10 has no way of doing this outside the overworld: {@code CommandToggleDownfall} works
 * on {@code MinecraftServer.getServer().worldServers[0]} and nothing else, so it could never touch
 * the Tropics (issue #35). Since the Tropics run on their own weather now, that left no way to see
 * it - natural tropical rain takes the best part of an hour to arrive on its own.
 * <p>
 * The countdowns are set alongside the flags, exactly as vanilla keeps them, so the weather then
 * behaves normally rather than flipping back on the next tick: a rain flag with a spent countdown
 * ends immediately.
 */
public class CommandTCWeather extends CommandBase {

    /** Ten minutes, which is what vanilla leaves the weather on when it clears up. */
    private static final int DEFAULT_DURATION = 12000;

    public String getCommandName() {
        return "weather";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "/tc weather <clear|rain|thunder> [ticks]";
    }

    public List<String> addTabCompletionOptions(final ICommandSender commandSender, final String[] args) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "clear", "rain", "thunder" })
            : null;
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            throw new WrongUsageException(getCommandUsage(commandSender), new Object[0]);
        }
        final World world = commandSender.getEntityWorld();
        if (world.provider.hasNoSky) {
            commandSender.addChatMessage(
                new ChatComponentText(
                    "Dim " + world.provider.dimensionId + " has no sky, so it has no weather to change."));
            return;
        }

        final int ticks = args.length > 1 ? parseIntBounded(commandSender, args[1], 1, Integer.MAX_VALUE)
            : DEFAULT_DURATION;
        final boolean raining;
        final boolean thundering;
        switch (args[0].toLowerCase()) {
            case "clear":
                raining = false;
                thundering = false;
                break;
            case "rain":
                raining = true;
                thundering = false;
                break;
            case "thunder":
                raining = true;
                thundering = true;
                break;
            default:
                throw new WrongUsageException(getCommandUsage(commandSender), new Object[0]);
        }

        final WorldInfo info = world.getWorldInfo();
        info.setRaining(raining);
        info.setThundering(thundering);
        info.setRainTime(ticks);
        info.setThunderTime(ticks);

        commandSender.addChatMessage(
            new ChatComponentText(
                String.format(
                    "Dim %d (%s): raining %s (%d), thundering %s (%d)",
                    world.provider.dimensionId,
                    world.provider.getDimensionName(),
                    info.isRaining(),
                    info.getRainTime(),
                    info.isThundering(),
                    info.getThunderTime())));
    }
}
