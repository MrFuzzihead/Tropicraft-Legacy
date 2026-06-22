package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.BlockTropicraftFlowerPot;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCBlockRegistry;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class FlowerPotRenderHandler implements ISimpleBlockRenderingHandler {

    public void renderInventoryBlock(final Block block, final int metadata, final int modelId,
        final RenderBlocks renderer) {}

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
        final int modelId, final RenderBlocks renderer) {
        this.renderFlowerPot(world, x, y, z, (BlockTropicraftFlowerPot) block, renderer);
        return true;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return false;
    }

    public int getRenderId() {
        return TCRenderIDs.flowerPot;
    }

    private void renderFlowerPot(final IBlockAccess world, final int x, final int y, final int z,
        final BlockTropicraftFlowerPot block, final RenderBlocks rb) {
        rb.renderStandardBlock((Block) block, x, y, z);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        final float f = 1.0f;
        final int l = block.colorMultiplier(world, x, y, z);
        final IIcon icon = rb.getBlockIconFromSide((Block) block, 0);
        float f2 = (l >> 16 & 0xFF) / 255.0f;
        float f3 = (l >> 8 & 0xFF) / 255.0f;
        float f4 = (l & 0xFF) / 255.0f;
        if (EntityRenderer.anaglyphEnable) {
            final float f5 = (f2 * 30.0f + f3 * 59.0f + f4 * 11.0f) / 100.0f;
            final float f6 = (f2 * 30.0f + f3 * 70.0f) / 100.0f;
            final float f7 = (f2 * 30.0f + f4 * 70.0f) / 100.0f;
            f2 = f5;
            f3 = f6;
            f4 = f7;
        }
        tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
        final float f5 = 0.1865f;
        rb.renderFaceXPos((Block) block, (double) (x - 0.5f + f5), (double) y, (double) z, icon);
        rb.renderFaceXNeg((Block) block, (double) (x + 0.5f - f5), (double) y, (double) z, icon);
        rb.renderFaceZPos((Block) block, (double) x, (double) y, (double) (z - 0.5f + f5), icon);
        rb.renderFaceZNeg((Block) block, (double) x, (double) y, (double) (z + 0.5f - f5), icon);
        rb.renderFaceYPos(
            (Block) block,
            (double) x,
            (double) (y - 0.5f + f5 + 0.1875f),
            (double) z,
            rb.getBlockIcon(Blocks.dirt));
        final TileEntityTropicraftFlowerPot te = (TileEntityTropicraftFlowerPot) world.getTileEntity(x, y, z);
        if (te == null) {
            return;
        }
        final int var19 = te.getFlowerID();
        if (var19 != 0) {
            final float f7 = 0.0f;
            final float var20 = 4.0f;
            final float var21 = 0.0f;
            tessellator.addTranslation(f7 / 16.0f, var20 / 16.0f, var21 / 16.0f);
            if (var19 > 0 && var19 < 17) {
                rb.drawCrossedSquares(
                    this.draw((Block) TCBlockRegistry.flowers, var19 - 1),
                    (double) x,
                    (double) y,
                    (double) z,
                    0.75f);
            } else if (var19 == 17) {
                rb.drawCrossedSquares(
                    this.draw((Block) TCBlockRegistry.tallFlowers, 0),
                    (double) x,
                    (double) y,
                    (double) z,
                    0.75f);
                rb.drawCrossedSquares(
                    this.draw((Block) TCBlockRegistry.tallFlowers, 1),
                    (double) x,
                    y + 0.75,
                    (double) z,
                    0.75f);
            } else if (var19 == 18) {
                rb.drawCrossedSquares(
                    this.draw((Block) TCBlockRegistry.pineapple, 7),
                    (double) x,
                    (double) y,
                    (double) z,
                    0.75f);
                rb.drawCrossedSquares(
                    this.draw((Block) TCBlockRegistry.pineapple, 8),
                    (double) x,
                    y + 0.75,
                    (double) z,
                    0.75f);
            } else if (var19 > 18 && var19 < 25) {
                rb.drawCrossedSquares(
                    this.draw((Block) TCBlockRegistry.saplings, var19 - 19),
                    (double) x,
                    (double) y,
                    (double) z,
                    0.75f);
            } else if (var19 >= 25 && var19 < 34) {
                rb.drawCrossedSquares(
                    this.draw((Block) Blocks.red_flower, var19 - 25),
                    (double) x,
                    (double) y,
                    (double) z,
                    0.75f);
            } else if (var19 == 34) {
                rb.drawCrossedSquares(
                    this.draw((Block) Blocks.yellow_flower, var19 - 34),
                    (double) x,
                    (double) y,
                    (double) z,
                    0.75f);
            }
            tessellator.addTranslation(-f7 / 16.0f, -var20 / 16.0f, -var21 / 16.0f);
        }
    }

    private IIcon draw(final Block block, final int damage) {
        return block.getIcon(0, damage);
    }
}
