package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMarlin extends ModelBase {

    ModelRenderer Body;
    ModelRenderer DorsalFin1;
    ModelRenderer LeftFin;
    ModelRenderer RightFin;
    ModelRenderer BottomFIn;
    ModelRenderer head;
    ModelRenderer tail;
    ModelRenderer tail1;
    ModelRenderer sword;
    ModelRenderer tail3;
    ModelRenderer tailEndB;
    ModelRenderer tailEndT;
    public boolean inWater;

    public ModelMarlin() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.setTextureOffset("head.Head", 46, 24);
        this.setTextureOffset("head.Nose2", 28, 0);
        this.setTextureOffset("head.Nose3", 22, 0);
        this.setTextureOffset("head.DorsalFin2", 23, 24);
        this.setTextureOffset("sword.Nose1", 0, 0);
        this.setTextureOffset("tail.TailBase", 0, 13);
        this.setTextureOffset("tail1.TailMid", 0, 5);
        this.setTextureOffset("tail3.TailEnd", 46, 0);
        this.setTextureOffset("tailEndB.TailFinBottom", 40, 0);
        this.setTextureOffset("tailEndT.TailFinTop", 34, 0);
        (this.Body = new ModelRenderer((ModelBase) this, 0, 22)).addBox(-5.0f, -3.0f, -2.0f, 7, 6, 4);
        this.Body.setRotationPoint(0.0f, 19.0f, 0.0f);
        this.Body.setTextureSize(64, 32);
        this.Body.mirror = true;
        this.setRotation(this.Body, 0.0f, -1.570796f, 0.0f);
        (this.DorsalFin1 = new ModelRenderer((ModelBase) this, 24, 20)).addBox(-0.5f, -0.5f, -0.5f, 1, 2, 10);
        this.DorsalFin1.setRotationPoint(0.0f, 15.5f, -5.0f);
        this.DorsalFin1.setTextureSize(64, 32);
        this.DorsalFin1.mirror = true;
        this.setRotation(this.DorsalFin1, 0.0f, 0.0f, 0.0f);
        (this.LeftFin = new ModelRenderer((ModelBase) this, 12, 10)).addBox(0.0f, -0.5f, -2.0f, 4, 1, 2);
        this.LeftFin.setRotationPoint(2.0f, 21.0f, -3.0f);
        this.LeftFin.setTextureSize(64, 32);
        this.LeftFin.mirror = true;
        this.setRotation(this.LeftFin, 0.0f, 0.0f, 0.0f);
        (this.RightFin = new ModelRenderer((ModelBase) this, 12, 7)).addBox(-4.0f, -0.5f, -2.0f, 4, 1, 2);
        this.RightFin.setRotationPoint(-2.0f, 21.0f, -3.0f);
        this.RightFin.setTextureSize(64, 32);
        this.RightFin.mirror = true;
        this.setRotation(this.RightFin, 0.0f, 0.0f, 0.0f);
        (this.BottomFIn = new ModelRenderer((ModelBase) this, 52, 0)).addBox(-0.5f, 2.0f, -2.5f, 1, 3, 2);
        this.BottomFIn.setRotationPoint(0.0f, 19.0f, 0.0f);
        this.BottomFIn.setTextureSize(64, 32);
        this.BottomFIn.mirror = true;
        this.setRotation(this.BottomFIn, 0.6981317f, 0.0f, 0.0f);
        (this.head = new ModelRenderer((ModelBase) this, "head")).setRotationPoint(0.0f, 20.0f, -5.0f);
        this.setRotation(this.head, 0.0f, 0.0f, 0.0f);
        this.head.mirror = true;
        this.head.addBox("Head", -1.5f, -3.0f, -3.0f, 3, 5, 3);
        this.head.addBox("Nose2", -1.0f, -1.5f, -4.0f, 2, 3, 1);
        this.head.addBox("Nose3", -0.5f, -0.5f, -6.0f, 1, 2, 2);
        this.head.addBox("DorsalFin2", -0.5f, -6.0f, -2.5f, 1, 3, 2);
        (this.sword = new ModelRenderer((ModelBase) this, "sword")).setRotationPoint(0.0f, 0.0f, 0.0f);
        this.setRotation(this.sword, 0.0f, 1.5707f, 0.0f);
        this.sword.mirror = true;
        this.sword.addBox("Nose1", 4.0f, -1.5f, -0.5f, 10, 1, 1);
        this.head.addChild(this.sword);
        (this.tail = new ModelRenderer((ModelBase) this, "tail")).setRotationPoint(0.0f, 19.0f, 2.0f);
        this.setRotation(this.tail, 0.0f, 0.0f, 0.0f);
        this.tail.mirror = true;
        this.tail.addBox("TailBase", -1.5f, -2.0f, 0.0f, 3, 5, 4);
        (this.tail1 = new ModelRenderer((ModelBase) this, "tail1")).setRotationPoint(0.0f, 0.0f, 4.0f);
        this.setRotation(this.tail1, 0.0f, 0.0f, 0.0f);
        this.tail1.mirror = true;
        this.tail1.addBox("TailMid", -1.0f, -1.5f, 0.0f, 2, 4, 4);
        (this.tail3 = new ModelRenderer((ModelBase) this, "tail3")).setRotationPoint(0.0f, 1.0f, 4.0f);
        this.setRotation(this.tail3, 0.0f, 0.0f, 0.0f);
        this.tail3.mirror = true;
        this.tail3.addBox("TailEnd", -0.5f, -1.5f, 0.0f, 1, 3, 2);
        (this.tailEndB = new ModelRenderer((ModelBase) this, "tailEndB")).setRotationPoint(0.0f, 0.0f, 0.0f);
        this.setRotation(this.tailEndB, 0.593411f, 0.0f, 0.0f);
        this.tailEndB.mirror = true;
        this.tailEndB.addBox("TailFinBottom", -0.5f, 1.0f, -1.0f, 1, 5, 2);
        this.tail3.addChild(this.tailEndB);
        (this.tailEndT = new ModelRenderer((ModelBase) this, "tailEndT")).setRotationPoint(0.0f, 0.0f, 0.0f);
        this.setRotation(this.tailEndT, 2.548179f, 0.0f, 0.0f);
        this.tailEndT.mirror = true;
        this.tailEndT.addBox("TailFinTop", -0.5f, 1.0f, -1.0f, 1, 5, 2);
        this.tail3.addChild(this.tailEndT);
        this.tail1.addChild(this.tail3);
        this.tail.addChild(this.tail1);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Body.render(f5);
        this.DorsalFin1.render(f5);
        this.LeftFin.render(f5);
        this.RightFin.render(f5);
        this.BottomFIn.render(f5);
        this.head.render(f5);
        this.tail.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        final float angle = f2 * 3.14159f / 180.0f;
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        if (!this.inWater) {
            this.head.rotateAngleY = (float) Math.sin(f2 * 0.55f) * 0.26f;
            this.tail.rotateAngleY = (float) Math.sin(f2 * 0.55f) * 0.26f;
            this.tail1.rotateAngleY = (float) Math.sin(f2 * 0.55f) * 0.26f;
            this.tail3.rotateAngleY = (float) Math.sin(f2 * 0.55f) * 0.26f;
            this.LeftFin.rotateAngleZ = (float) Math.sin(f2 * 0.25f) * 0.165f + 0.523598f;
            this.RightFin.rotateAngleZ = -(float) Math.sin(f2 * 0.25f) * 0.165f - 0.523598f;
            this.LeftFin.rotateAngleY = -1.5f;
            this.RightFin.rotateAngleY = 1.5f - (float) Math.sin(f2 * 0.25f) * 0.165f - 0.523598f;
        } else {
            this.head.rotateAngleY = (float) Math.sin(f2 * 0.25f) * 0.135f;
            this.tail.rotateAngleY = (float) Math.sin(f2 * 0.25f) * 0.135f;
            this.tail1.rotateAngleY = (float) Math.sin(f2 * 0.35f) * 0.15f;
            this.tail3.rotateAngleY = (float) Math.sin(f2 * 0.45f) * 0.16f;
            this.LeftFin.rotateAngleZ = (float) Math.sin(f2 * 0.25f) * 0.165f + 0.523598f;
            this.RightFin.rotateAngleZ = -(float) Math.sin(f2 * 0.25f) * 0.165f - 0.523598f;
            this.LeftFin.rotateAngleY = -0.392699f;
            this.RightFin.rotateAngleY = 0.392699f;
        }
    }
}
