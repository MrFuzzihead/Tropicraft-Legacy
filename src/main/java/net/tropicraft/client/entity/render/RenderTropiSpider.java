package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.hostile.SpiderBase;
import net.tropicraft.entity.hostile.SpiderChild;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTropiSpider extends RenderLiving {

    public RenderTropiSpider() {
        super((ModelBase) new ModelSpider(), 1.0f);
        this.setRenderPassModel((ModelBase) new ModelSpider());
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        if (entity instanceof SpiderChild) {
            return TropicraftUtils.bindTextureEntity("spiderchild");
        }
        final boolean isMother = entity.getDataWatcher()
            .getWatchableObjectInt(25) == 1;
        if (isMother) {
            return TropicraftUtils.bindTextureEntity("spidermother");
        }
        return TropicraftUtils.bindTextureEntity("spideradult");
    }

    protected float setSpiderDeathMaxRotation(final SpiderBase par1EntitySpider) {
        return 180.0f;
    }

    protected int setSpiderEyeBrightness(final SpiderBase par1EntitySpider, final int par2, final float par3) {
        if (par2 != 0) {
            return -1;
        }
        this.renderManager.renderEngine.bindTexture(new ResourceLocation("textures/entity/spider_eyes.png"));
        final float f1 = 1.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(1, 1);
        if (par1EntitySpider.isInvisible()) {
            GL11.glDepthMask(false);
        } else {
            GL11.glDepthMask(true);
        }
        final char c0 = '\uf0f0';
        final int j = c0 % 65536;
        final int k = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f1);
        return 1;
    }

    protected void scaleSpider(final SpiderBase par1EntitySpider, final float par2) {
        final float f1 = par1EntitySpider.spiderScaleAmount();
        GL11.glScalef(f1, f1, f1);
    }

    protected void preRenderCallback(final EntityLivingBase par1EntityLiving, final float par2) {
        this.scaleSpider((SpiderBase) par1EntityLiving, par2);
    }

    protected float getDeathMaxRotation(final EntityLivingBase par1EntityLiving) {
        return this.setSpiderDeathMaxRotation((SpiderBase) par1EntityLiving);
    }

    protected int shouldRenderPass(final EntityLivingBase par1EntityLiving, final int par2, final float par3) {
        return this.setSpiderEyeBrightness((SpiderBase) par1EntityLiving, par2, par3);
    }
}
