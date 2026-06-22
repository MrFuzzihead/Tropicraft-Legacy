package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.info.TCRenderIDs;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class TikiTorchRenderHandler implements ISimpleBlockRenderingHandler {

    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
        final RenderBlocks renderer) {}

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        final int metadata = world.getBlockMetadata(x, y, z);
        final Tessellator tessellator = Tessellator.instance;
        final IIcon icon = block.getIcon(0, metadata);
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        final float pixX1 = icon.getMinU();
        final float pixX2 = icon.getMaxU();
        final float pixY1 = icon.getMinV();
        final float pixY2 = icon.getMaxV();
        final double topX1 = pixX1 + 0.02734375;
        final double topY1 = pixY1 + 0.0234375;
        final double topX2 = pixX1 + 0.03515625;
        final double topY2 = pixY1 + 0.03125;
        final double centerX = x + 0.5;
        final double centerZ = z + 0.5;
        final double width = 0.0625;
        final double height = (metadata == 0) ? 0.625 : 1.0;
        tessellator.addVertexWithUV(centerX - width, y + height, centerZ - width, topX1, topY1);
        tessellator.addVertexWithUV(centerX - width, y + height, centerZ + width, topX1, topY2);
        tessellator.addVertexWithUV(centerX + width, y + height, centerZ + width, topX2, topY2);
        tessellator.addVertexWithUV(centerX + width, y + height, centerZ - width, topX2, topY1);
        tessellator.addVertexWithUV(centerX - width, y + 1.0, (double) z, (double) pixX1, (double) pixY1);
        tessellator.addVertexWithUV(centerX - width, (double) y, (double) z, (double) pixX1, (double) pixY2);
        tessellator.addVertexWithUV(centerX - width, (double) y, z + 1.0, (double) pixX2, (double) pixY2);
        tessellator.addVertexWithUV(centerX - width, y + 1.0, z + 1.0, (double) pixX2, (double) pixY1);
        tessellator.addVertexWithUV(centerX + width, y + 1.0, z + 1.0, (double) pixX1, (double) pixY1);
        tessellator.addVertexWithUV(centerX + width, (double) y, z + 1.0, (double) pixX1, (double) pixY2);
        tessellator.addVertexWithUV(centerX + width, (double) y, (double) z, (double) pixX2, (double) pixY2);
        tessellator.addVertexWithUV(centerX + width, y + 1.0, (double) z, (double) pixX2, (double) pixY1);
        tessellator.addVertexWithUV((double) x, y + 1.0, centerZ + width, (double) pixX1, (double) pixY1);
        tessellator.addVertexWithUV((double) x, (double) y, centerZ + width, (double) pixX1, (double) pixY2);
        tessellator.addVertexWithUV(x + 1.0, (double) y, centerZ + width, (double) pixX2, (double) pixY2);
        tessellator.addVertexWithUV(x + 1.0, y + 1.0, centerZ + width, (double) pixX2, (double) pixY1);
        tessellator.addVertexWithUV(x + 1.0, y + 1.0, centerZ - width, (double) pixX1, (double) pixY1);
        tessellator.addVertexWithUV(x + 1.0, (double) y, centerZ - width, (double) pixX1, (double) pixY2);
        tessellator.addVertexWithUV((double) x, (double) y, centerZ - width, (double) pixX2, (double) pixY2);
        tessellator.addVertexWithUV((double) x, y + 1.0, centerZ - width, (double) pixX2, (double) pixY1);
        return true;
    }

    public int getRenderId() {
        return TCRenderIDs.tikiTorch;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return false;
    }
}
