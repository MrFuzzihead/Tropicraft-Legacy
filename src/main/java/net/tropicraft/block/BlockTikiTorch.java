package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.info.*;
import net.tropicraft.registry.*;

import cpw.mods.fml.relauncher.*;

public class BlockTikiTorch extends BlockTropicraft {

    private IIcon lowerTorch;
    private IIcon upperTorch;

    public BlockTikiTorch() {
        super(Material.circuits);
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs) null);
        this.lightValue = 15;
        final float w = 0.0625f;
        this.setBlockBounds(0.5f - w, 0.0f, 0.5f - w, 0.5f + w, 0.9f, 0.5f + w);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int id, final int metadata) {
        if (metadata != 0) {
            return this.lowerTorch;
        }
        return this.upperTorch;
    }

    public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
        final float w = 0.0625f;
        final float top = (world.getBlockMetadata(x, y, z) == 0) ? 0.625f : 1.0f;
        this.setBlockBounds(0.5f - w, 0.0f, 0.5f - w, 0.5f + w, top, 0.5f + w);
    }

    public boolean canPlaceBlockAt(final World world, final int x, final int y, final int z) {
        return !world.isRemote && this.canPlaceTikiTorchOn(world, x, y - 1, z);
    }

    public Item getItemDropped(final int meta, final Random rand, final int unused) {
        if (meta == 0) {
            return TCItemRegistry.tikiTorch;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World world, final int x, final int y, final int z) {
        return TCItemRegistry.tikiTorch;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return TCRenderIDs.tikiTorch;
    }

    private boolean canPlaceTikiTorchOn(final World world, final int x, final int y, final int z) {
        if (world.isBlockNormalCubeDefault(x, y, z, false)) {
            return true;
        }
        final Block b = world.getBlock(x, y, z);
        if (world.isAirBlock(x, y, z)) {
            return false;
        }
        if (b == this && world.getBlockMetadata(x, y, z) == 1) {
            return true;
        }
        if (b != Blocks.glass && !(b instanceof BlockFence)) {
            if (b != null && b instanceof BlockStairs) {
                final int meta = world.getBlockMetadata(x, y, z);
                if ((0x4 & meta) != 0x0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block oldBlock) {
        if (!world.isRemote && !this.canPlaceTikiTorchOn(world, x, y - 1, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
        super.onNeighborBlockChange(world, x, y, z, oldBlock);
    }

    public void onBlockDestroyedByPlayer(final World world, final int x, int y, final int z, final int meta) {
        if (!world.isRemote) {
            while (world.getBlock(x, --y, z) == this) {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z,
        final Explosion explosion) {
        this.onBlockDestroyedByPlayer(world, x, y, z, world.getBlockMetadata(x, y, z));
    }

    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        final int l = world.getBlockMetadata(x, y, z);
        if (l == 0) {
            final double d = x + 0.5f;
            final double d2 = y + 0.7f;
            final double d3 = z + 0.5f;
            world.spawnParticle("smoke", d, d2, d3, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", d, d2, d3, 0.0, 0.0, 0.0);
        }
    }

    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        final int l = world.getBlockMetadata(x, y, z);
        if (l == 0) {
            return super.getLightValue(world, x, y, z);
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public void registerBlockIcons(final IIconRegister iconRegistry) {
        this.lowerTorch = iconRegistry.registerIcon("tropicraft:tikiTorch_Lower");
        this.upperTorch = iconRegistry.registerIcon("tropicraft:tikiTorch_Upper");
    }
}
