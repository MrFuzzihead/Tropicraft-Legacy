package net.tropicraft.encyclopedia;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers the recipe index in {@link Encyclopedia}: the lookup has to find the recipes for an <em>equal</em> stack, not
 * just the identical one.
 * <p>
 * The map used to be keyed by {@code ItemStack}, which has no {@code equals}/{@code hashCode} in 1.7.10, so this
 * behaviour was provided by linear {@code isItemEqual} scans over the key set. The index is now keyed by item + damage,
 * which is exactly what {@code isItemEqual} compares - these tests pin that the semantics did not move with it.
 * </p>
 */
public class EncyclopediaTest {

    private static final String CONTENTS = "/assets/tropicraft/gui/EncyclopediaTropica.txt";

    /** A book with no per-world state touched: nothing here reads or writes progress. */
    private static Encyclopedia book() {
        return new Encyclopedia("eTsave.dat", CONTENTS, "outside", "inside");
    }

    /** The first page listed in the contents file, so the test does not depend on a specific entry name. */
    private static String firstPage(final Encyclopedia book) {
        final List<String> pages = book.getSortedPages();
        Assert.assertFalse("the contents file produced no pages", pages.isEmpty());
        return pages.get(0);
    }

    private static Item item() {
        return new Item();
    }

    private static ItemStack stack(final Item item, final int count) {
        return new ItemStack(item, count);
    }

    @Test
    public void anEqualStackFindsTheRecipesOfANonIdenticalOne() {
        final Encyclopedia book = book();
        final Item item = item();
        book.includeItem(firstPage(book), stack(item, 1));
        book.includeRecipe(stack(item, 1), new Object[] { "X", 'X', stack(item, 1) });

        // A brand new stack instance, as the renderer and the inventory both produce.
        final List<ShapedRecipes> recipes = book.getRecipesForEntry(0);
        Assert.assertEquals(1, recipes.size());
        Assert.assertEquals(
            item,
            recipes.get(0)
                .getRecipeOutput()
                .getItem());
    }

    @Test
    public void damageIsPartOfTheEntryIdentity() {
        final Encyclopedia book = book();
        final Item item = item();
        // The page shows metadata 2; the recipe is for metadata 0, so it must not be served for this page.
        book.includeItem(firstPage(book), new ItemStack(item, 1, 2));
        book.includeRecipe(new ItemStack(item, 1, 0), new Object[] { "X", 'X', new ItemStack(item, 1, 0) });
        Assert.assertTrue(
            "a different metadata is a different entry",
            book.getRecipesForEntry(0)
                .isEmpty());

        // The matching metadata does find it.
        book.includeRecipe(new ItemStack(item, 1, 2), new Object[] { "X", 'X', new ItemStack(item, 1, 2) });
        Assert.assertEquals(
            1,
            book.getRecipesForEntry(0)
                .size());
    }

    @Test
    public void differentItemsDoNotShareRecipes() {
        final Encyclopedia book = book();
        final Item crafted = item();
        final Item other = item();
        book.includeItem(firstPage(book), stack(other, 1));
        book.includeRecipe(stack(crafted, 1), new Object[] { "X", 'X', stack(crafted, 1) });

        Assert.assertTrue(
            "the other item has no recipes",
            book.getRecipesForEntry(0)
                .isEmpty());
    }

    @Test
    public void aRecipeWithTwoIngredientsIsListedForEachOfThem() {
        final Encyclopedia book = book();
        final Item a = item();
        final Item b = item();
        final String page = firstPage(book);
        book.includeItem(page, stack(a, 1));
        book.includeItem(page, stack(b, 1));
        // One recipe using both ingredients. The page lists both items, and getRecipesForEntry concatenates the
        // recipes of every item the page shows, so the recipe is listed twice - existing behaviour, pinned here.
        book.includeRecipe(stack(a, 1), new Object[] { "XY", 'X', stack(a, 1), 'Y', stack(b, 1) });

        Assert.assertEquals(
            2,
            book.getRecipesForEntry(0)
                .size());
    }

    @Test
    public void duplicateIngredientsAreIndexedOnce() {
        final Encyclopedia book = book();
        final Item a = item();
        final String page = firstPage(book);
        book.includeItem(page, stack(a, 1));
        // The same ingredient twice in one recipe, with two different stack instances.
        book.includeRecipe(stack(a, 1), new Object[] { "XX", 'X', stack(a, 1), 'X', stack(a, 1) });

        Assert.assertEquals(
            1,
            book.getRecipesForEntry(0)
                .size());
    }
}
