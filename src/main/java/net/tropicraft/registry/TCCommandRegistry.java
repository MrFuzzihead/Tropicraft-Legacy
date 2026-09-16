package net.tropicraft.registry;

import net.minecraft.command.ICommand;
import net.tropicraft.command.CommandTropicraft;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class TCCommandRegistry {

    public static void init(final FMLServerStartingEvent event) {
        // /tc is the only root the mod registers - /tc time, /tc weather and /tc tropics hang off it.
        event.registerServerCommand((ICommand) new CommandTropicraft());
    }
}
