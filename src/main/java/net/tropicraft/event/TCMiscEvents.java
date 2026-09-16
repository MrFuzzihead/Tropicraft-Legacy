package net.tropicraft.event;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.tropicraft.Tropicraft;
import net.tropicraft.config.ConfigMisc;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.util.EffectHelper;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.TCTimeAndWeatherData;

import CoroUtil.forge.CoroAI;
import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.ExtendedRenderer;

public class TCMiscEvents {

    @SubscribeEvent
    public void worldLoad(final WorldEvent.Load event) {
        if (event.world.isRemote
            || ((WorldServer) event.world).provider.dimensionId != TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
            return;
        }
        if (WorldDirectorManager.instance()
            .getWorldDirector(CoroAI.modID, event.world) == null) {
            WorldDirectorManager.instance()
                .registerWorldDirector(new WorldDirector(), CoroAI.modID, event.world);
        }
        if (ConfigMisc.separateTimeAndWeather && !TCTimeAndWeatherData.isDetached(event.world.getWorldInfo())) {
            Tropicraft.dbg(
                "[Tropicraft] Could not give the Tropics their own time and weather - the WorldServerMulti mixin did not apply. Is a mixin loader such as UniMixins installed? The Tropics will keep mirroring the overworld.");
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void tickClient(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EffectHelper.tick();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
            for (int ii = 0; ii < ExtendedRenderer.rotEffRenderer.fxLayers.length; ++ii) {
                final List list = ExtendedRenderer.rotEffRenderer.fxLayers[ii];
                list.clear();
            }
        }
    }

    @SubscribeEvent
    public void tickServer(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EffectHelper.tick();
        }
        final MinecraftServer server = FMLCommonHandler.instance()
            .getMinecraftServerInstance();
        if (server == null || server.worldServers == null) {
            return;
        }
        // Sitting in a chair at sunset whisks the player away to the other dimension. That runs off
        // the time of the dimension the player is actually in, so now that every dimension can be on
        // its own clock it also keeps working from the Tropics and from other mod's dimensions.
        for (final WorldServer world : server.worldServers) {
            if (world == null) {
                continue;
            }
            for (int ii = 0; ii < world.playerEntities.size(); ++ii) {
                final Entity entity1 = (Entity) world.playerEntities.get(ii);
                if (entity1 instanceof EntityPlayerMP && ((EntityPlayerMP) entity1).isPotionActive(Potion.confusion)
                    && this.isSunset(world)
                    && entity1.ridingEntity instanceof EntityChair) {
                    entity1.ridingEntity = null;
                    TropicraftWorldUtils.teleportPlayer((EntityPlayerMP) entity1);
                }
            }
        }
    }

    private boolean isSunset(final World world) {
        final long timeDay = world.getWorldTime() % 24000L;
        return timeDay > 12200L && timeDay < 14000L;
    }
}
