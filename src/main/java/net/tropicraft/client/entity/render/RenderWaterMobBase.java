package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;

/**
 * The render pass shared by Tropicraft's water mobs.
 * <p>
 * {@code RenderMarlin}, {@code RenderSeaTurtle} and {@code RenderWaterMob} each carried their own ~150 line copy of
 * vanilla's {@code RendererLivingEntity#doRender} - the glint pass, the hurt/death overlay, the four render passes -
 * and differed only in how they oriented the model. That body lives here once; a subclass implements
 * {@link #orientWaterMob} and, if its model needs to know, {@link #renderWaterMob} to set the flag.
 * </p>
 * <p>
 * Every entry point that vanilla could use is overridden and each routes straight to {@link #renderWaterMob}, so the
 * body runs exactly once however the render manager gets here ({@code RenderLiving} chains
 * {@code doRender(Entity)} → {@code doRender(EntityLiving)} → {@code func_110827_b}). Preserved from the copies: the
 * name tag pass is not run, only {@code passSpecialRender}.
 * </p>
 */
public abstract class RenderWaterMobBase extends RenderLiving {

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
        "textures/misc/enchanted_item_glint.png");

    protected RenderWaterMobBase(final ModelBase modelbase, final float shadowSize) {
        super(modelbase, shadowSize);
    }

    @Override
    public void doRender(final Entity entity, final double x, final double y, final double z, final float yaw,
        final float partialTicks) {
        this.renderWaterMob((EntityLivingBase) entity, x, y, z, yaw, partialTicks);
    }

    @Override
    public void doRender(final EntityLivingBase entity, final double x, final double y, final double z, final float yaw,
        final float partialTicks) {
        this.renderWaterMob(entity, x, y, z, yaw, partialTicks);
    }

    @Override
    public void doRender(final EntityLiving entity, final double x, final double y, final double z, final float yaw,
        final float partialTicks) {
        this.renderWaterMob(entity, x, y, z, yaw, partialTicks);
    }

    @Override
    protected void func_110827_b(final EntityLiving entity, final double x, final double y, final double z,
        final float yaw, final float partialTicks) {
        // Reached only if something calls the base class's doRenderdirectly, which cannot happen for
        // these classes; routed to the same body so it can never render twice or render vanilla's version.
        this.renderWaterMob(entity, x, y, z, yaw, partialTicks);
    }

    protected void renderWaterMob(final EntityLivingBase entity, final double x, final double y, final double z,
        final float yaw, final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glDisable(2884);
        this.mainModel.onGround = this.renderSwingProgress(entity, partialTicks);
        if (this.renderPassModel != null) {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }
        this.mainModel.isRiding = entity.isRiding();
        if (this.renderPassModel != null) {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }
        this.mainModel.isChild = entity.isChild();
        if (this.renderPassModel != null) {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }
        try {
            final float yawOffset = entity.prevRenderYawOffset
                + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
            final float rotationYaw = entity.prevRotationYaw
                + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
            final float pitch = entity.prevRotationPitch
                + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            final float rotation = this.handleRotationFloat(entity, partialTicks);
            this.orientWaterMob(entity, rotation, yawOffset, pitch, partialTicks);
            final float pixel = 0.0625f;
            GL11.glEnable(32826);
            GL11.glScalef(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback(entity, partialTicks);
            GL11.glTranslatef(0.0f, -24.0f * pixel - 0.0078125f, 0.0f);
            float limbSwingAmount = entity.prevLimbSwingAmount
                + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
            float limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
            if (entity.isChild()) {
                limbSwing *= 3.0f;
            }
            if (limbSwingAmount > 1.0f) {
                limbSwingAmount = 1.0f;
            }
            GL11.glEnable(3008);
            this.mainModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            this.renderModel(entity, limbSwing, limbSwingAmount, rotation, rotationYaw - yawOffset, pitch, pixel);
            for (int pass = 0; pass < 4; ++pass) {
                final int passResult = this.shouldRenderPass(entity, pass, partialTicks);
                if (passResult > 0) {
                    this.renderPassModel
                        .render(entity, limbSwing, limbSwingAmount, rotation, rotationYaw - yawOffset, pitch, pixel);
                    if (passResult == 15) {
                        final float glintTime = entity.ticksExisted + partialTicks;
                        this.renderManager.renderEngine.bindTexture(RenderWaterMobBase.RES_ITEM_GLINT);
                        GL11.glEnable(3042);
                        final float glintBrightness = 0.5f;
                        GL11.glColor4f(glintBrightness, glintBrightness, glintBrightness, 1.0f);
                        GL11.glDepthFunc(514);
                        GL11.glDepthMask(false);
                        for (int glintPass = 0; glintPass < 2; ++glintPass) {
                            GL11.glDisable(2896);
                            final float glintScale = 0.76f;
                            GL11.glColor4f(0.5f * glintScale, 0.25f * glintScale, 0.8f * glintScale, 1.0f);
                            GL11.glBlendFunc(768, 1);
                            GL11.glMatrixMode(5890);
                            GL11.glLoadIdentity();
                            final float glintOffset = glintTime * (0.001f + glintPass * 0.003f) * 20.0f;
                            final float glintTiling = 0.3333333f;
                            GL11.glScalef(glintTiling, glintTiling, glintTiling);
                            GL11.glRotatef(30.0f - glintPass * 60.0f, 0.0f, 0.0f, 1.0f);
                            GL11.glTranslatef(0.0f, glintOffset, 0.0f);
                            GL11.glMatrixMode(5888);
                            this.renderPassModel.render(
                                entity,
                                limbSwing,
                                limbSwingAmount,
                                rotation,
                                rotationYaw - yawOffset,
                                pitch,
                                pixel);
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
            this.renderEquippedItems(entity, partialTicks);
            final float brightness = entity.getBrightness(partialTicks);
            final int color = this.getColorMultiplier(entity, brightness, partialTicks);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(3553);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            if ((color >> 24 & 0xFF) > 0 || entity.hurtTime > 0 || entity.deathTime > 0) {
                GL11.glDisable(3553);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if (entity.hurtTime > 0 || entity.deathTime > 0) {
                    GL11.glColor4f(brightness, 0.0f, 0.0f, 0.4f);
                    this.mainModel
                        .render(entity, limbSwing, limbSwingAmount, rotation, rotationYaw - yawOffset, pitch, pixel);
                    for (int pass = 0; pass < 4; ++pass) {
                        if (this.inheritRenderPass(entity, pass, partialTicks) >= 0) {
                            GL11.glColor4f(brightness, 0.0f, 0.0f, 0.4f);
                            this.renderPassModel.render(
                                entity,
                                limbSwing,
                                limbSwingAmount,
                                rotation,
                                rotationYaw - yawOffset,
                                pitch,
                                pixel);
                        }
                    }
                }
                if ((color >> 24 & 0xFF) > 0) {
                    final float red = (color >> 16 & 0xFF) / 255.0f;
                    final float green = (color >> 8 & 0xFF) / 255.0f;
                    final float blue = (color & 0xFF) / 255.0f;
                    final float alpha = (color >> 24 & 0xFF) / 255.0f;
                    GL11.glColor4f(red, green, blue, alpha);
                    this.mainModel
                        .render(entity, limbSwing, limbSwingAmount, rotation, rotationYaw - yawOffset, pitch, pixel);
                    for (int pass = 0; pass < 4; ++pass) {
                        if (this.inheritRenderPass(entity, pass, partialTicks) >= 0) {
                            GL11.glColor4f(red, green, blue, alpha);
                            this.renderPassModel.render(
                                entity,
                                limbSwing,
                                limbSwingAmount,
                                rotation,
                                rotationYaw - yawOffset,
                                pitch,
                                pixel);
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
            FMLLog.fine("[Tropicraft] Entity render failed: %s", exception);
        }
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        this.passSpecialRender(entity, x, y, z);
    }

    /**
     * Orients the model: called after the entity has been positioned and before the model is scaled.
     *
     * @param rotation     the value from {@code handleRotationFloat}, what vanilla hands to {@code rotateCorpse}
     * @param yawOffset    the interpolated render yaw offset
     * @param pitch        the interpolated render pitch
     * @param partialTicks the partial tick time
     */
    protected abstract void orientWaterMob(final EntityLivingBase entity, final float rotation, final float yawOffset,
        final float pitch, final float partialTicks);
}
