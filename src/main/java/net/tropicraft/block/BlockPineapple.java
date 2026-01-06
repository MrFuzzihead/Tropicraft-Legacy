package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.info.*;
import net.tropicraft.registry.*;

import cpw.mods.fml.relauncher.*;

public class BlockPineapple extends BlockTallFlowers implements IGrowable {

    public static final int TOTAL_GROW_TICKS = 7;
    private static final int FULL_GROWTH = 8;

    public BlockPineapple(final String[] names) {
        super(names);
        this.setBlockName("pineapple");
        this.setBlockTextureName("tallFlower");
        this.setCreativeTab(TCCreativeTabRegistry.tabFood);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(final int side, final int meta) {
        return (meta <= 7) ? this.bottomIcon : this.topIcons[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.topIcons = new IIcon[this.names.length];
        for (int i = 0; i < this.names.length; ++i) {
            this.topIcons[i] = iconRegister
                .registerIcon(this.getActualName(this.getFormattedTextureName()) + "_" + TCNames.pineappleNames[i]);
        }
        this.bottomIcon = iconRegister.registerIcon(this.getActualName(this.getFormattedTextureName()) + "_" + "Stem");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    protected void checkAndDropBlock(final World p_149855_1_, final int p_149855_2_, final int p_149855_3_,
        final int p_149855_4_) {}

    public void onBlockHarvested(final World p_149681_1_, final int p_149681_2_, final int p_149681_3_,
        final int p_149681_4_, final int p_149681_5_, final EntityPlayer p_149681_6_) {}

    public void updateTick(final World world, final int i, final int j, final int k, final Random random) {
        this.checkFlowerChange(world, i, j, k);
        if (j > world.getHeight() - 2) {
            return;
        }
        final int meta = world.getBlockMetadata(i, j, k);
        if (world.getBlock(i, j, k) == this && meta < 7 && world.isAirBlock(i, j + 1, k)) {
            final int growth = meta;
            if (growth >= 6) {
                world.setBlock(i, j + 1, k, (Block) this, 8, 3);
                world.setBlockMetadataWithNotify(i, j, k, 7, 3);
            } else {
                world.setBlockMetadataWithNotify(i, j, k, growth + 1, 3);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase player,
        final ItemStack itemstack) {}

    @Override
    public Item getItemDropped(final int meta, final Random rand, final int unused) {
        return null;
    }

    protected void checkFlowerChange(final World world, final int i, final int j, final int k) {
        if (!world.isRemote && !this.canBlockStay(world, i, j, k)) {
            if (world.getBlockMetadata(i, j, k) == 7) {
                this.dropBlockAsItem(world, i, j, k, 0, 0);
            }
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public boolean canBlockStay(final World world, final int i, final int j, final int k) {
        boolean belowCheck = false;
        if (world.getBlock(i, j, k) == this && world.getBlockMetadata(i, j, k) == 8) {
            belowCheck = (world.getBlock(i, j - 1, k) == this && world.getBlockMetadata(i, j - 1, k) == 7);
        } else {
            belowCheck = this.canThisPlantGrowOnThisBlock(world.getBlock(i, j - 1, k));
        }
        return belowCheck && (world.getFullBlockLightValue(i, j, k) >= 8 || world.canBlockSeeTheSky(i, j, k));
    }

    private boolean canThisPlantGrowOnThisBlock(final Block b) {
        return b != null && (b.getMaterial() == Material.ground || b.getMaterial() == Material.grass);
    }

    protected String getFormattedTextureName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(this.getTextureName()));
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName("tallFlower"));
    }

    @Override
    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @Override
    public boolean func_149851_a(final World world, final int x, final int y, final int z, final boolean var5) {
        return true;
    }

    @Override
    public boolean func_149852_a(final World world, final Random rand, final int x, final int y, final int z) {
        return world.getBlockMetadata(x, y, z) <= 7 && world.isAirBlock(x, y + 1, z);
    }

    @Override
    public void func_149853_b(final World world, final Random rand, final int x, final int y, final int z) {
        world.setBlock(x, y + 1, z, (Block) this, 8, 3);
    }
}
