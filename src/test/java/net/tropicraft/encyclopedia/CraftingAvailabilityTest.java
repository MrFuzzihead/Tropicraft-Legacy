package net.tropicraft.encyclopedia;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers {@link CraftingAvailability}, the check behind the encyclopedia's "only recipes I can make" filter.
 * <p>
 * It has to be count aware (two of an item is not one) and damage aware (a recipe for a red dart is not satisfied by
 * a plain one), because that is the difference between a filter that is useful and one that lies.
 * </p>
 */
public class CraftingAvailabilityTest {

    private static Item item() {
        return new Item();
    }

    private static ShapedRecipes recipe(final ItemStack... ingredients) {
        return new ShapedRecipes(ingredients.length, 1, ingredients, new ItemStack(item(), 1));
    }

    private static InventoryPlayer inventory(final ItemStack... stacks) {
        final InventoryPlayer inventory = new InventoryPlayer(null);
        for (int i = 0; i < stacks.length; ++i) {
            inventory.mainInventory[i] = stacks[i];
        }
        return inventory;
    }

    @Test
    public void aRecipeWithEveryIngredientIsCraftable() {
        final Item wood = item();
        final Item string = item();
        Assert.assertTrue(
            CraftingAvailability.canCraft(
                recipe(new ItemStack(wood), new ItemStack(string)),
                inventory(new ItemStack(wood), new ItemStack(string))));
    }

    @Test
    public void aMissingIngredientIsNotCraftable() {
        final Item wood = item();
        final Item string = item();
        Assert.assertFalse(
            CraftingAvailability
                .canCraft(recipe(new ItemStack(wood), new ItemStack(string)), inventory(new ItemStack(wood))));
    }

    @Test
    public void twoOfTheSameIngredientNeedTwoOfThem() {
        final Item wood = item();
        final ShapedRecipes recipe = recipe(new ItemStack(wood, 1), new ItemStack(wood, 1));
        Assert.assertFalse(
            "a single one is not two",
            CraftingAvailability.canCraft(recipe, inventory(new ItemStack(wood, 1))));
        Assert.assertTrue(
            "two stacks of one works",
            CraftingAvailability.canCraft(recipe, inventory(new ItemStack(wood, 1), new ItemStack(wood, 1))));
        Assert.assertTrue(
            "one stack of two works",
            CraftingAvailability.canCraft(recipe, inventory(new ItemStack(wood, 2))));
    }

    @Test
    public void damageIsPartOfTheMatch() {
        final Item dart = item();
        Assert.assertFalse(
            "a plain dart is not a red dart",
            CraftingAvailability.canCraft(recipe(new ItemStack(dart, 1, 1)), inventory(new ItemStack(dart, 1, 0))));
        Assert.assertTrue(
            CraftingAvailability.canCraft(recipe(new ItemStack(dart, 1, 1)), inventory(new ItemStack(dart, 1, 1))));
    }

    @Test
    public void emptySlotsInTheRecipeAreIgnored() {
        final Item wood = item();
        // A 3x1 recipe with two empty slots, as the vanilla shaped recipe stores it.
        final ShapedRecipes recipe = new ShapedRecipes(
            3,
            1,
            new ItemStack[] { new ItemStack(wood), null, null },
            new ItemStack(item(), 1));
        Assert.assertTrue(CraftingAvailability.canCraft(recipe, inventory(new ItemStack(wood))));
    }

    @Test
    public void anEmptyInventoryCannotCraftAnything() {
        Assert.assertFalse(CraftingAvailability.canCraft(recipe(new ItemStack(item())), inventory()));
    }

    @Test
    public void aRecipeWithNoIngredientsIsNotCraftable() {
        final ShapedRecipes recipe = new ShapedRecipes(1, 1, new ItemStack[] { null }, new ItemStack(item(), 1));
        Assert.assertFalse(CraftingAvailability.canCraft(recipe, inventory(new ItemStack(item()))));
    }

    @Test
    public void nullInputsAreAnsweredNotThrown() {
        Assert.assertFalse(CraftingAvailability.canCraft(null, inventory()));
        Assert.assertFalse(CraftingAvailability.canCraft(recipe(new ItemStack(item())), null));
    }
}
