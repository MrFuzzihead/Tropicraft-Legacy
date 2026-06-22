package net.tropicraft.drinks;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class Ingredient implements Comparable<Ingredient> {

    public static final Ingredient[] ingredientsList;
    public static final Ingredient sugar;
    public static final Ingredient lemon;
    public static final Ingredient lime;
    public static final Ingredient orange;
    public static final Ingredient grapefruit;
    public static final Ingredient pineapple;
    public static final Ingredient pineappleChunks;
    public static final Ingredient coconut;
    public static final Ingredient coconutChunk;
    public static final Ingredient sugarcane;
    public static final Ingredient roastedCoffeeBean;
    public static final Ingredient waterBucket;
    public static final Ingredient milkBucket;
    public static final Ingredient cocoaBean;
    private ItemStack ingredientItem;
    private int color;
    public int ingredientId;
    private boolean primary;
    private float alpha;
    private List<DrinkAction> actions;

    public Ingredient(final int id, final ItemStack ingredient, final boolean primary, final int color) {
        this.alpha = 1.0f;
        this.actions = new LinkedList<DrinkAction>();
        if (Ingredient.ingredientsList[id] != null) {
            throw new IllegalArgumentException(
                "Ingredient Id slot " + id
                    + " already occupied by "
                    + Ingredient.ingredientsList[id].ingredientItem.getUnlocalizedName()
                    + "!");
        }
        this.ingredientId = id;
        this.ingredientItem = ingredient;
        this.primary = primary;
        this.color = color;
        Ingredient.ingredientsList[id] = this;
    }

    public Ingredient(final int id, final ItemStack ingredient, final boolean primary, final int color,
        final float alpha) {
        this(id, ingredient, primary, color);
        this.alpha = alpha;
    }

    public Ingredient addAction(final DrinkAction action) {
        this.actions.add(action);
        return this;
    }

    public ItemStack getIngredient() {
        return this.ingredientItem;
    }

    public Item getIngredientItem() {
        return this.ingredientItem.getItem();
    }

    public int getColor() {
        return this.color;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    public float getAlpha() {
        return this.alpha;
    }

    @Override
    public int compareTo(final Ingredient other) {
        return (this.ingredientId < other.ingredientId) ? -1 : ((this.ingredientId == other.ingredientId) ? 0 : 1);
    }

    public void onDrink(final EntityPlayer player) {
        for (final DrinkAction action : this.actions) {
            action.onDrink(player);
        }
    }

    static {
        ingredientsList = new Ingredient[128];
        sugar = new Ingredient(0, new ItemStack(Items.sugar), false, 16777215, 0.1f)
            .addAction((DrinkAction) new DrinkActionFood(1, 0.1f));
        lemon = new Ingredient(5, new ItemStack((Item) TCItemRegistry.lemon), true, 16776960)
            .addAction((DrinkAction) new DrinkActionFood(2, 0.2f));
        lime = new Ingredient(6, new ItemStack((Item) TCItemRegistry.lime), true, 8388352)
            .addAction((DrinkAction) new DrinkActionFood(2, 0.2f));
        orange = new Ingredient(7, new ItemStack((Item) TCItemRegistry.orange), true, 16753920)
            .addAction((DrinkAction) new DrinkActionFood(3, 0.2f));
        grapefruit = new Ingredient(8, new ItemStack((Item) TCItemRegistry.grapefruit), true, 16737095)
            .addAction((DrinkAction) new DrinkActionFood(4, 0.2f));
        pineapple = new Ingredient(9, new ItemStack((Block) TCBlockRegistry.pineapple, 1, 0), true, 15662848)
            .addAction((DrinkAction) new DrinkActionFood(1, 0.1f));
        pineappleChunks = new Ingredient(10, new ItemStack((Item) TCItemRegistry.pineappleCubes), false, 15662848, 0.1f)
            .addAction((DrinkAction) new DrinkActionFood(1, 0.1f));
        coconut = new Ingredient(11, new ItemStack((Block) TCBlockRegistry.coconut), true, 15724527)
            .addAction((DrinkAction) new DrinkActionFood(1, 0.1f));
        coconutChunk = new Ingredient(12, new ItemStack((Item) TCItemRegistry.coconutChunk), false, 15724527, 0.1f)
            .addAction((DrinkAction) new DrinkActionFood(1, 0.1f));
        sugarcane = new Ingredient(13, new ItemStack(Items.reeds), false, 11665259, 0.1f);
        roastedCoffeeBean = new Ingredient(
            14,
            new ItemStack((Item) TCItemRegistry.coffeeBean, 1, 1),
            false,
            6833196,
            0.95f).addAction((DrinkAction) new DrinkActionFood(4, 0.2f))
                .addAction((DrinkAction) new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
        waterBucket = new Ingredient(15, new ItemStack((Item) TCItemRegistry.bucketTropicsWater), true, 16777215);
        milkBucket = new Ingredient(16, new ItemStack(Items.milk_bucket), false, 16777215, 0.1f)
            .addAction((DrinkAction) new DrinkActionFood(2, 0.2f));
        cocoaBean = new Ingredient(17, new ItemStack(Items.dye, 1, 3), false, 8411710, 0.95f)
            .addAction((DrinkAction) new DrinkActionFood(4, 0.2f));
    }
}
