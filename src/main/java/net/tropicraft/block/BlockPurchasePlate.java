package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPurchasePlate extends BlockContainer {

    public BlockPurchasePlate() {
        super(Material.circuits);
        this.setTickRandomly(true);
        final float var5 = 0.0625f;
        this.setBlockBounds(var5, 0.0f, var5, 1.0f - var5, 0.03125f, 1.0f - var5);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
        this.setHardness(20.0f);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World par1World, final int par2, final int par3,
        final int par4) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean canPlaceBlockAt(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
        final int p_149742_4_) {
        return World.doesBlockHaveSolidTopSurface((IBlockAccess) p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_)
            || BlockFence.func_149825_a(p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_));
    }

    public void onNeighborBlockChange(final World p_149695_1_, final int p_149695_2_, final int p_149695_3_,
        final int p_149695_4_, final Block p_149695_5_) {
        boolean flag = false;
        if (!World.doesBlockHaveSolidTopSurface((IBlockAccess) p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_)
            && !BlockFence.func_149825_a(p_149695_1_.getBlock(p_149695_2_, p_149695_3_ - 1, p_149695_4_))) {
            flag = true;
        }
        if (flag) {
            this.dropBlockAsItem(
                p_149695_1_,
                p_149695_2_,
                p_149695_3_,
                p_149695_4_,
                p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_),
                0);
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4,
        final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        final TileEntityPurchasePlate pp = (TileEntityPurchasePlate) par1World.getTileEntity(par2, par3, par4);
        if (pp != null) {
            pp.onClicked(true);
        }
        return true;
    }

    public void onBlockClicked(final World world, final int i, final int j, final int k,
        final EntityPlayer entityplayer) {
        if (world.isRemote) {
            return;
        }
        final TileEntityPurchasePlate pp = (TileEntityPurchasePlate) world.getTileEntity(i, j, k);
        if (pp != null) {
            pp.onClicked(false);
        }
    }

    public TileEntity createNewTileEntity(final World world, final int meta) {
        return new TileEntityPurchasePlate();
    }

    public void setBlockBoundsBasedOnState(final IBlockAccess par1IBlockAccess, final int par2, final int par3,
        final int par4) {
        final boolean var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1;
        final float var6 = 0.0625f;
        if (var5) {
            this.setBlockBounds(var6, 0.0f, var6, 1.0f - var6, 0.03125f, 1.0f - var6);
        } else {
            this.setBlockBounds(var6, 0.0f, var6, 1.0f - var6, 0.0625f, 1.0f - var6);
        }
    }

    public boolean canProvidePower() {
        return true;
    }

    public void setBlockBoundsForItemRender() {
        final float var1 = 0.5f;
        final float var2 = 0.125f;
        final float var3 = 0.5f;
        this.setBlockBounds(0.5f - var1, 0.5f - var2, 0.5f - var3, 0.5f + var1, 0.5f + var2, 0.5f + var3);
    }

    public int getMobilityFlag() {
        return 1;
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(String.format("%s", this.getActualName(this.getUnlocalizedName())));
    }
}
