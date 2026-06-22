package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSeaTurtle;
import net.tropicraft.entity.underdasea.EntityAmphibian;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderSeaTurtle extends RenderLiving {

    public ModelSeaTurtle turtle;

    public RenderSeaTurtle(final ModelBase modelbase, final float f) {
        super(modelbase, f);
        this.turtle = (ModelSeaTurtle) modelbase;
    }

    public void renderTurtle(final EntityAmphibian entityTurtle, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.turtle.inWater = entityTurtle.isInWater();
        GL11.glPushMatrix();
        GL11.glDisable(2884);
        this.mainModel.onGround = this.renderSwingProgress((EntityLivingBase) entityTurtle, f1);
        if (this.renderPassModel != null) {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }
        this.mainModel.isRiding = entityTurtle.isRiding();
        if (this.renderPassModel != null) {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }
        this.mainModel.isChild = entityTurtle.isChild();
        if (this.renderPassModel != null) {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }
        try {
            final float f2 = entityTurtle.prevRenderYawOffset
                + (entityTurtle.renderYawOffset - entityTurtle.prevRenderYawOffset) * f1;
            final float f3 = entityTurtle.prevRotationYaw
                + (entityTurtle.rotationYaw - entityTurtle.prevRotationYaw) * f1;
            final float f4 = entityTurtle.prevRotationPitch
                + (entityTurtle.rotationPitch - entityTurtle.prevRotationPitch) * f1;
            this.renderLivingAt((EntityLivingBase) entityTurtle, d, d1, d2);
            final float f5 = this.handleRotationFloat((EntityLivingBase) entityTurtle, f1);
            this.rotateCorpse((EntityLivingBase) entityTurtle, f5, f2, f1);
            if (entityTurtle.isInWater()) {
                GL11.glRotatef(f4, 1.0f, 0.0f, 0.0f);
            }
            final float f6 = 0.0625f;
            GL11.glEnable(32826);
            GL11.glScalef(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback((EntityLivingBase) entityTurtle, f1);
            GL11.glTranslatef(0.0f, -24.0f * f6 - 0.0078125f, 0.0f);
            float f7 = entityTurtle.prevLimbSwingAmount
                + (entityTurtle.limbSwingAmount - entityTurtle.prevLimbSwingAmount) * f1;
            float f8 = entityTurtle.limbSwing - entityTurtle.limbSwingAmount * (1.0f - f1);
            if (entityTurtle.isChild()) {
                f8 *= 3.0f;
            }
            if (f7 > 1.0f) {
                f7 = 1.0f;
            }
            GL11.glEnable(3008);
            this.mainModel.setLivingAnimations((EntityLivingBase) entityTurtle, f8, f7, f1);
            this.renderModel((EntityLivingBase) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
            for (int i = 0; i < 4; ++i) {
                final int j = this.shouldRenderPass((EntityLivingBase) entityTurtle, i, f1);
                if (j > 0) {
                    this.renderPassModel.render((Entity) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                    if (j == 15) {
                        final float f9 = entityTurtle.ticksExisted + f1;
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
                            this.renderPassModel.render((Entity) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
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
            this.renderEquippedItems((EntityLivingBase) entityTurtle, f1);
            final float f14 = entityTurtle.getBrightness(f1);
            final int k = this.getColorMultiplier((EntityLivingBase) entityTurtle, f14, f1);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(3553);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            if ((k >> 24 & 0xFF) > 0 || entityTurtle.hurtTime > 0 || entityTurtle.deathTime > 0) {
                GL11.glDisable(3553);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if (entityTurtle.hurtTime > 0 || entityTurtle.deathTime > 0) {
                    GL11.glColor4f(f14, 0.0f, 0.0f, 0.4f);
                    this.mainModel.render((Entity) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                    for (int l = 0; l < 4; ++l) {
                        if (this.inheritRenderPass((EntityLivingBase) entityTurtle, l, f1) >= 0) {
                            GL11.glColor4f(f14, 0.0f, 0.0f, 0.4f);
                            this.renderPassModel.render((Entity) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }
                }
                if ((k >> 24 & 0xFF) > 0) {
                    final float f15 = (k >> 16 & 0xFF) / 255.0f;
                    final float f16 = (k >> 8 & 0xFF) / 255.0f;
                    final float f17 = (k & 0xFF) / 255.0f;
                    final float f18 = (k >> 24 & 0xFF) / 255.0f;
                    GL11.glColor4f(f15, f16, f17, f18);
                    this.mainModel.render((Entity) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                    for (int j2 = 0; j2 < 4; ++j2) {
                        if (this.inheritRenderPass((EntityLivingBase) entityTurtle, j2, f1) >= 0) {
                            GL11.glColor4f(f15, f16, f17, f18);
                            this.renderPassModel.render((Entity) entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
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
        this.passSpecialRender((EntityLivingBase) entityTurtle, d, d1, d2);
    }

    protected void preRenderCallback(final EntityLivingBase entityliving, final float f) {
        this.preRenderScale((EntityAmphibian) entityliving, f);
    }

    protected void preRenderScale(final EntityAmphibian entitymarlin, final float f) {
        final float f2 = (float) Math.log(entitymarlin.getAge() + 1.75) - 0.25f;
        GL11.glScalef(f2, f2, f2);
    }

    public void doRender(final EntityLiving entityTurtle, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.renderTurtle((EntityAmphibian) entityTurtle, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderTurtle((EntityAmphibian) entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("turtle/seaTurtle");
    }
}
