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
        return (!this.compareNBT || this.item.isItemEqual(parItem))
            && (!this.compareDamage || this.item.getItemDamage() == parItem.getItemDamage())
            && this.item == parItem;
    }

    public int getTotalValue(final ItemStack parItem) {
        double val = this.value;
        final double priceFactor = this.value / Math.max(1, this.item.stackSize);
        if (!this.compareDamage && parItem.getMaxDamage() != 0) {
            val *= (parItem.getMaxDamage() - parItem.getItemDamage()) / (double) parItem.getMaxDamage();
        } else if (this.item.stackSize > 1) {}
        if (parItem.stackSize > 1) {
            val = priceFactor * parItem.stackSize;
        }
        return (int) val;
    }
}
