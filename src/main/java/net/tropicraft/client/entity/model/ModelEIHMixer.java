package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelEIHMixer extends ModelBase {

    ModelRenderer Base;
    ModelRenderer Back;
    ModelRenderer Nose;
    ModelRenderer Forehead;
    ModelRenderer LeftEye;
    ModelRenderer RightEye;
    ModelRenderer BasinNearBack;
    ModelRenderer BasinSide;
    ModelRenderer BasinSide2;
    ModelRenderer BasinNearFront;
    ModelRenderer BasinCorner1;
    ModelRenderer BasinCorner2;
    ModelRenderer BasinCorner3;
    ModelRenderer BasinCorner4;
    ModelRenderer LidBase;
    ModelRenderer LidTop;
    ModelRenderer Mouth;

    public ModelEIHMixer() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.Base = new ModelRenderer((ModelBase) this, 0, 44)).addBox(-8.0f, -1.0f, -8.0f, 16, 3, 16);
        this.Base.setRotationPoint(0.0f, 22.0f, 0.0f);
        this.Base.setTextureSize(64, 64);
        this.Base.mirror = true;
        this.setRotation(this.Base, 0.0f, 0.0f, 0.0f);
        (this.Back = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-3.0f, -15.0f, -8.0f, 6, 25, 16);
        this.Back.setRotationPoint(5.0f, 11.0f, 0.0f);
        this.Back.setTextureSize(64, 64);
        this.Back.mirror = true;
        this.setRotation(this.Back, 0.0f, 0.0f, 0.0f);
        (this.Nose = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.0f, -7.0f, -2.0f, 2, 14, 4);
        this.Nose.setRotationPoint(1.0f, 8.0f, 0.0f);
        this.Nose.setTextureSize(64, 64);
        this.Nose.mirror = true;
        this.setRotation(this.Nose, 0.0f, 0.0f, 0.0f);
        (this.Forehead = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.0f, -1.0f, -8.0f, 3, 5, 16);
        this.Forehead.setRotationPoint(0.0f, -3.0f, 0.0f);
        this.Forehead.setTextureSize(64, 64);
        this.Forehead.mirror = true;
        this.setRotation(this.Forehead, 0.0f, 0.0f, 0.0f);
        (this.LeftEye = new ModelRenderer((ModelBase) this, 1, 35)).addBox(0.0f, -1.0f, -3.0f, 1, 4, 6);
        this.LeftEye.setRotationPoint(1.0f, 2.0f, 5.0f);
        this.LeftEye.setTextureSize(64, 64);
        this.LeftEye.mirror = true;
        this.setRotation(this.LeftEye, 0.0f, 0.0f, 0.0f);
        (this.RightEye = new ModelRenderer((ModelBase) this, 1, 35)).addBox(0.0f, -1.0f, -3.0f, 1, 4, 6);
        this.RightEye.setRotationPoint(1.0f, 2.0f, -5.0f);
        this.RightEye.setTextureSize(64, 64);
        this.RightEye.mirror = true;
        this.setRotation(this.RightEye, 0.0f, 0.0f, 0.0f);
        (this.BasinNearBack = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.0f, 0.0f, -4.0f, 1, 1, 8);
        this.BasinNearBack.setRotationPoint(2.0f, 20.0f, 0.0f);
        this.BasinNearBack.setTextureSize(64, 64);
        this.BasinNearBack.mirror = true;
        this.setRotation(this.BasinNearBack, 0.0f, 0.0f, 0.0f);
        (this.BasinSide = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-5.0f, 0.0f, -2.0f, 10, 1, 4);
        this.BasinSide.setRotationPoint(-3.0f, 20.0f, 6.0f);
        this.BasinSide.setTextureSize(64, 64);
        this.BasinSide.mirror = true;
        this.setRotation(this.BasinSide, 0.0f, 0.0f, 0.0f);
        (this.BasinSide2 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-5.0f, 0.0f, -2.0f, 10, 1, 4);
        this.BasinSide2.setRotationPoint(-3.0f, 20.0f, -6.0f);
        this.BasinSide2.setTextureSize(64, 64);
        this.BasinSide2.mirror = true;
        this.setRotation(this.BasinSide2, 0.0f, 0.0f, 0.0f);
        (this.BasinNearFront = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.0f, 0.0f, -4.0f, 2, 1, 8);
        this.BasinNearFront.setRotationPoint(-7.0f, 20.0f, 0.0f);
        this.BasinNearFront.setTextureSize(64, 64);
        this.BasinNearFront.mirror = true;
        this.setRotation(this.BasinNearFront, 0.0f, 0.0f, 0.0f);
        (this.BasinCorner1 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
        this.BasinCorner1.setRotationPoint(0.0f, 20.0f, 3.0f);
        this.BasinCorner1.setTextureSize(64, 64);
        this.BasinCorner1.mirror = true;
        this.setRotation(this.BasinCorner1, 0.0f, 0.0f, 0.0f);
        (this.BasinCorner2 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
        this.BasinCorner2.setRotationPoint(0.0f, 20.0f, -4.0f);
        this.BasinCorner2.setTextureSize(64, 64);
        this.BasinCorner2.mirror = true;
        this.setRotation(this.BasinCorner2, 0.0f, 0.0f, 0.0f);
        (this.BasinCorner3 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
        this.BasinCorner3.setRotationPoint(-6.0f, 20.0f, 3.0f);
        this.BasinCorner3.setTextureSize(64, 64);
        this.BasinCorner3.mirror = true;
        this.setRotation(this.BasinCorner3, 0.0f, 0.0f, 0.0f);
        (this.BasinCorner4 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
        this.BasinCorner4.setRotationPoint(-6.0f, 20.0f, -4.0f);
        this.BasinCorner4.setTextureSize(64, 64);
        this.BasinCorner4.mirror = true;
        this.setRotation(this.BasinCorner4, 0.0f, 0.0f, 0.0f);
        (this.LidBase = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-4.0f, 0.0f, -8.0f, 9, 1, 16);
        this.LidBase.setRotationPoint(3.0f, -5.0f, 0.0f);
        this.LidBase.setTextureSize(64, 64);
        this.LidBase.mirror = true;
        this.setRotation(this.LidBase, 0.0f, 0.0f, 0.0f);
        (this.LidTop = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4);
        this.LidTop.setRotationPoint(3.0f, -6.0f, 0.0f);
        this.LidTop.setTextureSize(64, 64);
        this.LidTop.mirror = true;
        this.setRotation(this.LidTop, 0.0f, 0.0f, 0.0f);
        (this.Mouth = new ModelRenderer((ModelBase) this, 54, 0)).addBox(0.0f, -1.0f, -2.0f, 1, 3, 4);
        this.Mouth.setRotationPoint(1.0f, 16.0f, 0.0f);
        this.Mouth.setTextureSize(64, 64);
        this.Mouth.mirror = true;
        this.setRotation(this.Mouth, 0.0f, 0.0f, 0.0f);
    }

    public void renderEIHMixer() {
        final float f5 = 0.0625f;
        this.Base.render(f5);
        this.Back.render(f5);
        this.Nose.render(f5);
        this.Forehead.render(f5);
        this.LeftEye.render(f5);
        this.RightEye.render(f5);
        this.BasinNearBack.render(f5);
        this.BasinSide.render(f5);
        this.BasinSide2.render(f5);
        this.BasinNearFront.render(f5);
        this.BasinCorner1.render(f5);
        this.BasinCorner2.render(f5);
        this.BasinCorner3.render(f5);
        this.BasinCorner4.render(f5);
        this.LidBase.render(f5);
        this.LidTop.render(f5);
        this.Mouth.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
