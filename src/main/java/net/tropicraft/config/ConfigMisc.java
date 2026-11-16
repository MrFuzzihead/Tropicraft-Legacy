package net.tropicraft.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modconfig.ConfigComment;
import modconfig.IConfigCategory;

public class ConfigMisc implements IConfigCategory {

    @ConfigComment({ "When true, only the players named in coconutBombWhitelist may throw coconut bombs.",
        "When false (the default) everybody can, and coconutBombWhitelist is ignored.",
        "The whitelist accepts names separated by commas or spaces." })
    public static boolean coconutBombWhitelistEnabled;

    @ConfigComment({ "Names allowed to throw coconut bombs while coconutBombWhitelistEnabled is true." })
    public static String coconutBombWhitelist;
    public static String[] COCONUT_BOMB_WHITELIST;
    public static List<String> coconutBombWhitelistedUsers;

    @ConfigComment({
        "When true, the Tropics keep their own time of day and their own weather instead of mirroring the overworld.",
        "Players can then sleep through the Tropics' night on their own, and rain in the Tropics no longer starts and",
        "stops with the overworld's.",
        "UniMixins provides the mixins this needs and is declared as a dependency; if the swap could not be made for some",
        "reason a warning is printed to the log at world load and the Tropics keep mirroring the overworld.",
        "Set to false to restore the old behaviour, where the Tropics always matched the overworld.",
        "Changing this takes effect the next time the Tropics dimension loads." })
    public static boolean separateTimeAndWeather;

    @ConfigComment({
        "The dimension id the Tropics are registered under. Default -127, which is what existing worlds were saved with.",
        "Change it only before a world is created: the dimension id is part of every saved chunk's location, and a world",
        "saved with the old id keeps its chunks there - they will not appear under a new id.",
        "Must not collide with another mod's dimension; a failed registration is reported in the log at startup." })
    public static int tropicsDimensionId;

    @ConfigComment({ "Time of day the Tropics start at when their clock first starts.",
        "The island's clock is pinned to this time of day on day 1, in clear weather, the first time a player actually",
        "travels there. From then on the island keeps its own time, so later visits land at whatever hour it has reached.",
        "0 = sunrise, 1000 = morning, 6000 = noon, 12000 = sunset, 13000 = night, 18000 = midnight." })
    public static int tropicsInitialTime;

    @ConfigComment({ "When every player in the Tropics wakes up, vanilla clears the rain and thunder there.",
        "True keeps that vanilla behaviour, false lets a tropical downpour survive the vacationers.",
        "Only means anything while separateTimeAndWeather is on; without it the Tropics share the overworld's weather." })
    public static boolean sleepResetsWeather;

    public String getConfigFileName() {
        return "Tropicraft_Misc";
    }

    public String getCategory() {
        return "Tropicraft Misc Config";
    }

    public void hookUpdatedValues() {
        // An empty string used to split into [""], i.e. one nameless entry; accept both "a, b" and "a b" instead.
        final String raw = ConfigMisc.coconutBombWhitelist == null ? "" : ConfigMisc.coconutBombWhitelist.trim();
        ConfigMisc.COCONUT_BOMB_WHITELIST = raw.isEmpty() ? new String[0] : raw.split("[,\s]+");
        ConfigMisc.coconutBombWhitelistedUsers = Arrays.asList(ConfigMisc.COCONUT_BOMB_WHITELIST);
    }

    static {
        ConfigMisc.separateTimeAndWeather = true;
        ConfigMisc.tropicsDimensionId = -127;
        ConfigMisc.tropicsInitialTime = 1000;
        ConfigMisc.sleepResetsWeather = true;
        ConfigMisc.coconutBombWhitelistEnabled = false;
        ConfigMisc.coconutBombWhitelist = "";
        ConfigMisc.COCONUT_BOMB_WHITELIST = null;
        ConfigMisc.coconutBombWhitelistedUsers = new ArrayList<String>();
    }
}
