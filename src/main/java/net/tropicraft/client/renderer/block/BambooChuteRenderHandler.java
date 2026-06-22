package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.BlockBambooChute;
import net.tropicraft.info.TCRenderIDs;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BambooChuteRenderHandler implements ISimpleBlockRenderingHandler {

    public void renderInventoryBlock(final Block block, final int metadata, final int modelId,
        final RenderBlocks renderer) {}

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        if (modelId == this.getRenderId()) {
            final Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            final int l = block.colorMultiplier(world, x, y, z);
            float f = (l >> 16 & 0xFF) / 255.0f;
            float f2 = (l >> 8 & 0xFF) / 255.0f;
            float f3 = (l & 0xFF) / 255.0f;
            if (EntityRenderer.anaglyphEnable) {
                final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
                final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
                final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
                f = f4;
                f2 = f5;
                f3 = f6;
            }
            tessellator.setColorOpaque_F(f, f2, f3);
            final float sixteenth = 0.0625f;
            final IIcon sideTex = BlockBambooChute.getBambooIcon("side");
            final IIcon topTex = BlockBambooChute.getBambooIcon("top");
            final IIcon bottomTex = BlockBambooChute.getBambooIcon("bottom");
            final IIcon indentTex = BlockBambooChute.getBambooIcon("indent");
            final IIcon leafTex = BlockBambooChute.getBambooIcon("leaf");
            final IIcon leafFlippedTex = BlockBambooChute.getBambooIcon("leafFlipped");
            renderer.renderFaceYNeg(
                block,
                (double) (x + sixteenth / 2.0f),
                (double) y,
                (double) (z + sixteenth / 2.0f),
                bottomTex);
            renderer.renderFaceXPos(block, x - 3.5 * sixteenth, (double) y, (double) (z + sixteenth / 2.0f), indentTex);
            renderer.renderFaceXNeg(block, x + 3.5 * sixteenth, (double) y, (double) (z + sixteenth / 2.0f), indentTex);
            renderer.renderFaceZPos(block, (double) (x + sixteenth / 2.0f), (double) y, z - 3.5 * sixteenth, indentTex);
            renderer.renderFaceZNeg(block, (double) (x + sixteenth / 2.0f), (double) y, z + 3.5 * sixteenth, indentTex);
            renderer.renderFaceYNeg(block, (double) x, (double) (y + sixteenth), (double) z, topTex);
            renderer
                .renderFaceXPos(block, (double) (x - 3.0f * sixteenth), (double) (y + sixteenth), (double) z, sideTex);
            renderer
                .renderFaceXNeg(block, (double) (x + 3.0f * sixteenth), (double) (y + sixteenth), (double) z, sideTex);
            renderer
                .renderFaceZPos(block, (double) x, (double) (y + sixteenth), (double) (z - 3.0f * sixteenth), sideTex);
            renderer
                .renderFaceZNeg(block, (double) x, (double) (y + sixteenth), (double) (z + 3.0f * sixteenth), sideTex);
            renderer.renderFaceYPos(block, (double) x, (double) (y - 8.0f * sixteenth), (double) z, topTex);
            renderer.renderFaceXPos(
                block,
                x - 3.5 * sixteenth,
                (double) (y + 8.0f * sixteenth),
                (double) (z + sixteenth / 2.0f),
                indentTex);
            renderer.renderFaceXNeg(
                block,
                x + 3.5 * sixteenth,
                (double) (y + 8.0f * sixteenth),
                (double) (z + sixteenth / 2.0f),
                indentTex);
            renderer.renderFaceZPos(
                block,
                (double) (x + sixteenth / 2.0f),
                (double) (y + 8.0f * sixteenth),
                z - 3.5 * sixteenth,
                indentTex);
            renderer.renderFaceZNeg(
                block,
                (double) (x + sixteenth / 2.0f),
                (double) (y + 8.0f * sixteenth),
                z + 3.5 * sixteenth,
                indentTex);
            renderer.renderFaceYNeg(block, (double) x, (double) (y + 9.0f * sixteenth), (double) z, topTex);
            renderer.renderFaceXPos(
                block,
                (double) (x - 3.0f * sixteenth),
                (double) (y + 9.0f * sixteenth),
                (double) z,
                sideTex);
            renderer.renderFaceXNeg(
                block,
                (double) (x + 3.0f * sixteenth),
                (double) (y + 9.0f * sixteenth),
                (double) z,
                sideTex);
            renderer.renderFaceZPos(
                block,
                (double) x,
                (double) (y + 9.0f * sixteenth),
                (double) (z - 3.0f * sixteenth),
                sideTex);
            renderer.renderFaceZNeg(
                block,
                (double) x,
                (double) (y + 9.0f * sixteenth),
                (double) (z + 3.0f * sixteenth),
                sideTex);
            renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, topTex);
            final float leafOffset1 = 5.5f * sixteenth;
            final float leafOffset2 = 9.0f * sixteenth;
            final float leafOffsetY = 3.5f * sixteenth;
            if (y % 2 == 0) {
                renderer.renderFaceXPos(
                    block,
                    (double) (x - leafOffset1),
                    (double) (y + leafOffsetY),
                    (double) (z + leafOffset2),
                    leafTex);
                renderer.renderFaceXNeg(
                    block,
                    (double) (x + leafOffset1),
                    (double) (y + leafOffsetY),
                    (double) (z + leafOffset2),
                    leafTex);
                renderer.renderFaceXPos(
                    block,
                    (double) (x - leafOffset1),
                    (double) (y + leafOffsetY),
                    (double) (z - leafOffset2),
                    leafFlippedTex);
                renderer.renderFaceXNeg(
                    block,
                    (double) (x + leafOffset1),
                    (double) (y + leafOffsetY),
                    (double) (z - leafOffset2),
                    leafFlippedTex);
            } else {
                renderer.renderFaceZPos(
                    block,
                    (double) (x + leafOffset2),
                    (double) (y + leafOffsetY),
                    (double) (z - leafOffset1),
                    leafTex);
                renderer.renderFaceZNeg(
                    block,
                    (double) (x + leafOffset2),
                    (double) (y + leafOffsetY),
                    (double) (z + leafOffset1),
                    leafTex);
                renderer.renderFaceZPos(
                    block,
                    (double) (x - leafOffset2),
                    (double) (y + leafOffsetY),
                    (double) (z - leafOffset1),
                    leafFlippedTex);
                renderer.renderFaceZNeg(
                    block,
                    (double) (x - leafOffset2),
                    (double) (y + leafOffsetY),
                    (double) (z + leafOffset1),
                    leafFlippedTex);
            }
        }
        return false;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return false;
    }

    public int getRenderId() {
        return TCRenderIDs.bambooChute;
    }
}
