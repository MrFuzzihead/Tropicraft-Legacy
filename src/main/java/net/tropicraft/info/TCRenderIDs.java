package net.tropicraft.info;

/**
 * Block render ids, assigned once during {@code ClientProxy#initRenderHandlersAndIDs} in preInit.
 * <p>
 * These are plain fields rather than finals because they are filled in from the client proxy after class
 * initialisation; nothing else may write to them, and they must not be reassigned later (block render ids are baked
 * into the block instances that were registered with them). They are meaningless on a dedicated server, where the
 * proxy never touches them and they stay 0 - which is fine, nothing reads them there.
 * </p>
 */
public class TCRenderIDs {

    public static int airCompressor;
    public static int bambooChest;
    public static int koaChest;
    public static int coffeePlant;
    public static int tikiTorch;
    public static int flowerPot;
    public static int bambooChute;
    public static int curareBowl;
    public static int eihMixer;
}
