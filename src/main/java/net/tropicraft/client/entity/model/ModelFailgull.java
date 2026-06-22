package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelFailgull extends ModelBase {

    ModelRenderer BaseFootLeft;
    ModelRenderer BaseFootRight;
    ModelRenderer LowerLeg1;
    ModelRenderer LowestBody;
    ModelRenderer LowerLeg11;
    ModelRenderer LowerBody1;
    ModelRenderer LowerBody2;
    ModelRenderer RightWing;
    ModelRenderer RightWing1;
    ModelRenderer Neck;
    ModelRenderer New_Shape1;

    public ModelFailgull() {
        (this.BaseFootLeft = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 0, 1);
        this.BaseFootLeft.setRotationPoint(-1.0f, 23.0f, 0.0f);
        this.BaseFootLeft.rotateAngleX = 0.0f;
        this.BaseFootLeft.rotateAngleY = 0.0f;
        this.BaseFootLeft.rotateAngleZ = 0.0f;
        this.BaseFootLeft.mirror = false;
        (this.BaseFootRight = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 0, 1);
        this.BaseFootRight.setRotationPoint(1.0f, 23.0f, 0.0f);
        this.BaseFootRight.rotateAngleX = 0.0f;
        this.BaseFootRight.rotateAngleY = 0.0f;
        this.BaseFootRight.rotateAngleZ = 0.0f;
        this.BaseFootRight.mirror = false;
        (this.LowerLeg1 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, -1.0f, 0.0f, 1, 2, 0);
        this.LowerLeg1.setRotationPoint(1.0f, 22.0f, 1.0f);
        this.LowerLeg1.rotateAngleX = 0.0f;
        this.LowerLeg1.rotateAngleY = 0.0f;
        this.LowerLeg1.rotateAngleZ = 0.0f;
        this.LowerLeg1.mirror = false;
        (this.LowestBody = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 4);
        this.LowestBody.setRotationPoint(-1.0f, 20.0f, 0.0f);
        this.LowestBody.rotateAngleX = 0.0f;
        this.LowestBody.rotateAngleY = 0.0f;
        this.LowestBody.rotateAngleZ = 0.0f;
        this.LowestBody.mirror = false;
        (this.LowerLeg11 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 0);
        this.LowerLeg11.setRotationPoint(-1.0f, 21.0f, 1.0f);
        this.LowerLeg11.rotateAngleX = 0.0f;
        this.LowerLeg11.rotateAngleY = 0.0f;
        this.LowerLeg11.rotateAngleZ = 0.0f;
        this.LowerLeg11.mirror = false;
        (this.LowerBody1 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 8);
        this.LowerBody1.setRotationPoint(-1.0f, 19.0f, -1.0f);
        this.LowerBody1.rotateAngleX = 0.0f;
        this.LowerBody1.rotateAngleY = 0.0f;
        this.LowerBody1.rotateAngleZ = 0.0f;
        this.LowerBody1.mirror = false;
        (this.LowerBody2 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 7);
        this.LowerBody2.setRotationPoint(-1.0f, 18.0f, -2.0f);
        this.LowerBody2.rotateAngleX = 0.0f;
        this.LowerBody2.rotateAngleY = 0.0f;
        this.LowerBody2.rotateAngleZ = 0.0f;
        this.LowerBody2.mirror = false;
        (this.RightWing = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 2, 5);
        this.RightWing.setRotationPoint(-1.0f, 18.0f, 0.0f);
        this.RightWing.rotateAngleX = -0.06981f;
        this.RightWing.rotateAngleY = -0.06981f;
        this.RightWing.rotateAngleZ = 0.0f;
        this.RightWing.mirror = false;
        (this.RightWing1 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 2, 5);
        this.RightWing1.setRotationPoint(2.0f, 18.0f, 0.0f);
        this.RightWing1.rotateAngleX = -0.06981f;
        this.RightWing1.rotateAngleY = 0.06981f;
        this.RightWing1.rotateAngleZ = 0.0f;
        this.RightWing1.mirror = false;
        (this.Neck = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 2);
        this.Neck.setRotationPoint(-1.0f, 16.0f, -3.0f);
        this.Neck.rotateAngleX = 0.0f;
        this.Neck.rotateAngleY = 0.0f;
        this.Neck.rotateAngleZ = 0.0f;
        this.Neck.mirror = false;
        (this.New_Shape1 = new ModelRenderer((ModelBase) this, 14, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 2);
        this.New_Shape1.setRotationPoint(0.0f, 17.0f, -5.0f);
        this.New_Shape1.rotateAngleX = 0.0f;
        this.New_Shape1.rotateAngleY = 0.0f;
        this.New_Shape1.rotateAngleZ = 0.0f;
        this.New_Shape1.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.BaseFootLeft.render(f5);
        this.BaseFootRight.render(f5);
        this.LowerLeg1.render(f5);
        this.LowestBody.render(f5);
        this.LowerLeg11.render(f5);
        this.LowerBody1.render(f5);
        this.LowerBody2.render(f5);
        this.RightWing.render(f5);
        this.RightWing1.render(f5);
        this.Neck.render(f5);
        this.New_Shape1.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
        this.LowerLeg1.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f1;
        this.LowerLeg11.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.4f * f1;
        this.RightWing.rotateAngleZ = f2;
        this.RightWing1.rotateAngleZ = -f2;
    }
}
