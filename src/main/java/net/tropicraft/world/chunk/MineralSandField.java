package net.tropicraft.world.chunk;

/**
 * Decides where the decorative mineral sands go, and which of the four they are.
 * <p>
 * These used to be picked by a single {@code rand.nextInt(200)} per chunk, applied to every beach and ocean column of
 * that chunk - so each 16×16 chunk was either entirely "Foamy Sands", entirely one of the other three variants, or
 * entirely plain sand, and neighbouring chunks rolled independently. On a coastline that reads as a checkerboard of
 * solid squares.
 * </p>
 * <p>
 * Here the variant is a function of the world coordinates only:
 * </p>
 * <ul>
 * <li>a low frequency <em>patch</em> field (lattice spacing {@link #PATCH_SIZE}) decides where mineral sand can appear
 * at all, so patches are a few chunks across instead of one;</li>
 * <li>the outer {@link #FADE_WIDTH} of the patch is dithered per column, so the patch edge fades into the ordinary sand
 * instead of stopping on a straight line;</li>
 * <li>inside a patch a finer <em>variant</em> field ({@link #VARIANT_SIZE}) picks which of the four sands, so a patch
 * is mottled rather than one flat colour.</li>
 * </ul>
 * <p>
 * Deterministic for a given world seed and coordinates, which is also what makes it unit testable.
 * </p>
 */
public final class MineralSandField {

    /** Lattice spacing of the patch mask, in blocks: a patch is comfortably wider than a chunk. */
    static final int PATCH_SIZE = 40;
    /** Lattice spacing of the variant field, in blocks: fine enough to mottle the inside of a patch. */
    static final int VARIANT_SIZE = 14;
    /** Patch values below this are plain sand; values above are mineral sand. */
    private static final double PATCH_THRESHOLD = 0.78D;
    /** Width of the dithered transition band just below the threshold. */
    private static final double FADE_WIDTH = 0.10D;

    /** Number of mineral sand variants (metadata 0..3: Coral, Green/Foamy, Black, Mineral). */
    public static final int VARIANTS = 4;
    /** Returned for a column that should stay plain sand. */
    public static final int PLAIN_SAND = -1;

    private static final long PATCH_SALT = 0x9E3779B97F4A7C15L;
    private static final long VARIANT_SALT = 0xC2B2AE3D27D4EB4FL;
    private static final long DITHER_SALT = 0x165667B19E3779F9L;

    private MineralSandField() {}

    /**
     * @return the metadata (0..3) of the mineral sand for this column, or {@link #PLAIN_SAND} for ordinary sand.
     */
    public static int variantAt(final long worldSeed, final int x, final int z) {
        final double patch = valueNoise(worldSeed, x, z, PATCH_SIZE, PATCH_SALT);
        if (patch < PATCH_THRESHOLD - FADE_WIDTH) {
            return PLAIN_SAND;
        }
        if (patch < PATCH_THRESHOLD) {
            // Fade band: the chance of mineral sand rises across the band, decided per column, so the edge speckles
            // into the plain sand rather than ending on a hard line.
            final double chance = (patch - (PATCH_THRESHOLD - FADE_WIDTH)) / FADE_WIDTH;
            if (hashToUnit(worldSeed, x, z, DITHER_SALT) > chance) {
                return PLAIN_SAND;
            }
        }
        final int variant = (int) (valueNoise(worldSeed, x, z, VARIANT_SIZE, VARIANT_SALT) * VARIANTS);
        return variant < 0 ? 0 : (variant >= VARIANTS ? VARIANTS - 1 : variant);
    }

    /** Smoothly interpolated value noise in [0, 1) at the given lattice spacing. */
    private static double valueNoise(final long worldSeed, final int x, final int z, final int size, final long salt) {
        final double fx = (double) x / size;
        final double fz = (double) z / size;
        final int x0 = floor(fx);
        final int z0 = floor(fz);
        final double tx = smooth(fx - x0);
        final double tz = smooth(fz - z0);
        final double v00 = hashToUnit(worldSeed, x0, z0, salt);
        final double v10 = hashToUnit(worldSeed, x0 + 1, z0, salt);
        final double v01 = hashToUnit(worldSeed, x0, z0 + 1, salt);
        final double v11 = hashToUnit(worldSeed, x0 + 1, z0 + 1, salt);
        final double bottom = v00 + (v10 - v00) * tx;
        final double top = v01 + (v11 - v01) * tx;
        return bottom + (top - bottom) * tz;
    }

    /** Smoothstep, so the value noise has no ridges on the lattice lines. */
    private static double smooth(final double t) {
        return t * t * (3.0D - 2.0D * t);
    }

    private static int floor(final double value) {
        final int truncated = (int) value;
        return value < truncated ? truncated - 1 : truncated;
    }

    /** Hash to [0, 1). Any well-mixed integer hash works; this is the murmur finaliser on the mixed coordinates. */
    private static double hashToUnit(final long worldSeed, final int x, final int z, final long salt) {
        long h = worldSeed ^ salt;
        h ^= x * 0x9E3779B97F4A7C15L;
        h ^= z * 0xC2B2AE3D27D4EB4FL;
        h ^= h >>> 33;
        h *= 0xFF51AFD7ED558CCDL;
        h ^= h >>> 33;
        h *= 0xC4CEB9FE1A85EC53L;
        h ^= h >>> 33;
        return (h >>> 11) * 0x1.0p-53;
    }
}
