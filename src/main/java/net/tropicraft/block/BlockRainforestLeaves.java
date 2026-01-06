package net.tropicraft.block;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;

public class BlockRainforestLeaves extends BlockLeaves {

    public static final String[][] imageNames;
    public static final String[] treeNames;
    @SideOnly(Side.CLIENT)
    protected IIcon[][] icons;

    public BlockRainforestLeaves() {
        this.disableStats();
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT) {
            this.icons = new IIcon[2][];
        }
    }

    public int damageDropped(final int meta) {
        return meta & 0x3;
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(final int p_149741_1_) {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess p_149720_1_, final int p_149720_2_, final int p_149720_3_,
        final int p_149720_4_) {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    /*
     * The Server only annotation made the leaves drop birch saplings in offline worlds.
     * So I disabled it.
     */
    @Override
    // @SideOnly(Side.SERVER)
    public Item getItemDropped(final int metadata, final Random random, final int j) {
        return Item.getItemFromBlock(TCBlockRegistry.saplings);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int id, final int meta) {
        if ((meta & 0x3) == 0x2) {
            return this.icons[this.field_150127_b][2];
        }
        if ((meta & 0x3) == 0x1) {
            return this.icons[this.field_150127_b][1];
        }
        return this.icons[this.field_150127_b][0];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.icons = new IIcon[2][BlockRainforestLeaves.treeNames.length];
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < BlockRainforestLeaves.treeNames.length; ++j) {
                this.icons[i][j] = iconRegister.registerIcon("tropicraft:" + BlockRainforestLeaves.imageNames[j][i]);
            }
        }
    }

    public String[] func_150125_e() {
        return BlockRainforestLeaves.treeNames;
    }

    static {
        imageNames = new String[][] { { "leafKapok", "leafMahogany" }, { "leafKapok", "leafMahoganyFast" },
            { "leafFruit", "leafFruitFast" } };
        treeNames = new String[] { "kapok", "mahogany", "fruit" };
    }
}
