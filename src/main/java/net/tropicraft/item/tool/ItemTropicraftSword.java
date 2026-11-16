package net.tropicraft.item.tool;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftSword extends ItemSword {

    public ItemTropicraftSword(final Item.ToolMaterial material, final String textureName) {
        super(material);
        this.setTextureName(textureName);
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    public String getUnlocalizedName() {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    public String getUnlocalizedName(final ItemStack itemStack) {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("tropicraft:tools/" + this.getIconString());
    }
}
