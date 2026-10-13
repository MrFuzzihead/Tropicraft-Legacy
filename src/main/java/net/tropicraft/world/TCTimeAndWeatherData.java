package net.tropicraft.world;

import java.lang.ref.WeakReference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.config.ConfigMisc;

import cpw.mods.fml.common.FMLLog;

/**
 * The Tropics' own clock and weather, and the one thing that knows which
 * {@link net.minecraft.world.storage.WorldInfo} they belong to (issue #35).
 * <p>
 * A custom dimension is a {@link net.minecraft.world.WorldServerMulti}, and a WorldServerMulti gets
 * a {@link net.minecraft.world.storage.DerivedWorldInfo} that reads time and weather straight out of
 * the overworld's WorldInfo while making every setter a no-op. That is why the Tropics always matched
 * the overworld. Vanilla never saves a WorldServerMulti's WorldInfo anywhere, so this
 * {@link WorldSavedData} is what persists the detached state - it lands in the dimension's own folder
 * ({@code TROPICS/data/tropicraft_clock.dat}) because it lives in {@code World.perWorldStorage}.
 * <p>
 * {@link net.tropicraft.mixins.early.MixinDerivedWorldInfo} asks {@link #clockFor(WorldInfo)} whether
 * the WorldInfo it is being called on is the one registered here, and reads and writes this object
 * instead of the overworld when it is. Registering the WorldInfo rather than replacing it means no
 * field of Minecraft has to be written, and everything that is not time or weather - seed, spawn
 * point, difficulty, game rules, the NBT that goes to the root level.dat - keeps delegating to the
 * overworld exactly as vanilla intends.
 */
public class TCTimeAndWeatherData extends WorldSavedData {

    /** Name of the file written to {@code <world>/TROPICS/data/<name>.dat}. */
    public static final String DATA_NAME = "tropicraft_clock";

    private static final String KEY_WORLD_TIME = "WorldTime";
    private static final String KEY_TOTAL_TIME = "TotalWorldTime";
    private static final String KEY_RAINING = "Raining";
    private static final String KEY_RAIN_TIME = "RainTime";
    private static final String KEY_THUNDERING = "Thundering";
    private static final String KEY_THUNDER_TIME = "ThunderTime";
    private static final String KEY_VISITED = "Visited";

    /** Ten minutes of dry weather, used whenever a countdown is missing from a clock file. */
    private static final int CLEAR_SKY_DELAY = 12000;

    /**
     * A time of day the overworld will not be reporting, used to find out whether the mixin that makes
     * this clock answer is actually in place.
     */
    private static final long PROBE_TIME = 1234567890L;

    /**
     * The registered WorldInfo and the clock behind it. There is one Tropics per server, so one
     * immutable pair is all that is needed, and it is published in a single write.
     */
    private static volatile Attachment attachment;

    /** Ticks of day, 0 - 23999. Same meaning as {@link WorldInfo#getWorldTime()}. */
    public long worldTime;
    /** Ticks since the Tropics were created. Same meaning as {@link WorldInfo#getWorldTotalTime()}. */
    public long totalTime;
    public boolean raining;
    /** Ticks until the rain toggles, mirrors vanilla's countdown. */
    public int rainTime;
    public boolean thundering;
    /** Ticks until thunder toggles, mirrors vanilla's countdown. */
    public int thunderTime;
    /**
     * Whether anybody has ever actually been to the island. Until they have, the clock is only
     * running because the dimension was created with the world - see
     * {@link #startOnFirstVisit(WorldServer)}.
     */
    public boolean visited;

    public TCTimeAndWeatherData(final String name) {
        super(name);
    }

    private static final class Attachment {

        /**
         * Held weakly: as long as the dimension that owns this WorldInfo is loaded, the world holds
         * it too, and once the world is gone there is nothing left to answer for.
         */
        private final WeakReference<WorldInfo> info;
        private final TCTimeAndWeatherData clock;

        Attachment(final WorldInfo info, final TCTimeAndWeatherData clock) {
            this.info = new WeakReference<WorldInfo>(info);
            this.clock = clock;
        }

        boolean belongsTo(final WorldInfo candidate) {
            return this.info.get() == candidate;
        }
    }

    /**
     * The clock behind this WorldInfo, or {@code null} when it is an ordinary
     * {@link net.minecraft.world.storage.DerivedWorldInfo} belonging to some other dimension.
     */
    public static TCTimeAndWeatherData clockFor(final WorldInfo info) {
        final Attachment current = attachment;
        return current != null && current.belongsTo(info) ? current.clock : null;
    }

    /** Whether the Tropics currently run on their own time and weather. */
    public static boolean isDetached(final WorldInfo info) {
        return clockFor(info) != null;
    }

    /**
     * Whether this WorldInfo really is answering from the clock behind it, rather than from the
     * overworld.
     * <p>
     * Registering the WorldInfo is something this class does by itself, so on its own it cannot tell
     * whether {@link net.tropicraft.mixins.early.MixinDerivedWorldInfo} is doing its half. Asking the
     * WorldInfo for a time of day that the overworld is not reporting does tell: for the length of one
     * call the registration is a probe clock, so an answer of {@link #PROBE_TIME} means the injection
     * is in place and anything else means the island is still reading the overworld.
     */
    public static boolean isAnswering(final WorldInfo info) {
        final Attachment current = attachment;
        final TCTimeAndWeatherData probe = new TCTimeAndWeatherData(DATA_NAME);
        probe.worldTime = PROBE_TIME;
        attachment = new Attachment(info, probe);
        try {
            return info.getWorldTime() == PROBE_TIME;
        } finally {
            attachment = current;
        }
    }

    /**
     * Hands the Tropics' WorldInfo over to the Tropics' own clock and weather, and re-derives the
     * sky brightness and the rain strengths from them - up to this point the world was still reading
     * the overworld, whose weather and time have nothing to do with the island's.
     * <p>
     * Does nothing when {@code separateTimeAndWeather} is off, so switching it off leaves the vanilla
     * DerivedWorldInfo in place.
     */
    public static void attach(final WorldServer world) {
        final WorldInfo info = world.getWorldInfo();
        if (!ConfigMisc.separateTimeAndWeather || isDetached(info)) {
            return;
        }
        final TCTimeAndWeatherData clock = loadOrCreate(world, info);
        attachment = new Attachment(info, clock);
        if (!isAnswering(info)) {
            FMLLog.warning(
                "[Tropicraft] The Tropics have been given their own clock and weather, but DerivedWorldInfo is not answering from it - the mixin did not apply. The Tropics will keep mirroring the overworld until this is sorted out.");
            return;
        }
        FMLLog.info(
            "[Tropicraft] The Tropics are on their own clock: time %d, raining %s, thundering %s.",
            clock.worldTime,
            clock.raining,
            clock.thundering);
        applyToWorld(world, clock);
    }

    /**
     * Starts the island's clock the first time somebody is actually standing on it.
     * <p>
     * This exists because of when the dimension comes into being: Forge creates every statically
     * registered dimension as the world loads, not on first use, so the Tropics are built along with
     * the overworld and their clock starts ticking there and then. Ten minutes of play in the
     * overworld is enough for the island to reach nightfall, so without this a first visit would land
     * at whatever hour the island happened to have reached - which is not what
     * {@code tropicsInitialTime} promises. The first visit therefore restarts the clock on day 1 at
     * that time of day, in clear weather, and marks the island as visited; from then on it keeps its
     * own time, running on while nobody is there, and later visits land wherever it has got to.
     * <p>
     * Cheap enough to call every tick for every world: it does nothing at all unless the world is the
     * Tropics and somebody has just arrived there for the first time.
     */
    public static void startOnFirstVisit(final WorldServer world) {
        final WorldInfo info = world.getWorldInfo();
        final TCTimeAndWeatherData clock = clockFor(info);
        if (clock == null || clock.visited || world.playerEntities.isEmpty()) {
            return;
        }
        if (!isAnswering(info)) {
            // Nothing to restart: the island is still reading the overworld, and that was already
            // reported when the dimension was set up.
            clock.visited = true;
            clock.markDirty();
            return;
        }
        clock.visited = true;
        clock.seedClockAndWeather(world);
        clock.markDirty();
        FMLLog.info(
            "[Tropicraft] The first visitor has reached the Tropics, so their clock starts at time %d on day %d.",
            clock.worldTime,
            clock.worldTime / 24000L + 1);
        applyToWorld(world, clock);
    }

    /**
     * Re-derives the sky brightness and the rain and thunder strengths from the given clock, which is
     * how the world catches up after the clock has been created, started or moved.
     */
    private static void applyToWorld(final WorldServer world, final TCTimeAndWeatherData clock) {
        world.calculateInitialSkylight();
        world.rainingStrength = clock.raining ? 1.0f : 0.0f;
        world.prevRainingStrength = world.rainingStrength;
        world.thunderingStrength = clock.thundering ? 1.0f : 0.0f;
        world.prevThunderingStrength = world.thunderingStrength;
    }

    /**
     * Reads the saved Tropics clock, or creates the first one for this world.
     *
     * @param world     the Tropics world server
     * @param inherited the WorldInfo the Tropics used before it got its own, used to seed the total
     *                  time on first run
     */
    public static TCTimeAndWeatherData loadOrCreate(final WorldServer world, final WorldInfo inherited) {
        final MapStorage storage = world.perWorldStorage;
        TCTimeAndWeatherData data = (TCTimeAndWeatherData) storage.loadData(TCTimeAndWeatherData.class, DATA_NAME);
        if (data == null) {
            data = new TCTimeAndWeatherData(DATA_NAME);
            data.seed(world, inherited);
            storage.setData(DATA_NAME, data);
            data.markDirty();
            FMLLog.info(
                "[Tropicraft] The Tropics now run on their own clock and weather, starting at time %d with clear skies (see the tropicsInitialTime config).",
                data.worldTime);
        }
        return data;
    }

    /**
     * First run: a clear tropical morning on day 1. The countdowns are seeded with the same ranges
     * vanilla uses when the weather clears up - leaving them at zero would toggle rain on the very
     * next weather tick.
     * <p>
     * The clock starts fresh even in a world that has been played for a long time. That is deliberate:
     * the island is meant to begin on a clear tropical morning rather than at whatever hour the
     * overworld happened to be at, and for a world that has just been detached the first visitor gets
     * exactly that (see {@link #startOnFirstVisit(WorldServer)}). The total time is taken from the
     * overworld instead, so anything that measures how long this dimension has been running keeps its
     * meaning.
     */
    private void seed(final WorldServer world, final WorldInfo inherited) {
        this.totalTime = Math.max(0, inherited.getWorldTotalTime());
        this.seedClockAndWeather(world);
    }

    /** The part of the first run that a first visit can repeat: a morning on day 1, in clear weather. */
    private void seedClockAndWeather(final WorldServer world) {
        this.worldTime = Math.max(0, ConfigMisc.tropicsInitialTime);
        this.raining = false;
        this.rainTime = world.rand.nextInt(168000) + CLEAR_SKY_DELAY;
        this.thundering = false;
        this.thunderTime = world.rand.nextInt(168000) + CLEAR_SKY_DELAY;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.worldTime = nbt.getLong(KEY_WORLD_TIME);
        this.totalTime = nbt.getLong(KEY_TOTAL_TIME);
        this.raining = nbt.getBoolean(KEY_RAINING);
        this.thundering = nbt.getBoolean(KEY_THUNDERING);
        // A countdown read as zero from a file that never had one would bring the rain on the very
        // next weather tick, so a missing one counts as clear skies instead.
        this.rainTime = nbt.hasKey(KEY_RAIN_TIME) ? nbt.getInteger(KEY_RAIN_TIME) : CLEAR_SKY_DELAY;
        this.thunderTime = nbt.hasKey(KEY_THUNDER_TIME) ? nbt.getInteger(KEY_THUNDER_TIME) : CLEAR_SKY_DELAY;
        // A clock file written before the island had visitors, or one from a world that has never
        // been travelled to since, starts its clock again on the next arrival.
        this.visited = nbt.getBoolean(KEY_VISITED);
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setLong(KEY_WORLD_TIME, this.worldTime);
        nbt.setLong(KEY_TOTAL_TIME, this.totalTime);
        nbt.setBoolean(KEY_RAINING, this.raining);
        nbt.setInteger(KEY_RAIN_TIME, this.rainTime);
        nbt.setBoolean(KEY_THUNDERING, this.thundering);
        nbt.setInteger(KEY_THUNDER_TIME, this.thunderTime);
        nbt.setBoolean(KEY_VISITED, this.visited);
    }
}
