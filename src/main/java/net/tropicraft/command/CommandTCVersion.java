package net.tropicraft.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.tropicraft.Tags;
import net.tropicraft.Tropicraft;

/**
 * {@code /tc version} - prints the mod version, so a bug report can say which build it came from instead of "latest".
 */
public class CommandTCVersion extends CommandBase {

    public String getCommandName() {
        return "version";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "/tc version";
    }

    public List<String> addTabCompletionOptions(final ICommandSender commandSender, final String[] args) {
        return null;
    }

    /** Anyone may ask; there is nothing privileged in a version string. */
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        final World world = commandSender.getEntityWorld();
        final String side = world == null ? "unknown side" : (world.isRemote ? "client" : "server");
        commandSender.addChatMessage(
            new ChatComponentText(Tropicraft.MODNAME + " " + Tags.VERSION + " (Minecraft 1.7.10, " + side + ")"));
    }
}
