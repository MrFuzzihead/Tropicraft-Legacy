package net.tropicraft.client.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.client.entity.model.ModelAirCompressor;
import net.tropicraft.client.entity.model.ModelScubaTank;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class TileEntityAirCompressorRenderer extends TileEntitySpecialRenderer {

    private ModelAirCompressor model;
    private ModelScubaTank modelScubaTank;

    public TileEntityAirCompressorRenderer() {
        this.model = new ModelAirCompressor();
        this.modelScubaTank = new ModelScubaTank();
    }

    private void renderAirCompressor(final TileEntityAirCompressor te, final double x, final double y, final double z,
        final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
        if (te.getWorldObj() == null) {
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        } else {
            final int meta = te.getBlockMetadata();
            if (meta == 2) {
                GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
            } else if (meta == 3) {
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            } else if (meta == 4) {
                GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
            } else if (meta == 5) {
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        float angle = Float.MIN_VALUE;
        if (te.isCompressing()) {
            angle = MathHelper.sin((float) (31.41592653589793 * te.getTickRatio())) * 15.0f;
            GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
        }
        if ((angle <= -13.0f && angle != Float.MIN_VALUE) || angle >= 13.0f) {
            TropicraftUtils.bindTextureTE("airCompressorBlow");
        } else {
            TropicraftUtils.bindTextureTE("airCompressor");
        }
        this.model.renderAirCompressor();
        if (te.isCompressing()) {
            GL11.glPushMatrix();
            GL11.glScalef(1.1f, 1.1f, 1.1f);
            GL11.glTranslatef(-0.35f, 0.9f, 0.4f);
            GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
            TropicraftUtils.bindTextureArmor("scubaGearPink");
            this.modelScubaTank.renderBambooMug();
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
        final float var8) {
        this.renderAirCompressor((TileEntityAirCompressor) var1, var2, var4, var6, var8);
    }
}
