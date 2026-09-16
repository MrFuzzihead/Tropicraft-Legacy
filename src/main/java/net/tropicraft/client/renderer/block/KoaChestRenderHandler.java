package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.info.TCRenderIDs;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class KoaChestRenderHandler implements ISimpleBlockRenderingHandler {

    private final TileEntityKoaChest dummyTileEnt = new TileEntityKoaChest();

    @Override
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
        final RenderBlocks renderer) {
        if (modelID == this.getRenderId()) {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.dummyTileEnt, 0.0, 0.0, 0.0, 0.0f);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @Override
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        // Placed chests are rendered by TileEntityKoaChestRenderer.
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return TCRenderIDs.koaChest;
    }
}
