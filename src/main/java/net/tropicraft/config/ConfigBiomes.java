package net.tropicraft.config;

import modconfig.IConfigCategory;

public class ConfigBiomes implements IConfigCategory {

    public static int tropicsOceanID;
    public static int tropicsID;
    public static int rainforestPlainsID;
    public static int rainforestHillsID;
    public static int rainforestMountainsID;
    public static int tropicsRiverID;
    public static int tropicsBeachID;
    public static int tropicsLakeID;
    public static int islandMountainsID;

    public String getConfigFileName() {
        return "Tropicraft_Biomes";
    }

    public String getCategory() {
        return "Tropicraft Biomes Config";
    }

    public void hookUpdatedValues() {}

    static {
        ConfigBiomes.tropicsOceanID = 60;
        ConfigBiomes.tropicsID = 61;
        ConfigBiomes.rainforestPlainsID = 62;
        ConfigBiomes.rainforestHillsID = 63;
        ConfigBiomes.rainforestMountainsID = 64;
        ConfigBiomes.tropicsRiverID = 65;
        ConfigBiomes.tropicsBeachID = 66;
        ConfigBiomes.tropicsLakeID = 67;
        ConfigBiomes.islandMountainsID = 68;
    }
}
