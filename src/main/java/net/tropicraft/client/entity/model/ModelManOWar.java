package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelManOWar extends ModelBase {

    ModelRenderer Body;
    ModelRenderer CenterTent;
    ModelRenderer CenterTent2;
    ModelRenderer CenterTent3;
    ModelRenderer Tent1;
    ModelRenderer Tent2;
    ModelRenderer Tent3;
    ModelRenderer Tent4;
    public boolean isOnGround;

    public ModelManOWar(final int i, final int j, final boolean derp) {
        this.isOnGround = false;
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.setTextureOffset("Body.float", i, j);
        if (derp) {
            this.setTextureOffset("Body.tentbase", 0, 14);
            this.setTextureOffset("CenterTent.tent51", 7, 0);
            this.setTextureOffset("CenterTent2.tent52", 11, 0);
            this.setTextureOffset("CenterTent3.tent53", 11, 5);
            this.setTextureOffset("Tent1.tent1", 0, 0);
            this.setTextureOffset("Tent2.tent2", 0, 0);
            this.setTextureOffset("Tent3.tent3", 0, 0);
            this.setTextureOffset("Tent4.tent4", 0, 0);
            this.setTextureOffset("Body.Shape1", 32, 20);
        }
        if (!derp) {
            this.setTextureOffset("Body.Shape1", 15, -10);
            this.setTextureOffset("Body.tentbase", 32, 20);
            this.setTextureOffset("CenterTent.tent51", 32, 20);
            this.setTextureOffset("CenterTent2.tent52", 32, 20);
            this.setTextureOffset("CenterTent3.tent53", 32, 20);
            this.setTextureOffset("Tent1.tent1", 32, 20);
            this.setTextureOffset("Tent2.tent2", 32, 20);
            this.setTextureOffset("Tent3.tent3", 32, 20);
            this.setTextureOffset("Tent4.tent4", 32, 20);
        }
        (this.Body = new ModelRenderer((ModelBase) this, "Body")).setRotationPoint(0.0f, 18.0f, 0.0f);
        this.setRotation(this.Body, 0.0f, 0.0f, 0.0f);
        this.Body.mirror = true;
        this.Body.addBox("Shape1", 0.0f, -6.0f, -2.0f, 0, 6, 10);
        this.Body.addBox("tentbase", -2.0f, 0.0f, -2.0f, 4, 2, 4);
        (this.CenterTent = new ModelRenderer((ModelBase) this, "CenterTent")).setRotationPoint(0.0f, 2.0f, 0.0f);
        this.setRotation(this.CenterTent, 0.0f, 0.0f, 0.0f);
        this.CenterTent.mirror = true;
        this.CenterTent.addBox("tent51", -0.5f, 0.0f, -0.5f, 1, 10, 1);
        (this.CenterTent2 = new ModelRenderer((ModelBase) this, "CenterTent2")).setRotationPoint(0.0f, 10.0f, 0.0f);
        this.setRotation(this.CenterTent2, 0.0f, 0.0f, 0.0f);
        this.CenterTent2.mirror = true;
        this.CenterTent2.addBox("tent52", -0.5f, 0.0f, -0.5f, 1, 4, 1);
        (this.CenterTent3 = new ModelRenderer((ModelBase) this, "CenterTent3")).setRotationPoint(0.0f, 4.0f, 0.0f);
        this.setRotation(this.CenterTent3, 0.0f, 0.0f, 0.0f);
        this.CenterTent3.mirror = true;
        this.CenterTent3.addBox("tent53", -0.5f, 0.0f, -0.5f, 1, 5, 1);
        this.CenterTent2.addChild(this.CenterTent3);
        this.CenterTent.addChild(this.CenterTent2);
        this.Body.addChild(this.CenterTent);
        this.Body.addBox("float", -2.0f, -4.0f, -2.0f, 4, 4, 8);
        (this.Tent1 = new ModelRenderer((ModelBase) this, "Tent1")).setRotationPoint(-1.5f, 2.0f, -1.5f);
        this.setRotation(this.Tent1, 0.0f, 0.0f, 0.0f);
        this.Tent1.mirror = true;
        this.Tent1.addBox("tent1", -0.5f, 0.0f, -0.5f, 1, 11, 1);
        this.Body.addChild(this.Tent1);
        (this.Tent2 = new ModelRenderer((ModelBase) this, "Tent2")).setRotationPoint(1.5f, 2.0f, 1.5f);
        this.setRotation(this.Tent2, 0.0f, 0.0f, 0.0f);
        this.Tent2.mirror = true;
        this.Tent2.addBox("tent2", -0.5f, 0.0f, -0.5f, 1, 11, 1);
        this.Body.addChild(this.Tent2);
        (this.Tent3 = new ModelRenderer((ModelBase) this, "Tent3")).setRotationPoint(-1.5f, 2.0f, 1.5f);
        this.setRotation(this.Tent3, 0.0f, 0.0f, 0.0f);
        this.Tent3.mirror = true;
        this.Tent3.addBox("tent3", -0.5f, 0.0f, -0.5f, 1, 11, 1);
        this.Body.addChild(this.Tent3);
        (this.Tent4 = new ModelRenderer((ModelBase) this, "Tent4")).setRotationPoint(1.5f, 2.0f, -1.5f);
        this.setRotation(this.Tent4, 0.0f, 0.0f, 0.0f);
        this.Tent4.mirror = true;
        this.Tent4.addBox("tent4", -0.5f, 0.0f, -0.5f, 1, 11, 1);
        this.Body.addChild(this.Tent4);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, null);
        this.Body.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        if (this.isOnGround) {
            this.Tent3.rotateAngleZ = 0.0f;
            this.Tent3.rotateAngleX = 0.0f;
            this.Tent1.rotateAngleZ = 0.0f;
            this.Tent1.rotateAngleX = 0.0f;
            this.Tent4.rotateAngleZ = 0.0f;
            this.Tent4.rotateAngleX = 0.0f;
            this.Tent2.rotateAngleZ = 0.0f;
            this.Tent2.rotateAngleX = 0.0f;
            this.CenterTent.rotateAngleX = 0.0f;
            this.CenterTent2.rotateAngleX = 0.0f;
            this.CenterTent3.rotateAngleX = 0.0f;
        } else {
            this.Tent3.rotateAngleZ = (float) Math.sin(f2 * 0.1f) * 0.07f + 0.4f;
            this.Tent3.rotateAngleX = (float) Math.sin(f2 * 0.1f) * 0.05f + 0.4f;
            this.Tent1.rotateAngleZ = -(float) Math.sin(f2 * 0.1f) * 0.06f + 0.4f;
            this.Tent1.rotateAngleX = -(float) Math.sin(f2 * 0.1f) * 0.05f + 0.4f;
            this.Tent4.rotateAngleZ = -(float) Math.sin(f2 * 0.1f) * 0.06f - 0.4f;
            this.Tent4.rotateAngleX = -(float) Math.sin(f2 * 0.1f) * 0.04f + 0.4f;
            this.Tent2.rotateAngleZ = (float) Math.sin(f2 * 0.025f) * 0.05f - 0.4f;
            this.Tent2.rotateAngleX = (float) Math.sin(f2 * 0.025f) * 0.05f + 0.4f;
            this.CenterTent.rotateAngleX = (float) Math.sin(f2 * 0.0125f) * 0.05f + 0.2f;
            this.CenterTent2.rotateAngleX = (float) Math.sin(f2 * 0.0125f) * 0.65f + 1.507f;
            this.CenterTent3.rotateAngleX = Math.abs((float) Math.sin(f2 * 0.0125f) * 0.35f) - 1.25f;
        }
    }
}
