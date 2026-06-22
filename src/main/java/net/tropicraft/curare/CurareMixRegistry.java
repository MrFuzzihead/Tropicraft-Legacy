package net.tropicraft.curare;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class CurareMixRegistry {

    private static CurareMixRegistry instance;
    private Map<CurareType, CurareMix> recipes;

    private CurareMixRegistry() {
        this.recipes = new HashMap<CurareType, CurareMix>();
    }

    public static CurareMixRegistry getInstance() {
        if (CurareMixRegistry.instance == null) {
            CurareMixRegistry.instance = new CurareMixRegistry();
        }
        return CurareMixRegistry.instance;
    }

    public void registerRecipe(final CurareMix recipe) {
        this.recipes.put(recipe.getResult(), recipe);
    }

    public Collection<CurareMix> getRecipes() {
        return this.recipes.values();
    }

    public CurareMix getCurareMixFromType(final CurareType type) {
        return this.recipes.get(type);
    }

    public int[] getSortedDamageVals(final ItemStack[] ingredients) {
        final int[] temp = new int[ingredients.length];
        int count = 0;
        for (final ItemStack ing : ingredients) {
            System.out.println(ing.getItemDamage());
            temp[count] = ing.getItemDamage();
            ++count;
        }
        Arrays.sort(temp);
        return temp;
    }

    public CurareType getCurareFromIngredients(final ItemStack... ingredients) {
        for (final CurareMix mix : this.getRecipes()) {
            if (Arrays.equals(mix.getSortedDamageVals(), this.getSortedDamageVals(ingredients))) {
                return mix.getResult();
            }
        }
        return CurareType.weakness;
    }
}
