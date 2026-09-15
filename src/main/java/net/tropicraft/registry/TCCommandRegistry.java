package net.tropicraft.registry;

import net.minecraft.command.ICommand;
import net.tropicraft.command.CommandTropicraft;
import net.tropicraft.command.CommandTropicsTeleport;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class TCCommandRegistry {

    public static void init(final FMLServerStartingEvent event) {
        // /tc is the root for everything the mod adds - /tc time, /tc weather, /tc tropics. The only
        // command registered on its own is /tropics, which players have been typing since long before
        // there was a root to put it under.
        event.registerServerCommand((ICommand) new CommandTropicraft());
        event.registerServerCommand((ICommand) new CommandTropicsTeleport());
    }
}
