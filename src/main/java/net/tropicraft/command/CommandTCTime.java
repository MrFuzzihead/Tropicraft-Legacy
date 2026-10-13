package net.tropicraft.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.TCTimeAndWeatherData;

/**
 * {@code /tc time} - the time and weather of every loaded dimension side by side, which is the
 * quickest way to see whether a dimension is running on its own clock or still reading the
 * overworld's (issue #35).
 * <p>
 * {@code /tc time set <time>} sets the Tropicraft clock and nothing else - the rest of the listing is
 * there to compare against. Vanilla's /time is no use for moving one dimension on its own: it loops
 * over every loaded dimension and sets them all, which is what makes separate clocks look like a
 * single shared one. The overworld keeps its own /time.
 * <p>
 * Two independent clocks also tick at the same rate, so they only visibly come apart once something
 * moves one of them: this command, sleeping, or vanilla /time moving all of them at once.
 */
public class CommandTCTime extends CommandBase {

    public String getCommandName() {
        return "time";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "/tc time [set <time>]";
    }

    public List<String> addTabCompletionOptions(final ICommandSender commandSender, final String[] args) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "set" }) : null;
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                throw new WrongUsageException(getCommandUsage(commandSender), new Object[0]);
            }
            final long time = parseIntWithMin(commandSender, args[1], 0);
            final WorldServer tropics = TropicraftWorldUtils.tropics();
            final WorldInfo info = tropics.getWorldInfo();
            if (!TCTimeAndWeatherData.isDetached(info)) {
                commandSender.addChatMessage(
                    new ChatComponentText(
                        "The Tropics are not keeping their own clock (separateTimeAndWeather is off, or the mixins did not apply), so there is nothing to set."));
                return;
            }
            info.setWorldTime(time);
        }

        for (final WorldServer world : MinecraftServer.getServer().worldServers) {
            if (world == null) {
                continue;
            }
            final WorldInfo info = world.getWorldInfo();
            commandSender.addChatMessage(
                new ChatComponentText(
                    String.format(
                        "Dim %d (%s): time %d, day %d, raining %s (%d), thundering %s (%d), own clock %s",
                        world.provider.dimensionId,
                        world.provider.getDimensionName(),
                        info.getWorldTime() % 24000L,
                        info.getWorldTime() / 24000L + 1,
                        info.isRaining(),
                        info.getRainTime(),
                        info.isThundering(),
                        info.getThunderTime(),
                        TCTimeAndWeatherData.isDetached(info))));
        }
    }
}
