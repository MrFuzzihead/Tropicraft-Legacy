package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooChute extends BlockTropicraft implements IPlantable {

    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    private IIcon bottomIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon indentIcon;
    @SideOnly(Side.CLIENT)
    private IIcon leafIcon;
    @SideOnly(Side.CLIENT)
    private IIcon leafFlippedIcon;

    public BlockBambooChute() {
        super(Material.plants);
        this.setStepSound(BlockBambooChute.soundTypeGrass);
        final float f = 0.375f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 1.0f, 0.5f + f);
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs) null);
        this.setBlockTextureName("bambooChute");
    }

    public boolean isBlockNormalCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return 1;
    }

    @Override
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("tropicraft:" + this.getTextureName());
    }

    public static IIcon getBambooIcon(final String iconString) {
        return null;
    }

    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        if (world.isAirBlock(x, y + 1, z)) {
            int plantHeight;
            for (plantHeight = 1; world.getBlock(x, y - plantHeight, z)
                == TCBlockRegistry.bambooChute; ++plantHeight) {}
            if (plantHeight < 12) {
                final int meta = world.getBlockMetadata(x, y, z);
                if (meta == 8) {
                    world.setBlock(x, y + 1, z, (Block) TCBlockRegistry.bambooChute);
                    world.setBlockMetadataWithNotify(x, y, z, 0, 3);
                } else {
                    world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
                }
            }
        }
    }

    public boolean canPlaceBlockAt(final World world, final int i, final int j, final int k) {
        final Block idBelow = world.getBlock(i, j - 1, k);
        final Block idAdjacentX1 = world.getBlock(i - 1, j - 1, k);
        final Block idAdjacentX2 = world.getBlock(i + 1, j - 1, k);
        final Block idAdjacentZ1 = world.getBlock(i, j - 1, k - 1);
        final Block idAdjacentZ2 = world.getBlock(i, j - 1, k + 1);
        return idBelow == TCBlockRegistry.bambooChute
            || ((idBelow == Blocks.grass || idBelow == Blocks.dirt || idBelow == Blocks.sand)
                && (idAdjacentX1 == Blocks.dirt || idAdjacentX1 == Blocks.grass
                    || idAdjacentX1 == Blocks.sand
                    || (idAdjacentX2 == Blocks.dirt || idAdjacentX2 == Blocks.grass || idAdjacentX2 == Blocks.sand)
                    || (idAdjacentZ1 == Blocks.dirt || idAdjacentZ1 == Blocks.grass || idAdjacentZ1 == Blocks.sand)
                    || idAdjacentZ2 == Blocks.grass));
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World par1World, final int par2, final int par3,
        final int par4) {
        return null;
    }

    public boolean canBlockStay(final World par1World, final int par2, final int par3, final int par4) {
        return this.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    public void onNeighborBlockChange(final World world, final int x, final int y, final int z,
        final Block neighborID) {
        this.checkBlockCoordValid(world, x, y, z);
    }

    protected final void checkBlockCoordValid(final World world, final int x, final int y, final int z) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    public boolean canSustainPlant(final IBlockAccess world, final int x, final int y, final int z,
        final ForgeDirection direction, final IPlantable plant) {
        final Block plantID = plant.getPlant(world, x, y + 1, z);
        final EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);
        if (plantID == TCBlockRegistry.bambooChute) {
            return true;
        }
        final Block blockID = TCBlockRegistry.bambooChute;
        switch (plantType) {
            case Desert: {
                return blockID == Blocks.sand;
            }
            case Nether: {
                return blockID == Blocks.soul_sand;
            }
            case Crop: {
                return blockID == Blocks.farmland;
            }
            case Cave: {
                return this.isBlockSolid(world, x, y, z, direction.flag);
            }
            case Plains: {
                return blockID == Blocks.grass || blockID == Blocks.dirt;
            }
            case Water: {
                return world.getBlock(x, y, z)
                    .getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0;
            }
            case Beach: {
                final boolean isBeach = blockID == Blocks.grass || blockID == Blocks.dirt || blockID == Blocks.sand;
                final boolean hasWater = world.getBlock(x - 1, y, z)
                    .getMaterial() == Material.water
                    || world.getBlock(x + 1, y, z)
                        .getMaterial() == Material.water
                    || world.getBlock(x, y, z - 1)
                        .getMaterial() == Material.water
                    || world.getBlock(x, y, z + 1)
                        .getMaterial() == Material.water;
                return isBeach && hasWater;
            }
            default: {
                return false;
            }
        }
    }

    public EnumPlantType getPlantType(final IBlockAccess world, final int x, final int y, final int z) {
        return EnumPlantType.Plains;
    }

    public Block getPlant(final IBlockAccess world, final int x, final int y, final int z) {
        return TCBlockRegistry.bambooChute;
    }

    public int getPlantMetadata(final IBlockAccess world, final int x, final int y, final int z) {
        return world.getBlockMetadata(x, y, z);
    }

    public Item getItemDropped(final int meta, final Random rand, final int unused) {
        return TCItemRegistry.bambooChute;
    }
}
