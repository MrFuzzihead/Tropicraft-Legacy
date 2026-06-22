package net.tropicraft.registry;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.info.TCNames;

public class TCKoaCurrencyRegistry {

    public static ItemStack currency;

    public static void init() {
        TCKoaCurrencyRegistry.currency = new ItemStack((Item) TCItemRegistry.pearl, 0, 1);
        addBuyableEntries();
        addValuedEntries();
    }

    public static void addBuyableEntries() {
        ItemValues.addEntryBuyable(new ItemStack(Items.fish), 1);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.freshMarlin), 3);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.scaleChestplate), 15);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.scaleLeggings), 10);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.scaleHelmet), 5);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.scaleBoots), 5);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.coconutBomb, 3), 15);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.blowGun), 20);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.dart, 10), 30, true);
        ItemValues.addEntryBuyable(new ItemStack((Item) TCItemRegistry.fishingNet), 15);
    }

    public static void addValuedEntries() {
        ItemValues.addEntry(TCKoaCurrencyRegistry.currency, 1);
        for (int i = 0; i < TCNames.shellNames.length; ++i) {
            ItemValues.addEntry(new ItemStack(TCItemRegistry.shells, 1, i), 1, true);
        }
    }
}
