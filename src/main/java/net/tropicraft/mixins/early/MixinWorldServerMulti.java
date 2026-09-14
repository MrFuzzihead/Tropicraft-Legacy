package net.tropicraft.mixins.early;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.tropicraft.world.WorldInfoTropicraft;
import net.tropicraft.world.WorldProviderTropicraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Gives the Tropics its own {@link net.minecraft.world.storage.WorldInfo} so that its time of day and
 * its weather are no longer the overworld's (issue #35).
 * <p>
 * Every custom dimension is created as a WorldServerMulti, which hands itself a DerivedWorldInfo:
 * time and weather are read from the overworld and every write to them is silently dropped. That is
 * why the Tropics always matched the overworld, and why sleeping there did nothing - the "skip to
 * dawn" write in {@code WorldServer.tick()} went nowhere.
 * <p>
 * Swapping the WorldInfo as the constructor finishes is the earliest point that works: the superclass
 * constructor has already set {@code provider} and {@code perWorldStorage} (which is where the
 * Tropics' clock is persisted), but no world tick has run and no {@code WorldEvent.Load} has been
 * posted yet. Mixing in also means the inherited protected {@code worldInfo} field can simply be
 * written - no reflection and no access transformer needed.
 */
@Mixin(WorldServerMulti.class)
public abstract class MixinWorldServerMulti extends WorldServer {

    protected MixinWorldServerMulti(final MinecraftServer server, final ISaveHandler saveHandler,
        final String levelName, final int dimension, final WorldSettings settings, final Profiler profiler) {
        super(server, saveHandler, levelName, dimension, settings, profiler);
    }

    @Inject(method = { "<init>" }, at = @At("RETURN"))
    private void tropicraft$ownTimeAndWeather(final MinecraftServer server, final ISaveHandler saveHandler,
        final String levelName, final int dimension, final WorldSettings settings, final WorldServer parent,
        final Profiler profiler, final CallbackInfo ci) {
        if (!(this.provider instanceof WorldProviderTropicraft)) {
            return;
        }
        this.worldInfo = WorldInfoTropicraft.create(this, this.worldInfo);
        WorldInfoTropicraft.refreshSkyAndWeather(this);
    }
}
