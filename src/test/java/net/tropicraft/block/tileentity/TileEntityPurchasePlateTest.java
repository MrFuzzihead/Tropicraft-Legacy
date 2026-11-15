package net.tropicraft.block.tileentity;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Covers the purchase plate's saved {@code itemIndex}.
 * <p>
 * The index is written to NBT and then used directly by {@code JobTrade.tradeConfirmCallback} and by three renderer
 * call sites as {@code ItemValues.itemsBuyable.get(itemIndex)}, so a save written by a version with a different
 * trade list - or a hand-edited one - used to crash the client. The plate now clamps it on load.
 * <p>
 * {@link NBTTagCompound} is mocked rather than built up tag by tag: the point of the test is the clamp, not NBT
 * serialisation, and the inline mock maker keeps the production class untouched.
 */
public class TileEntityPurchasePlateTest {

    private ArrayList<ItemEntry> originalItems;
    private ArrayList<ItemEntry> originalBuyable;

    @Before
    public void snapshotEntries() {
        this.originalItems = new ArrayList<ItemEntry>(ItemValues.items);
        this.originalBuyable = new ArrayList<ItemEntry>(ItemValues.itemsBuyable);
        ItemValues.addEntryBuyable(new ItemStack(new Item()), 1);
    }

    @After
    public void restoreEntries() {
        ItemValues.items.clear();
        ItemValues.items.addAll(this.originalItems);
        ItemValues.itemsBuyable.clear();
        ItemValues.itemsBuyable.addAll(this.originalBuyable);
    }

    private static NBTTagCompound tagWithIndex(final int index) {
        final NBTTagCompound tag = Mockito.mock(NBTTagCompound.class);
        Mockito.when(tag.getInteger("itemIndex"))
            .thenReturn(index);
        return tag;
    }

    @Test
    public void clampsAnIndexPastTheEndOfTheTradeList() {
        final TileEntityPurchasePlate plate = new TileEntityPurchasePlate();
        plate.readFromNBT(tagWithIndex(ItemValues.itemsBuyable.size() + 50));
        Assert.assertEquals(0, plate.itemIndex);
    }

    @Test
    public void clampsANegativeIndex() {
        final TileEntityPurchasePlate plate = new TileEntityPurchasePlate();
        plate.readFromNBT(tagWithIndex(-4));
        Assert.assertEquals(0, plate.itemIndex);
    }

    @Test
    public void keepsAValidIndex() {
        final TileEntityPurchasePlate plate = new TileEntityPurchasePlate();
        plate.readFromNBT(tagWithIndex(0));
        Assert.assertEquals(0, plate.itemIndex);
    }
}
