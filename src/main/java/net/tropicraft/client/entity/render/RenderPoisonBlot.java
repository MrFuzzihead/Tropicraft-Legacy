package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderPoisonBlot extends Render {

    public void renderBlot(final EntityPoisonBlot entitypoisonblot, final double d, final double d1, final double d2,
        final float f, final float f1) {
        if (entitypoisonblot.prevRotationYaw == 0.0f && entitypoisonblot.prevRotationPitch == 0.0f) {
            return;
        }
        TropicraftUtils.bindTextureEntity("treefrog/blot");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glRotatef(
            entitypoisonblot.prevRotationYaw + (entitypoisonblot.rotationYaw - entitypoisonblot.prevRotationYaw) * f1
                - 90.0f,
            0.0f,
            1.0f,
            0.0f);
        GL11.glRotatef(
            entitypoisonblot.prevRotationPitch
                + (entitypoisonblot.rotationPitch - entitypoisonblot.prevRotationPitch) * f1,
            0.0f,
            0.0f,
            1.0f);
        final Tessellator tessellator = Tessellator.instance;
        final int i = 0;
        final float f2 = 0.0f;
        final float f3 = 0.5f;
        final float f4 = (0 + i * 10) / 32.0f;
        final float f5 = (5 + i * 10) / 32.0f;
        final float f6 = 0.0f;
        final float f7 = 0.15625f;
        final float f8 = (5 + i * 10) / 32.0f;
        final float f9 = (10 + i * 10) / 32.0f;
        final float f10 = 0.05625f;
        GL11.glEnable(32826);
        GL11.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4.0f, 0.0f, 0.0f);
        GL11.glNormal3f(f10, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0, -2.0, -2.0, (double) f6, (double) f8);
        tessellator.addVertexWithUV(-7.0, -2.0, 2.0, (double) f7, (double) f8);
        tessellator.addVertexWithUV(-7.0, 2.0, 2.0, (double) f7, (double) f9);
        tessellator.addVertexWithUV(-7.0, 2.0, -2.0, (double) f6, (double) f9);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0, 2.0, -2.0, (double) f6, (double) f8);
        tessellator.addVertexWithUV(-7.0, 2.0, 2.0, (double) f7, (double) f8);
        tessellator.addVertexWithUV(-7.0, -2.0, 2.0, (double) f7, (double) f9);
        tessellator.addVertexWithUV(-7.0, -2.0, -2.0, (double) f6, (double) f9);
        tessellator.draw();
        for (int j = 0; j < 4; ++j) {
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glNormal3f(0.0f, 0.0f, f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0, -2.0, 0.0, (double) f2, (double) f4);
            tessellator.addVertexWithUV(8.0, -2.0, 0.0, (double) f3, (double) f4);
            tessellator.addVertexWithUV(8.0, 2.0, 0.0, (double) f3, (double) f5);
            tessellator.addVertexWithUV(-8.0, 2.0, 0.0, (double) f2, (double) f5);
            tessellator.draw();
        }
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderBlot((EntityPoisonBlot) entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(final Entity var1) {
        return TropicraftUtils.getTextureEntity("treefrog/blot");
    }
}
