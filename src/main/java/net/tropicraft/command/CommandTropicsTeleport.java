package net.tropicraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.tropicraft.util.TropicraftWorldUtils;

public class CommandTropicsTeleport extends CommandBase {

    public String getCommandName() {
        return "tropics";
    }

    public String getCommandUsage(final ICommandSender commandSender) {
        return "";
    }

    public void processCommand(final ICommandSender commandSender, final String[] args) {
        final EntityPlayerMP player = getCommandSenderAsPlayer(commandSender);
        TropicraftWorldUtils.teleportPlayer(player);
    }
}
