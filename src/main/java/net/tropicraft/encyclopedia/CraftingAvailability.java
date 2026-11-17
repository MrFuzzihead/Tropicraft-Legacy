package net.tropicraft.encyclopedia;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 * Answers "could the player make this right now", for the encyclopedia's craftable-only filter.
 * <p>
 * Deliberately conservative and self-contained: it only looks at the player's main inventory, it matches ingredients
 * with {@code ItemStack.isItemEqual} (the same rule the recipe book and the crafting table use for a shaped recipe),
 * and it does not try to be clever about taking from a chest, a crafting grid or a second stack of a subtype. A recipe
 * it says yes to is one whose ingredients, counted, are present - which is exactly what the filter is for.
 * </p>
 */
public final class CraftingAvailability {

    private CraftingAvailability() {}

    public static boolean canCraft(final ShapedRecipes recipe, final InventoryPlayer inventory) {
        if (recipe == null || inventory == null || inventory.mainInventory == null) {
            return false;
        }
        final ItemStack[] pool = inventory.mainInventory;
        final int[] remaining = new int[pool.length];
        for (int i = 0; i < pool.length; ++i) {
            remaining[i] = pool[i] == null ? 0 : pool[i].stackSize;
        }
        int required = 0;
        for (final ItemStack ingredient : recipe.recipeItems) {
            if (ingredient == null) {
                continue;
            }
            ++required;
            boolean satisfied = false;
            for (int i = 0; i < pool.length; ++i) {
                if (remaining[i] > 0 && pool[i].isItemEqual(ingredient)) {
                    --remaining[i];
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) {
                return false;
            }
        }
        return required > 0;
    }
}
