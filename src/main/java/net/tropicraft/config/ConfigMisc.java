package net.tropicraft.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modconfig.ConfigComment;
import modconfig.IConfigCategory;

public class ConfigMisc implements IConfigCategory {

    @ConfigComment({ "List of users who can use coconut bombs." })
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
        ConfigMisc.COCONUT_BOMB_WHITELIST = (ConfigMisc.coconutBombWhitelist.contains(",")
            ? ConfigMisc.coconutBombWhitelist.replace(" ", "")
                .split(",")
            : ConfigMisc.coconutBombWhitelist.split(" "));
        ConfigMisc.coconutBombWhitelistedUsers = Arrays.asList(ConfigMisc.COCONUT_BOMB_WHITELIST);
    }

    static {
        ConfigMisc.separateTimeAndWeather = true;
        ConfigMisc.tropicsInitialTime = 1000;
        ConfigMisc.sleepResetsWeather = true;
        ConfigMisc.coconutBombWhitelist = "";
        ConfigMisc.COCONUT_BOMB_WHITELIST = null;
        ConfigMisc.coconutBombWhitelistedUsers = new ArrayList<String>();
    }
}
