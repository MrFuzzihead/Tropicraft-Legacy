package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelFailgull;
import net.tropicraft.util.TropicraftUtils;

public class RenderFailgull extends RenderLiving {

    public RenderFailgull(final float f) {
        super((ModelBase) new ModelFailgull(), f);
    }

    public void renderNew(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        super.doRender(entityliving, d, d1, d2, f, f1);
    }

    public void doRenderLiving(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.renderNew(entityliving, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderNew((EntityLiving) entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("failgull");
    }
}
