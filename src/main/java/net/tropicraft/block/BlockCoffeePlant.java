package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCoffeePlant extends BlockTropicraft {

    public static final int MAX_HEIGHT = 3;
    public static final int GROWTH_RATE_FERTILE = 10;
    public static final int GROWTH_RATE_INFERTILE = 20;
    public static final int RIPENING_RATE_FERTILE = 12;
    public static final int RIPENING_RATE_INFERTILE = 25;
    @SideOnly(Side.CLIENT)
    public IIcon[] leafIcons;
    @SideOnly(Side.CLIENT)
    public IIcon stemIcon;

    public BlockCoffeePlant() {
        super(Material.plants);
        this.setTickRandomly(true);
        this.disableStats();
        this.setCreativeTab((CreativeTabs) null);
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World world, final int x, final int y, final int z) {
        return TCItemRegistry.coffeeBean;
    }

    public IIcon getIcon(final int side, int meta) {
        meta &= 0x7;
        return this.leafIcons[meta];
    }

    public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z,
        final int side) {
        return (side != 1 || world.getBlock(x, y, z) != this) && (side != 0 || world.getBlock(x, y, z) != this)
            && super.shouldSideBeRendered(world, x, y, z, side);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return null;
    }

    private void tryToGrowUpwards(final World world, final int x, final int y, final int z, final Random random) {
        if (world.isAirBlock(x, y + 1, z)) {
            int height;
            for (height = 1; world.getBlock(x, y - height, z) == this; ++height) {}
            if (height < 3 && random.nextInt(this.isFertile(world, x, y - height, z) ? 10 : 20) == 0) {
                world.setBlock(x, y + 1, z, (Block) this, 0, 2);
            }
        }
    }

    private void tryToRipen(final World world, final int x, final int y, final int z, final Random random) {
        final int meta = world.getBlockMetadata(x, y, z) & 0x7;
        if (meta == 7) {
            world.setBlockMetadataWithNotify(x, y, z, 6, 2);
            return;
        }
        if (meta == 6) {
            return;
        }
        if (world.getBlockLightValue(x, y + 1, z) < 9) {
            return;
        }
        if (random.nextInt(this.isFertile(world, x, y - 1, z) ? 12 : 25) != 0) {
            return;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    }

    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        if (world.isRemote) {
            return;
        }
        this.tryToGrowUpwards(world, x, y, z, random);
        this.tryToRipen(world, x, y, z, random);
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
        final int side, final float offsetX, final float offsetY, final float offsetZ) {
        if ((world.getBlockMetadata(x, y, z) & 0x7) != 0x6) {
            return false;
        }
        world.setBlock(x, y, z, (Block) this, 0, 3);
        final ItemStack stack = new ItemStack((Item) TCItemRegistry.coffeeBean, 1, 2);
        this.dropBlockAsItem(world, x, y, z, stack);
        return true;
    }

    public Item getItemDropped(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return TCItemRegistry.coffeeBean;
    }

    public int damageDropped(final int meta) {
        return 2;
    }

    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y,
        final int z) {
        return new ItemStack((Item) TCItemRegistry.coffeeBean, 1, 0);
    }

    public int getDamageValue(final World par1World, final int par2, final int par3, final int par4) {
        return 0;
    }

    public int quantityDropped(final int meta, final int fortune, final Random random) {
        return (meta == 6) ? 1 : 0;
    }

    public void onNeighborBlockChange(final World world, final int x, final int y, final int z,
        final Block neighborBlock) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    public boolean canBlockStay(final World world, final int x, final int y, final int z) {
        final Block soil = world.getBlock(x, y - 1, z);
        return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && soil != null
            && (soil == this || soil.canSustainPlant(
                (IBlockAccess) world,
                x,
                y - 1,
                z,
                ForgeDirection.UP,
                (IPlantable) TCItemRegistry.coffeeBean));
    }

    public int getRenderType() {
        return TCRenderIDs.coffeePlant;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.leafIcons = new IIcon[7];
        for (int i = 0; i < 7; ++i) {
            this.leafIcons[i] = iconRegister.registerIcon(
                this.getUnlocalizedName()
                    .substring(
                        this.getUnlocalizedName()
                            .indexOf(".") + 1)
                    + "_"
                    + i);
        }
        this.stemIcon = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + "Stem");
    }
}
