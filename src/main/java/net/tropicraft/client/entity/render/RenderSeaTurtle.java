package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSeaTurtle;
import net.tropicraft.entity.underdasea.EntityAmphibian;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderSeaTurtle extends RenderWaterMobBase {

    private final ModelSeaTurtle turtle;

    public RenderSeaTurtle(final ModelBase modelbase, final float shadowSize) {
        super(modelbase, shadowSize);
        this.turtle = (ModelSeaTurtle) modelbase;
    }

    @Override
    protected void renderWaterMob(final EntityLivingBase entity, final double x, final double y, final double z,
        final float yaw, final float partialTicks) {
        this.turtle.inWater = entity.isInWater();
        super.renderWaterMob(entity, x, y, z, yaw, partialTicks);
    }

    @Override
    protected void orientWaterMob(final EntityLivingBase entity, final float rotation, final float yawOffset,
        final float pitch, final float partialTicks) {
        this.rotateCorpse(entity, rotation, yawOffset, partialTicks);
        if (entity.isInWater()) {
            GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        }
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("turtle/seaTurtle");
    }

    /** Grows with age, so a baby turtle is not a scaled-down adult. */
    protected void preRenderCallback(final EntityLivingBase entity, final float partialTicks) {
        final EntityAmphibian turtle = (EntityAmphibian) entity;
        final float scale = (float) Math.log(turtle.getAge() + 1.75) - 0.25f;
        GL11.glScalef(scale, scale, scale);
    }
}
