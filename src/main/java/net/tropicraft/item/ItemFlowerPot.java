package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemReed;

public class ItemFlowerPot extends ItemReed {

    public ItemFlowerPot(final Block block) {
        super(block);
    }

    public void registerIcons(final IIconRegister registry) {
        this.itemIcon = registry.registerIcon("tropicraft:flowerPot");
    }
}
