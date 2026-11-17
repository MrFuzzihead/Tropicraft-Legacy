package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBambooChute extends ItemReed {

    public ItemBambooChute(final Block block) {
        super(block);
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
    }

    public String getUnlocalizedName() {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    public String getUnlocalizedName(final ItemStack itemStack) {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1));
    }
}
