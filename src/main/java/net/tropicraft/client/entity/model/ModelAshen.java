package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelAshen extends ModelBase {

    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer mask;
    public ModelRenderer leftArm;
    public ModelRenderer rightArm;
    public ModelRenderer leftArmSub;
    public ModelRenderer rightArmSub;
    public float headAngle;
    public boolean swinging;
    public int actionState;

    public ModelAshen() {
        this.swinging = false;
        this.actionState = 0;
        this.headAngle = 0.0f;
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.setTextureOffset("leftArm.leftArmBase", 0, 24);
        this.setTextureOffset("leftArmSub.leftArmTop", 31, 0);
        this.setTextureOffset("rightArm.rightArmBase", 0, 24);
        this.setTextureOffset("rightArmSub.rightArmTop", 31, 0);
        (this.rightLeg = new ModelRenderer((ModelBase) this, 25, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 7, 1);
        this.rightLeg.setRotationPoint(1.0f, 17.0f, 0.0f);
        this.rightLeg.setTextureSize(64, 32);
        this.rightLeg.mirror = true;
        this.setRotation(this.rightLeg, 0.0f, 0.0f, 0.0f);
        (this.leftLeg = new ModelRenderer((ModelBase) this, 25, 0)).addBox(-1.0f, 0.0f, 0.0f, 1, 7, 1);
        this.leftLeg.setRotationPoint(-1.0f, 17.0f, 0.0f);
        this.leftLeg.setTextureSize(64, 32);
        this.leftLeg.mirror = true;
        this.setRotation(this.leftLeg, 0.0f, 0.0f, 0.0f);
        (this.body = new ModelRenderer((ModelBase) this, 24, 8)).addBox(-2.0f, -3.0f, 0.0f, 4, 7, 3);
        this.body.setRotationPoint(0.0f, 13.0f, 2.0f);
        this.body.setTextureSize(64, 32);
        this.body.mirror = true;
        this.setRotation(this.body, 0.0f, 3.141593f, 0.0f);
        (this.head = new ModelRenderer((ModelBase) this, 24, 18)).addBox(-2.0f, -3.0f, -1.0f, 4, 3, 4);
        this.head.setRotationPoint(0.0f, 10.0f, 1.0f);
        this.head.setTextureSize(64, 32);
        this.head.mirror = true;
        this.setRotation(this.head, 0.0f, 3.141593f, 0.0f);
        (this.leftArm = new ModelRenderer((ModelBase) this, "leftArm")).setRotationPoint(-2.0f, 10.5f, 0.5f);
        this.setRotation(this.leftArm, 0.0f, 0.0f, 0.0f);
        this.leftArm.mirror = true;
        this.leftArm.addBox("leftArmBase", -6.0f, -0.5f, -0.5f, 6, 1, 1);
        (this.leftArmSub = new ModelRenderer((ModelBase) this, "leftArmSub")).setRotationPoint(-5.5f, 0.0f, 0.0f);
        this.setRotation(this.leftArmSub, 0.0f, 0.0f, 0.0f);
        this.leftArmSub.mirror = true;
        this.leftArmSub.addBox("leftArmTop", -0.5f, -6.0f, -0.5f, 1, 6, 1);
        this.leftArm.addChild(this.leftArmSub);
        (this.rightArm = new ModelRenderer((ModelBase) this, "rightArm")).setRotationPoint(2.0f, 10.46667f, 0.5f);
        this.setRotation(this.rightArm, 0.0f, 0.0f, 0.0f);
        this.rightArm.mirror = true;
        this.rightArm.addBox("rightArmBase", 0.0f, -0.5f, -0.5f, 6, 1, 1);
        (this.rightArmSub = new ModelRenderer((ModelBase) this, "rightArmSub")).setRotationPoint(5.5f, 0.0f, 0.0f);
        this.setRotation(this.rightArmSub, 0.0f, 0.0f, 0.0f);
        this.rightArmSub.mirror = true;
        this.rightArmSub.addBox("rightArmTop", -0.5f, -6.0f, -0.5f, 1, 6, 1);
        this.rightArm.addChild(this.rightArmSub);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.rightLeg.render(f5);
        this.leftLeg.render(f5);
        this.body.render(f5);
        this.head.render(f5);
        this.leftArm.render(f5);
        this.rightArm.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.25f * f1;
        this.leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.25f * f1;
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        this.head.rotateAngleX = f4 / 125.0f + this.headAngle;
        this.head.rotateAngleY = f3 / 125.0f + 3.14159f;
        final float ArmRotater = 1.247196f;
        final float subStraight = 1.570795f;
        switch (this.actionState) {
            case 1: {
                this.headAngle = -0.4f;
                this.leftArm.rotateAngleZ = -ArmRotater;
                this.leftArmSub.rotateAngleZ = -5.1f;
                this.rightArm.rotateAngleZ = ArmRotater;
                this.rightArmSub.rotateAngleZ = 5.1f;
                this.rightArm.rotateAngleX = subStraight;
                this.leftArm.rotateAngleX = subStraight;
                this.leftArm.rotateAngleY = -0.5f;
                this.rightArm.rotateAngleY = 0.5f;
                break;
            }
            case 2: {
                this.headAngle = 0.0f;
                this.rightArm.rotateAngleX = 1.65f + f3 / 125.0f;
                this.rightArm.rotateAngleY = 0.9f + f4 / 125.0f;
                this.rightArm.rotateAngleZ = ArmRotater;
                this.rightArmSub.rotateAngleZ = 6.2f;
                this.leftArm.rotateAngleZ = 0.0f - MathHelper.sin(f2 * 0.75f) * 0.022f;
                this.leftArm.rotateAngleY = 0.0f;
                this.leftArmSub.rotateAngleZ = 0.0f;
                if (this.swinging) {
                    final ModelRenderer leftArm = this.leftArm;
                    leftArm.rotateAngleX += MathHelper.sin(f2 * 0.75f) * 0.052f;
                    break;
                }
                this.leftArm.rotateAngleX = 0.0f;
                break;
            }
            default: {
                this.headAngle = 0.0f;
                this.leftArm.rotateAngleZ = -ArmRotater;
                this.leftArmSub.rotateAngleZ = -subStraight;
                this.rightArm.rotateAngleZ = ArmRotater;
                this.rightArmSub.rotateAngleZ = subStraight;
                this.leftArm.rotateAngleY = 0.0f;
                this.rightArm.rotateAngleY = 0.0f;
                break;
            }
        }
        final ModelRenderer rightArm = this.rightArm;
        rightArm.rotateAngleY += MathHelper.sin(f2 * 0.25f) * 0.002f;
        final ModelRenderer leftArm2 = this.leftArm;
        leftArm2.rotateAngleY -= MathHelper.sin(f2 * 0.25f) * 0.002f;
    }
}
