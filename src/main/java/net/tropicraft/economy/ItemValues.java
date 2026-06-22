package net.tropicraft.economy;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class ItemValues {

    public static ArrayList<ItemEntry> items;
    public static ArrayList<ItemEntry> itemsBuyable;

    public static void addEntry(final ItemStack parItem, final int value) {
        addEntry(parItem, value, false);
    }

    public static void addEntryBuyable(final ItemStack parItem, final int value) {
        addEntry(parItem, value, true, false);
    }

    public static void addEntry(final ItemStack parItem, final int value, final boolean compareDamage) {
        addEntry(parItem, value, false, compareDamage);
    }

    public static void addEntryBuyable(final ItemStack parItem, final int value, final boolean compareDamage) {
        addEntry(parItem, value, true, compareDamage);
    }

    public static void addEntry(final ItemStack parItem, final int value, final boolean buyable,
        final boolean compareDamage) {
        final ItemEntry ie = new ItemEntry(parItem, value);
        ie.compareDamage = compareDamage;
        ItemValues.items.add(ie);
        if (buyable) {
            ItemValues.itemsBuyable.add(ie);
        }
    }

    public static boolean getIsValuedItem(final ItemStack parItem) {
        return getItemEntry(parItem, false) != null;
    }

    public static boolean getIsBuyableItem(final ItemStack parItem) {
        return getItemEntry(parItem, true) != null;
    }

    public static ItemEntry getItemEntry(final ItemStack parItem) {
        return getItemEntry(parItem, false);
    }

    public static ItemEntry getItemEntry(final ItemStack parItem, final boolean parNeedsBuyable) {
        try {
            for (int i = 0; i < ItemValues.items.size(); ++i) {
                final ItemEntry ie = ItemValues.items.get(i);
                if (ie.matchesItem(parItem) && (!parNeedsBuyable || ie.buyable)) {
                    return ie;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    static {
        ItemValues.items = new ArrayList<ItemEntry>();
        ItemValues.itemsBuyable = new ArrayList<ItemEntry>();
    }
}
