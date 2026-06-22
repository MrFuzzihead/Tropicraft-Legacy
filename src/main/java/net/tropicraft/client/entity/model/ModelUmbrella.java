package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelUmbrella extends ModelBase {

    public ModelRenderer New_Shape1;
    public ModelRenderer New_Shape2;
    public ModelRenderer New_Shape3;
    public ModelRenderer New_Shape31;
    public ModelRenderer New_Shape32;
    public ModelRenderer New_Shape33;
    public ModelRenderer New_Shape4;
    public ModelRenderer New_Shape11;
    public ModelRenderer New_Shape12;
    public ModelRenderer New_Shape111;
    public ModelRenderer New_Shape112;

    public ModelUmbrella() {
        (this.New_Shape1 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 14, 1, 0.0f);
        this.New_Shape1.setRotationPoint(0.0f, -13.0f, 0.0f);
        this.New_Shape1.rotateAngleX = 0.0f;
        this.New_Shape1.rotateAngleY = 0.0f;
        this.New_Shape1.rotateAngleZ = 0.0f;
        this.New_Shape1.mirror = false;
        (this.New_Shape2 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-7.5f, -2.0f, -7.5f, 15, 1, 15, 0.0f);
        this.New_Shape2.setRotationPoint(0.0f, -12.0f, 0.0f);
        this.New_Shape2.rotateAngleX = 0.0f;
        this.New_Shape2.rotateAngleY = 0.0f;
        this.New_Shape2.rotateAngleZ = 0.0f;
        this.New_Shape2.mirror = false;
        (this.New_Shape3 = new ModelRenderer((ModelBase) this, 0, 20)).addBox(-4.0f, -1.0f, 0.0f, 9, 1, 3, 0.0f);
        this.New_Shape3.setRotationPoint(-0.5f, -13.0f, 7.5f);
        this.New_Shape3.rotateAngleX = -0.2443461f;
        this.New_Shape3.rotateAngleY = 0.0f;
        this.New_Shape3.rotateAngleZ = 0.0f;
        this.New_Shape3.mirror = false;
        (this.New_Shape31 = new ModelRenderer((ModelBase) this, 0, 24)).addBox(-4.5f, -1.0f, 0.0f, 9, 1, 3, 0.0f);
        this.New_Shape31.setRotationPoint(7.5f, -13.0f, 0.0f);
        this.New_Shape31.rotateAngleX = -0.2443461f;
        this.New_Shape31.rotateAngleY = 1.570796f;
        this.New_Shape31.rotateAngleZ = 0.0f;
        this.New_Shape31.mirror = false;
        (this.New_Shape32 = new ModelRenderer((ModelBase) this, 0, 28)).addBox(-4.5f, -1.0f, -1.0f, 9, 1, 3, 0.0f);
        this.New_Shape32.setRotationPoint(0.0f, -12.75f, -8.5f);
        this.New_Shape32.rotateAngleX = -0.2443461f;
        this.New_Shape32.rotateAngleY = 3.141593f;
        this.New_Shape32.rotateAngleZ = 0.0f;
        this.New_Shape32.mirror = false;
        (this.New_Shape33 = new ModelRenderer((ModelBase) this, 24, 28)).addBox(-4.5f, -1.0f, 1.0f, 9, 1, 3, 0.0f);
        this.New_Shape33.setRotationPoint(-6.5f, -13.25f, 0.0f);
        this.New_Shape33.rotateAngleX = -0.2443461f;
        this.New_Shape33.rotateAngleY = -1.570796f;
        this.New_Shape33.rotateAngleZ = 0.0f;
        this.New_Shape33.mirror = false;
        (this.New_Shape4 = new ModelRenderer((ModelBase) this, 25, 25)).addBox(-1.0f, -1.0f, -1.0f, 2, 1, 2, 0.0f);
        this.New_Shape4.setRotationPoint(0.0f, -14.0f, 0.0f);
        this.New_Shape4.rotateAngleX = 0.0f;
        this.New_Shape4.rotateAngleY = 0.0f;
        this.New_Shape4.rotateAngleZ = 0.0f;
        this.New_Shape4.mirror = false;
        (this.New_Shape11 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1, 0.0f);
        this.New_Shape11.setRotationPoint(0.0f, -10.0f, 0.0f);
        this.New_Shape11.rotateAngleX = 1.902409f;
        this.New_Shape11.rotateAngleY = 0.0f;
        this.New_Shape11.rotateAngleZ = 0.0f;
        this.New_Shape11.mirror = false;
        (this.New_Shape12 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1, 0.0f);
        this.New_Shape12.setRotationPoint(0.0f, -10.0f, 0.0f);
        this.New_Shape12.rotateAngleX = -1.902409f;
        this.New_Shape12.rotateAngleY = 0.0f;
        this.New_Shape12.rotateAngleZ = 0.0f;
        this.New_Shape12.mirror = false;
        (this.New_Shape111 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1, 0.0f);
        this.New_Shape111.setRotationPoint(0.0f, -10.0f, 0.0f);
        this.New_Shape111.rotateAngleX = 1.902409f;
        this.New_Shape111.rotateAngleY = 1.570796f;
        this.New_Shape111.rotateAngleZ = 0.0f;
        this.New_Shape111.mirror = false;
        (this.New_Shape112 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1, 0.0f);
        this.New_Shape112.setRotationPoint(0.0f, -10.0f, 0.0f);
        this.New_Shape112.rotateAngleX = 1.902409f;
        this.New_Shape112.rotateAngleY = -1.570796f;
        this.New_Shape112.rotateAngleZ = 0.0f;
        this.New_Shape112.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.New_Shape1.render(f5);
        this.New_Shape2.render(f5);
        this.New_Shape3.render(f5);
        this.New_Shape31.render(f5);
        this.New_Shape32.render(f5);
        this.New_Shape33.render(f5);
        this.New_Shape4.render(f5);
        this.New_Shape11.render(f5);
        this.New_Shape12.render(f5);
        this.New_Shape111.render(f5);
        this.New_Shape112.render(f5);
    }

    public void renderUmbrella() {
        final float f5 = 0.0625f;
        this.New_Shape1.render(f5);
        this.New_Shape2.render(f5);
        this.New_Shape3.render(f5);
        this.New_Shape31.render(f5);
        this.New_Shape32.render(f5);
        this.New_Shape33.render(f5);
        this.New_Shape4.render(f5);
        this.New_Shape11.render(f5);
        this.New_Shape12.render(f5);
        this.New_Shape111.render(f5);
        this.New_Shape112.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
