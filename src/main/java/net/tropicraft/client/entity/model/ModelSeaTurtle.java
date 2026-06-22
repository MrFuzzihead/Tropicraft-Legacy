package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelSeaTurtle extends ModelBase {

    public ModelRenderer Body;
    public ModelRenderer FRFlipper;
    public ModelRenderer FLFlipper;
    public ModelRenderer Head;
    public ModelRenderer RLFlipper;
    public ModelRenderer RRFlipper;
    public boolean inWater;

    public ModelSeaTurtle() {
        this.inWater = false;
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.setTextureOffset("FRFlipper.Shape8", 0, 20);
        this.setTextureOffset("FLFlipper.Shape9", 0, 20);
        this.setTextureOffset("Body.Shape6", 0, 29);
        this.setTextureOffset("Body.Shape4", 43, 40);
        this.setTextureOffset("Body.Shape1", 0, 52);
        this.setTextureOffset("Body.Shape2", 0, 41);
        this.setTextureOffset("Body.Shape3", 0, 32);
        this.setTextureOffset("Body.Shape5", 44, 55);
        this.setTextureOffset("Body.Shape5", 44, 55);
        this.setTextureOffset("Body.Shape7", 0, 25);
        this.setTextureOffset("Head.Shape10", 0, 0);
        this.setTextureOffset("RLFlipper.Shape11", 0, 16);
        this.setTextureOffset("RRFlipper.Shape12", 0, 16);
        (this.Body = new ModelRenderer((ModelBase) this, "Body")).setRotationPoint(0.0f, 19.0f, 0.0f);
        this.setRotation(this.Body, 0.0f, 0.0f, 0.0f);
        this.Body.mirror = true;
        (this.FRFlipper = new ModelRenderer((ModelBase) this, "FRFlipper")).setRotationPoint(-7.0f, 2.0f, -6.0f);
        this.setRotation(this.FRFlipper, 0.0f, 0.0f, 0.0f);
        this.FRFlipper.mirror = true;
        this.FRFlipper.addBox("Shape8", -10.0f, 0.0f, -3.0f, 10, 1, 4);
        this.Body.addChild(this.FRFlipper);
        (this.FLFlipper = new ModelRenderer((ModelBase) this, "FLFlipper")).setRotationPoint(7.0f, 2.0f, -6.0f);
        this.setRotation(this.FLFlipper, 0.0f, 0.0f, 0.0f);
        this.FLFlipper.mirror = true;
        this.FLFlipper.addBox("Shape9", 0.0f, 0.0f, -3.0f, 10, 1, 4);
        this.Body.addChild(this.FLFlipper);
        this.Body.addBox("Shape6", -4.5f, -1.0f, -9.0f, 9, 2, 1);
        this.Body.addBox("Shape4", -3.0f, -2.0f, 1.0f, 6, 1, 4);
        this.Body.addBox("Shape1", -7.0f, -2.0f, -8.0f, 14, 4, 8);
        this.Body.addBox("Shape2", -5.0f, -1.0f, 0.0f, 10, 3, 8);
        this.Body.addBox("Shape3", -4.0f, -2.5f, -6.0f, 8, 2, 7);
        this.Body.addBox("Shape5", -6.0f, -0.5f, 0.0f, 1, 2, 7);
        this.Body.addBox("Shape5", 5.0f, -0.5f, 0.0f, 1, 2, 7);
        this.Body.addBox("Shape7", -4.0f, -0.5f, 8.0f, 8, 2, 2);
        (this.Head = new ModelRenderer((ModelBase) this, "Head")).setRotationPoint(0.0f, 1.0f, -8.0f);
        this.setRotation(this.Head, 0.0f, 0.0f, 0.0f);
        this.Head.mirror = true;
        this.Head.addBox("Shape10", -1.5f, -1.5f, -6.0f, 3, 3, 6);
        this.Body.addChild(this.Head);
        (this.RLFlipper = new ModelRenderer((ModelBase) this, "RLFlipper")).setRotationPoint(-4.0f, 2.0f, 7.0f);
        this.setRotation(this.RLFlipper, 0.0f, 0.0f, 0.0f);
        this.RLFlipper.mirror = true;
        this.RLFlipper.addBox("Shape11", -7.0f, 0.0f, -1.0f, 7, 1, 3);
        this.Body.addChild(this.RLFlipper);
        (this.RRFlipper = new ModelRenderer((ModelBase) this, "RRFlipper")).setRotationPoint(4.0f, 2.0f, 7.0f);
        this.setRotation(this.RRFlipper, 0.0f, 0.0f, 0.0f);
        this.RRFlipper.mirror = true;
        this.RRFlipper.addBox("Shape12", -1.0f, 0.0f, -1.0f, 7, 1, 3);
        this.Body.addChild(this.RRFlipper);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Body.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        final float defFront = 0.3927f;
        final float defFront2 = 0.3f;
        final float defRear = 0.5f;
        if (!entityliving.isInWater()) {
            this.Body.rotateAngleX = -Math.abs(MathHelper.sin(f * 0.25f) * 1.25f * f1) - 0.1f;
            this.FRFlipper.rotateAngleY = MathHelper.cos(f * 0.5f) * 2.5f * f1 + defFront;
            this.FRFlipper.rotateAngleX = -defFront2;
            this.FRFlipper.rotateAngleZ = MathHelper.cos(f * 0.5f) * 1.25f * f1 - defFront2;
            this.FLFlipper.rotateAngleY = MathHelper.cos(f * 0.5f) * 2.5f * f1 - defFront;
            this.FLFlipper.rotateAngleZ = -MathHelper.cos(f * 0.5f) * 1.25f * f1 + defFront2;
            this.FRFlipper.rotateAngleX = defFront2;
            this.RRFlipper.rotateAngleY = -MathHelper.cos(f * 0.5f) * 1.25f * f1 - defRear;
            this.RLFlipper.rotateAngleY = -MathHelper.cos(f * 0.5f) * 1.25f * f1 + defRear;
            this.RRFlipper.rotateAngleZ = 0.0f;
            this.RLFlipper.rotateAngleZ = 0.0f;
        } else if (entityliving.isInWater()) {
            this.Body.rotateAngleX = -0.35f;
            this.FRFlipper.rotateAngleY = MathHelper.cos(f * 0.25f) * 1.5f * f1 + defFront;
            this.FRFlipper.rotateAngleX = -defFront2;
            this.FRFlipper.rotateAngleZ = -MathHelper.cos(f * 1.25f) * 1.75f * f1 - defFront2;
            this.FLFlipper.rotateAngleY = MathHelper.cos(f * 0.25f) * 1.5f * f1 - defFront;
            this.FLFlipper.rotateAngleZ = MathHelper.cos(f * 1.25f) * 1.75f * f1 + defFront2;
            this.FRFlipper.rotateAngleX = defFront2;
            this.RRFlipper.rotateAngleY = -MathHelper.cos(f * 0.25f) * 0.25f * f1 - defRear;
            this.RLFlipper.rotateAngleY = MathHelper.cos(f * 0.25f) * 0.25f * f1 + defRear;
            this.RRFlipper.rotateAngleZ = -MathHelper.cos(f * 1.25f) * 1.25f * f1;
            this.RLFlipper.rotateAngleZ = -MathHelper.cos(f * 1.25f) * 1.25f * f1;
        }
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        this.Head.rotateAngleX = f4 / 125.0f;
        this.Head.rotateAngleY = f3 / 125.0f;
    }
}
