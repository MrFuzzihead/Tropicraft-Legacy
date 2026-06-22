package net.tropicraft.client.block.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBamboo extends ModelBase {

    ModelRenderer chute1;
    ModelRenderer chute1m1;
    ModelRenderer chute1m2;
    ModelRenderer chute1m3;
    ModelRenderer chute2;
    ModelRenderer chute2m1;
    ModelRenderer chute2m2;
    ModelRenderer chute3m3;
    ModelRenderer leaves1;
    ModelRenderer leaves4;
    ModelRenderer leaves2;
    ModelRenderer leaves3;
    ModelRenderer leaves5;
    ModelRenderer leaves6;
    ModelRenderer leaves7;
    ModelRenderer leaves8;

    public ModelBamboo() {
        this(0.0f);
    }

    public ModelBamboo(final float par1) {
        (this.chute1 = new ModelRenderer((ModelBase) this, 1, 1)).setTextureSize(64, 64);
        this.chute1.addBox(-2.5f, -0.5f, -2.5f, 5, 1, 5);
        this.chute1.setRotationPoint(-4.0f, 23.5f, 4.0f);
        (this.chute1m1 = new ModelRenderer((ModelBase) this, 0, 7)).setTextureSize(64, 64);
        this.chute1m1.addBox(-3.0f, -3.5f, -3.0f, 6, 7, 6);
        this.chute1m1.setRotationPoint(-4.0f, 19.5f, 4.0f);
        (this.chute1m2 = new ModelRenderer((ModelBase) this, 1, 1)).setTextureSize(64, 64);
        this.chute1m2.addBox(-2.5f, -0.5f, -2.5f, 5, 1, 5);
        this.chute1m2.setRotationPoint(-4.0f, 15.5f, 4.0f);
        (this.chute1m3 = new ModelRenderer((ModelBase) this, 25, 7)).setTextureSize(64, 64);
        this.chute1m3.addBox(-3.0f, -3.5f, -3.0f, 6, 7, 6);
        this.chute1m3.setRotationPoint(-4.0f, 11.5f, 4.0f);
        (this.chute2 = new ModelRenderer((ModelBase) this, 1, 1)).setTextureSize(64, 64);
        this.chute2.addBox(-2.5f, -0.5f, -2.5f, 5, 1, 5);
        this.chute2.setRotationPoint(4.0f, 23.5f, -4.0f);
        (this.chute2m1 = new ModelRenderer((ModelBase) this, 25, 7)).setTextureSize(64, 64);
        this.chute2m1.addBox(-3.0f, -3.5f, -3.0f, 6, 7, 6);
        this.chute2m1.setRotationPoint(4.0f, 19.5f, -4.0f);
        (this.chute2m2 = new ModelRenderer((ModelBase) this, 1, 1)).setTextureSize(64, 64);
        this.chute2m2.addBox(-2.5f, -0.5f, -2.5f, 5, 1, 5);
        this.chute2m2.setRotationPoint(4.0f, 15.5f, -4.0f);
        (this.chute3m3 = new ModelRenderer((ModelBase) this, 0, 7)).setTextureSize(64, 64);
        this.chute3m3.addBox(-3.0f, -3.5f, -3.0f, 6, 7, 6);
        this.chute3m3.setRotationPoint(4.0f, 11.5f, -4.0f);
        (this.leaves1 = new ModelRenderer((ModelBase) this, 21, 41)).setTextureSize(64, 64);
        this.leaves1.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves1.setRotationPoint(6.0f, 14.0f, -11.0f);
        (this.leaves4 = new ModelRenderer((ModelBase) this, 21, 22)).setTextureSize(64, 64);
        this.leaves4.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves4.setRotationPoint(11.0f, 14.0f, -2.0f);
        (this.leaves2 = new ModelRenderer((ModelBase) this, 4, 41)).setTextureSize(64, 64);
        this.leaves2.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves2.setRotationPoint(3.0f, 17.0f, 3.0f);
        (this.leaves3 = new ModelRenderer((ModelBase) this, 4, 22)).setTextureSize(64, 64);
        this.leaves3.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves3.setRotationPoint(-3.0f, 13.0f, -5.0f);
        (this.leaves5 = new ModelRenderer((ModelBase) this, 21, 41)).setTextureSize(64, 64);
        this.leaves5.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves5.setRotationPoint(-2.0f, 16.0f, -3.0f);
        (this.leaves6 = new ModelRenderer((ModelBase) this, 21, 22)).setTextureSize(64, 64);
        this.leaves6.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves6.setRotationPoint(3.0f, 16.0f, 6.0f);
        (this.leaves7 = new ModelRenderer((ModelBase) this, 4, 41)).setTextureSize(64, 64);
        this.leaves7.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves7.setRotationPoint(-5.0f, 19.0f, 11.0f);
        (this.leaves8 = new ModelRenderer((ModelBase) this, 4, 22)).setTextureSize(64, 64);
        this.leaves8.addBox(0.0f, -5.0f, -4.0f, 0, 10, 8);
        this.leaves8.setRotationPoint(-11.0f, 15.0f, 3.0f);
    }

    public void render(final Entity par1Entity, final float par2, final float par3, final float par4, final float par5,
        final float par6, final float par7) {
        this.chute1.rotateAngleX = 0.0f;
        this.chute1.rotateAngleY = 0.0f;
        this.chute1.rotateAngleZ = 0.0f;
        this.chute1.renderWithRotation(par7);
        this.chute1m1.rotateAngleX = 0.0f;
        this.chute1m1.rotateAngleY = 0.0f;
        this.chute1m1.rotateAngleZ = 0.0f;
        this.chute1m1.renderWithRotation(par7);
        this.chute1m2.rotateAngleX = 0.0f;
        this.chute1m2.rotateAngleY = 0.0f;
        this.chute1m2.rotateAngleZ = 0.0f;
        this.chute1m2.renderWithRotation(par7);
        this.chute1m3.rotateAngleX = 0.0f;
        this.chute1m3.rotateAngleY = 0.0f;
        this.chute1m3.rotateAngleZ = 0.0f;
        this.chute1m3.renderWithRotation(par7);
        this.chute2.rotateAngleX = 0.0f;
        this.chute2.rotateAngleY = 0.0f;
        this.chute2.rotateAngleZ = 0.0f;
        this.chute2.renderWithRotation(par7);
        this.chute2m1.rotateAngleX = 0.0f;
        this.chute2m1.rotateAngleY = 0.0f;
        this.chute2m1.rotateAngleZ = 0.0f;
        this.chute2m1.renderWithRotation(par7);
        this.chute2m2.rotateAngleX = 0.0f;
        this.chute2m2.rotateAngleY = 0.0f;
        this.chute2m2.rotateAngleZ = 0.0f;
        this.chute2m2.renderWithRotation(par7);
        this.chute3m3.rotateAngleX = 0.0f;
        this.chute3m3.rotateAngleY = 0.0f;
        this.chute3m3.rotateAngleZ = 0.0f;
        this.chute3m3.renderWithRotation(par7);
        this.leaves1.rotateAngleX = 0.0f;
        this.leaves1.rotateAngleY = 0.0f;
        this.leaves1.rotateAngleZ = 0.0f;
        this.leaves1.renderWithRotation(par7);
        this.leaves4.rotateAngleX = 0.0f;
        this.leaves4.rotateAngleY = -1.570796f;
        this.leaves4.rotateAngleZ = 0.0f;
        this.leaves4.renderWithRotation(par7);
        this.leaves2.rotateAngleX = 0.0f;
        this.leaves2.rotateAngleY = 0.0f;
        this.leaves2.rotateAngleZ = 0.0f;
        this.leaves2.renderWithRotation(par7);
        this.leaves3.rotateAngleX = 0.0f;
        this.leaves3.rotateAngleY = -1.570796f;
        this.leaves3.rotateAngleZ = 0.0f;
        this.leaves3.renderWithRotation(par7);
        this.leaves5.rotateAngleX = 0.0f;
        this.leaves5.rotateAngleY = 0.0f;
        this.leaves5.rotateAngleZ = 0.0f;
        this.leaves5.renderWithRotation(par7);
        this.leaves6.rotateAngleX = 0.0f;
        this.leaves6.rotateAngleY = -1.570796f;
        this.leaves6.rotateAngleZ = 0.0f;
        this.leaves6.renderWithRotation(par7);
        this.leaves7.rotateAngleX = 0.0f;
        this.leaves7.rotateAngleY = 0.0f;
        this.leaves7.rotateAngleZ = 0.0f;
        this.leaves7.renderWithRotation(par7);
        this.leaves8.rotateAngleX = 0.0f;
        this.leaves8.rotateAngleY = -1.570796f;
        this.leaves8.rotateAngleZ = 0.0f;
        this.leaves8.renderWithRotation(par7);
    }
}
