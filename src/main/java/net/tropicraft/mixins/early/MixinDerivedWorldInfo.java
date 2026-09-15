package net.tropicraft.mixins.early;

import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.world.TCTimeAndWeatherData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes the Tropics' {@link DerivedWorldInfo} read and write the island's own time and weather
 * instead of the overworld's (issue #35).
 * <p>
 * A DerivedWorldInfo delegates time and weather to the overworld and throws away everything written
 * to it, which is what glued the Tropics to the overworld's clock and sky. {@code World},
 * {@code WorldServer}, {@code WorldProvider} and every mod out there reach time and weather through
 * a WorldInfo though, so making this one WorldInfo answer differently is enough - vanilla's own
 * ticking, sleeping and weather code then keeps working per dimension unmodified, and it is the
 * reason sleeping in the Tropics finally skips the Tropics' night.
 * <p>
 * Each handler is a cancellable injection at the head of the method that only takes over for the one
 * WorldInfo registered in {@link TCTimeAndWeatherData} - every other dimension's DerivedWorldInfo
 * falls through to vanilla's own code. Nothing here shadows anything, and the checks are an identity
 * comparison against a single static field, which matters as these are called for every world every
 * tick.
 */
@Mixin(DerivedWorldInfo.class)
public abstract class MixinDerivedWorldInfo {

    private TCTimeAndWeatherData tropicraft$clock() {
        return TCTimeAndWeatherData.clockFor((WorldInfo) (Object) this);
    }

    @Inject(method = { "getWorldTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$getWorldTime(final CallbackInfoReturnable<Long> ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            ci.setReturnValue(clock.worldTime);
        }
    }

    @Inject(method = { "setWorldTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$setWorldTime(final long time, final CallbackInfo ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            if (clock.worldTime != time) {
                clock.worldTime = time;
                clock.markDirty();
            }
            ci.cancel();
        }
    }

    @Inject(method = { "getWorldTotalTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$getWorldTotalTime(final CallbackInfoReturnable<Long> ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            ci.setReturnValue(clock.totalTime);
        }
    }

    @Inject(method = { "incrementTotalWorldTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$incrementTotalWorldTime(final long time, final CallbackInfo ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            if (clock.totalTime != time) {
                clock.totalTime = time;
                clock.markDirty();
            }
            ci.cancel();
        }
    }

    @Inject(method = { "isRaining" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$isRaining(final CallbackInfoReturnable<Boolean> ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            ci.setReturnValue(clock.raining);
        }
    }

    @Inject(method = { "setRaining" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$setRaining(final boolean raining, final CallbackInfo ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            if (clock.raining != raining) {
                clock.raining = raining;
                clock.markDirty();
            }
            ci.cancel();
        }
    }

    @Inject(method = { "getRainTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$getRainTime(final CallbackInfoReturnable<Integer> ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            ci.setReturnValue(clock.rainTime);
        }
    }

    @Inject(method = { "setRainTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$setRainTime(final int rainTime, final CallbackInfo ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            if (clock.rainTime != rainTime) {
                clock.rainTime = rainTime;
                clock.markDirty();
            }
            ci.cancel();
        }
    }

    @Inject(method = { "isThundering" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$isThundering(final CallbackInfoReturnable<Boolean> ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            ci.setReturnValue(clock.thundering);
        }
    }

    @Inject(method = { "setThundering" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$setThundering(final boolean thundering, final CallbackInfo ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            if (clock.thundering != thundering) {
                clock.thundering = thundering;
                clock.markDirty();
            }
            ci.cancel();
        }
    }

    @Inject(method = { "getThunderTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$getThunderTime(final CallbackInfoReturnable<Integer> ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            ci.setReturnValue(clock.thunderTime);
        }
    }

    @Inject(method = { "setThunderTime" }, at = @At("HEAD"), cancellable = true)
    private void tropicraft$setThunderTime(final int thunderTime, final CallbackInfo ci) {
        final TCTimeAndWeatherData clock = tropicraft$clock();
        if (clock != null) {
            if (clock.thunderTime != thunderTime) {
                clock.thunderTime = thunderTime;
                clock.markDirty();
            }
            ci.cancel();
        }
    }
}
