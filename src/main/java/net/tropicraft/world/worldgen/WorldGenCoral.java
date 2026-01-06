package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.tropicraft.registry.*;

public class WorldGenCoral extends WorldGenerator {

    public boolean generate(final World world, final Random random, final int i, final int j, final int k) {
        for (int i2 = 0; i2 < 12; ++i2) {
            int x;
            int z;
            int y;
            for (x = i + random.nextInt(8) - random.nextInt(8), z = k + random.nextInt(8)
                - random.nextInt(8), y = j; (world.isAirBlock(x, y, z) || world.getBlock(x, y - 1, z)
                    .getMaterial() == Material.water) && y > 0; --y) {}
            if (TCBlockRegistry.coral.canBlockStay(world, x, y, z)) {
                world.setBlock(x, y, z, (Block) TCBlockRegistry.coral, random.nextInt(6), 3);
            }
        }
        return true;
    }
}
