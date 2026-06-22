package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPalmLeaves extends BlockLeaves {

    public BlockPalmLeaves() {
        this.disableStats();
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(final int i) {
        return 4764952;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        return 4764952;
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    public int quantityDropped(final Random random) {
        return (random.nextInt(20) == 0) ? 1 : 0;
    }

    public Item getItemDropped(final int metadata, final Random random, final int j) {
        return Item.getItemFromBlock((Block) TCBlockRegistry.saplings);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int var1, final int var2) {
        return this.blockIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        this.blockIcon = register.registerIcon("tropicraft:leafPalm");
    }

    public String[] func_150125_e() {
        return new String[] { "palm" };
    }
}
