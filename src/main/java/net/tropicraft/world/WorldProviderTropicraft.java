package net.tropicraft.world;

import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;
import net.tropicraft.world.biomes.*;
import net.tropicraft.world.chunk.*;

public class WorldProviderTropicraft extends WorldProvider {

    public static final int MID_HEIGHT = 63;
    public static final int MAX_HEIGHT = 256;
    public static final int INTER_HEIGHT = 193;

    protected void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerTropicraft(this.worldObj.getSeed(), this.terrainType);
    }

    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderTropicraft(
            this.worldObj,
            this.worldObj.getSeed(),
            this.worldObj.getWorldInfo()
                .isMapFeaturesEnabled());
    }

    public String getWelcomeMessage() {
        return "Drifting in to the Tropics of " + this.worldObj.getWorldInfo()
            .getWorldName();
    }

    public String getDepartMessage() {
        return "Fading out of the Tropics of " + this.worldObj.getWorldInfo()
            .getWorldName();
    }

    public String getDimensionName() {
        return "Tropics";
    }

    public String getSaveFolder() {
        return "TROPICS";
    }
}
