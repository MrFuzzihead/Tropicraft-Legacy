package net.tropicraft.registry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.*;
import net.tropicraft.client.entity.model.*;
import net.tropicraft.client.entity.render.*;
import net.tropicraft.client.tileentity.*;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.entity.hostile.*;
import net.tropicraft.entity.koa.EntityKoaBase;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.passive.Failgull;
import net.tropicraft.entity.passive.VMonkey;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntitySnareTrap;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.placeable.EntityWallStarfish;
import net.tropicraft.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.entity.projectile.EntityTropicraftLeafballNew;
import net.tropicraft.entity.underdasea.*;

import CoroUtil.entity.EntityTropicalFishHook;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class TCRenderRegistry {

    public static void initEntityRenderers() {
        registerEntityRender(EntityChair.class, new RenderChair());
        registerEntityRender(EntityUmbrella.class, new RenderUmbrella());
        registerEntityRender(EntitySeahorse.class, new RenderSeahorse(new ModelSeahorse(), 0.75f));
        registerEntityRender(EntityDart.class, new RenderDart());
        registerEntityRender(EntityIguana.class, new RenderIguana(new ModelIguana(), 0.75f));
        registerEntityRender(EntityEIH.class, new RenderEIH(new ModelEIH(), 0.75f));
        registerEntityRender(EntityTreeFrog.class, new RenderTreeFrog(new ModelTreeFrog(), 0.5f));
        registerEntityRender(EntityPoisonBlot.class, new RenderPoisonBlot());
        registerEntityRender(EntitySeaTurtle.class, new RenderSeaTurtle(new ModelSeaTurtle(), 0.75f));
        registerEntityRender(EntityTurtleEgg.class, new RenderTurtleEgg(new ModelTurtleEgg(), 0.75f));
        registerEntityRender(EntityEagleRay.class, new RenderEagleRay());
        registerEntityRender(EntityAshenHunter.class, new RenderAshen(new ModelAshen(), 0.75f));
        registerEntityRender(EntityCoconutGrenade.class, new RenderSnowball(TCItemRegistry.coconutBomb));
        registerEntityRender(EntityTropiCreeper.class, new RenderTropiCreeper());
        registerEntityRender(EntityTropiSkeleton.class, new RenderTropiSkeleton(0.5f));
        registerEntityRender(SpiderAdult.class, new RenderTropiSpider());
        registerEntityRender(SpiderChild.class, new RenderTropiSpider());
        registerEntityRender(SpiderEgg.class, new RenderSpiderEgg(new ModelSpiderEgg(), 0.5f));
        registerEntityRender(VMonkey.class, new RenderVMonkey(new ModelVMonkey(), 0.5f));
        registerEntityRender(EntityKoaBase.class, new RenderKoaMan(new ModelKoaMan(), 0.7f));
        registerEntityRender(EntityTropicalFishHook.class, new RenderFishHook());
        registerEntityRender(EntityTCItemFrame.class, new RenderTCItemFrame());
        registerEntityRender(EntityTropicraftLeafballNew.class, new RenderSnowball(TCItemRegistry.leafBall));
        registerEntityRender(EntityTropicalFish.class, new RenderTropicalFish(new ModelFish(), 0.25f));
        registerEntityRender(EntityLostMask.class, new RenderLostMask());
        registerEntityRender(EntityMarlin.class, new RenderMarlin(new ModelMarlin(), 0.25f));
        registerEntityRender(EntitySnareTrap.class, new RenderSnareTrap());
        registerEntityRender(Failgull.class, new RenderFailgull(0.5f));
        registerEntityRender(
            EntityManOWar.class,
            new RenderManOWar(new ModelManOWar(32, 20, true), new ModelManOWar(0, 20, false), 0.35f));
        registerEntityRender(EntityStarfish.class, new RenderStarfish());
        registerEntityRender(EntityStarfishEgg.class, new RenderEchinodermEgg());
        registerEntityRender(EntitySeaUrchin.class, new RenderSeaUrchin());
        registerEntityRender(EntitySeaUrchinEgg.class, new RenderEchinodermEgg());
        registerEntityRender(EntityWallStarfish.class, new RenderWallStarfish());
    }

    public static void initTileEntityRenderers() {
        registerTileEntityRenderer(TileEntityBambooChest.class, new TileEntityBambooChestRenderer());
        registerTileEntityRenderer(TileEntityAirCompressor.class, new TileEntityAirCompressorRenderer());
        registerTileEntityRenderer(TileEntitySifter.class, new TileEntitySifterRenderer());
        registerTileEntityRenderer(TileEntityCurareBowl.class, new TileEntityCurareBowlRenderer());
        registerTileEntityRenderer(TileEntityKoaChest.class, new TileEntityKoaChestRenderer());
        registerTileEntityRenderer(TileEntityEIHMixer.class, new TileEntityEIHMixerRenderer());
        registerTileEntityRenderer(TileEntityPurchasePlate.class, new TileEntityPurchasePlateRenderer());
        registerTileEntityRenderer(TileEntityBambooMug.class, new TileEntityBambooMugRenderer());
    }

    private static void registerTileEntityRenderer(final Class<? extends TileEntity> tileEntityClass,
        final TileEntitySpecialRenderer specialRenderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
    }

    private static void registerEntityRender(final Class<? extends Entity> entityClass, final Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }
}
