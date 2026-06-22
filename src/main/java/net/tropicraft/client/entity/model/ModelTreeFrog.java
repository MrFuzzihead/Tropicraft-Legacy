package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelTreeFrog extends ModelBase {

    public ModelRenderer frontleft_leg;
    public ModelRenderer frontright_leg;
    public ModelRenderer body;
    public ModelRenderer rear_right_leg;
    public ModelRenderer rear_left_leg;
    public ModelRenderer right_eye;
    public ModelRenderer left_eye;

    public ModelTreeFrog() {
        (this.frontleft_leg = new ModelRenderer((ModelBase) this, 12, 19)).addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4, 0.0f);
        this.frontleft_leg.setRotationPoint(2.0f, 23.0f, -3.0f);
        this.frontleft_leg.rotateAngleX = 0.0f;
        this.frontleft_leg.rotateAngleY = 0.0f;
        this.frontleft_leg.rotateAngleZ = 0.0f;
        this.frontleft_leg.mirror = false;
        (this.frontright_leg = new ModelRenderer((ModelBase) this, 12, 14)).addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4, 0.0f);
        this.frontright_leg.setRotationPoint(-2.0f, 23.0f, -3.0f);
        this.frontright_leg.rotateAngleX = 0.0f;
        this.frontright_leg.rotateAngleY = 0.0f;
        this.frontright_leg.rotateAngleZ = 0.0f;
        this.frontright_leg.mirror = false;
        (this.body = new ModelRenderer((ModelBase) this, 28, 8)).addBox(-2.0f, -5.0f, -2.0f, 4, 9, 4, 0.0f);
        this.body.setRotationPoint(0.0f, 21.0f, 1.0f);
        this.body.rotateAngleX = 1.570796f;
        this.body.rotateAngleY = 0.0f;
        this.body.rotateAngleZ = 0.0f;
        this.body.mirror = false;
        (this.rear_right_leg = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-3.0f, 0.0f, -2.0f, 3, 5, 3, 0.0f);
        this.rear_right_leg.setRotationPoint(-2.0f, 19.0f, 4.0f);
        this.rear_right_leg.rotateAngleX = 0.0f;
        this.rear_right_leg.rotateAngleY = 0.0f;
        this.rear_right_leg.rotateAngleZ = 0.0f;
        this.rear_right_leg.mirror = false;
        (this.rear_left_leg = new ModelRenderer((ModelBase) this, 0, 8)).addBox(0.0f, 0.0f, -2.0f, 3, 5, 3, 0.0f);
        this.rear_left_leg.setRotationPoint(2.0f, 19.0f, 4.0f);
        this.rear_left_leg.rotateAngleX = 0.0f;
        this.rear_left_leg.rotateAngleY = 0.0f;
        this.rear_left_leg.rotateAngleZ = 0.0f;
        this.rear_left_leg.mirror = false;
        (this.right_eye = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-2.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        this.right_eye.setRotationPoint(-1.0f, 19.0f, -1.0f);
        this.right_eye.rotateAngleX = 0.0f;
        this.right_eye.rotateAngleY = 0.0f;
        this.right_eye.rotateAngleZ = 0.0f;
        this.right_eye.mirror = false;
        (this.left_eye = new ModelRenderer((ModelBase) this, 0, 4)).addBox(0.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        this.left_eye.setRotationPoint(1.0f, 19.0f, -1.0f);
        this.left_eye.rotateAngleX = 0.0f;
        this.left_eye.rotateAngleY = 0.0f;
        this.left_eye.rotateAngleZ = 0.0f;
        this.left_eye.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.frontleft_leg.render(f5);
        this.frontright_leg.render(f5);
        this.body.render(f5);
        this.rear_right_leg.render(f5);
        this.rear_left_leg.render(f5);
        this.right_eye.render(f5);
        this.left_eye.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        this.frontleft_leg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f1;
        this.rear_left_leg.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.4f * f1;
        this.rear_right_leg.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.4f * f1;
        this.frontright_leg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f1;
    }
}
