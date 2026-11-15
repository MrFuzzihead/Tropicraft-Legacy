package net.tropicraft.economy;

import net.minecraft.item.ItemStack;

public class ItemEntry {

    public ItemStack item;
    public int value;
    public boolean buyable;
    public boolean compareNBT;
    public boolean compareDamage;

    public ItemEntry(final ItemStack parItem, final int parValue) {
        this.item = null;
        this.value = 0;
        this.buyable = false;
        this.compareNBT = false;
        this.compareDamage = false;
        this.item = parItem;
        this.value = parValue;
    }

    public boolean matchesItem(final ItemStack parItem) {
        if (parItem == null || this.item == null) {
            return false;
        }
        // The last term used to be `this.item == parItem`, i.e. object identity. The entry holds a registered
        // prototype stack, which is never the object a player is holding, so no lookup ever matched and the whole
        // Koa trade economy silently did nothing.
        return (!this.compareNBT || this.item.isItemEqual(parItem))
            && (!this.compareDamage || this.item.getItemDamage() == parItem.getItemDamage())
            && this.item.getItem() == parItem.getItem();
    }

    public int getTotalValue(final ItemStack parItem) {
        if (parItem == null) {
            return 0;
        }
        double val = this.value;
        final double priceFactor = this.value / Math.max(1, this.item.stackSize);
        if (!this.compareDamage && parItem.getMaxDamage() != 0) {
            // Damageable items are worth their remaining durability fraction.
            val *= (parItem.getMaxDamage() - parItem.getItemDamage()) / (double) parItem.getMaxDamage();
        }
        if (parItem.stackSize > 1) {
            val = priceFactor * parItem.stackSize;
        }
        return (int) val;
    }
}
