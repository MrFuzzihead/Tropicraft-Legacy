package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftColored extends ItemTropicraft {

    @SideOnly(Side.CLIENT)
    private IIcon overlayIcon;

    public ItemTropicraftColored() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        this.itemIcon = par1IconRegister.registerIcon("tropicraft:" + this.getIconString());
        this.overlayIcon = par1IconRegister.registerIcon("tropicraft:" + this.getIconString() + "Inverted");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        return (pass > 0) ? this.overlayIcon : super.getIconFromDamageForRenderPass(damage, pass);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack itemstack, final int pass) {
        final Integer color = ColorHelper.getColorFromDamage(itemstack.getItemDamage());
        return (pass == 0) ? 16777215 : color;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < ColorHelper.getNumColors(); ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public String getItemStackDisplayName(final ItemStack itemstack) {
        final String name = ("" + StatCollector.translateToLocal(
            this.getUnlocalizedName()
                .replace("item.", String.format("item.%s:", "tropicraft"))
                .split(":")[0] + ":"
                + this.iconString
                + "_"
                + ItemDye.field_150923_a[itemstack.getItemDamage()]
                + ".name")).trim();
        return name;
    }
}
