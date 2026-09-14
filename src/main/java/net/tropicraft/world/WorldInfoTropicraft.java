package net.tropicraft.world;

import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.config.ConfigMisc;

/**
 * The WorldInfo the Tropics dimension runs on (issue #35).
 * <p>
 * Vanilla gives every custom dimension a {@link DerivedWorldInfo}: time and weather are read from
 * the overworld and every setter for them is empty, so the Tropics could neither keep their own
 * time nor their own weather, and sleeping there did nothing because
 * {@code WorldServer.tick()}'s "skip to dawn" write went nowhere.
 * <p>
 * This subclass keeps that delegation for everything it should share with the overworld (seed,
 * spawn point, game mode, difficulty, game rules, NBT, ...) and owns only the clock and the weather,
 * backed by {@link TCTimeAndWeatherData}. Because the swap happens on the WorldInfo itself - the
 * object every time and weather read in the game funnels through - vanilla's own ticking, sleeping
 * and weather code starts working per dimension without patching any of it, and mods that ask
 * {@code world.getWorldTime()}, {@code world.isDaytime()} or {@code world.getRainStrength()} get
 * the Tropics' answers instead of the overworld's.
 */
public class WorldInfoTropicraft extends DerivedWorldInfo {

    private final TCTimeAndWeatherData clock;

    public WorldInfoTropicraft(final WorldInfo overworld, final TCTimeAndWeatherData clock) {
        super(overworld);
        this.clock = clock;
    }

    /**
     * Builds the Tropics' own WorldInfo on top of the one the dimension inherited. Returns
     * {@code inherited} untouched when the feature is switched off, so disabling it in the config
     * restores the old behaviour exactly.
     * <p>
     * Call {@link #refreshSkyAndWeather(WorldServer)} right after assigning the result, the world
     * still carries the sky and rain strengths the {@code World} constructor derived from the
     * overworld until then.
     */
    public static WorldInfo create(final WorldServer world, final WorldInfo inherited) {
        if (!ConfigMisc.separateTimeAndWeather || inherited instanceof WorldInfoTropicraft) {
            return inherited;
        }
        return new WorldInfoTropicraft(inherited, TCTimeAndWeatherData.loadOrCreate(world, inherited));
    }

    /**
     * Re-derives the sky brightness and the rain/thunder strengths from the Tropics' own clock and
     * weather now that it is in place, instead of leaving the values the {@code World} constructor
     * read off the overworld.
     */
    public static void refreshSkyAndWeather(final WorldServer world) {
        if (!(world.getWorldInfo() instanceof WorldInfoTropicraft)) {
            return;
        }
        final WorldInfo info = world.getWorldInfo();

        world.calculateInitialSkylight();
        world.rainingStrength = info.isRaining() ? 1.0f : 0.0f;
        world.prevRainingStrength = world.rainingStrength;
        world.thunderingStrength = info.isThundering() ? 1.0f : 0.0f;
        world.prevThunderingStrength = world.thunderingStrength;
    }

    /* ==================================================================== */
    /* State the Tropics owns. Everything else stays delegated to the overworld. */
    /* ==================================================================== */

    @Override
    public long getWorldTime() {
        return this.clock.worldTime;
    }

    @Override
    public void setWorldTime(final long time) {
        if (this.clock.worldTime != time) {
            this.clock.worldTime = time;
            this.clock.markDirty();
        }
    }

    @Override
    public long getWorldTotalTime() {
        return this.clock.totalTime;
    }

    @Override
    public void incrementTotalWorldTime(final long time) {
        if (this.clock.totalTime != time) {
            this.clock.totalTime = time;
            this.clock.markDirty();
        }
    }

    @Override
    public boolean isRaining() {
        return this.clock.raining;
    }

    @Override
    public void setRaining(final boolean raining) {
        if (this.clock.raining != raining) {
            this.clock.raining = raining;
            this.clock.markDirty();
        }
    }

    @Override
    public int getRainTime() {
        return this.clock.rainTime;
    }

    @Override
    public void setRainTime(final int rainTime) {
        if (this.clock.rainTime != rainTime) {
            this.clock.rainTime = rainTime;
            this.clock.markDirty();
        }
    }

    @Override
    public boolean isThundering() {
        return this.clock.thundering;
    }

    @Override
    public void setThundering(final boolean thundering) {
        if (this.clock.thundering != thundering) {
            this.clock.thundering = thundering;
            this.clock.markDirty();
        }
    }

    @Override
    public int getThunderTime() {
        return this.clock.thunderTime;
    }

    @Override
    public void setThunderTime(final int thunderTime) {
        if (this.clock.thunderTime != thunderTime) {
            this.clock.thunderTime = thunderTime;
            this.clock.markDirty();
        }
    }

    /* ==================================================================== */
    /* Everything below stays exactly as DerivedWorldInfo has it on purpose. */
    /* ==================================================================== */

    /*
     * getNBTTagCompound() and cloneNBTCompound() already hand out the overworld's own copy, which is
     * what keeps our values out of the root level.dat: a WorldServerMulti is never handed to a save
     * handler (its saveLevel() only writes perWorldStorage), so the one place our clock lives is the
     * dimension's own data folder. The same goes for the additional properties, which vanilla keeps
     * per WorldInfo object - leaving them alone means a mod that stores something there for the
     * Tropics still behaves the way it did before this class existed.
     */
}
