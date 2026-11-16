package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.underdasea.EntityTropicraftWaterMob;

import org.lwjgl.opengl.GL11;

/**
 * The generic water mob orientation: upright in water and flipped onto its side once it is out of it. Used directly by
 * {@link RenderTropicalFish}; {@link RenderMarlin} and {@link RenderSeaTurtle} have their own.
 */
public class RenderWaterMob extends RenderWaterMobBase {

    public RenderWaterMob(final ModelBase modelbase, final float shadowSize) {
        super(modelbase, shadowSize);
    }

    @Override
    protected void orientWaterMob(final EntityLivingBase entity, final float rotation, final float yawOffset,
        final float pitch, final float partialTicks) {
        final EntityTropicraftWaterMob mob = (EntityTropicraftWaterMob) entity;
        if (mob.isInWater() || mob.surfaceTick != 0) {
            this.rotateCorpse(entity, rotation, yawOffset, partialTicks);
            GL11.glRotatef(mob.rotationPitch, 1.0f, 0.0f, 0.0f);
        } else {
            GL11.glTranslatef(0.0f, 0.125f, 0.0f);
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(mob.rotationPitch, 0.0f, 1.0f, 0.0f);
        }
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return null;
    }
}
