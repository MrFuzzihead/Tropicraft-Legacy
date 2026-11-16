package net.tropicraft.world.chunk;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers {@link MineralSandField}, the worldgen noise that places the decorative mineral sands.
 * <p>
 * The regression it exists for: the variant used to be rolled once per chunk, so every 16×16 chunk was one flat colour
 * and the colour changed exactly on the chunk border - a checkerboard of squares on every coastline (B28). The tests
 * that matter are therefore "a chunk border is not special" and "patches are larger than a chunk and fade at their
 * edges".
 */
public class MineralSandFieldTest {

    private static final long SEED = 0xC0FFEEL;

    /** Deterministic sample of the field over a square of columns. */
    private static int[][] sample(final int originX, final int originZ, final int size) {
        final int[][] field = new int[size][size];
        for (int x = 0; x < size; ++x) {
            for (int z = 0; z < size; ++z) {
                field[x][z] = MineralSandField.variantAt(SEED, originX + x, originZ + z);
            }
        }
        return field;
    }

    private static double disagreementRate(final int[][] field, final boolean chunkEdgeOnly) {
        int pairs = 0;
        int different = 0;
        for (int x = 0; x < field.length - 1; ++x) {
            for (int z = 0; z < field[x].length; ++z) {
                final boolean edge = Math.floorMod(x, 16) == 15;
                if (edge != chunkEdgeOnly) {
                    continue;
                }
                ++pairs;
                if (field[x][z] != field[x + 1][z]) {
                    ++different;
                }
            }
        }
        return pairs == 0 ? 0.0 : (double) different / pairs;
    }

    @Test
    public void theSameCoordinatesAlwaysProduceTheSameVariant() {
        for (int i = 0; i < 256; ++i) {
            final int x = i * 37 - 5000;
            final int z = i * -91 + 3000;
            Assert.assertEquals(MineralSandField.variantAt(SEED, x, z), MineralSandField.variantAt(SEED, x, z));
        }
    }

    @Test
    public void aChunkBorderIsNotSpecial() {
        // The old per-chunk roll made every interior pair agree and every chunk-edge pair disagree. With the field,
        // the chance of a change is a property of the noise, so a 16-block step must look like a 1-block step.
        final int[][] field = sample(-4096, -4096, 512);
        final double interior = disagreementRate(field, false);
        final double chunkEdge = disagreementRate(field, true);
        Assert.assertTrue("some columns must differ at all: " + interior, interior > 0.02D && interior < 0.5D);
        Assert.assertEquals(
            "chunk borders must not be a special place: interior=" + interior + " edge=" + chunkEdge,
            interior,
            chunkEdge,
            0.08D);
    }

    @Test
    public void everyVariantOccursAndPlainSandIsStillCommon() {
        final int[][] field = sample(0, 0, 512);
        final int[] counts = new int[MineralSandField.VARIANTS + 1];
        for (final int[] column : field) {
            for (final int value : column) {
                ++counts[value + 1];
            }
        }
        final int total = 512 * 512;
        Assert.assertTrue("plain sand should dominate: " + counts[0], counts[0] > total / 2);
        for (int variant = 0; variant < MineralSandField.VARIANTS; ++variant) {
            Assert.assertTrue("variant " + variant + " should appear: " + counts[variant + 1], counts[variant + 1] > 0);
        }
    }

    @Test
    public void mineralSandClustersRatherThanSpeckling() {
        // A mineral column should mostly have mineral neighbours; an actually random field would sit near 1%.
        final int[][] field = sample(1234, -5678, 256);
        int mineral = 0;
        int mineralWithMineralNeighbour = 0;
        for (int x = 1; x < field.length - 1; ++x) {
            for (int z = 1; z < field[x].length - 1; ++z) {
                if (field[x][z] == MineralSandField.PLAIN_SAND) {
                    continue;
                }
                ++mineral;
                boolean neighbour = false;
                for (int dx = -1; dx <= 1 && !neighbour; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        if ((dx != 0 || dz != 0) && field[x + dx][z + dz] != MineralSandField.PLAIN_SAND) {
                            neighbour = true;
                            break;
                        }
                    }
                }
                if (neighbour) {
                    ++mineralWithMineralNeighbour;
                }
            }
        }
        Assert.assertTrue("some mineral sand must exist", mineral > 100);
        final double clustered = (double) mineralWithMineralNeighbour / mineral;
        Assert.assertTrue("mineral sand should cluster: " + clustered, clustered > 0.9D);
    }

    @Test
    public void patchEdgesFadeInsteadOfStoppingOnALine() {
        // The old code had no edges inside a chunk at all; with a fade band there must be mineral columns that touch
        // plain sand (i.e. a speckled transition) rather than only solid blocks of colour.
        final int[][] field = sample(-200, 200, 256);
        int mixed = 0;
        for (int x = 1; x < field.length - 1; ++x) {
            for (int z = 1; z < field[x].length - 1; ++z) {
                if (field[x][z] == MineralSandField.PLAIN_SAND) {
                    continue;
                }
                boolean adjacentPlain = false;
                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        if (field[x + dx][z + dz] == MineralSandField.PLAIN_SAND) {
                            adjacentPlain = true;
                        }
                    }
                }
                if (adjacentPlain) {
                    ++mixed;
                }
            }
        }
        Assert.assertTrue("patch edges must exist", mixed > 0);
    }

    @Test
    public void variantsAreNotChunkAligned() {
        // Within one 16x16 chunk there should be more than one value across a large sample of chunks, i.e. the
        // variant is not a per-chunk constant.
        int chunksWithOneVariant = 0;
        int chunksWithSeveral = 0;
        for (int chunkX = -4; chunkX <= 4; ++chunkX) {
            for (int chunkZ = -4; chunkZ <= 4; ++chunkZ) {
                final int[][] field = sample(chunkX * 16, chunkZ * 16, 16);
                final java.util.Set<Integer> values = new java.util.HashSet<Integer>();
                for (final int[] column : field) {
                    for (final int value : column) {
                        values.add(value);
                    }
                }
                if (values.size() == 1) {
                    ++chunksWithOneVariant;
                } else {
                    ++chunksWithSeveral;
                }
            }
        }
        Assert.assertTrue(
            "some chunks must contain more than one value: " + chunksWithSeveral
                + "/"
                + (chunksWithOneVariant + chunksWithSeveral),
            chunksWithSeveral > 0);
    }
}
