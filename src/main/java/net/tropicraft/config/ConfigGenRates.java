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
    @ConfigComment({
        "Restrict overworld Eastern Island Head structures to warm biomes (jungles, swampland, beaches, savanna).",
        "Defaults to false so heads can appear on any grassy overworld surface." })
    public static boolean genOverworldEIHInWarmBiomesOnly;
    @ConfigComment({
        "How rare Eastern Island Head structures are in the Tropics: an attempt is made once every N decorate calls",
        "on average. Higher numbers are rarer, 0 disables them." })
    public static int eihChanceInTropics;
    public static boolean genTropicraftFlowersInOverworld;
    @ConfigComment({
        "How many tropical flower features to try per overworld chunk. One feature places at most 35 flowers inside a",
        "15x15 patch: 1 is about 1% of a grass chunk's surface and 3 about 2%. The ten per chunk the mod shipped before",
        "6.2.5 was ten times that, which is why the overworld looked like a meadow once the feature started working.",
        "0 disables the flowers without touching genTropicraftFlowersInOverworld." })
    public static int flowersPerChunkInOverworld;
    @ConfigComment({
        "Restrict overworld tropical flowers to warm biomes - hot, wet or jungle per Forge's biome dictionary, i.e.",
        "jungle, jungle edge, savanna and swamp, plus any modded biome registered as tropical.",
        "Set to false for the old behaviour, where every grassy overworld biome was fair game." })
    public static boolean genOverworldFlowersInWarmBiomesOnly;
    @ConfigComment({ "Whether volcanoes generate in the Tropics.",
        "Turning this off only affects chunks generated afterwards; existing volcanoes stay in the world." })
    public static boolean genVolcanoes;

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
        ConfigGenRates.eihChanceInOverworld = 500;
        ConfigGenRates.genOverworldEIHInWarmBiomesOnly = false;
        ConfigGenRates.eihChanceInTropics = EIH_CHANCE;
        ConfigGenRates.genTropicraftFlowersInOverworld = true;
        ConfigGenRates.flowersPerChunkInOverworld = 1;
        ConfigGenRates.genOverworldFlowersInWarmBiomesOnly = true;
        ConfigGenRates.genVolcanoes = true;
        ConfigGenRates.genTropicraftInOverworld = true;
        ConfigGenRates.genPineapplesInOverworld = true;
        ConfigGenRates.genBambooInOverworld = true;
    }
}
