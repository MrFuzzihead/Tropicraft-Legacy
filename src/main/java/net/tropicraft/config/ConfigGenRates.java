package net.tropicraft.config;

import modconfig.ConfigComment;
import modconfig.IConfigCategory;

public class ConfigGenRates implements IConfigCategory {

    public static final int EIH_CHANCE = 50;
    public static final int SHIPWRECK_CHANCE = 300;
    public static final int TALL_FLOWERS_CHANCE = 3;
    public static final int BAMBOO_CHANCE = 2;
    public static final int WATERFALL_AMOUNT = 25;
    public static final int TALL_GRASS_CHANCE = 4;
    public static final int CURVED_PALM_CHANCE = 3;
    public static final int LARGE_PALM_CHANCE = 3;
    public static final int NORMAL_PALM_CHANCE = 3;
    public static boolean genPalmsInOverworld;
    public static boolean genOverworldPalmsInBeachOnly;
    public static int palmChanceOfGenInOverworld;
    public static int palmPopulationFactorInOverworld;
    @ConfigComment({ "Spawns Eastern Island Head structures in the overworld (dimension 0).",
        "Requires genTropicraftInOverworld to be enabled as well." })
    public static boolean genTropicraftEIHInOverworld;
    @ConfigComment({
        "How rare overworld Eastern Island Head structures are: an attempt is made once every N chunks on average.",
        "Higher numbers are rarer, 0 disables attempts regardless of genTropicraftEIHInOverworld." })
    public static int eihChanceInOverworld;
    public static boolean genTropicraftFlowersInOverworld;
    public static boolean genTropicraftInOverworld;
    public static boolean genPineapplesInOverworld;
    public static boolean genBambooInOverworld;

    public String getConfigFileName() {
        return "Tropicraft_GenRates";
    }

    public String getCategory() {
        return "Tropicraft Gen Rates Config";
    }

    public void hookUpdatedValues() {}

    static {
        ConfigGenRates.genPalmsInOverworld = true;
        ConfigGenRates.genOverworldPalmsInBeachOnly = false;
        ConfigGenRates.palmChanceOfGenInOverworld = -1;
        ConfigGenRates.palmPopulationFactorInOverworld = 3;
        ConfigGenRates.genTropicraftEIHInOverworld = true;
        ConfigGenRates.eihChanceInOverworld = 100;
        ConfigGenRates.genTropicraftFlowersInOverworld = true;
        ConfigGenRates.genTropicraftInOverworld = true;
        ConfigGenRates.genPineapplesInOverworld = true;
        ConfigGenRates.genBambooInOverworld = true;
    }
}
