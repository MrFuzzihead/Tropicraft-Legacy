package net.tropicraft.registry;

import net.minecraft.command.ICommand;
import net.tropicraft.command.CommandTropicsTeleport;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class TCCommandRegistry {

    public static void init(final FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand) new CommandTropicsTeleport());
    }
}
