package net.tropicraft.curare;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

public class CurareMix {

    private CurareType result;
    private ItemStack[] ingredients;

    public CurareMix(final CurareType result, final ItemStack... ingredients) {
        this.result = result;
        this.ingredients = ingredients;
    }

    public ItemStack[] getIngredients() {
        return this.ingredients;
    }

    public CurareType getResult() {
        return this.result;
    }

    public int[] getSortedDamageVals() {
        final int[] temp = new int[this.ingredients.length];
        int count = 0;
        for (final ItemStack ing : this.ingredients) {
            temp[count] = ing.getItemDamage();
            ++count;
        }
        Arrays.sort(temp);
        return temp;
    }

    @Override
    public String toString() {
        return this.getResult()
            .toString();
    }
}
