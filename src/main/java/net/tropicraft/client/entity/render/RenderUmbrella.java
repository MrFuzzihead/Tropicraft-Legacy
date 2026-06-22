package net.tropicraft.client.entity.render;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelUmbrella;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderUmbrella extends Render {

    protected ModelBase modelUmbrella;
    FloatBuffer color;
    float red;
    float green;
    float blue;
    float alpha;

    public RenderUmbrella() {
        this.red = 0.0f;
        this.green = 0.0f;
        this.blue = 0.0f;
        this.alpha = 1.0f;
        this.shadowSize = 2.0f;
        this.modelUmbrella = (ModelBase) new ModelUmbrella();
    }

    public void renderUmbrella(final EntityUmbrella entityUmbrella, final double d, final double d1, final double d2,
        final float f, final float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glRotatef(180.0f - f, 0.0f, 1.0f, 0.0f);
        final float f2 = entityUmbrella.getTimeSinceHit() - f1;
        float f3 = entityUmbrella.getDamage() - f1;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f2 > 0.0f) {
            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0f * entityUmbrella.getRockDirection(), 1.0f, 0.0f, 0.0f);
        }
        final float f4 = 0.75f;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1.0f / f4, 1.0f / f4, 1.0f / f4);
        this.red = ColorHelper.getRed(entityUmbrella.getColor());
        this.green = ColorHelper.getGreen(entityUmbrella.getColor());
        this.blue = ColorHelper.getBlue(entityUmbrella.getColor());
        Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftUtils.getTextureEntity("umbrellaLayer"));
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        this.modelUmbrella.render((Entity) entityUmbrella, 0.0f, 1.0f, 0.1f, 0.0f, 0.0f, 0.25f);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTexEnvf(8960, 8704, 3042.0f);
        (this.color = BufferUtils.createFloatBuffer(4)
            .put(new float[] { this.red, this.green, this.blue, this.alpha })).position(0);
        GL11.glTexEnv(8960, 8705, this.color);
        Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftUtils.getTextureEntity("umbrellaColorLayer"));
        this.modelUmbrella.render((Entity) entityUmbrella, 0.0f, 1.0f, 0.1f, 0.0f, 0.0f, 0.25f);
        GL11.glDisable(3042);
        GL11.glTexEnvf(8960, 8704, 8448.0f);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderUmbrella((EntityUmbrella) entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return null;
    }
}
