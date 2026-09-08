package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftLog extends BlockTropicraftMulti {

    protected IIcon palmSide;
    protected IIcon palmEnd;
    protected IIcon mahoganySide;
    protected IIcon mahoganyEnd;

    public BlockTropicraftLog(final String[] names) {
        super(names, Material.wood);
        this.setBlockTextureName("log");
        this.disableStats();
        this.setHardness(2.0f);
        this.setStepSound(BlockTropicraftLog.soundTypeWood);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public static void spawnCoconuts(final World world, final int i, final int j, final int k, final Random random,
        final int chance) {
        if (world.getBlock(i, j + 1, k) == TCBlockRegistry.palmLeaves
            || world.getBlock(i, j + 2, k) == TCBlockRegistry.palmLeaves) {
            if (world.isAirBlock(i + 1, j, k) && random.nextInt(chance) == 0) {
                world.setBlock(i + 1, j, k, TCBlockRegistry.coconut);
                world.setBlockMetadataWithNotify(i, j, k, 0, 3);
            }
            if (world.isAirBlock(i - 1, j, k) && random.nextInt(chance) == 0) {
                world.setBlock(i - 1, j, k, TCBlockRegistry.coconut);
                world.setBlockMetadataWithNotify(i, j, k, 0, 3);
            }
            if (world.isAirBlock(i, j, k - 1) && random.nextInt(chance) == 0) {
                world.setBlock(i, j, k - 1, TCBlockRegistry.coconut);
                world.setBlockMetadataWithNotify(i, j, k, 0, 3);
            }
            if (world.isAirBlock(i, j, k + 1) && random.nextInt(chance) == 0) {
                world.setBlock(i, j, k + 1, TCBlockRegistry.coconut);
                world.setBlockMetadataWithNotify(i, j, k, 0, 3);
            }
            if (world.isAirBlock(i, j - 1, k) && random.nextInt(chance) == 0) {
                world.setBlock(i, j - 1, k, TCBlockRegistry.coconut);
                world.setBlockMetadataWithNotify(i, j, k, 0, 3);
            }
        }
    }

    public int getRenderType() {
        return 31;
    }

    public int onBlockPlaced(final World world, final int x, final int y, final int z, final int side, final float hitX,
        final float hitY, final float hitZ, final int metadata) {
        final int j1 = metadata & 0x1;
        byte b0 = 0;
        switch (side) {
            case 0:
            case 1: {
                b0 = 0;
                break;
            }
            case 2:
            case 3: {
                b0 = 8;
                break;
            }
            case 4:
            case 5: {
                b0 = 4;
                break;
            }
        }
        return j1 | b0;
    }

    @Override
    public IIcon getIcon(final int i, final int j) {
        if (j == 0) {
            if (i == 0 || i == 1) {
                return this.palmEnd;
            }
            return this.palmSide;
        } else if (j == 8) {
            if (i == 2 || i == 3) {
                return this.palmEnd;
            }
            return this.palmSide;
        } else if (j == 4) {
            if (i == 4 || i == 5) {
                return this.palmEnd;
            }
            return this.palmSide;
        } else if (j == 1) {
            if (i == 0 || i == 1) {
                return this.mahoganyEnd;
            }
            return this.mahoganySide;
        } else if (j == 5) {
            if (i == 4 || i == 5) {
                return this.mahoganyEnd;
            }
            return this.mahoganySide;
        } else {
            if (j != 9) {
                return this.palmEnd;
            }
            if (i == 3 || i == 2) {
                return this.mahoganyEnd;
            }
            return this.mahoganySide;
        }
    }

    public int quantityDropped(final Random rand) {
        return 1;
    }

    public Item getItemDropped(final int p_149650_1_, final Random rand, final int p_149650_3_) {
        return Item.getItemFromBlock(this);
    }

    public boolean canSustainLeaves(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    public boolean isWood(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.palmSide = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + this.names[0]
                + "_Side");
        this.palmEnd = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + this.names[0]
                + "_End");
        this.mahoganySide = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + this.names[1]
                + "_Side");
        this.mahoganyEnd = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + this.names[1]
                + "_End");
    }
}
