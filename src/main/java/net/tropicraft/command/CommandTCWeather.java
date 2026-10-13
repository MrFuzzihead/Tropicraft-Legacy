package net.tropicraft.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.TCTimeAndWeatherData;

/**
 * {@code /tc weather} - starts or stops the rain and thunder of the Tropicraft dimension, the way
 * modern {@code /weather} does.
 * <p>
 * It only ever touches the Tropics, which vanilla 1.7.10 has no way of doing at all:
 * {@code CommandToggleDownfall} works on {@code MinecraftServer.getServer().worldServers[0]} and
 * nothing else, and there is no {@code /weather} yet. Since the Tropics run on their own weather now
 * (issue #35), that left no way to see it - natural tropical rain takes the best part of an hour to
 * arrive on its own. The overworld keeps its own /toggledownfall.
 * <p>
 * The countdowns are set alongside the flags, exactly as vanilla keeps them, so the weather then
 * behaves normally rather than flipping back on the next tick: a rain flag with a spent countdown
 * ends immediately. Note that a vanilla countdown is "ticks until this weather changes", so
 * {@code clear 6000} means six thousand ticks of clear skies and then rain.
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

        final WorldServer tropics = TropicraftWorldUtils.tropics();
        final WorldInfo info = tropics.getWorldInfo();
        if (!TCTimeAndWeatherData.isDetached(info)) {
            commandSender.addChatMessage(
                new ChatComponentText(
                    "The Tropics are not keeping their own weather (separateTimeAndWeather is off, or the mixins did not apply), so there is nothing to set."));
            return;
        }

        info.setRaining(raining);
        info.setThundering(thundering);
        info.setRainTime(ticks);
        info.setThunderTime(thunderCountdown(ticks, thundering));

        commandSender.addChatMessage(
            new ChatComponentText(
                String.format(
                    "Dim %d (%s): raining %s (%d), thundering %s (%d)",
                    tropics.provider.dimensionId,
                    tropics.provider.getDimensionName(),
                    info.isRaining(),
                    info.getRainTime(),
                    info.isThundering(),
                    info.getThunderTime())));
    }

    /**
     * The two countdowns are independent, and each one flips its own flag when it runs out - thunder
     * first. A thunder countdown that ended together with the rain would therefore start a
     * thunderstorm on the very tick the requested rain stops, so it is only lined up with the rain
     * when thunder is being switched on; otherwise it is left well behind, where it takes over as
     * vanilla's clear-sky countdown.
     */
    private static int thunderCountdown(final int ticks, final boolean thundering) {
        return thundering ? ticks : (int) Math.min(Integer.MAX_VALUE, (long) ticks + DEFAULT_DURATION);
    }
}
