package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelTropiCreeper extends ModelBase {

    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer leg3;
    ModelRenderer leg4;
    ModelRenderer leg1;
    ModelRenderer leg2;
    ModelRenderer hat1;
    ModelRenderer hat2;
    ModelRenderer hat3;

    public ModelTropiCreeper() {
        final int i = 4;
        (this.head = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8);
        this.head.setRotationPoint(0.0f, 6.0f, 0.0f);
        this.head.rotateAngleX = 0.0f;
        this.head.rotateAngleY = 0.0f;
        this.head.rotateAngleZ = 0.0f;
        this.head.mirror = false;
        (this.body = new ModelRenderer((ModelBase) this, 16, 16)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4);
        this.body.setRotationPoint(0.0f, 6.0f, 0.0f);
        this.body.rotateAngleX = 0.0f;
        this.body.rotateAngleY = 0.0f;
        this.body.rotateAngleZ = 0.0f;
        this.body.mirror = false;
        (this.leg3 = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.leg3.setRotationPoint(-2.0f, (float) (12 + i), -4.0f);
        this.leg3.rotateAngleX = 0.0f;
        this.leg3.rotateAngleY = 0.0f;
        this.leg3.rotateAngleZ = 0.0f;
        this.leg3.mirror = false;
        (this.leg4 = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.leg4.setRotationPoint(2.0f, (float) (12 + i), -4.0f);
        this.leg4.rotateAngleX = 0.0f;
        this.leg4.rotateAngleY = 0.0f;
        this.leg4.rotateAngleZ = 0.0f;
        this.leg4.mirror = false;
        (this.leg1 = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.leg1.setRotationPoint(-2.0f, (float) (12 + i), 4.0f);
        this.leg1.rotateAngleX = 0.0f;
        this.leg1.rotateAngleY = 0.0f;
        this.leg1.rotateAngleZ = 0.0f;
        this.leg1.mirror = false;
        (this.leg2 = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.leg2.setRotationPoint(2.0f, (float) (12 + i), 4.0f);
        this.leg2.rotateAngleX = 0.0f;
        this.leg2.rotateAngleY = 0.0f;
        this.leg2.rotateAngleZ = 0.0f;
        this.leg2.mirror = false;
        (this.hat1 = new ModelRenderer((ModelBase) this, 24, 0)).addBox(-5.0f, 0.0f, -5.0f, 12, 1, 6);
        this.hat1.setRotationPoint(-1.0f, -3.0f, -1.0f);
        this.hat1.rotateAngleX = 0.0f;
        this.hat1.rotateAngleY = 0.0f;
        this.hat1.rotateAngleZ = 0.0f;
        this.hat1.mirror = true;
        (this.hat2 = new ModelRenderer((ModelBase) this, 40, 24)).addBox(0.0f, 0.0f, 0.0f, 6, 2, 6);
        this.hat2.setRotationPoint(-3.0f, -5.0f, -3.0f);
        this.hat2.rotateAngleX = 0.0f;
        this.hat2.rotateAngleY = 0.0f;
        this.hat2.rotateAngleZ = 0.0f;
        this.hat2.mirror = false;
        (this.hat3 = new ModelRenderer((ModelBase) this, 24, 0)).addBox(-5.0f, 0.0f, 0.0f, 12, 1, 6);
        this.hat3.setRotationPoint(-1.0f, -3.0f, 0.0f);
        this.hat3.rotateAngleX = 0.0f;
        this.hat3.rotateAngleY = 0.0f;
        this.hat3.rotateAngleZ = 0.0f;
        this.hat3.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.head.render(f5);
        this.body.render(f5);
        this.leg3.render(f5);
        this.leg4.render(f5);
        this.leg1.render(f5);
        this.leg2.render(f5);
        this.hat1.render(f5);
        this.hat2.render(f5);
        this.hat3.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        this.head.rotateAngleY = f3 / 57.29578f;
        this.head.rotateAngleX = f4 / 57.29578f;
        this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f1;
        this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.4f * f1;
        this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.4f * f1;
        this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f1;
        final ModelRenderer hat1 = this.hat1;
        final ModelRenderer hat2 = this.hat2;
        final ModelRenderer hat3 = this.hat3;
        final float rotateAngleX = this.head.rotateAngleX / 6.2342343f;
        hat3.rotateAngleX = rotateAngleX;
        hat2.rotateAngleX = rotateAngleX;
        hat1.rotateAngleX = rotateAngleX;
        final ModelRenderer hat4 = this.hat1;
        final ModelRenderer hat5 = this.hat2;
        final ModelRenderer hat6 = this.hat3;
        final float rotateAngleY = this.head.rotateAngleY / 6.2342343f;
        hat6.rotateAngleY = rotateAngleY;
        hat5.rotateAngleY = rotateAngleY;
        hat4.rotateAngleY = rotateAngleY;
    }
}
