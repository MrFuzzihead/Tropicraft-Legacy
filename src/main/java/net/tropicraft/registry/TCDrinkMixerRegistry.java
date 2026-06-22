package net.tropicraft.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.tropicraft.block.tileentity.TileEntityEIHMixer;
import net.tropicraft.drinks.Ingredient;
import net.tropicraft.drinks.MixerRecipe;
import net.tropicraft.item.ItemCocktail;

public class TCDrinkMixerRegistry {

    private static TCDrinkMixerRegistry instance;
    private List<MixerRecipe> recipes;

    private TCDrinkMixerRegistry() {
        this.recipes = new LinkedList<MixerRecipe>();
    }

    public static TCDrinkMixerRegistry getInstance() {
        if (TCDrinkMixerRegistry.instance == null) {
            TCDrinkMixerRegistry.instance = new TCDrinkMixerRegistry();
        }
        return TCDrinkMixerRegistry.instance;
    }

    public void registerRecipe(final MixerRecipe recipe) {
        this.recipes.add(recipe);
    }

    public boolean isRegisteredIngredient(final ItemStack item) {
        if (item.getItem() != null && item.getItem() == TCItemRegistry.cocktail) {
            return true;
        }
        for (final MixerRecipe recipe : this.recipes) {
            for (final Ingredient i : recipe.getIngredients()) {
                if (ItemStack.areItemStacksEqual(i.getIngredient(), item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRegisteredIngredient(final Item item) {
        return this.isRegisteredIngredient(new ItemStack(item, 1, 0));
    }

    public ItemStack getResult(final ItemStack[] ingredients) {
        for (final MixerRecipe recipe : this.recipes) {
            if (ItemStack.areItemStacksEqual(recipe.getIngredients()[0].getIngredient(), ingredients[0])
                && ItemStack.areItemStacksEqual(recipe.getIngredients()[1].getIngredient(), ingredients[1])) {
                return ItemCocktail.makeCocktail(recipe);
            }
            if (ItemStack.areItemStacksEqual(recipe.getIngredients()[0].getIngredient(), ingredients[1])
                && ItemStack.areItemStacksEqual(recipe.getIngredients()[1].getIngredient(), ingredients[0])) {
                return ItemCocktail.makeCocktail(recipe);
            }
        }
        final List<Ingredient> is = new ArrayList<Ingredient>();
        is.addAll(TileEntityEIHMixer.listIngredients(ingredients[0]));
        is.addAll(TileEntityEIHMixer.listIngredients(ingredients[1]));
        Collections.sort(is);
        return ItemCocktail.makeCocktail((Ingredient[]) is.toArray(new Ingredient[is.size()]));
    }

    public List<MixerRecipe> getRecipes() {
        return this.recipes;
    }

    static {
        TCDrinkMixerRegistry.instance = null;
    }
}
