package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.BlockCoffeePlant;
import net.tropicraft.info.TCRenderIDs;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class CoffeePlantRenderHandler implements ISimpleBlockRenderingHandler {

    public void renderInventoryBlock(final Block block, final int metadata, final int modelId,
        final RenderBlocks renderer) {}

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        if (block instanceof BlockCoffeePlant) {
            renderer.overrideBlockTexture = ((BlockCoffeePlant) block).stemIcon;
            renderer.renderCrossedSquares(block, x, y, z);
            renderer.overrideBlockTexture = null;
            renderer.renderStandardBlock(block, x, y, z);
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return false;
    }

    public int getRenderId() {
        return TCRenderIDs.coffeePlant;
    }
}
