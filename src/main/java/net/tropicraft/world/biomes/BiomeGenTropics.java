package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.worldgen.WorldGenTropicraftFlowers;
import net.tropicraft.world.worldgen.WorldGenTropicraftFruitTrees;
import net.tropicraft.world.worldgen.WorldGenTropicsTreasure;

public class BiomeGenTropics extends BiomeGenTropicraft {

    private static final int FRUIT_TREE_CHANCE = 2;
    private static final int TREASURE_CHANCE = 25;

    public BiomeGenTropics(final int biomeID) {
        super(biomeID);
    }

    public void decorate(final World world, final Random rand, final int x, final int z) {
        if (BiomeGenTropics.DISABLEDECORATION) {
            System.out.println("decoration disabled via BiomeGenTropics.DISABLEDECORATION, " + this);
            return;
        }

        super.decorate(world, rand, x, z);

        int i = this.randCoord(rand, x, 16);
        int k = this.randCoord(rand, z, 16);
        int j = this.randCoord(rand, x, 16);
        int l = this.randCoord(rand, z, 16);

        if (world.isAirBlock(i, this.getTerrainHeightAt(world, i, k), k)) {
            new WorldGenTropicraftFlowers(world, rand, TCBlockRegistry.flowers, BiomeGenTropics.DEFAULT_FLOWER_META)
                .generate(i, this.getTerrainHeightAt(world, i, k), k);
        }

        if (rand.nextInt(2) == 0) {
            final int treeType = new Random((long) (j >> 2) << 32 | (l >> 2)).nextInt(4);
            j = this.randCoord(rand, x, 16);
            l = this.randCoord(rand, z, 16);
            new WorldGenTropicraftFruitTrees(world, rand, treeType)
                .generate(j, this.getTerrainHeightAt(world, j, l), l);
        }
        if (rand.nextInt(25) == 0) {
            i = this.randCoord(rand, x, 16);
            k = this.randCoord(rand, z, 16);
            new WorldGenTropicsTreasure(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }
    }
}
