package net.tropicraft.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    // Issue #35 - the Tropics keep their own time and weather instead of the overworld's.
    // MixinWorldServerMulti notices the dimension, MixinDerivedWorldInfo is where the clock lives.
    TROPICS_SEPARATE_TIME_AND_WEATHER(new MixinBuilder().setPhase(Phase.EARLY)
        .addCommonMixins("MixinWorldServerMulti", "MixinDerivedWorldInfo"));

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
