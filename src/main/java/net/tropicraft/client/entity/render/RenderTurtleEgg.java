package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.underdasea.EntityTurtleEgg;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderTurtleEgg extends RenderLiving {

    public RenderTurtleEgg(final ModelBase modelbase, final float f) {
        super(modelbase, f);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("turtle/eggText");
    }

    public void renderTurtleEgg(final EntityTurtleEgg entityTurtleEgg, final double d, final double d1, final double d2,
        final float f, final float f1) {
        super.doRender((EntityLiving) entityTurtleEgg, d, d1, d2, f, f1);
    }

    public void doRender(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.renderTurtleEgg((EntityTurtleEgg) entityliving, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderTurtleEgg((EntityTurtleEgg) entity, d, d1, d2, f, f1);
    }

    protected void preRenderScale(final EntityTurtleEgg egg, final float f) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
    }

    protected void preRenderCallback(final EntityLivingBase entityliving, final float f) {
        this.preRenderScale((EntityTurtleEgg) entityliving, f);
    }
}
