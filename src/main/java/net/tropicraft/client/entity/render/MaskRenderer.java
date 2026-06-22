package net.tropicraft.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

public class MaskRenderer {

    private Minecraft mc;

    public void renderMask(final int i) {
        GL11.glPushMatrix();
        final Tessellator tessellator = Tessellator.instance;
        final int j = i;
        final float f = (j % 8 * 32 + 0.0f) / 256.0f;
        final float f2 = (j % 8 * 32 + 31.99f) / 256.0f;
        final float f3 = (j / 8 * 32 + 0.0f) / 256.0f;
        final float f4 = (j / 8 * 32 + 31.99f) / 256.0f;
        final float f1shifted = (j / 8 * 32 + 128.0f) / 256.0f;
        final float f3shifted = (j / 8 * 32 + 159.99f) / 256.0f;
        final float f5 = 0.0f;
        final float f6 = 0.3f;
        GL11.glEnable(32826);
        GL11.glTranslatef(-f5, -f6, 0.0f);
        final float f7 = 1.7f;
        GL11.glScalef(f7, f7, f7);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
        this.popper(tessellator, f2, f3, f, f4, f1shifted, f3shifted);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    public void renderFish(final int i) {
        GL11.glPushMatrix();
        final Tessellator tessellator = Tessellator.instance;
        final int j = i;
        final float f = (j % 8 * 32 + 0.0f) / 256.0f;
        final float f2 = (j % 8 * 32 + 31.99f) / 256.0f;
        final float f3 = (j / 8 * 32 + 0.0f) / 256.0f;
        final float f4 = (j / 8 * 32 + 31.99f) / 256.0f;
        final float f5 = 0.0f;
        final float f6 = 0.3f;
        GL11.glEnable(32826);
        GL11.glTranslatef(-f5, -f6, 0.0f);
        final float f7 = 1.7f;
        GL11.glScalef(f7, f7, f7);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
        this.popper(tessellator, f2, f3, f, f4, f3, f4);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    public void renderItem(final IIcon icon) {
        GL11.glPushMatrix();
        final Tessellator tessellator = Tessellator.instance;
        final float f = icon.getMinU();
        final float f2 = icon.getMaxU();
        final float f3 = icon.getMinV();
        final float f4 = icon.getMaxV();
        final float f5 = 0.0f;
        final float f6 = 0.3f;
        GL11.glEnable(32826);
        GL11.glTranslatef(-f5, -f6, 0.0f);
        final float f7 = 0.5f;
        GL11.glScalef(f7, f7, f7);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
        this.popper2(tessellator, f2, f3, f, f4);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    public void renderItem(final int j) {
        GL11.glPushMatrix();
        final Tessellator tessellator = Tessellator.instance;
        final float f = (j % 16 * 16 + 0.0f) / 256.0f;
        final float f2 = (j % 16 * 16 + 15.99f) / 256.0f;
        final float f3 = (j / 16 * 16 + 0.0f) / 256.0f;
        final float f4 = (j / 16 * 16 + 15.99f) / 256.0f;
        final float f5 = 0.0f;
        final float f6 = 0.3f;
        GL11.glEnable(32826);
        GL11.glTranslatef(-f5, -f6, 0.0f);
        final float f7 = 0.5f;
        GL11.glScalef(f7, f7, f7);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
        this.popper2(tessellator, f2, f3, f, f4);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    private void popper(final Tessellator tessellator, final float f, final float f1, final float f2, final float f3,
        final float f1shifted, final float f3shifted) {
        final float f4 = 1.0f;
        final float f5 = 0.03125f;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double) f, (double) f3shifted);
        tessellator.addVertexWithUV((double) f4, 0.0, 0.0, (double) f2, (double) f3shifted);
        tessellator.addVertexWithUV((double) f4, 1.0, 0.0, (double) f2, (double) f1shifted);
        tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double) f, (double) f1shifted);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        tessellator.addVertexWithUV(0.0, 1.0, (double) (0.0f - f5), (double) f, (double) f1);
        tessellator.addVertexWithUV((double) f4, 1.0, (double) (0.0f - f5), (double) f2, (double) f1);
        tessellator.addVertexWithUV((double) f4, 0.0, (double) (0.0f - f5), (double) f2, (double) f3);
        tessellator.addVertexWithUV(0.0, 0.0, (double) (0.0f - f5), (double) f, (double) f3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        for (int i = 0; i < 32; ++i) {
            final float f6 = i / 32.0f;
            final float f7 = f + (f2 - f) * f6 - 0.001953125f;
            final float f8 = f4 * f6;
            tessellator.addVertexWithUV((double) f8, 0.0, (double) (0.0f - f5), (double) f7, (double) f3);
            tessellator.addVertexWithUV((double) f8, 0.0, 0.0, (double) f7, (double) f3);
            tessellator.addVertexWithUV((double) f8, 1.0, 0.0, (double) f7, (double) f1);
            tessellator.addVertexWithUV((double) f8, 1.0, (double) (0.0f - f5), (double) f7, (double) f1);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        for (int j = 0; j < 32; ++j) {
            final float f9 = j / 32.0f;
            final float f10 = f + (f2 - f) * f9 - 0.001953125f;
            final float f11 = f4 * f9 + 0.03125f;
            tessellator.addVertexWithUV((double) f11, 1.0, (double) (0.0f - f5), (double) f10, (double) f1);
            tessellator.addVertexWithUV((double) f11, 1.0, 0.0, (double) f10, (double) f1);
            tessellator.addVertexWithUV((double) f11, 0.0, 0.0, (double) f10, (double) f3);
            tessellator.addVertexWithUV((double) f11, 0.0, (double) (0.0f - f5), (double) f10, (double) f3);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        for (int k = 0; k < 32; ++k) {
            final float f12 = k / 32.0f;
            final float f13 = f3 + (f1 - f3) * f12 - 0.001953125f;
            final float f14 = f4 * f12 + 0.03125f;
            tessellator.addVertexWithUV(0.0, (double) f14, 0.0, (double) f, (double) f13);
            tessellator.addVertexWithUV((double) f4, (double) f14, 0.0, (double) f2, (double) f13);
            tessellator.addVertexWithUV((double) f4, (double) f14, (double) (0.0f - f5), (double) f2, (double) f13);
            tessellator.addVertexWithUV(0.0, (double) f14, (double) (0.0f - f5), (double) f, (double) f13);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        for (int l = 0; l < 32; ++l) {
            final float f15 = l / 32.0f;
            final float f16 = f3 + (f1 - f3) * f15 - 0.001953125f;
            final float f17 = f4 * f15;
            tessellator.addVertexWithUV((double) f4, (double) f17, 0.0, (double) f2, (double) f16);
            tessellator.addVertexWithUV(0.0, (double) f17, 0.0, (double) f, (double) f16);
            tessellator.addVertexWithUV(0.0, (double) f17, (double) (0.0f - f5), (double) f, (double) f16);
            tessellator.addVertexWithUV((double) f4, (double) f17, (double) (0.0f - f5), (double) f2, (double) f16);
        }
        tessellator.draw();
    }

    private void popper2(final Tessellator tessellator, final float f, final float f1, final float f2, final float f3) {
        final float f4 = 1.0f;
        final float f5 = 0.0625f;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double) f, (double) f3);
        tessellator.addVertexWithUV((double) f4, 0.0, 0.0, (double) f2, (double) f3);
        tessellator.addVertexWithUV((double) f4, 1.0, 0.0, (double) f2, (double) f1);
        tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double) f, (double) f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        tessellator.addVertexWithUV(0.0, 1.0, (double) (0.0f - f5), (double) f, (double) f1);
        tessellator.addVertexWithUV((double) f4, 1.0, (double) (0.0f - f5), (double) f2, (double) f1);
        tessellator.addVertexWithUV((double) f4, 0.0, (double) (0.0f - f5), (double) f2, (double) f3);
        tessellator.addVertexWithUV(0.0, 0.0, (double) (0.0f - f5), (double) f, (double) f3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        for (int i = 0; i < 16; ++i) {
            final float f6 = i / 16.0f;
            final float f7 = f + (f2 - f) * f6 - 0.001953125f;
            final float f8 = f4 * f6;
            tessellator.addVertexWithUV((double) f8, 0.0, (double) (0.0f - f5), (double) f7, (double) f3);
            tessellator.addVertexWithUV((double) f8, 0.0, 0.0, (double) f7, (double) f3);
            tessellator.addVertexWithUV((double) f8, 1.0, 0.0, (double) f7, (double) f1);
            tessellator.addVertexWithUV((double) f8, 1.0, (double) (0.0f - f5), (double) f7, (double) f1);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        for (int j = 0; j < 16; ++j) {
            final float f9 = j / 16.0f;
            final float f10 = f + (f2 - f) * f9 - 0.001953125f;
            final float f11 = f4 * f9 + 0.0625f;
            tessellator.addVertexWithUV((double) f11, 1.0, (double) (0.0f - f5), (double) f10, (double) f1);
            tessellator.addVertexWithUV((double) f11, 1.0, 0.0, (double) f10, (double) f1);
            tessellator.addVertexWithUV((double) f11, 0.0, 0.0, (double) f10, (double) f3);
            tessellator.addVertexWithUV((double) f11, 0.0, (double) (0.0f - f5), (double) f10, (double) f3);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        for (int k = 0; k < 16; ++k) {
            final float f12 = k / 16.0f;
            final float f13 = f3 + (f1 - f3) * f12 - 0.001953125f;
            final float f14 = f4 * f12 + 0.0625f;
            tessellator.addVertexWithUV(0.0, (double) f14, 0.0, (double) f, (double) f13);
            tessellator.addVertexWithUV((double) f4, (double) f14, 0.0, (double) f2, (double) f13);
            tessellator.addVertexWithUV((double) f4, (double) f14, (double) (0.0f - f5), (double) f2, (double) f13);
            tessellator.addVertexWithUV(0.0, (double) f14, (double) (0.0f - f5), (double) f, (double) f13);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        for (int l = 0; l < 16; ++l) {
            final float f15 = l / 16.0f;
            final float f16 = f3 + (f1 - f3) * f15 - 0.001953125f;
            final float f17 = f4 * f15;
            tessellator.addVertexWithUV((double) f4, (double) f17, 0.0, (double) f2, (double) f16);
            tessellator.addVertexWithUV(0.0, (double) f17, 0.0, (double) f, (double) f16);
            tessellator.addVertexWithUV(0.0, (double) f17, (double) (0.0f - f5), (double) f, (double) f16);
            tessellator.addVertexWithUV((double) f4, (double) f17, (double) (0.0f - f5), (double) f2, (double) f16);
        }
        tessellator.draw();
    }
}
