package net.tropicraft.client.entity.render;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelChair;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderChair extends Render {

    protected ModelBase modelChair;
    FloatBuffer color;
    float red;
    float green;
    float blue;
    float alpha;

    public RenderChair() {
        this.red = 0.0f;
        this.green = 0.0f;
        this.blue = 0.0f;
        this.alpha = 1.0f;
        this.shadowSize = 0.5f;
        this.modelChair = (ModelBase) new ModelChair();
    }

    public void renderChair(final EntityChair entitychair, final double d, final double d1, final double d2,
        final float f, final float f1) {
        GL11.glPushMatrix();
        this.red = ColorHelper.getRed(entitychair.getColor());
        this.green = ColorHelper.getGreen(entitychair.getColor());
        this.blue = ColorHelper.getBlue(entitychair.getColor());
        GL11.glTranslatef((float) d, (float) d1 + 0.3125f, (float) d2);
        GL11.glRotatef(f + (180.0f - f) * 2.0f, 0.0f, 1.0f, 0.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftUtils.getTextureEntity("chairLayer"));
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        this.modelChair.render((Entity) entitychair, 0.0f, 1.0f, 0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTexEnvf(8960, 8704, 3042.0f);
        (this.color = BufferUtils.createFloatBuffer(4)
            .put(new float[] { this.red, this.green, this.blue, this.alpha })).position(0);
        GL11.glTexEnv(8960, 8705, this.color);
        Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftUtils.getTextureEntity("chairColorLayer"));
        this.modelChair.render((Entity) entitychair, 0.0f, 1.0f, 0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glDisable(3042);
        GL11.glTexEnvf(8960, 8704, 8448.0f);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderChair((EntityChair) entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.getTextureEntity("chairBlue");
    }
}
