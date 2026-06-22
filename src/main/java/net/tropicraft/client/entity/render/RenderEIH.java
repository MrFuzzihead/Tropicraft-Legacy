package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelEIH;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderEIH extends RenderLiving {

    public RenderEIH(final ModelEIH modeleih, final float f) {
        super((ModelBase) modeleih, f);
    }

    public void doRender(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        super.doRender((EntityLiving) entityliving, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.doRender((EntityLiving) entity, d, d1, d2, f, f1);
    }

    protected void preRenderScale(final EntityEIH entityeih, final float f) {
        GL11.glScalef(2.0f, 1.75f, 2.0f);
    }

    protected void preRenderCallback(final EntityLivingBase entityliving, final float f) {
        this.preRenderScale((EntityEIH) entityliving, f);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        final EntityEIH eih = (EntityEIH) entity;
        final String texture_path = "eih/head" + (eih.isAngry() ? "angry" : (eih.isAwake() ? "aware" : "")) + "text";
        return TropicraftUtils.bindTextureEntity(texture_path);
    }
}
