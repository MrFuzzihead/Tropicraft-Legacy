package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.tropicraft.world.location.TownKoaVillageGenHelper;
import net.tropicraft.world.worldgen.WorldGenTropicsTreasure;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

    private static final int TREASURE_CHANCE = 25;
    private static final int VILLAGE_CHANCE = 10;

    public BiomeGenTropicsBeach(final int biomeID) {
        super(biomeID);
    }

    public void decorate(final World world, final Random rand, final int x, final int z) {
        if (rand.nextInt(25) == 0) {
            final int i = this.randCoord(rand, x, 16);
            final int k = this.randCoord(rand, z, 16);
            new WorldGenTropicsTreasure(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }
        if (rand.nextInt(10) == 0) {
            boolean success = false;
            int j;
            int l;
            for (int ii = 0; ii < 3 && !success; success = TownKoaVillageGenHelper
                .hookTryGenVillage(new ChunkCoordinates(j, this.getTerrainHeightAt(world, j, l), l), world), ++ii) {
                j = this.randCoord(rand, x, 16);
                l = this.randCoord(rand, z, 16);
                int y = world.getTopSolidOrLiquidBlock(j, l);
                if (y < 63) {
                    y = 64;
                }
            }
        }
        super.decorate(world, rand, x, z);
    }
}
