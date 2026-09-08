package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDart extends ItemTropicraft {

    public static final String[] dartNames;
    private static final int[] colors;
    private static final String[] tooltipText;
    @SideOnly(Side.CLIENT)
    private IIcon overlay;
    @SideOnly(Side.CLIENT)
    private IIcon backImage;

    public ItemDart() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
        this.setMaxStackSize(64);
    }

    public void getSubItems(final Item item, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < ItemDart.dartNames.length; ++i) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int par1, final int par2) {
        return (par2 > 0) ? this.overlay : this.getIconFromDamage(par1);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
        return ItemDart.colors[par1ItemStack.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack, final EntityPlayer ent, final List list, final boolean wat) {
        list.clear();
        list.add(
            ItemDart.tooltipText[itemstack.getItemDamage()]
                + StatCollector.translateToLocal("dart.tropicraft:prefix.name")
                + " "
                + StatCollector
                    .translateToLocal("dart.tropicraft:" + ItemDart.dartNames[itemstack.getItemDamage()] + ".name"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(final IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        this.overlay = par1IconRegister.registerIcon("tropicraft:dartOverlay");
        this.backImage = par1IconRegister.registerIcon("tropicraft:dart");
    }

    static {
        dartNames = new String[] { "paralyze", "poison", "moveSlowdown", "harm", "confusion", "hunger", "weakness" };
        colors = new int[] { 16758271, 2461347, 13621209, 14672442, 5322747, 14754852, 7129087 };
        tooltipText = new String[] { "§d", "§3", "§7", "§6", "§1", "§4", "§9" };
    }
}
