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
        ConfigMisc.coconutBombWhitelist = "";
        ConfigMisc.COCONUT_BOMB_WHITELIST = null;
        ConfigMisc.coconutBombWhitelistedUsers = new ArrayList<String>();
    }
}
