package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;
import net.tropicraft.Tropicraft;
import net.tropicraft.client.entity.model.ModelScubaGear;
import net.tropicraft.client.renderer.block.BambooChestRenderHandler;
import net.tropicraft.client.renderer.block.BambooChuteRenderHandler;
import net.tropicraft.client.renderer.block.CoffeePlantRenderHandler;
import net.tropicraft.client.renderer.block.CurareBowlRenderHandler;
import net.tropicraft.client.renderer.block.EIHMixerRenderHandler;
import net.tropicraft.client.renderer.block.FlowerPotRenderHandler;
import net.tropicraft.client.renderer.block.TikiTorchRenderHandler;
import net.tropicraft.encyclopedia.Encyclopedia;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCraftingRegistry;
import net.tropicraft.registry.TCRenderRegistry;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerBooks() {
        Tropicraft.encyclopedia = new Encyclopedia(
            "eTsave.dat",
            "/assets/tropicraft/gui/EncyclopediaTropica.txt",
            "encyclopediaTropica",
            "encyclopediaTropicaInside");
        TCCraftingRegistry.addItemsToEncyclopedia();
    }

    @Override
    public void initRenderHandlersAndIDs() {
        TCRenderIDs.coffeePlant = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.tikiTorch = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.flowerPot = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.bambooChest = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.bambooChute = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.curareBowl = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.eihMixer = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new CoffeePlantRenderHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new TikiTorchRenderHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new FlowerPotRenderHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new BambooChestRenderHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new BambooChuteRenderHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new CurareBowlRenderHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) new EIHMixerRenderHandler());
    }

    @Override
    public void initRenderRegistry() {
        TCRenderRegistry.initEntityRenderers();
        TCRenderRegistry.initTileEntityRenderers();
    }

    @Override
    public ModelBiped getArmorModel(final int id) {
        if (id == 0) {
            return (ModelBiped) new ModelScubaGear();
        }
        return null;
    }

    @Override
    public void preInit() {}
}
