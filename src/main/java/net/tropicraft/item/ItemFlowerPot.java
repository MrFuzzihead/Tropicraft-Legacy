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

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister registry) {
        this.itemIcon = registry.registerIcon("tropicraft:flowerPot");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int damage) {
        return this.itemIcon;
    }
}
