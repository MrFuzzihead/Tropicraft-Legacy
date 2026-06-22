package net.tropicraft.factory;

import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.block.tileentity.TileEntityBambooMug;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.block.tileentity.TileEntityFirePit;
import net.tropicraft.block.tileentity.TileEntitySifter;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;

public class TileEntityFactory {

    public static TileEntity getAirCompressorTE() {
        return (TileEntity) new TileEntityAirCompressor();
    }

    public static TileEntity getBambooChestTE() {
        return (TileEntity) new TileEntityBambooChest();
    }

    public static TileEntity getBambooMugTE() {
        return (TileEntity) new TileEntityBambooMug();
    }

    public static TileEntity getCurareBowlTE() {
        return (TileEntity) new TileEntityCurareBowl();
    }

    public static TileEntity getFirePitTE() {
        return (TileEntity) new TileEntityFirePit();
    }

    public static TileEntity getFlowerPotTE() {
        return (TileEntity) new TileEntityTropicraftFlowerPot();
    }

    public static TileEntity getSifterTE() {
        return (TileEntity) new TileEntitySifter();
    }
}
