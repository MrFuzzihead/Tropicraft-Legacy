package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftFence extends BlockFence {

    private BlockTropicraftFenceGate fenceGate;

    public BlockTropicraftFence(final String name, final String textureName, final BlockTropicraftFenceGate fenceGate,
        final Material material) {
        super(name, material);
        this.setStepSound(BlockTropicraftFence.soundTypeWood);
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
        this.setBlockName(name);
        this.setBlockTextureName(textureName);
    }

    public boolean canConnectFenceTo(final IBlockAccess world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        return block == this || block == TCBlockRegistry.bambooFenceGate
            || block == TCBlockRegistry.palmFenceGate
            || (block != null && block.getMaterial()
                .isOpaque() && block.renderAsNormalBlock() && block.getMaterial() != Material.gourd);
    }

    public boolean canPlaceTorchOnTop(final World world, final int x, final int y, final int z) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(TropicraftNames.afterFirstDot(this.getFormattedTextureName()));
    }

    protected String getFormattedTextureName() {
        return TropicraftNames.block(this.getTextureName());
    }

    public String getUnlocalizedName() {
        return TropicraftNames.block(super.getUnlocalizedName());
    }

}
