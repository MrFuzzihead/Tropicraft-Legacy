package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChair extends ModelBase {

    public ModelRenderer seat;
    public ModelRenderer back;
    public ModelRenderer backRightLeg;
    public ModelRenderer backLeftLeg;
    public ModelRenderer frontLeftLeg;
    public ModelRenderer frontRightLeg;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;

    public ModelChair() {
        (this.seat = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-7.0f, 0.0f, -8.0f, 16, 1, 16, 0.0f);
        this.seat.setRotationPoint(-1.0f, 0.0f, 0.0f);
        this.seat.rotateAngleX = 0.0f;
        this.seat.rotateAngleY = 0.0f;
        this.seat.rotateAngleZ = 0.0f;
        this.seat.mirror = false;
        (this.back = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-7.0f, 0.0f, 0.0f, 16, 1, 16, 0.0f);
        this.back.setRotationPoint(-1.0f, 0.0f, 8.0f);
        this.back.rotateAngleX = 1.169371f;
        this.back.rotateAngleY = 0.0f;
        this.back.rotateAngleZ = 0.0f;
        this.back.mirror = false;
        (this.backRightLeg = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.0f, -1.0f, 0.0f, 1, 10, 1, 0.0f);
        this.backRightLeg.setRotationPoint(-8.0f, -3.0f, 6.0f);
        this.backRightLeg.rotateAngleX = 0.4537856f;
        this.backRightLeg.rotateAngleY = 0.0f;
        this.backRightLeg.rotateAngleZ = 0.0f;
        this.backRightLeg.mirror = false;
        (this.backLeftLeg = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 10, 1, 0.0f);
        this.backLeftLeg.setRotationPoint(8.0f, -4.0f, 5.0f);
        this.backLeftLeg.rotateAngleX = 0.4537856f;
        this.backLeftLeg.rotateAngleY = 0.0f;
        this.backLeftLeg.rotateAngleZ = 0.0f;
        this.backLeftLeg.mirror = false;
        (this.frontLeftLeg = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, -1.0f, 1, 10, 1, 0.0f);
        this.frontLeftLeg.setRotationPoint(8.0f, -4.0f, 0.0f);
        this.frontLeftLeg.rotateAngleX = -0.4537856f;
        this.frontLeftLeg.rotateAngleY = 0.0f;
        this.frontLeftLeg.rotateAngleZ = 0.0f;
        this.frontLeftLeg.mirror = false;
        (this.frontRightLeg = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.0f, 0.0f, -1.0f, 1, 10, 1, 0.0f);
        this.frontRightLeg.setRotationPoint(-8.0f, -4.0f, 0.0f);
        this.frontRightLeg.rotateAngleX = -0.4537856f;
        this.frontRightLeg.rotateAngleY = 0.0f;
        this.frontRightLeg.rotateAngleZ = 0.0f;
        this.frontRightLeg.mirror = false;
        (this.rightArm = new ModelRenderer((ModelBase) this, 0, 29)).addBox(0.0f, -1.0f, 0.0f, 14, 1, 2, 0.0f);
        this.rightArm.setRotationPoint(-10.0f, -4.0f, 11.0f);
        this.rightArm.rotateAngleX = 0.0f;
        this.rightArm.rotateAngleY = 1.570796f;
        this.rightArm.rotateAngleZ = 0.0f;
        this.rightArm.mirror = false;
        (this.leftArm = new ModelRenderer((ModelBase) this, 0, 29)).addBox(0.0f, 0.0f, 0.0f, 14, 1, 2, 0.0f);
        this.leftArm.setRotationPoint(8.0f, -5.0f, 11.0f);
        this.leftArm.rotateAngleX = 0.0f;
        this.leftArm.rotateAngleY = 1.570796f;
        this.leftArm.rotateAngleZ = 0.0f;
        this.leftArm.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.seat.render(f5);
        this.back.render(f5);
        this.backRightLeg.render(f5);
        this.backLeftLeg.render(f5);
        this.frontLeftLeg.render(f5);
        this.frontRightLeg.render(f5);
        this.rightArm.render(f5);
        this.leftArm.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
