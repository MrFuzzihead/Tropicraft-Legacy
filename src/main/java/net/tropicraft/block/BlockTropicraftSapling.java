package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.world.worldgen.TCGenBase;
import net.tropicraft.world.worldgen.WorldGenTallTree;
import net.tropicraft.world.worldgen.WorldGenTropicraftCurvedPalm;
import net.tropicraft.world.worldgen.WorldGenTropicraftFruitTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftLargePalmTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftNormalPalms;
import net.tropicraft.world.worldgen.WorldGenTualang;
import net.tropicraft.world.worldgen.WorldGenUpTree;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftSapling extends BlockSapling implements IGrowable {

    protected String[] names;
    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;

    public BlockTropicraftSapling(final String[] names) {
        this.names = names;
        this.setTickRandomly(true);
        this.disableStats();
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    protected boolean canPlaceBlockOn(final Block block) {
        return block == Blocks.grass || block == Blocks.dirt
            || block == Blocks.farmland
            || block == Blocks.sand
            || block == TCBlockRegistry.purifiedSand
            || block == TCBlockRegistry.mineralSands;
    }

    public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
        if (!world.isRemote) {
            super.updateTick(world, x, y, z, rand);
            if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0) {
                this.markOrGrowMarked(world, x, y, z, rand);
            }
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int i, final int j, final int k) {
        return null;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean func_149851_a(final World p_149851_1_, final int p_149851_2_, final int p_149851_3_,
        final int p_149851_4_, final boolean p_149851_5_) {
        return true;
    }

    public boolean func_149852_a(final World p_149852_1_, final Random p_149852_2_, final int p_149852_3_,
        final int p_149852_4_, final int p_149852_5_) {
        return p_149852_1_.rand.nextFloat() < 0.45;
    }

    public void func_149853_b(final World p_149853_1_, final Random p_149853_2_, final int p_149853_3_,
        final int p_149853_4_, final int p_149853_5_) {
        this.markOrGrowMarked(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
    }

    public void markOrGrowMarked(final World world, final int x, final int y, final int z, final Random rand) {
        TCGenBase.blockGenNotifyFlag = 3;
        final int l = world.getBlockMetadata(x, y, z);
        this.func_149878_d(world, x, y, z, rand);
        TCGenBase.blockGenNotifyFlag = 2;
    }

    public void func_149878_d(final World world, final int x, final int y, final int z, final Random rand) {
        final int type = world.getBlockMetadata(x, y, z);
        WorldGenerator gen = null;
        if (type == 0) {
            final int b = rand.nextInt(3);
            if (b == 0) {
                gen = new WorldGenTropicraftLargePalmTrees(false);
            } else if (b == 1) {
                gen = new WorldGenTropicraftCurvedPalm(world, rand);
            } else if (b == 2) {
                gen = new WorldGenTropicraftNormalPalms(false);
            }
        } else if (type == 1) {
            gen = this.randomRainforestTreeGen(world);
        } else {
            gen = new WorldGenTropicraftFruitTrees(world, rand, type - 2);
        }
        if (gen != null) {
            world.setBlockToAir(x, y, z);
            if (!gen.generate(world, rand, x, y, z)) {
                world.setBlock(x, y, z, (Block) this, type, 3);
            }
        }
    }

    private WorldGenerator randomRainforestTreeGen(final World world) {
        final Random rand = new Random();
        final int type = rand.nextInt(4);
        switch (type) {
            case 0: {
                return new WorldGenTallTree(world, rand);
            }
            case 1: {
                return new WorldGenUpTree(world, rand);
            }
            case 2:
            case 3: {
                return new WorldGenTualang(world, rand, 18, 9);
            }
            default: {
                return new WorldGenTualang(world, rand, 25, 10);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int id, int metadata) {
        if (metadata < 0 || metadata > this.names.length - 1) {
            metadata = this.names.length - 1;
        }
        return this.icons[metadata];
    }

    public int damageDropped(final int meta) {
        return meta;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < this.names.length; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.icons = new IIcon[this.names.length];
        for (int i = 0; i < this.names.length; ++i) {
            this.icons[i] = iconRegister.registerIcon(
                this.getUnlocalizedName()
                    .substring(
                        this.getUnlocalizedName()
                            .indexOf(".") + 1)
                    + "_"
                    + this.names[i]);
        }
    }
}
