package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCoral extends BlockTropicraftMulti {

    private static final float BRIGHTNESS_DAY = 0.3f;
    private static final float BRIGHTNESS_NIGHT = 0.6f;

    public BlockCoral(final String[] names) {
        super(names, Material.water);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
        this.setLightLevel(0.3f);
        this.setHardness(0.0f);
        this.setTickRandomly(true);
        this.setBlockTextureName("coral");
    }

    @SideOnly(Side.CLIENT)
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public int damageDropped(final int i) {
        return i & 0x7;
    }

    public boolean canPlaceBlockAt(final World world, final int i, final int j, final int k) {
        return super.canPlaceBlockAt(world, i, j, k) && world.getBlock(i, j, k) != this
            && this.canThisPlantGrowOnThisBlock(world.getBlock(i, j - 1, k))
            && world.getBlock(i, j, k)
                .getMaterial() == Material.water
            && world.getBlock(i, j + 1, k)
                .getMaterial() == Material.water;
    }

    protected boolean canThisPlantGrowOnThisBlock(final Block b) {
        return b == Blocks.grass || b == Blocks.dirt
            || b == Blocks.sand
            || b == TCBlockRegistry.purifiedSand
            || b == TCBlockRegistry.mineralSands;
    }

    public void onNeighborBlockChange(final World world, final int i, final int j, final int k, final Block b) {
        super.onNeighborBlockChange(world, i, j, k, b);
        this.checkFlowerChange(world, i, j, k);
    }

    public void updateTick(final World world, final int i, final int j, final int k, final Random random) {
        this.checkFlowerChange(world, i, j, k);
    }

    public int getRenderType() {
        return 1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int i, final int j, final int k) {
        return null;
    }

    protected void checkFlowerChange(final World world, final int i, final int j, final int k) {
        if (!this.canBlockStay(world, i, j, k)) {
            this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k) & 0x7, 0);
            world.setBlockToAir(i, j, k);
        }
    }

    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        return 9;
    }

    public boolean canBlockStay(final World world, final int x, final int y, final int z) {
        return world.getBlock(x, y, z)
            .getMaterial()
            .isLiquid()
            && world.getBlock(x, y + 1, z)
                .getMaterial()
                .isLiquid()
            && this.canThisPlantGrowOnThisBlock(world.getBlock(x, y - 1, z));
    }
}
