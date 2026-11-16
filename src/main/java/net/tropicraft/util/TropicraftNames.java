package net.tropicraft.util;

/**
 * The naming convention Tropicraft's items and blocks use.
 * <p>
 * Every Tropicraft item's unlocalized name is {@code item.tropicraft:<name>} and every block's is
 * {@code tile.tropicraft:<name>}, where {@code <name>} is whatever follows the first dot of the raw
 * {@code super.getUnlocalizedName()} ({@code item.flippers} → {@code flippers}). That logic used to be copy-pasted
 * into ~35 classes as a {@code getUnlocalizedName()} override plus a private {@code getActualName(String)}.
 * </p>
 * <p>
 * This is a static helper rather than a default method on an interface because {@code getUnlocalizedName} is declared
 * by {@code Item}/{@code Block}: a default method would lose to the inherited class method and never run.
 * </p>
 */
public final class TropicraftNames {

    private TropicraftNames() {}

    /** @param rawName what {@code super.getUnlocalizedName()} returned, e.g. {@code item.flippers} */
    public static String item(final String rawName) {
        return "item.tropicraft:" + afterFirstDot(rawName);
    }

    /** @param rawName what {@code super.getUnlocalizedName()} returned, e.g. {@code tile.log} */
    public static String block(final String rawName) {
        return "tile.tropicraft:" + afterFirstDot(rawName);
    }

    /**
     * Everything after the first dot. Also used for texture paths, where the input is e.g.
     * {@code item.tropicraft:flippers} and the interesting part is everything after {@code item.}.
     * <p>
     * A name without a dot is returned unchanged, which is what the duplicated helper did ({@code indexOf} returns
     * -1, so it took the whole string).
     * </p>
     */
    public static String afterFirstDot(final String name) {
        return name.substring(name.indexOf('.') + 1);
    }
}
