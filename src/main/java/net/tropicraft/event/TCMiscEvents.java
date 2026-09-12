package net.tropicraft.event;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.util.EffectHelper;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.chunk.ChunkProviderTropicraft;

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
    public void serverStopping(final cpw.mods.fml.common.event.FMLServerStoppingEvent event) {
        // Fires before the final world save, so pending decorations are applied before chunks hit disk.
        ChunkProviderTropicraft.flushAllProvidersForSave();
    }

    @SubscribeEvent
    public void worldLoad(final WorldEvent.Load event) {
        if (!event.world.isRemote && ((WorldServer) event.world).provider.dimensionId == -127
            && WorldDirectorManager.instance()
                .getWorldDirector(CoroAI.modID, event.world) == null) {
            WorldDirectorManager.instance()
                .registerWorldDirector(new WorldDirector(), CoroAI.modID, event.world);
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
        final World world = (World) FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .worldServerForDimension(0);
        if (world != null && world instanceof WorldServer) {
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
