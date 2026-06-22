package net.tropicraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBongoDrum extends BlockTropicraftMulti {

    public static final float SMALL_DRUM_SIZE = 0.5f;
    public static final float MEDIUM_DRUM_SIZE = 0.6f;
    public static final float BIG_DRUM_SIZE = 0.7f;
    public static final float SMALL_DRUM_OFFSET = 0.25f;
    public static final float MEDIUM_DRUM_OFFSET = 0.19999999f;
    public static final float BIG_DRUM_OFFSET = 0.15f;
    public static final float DRUM_HEIGHT = 1.0f;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;

    public BlockBongoDrum(final String[] names) {
        super(names, Material.circuits);
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
        this.setLightOpacity(255);
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iconRegistry) {
        this.topIcon = iconRegistry.registerIcon("tropicraft:bongoDrumTop");
        final IIcon registerIcon = iconRegistry.registerIcon("tropicraft:bongoDrumSide");
        this.sideIcon = registerIcon;
        this.blockIcon = registerIcon;
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
        final int side, final float offsetX, final float offsetY, final float offsetZ) {
        if (side != 1) {
            return true;
        }
        final int meta = world.getBlockMetadata(x, y, z) & 0x3;
        switch (meta) {
            case 2: {
                this.playLowBongo(world, x, y, z);
                break;
            }
            case 1: {
                this.playMediumBongo(world, x, y, z);
                break;
            }
            default: {
                this.playHighBongo(world, x, y, z);
                break;
            }
        }
        return true;
    }

    private void playHighBongo(final World world, final int x, final int y, final int z) {
        world.playSoundEffect((double) x, (double) y, (double) z, "tropicraft:bongohigh", 1.0f, 1.0f);
    }

    private void playMediumBongo(final World world, final int x, final int y, final int z) {
        world.playSoundEffect((double) x, (double) y, (double) z, "tropicraft:bongomedium", 1.0f, 1.0f);
    }

    private void playLowBongo(final World world, final int x, final int y, final int z) {
        world.playSoundEffect((double) x, (double) y, (double) z, "tropicraft:bongolow", 1.0f, 1.0f);
    }

    public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
        final int meta = world.getBlockMetadata(x, y, z) & 0x3;
        switch (meta) {
            case 2: {
                this.setBlockBounds(0.15f, 0.0f, 0.15f, 0.85f, 1.0f, 0.85f);
                break;
            }
            case 1: {
                this.setBlockBounds(0.19999999f, 0.0f, 0.19999999f, 0.8f, 1.0f, 0.8f);
                break;
            }
            default: {
                this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
                break;
            }
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.15f, 0.0f, 0.15f, 0.85f, 1.0f, 0.85f);
    }

    @Override
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    public IIcon getIcon(final int side, final int meta) {
        if (side == 1) {
            return this.topIcon;
        }
        return this.sideIcon;
    }

    public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z,
        final int side) {
        return side != 0 && super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public int damageDropped(final int meta) {
        return meta;
    }

    public int getLightOpacity(final IBlockAccess world, final int x, final int y, final int z) {
        return 255;
    }

    public int getDamageValue(final World world, final int x, final int y, final int z) {
        int dmg = super.getDamageValue(world, x, y, z);
        if (dmg > 2) {
            dmg = 0;
        }
        return dmg;
    }
}
