package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.hostile.EntityLostMask;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderLostMask extends Render {

    protected MaskRenderer mask;

    public RenderLostMask() {
        this.shadowSize = 0.5f;
        this.mask = new MaskRenderer();
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("ashen/mask");
    }

    public void renderLostMask(final EntityLostMask entity, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.bindEntityTexture((Entity) entity);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glRotatef(f, 0.0f, 1.0f, 0.0f);
        if (!entity.onGround && !entity.checkForWater(0) && !entity.checkForWater(-1)) {
            final int[] a = entity.getRotator();
            GL11.glRotatef(a[0] + f1, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(a[1] + f1, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(a[2] + f1, 0.0f, 0.0f, 1.0f);
        } else {
            GL11.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        }
        this.mask.renderMask(entity.getColor());
        GL11.glPopMatrix();
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderLostMask((EntityLostMask) entity, d, d1, d2, f, f1);
    }

    private void lightingHelper(final EntityLostMask mask, final float f, final float f1) {
        int i = MathHelper.floor_double(mask.posX);
        final int j = MathHelper.floor_double(mask.posY + f1 / 16.0f);
        int k = MathHelper.floor_double(mask.posZ);
        final int direction = mask.getDirection();
        if (direction == 0) {
            i = MathHelper.floor_double(mask.posX + f / 16.0f);
        }
        if (direction == 1) {
            k = MathHelper.floor_double(mask.posZ - f / 16.0f);
        }
        if (direction == 2) {
            i = MathHelper.floor_double(mask.posX - f / 16.0f);
        }
        if (direction == 3) {
            k = MathHelper.floor_double(mask.posZ + f / 16.0f);
        }
        final int l = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        final int i2 = l % 65536;
        final int j2 = l / 65536;
        final int var7 = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        final int var8 = var7 % 65536;
        final int var9 = var7 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var8, (float) var9);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }
}
