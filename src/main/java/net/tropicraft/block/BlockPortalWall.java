package net.tropicraft.block;

import net.minecraft.block.BlockSandStone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPortalWall extends BlockSandStone {

    public BlockPortalWall() {
        this.setBlockUnbreakable();
        this.setResistance(6000000.0f);
        this.setCreativeTab((CreativeTabs) null);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return Blocks.sandstone.getIcon(side, 0);
    }

    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4,
        final EntityPlayer par5EntityPlayer, final int doop, final float doo, final float doe, final float doa) {
        return par5EntityPlayer.getCurrentEquippedItem() != null && par5EntityPlayer.getCurrentEquippedItem()
            .getItem() instanceof ItemBucket;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {}
}
