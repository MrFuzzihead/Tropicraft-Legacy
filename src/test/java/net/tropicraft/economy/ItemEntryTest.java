package net.tropicraft.economy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers {@link ItemEntry}, the rules the Koa trading economy uses to decide whether a player is offering something
 * it wants and what it is worth.
 * <p>
 * The bug this suite exists for: {@code matchesItem} ended with {@code this.item == parItem}, comparing the
 * registered prototype stack against the player's stack by identity. That is never true, so no trade could ever be
 * accepted and the whole economy was inert - silently, because the caller swallowed the exception the failed
 * lookups caused downstream.
 * <p>
 * {@code new Item()} is enough here: on Forge 1.7.10 an {@code ItemStack} resolves its item through
 * {@code Item.delegate}, which the {@code Item} constructor populates from the item registry, so a plain
 * unregistered item behaves like a registered one for identity, metadata and durability.
 */
public class ItemEntryTest {

    private static Item item() {
        return new Item();
    }

    private static Item damageableItem(final int maxDamage) {
        final Item item = new Item();
        item.setMaxDamage(maxDamage);
        return item;
    }

    @Test
    public void matchesADifferentStackHoldingTheSameItem() {
        final Item item = item();
        final ItemEntry entry = new ItemEntry(new ItemStack(item), 5);
        Assert.assertTrue(
            "a player-held stack is never the registered prototype instance",
            entry.matchesItem(new ItemStack(item)));
    }

    @Test
    public void doesNotMatchADifferentItem() {
        final ItemEntry entry = new ItemEntry(new ItemStack(item()), 5);
        Assert.assertFalse(entry.matchesItem(new ItemStack(item())));
    }

    @Test
    public void doesNotMatchNull() {
        final ItemEntry entry = new ItemEntry(new ItemStack(item()), 5);
        Assert.assertFalse(entry.matchesItem(null));
    }

    @Test
    public void ignoresMetadataUnlessItIsToldToCompareIt() {
        final Item item = item();
        final ItemEntry loose = new ItemEntry(new ItemStack(item, 1, 0), 5);
        Assert.assertTrue(loose.matchesItem(new ItemStack(item, 1, 3)));

        final ItemEntry strict = new ItemEntry(new ItemStack(item, 1, 0), 5);
        strict.compareDamage = true;
        Assert.assertFalse(strict.matchesItem(new ItemStack(item, 1, 3)));
        Assert.assertTrue(strict.matchesItem(new ItemStack(item, 1, 0)));
    }

    @Test
    public void totalValueOfASingleItemIsItsValue() {
        final ItemEntry entry = new ItemEntry(new ItemStack(item()), 7);
        Assert.assertEquals(7, entry.getTotalValue(new ItemStack(item(), 1)));
    }

    @Test
    public void totalValueScalesWithTheStackSize() {
        // A stack of four is worth eight, so five are worth ten.
        final Item item = item();
        final ItemEntry entry = new ItemEntry(new ItemStack(item, 4), 8);
        Assert.assertEquals(10, entry.getTotalValue(new ItemStack(item, 5)));
    }

    @Test
    public void totalValueOfADamageableItemUsesTheRemainingDurability() {
        final Item item = damageableItem(100);
        final ItemEntry entry = new ItemEntry(new ItemStack(item), 100);
        Assert.assertEquals(50, entry.getTotalValue(new ItemStack(item, 1, 50)));
    }

    @Test
    public void totalValueOfAFullyRepairedItemIsItsFullValue() {
        final Item item = damageableItem(100);
        final ItemEntry entry = new ItemEntry(new ItemStack(item), 100);
        Assert.assertEquals(100, entry.getTotalValue(new ItemStack(item, 1, 0)));
    }

    @Test
    public void totalValueOfNullIsZero() {
        Assert.assertEquals(0, new ItemEntry(new ItemStack(item()), 7).getTotalValue(null));
    }
}
