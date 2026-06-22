package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftSlab extends BlockSlab {

    public static Block[] modelBlocks;
    @SideOnly(Side.CLIENT)
    private IIcon bambooIcon;
    @SideOnly(Side.CLIENT)
    private IIcon thatchIcon;
    @SideOnly(Side.CLIENT)
    private IIcon chunkIcon;
    @SideOnly(Side.CLIENT)
    private IIcon palmIcon;

    public BlockTropicraftSlab(final boolean isFullSlab) {
        super(isFullSlab, Material.rock);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        final int type = metadata & 0x7;
        switch (type) {
            case 0: {
                return TCBlockRegistry.bambooBundle.getBlockTextureFromSide(side);
            }
            case 1: {
                return TCBlockRegistry.thatchBundle.getBlockTextureFromSide(side);
            }
            case 2: {
                return TCBlockRegistry.chunkOHead.getBlockTextureFromSide(side);
            }
            case 3: {
                return TCBlockRegistry.planks.getIcon(side, metadata);
            }
            default: {
                return TCBlockRegistry.bambooBundle.getBlockTextureFromSide(side);
            }
        }
    }

    public float getPlayerRelativeBlockHardness(final EntityPlayer par1EntityPlayer, final World par2World, final int j,
        final int k, final int l) {
        final int type = par2World.getBlockMetadata(j, k, l) & 0x7;
        if (type < BlockTropicraftSlab.modelBlocks.length) {
            return BlockTropicraftSlab.modelBlocks[type]
                .getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, j, k, l);
        }
        return super.getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, j, k, l);
    }

    public boolean canHarvestBlock(final EntityPlayer player, final int meta) {
        final int type = meta & 0x7;
        if (type < BlockTropicraftSlab.modelBlocks.length) {
            return BlockTropicraftSlab.modelBlocks[type].canHarvestBlock(player, meta);
        }
        return super.canHarvestBlock(player, meta);
    }

    protected ItemStack createStackedBlock(final int meta) {
        return new ItemStack((Block) TCBlockRegistry.singleSlabs, 2, meta & 0x7);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {}

    public String func_150002_b(int meta) {
        if (meta < 0 || meta >= TCNames.slabTypes.length) {
            meta = 0;
        }
        return this.getUnlocalizedName() + "_" + TCNames.slabTypes[meta];
    }

    public Item getItemDropped(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return Item.getItemFromBlock((Block) TCBlockRegistry.singleSlabs);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        if (item != Item.getItemFromBlock((Block) TCBlockRegistry.doubleSlabs)) {
            for (int i = 0; i < TCNames.slabTypes.length; ++i) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World p_149694_1_, final int p_149694_2_, final int p_149694_3_, final int p_149694_4_) {
        return Item.getItemFromBlock((Block) TCBlockRegistry.singleSlabs);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    static {
        BlockTropicraftSlab.modelBlocks = new Block[] { (Block) TCBlockRegistry.bambooBundle,
            (Block) TCBlockRegistry.thatchBundle, (Block) TCBlockRegistry.chunkOHead,
            (Block) TCBlockRegistry.palmStairs };
    }
}
