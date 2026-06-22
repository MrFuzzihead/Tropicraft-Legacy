package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSeaUrchin;
import net.tropicraft.entity.underdasea.EntitySeaUrchin;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderSeaUrchin extends RenderLiving {

    public static final float BABY_RENDER_SCALE = 0.5f;
    public static final float ADULT_RENDER_SCALE = 1.0f;

    public RenderSeaUrchin() {
        super((ModelBase) new ModelSeaUrchin(), 0.5f);
    }

    public void doRender(final EntityLiving par1EntityLiving, final double par2, final double par4, final double par6,
        final float par8, final float par9) {
        super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
    }

    protected void preRenderCallback(final EntityLivingBase par1EntityLiving, final float par2) {
        final EntitySeaUrchin urchin = (EntitySeaUrchin) par1EntityLiving;
        final float growthProgress = urchin.getGrowthProgress();
        final float scale = 0.5f + growthProgress * 0.5f;
        final float preScale = 0.5f;
        GL11.glScalef(preScale * scale, preScale * scale, preScale * scale);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("seaurchin");
    }
}
