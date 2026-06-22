package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelMarlin;
import net.tropicraft.entity.underdasea.EntityMarlin;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderMarlin extends RenderLiving {

    private ModelMarlin marlin;

    public RenderMarlin(final ModelBase modelbase, final float f) {
        super(modelbase, f);
        this.marlin = (ModelMarlin) this.mainModel;
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("marlin");
    }

    public void renderMarlin(final EntityMarlin entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.marlin.inWater = entityliving.isInWater();
        GL11.glPushMatrix();
        GL11.glDisable(2884);
        this.mainModel.onGround = this.renderSwingProgress((EntityLivingBase) entityliving, f1);
        if (this.renderPassModel != null) {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }
        this.mainModel.isRiding = entityliving.isRiding();
        if (this.renderPassModel != null) {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }
        this.mainModel.isChild = entityliving.isChild();
        if (this.renderPassModel != null) {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }
        try {
            final float f2 = entityliving.prevRenderYawOffset
                + (entityliving.renderYawOffset - entityliving.prevRenderYawOffset) * f1;
            final float f3 = entityliving.prevRotationYaw
                + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f1;
            final float f4 = entityliving.prevRotationPitch
                + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f1;
            this.renderLivingAt((EntityLivingBase) entityliving, d, d1, d2);
            final float f5 = this.handleRotationFloat((EntityLivingBase) entityliving, f1);
            this.rotateCorpse((EntityLivingBase) entityliving, f5, f2, f1);
            if (entityliving.isInWater()) {
                GL11.glRotatef(entityliving.rotationPitch, 1.0f, 0.0f, 0.0f);
            } else {
                GL11.glRotatef(
                    (float) Math.toRadians(
                        (entityliving.motionY * 45.0 < 0.0) ? (-(entityliving.motionY * 45.0))
                            : (entityliving.motionY * 45.0)),
                    1.0f,
                    0.0f,
                    0.0f);
            }
            if (!entityliving.isInWater()) {
                float rot = 0.0f;
                if (entityliving.outOfWaterTick * 4 < 91 && entityliving.getHookID() == -1) {
                    rot = entityliving.outOfWaterTick * 4.0f;
                } else {
                    rot = 90.0f;
                }
                GL11.glTranslatef(0.0f, 0.125f, 0.0f);
                GL11.glRotatef(rot, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(entityliving.rotationPitch, 0.0f, 1.0f, 0.0f);
            }
            final float f6 = 0.0625f;
            GL11.glEnable(32826);
            GL11.glScalef(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback((EntityLivingBase) entityliving, f1);
            GL11.glTranslatef(0.0f, -24.0f * f6 - 0.0078125f, 0.0f);
            float f7 = entityliving.prevLimbSwingAmount
                + (entityliving.limbSwingAmount - entityliving.prevLimbSwingAmount) * f1;
            float f8 = entityliving.limbSwing - entityliving.limbSwingAmount * (1.0f - f1);
            if (entityliving.isChild()) {
                f8 *= 3.0f;
            }
            if (f7 > 1.0f) {
                f7 = 1.0f;
            }
            GL11.glEnable(3008);
            this.mainModel.setLivingAnimations((EntityLivingBase) entityliving, f8, f7, f1);
            this.renderModel((EntityLivingBase) entityliving, f8, f7, f5, f3 - f2, f4, f6);
            for (int i = 0; i < 4; ++i) {
                final int j = this.shouldRenderPass((EntityLivingBase) entityliving, i, f1);
                if (j > 0) {
                    this.renderPassModel.render((Entity) entityliving, f8, f7, f5, f3 - f2, f4, f6);
                    if (j == 15) {
                        final float f9 = entityliving.ticksExisted + f1;
                        this.bindEntityTexture((Entity) entityliving);
                        GL11.glEnable(3042);
                        final float f10 = 0.5f;
                        GL11.glColor4f(f10, f10, f10, 1.0f);
                        GL11.glDepthFunc(514);
                        GL11.glDepthMask(false);
                        for (int i2 = 0; i2 < 2; ++i2) {
                            GL11.glDisable(2896);
                            final float f11 = 0.76f;
                            GL11.glColor4f(0.5f * f11, 0.25f * f11, 0.8f * f11, 1.0f);
                            GL11.glBlendFunc(768, 1);
                            GL11.glMatrixMode(5890);
                            GL11.glLoadIdentity();
                            final float f12 = f9 * (0.001f + i2 * 0.003f) * 20.0f;
                            final float f13 = 0.3333333f;
                            GL11.glScalef(f13, f13, f13);
                            GL11.glRotatef(30.0f - i2 * 60.0f, 0.0f, 0.0f, 1.0f);
                            GL11.glTranslatef(0.0f, f12, 0.0f);
                            GL11.glMatrixMode(5888);
                            this.renderPassModel.render((Entity) entityliving, f8, f7, f5, f3 - f2, f4, f6);
                        }
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        GL11.glMatrixMode(5890);
                        GL11.glDepthMask(true);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(5888);
                        GL11.glEnable(2896);
                        GL11.glDisable(3042);
                        GL11.glDepthFunc(515);
                    }
                    GL11.glDisable(3042);
                    GL11.glEnable(3008);
                }
            }
            this.renderEquippedItems((EntityLivingBase) entityliving, f1);
            final float f14 = entityliving.getBrightness(f1);
            final int k = this.getColorMultiplier((EntityLivingBase) entityliving, f14, f1);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(3553);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            if ((k >> 24 & 0xFF) > 0 || entityliving.hurtTime > 0 || entityliving.deathTime > 0) {
                GL11.glDisable(3553);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if (entityliving.hurtTime > 0 || entityliving.deathTime > 0) {
                    GL11.glColor4f(f14, 0.0f, 0.0f, 0.4f);
                    this.mainModel.render((Entity) entityliving, f8, f7, f5, f3 - f2, f4, f6);
                    for (int l = 0; l < 4; ++l) {
                        if (this.inheritRenderPass((EntityLivingBase) entityliving, l, f1) >= 0) {
                            GL11.glColor4f(f14, 0.0f, 0.0f, 0.4f);
                            this.renderPassModel.render((Entity) entityliving, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }
                }
                if ((k >> 24 & 0xFF) > 0) {
                    final float f15 = (k >> 16 & 0xFF) / 255.0f;
                    final float f16 = (k >> 8 & 0xFF) / 255.0f;
                    final float f17 = (k & 0xFF) / 255.0f;
                    final float f18 = (k >> 24 & 0xFF) / 255.0f;
                    GL11.glColor4f(f15, f16, f17, f18);
                    this.mainModel.render((Entity) entityliving, f8, f7, f5, f3 - f2, f4, f6);
                    for (int j2 = 0; j2 < 4; ++j2) {
                        if (this.inheritRenderPass((EntityLivingBase) entityliving, j2, f1) >= 0) {
                            GL11.glColor4f(f15, f16, f17, f18);
                            this.renderPassModel.render((Entity) entityliving, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }
                }
                GL11.glDepthFunc(515);
                GL11.glDisable(3042);
                GL11.glEnable(3008);
                GL11.glEnable(3553);
            }
            GL11.glDisable(32826);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        this.passSpecialRender((EntityLivingBase) entityliving, d, d1, d2);
    }

    public void doRender(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.renderMarlin((EntityMarlin) entityliving, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderMarlin((EntityMarlin) entity, d, d1, d2, f, f1);
    }

    protected void preRenderCallback(final EntityLivingBase entityliving, final float f) {
        this.preRenderScale((EntityMarlin) entityliving, f);
    }

    protected void preRenderScale(final EntityMarlin entitymarlin, final float f) {
        GL11.glScalef(1.5f, 1.5f, 1.5f);
    }
}
