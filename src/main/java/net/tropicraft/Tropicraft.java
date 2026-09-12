package net.tropicraft;

import net.minecraftforge.common.MinecraftForge;
import net.tropicraft.config.ConfigBiomes;
import net.tropicraft.config.ConfigGenRates;
import net.tropicraft.config.ConfigMisc;
import net.tropicraft.drinks.MixerRecipes;
import net.tropicraft.encyclopedia.Encyclopedia;
import net.tropicraft.event.TCBlockEvents;
import net.tropicraft.event.TCItemEvents;
import net.tropicraft.event.TCMiscEvents;
import net.tropicraft.event.TCPacketEvents;
import net.tropicraft.proxy.ISuperProxy;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCommandRegistry;
import net.tropicraft.registry.TCCraftingRegistry;
import net.tropicraft.registry.TCEntityRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;
import net.tropicraft.registry.TCTileEntityRegistry;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.TCWorldGenerator;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import modconfig.ConfigMod;
import modconfig.IConfigCategory;

@Mod(
    modid = Tropicraft.MODID,
    name = Tropicraft.MODNAME,
    version = Tags.VERSION,
    dependencies = "required-after:CoroAI")
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
