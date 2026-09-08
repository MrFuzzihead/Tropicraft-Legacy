package net.tropicraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.tropicraft.curare.CurareMix;
import net.tropicraft.curare.CurareMixRegistry;
import net.tropicraft.curare.CurareType;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCurare extends ItemTropicraft {

    public static final String[] effectNames;
    private static final int[] colors;
    private static final String[] tooltipText;
    @SideOnly(Side.CLIENT)
    private IIcon overlay;
    @SideOnly(Side.CLIENT)
    private IIcon backImage;

    public ItemCurare() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < ItemCurare.effectNames.length; ++i) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack, final EntityPlayer ent, final List list, final boolean wat) {
        list.clear();
        list.add(
            ItemCurare.tooltipText[itemstack.getItemDamage()]
                + TropicraftUtils
                    .translate("dart.tropicraft:" + ItemCurare.effectNames[itemstack.getItemDamage()] + ".name")
                + " "
                + TropicraftUtils.translate("item.tropicraft:curare.name"));
        list.add(TropicraftUtils.translate("curare.tropicraft:mix.name") + ":");
        int count = 0;
        int totalcount = 0;
        int previous = Integer.MIN_VALUE;
        final CurareMix mix = CurareMixRegistry.getInstance()
            .getCurareMixFromType(CurareType.getCurareFromDamage(itemstack.getItemDamage()));
        if (mix == null) {
            list.add(TropicraftUtils.translate("curare.tropicraft:nomix.name"));
            return;
        }
        final int[] arr$;
        final int[] damages = arr$ = mix.getSortedDamageVals();
        for (final int i : arr$) {
            ++totalcount;
            if (previous == Integer.MIN_VALUE || previous == i) {
                previous = i;
                ++count;
            } else {
                ItemStack flower = new ItemStack((Block) TCBlockRegistry.flowers, 1, previous);
                list.add(
                    count + " " + ColorHelper.color(this.getColorValueFromDamage(previous)) + flower.getDisplayName());
                previous = i;
                count = 1;
                if (totalcount == damages.length) {
                    flower = new ItemStack((Block) TCBlockRegistry.flowers, 1, i);
                    list.add(
                        count + " " + ColorHelper.color(this.getColorValueFromDamage(i)) + flower.getDisplayName());
                }
            }
        }
    }

    private int getColorValueFromDamage(final int damage) {
        switch (damage) {
            case 0: {
                return 9;
            }
            case 1: {
                return 6;
            }
            case 2: {
                return 13;
            }
            case 3: {
                return 14;
            }
            case 4: {
                return 15;
            }
            case 7: {
                return 7;
            }
            case 8: {
                return 10;
            }
            case 9: {
                return 5;
            }
            default: {
                return 15;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int par1, final int par2) {
        return (par2 > 0) ? this.overlay : this.backImage;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
        return (par2 == 0) ? 65280 : ItemCurare.colors[par1ItemStack.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(final IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        this.overlay = par1IconRegister.registerIcon("tropicraft:curareOverlay");
        this.backImage = par1IconRegister.registerIcon("tropicraft:curare");
    }

    static {
        effectNames = new String[] { "paralyze", "poison", "moveSlowdown", "harm", "confusion", "hunger", "weakness" };
        colors = new int[] { 16758271, 2461347, 13621209, 14672442, 5322747, 14754852, 7129087 };
        tooltipText = new String[] { "§d", "§3", "§7", "§6", "§1", "§4", "§9" };
    }
}
