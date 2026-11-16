package net.tropicraft.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers {@link TropicraftNames}, the single place the {@code item.tropicraft:<name>} / {@code tile.tropicraft:<name>}
 * convention now lives.
 * <p>
 * The behaviour has to stay exactly what the ~35 duplicated {@code getUnlocalizedName}/{@code getActualName} copies
 * did, including the edge cases: {@code indexOf('.')} returns -1 for a name with no dot, so the old helper took the
 * whole string - a "fix" that returns an empty name there would silently rename such an item.
 * </p>
 */
public class TropicraftNamesTest {

    @Test
    public void itemsAreNamespacedWithTropicraft() {
        Assert.assertEquals("item.tropicraft:flippers", TropicraftNames.item("item.flippers"));
        Assert.assertEquals("item.tropicraft:pineappleCubes", TropicraftNames.item("item.pineappleCubes"));
    }

    @Test
    public void blocksAreNamespacedWithTropicraft() {
        Assert.assertEquals("tile.tropicraft:log", TropicraftNames.block("tile.log"));
        Assert.assertEquals("tile.tropicraft:tallFlower", TropicraftNames.block("tile.tallFlower"));
    }

    @Test
    public void onlyTheFirstSegmentIsStripped() {
        // BlockFruitLeaves and friends build names from a raw value that already carries a namespace.
        Assert.assertEquals("tropicraft:fruitLeaves", TropicraftNames.afterFirstDot("tile.tropicraft:fruitLeaves"));
        Assert.assertEquals("bar", TropicraftNames.afterFirstDot("foo.bar"));
    }

    @Test
    public void aNameWithoutADotIsUnchanged() {
        // This is what the old `substring(indexOf('.') + 1)` did, and several texture paths depend on it.
        Assert.assertEquals("flippers", TropicraftNames.afterFirstDot("flippers"));
        Assert.assertEquals("tropicraft:flippers", TropicraftNames.afterFirstDot("tropicraft:flippers"));
    }

    @Test
    public void repeatedCallsAreStable() {
        final String once = TropicraftNames.item("item.flippers");
        Assert.assertEquals(once, TropicraftNames.item("item.flippers"));
    }
}
