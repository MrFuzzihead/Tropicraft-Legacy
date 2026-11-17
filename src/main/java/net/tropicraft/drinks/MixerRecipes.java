package net.tropicraft.drinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.tropicraft.registry.TCDrinkMixerRegistry;

public final class MixerRecipes {

    private static Map<Drink, Ingredient[]> drinkToIngredientsMap;

    private MixerRecipes() {}

    public static void addMixerRecipes() {
        registerMixerRecipe(Drink.limeade, Ingredient.lime, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(Drink.caipirinha, Ingredient.lime, Ingredient.sugarcane, Ingredient.waterBucket);
        registerMixerRecipe(Drink.orangeade, Ingredient.orange, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(Drink.lemonade, Ingredient.lemon, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(Drink.blackCoffee, Ingredient.roastedCoffeeBean, Ingredient.waterBucket);
        // Pineapple Cubes + Coconut Chunk is the pair the drink mixer's own recipe list gives for a Piña Colada, and
        // the form the two ingredients actually have in the inventory; it is registered first so that
        // drinkToIngredientsMap - which the crafting table recipe and the encyclopedia read - keeps pointing at the
        // pair it pointed at before.
        registerMixerRecipe(Drink.pinaColada, Ingredient.pineappleChunks, Ingredient.coconutChunk);
        registerMixerRecipe(Drink.pinaColada, Ingredient.pineapple, Ingredient.coconutChunk);
        registerMixerRecipe(Drink.pinaColada, Ingredient.pineappleChunks, Ingredient.coconut);
    }

    private static void registerMixerRecipe(final Drink result, final Ingredient... ingredients) {
        TCDrinkMixerRegistry.getInstance()
            .registerRecipe(new MixerRecipe(result, ingredients));
        MixerRecipes.drinkToIngredientsMap.put(result, ingredients);
    }

    public static ItemStack getItemStack(final Drink drink) {
        final List<ItemStack> stack = new ArrayList<ItemStack>();
        for (final Ingredient i : MixerRecipes.drinkToIngredientsMap.get(drink)) {
            stack.add(i.getIngredient());
        }
        return TCDrinkMixerRegistry.getInstance()
            .getResult(stack.toArray(new ItemStack[stack.size()]));
    }

    static {
        MixerRecipes.drinkToIngredientsMap = new HashMap<Drink, Ingredient[]>();
    }
}
