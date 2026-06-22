package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFruitLeaves extends BlockLeaves {

    public static final String[][] imageNames;
    public static final String[] treeNames;
    @SideOnly(Side.CLIENT)
    protected IIcon[][] icons;

    public BlockFruitLeaves() {
        this.disableStats();
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT) {
            this.icons = new IIcon[2][];
        }
    }

    public void dropBlockAsItemWithChance(final World par1World, final int x, final int y, final int z, final int meta,
        final float par6, final int par7) {
        if (!par1World.isRemote && par1World.rand.nextInt(3) == 0) {
            final Item item = this.getItemDropped(meta, par1World.rand, par7);
            if (item == Item.getItemFromBlock((Block) TCBlockRegistry.saplings)) {
                super.dropBlockAsItem(par1World, x, y, z, new ItemStack(item, 1, this.damageDropped(meta) + 2));
            } else {
                super.dropBlockAsItem(par1World, x, y, z, new ItemStack(item, 1, this.damageDropped(meta)));
            }
        }
    }

    public int damageDropped(final int meta) {
        return meta & 0x3;
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(final int p_149741_1_) {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess p_149720_1_, final int p_149720_2_, final int p_149720_3_,
        final int p_149720_4_) {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
    }

    public int quantityDropped(final Random random) {
        return (random.nextInt(3) == 0) ? 1 : 0;
    }

    public Item getItemDropped(final int metadata, final Random random, final int j) {
        final int treeType = metadata & 0x3;
        if (random.nextFloat() >= 0.8f) {
            return Item.getItemFromBlock((Block) TCBlockRegistry.saplings);
        }
        if (treeType == 0) {
            return (Item) TCItemRegistry.grapefruit;
        }
        if (treeType == 1) {
            return (Item) TCItemRegistry.lemon;
        }
        if (treeType == 2) {
            return (Item) TCItemRegistry.lime;
        }
        return (Item) TCItemRegistry.orange;
    }

    public IIcon getIcon(final int var1, final int meta) {
        return ((meta & 0x3) == 0x1) ? this.icons[this.field_150127_b][1]
            : (((meta & 0x3) == 0x3) ? this.icons[this.field_150127_b][3]
                : (((meta & 0x3) == 0x2) ? this.icons[this.field_150127_b][2] : this.icons[this.field_150127_b][0]));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        for (int i = 0; i < BlockFruitLeaves.imageNames.length; ++i) {
            this.icons[i] = new IIcon[BlockFruitLeaves.imageNames[i].length];
            for (int j = 0; j < BlockFruitLeaves.imageNames[i].length; ++j) {
                this.icons[i][j] = iconRegister.registerIcon("tropicraft:" + BlockFruitLeaves.imageNames[i][j]);
            }
        }
    }

    public String[] func_150125_e() {
        return BlockFruitLeaves.treeNames;
    }

    static {
        imageNames = new String[][] { { "leafGrapefruit", "leafLemon", "leafLime", "leafOrange" },
            { "leafGrapefruitFast", "leafLemonFast", "leafLimeFast", "leafOrangeFast" } };
        treeNames = new String[] { "grapefruit", "lemon", "lime", "orange" };
    }
}
