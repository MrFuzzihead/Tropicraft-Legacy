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
            new WorldGenCoral().generate(world, rand, x + 6 + rand.nextInt(4), 64, z + 6 + rand.nextInt(4));
        }
        super.decorate(world, rand, x, z);
    }
}
