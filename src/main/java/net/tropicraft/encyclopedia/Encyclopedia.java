package net.tropicraft.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class Encyclopedia extends TropicalBook {

    /**
     * Identity of an encyclopedia entry: the item plus its damage value, which is exactly what
     * {@code ItemStack.isItemEqual} compares.
     * <p>
     * The recipe map used to be keyed by {@code ItemStack} itself. In 1.7.10 {@code ItemStack} does not override
     * {@code equals}/{@code hashCode}, so every map and set that stored one was keyed by identity, and every lookup had
     * to fall back to a linear {@code isItemEqual} scan over the keys (two of them per rendered recipe page) or key by
     * an object that could never be found again.
     * </p>
     */
    private static final class ItemKey {

        private final Item item;
        private final int damage;

        private ItemKey(final Item item, final int damage) {
            this.item = item;
            this.damage = damage;
        }

        static ItemKey of(final ItemStack stack) {
            return new ItemKey(stack.getItem(), stack.getItemDamage());
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ItemKey)) {
                return false;
            }
            final ItemKey key = (ItemKey) other;
            return this.item == key.item && this.damage == key.damage;
        }

        @Override
        public int hashCode() {
            return 31 * System.identityHashCode(this.item) + this.damage;
        }
    }

    private final Map<ItemKey, List<ShapedRecipes>> recipes;
    private final HashMap<String, List<ItemStack>> itemEntries;

    public Encyclopedia(final String savedDataFile, final String contentsFile, final String outsideTexture,
        final String insideTexture) {
        super(savedDataFile, contentsFile, outsideTexture, insideTexture);
        this.recipes = new HashMap<ItemKey, List<ShapedRecipes>>();
        this.itemEntries = new HashMap<String, List<ItemStack>>();
    }

    @Override
    public boolean hasRecipeList() {
        return true;
    }

    public void includeItem(final String itemname, final ItemStack item) {
        if (!this.itemEntries.containsKey(itemname)) {
            this.itemEntries.put(itemname, new ArrayList<ItemStack>());
        }
        this.itemEntries.get(itemname)
            .add(item);
    }

    public void includeRecipe(final ItemStack result, final Object[] aobj) {
        final Map<ItemKey, ItemStack> recipeContents = new LinkedHashMap<ItemKey, ItemStack>();
        addItemToRecipeContents(recipeContents, result);
        String recipeString = "";
        int i = 0;
        int width = 0;
        int height = 0;
        if (aobj[i] instanceof String[]) {
            final String[] cols = (String[]) aobj[i++];
            for (int j = 0; j < cols.length; ++j) {
                final String row = cols[j];
                ++height;
                width = row.length();
                recipeString += row;
            }
        } else {
            while (aobj[i] instanceof String) {
                final String row2 = (String) aobj[i++];
                ++height;
                width = row2.length();
                recipeString += row2;
            }
        }
        final HashMap<Character, ItemStack> charMap = new HashMap<Character, ItemStack>();
        while (i < aobj.length) {
            final Character itemChar = (Character) aobj[i];
            ItemStack itemStack = null;
            if (aobj[i + 1] instanceof Item) {
                // The casts are required: the recipe array is Object[], and ItemStack has separate Item and Block
                // constructors for exactly this vanilla format.
                itemStack = new ItemStack((Item) aobj[i + 1]);
            } else if (aobj[i + 1] instanceof Block) {
                itemStack = new ItemStack((Block) aobj[i + 1], 1, -1);
            } else if (aobj[i + 1] instanceof ItemStack) {
                itemStack = (ItemStack) aobj[i + 1];
            }
            charMap.put(itemChar, itemStack);
            addItemToRecipeContents(recipeContents, itemStack);
            i += 2;
        }
        final ItemStack[] slotArray = new ItemStack[width * height];
        for (int slots = 0; slots < width * height; ++slots) {
            final char itemChar2 = recipeString.charAt(slots);
            if (charMap.containsKey(itemChar2)) {
                slotArray[slots] = charMap.get(itemChar2)
                    .copy();
            } else {
                slotArray[slots] = null;
            }
        }
        final ShapedRecipes recipe = new ShapedRecipes(width, height, slotArray, result);
        for (final ItemStack item : recipeContents.values()) {
            final ItemKey key = ItemKey.of(item);
            List<ShapedRecipes> recipesForItem = this.recipes.get(key);
            if (recipesForItem == null) {
                recipesForItem = new ArrayList<ShapedRecipes>();
                this.recipes.put(key, recipesForItem);
            }
            recipesForItem.add(recipe);
        }
    }

    public List<ShapedRecipes> getRecipesForEntry(final int page) {
        final List<ItemStack> entryItems = this.itemEntries.get(this.getPageName(page));
        final List<ShapedRecipes> recipeList = new ArrayList<ShapedRecipes>();
        if (entryItems != null) {
            for (final ItemStack item : entryItems) {
                final List<ShapedRecipes> recipesForItem = this.recipes.get(ItemKey.of(item));
                if (recipesForItem != null) {
                    recipeList.addAll(recipesForItem);
                }
            }
        }
        return recipeList;
    }

    @Override
    public int getContentPageCount(final int page, final ContentMode mode) {
        if (page >= 0 && page < this.getPageCount()) {
            if (mode == ContentMode.INFO) {
                return 1;
            }
            if (mode == ContentMode.RECIPE) {
                final List<ShapedRecipes> recipeList = this.getRecipesForEntry(page);
                if (recipeList != null) {
                    return recipeList.size();
                }
            }
        }
        return 0;
    }

    @Override
    public int entriesPerContentPage(final ContentMode mode) {
        if (mode == ContentMode.RECIPE) {
            return 3;
        }
        return super.entriesPerContentPage(mode);
    }

    @Override
    public boolean hasIndexIcons() {
        return true;
    }

    @Override
    public ItemStack getPageItemStack(final int page) {
        if (page >= 0 && page < this.getPageCount()) {
            final List<ItemStack> items = this.itemEntries.get(this.getPageName(page));
            if (items != null && !items.isEmpty()) {
                return items.get(0);
            }
        }
        return null;
    }

    @Override
    public void updatePagesFromInventory(final InventoryPlayer inv) {
        for (final ItemStack is : inv.mainInventory) {
            if (is != null) {
                final ItemStack comparison = new ItemStack(is.getItem(), 1, is.getItemDamage());
                for (final String entry : this.itemEntries.keySet()) {
                    if (!this.isPageVisible(entry)) {
                        final List<ItemStack> itemsInBook = this.itemEntries.get(entry);
                        for (final ItemStack itemInBook : itemsInBook) {
                            if (ItemStack.areItemStacksEqual(itemInBook, comparison)) {
                                this.markPageAsNewlyVisible(entry);
                            }
                        }
                    }
                }
            }
        }
    }

    /** Adds the stack under its item/damage key, keeping the first stack seen (and its display name). */
    private static void addItemToRecipeContents(final Map<ItemKey, ItemStack> contents, final ItemStack stack) {
        if (stack == null) {
            return;
        }
        final ItemKey key = ItemKey.of(stack);
        if (!contents.containsKey(key)) {
            contents.put(key, stack);
        }
    }

    public RecipeEntry getFormattedRecipe(final ShapedRecipes recipe) {
        return new RecipeEntry(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
    }

    public class RecipeEntry {

        public int width;
        public int height;
        public ItemStack[] ingredients;
        public ItemStack output;

        public RecipeEntry(final int width, final int height, final ItemStack[] ingredients, final ItemStack output) {
            this.width = width;
            this.height = height;
            this.ingredients = ingredients;
            this.output = output;
        }
    }
}
