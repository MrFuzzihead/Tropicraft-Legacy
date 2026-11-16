package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;

/**
 * Scatters coral onto the sea floor around a point.
 * <p>
 * The radius is a parameter because a reef that only reaches {@code ±8} blocks from a point inside one chunk is pinned
 * to that chunk: every reef came out as a single 12-block cluster per chunk. The ocean biome now asks for a wider
 * spread, so a reef crosses chunk borders and reads as a reef.
 * </p>
 */
public class WorldGenCoral extends WorldGenerator {

    private final int radius;
    private final int attempts;

    public WorldGenCoral() {
        this(8, 12);
    }

    public WorldGenCoral(final int radius, final int attempts) {
        this.radius = radius;
        this.attempts = attempts;
    }

    public boolean generate(final World world, final Random random, final int i, final int j, final int k) {
        for (int attempt = 0; attempt < this.attempts; ++attempt) {
            int x = i + random.nextInt(this.radius * 2 + 1) - this.radius;
            int z = k + random.nextInt(this.radius * 2 + 1) - this.radius;
            int y;
            for (y = j; (world.isAirBlock(x, y, z) || world.getBlock(x, y - 1, z)
                .getMaterial() == Material.water) && y > 0; --y) {}
            if (TCBlockRegistry.coral.canBlockStay(world, x, y, z)) {
                world.setBlock(x, y, z, TCBlockRegistry.coral, random.nextInt(6), 3);
            }
        }
        return true;
    }
}
