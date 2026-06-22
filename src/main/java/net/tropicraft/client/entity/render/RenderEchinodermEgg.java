package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderEchinodermEgg extends Render {

    public void doRender(final Entity entity, final double x, final double y, final double z, final float yaw,
        final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glEnable(32826);
        this.bindEntityTexture(entity);
        final Tessellator tessellator = Tessellator.instance;
        GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glScalef(0.25f, 0.25f, 0.25f);
        final float f = 0.0f;
        final float f2 = 1.0f;
        final float f3 = 0.0f;
        final float f4 = 1.0f;
        final float f5 = 1.0f;
        final float f6 = 0.5f;
        final float f7 = 0.25f;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV((double) (0.0f - f6), (double) (0.0f - f7), 0.0, (double) f, (double) f4);
        tessellator.addVertexWithUV((double) (f5 - f6), (double) (0.0f - f7), 0.0, (double) f2, (double) f4);
        tessellator.addVertexWithUV((double) (f5 - f6), (double) (f5 - f7), 0.0, (double) f2, (double) f3);
        tessellator.addVertexWithUV((double) (0.0f - f6), (double) (f5 - f7), 0.0, (double) f, (double) f3);
        tessellator.draw();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("seaurchinegg");
    }
}
