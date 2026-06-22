package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelManOWar;
import net.tropicraft.entity.underdasea.EntityManOWar;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderManOWar extends RenderLiving {

    private ModelBase scaleAmount;

    public RenderManOWar(final ModelBase modelbase, final ModelBase modelbase1, final float f) {
        super(modelbase, f);
        this.mainModel = modelbase;
        this.scaleAmount = modelbase1;
    }

    protected int func_40287_a(final EntityManOWar manowar, final int i, final float f) {
        if (i == 0) {
            this.setRenderPassModel(this.scaleAmount);
            GL11.glEnable(2977);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            return 1;
        }
        if (i == 1) {
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        return -1;
    }

    public void renderManOWar(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        super.doRender(entityliving, d, d1, d2, f, f1);
        ((ModelManOWar) this.mainModel).isOnGround = entityliving.onGround;
    }

    protected int shouldRenderPass(final EntityLivingBase entityliving, final int i, final float f) {
        return this.func_40287_a((EntityManOWar) entityliving, i, f);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("manowar");
    }
}
