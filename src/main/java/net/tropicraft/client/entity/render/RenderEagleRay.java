package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelEagleRay;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderEagleRay extends RenderLiving {

    public RenderEagleRay() {
        super((ModelBase) new ModelEagleRay(), 1.0f);
    }

    protected void preRenderCallback(final EntityLivingBase par1EntityLiving, final float par2) {
        GL11.glTranslatef(0.0f, 1.25f, 0.0f);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("ray/eagleray");
    }
}
