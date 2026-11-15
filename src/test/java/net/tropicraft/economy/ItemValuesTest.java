package net.tropicraft.economy;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Covers {@link ItemValues}, the lookup the trade jobs and the purchase plate go through.
 * <p>
 * {@code items}/{@code itemsBuyable} are process-wide state populated at mod init, so every test restores the lists
 * it started with.
 */
public class ItemValuesTest {

    private ArrayList<ItemEntry> originalItems;
    private ArrayList<ItemEntry> originalBuyable;

    @Before
    public void snapshotEntries() {
        this.originalItems = new ArrayList<ItemEntry>(ItemValues.items);
        this.originalBuyable = new ArrayList<ItemEntry>(ItemValues.itemsBuyable);
    }

    @After
    public void restoreEntries() {
        ItemValues.items.clear();
        ItemValues.items.addAll(this.originalItems);
        ItemValues.itemsBuyable.clear();
        ItemValues.itemsBuyable.addAll(this.originalBuyable);
    }

    private static Item item() {
        return new Item();
    }

    @Test
    public void findsTheEntryForAPlayerHeldStack() {
        final Item item = item();
        final ItemEntry entry = new ItemEntry(new ItemStack(item), 4);
        ItemValues.items.add(entry);
        Assert.assertSame(entry, ItemValues.getItemEntry(new ItemStack(item)));
    }

    @Test
    public void aMetadataSensitiveEntryOnlyMatchesItsOwnMetadata() {
        final Item item = item();
        final ItemEntry entry = new ItemEntry(new ItemStack(item, 1, 1), 2);
        entry.compareDamage = true;
        ItemValues.items.add(entry);
        Assert.assertSame(entry, ItemValues.getItemEntry(new ItemStack(item, 1, 1)));
        Assert.assertNull(ItemValues.getItemEntry(new ItemStack(item, 1, 0)));
    }

    @Test
    public void returnsNullForAnUnvaluedItem() {
        final ItemEntry entry = new ItemEntry(new ItemStack(item()), 4);
        ItemValues.items.add(entry);
        Assert.assertNull(ItemValues.getItemEntry(new ItemStack(item())));
        Assert.assertFalse(ItemValues.getIsValuedItem(new ItemStack(item())));
    }

    @Test
    public void returnsNullForANullStack() {
        Assert.assertNull(ItemValues.getItemEntry(null));
        Assert.assertFalse(ItemValues.getIsValuedItem(null));
        Assert.assertFalse(ItemValues.getIsBuyableItem(null));
    }

    @Test
    public void buyableLookupsOnlySeeBuyableEntries() {
        final Item valued = item();
        final Item buyable = item();
        ItemValues.addEntry(new ItemStack(valued), 1);
        ItemValues.addEntryBuyable(new ItemStack(buyable), 2);
        Assert.assertTrue(ItemValues.getIsValuedItem(new ItemStack(buyable)));
        Assert.assertTrue(ItemValues.getIsBuyableItem(new ItemStack(buyable)));
        Assert.assertFalse(ItemValues.getIsBuyableItem(new ItemStack(valued)));
    }

    @Test
    public void addEntryBuyablePutsTheEntryInBothLists() {
        final Item item = item();
        ItemValues.addEntryBuyable(new ItemStack(item), 3);
        final int buyableCount = ItemValues.itemsBuyable.size();
        Assert.assertTrue(ItemValues.getIsBuyableItem(new ItemStack(item)));
        Assert.assertEquals(buyableCount, ItemValues.itemsBuyable.size());
        Assert.assertTrue(ItemValues.itemsBuyable.contains(ItemValues.getItemEntry(new ItemStack(item), true)));
    }
}
