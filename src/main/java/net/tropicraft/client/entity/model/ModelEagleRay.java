package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.tropicraft.entity.underdasea.EntityEagleRay;

import org.lwjgl.opengl.GL11;

public class ModelEagleRay extends ModelBase {

    private float[] interpolatedWingAmplitudes;
    private ModelRenderer body;

    public ModelEagleRay() {
        this.interpolatedWingAmplitudes = new float[10];
        this.textureWidth = 128;
        this.textureHeight = 64;
        (this.body = new ModelRenderer((ModelBase) this, 32, 0)).addBox(-2.0f, 0.0f, 0.0f, 5, 3, 32);
        this.body.setRotationPoint(0.0f, 0.0f, -8.0f);
        this.body.setTextureSize(128, 64);
        this.body.mirror = true;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.body.render(f5);
        this.renderWings();
        this.renderTailSimple();
    }

    private void renderTailSimple() {
        final Tessellator tessellator = Tessellator.instance;
        final float minU = 0.75f;
        final float maxU = 1.0f;
        final float minV = 0.0f;
        final float maxV = 0.5f;
        GL11.glPushMatrix();
        GL11.glTranslatef(0.55f, 0.0f, 1.5f);
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glScalef(1.5f, 1.0f, 1.0f);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double) minU, (double) minV);
        tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double) minU, (double) maxV);
        tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double) maxU, (double) maxV);
        tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double) maxU, (double) minV);
        tessellator.draw();
        GL11.glPopMatrix();
    }

    private void renderWings() {
        GL11.glDisable(2896);
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 0.5f, 2.0f);
        GL11.glTranslatef(0.1f, 0.0f, -0.25f);
        this.renderWing(false);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(0.1f, 0.0f, -1.0f);
        this.renderWing(true);
        GL11.glPopMatrix();
        GL11.glEnable(2896);
    }

    private void renderWing(final boolean reverse) {
        final Tessellator tessellator = Tessellator.instance;
        final float minUFront = 0.0f;
        final float maxUFront = 0.25f;
        final float minVFront = 0.0f;
        final float maxVFront = 0.5f;
        final float minUBack = 0.0f;
        final float maxUBack = 0.25f;
        final float minVBack = 0.5f;
        final float maxVBack = 1.0f;
        for (int i = 1; i < 10; ++i) {
            final float prevAmplitude = this.interpolatedWingAmplitudes[i - 1];
            final float amplitude = this.interpolatedWingAmplitudes[i];
            final float prevX = (i - 1) / 9.0f;
            final float x = i / 9.0f;
            final float prevUFront = minUFront + (maxUFront - minUFront) * prevX;
            final float uFront = minUFront + (maxUFront - minUFront) * x;
            final float prevUBack = minUBack + (maxUBack - minUBack) * prevX;
            final float uBack = minUBack + (maxUBack - minUBack) * x;
            final float offset = -0.001f;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(
                (double) x,
                (double) (amplitude - offset),
                0.0,
                (double) uBack,
                reverse ? ((double) maxVBack) : ((double) minVBack));
            tessellator.addVertexWithUV(
                (double) x,
                (double) (amplitude - offset),
                1.0,
                (double) uBack,
                reverse ? ((double) minVBack) : ((double) maxVBack));
            tessellator.addVertexWithUV(
                (double) prevX,
                (double) (prevAmplitude - offset),
                1.0,
                (double) prevUBack,
                reverse ? ((double) minVBack) : ((double) maxVBack));
            tessellator.addVertexWithUV(
                (double) prevX,
                (double) (prevAmplitude - offset),
                0.0,
                (double) prevUBack,
                reverse ? ((double) maxVBack) : ((double) minVBack));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(
                (double) prevX,
                (double) prevAmplitude,
                0.0,
                (double) prevUFront,
                reverse ? ((double) maxVFront) : ((double) minVFront));
            tessellator.addVertexWithUV(
                (double) prevX,
                (double) prevAmplitude,
                1.0,
                (double) prevUFront,
                reverse ? ((double) minVFront) : ((double) maxVFront));
            tessellator.addVertexWithUV(
                (double) x,
                (double) amplitude,
                1.0,
                (double) uFront,
                reverse ? ((double) minVFront) : ((double) maxVFront));
            tessellator.addVertexWithUV(
                (double) x,
                (double) amplitude,
                0.0,
                (double) uFront,
                reverse ? ((double) maxVFront) : ((double) minVFront));
            tessellator.draw();
        }
    }

    private float lerp(final float start, final float end, final float partialTicks) {
        return start + (end - start) * partialTicks;
    }

    public void setLivingAnimations(final EntityLivingBase entityLiving, final float par2, final float par3,
        final float partialTicks) {
        final EntityEagleRay ray = (EntityEagleRay) entityLiving;
        final float[] prevWingAmplitudes = ray.getPrevWingAmplitudes();
        final float[] wingAmplitudes = ray.getWingAmplitudes();
        for (int i = 1; i < 10; ++i) {
            final float prevWingAmplitude = prevWingAmplitudes[i];
            final float wingAmplitude = wingAmplitudes[i];
            this.interpolatedWingAmplitudes[i] = prevWingAmplitude + partialTicks * (wingAmplitude - prevWingAmplitude);
        }
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
