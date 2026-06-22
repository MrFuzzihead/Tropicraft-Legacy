package net.tropicraft.registry;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class TCNetworkRegistry {

    public static final SimpleNetworkWrapper network;

    public static void init() {}

    static {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("tropicraft");
    }
}
