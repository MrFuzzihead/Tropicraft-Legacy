package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.world.worldgen.WorldGenCoffeePlant;
import net.tropicraft.world.worldgen.WorldGenForestAltarRuin;
import net.tropicraft.world.worldgen.WorldGenHomeTree;
import net.tropicraft.world.worldgen.WorldGenTCUndergrowth;
import net.tropicraft.world.worldgen.WorldGenTallTree;
import net.tropicraft.world.worldgen.WorldGenTualang;
import net.tropicraft.world.worldgen.WorldGenUpTree;

public class BiomeGenRainforest extends BiomeGenTropicraft {

    private static final int COFFEE_PLANT_AMOUNT = 2;
    private static final int ALTAR_CHANCE = 70;
    private static final int TALL_TREE_CHANCE = 2;
    private static final int UP_TREE_CHANCE = 2;
    private static final int UNDERGROWTH_AMOUNT = 15;
    private static final int SMALL_TUALANG_AMOUNT = 4;
    private static final int LARGE_TUALANG_AMOUNT = 2;
    private static final int HOME_TREE_RARITY = 80;

    public BiomeGenRainforest(final int biomeID) {
        super(biomeID);
    }

    @Override
    public void decorate(final World world, final Random rand, final int x, final int z) {
        if (BiomeGenRainforest.DISABLEDECORATION) {
            System.out.println("decoration disabled via BiomeGenTropics.DISABLEDECORATION, " + this);
            return;
        }
        if (rand.nextInt(80) == 0) {
            final int xx = rand.nextInt(16) + x + 8;
            final int zz = rand.nextInt(16) + z + 8;
            new WorldGenHomeTree(world, rand).generate(xx, 0, zz);
        }
        if (rand.nextInt(70) == 0) {
            new WorldGenForestAltarRuin(world, rand)
                .generate(this.randCoord(rand, x, 16), 0, this.randCoord(rand, x, 16));
        }
        if (rand.nextInt(2) == 0) {
            final int i = this.randCoord(rand, x, 16);
            final int k = this.randCoord(rand, z, 16);
            new WorldGenTallTree(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }
        if (rand.nextInt(2) == 0) {
            final int i = this.randCoord(rand, x, 16);
            final int k = this.randCoord(rand, z, 16);
            new WorldGenUpTree(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
        }
        for (int a = 0; a < 4; ++a) {
            final int j = this.randCoord(rand, x, 16);
            final int l = this.randCoord(rand, z, 16);
            new WorldGenTualang(world, rand, 16, 9).generate(j, this.getTerrainHeightAt(world, j, l), l);
        }
        for (int a = 0; a < 2; ++a) {
            final int j = this.randCoord(rand, x, 16);
            final int l = this.randCoord(rand, z, 16);
            new WorldGenTualang(world, rand, 25, 11).generate(j, this.getTerrainHeightAt(world, j, l), l);
        }
        for (int a = 0; a < 15; ++a) {
            final int j = this.randCoord(rand, x, 16);
            final int l = this.randCoord(rand, z, 16);
            new WorldGenTCUndergrowth(world, rand).generate(j, this.getTerrainHeightAt(world, j, l), l);
        }
        for (int a = 0; a < 2; ++a) {
            final int j = this.randCoord(rand, x, 16);
            final int l = this.randCoord(rand, z, 16);
            new WorldGenCoffeePlant(world, rand).generate(j, this.getTerrainHeightAt(world, j, l), l);
        }
        super.decorate(world, rand, x, z);
    }
}
