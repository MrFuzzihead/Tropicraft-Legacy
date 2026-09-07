package net.tropicraft.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPurifiedSand extends BlockFalling {

    public BlockPurifiedSand() {
        super(Material.sand);
        this.setStepSound(BlockPurifiedSand.soundTypeSand);
        this.setHardness(0.5f);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(String.format("%s", this.getActualName(this.getUnlocalizedName())));
    }
}
