package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.info.TCRenderIDs;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BambooChestRenderHandler implements ISimpleBlockRenderingHandler {

    private TileEntity dummyTileEnt;

    public BambooChestRenderHandler() {
        this.dummyTileEnt = (TileEntity) new TileEntityBambooChest();
    }

    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
        final RenderBlocks renderer) {
        if (modelID == this.getRenderId()) {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.dummyTileEnt, 0.0, 0.0, 0.0, 0.0f);
            GL11.glEnable(32826);
        }
    }

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        return false;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }

    public int getRenderId() {
        return TCRenderIDs.bambooChest;
    }
}
