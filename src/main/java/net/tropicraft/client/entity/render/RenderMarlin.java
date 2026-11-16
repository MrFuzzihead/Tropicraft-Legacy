package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelMarlin;
import net.tropicraft.entity.underdasea.EntityMarlin;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderMarlin extends RenderWaterMobBase {

    private final ModelMarlin marlin;

    public RenderMarlin(final ModelBase modelbase, final float shadowSize) {
        super(modelbase, shadowSize);
        this.marlin = (ModelMarlin) this.mainModel;
    }

    @Override
    protected void renderWaterMob(final EntityLivingBase entity, final double x, final double y, final double z,
        final float yaw, final float partialTicks) {
        this.marlin.inWater = entity.isInWater();
        super.renderWaterMob(entity, x, y, z, yaw, partialTicks);
    }

    @Override
    protected void orientWaterMob(final EntityLivingBase entity, final float rotation, final float yawOffset,
        final float pitch, final float partialTicks) {
        final EntityMarlin marlin = (EntityMarlin) entity;
        this.rotateCorpse(entity, rotation, yawOffset, partialTicks);
        if (marlin.isInWater()) {
            GL11.glRotatef(marlin.rotationPitch, 1.0f, 0.0f, 0.0f);
        } else {
            // Nose follows the fall, so a marlin leaving the water arcs instead of staying level.
            final double motionPitch = marlin.motionY * 45.0;
            GL11.glRotatef((float) Math.toRadians(motionPitch < 0.0 ? -motionPitch : motionPitch), 1.0f, 0.0f, 0.0f);
        }
        if (!marlin.isInWater()) {
            final float outOfWaterRoll;
            if (marlin.outOfWaterTick * 4 < 91 && marlin.getHookID() == -1) {
                outOfWaterRoll = marlin.outOfWaterTick * 4.0f;
            } else {
                outOfWaterRoll = 90.0f;
            }
            GL11.glTranslatef(0.0f, 0.125f, 0.0f);
            GL11.glRotatef(outOfWaterRoll, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(marlin.rotationPitch, 0.0f, 1.0f, 0.0f);
        }
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("marlin");
    }

    protected void preRenderCallback(final EntityLivingBase entity, final float partialTicks) {
        GL11.glScalef(1.5f, 1.5f, 1.5f);
    }
}
