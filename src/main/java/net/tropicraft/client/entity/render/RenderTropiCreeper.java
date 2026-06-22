package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelTropiCreeper;
import net.tropicraft.entity.hostile.EntityTropiCreeper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderTropiCreeper extends RenderLiving {

    private ModelBase field_27008_a;
    private static final ResourceLocation armoredCreeperTextures;

    public RenderTropiCreeper() {
        super((ModelBase) new ModelTropiCreeper(), 0.5f);
        this.field_27008_a = (ModelBase) new ModelTropiCreeper();
    }

    protected void updateCreeperScale(final EntityTropiCreeper par1EntityVolleyballCreeper, final float par2) {
        float var4 = par1EntityVolleyballCreeper.getCreeperFlashIntensity(par2);
        final float var5 = 1.0f + MathHelper.sin(var4 * 100.0f) * var4 * 0.01f;
        if (var4 < 0.0f) {
            var4 = 0.0f;
        }
        if (var4 > 1.0f) {
            var4 = 1.0f;
        }
        var4 *= var4;
        var4 *= var4;
        final float var6 = (1.0f + var4 * 0.4f) * var5;
        final float var7 = (1.0f + var4 * 0.1f) / var5;
        GL11.glScalef(var6, var7, var6);
    }

    protected int updateCreeperColorMultiplier(final EntityTropiCreeper par1EntityVolleyballCreeper, final float par2,
        final float par3) {
        final float var5 = par1EntityVolleyballCreeper.getCreeperFlashIntensity(par3);
        if ((int) (var5 * 10.0f) % 2 == 0) {
            return 0;
        }
        int var6 = (int) (var5 * 0.2f * 255.0f);
        if (var6 < 0) {
            var6 = 0;
        }
        if (var6 > 255) {
            var6 = 255;
        }
        final short var7 = 255;
        final short var8 = 255;
        final short var9 = 255;
        return var6 << 24 | var7 << 16 | var8 << 8 | var9;
    }

    protected int func_27006_a(final EntityTropiCreeper par1EntityVolleyballCreeper, final int par2, final float par3) {
        if (par1EntityVolleyballCreeper.getPowered()) {
            if (par2 == 1) {
                final float var4 = par1EntityVolleyballCreeper.ticksExisted + par3;
                this.renderManager.renderEngine.bindTexture(RenderTropiCreeper.armoredCreeperTextures);
                GL11.glMatrixMode(5890);
                GL11.glLoadIdentity();
                final float var5 = var4 * 0.01f;
                final float var6 = var4 * 0.01f;
                GL11.glTranslatef(var5, var6, 0.0f);
                this.setRenderPassModel(this.field_27008_a);
                GL11.glMatrixMode(5888);
                GL11.glEnable(3042);
                final float var7 = 0.5f;
                GL11.glColor4f(var7, var7, var7, 1.0f);
                GL11.glDisable(2896);
                GL11.glBlendFunc(1, 1);
                return 1;
            }
            if (par2 == 2) {
                GL11.glMatrixMode(5890);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(5888);
                GL11.glEnable(2896);
                GL11.glDisable(3042);
            }
        }
        return -1;
    }

    protected int func_27007_b(final EntityTropiCreeper par1EntityVolleyballCreeper, final int par2, final float par3) {
        return -1;
    }

    protected void preRenderCallback(final EntityLivingBase par1EntityLiving, final float par2) {
        this.updateCreeperScale((EntityTropiCreeper) par1EntityLiving, par2);
    }

    protected int getColorMultiplier(final EntityLivingBase par1EntityLiving, final float par2, final float par3) {
        return this.updateCreeperColorMultiplier((EntityTropiCreeper) par1EntityLiving, par2, par3);
    }

    protected int shouldRenderPass(final EntityLivingBase par1EntityLiving, final int par2, final float par3) {
        return this.func_27006_a((EntityTropiCreeper) par1EntityLiving, par2, par3);
    }

    protected int inheritRenderPass(final EntityLivingBase par1EntityLiving, final int par2, final float par3) {
        return this.func_27007_b((EntityTropiCreeper) par1EntityLiving, par2, par3);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("creeper");
    }

    static {
        armoredCreeperTextures = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }
}
