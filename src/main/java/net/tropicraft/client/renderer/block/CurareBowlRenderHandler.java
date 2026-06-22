package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.info.TCRenderIDs;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class CurareBowlRenderHandler implements ISimpleBlockRenderingHandler {

    private TileEntityCurareBowl dummyTileEntity;

    public CurareBowlRenderHandler() {
        this.dummyTileEntity = new TileEntityCurareBowl();
    }

    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
        final RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        TileEntityRendererDispatcher.instance
            .renderTileEntityAt((TileEntity) this.dummyTileEntity, 0.0, 0.0, 0.0, 0.0f);
        GL11.glPopMatrix();
    }

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        return false;
    }

    public int getRenderId() {
        return TCRenderIDs.curareBowl;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return false;
    }
}
