package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.world.worldgen.WorldGenCoral;

public class BiomeGenTropicsOcean extends BiomeGenTropicraft {

    public BiomeGenTropicsOcean(final int biomeID) {
        super(biomeID);
    }

    public void decorate(final World world, final Random rand, final int x, final int z) {
        if (rand.nextInt(5) == 0) {
            // A reef has to reach past its own chunk or it reads as a 12-block patch, so the spread is 24 blocks and
            // the anchor is the chunk centre rather than a point inside it.
            new WorldGenCoral(24, 6).generate(world, rand, x + 8, 64, z + 8);
        }
        super.decorate(world, rand, x, z);
    }
}
