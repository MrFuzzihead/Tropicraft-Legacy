package net.tropicraft.client.block.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCurareBowl extends ModelBase {

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
    ModelRenderer Shape10;
    ModelRenderer Shape11;

    public ModelCurareBowl() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.Shape1 = new ModelRenderer((ModelBase) this, 0, 12)).addBox(-4.0f, 0.0f, -6.0f, 8, 1, 10);
        this.Shape1.setRotationPoint(0.0f, 23.0f, 1.0f);
        this.Shape1.setTextureSize(64, 64);
        this.Shape1.mirror = true;
        this.setRotation(this.Shape1, 0.0f, 0.0f, 0.0f);
        (this.Shape2 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-4.0f, -1.0f, 0.0f, 8, 4, 1);
        this.Shape2.setRotationPoint(0.0f, 20.0f, 5.0f);
        this.Shape2.setTextureSize(64, 64);
        this.Shape2.mirror = true;
        this.setRotation(this.Shape2, 0.0f, 0.0f, 0.0f);
        (this.Shape3 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-4.0f, 0.0f, 0.0f, 8, 4, 1);
        this.Shape3.setRotationPoint(0.0f, 19.0f, -6.0f);
        this.Shape3.setTextureSize(64, 64);
        this.Shape3.mirror = true;
        this.setRotation(this.Shape3, 0.0f, 0.0f, 0.0f);
        (this.Shape4 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, -4.0f, 1, 4, 8);
        this.Shape4.setRotationPoint(5.0f, 19.0f, 0.0f);
        this.Shape4.setTextureSize(64, 64);
        this.Shape4.mirror = true;
        this.setRotation(this.Shape4, 0.0f, 0.0f, 0.0f);
        (this.Shape5 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, -4.0f, 1, 4, 8);
        this.Shape5.setRotationPoint(-6.0f, 19.0f, 0.0f);
        this.Shape5.setTextureSize(64, 64);
        this.Shape5.mirror = true;
        this.setRotation(this.Shape5, 0.0f, 0.0f, 0.0f);
        (this.Shape6 = new ModelRenderer((ModelBase) this, 0, 14)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 8);
        this.Shape6.setRotationPoint(-5.0f, 23.0f, -4.0f);
        this.Shape6.setTextureSize(64, 64);
        this.Shape6.mirror = true;
        this.setRotation(this.Shape6, 0.0f, 0.0f, 0.0f);
        (this.Shape7 = new ModelRenderer((ModelBase) this, 0, 14)).addBox(0.0f, 0.0f, -4.0f, 1, 1, 8);
        this.Shape7.setRotationPoint(4.0f, 23.0f, 0.0f);
        this.Shape7.setTextureSize(64, 64);
        this.Shape7.mirror = true;
        this.setRotation(this.Shape7, 0.0f, 0.0f, 0.0f);
        (this.Shape8 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, -1.0f, 0.0f, 1, 4, 1);
        this.Shape8.setRotationPoint(-5.0f, 20.0f, -5.0f);
        this.Shape8.setTextureSize(64, 64);
        this.Shape8.mirror = true;
        this.setRotation(this.Shape8, 0.0f, 0.0f, 0.0f);
        (this.Shape9 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
        this.Shape9.setRotationPoint(-5.0f, 19.0f, 4.0f);
        this.Shape9.setTextureSize(64, 64);
        this.Shape9.mirror = true;
        this.setRotation(this.Shape9, 0.0f, 0.0f, 0.0f);
        (this.Shape10 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
        this.Shape10.setRotationPoint(4.0f, 19.0f, -5.0f);
        this.Shape10.setTextureSize(64, 64);
        this.Shape10.mirror = true;
        this.setRotation(this.Shape10, 0.0f, 0.0f, 0.0f);
        (this.Shape11 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
        this.Shape11.setRotationPoint(4.0f, 19.0f, 4.0f);
        this.Shape11.setTextureSize(64, 64);
        this.Shape11.mirror = true;
        this.setRotation(this.Shape11, 0.0f, 0.0f, 0.0f);
    }

    public void renderBowl() {
        final float f5 = 0.0625f;
        this.Shape1.render(f5);
        this.Shape2.render(f5);
        this.Shape3.render(f5);
        this.Shape4.render(f5);
        this.Shape5.render(f5);
        this.Shape6.render(f5);
        this.Shape7.render(f5);
        this.Shape8.render(f5);
        this.Shape9.render(f5);
        this.Shape10.render(f5);
        this.Shape11.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
