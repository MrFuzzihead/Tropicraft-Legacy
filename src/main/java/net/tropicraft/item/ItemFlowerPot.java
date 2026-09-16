package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlowerPot extends ItemBlock {

    public ItemFlowerPot(final Block block) {
        super(block);
    }

    /**
     * ItemBlock defaults to the block texture sheet (0), which would make Minecraft register this
     * item's icon against the block sheet and draw it from the block sheet. This item has its own
     * texture in /textures/items, so it has to report the item texture sheet (1).
     */
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister registry) {
        this.itemIcon = registry.registerIcon("tropicraft:flowerPot");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int damage) {
        return this.itemIcon;
    }
}
