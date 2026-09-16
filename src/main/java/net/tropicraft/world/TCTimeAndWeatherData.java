package net.tropicraft.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.Tropicraft;
import net.tropicraft.config.ConfigMisc;

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

    /** Ten minutes of dry weather, used whenever a countdown is missing from a clock file. */
    private static final int CLEAR_SKY_DELAY = 12000;

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

    public TCTimeAndWeatherData(final String name) {
        super(name);
    }

    private static final class Attachment {

        final WorldInfo info;
        final TCTimeAndWeatherData clock;

        Attachment(final WorldInfo info, final TCTimeAndWeatherData clock) {
            this.info = info;
            this.clock = clock;
        }
    }

    /**
     * The clock behind this WorldInfo, or {@code null} when it is an ordinary
     * {@link net.minecraft.world.storage.DerivedWorldInfo} belonging to some other dimension.
     */
    public static TCTimeAndWeatherData clockFor(final WorldInfo info) {
        final Attachment current = attachment;
        return current != null && current.info == info ? current.clock : null;
    }

    /** Whether the Tropics currently run on their own time and weather. */
    public static boolean isDetached(final WorldInfo info) {
        return clockFor(info) != null;
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
        Tropicraft.dbg(
            "[Tropicraft] The Tropics are on their own clock: time " + clock.worldTime
                + ", raining "
                + clock.raining
                + ", thundering "
                + clock.thundering
                + ".");

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
            Tropicraft.dbg(
                "[Tropicraft] The Tropics now run on their own clock and weather, starting at time " + data.worldTime
                    + " with clear skies (see the tropicsInitialTime config).");
        }
        return data;
    }

    /**
     * First run: a clear tropical morning. The countdowns are seeded with the same ranges vanilla
     * uses when the weather clears up - leaving them at zero would toggle rain on the very next
     * weather tick.
     */
    private void seed(final WorldServer world, final WorldInfo inherited) {
        this.worldTime = Math.max(0, ConfigMisc.tropicsInitialTime);
        this.totalTime = Math.max(0, inherited.getWorldTotalTime());
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
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setLong(KEY_WORLD_TIME, this.worldTime);
        nbt.setLong(KEY_TOTAL_TIME, this.totalTime);
        nbt.setBoolean(KEY_RAINING, this.raining);
        nbt.setInteger(KEY_RAIN_TIME, this.rainTime);
        nbt.setBoolean(KEY_THUNDERING, this.thundering);
        nbt.setInteger(KEY_THUNDER_TIME, this.thunderTime);
    }
}
