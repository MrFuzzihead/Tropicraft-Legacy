package net.tropicraft.entity.underdasea;

import java.util.ArrayList;
import java.util.List;

public enum StarfishType {

    RED("starfishRed", "Red Starfish",
        new String[] { "starfish_red_0.png", "starfish_red_1.png", "starfish_red_2.png" },
        new float[] { 0.03125f, 0.015625f, 0.015625f }),
    ROYAL("starfishRoyal", "Royal Starfish", new String[] { "starfish_royal_0.png", "starfish_royal_1.png" });

    private String unlocalizedName;
    private String displayName;
    private List<String> texturePaths;
    private float[] layerHeights;

    private StarfishType(final String unlocalizedName, final String displayName, final String[] textures,
        final float[] heights) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;
        if (heights == null) {
            this.layerHeights = new float[textures.length];
            for (int i = 0; i < textures.length; ++i) {
                this.layerHeights[i] = 0.03125f;
            }
        } else {
            this.layerHeights = heights;
        }
        this.texturePaths = new ArrayList<String>(textures.length);
        for (final String texture : textures) {
            this.texturePaths.add("tropicraft:textures/entity/" + texture);
        }
    }

    private StarfishType(final String unlocalizedName, final String displayName, final String[] textures) {
        this(unlocalizedName, displayName, textures, null);
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getTexturePaths() {
        return this.texturePaths;
    }

    public float[] getLayerHeights() {
        return this.layerHeights;
    }

    public int getLayerCount() {
        return this.texturePaths.size();
    }
}
