package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEIH extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer base;
    public ModelRenderer nose;
    public ModelRenderer mouth;
    public ModelRenderer top;
    public ModelRenderer leye;
    public ModelRenderer reye;

    public ModelEIH() {
        (this.body = new ModelRenderer((ModelBase) this, 34, 8)).addBox(-4.0f, 1.0f, -1.0f, 8, 17, 7, 0.0f);
        this.body.setRotationPoint(0.0f, -2.0f, 0.0f);
        this.body.rotateAngleX = 0.0f;
        this.body.rotateAngleY = 0.0f;
        this.body.rotateAngleZ = 0.0f;
        this.body.mirror = false;
        (this.base = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-4.0f, 11.0f, -3.0f, 8, 8, 11, 0.0f);
        this.base.setRotationPoint(0.0f, 5.0f, -2.0f);
        this.base.rotateAngleX = 0.0f;
        this.base.rotateAngleY = 0.0f;
        this.base.rotateAngleZ = 0.0f;
        this.base.mirror = false;
        (this.nose = new ModelRenderer((ModelBase) this, 27, 2)).addBox(13.5f, -1.0f, -3.0f, 13, 2, 3, 0.0f);
        this.nose.setRotationPoint(0.0f, -14.8f, -1.0f);
        this.nose.rotateAngleX = 0.0f;
        this.nose.rotateAngleY = 0.0f;
        this.nose.rotateAngleZ = 1.570796f;
        this.nose.mirror = false;
        (this.mouth = new ModelRenderer((ModelBase) this, 56, 11)).addBox(-1.5f, 4.0f, -1.0f, 3, 3, 1, 0.0f);
        this.mouth.setRotationPoint(0.0f, 7.5f, -0.5f);
        this.mouth.rotateAngleX = 0.0f;
        this.mouth.rotateAngleY = 0.0f;
        this.mouth.rotateAngleZ = 0.0f;
        this.mouth.mirror = false;
        (this.top = new ModelRenderer((ModelBase) this, 0, 17)).addBox(-4.0f, -1.0f, -10.0f, 8, 5, 10, 0.0f);
        this.top.setRotationPoint(0.0f, -5.0f, 6.0f);
        this.top.rotateAngleX = 0.0f;
        this.top.rotateAngleY = 0.0f;
        this.top.rotateAngleZ = 0.0f;
        this.top.mirror = false;
        (this.leye = new ModelRenderer((ModelBase) this, 56, 7)).addBox(0.0f, 0.0f, 0.0f, 3, 3, 1, 0.0f);
        this.leye.setRotationPoint(1.0f, -1.0f, -2.0f);
        this.leye.rotateAngleX = 0.0f;
        this.leye.rotateAngleY = 0.0f;
        this.leye.rotateAngleZ = 0.0f;
        this.leye.mirror = true;
        (this.reye = new ModelRenderer((ModelBase) this, 56, 7)).addBox(-1.5f, -1.0f, -1.0f, 3, 3, 1, 0.0f);
        this.reye.setRotationPoint(-2.5f, 0.0f, -1.0f);
        this.reye.rotateAngleX = 0.0f;
        this.reye.rotateAngleY = 0.0f;
        this.reye.rotateAngleZ = 0.0f;
        this.reye.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, null);
        this.body.render(f5);
        this.base.render(f5);
        this.nose.render(f5);
        this.mouth.render(f5);
        this.top.render(f5);
        this.leye.render(f5);
        this.reye.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
