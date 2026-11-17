package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraft extends Block {

    public BlockTropicraft() {
        this(Material.rock);
    }

    public BlockTropicraft(final Material material) {
        super(material);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public String getUnlocalizedName() {
        return TropicraftNames.block(super.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister
            .registerIcon(String.format("%s", TropicraftNames.afterFirstDot(this.getUnlocalizedName())));
    }
}
