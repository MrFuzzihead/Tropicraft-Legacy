package net.tropicraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.world.TCTimeAndWeatherData;

/**
 * The time and weather of every loaded dimension side by side, which is the quickest way to see
 * whether a dimension is running on its own clock or still reading the overworld's (issue #35).
 * <p>
 * {@code /tctime set <time>} changes the time in the sender's own dimension only. Vanilla's /time is
 * no use for testing this: {@code CommandTime} loops over every loaded dimension and sets them all,
 * which makes separate clocks look like one shared clock. Two independent clocks also tick at the
 * same rate, so they only visibly come apart when something per-dimension changes one of them -
 * this command, or sleeping.
 */
public class CommandTCClock extends CommandBase {

    public String getCommandName() {
        return "tctime";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "/tctime [set <time>]";
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        if (args.length > 1 && args[0].equals("set")) {
            final long time = parseIntWithMin(commandSender, args[1], 0);
            final World world = commandSender.getEntityWorld();
            if (world == null) {
                commandSender.addChatMessage(
                    new ChatComponentText("/tctime set needs to be run by something standing in a dimension."));
                return;
            }
            world.setWorldTime(time);
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
