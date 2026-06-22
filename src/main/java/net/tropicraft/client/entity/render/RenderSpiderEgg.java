package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSpiderEgg;
import net.tropicraft.util.TropicraftUtils;

public class RenderSpiderEgg extends RenderLiving {

    protected ModelSpiderEgg modelVMonkey;

    public RenderSpiderEgg(final ModelSpiderEgg modelbase, final float f) {
        super((ModelBase) modelbase, f);
        this.modelVMonkey = modelbase;
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("spideregg");
    }
}
