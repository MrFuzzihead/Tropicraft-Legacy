package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.hostile.EntityTreeFrog;
import net.tropicraft.util.TropicraftUtils;

public class RenderTreeFrog extends RenderLiving {

    public RenderTreeFrog(final ModelBase modelbase, final float f) {
        super(modelbase, f);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        if (((EntityTreeFrog) entity).type == 0) {
            return TropicraftUtils.bindTextureEntity("treefrog/treefroggreen");
        }
        if (((EntityTreeFrog) entity).type == 1) {
            return TropicraftUtils.bindTextureEntity("treefrog/treefrogred");
        }
        if (((EntityTreeFrog) entity).type == 2) {
            return TropicraftUtils.bindTextureEntity("treefrog/treefrogblue");
        }
        return TropicraftUtils.bindTextureEntity("treefrog/treefrogyellow");
    }

    public void renderTreeFrog(final EntityTreeFrog entitytreefrog, final double d, final double d1, final double d2,
        final float f, final float f1) {
        super.doRender((EntityLiving) entitytreefrog, d, d1, d2, f, f1);
    }

    public void doRender(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.renderTreeFrog((EntityTreeFrog) entityliving, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderTreeFrog((EntityTreeFrog) entity, d, d1, d2, f, f1);
    }
}
