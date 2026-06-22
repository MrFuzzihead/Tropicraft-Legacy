package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import CoroUtil.entity.EntityTropicalFishHook;

public class RenderFishHook extends Render {

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return new ResourceLocation("textures/particle/particles.png");
    }

    public void doRenderFish(final EntityTropicalFishHook entityfish, final double d, final double d1, final double d2,
        final float f, final float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glEnable(32826);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        final int i = 1;
        final byte byte0 = 2;
        this.bindEntityTexture((Entity) entityfish);
        final Tessellator tessellator = Tessellator.instance;
        final float f2 = (i * 8 + 0) / 128.0f;
        final float f3 = (i * 8 + 8) / 128.0f;
        final float f4 = (byte0 * 8 + 0) / 128.0f;
        final float f5 = (byte0 * 8 + 8) / 128.0f;
        final float f6 = 1.0f;
        final float f7 = 0.5f;
        final float f8 = 0.5f;
        GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV((double) (0.0f - f7), (double) (0.0f - f8), 0.0, (double) f2, (double) f5);
        tessellator.addVertexWithUV((double) (f6 - f7), (double) (0.0f - f8), 0.0, (double) f3, (double) f5);
        tessellator.addVertexWithUV((double) (f6 - f7), (double) (1.0f - f8), 0.0, (double) f3, (double) f4);
        tessellator.addVertexWithUV((double) (0.0f - f7), (double) (1.0f - f8), 0.0, (double) f2, (double) f4);
        tessellator.draw();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
        if (entityfish.angler != null) {
            final float f9 = (entityfish.angler.prevRotationYaw
                + (entityfish.angler.rotationYaw - entityfish.angler.prevRotationYaw) * f1) * 3.141593f / 180.0f;
            final double d3 = MathHelper.sin(f9);
            final double d4 = MathHelper.cos(f9);
            final float f10 = entityfish.angler.getSwingProgress(f1);
            final float f11 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593f);
            final Vec3 vec3d = Vec3.createVectorHelper(-0.5, 0.03, 0.8);
            double d5 = entityfish.angler.prevPosX + (entityfish.angler.posX - entityfish.angler.prevPosX) * f1
                + vec3d.xCoord;
            double d6 = entityfish.angler.prevPosY + (entityfish.angler.posY - entityfish.angler.prevPosY) * f1
                + vec3d.yCoord;
            double d7 = entityfish.angler.prevPosZ + (entityfish.angler.posZ - entityfish.angler.prevPosZ) * f1
                + vec3d.zCoord;
            if (this.renderManager.options.thirdPersonView <= 0) {}
            final float f12 = (entityfish.angler.prevRenderYawOffset
                + (entityfish.angler.renderYawOffset - entityfish.angler.prevRenderYawOffset) * f1) * 3.141593f
                / 180.0f;
            final double d8 = MathHelper.sin(f12);
            final double d9 = MathHelper.cos(f12);
            d5 = entityfish.angler.prevPosX + (entityfish.angler.posX - entityfish.angler.prevPosX) * f1
                - d9 * 0.35
                - d8 * 0.85;
            d6 = entityfish.angler.prevPosY + (entityfish.angler.posY - entityfish.angler.prevPosY) * f1 - 0.45;
            d7 = entityfish.angler.prevPosZ + (entityfish.angler.posZ - entityfish.angler.prevPosZ) * f1
                - d8 * 0.35
                + d9 * 0.85;
            final double d10 = entityfish.prevPosX + (entityfish.posX - entityfish.prevPosX) * f1;
            final double d11 = entityfish.prevPosY + (entityfish.posY - entityfish.prevPosY) * f1 - 1.0;
            final double d12 = entityfish.prevPosZ + (entityfish.posZ - entityfish.prevPosZ) * f1;
            final double d13 = (float) (d5 - d10);
            final double d14 = (float) (d6 - d11);
            final double d15 = (float) (d7 - d12);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            tessellator.startDrawing(3);
            tessellator.setColorOpaque_I(11184810);
            for (int j = 16, k = 0; k <= j; ++k) {
                final float f13 = k / (float) j;
                tessellator.addVertex(d + d13 * f13, d1 + d14 * (f13 * f13 + f13) * 0.5 + 0.25, d2 + d15 * f13);
            }
            tessellator.draw();
            GL11.glEnable(2896);
            GL11.glEnable(3553);
        }
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.doRenderFish((EntityTropicalFishHook) entity, d, d1, d2, f, f1);
    }
}
