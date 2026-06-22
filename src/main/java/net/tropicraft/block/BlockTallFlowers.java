package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTallFlowers extends BlockDoublePlant {

    @SideOnly(Side.CLIENT)
    protected IIcon bottomIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon[] topIcons;
    protected String[] names;

    public BlockTallFlowers(final String[] names) {
        this.setBlockName("tallFlower");
        this.names = names;
        this.setBlockTextureName("tallFlower");
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
    }

    public boolean canBlockStay(final World world, final int x, final int y, final int z) {
        if (world.getBlock(x, y, z) != this) {
            return super.canBlockStay(world, x, y, z);
        }
        final int l = world.getBlockMetadata(x, y, z);
        return (l > 0) ? (world.getBlock(x, y - 1, z) == this)
            : (world.getBlock(x, y + 1, z) == this && super.canBlockStay(world, x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.topIcons = new IIcon[this.names.length];
        for (int i = 0; i < this.names.length; ++i) {
            this.topIcons[i] = iconRegister.registerIcon(
                this.getUnlocalizedName()
                    .substring(
                        this.getUnlocalizedName()
                            .indexOf(".") + 1)
                    + "_"
                    + this.names[i]);
        }
        this.bottomIcon = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + "Stem");
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < this.names.length; ++i) {
            list.add(new ItemStack(item, 1, i + 1));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return (meta == 0) ? this.bottomIcon : this.topIcons[meta - 1];
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149888_a(final boolean renderFlag, final int meta) {
        return (renderFlag && meta > 0) ? this.topIcons[meta - 1] : this.bottomIcon;
    }

    public Item getItemDropped(final int meta, final Random rand, final int unused) {
        if (meta == 0) {
            return null;
        }
        return Item.getItemFromBlock((Block) this);
    }

    public int getRenderType() {
        return 1;
    }

    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase player,
        final ItemStack itemstack) {
        world.setBlock(x, y + 1, z, (Block) this, itemstack.getItemDamage(), 3);
    }

    public boolean func_149851_a(final World world, final int x, final int y, final int z, final boolean var5) {
        return false;
    }

    public boolean func_149852_a(final World world, final Random rand, final int x, final int y, final int z) {
        return false;
    }

    public void func_149853_b(final World world, final Random rand, final int x, final int y, final int z) {}
}
