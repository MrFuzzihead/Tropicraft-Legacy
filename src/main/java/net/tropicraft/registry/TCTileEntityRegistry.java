package net.tropicraft.registry;

import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.block.tileentity.TileEntityBambooMug;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.block.tileentity.TileEntityEIHMixer;
import net.tropicraft.block.tileentity.TileEntityFirePit;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.block.tileentity.TileEntitySifter;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;

import cpw.mods.fml.common.registry.GameRegistry;

public class TCTileEntityRegistry {

    public static void init() {
        registerTE((Class<? extends TileEntity>) TileEntityTropicraftFlowerPot.class, "TCFlowerPot");
        registerTE((Class<? extends TileEntity>) TileEntityFirePit.class, "TCFirePit");
        registerTE((Class<? extends TileEntity>) TileEntityAirCompressor.class, "TCAirCompressor");
        registerTE((Class<? extends TileEntity>) TileEntityBambooChest.class, "TCBambooChest");
        registerTE((Class<? extends TileEntity>) TileEntitySifter.class, "TCSifter");
        registerTE((Class<? extends TileEntity>) TileEntityCurareBowl.class, "TCCurareBowl");
        registerTE((Class<? extends TileEntity>) TileEntityKoaChest.class, "TCKoaChest");
        registerTE((Class<? extends TileEntity>) TileEntityPurchasePlate.class, "TCTradePlate");
        registerTE((Class<? extends TileEntity>) TileEntityBambooMug.class, "TCBambooMug");
        registerTE((Class<? extends TileEntity>) TileEntityEIHMixer.class, "TCEIHMixer");
    }

    private static void registerTE(final Class<? extends TileEntity> clazz, final String name) {
        GameRegistry.registerTileEntity((Class) clazz, name);
    }
}
