package net.tropicraft.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class Encyclopedia extends TropicalBook {

    private HashMap<ItemStack, List<ShapedRecipes>> recipes;
    private HashMap<String, List<ItemStack>> itemEntries;

    public Encyclopedia(final String savedDataFile, final String contentsFile, final String outsideTexture,
        final String insideTexture) {
        super(savedDataFile, contentsFile, outsideTexture, insideTexture);
        this.recipes = new HashMap<ItemStack, List<ShapedRecipes>>();
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
        final Set<ItemStack> recipeContents = new HashSet<ItemStack>();
        this.addItemToRecipeContents(recipeContents, result);
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
                itemStack = new ItemStack((Item) aobj[i + 1]);
            } else if (aobj[i + 1] instanceof Block) {
                itemStack = new ItemStack((Block) aobj[i + 1], 1, -1);
            } else if (aobj[i + 1] instanceof ItemStack) {
                itemStack = (ItemStack) aobj[i + 1];
            }
            charMap.put(itemChar, itemStack);
            this.addItemToRecipeContents(recipeContents, itemStack);
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
        for (final ItemStack item : recipeContents) {
            boolean foundKey = false;
            for (final ItemStack key : this.recipes.keySet()) {
                if (item.isItemEqual(key)) {
                    foundKey = true;
                    this.recipes.get(key)
                        .add(recipe);
                    break;
                }
            }
            if (!foundKey) {
                this.recipes.put(item, new ArrayList<ShapedRecipes>());
                this.recipes.get(item)
                    .add(recipe);
            }
        }
    }

    public List<ShapedRecipes> getRecipesForEntry(final int page) {
        final List<ItemStack> entryItems = this.itemEntries.get(this.getPageName(page));
        final List<ShapedRecipes> recipeList = new ArrayList<ShapedRecipes>();
        if (entryItems != null) {
            for (final ItemStack item : entryItems) {
                for (final ItemStack recipeItem : this.recipes.keySet()) {
                    if (recipeItem.isItemEqual(item)) {
                        final List<ShapedRecipes> itemRecipes = this.recipes.get(recipeItem);
                        if (itemRecipes == null) {
                            continue;
                        }
                        recipeList.addAll(itemRecipes);
                    }
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

    private void addItemToRecipeContents(final Set<ItemStack> items, final ItemStack i) {
        boolean shouldAdd = !items.contains(i);
        for (final ItemStack listItem : items) {
            if (listItem.isItemEqual(i)) {
                shouldAdd = false;
                break;
            }
        }
        if (shouldAdd) {
            items.add(i);
        }
    }

    public RecipeEntry getFormattedRecipe(final ShapedRecipes recipe) {
        try {
            final int width = recipe.recipeWidth;
            final int height = recipe.recipeHeight;
            final ItemStack[] items = recipe.recipeItems;
            final ItemStack output = recipe.getRecipeOutput();
            return new RecipeEntry(width, height, items, output);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
