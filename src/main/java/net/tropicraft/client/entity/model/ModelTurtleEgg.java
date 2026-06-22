package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.tropicraft.entity.underdasea.EntityTurtleEgg;

public class ModelTurtleEgg extends ModelBase {

    public ModelRenderer Piece1;
    public int hatching;
    public double randRotator;

    public ModelTurtleEgg() {
        this.hatching = 0;
        this.randRotator = 1.0;
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.setTextureOffset("Piece1.Shape1", 0, 16);
        this.setTextureOffset("Piece1.Shape2", 0, 0);
        this.setTextureOffset("Piece1.Shape3", 0, 7);
        this.setTextureOffset("Piece1.Shape3", 24, 9);
        this.setTextureOffset("Piece1.Shape3", 16, 7);
        this.setTextureOffset("Piece1.Shape3", 8, 9);
        (this.Piece1 = new ModelRenderer((ModelBase) this, "Piece1")).setRotationPoint(0.0f, 24.0f, 0.0f);
        this.setRotation(this.Piece1, 0.0f, 0.0f, 0.0f);
        this.Piece1.mirror = true;
        this.Piece1.addBox("Shape1", -3.0f, -10.0f, -3.0f, 6, 10, 6);
        this.Piece1.addBox("Shape2", -1.5f, -11.0f, -1.5f, 3, 1, 3);
        this.Piece1.addBox("Shape3", 3.0f, -7.0f, -1.5f, 1, 6, 3);
        this.Piece1.addBox("Shape3", -1.5f, -7.0f, 3.0f, 3, 6, 1);
        this.Piece1.addBox("Shape3", -4.0f, -7.0f, -1.5f, 1, 6, 3);
        this.Piece1.addBox("Shape3", -1.5f, -7.0f, -4.0f, 3, 6, 1);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Piece1.render(f5);
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.hatching = ((EntityTurtleEgg) entityliving).hatchingTime;
        this.randRotator = ((EntityTurtleEgg) entityliving).rotationRand;
        if (this.hatching > 0) {
            this.Piece1.rotateAngleY = (float) Math.sin(this.hatching / 5.0);
            this.Piece1.rotateAngleX = (float) Math.sin(this.randRotator);
            this.Piece1.rotateAngleZ = (float) Math.sin(this.randRotator * 0.2);
        } else {
            this.hatching = 0;
            this.Piece1.rotateAngleY = 0.0f;
            this.Piece1.rotateAngleX = 0.0f;
            this.Piece1.rotateAngleZ = 0.0f;
        }
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
