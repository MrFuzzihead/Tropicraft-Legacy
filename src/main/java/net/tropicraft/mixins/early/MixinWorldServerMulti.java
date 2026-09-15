package net.tropicraft.mixins.early;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.tropicraft.world.TCTimeAndWeatherData;
import net.tropicraft.world.WorldProviderTropicraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Detaches the Tropics from the overworld's time and weather (issue #35).
 * <p>
 * Every custom dimension is created as a WorldServerMulti, which hands itself a
 * {@link net.minecraft.world.storage.DerivedWorldInfo}: time and weather are read from the
 * overworld and every write to them is silently dropped. That is why the Tropics always matched the
 * overworld, and why sleeping there did nothing - the "skip to dawn" write in
 * {@code WorldServer.tick()} went nowhere.
 * <p>
 * {@link TCTimeAndWeatherData} is what actually holds the detached values and
 * {@link MixinDerivedWorldInfo} is what makes the dimension's WorldInfo read and write them. All
 * this mixin has to do is notice that the Tropics have come into being and hand that WorldInfo over
 * to them, which is done as the constructor finishes: by then {@code provider},
 * {@code perWorldStorage} (where the clock is persisted) and {@code worldInfo} all exist, and it is
 * still before the world ticks and before {@code WorldEvent.Load} is posted.
 * <p>
 * Deliberately shadow-free: the fields are read through a properly typed local, which the build's
 * remapper resolves correctly, whereas shadowing members inherited from World produces a refmap
 * entry that cannot be resolved.
 */
@Mixin(WorldServerMulti.class)
public abstract class MixinWorldServerMulti {

    @Inject(method = { "<init>" }, at = @At("RETURN"))
    private void tropicraft$adoptTimeAndWeather(final MinecraftServer server, final ISaveHandler saveHandler,
        final String levelName, final int dimension, final WorldSettings settings, final WorldServer parent,
        final Profiler profiler, final CallbackInfo ci) {
        final WorldServerMulti world = (WorldServerMulti) (Object) this;
        if (world.provider instanceof WorldProviderTropicraft) {
            TCTimeAndWeatherData.attach(world);
        }
    }
}
