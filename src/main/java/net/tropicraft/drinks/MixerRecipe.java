package net.tropicraft.drinks;

import java.util.Arrays;

public class MixerRecipe implements IMixerRecipe {

    private Drink result;
    private Ingredient[] ingredients;

    public MixerRecipe(final Drink result, final Ingredient... ingredients) {
        this.result = result;
        Arrays.sort(this.ingredients = ingredients);
    }

    public Ingredient[] getIngredients() {
        return this.ingredients;
    }

    public Drink getCraftingResult() {
        return this.result;
    }
}
