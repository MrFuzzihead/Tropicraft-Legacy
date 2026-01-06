package net.tropicraft;

import net.minecraftforge.common.*;
import net.tropicraft.config.*;
import net.tropicraft.drinks.*;
import net.tropicraft.encyclopedia.*;
import net.tropicraft.event.*;
import net.tropicraft.proxy.*;
import net.tropicraft.registry.*;
import net.tropicraft.util.*;
import net.tropicraft.world.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.*;
import modconfig.*;

@Mod(modid = Tropicraft.MODID, name = Tropicraft.MODNAME, version = Tags.VERSION)
public class Tropicraft {

    public static final String MODNAME = "Tropicraft";
    public static final String MODID = "tropicraft";

    @SidedProxy(clientSide = "net.tropicraft.proxy.ClientProxy", serverSide = "net.tropicraft.proxy.ServerProxy")
    public static ISuperProxy proxy;
    @Mod.Instance(value = MODID)
    public static Tropicraft instance;
    public static Encyclopedia encyclopedia;
    public static String eventChannelName;
    public static final FMLEventChannel eventChannel;

    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        TCCommandRegistry.init(event);
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        ConfigMod.addConfigFile(event, "tc_biomes", (IConfigCategory) new ConfigBiomes());
        ConfigMod.addConfigFile(event, "tc_genrates", (IConfigCategory) new ConfigGenRates());
        ConfigMod.addConfigFile(event, "tc_misc", (IConfigCategory) new ConfigMisc());
        ColorHelper.init();
        TCFluidRegistry.init();
        TCBlockRegistry.init();
        TCTileEntityRegistry.init();
        TCItemRegistry.init();
        TCFluidRegistry.postInit();
        TCKoaCurrencyRegistry.init();
        MixerRecipes.addMixerRecipes();
        Tropicraft.proxy.registerBooks();
        TCCraftingRegistry.init();
        Tropicraft.eventChannel.register(new TCPacketEvents());
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Tropicraft.proxy.initRenderHandlersAndIDs();
        TCEntityRegistry.init();
        Tropicraft.proxy.initRenderRegistry();
        MinecraftForge.EVENT_BUS.register(new TCBlockEvents());
        MinecraftForge.EVENT_BUS.register(new TCItemEvents());
        final TCMiscEvents misc = new TCMiscEvents();
        MinecraftForge.EVENT_BUS.register(misc);
        FMLCommonHandler.instance()
            .bus()
            .register(misc);
        GameRegistry.registerWorldGenerator(new TCWorldGenerator(), 10);
        TropicraftWorldUtils.initializeDimension();
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {}

    @Mod.EventHandler
    public void handleIMCMessages(final FMLInterModComms.IMCEvent event) {}

    public static void dbg(final Object obj) {
        final boolean consoleDebug = true;
        if (consoleDebug) {
            System.out.println(obj);
        }
    }

    static {
        Tropicraft.eventChannelName = "tropicraft";
        eventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(Tropicraft.eventChannelName);
    }
}
