package net.tropicraft.util;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;

public class TropicraftUtils {

    public static void sync(final Packet packet, final int dimensionID) {
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getConfigurationManager()
            .sendPacketToAllPlayersInDimension(packet, dimensionID);
    }

    public static ResourceLocation getTexture(final String path) {
        final ResourceLocation derp = new ResourceLocation("tropicraft", path);
        return derp;
    }

    public static ResourceLocation getTextureArmor(final String path) {
        return getTexture(String.format("textures/models/armor/%s.png", path));
    }

    public static ResourceLocation getTextureBlock(final String path) {
        return getTexture(String.format("textures/blocks/%s.png", path));
    }

    public static ResourceLocation getTextureEntity(final String path) {
        return getTexture(String.format("textures/entity/%s.png", path));
    }

    public static ResourceLocation getTextureGui(final String path) {
        return getTexture(String.format("textures/gui/%s.png", path));
    }

    public static ResourceLocation getTextureTE(final String path) {
        return getTexture(String.format("textures/blocks/te/%s.png", path));
    }

    public static ResourceLocation bindTextureArmor(final String path) {
        return bindTexture(getTextureArmor(path));
    }

    public static ResourceLocation bindTextureEntity(final String path) {
        return bindTexture(getTextureEntity(path));
    }

    public static ResourceLocation bindTextureGui(final String path) {
        return bindTexture(getTextureGui(path));
    }

    public static ResourceLocation bindTextureTE(final String path) {
        return bindTexture(getTextureTE(path));
    }

    public static ResourceLocation bindTextureBlock(final String path) {
        return bindTexture(getTextureBlock(path));
    }

    public static ResourceLocation bindTexture(final ResourceLocation resource) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(resource);
        return resource;
    }

    public static int getTopWaterBlockY(final World world, final int xCoord, final int zCoord) {
        int y;
        for (y = world.getHeightValue(xCoord, zCoord); world.getBlock(xCoord, y, zCoord)
            .getMaterial() != Material.water; --y) {}
        return y;
    }

    public static String translateGUI(final String word) {
        return StatCollector.translateToLocal(String.format("gui.tropicraft:%s", word));
    }

    public static int getSlotOfItemWithDamage(final InventoryPlayer inventory, final Item item, final int damage) {
        for (int j = 0; j < inventory.mainInventory.length; ++j) {
            if (inventory.mainInventory[j] != null && inventory.mainInventory[j].getItem() == item
                && inventory.mainInventory[j].getItemDamage() == damage) {
                return j;
            }
        }
        return -1;
    }

    public static int getSlotOfItem(final InventoryPlayer inventory, final Item item) {
        for (int j = 0; j < inventory.mainInventory.length; ++j) {
            if (inventory.mainInventory[j] != null && inventory.mainInventory[j].getItem() == item) {
                return j;
            }
        }
        return -1;
    }

    public static String translate(final String toBeTranslated) {
        return StatCollector.translateToLocal(toBeTranslated);
    }
}
