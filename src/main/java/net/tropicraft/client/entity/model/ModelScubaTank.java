package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelScubaTank extends ModelBase {

    ModelRenderer Tank1;
    ModelRenderer Tank1m1;
    ModelRenderer Tank1m2;
    ModelRenderer Tank1m3;
    ModelRenderer Tank1m4;
    ModelRenderer Tank1m5;
    ModelRenderer Tank1m6;
    ModelRenderer Tank1m7;

    public ModelScubaTank() {
        (this.Tank1 = new ModelRenderer((ModelBase) this, 41, 50)).setTextureSize(128, 64);
        this.Tank1.addBox(-2.0f, -5.0f, -2.0f, 4, 10, 4);
        this.Tank1.setRotationPoint(3.0f, 7.0f, 6.5f);
        (this.Tank1m1 = new ModelRenderer((ModelBase) this, 45, 54)).setTextureSize(128, 64);
        this.Tank1m1.addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1);
        this.Tank1m1.setRotationPoint(3.0f, 7.0f, 8.5f);
        (this.Tank1m2 = new ModelRenderer((ModelBase) this, 45, 54)).setTextureSize(128, 64);
        this.Tank1m2.addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1);
        this.Tank1m2.setRotationPoint(1.0f, 7.0f, 6.5f);
        (this.Tank1m3 = new ModelRenderer((ModelBase) this, 45, 54)).setTextureSize(128, 64);
        this.Tank1m3.addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1);
        this.Tank1m3.setRotationPoint(5.0f, 7.0f, 6.5f);
        (this.Tank1m4 = new ModelRenderer((ModelBase) this, 43, 46)).setTextureSize(128, 64);
        this.Tank1m4.addBox(-1.5f, -0.5f, -1.5f, 3, 1, 3);
        this.Tank1m4.setRotationPoint(3.0f, 1.5f, 6.5f);
        (this.Tank1m5 = new ModelRenderer((ModelBase) this, 38, 49)).setTextureSize(128, 64);
        this.Tank1m5.addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1);
        this.Tank1m5.setRotationPoint(3.0f, -0.5f, 6.5f);
        (this.Tank1m6 = new ModelRenderer((ModelBase) this, 44, 44)).setTextureSize(128, 64);
        this.Tank1m6.addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1);
        this.Tank1m6.setRotationPoint(3.5f, -0.5f, 6.5f);
        (this.Tank1m7 = new ModelRenderer((ModelBase) this, 36, 44)).setTextureSize(128, 64);
        this.Tank1m7.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2);
        this.Tank1m7.setRotationPoint(5.5f, -0.5f, 6.5f);
    }

    public void render(final Entity par1Entity, final float par2, final float par3, final float par4, final float par5,
        final float par6, final float par7) {
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
        this.Tank1.rotateAngleX = 0.0f;
        this.Tank1.rotateAngleY = 0.0f;
        this.Tank1.rotateAngleZ = 0.0f;
        this.Tank1.renderWithRotation(par7);
        this.Tank1m1.rotateAngleX = 0.0f;
        this.Tank1m1.rotateAngleY = 0.0f;
        this.Tank1m1.rotateAngleZ = 0.0f;
        this.Tank1m1.renderWithRotation(par7);
        this.Tank1m2.rotateAngleX = 0.0f;
        this.Tank1m2.rotateAngleY = -1.570796f;
        this.Tank1m2.rotateAngleZ = 0.0f;
        this.Tank1m2.renderWithRotation(par7);
        this.Tank1m3.rotateAngleX = 0.0f;
        this.Tank1m3.rotateAngleY = -1.570796f;
        this.Tank1m3.rotateAngleZ = 0.0f;
        this.Tank1m3.renderWithRotation(par7);
        this.Tank1m4.rotateAngleX = 0.0f;
        this.Tank1m4.rotateAngleY = 0.0f;
        this.Tank1m4.rotateAngleZ = 0.0f;
        this.Tank1m4.renderWithRotation(par7);
        this.Tank1m5.rotateAngleX = 0.0f;
        this.Tank1m5.rotateAngleY = 0.0f;
        this.Tank1m5.rotateAngleZ = 0.0f;
        this.Tank1m5.renderWithRotation(par7);
        this.Tank1m6.rotateAngleX = 0.0f;
        this.Tank1m6.rotateAngleY = 0.0f;
        this.Tank1m6.rotateAngleZ = 0.0f;
        this.Tank1m6.renderWithRotation(par7);
        this.Tank1m7.rotateAngleX = 0.0f;
        this.Tank1m7.rotateAngleY = 0.0f;
        this.Tank1m7.rotateAngleZ = 0.0f;
        this.Tank1m7.renderWithRotation(par7);
    }

    public void renderBambooMug() {
        final float f5 = 0.0625f;
        this.Tank1.render(f5);
        this.Tank1m1.render(f5);
        this.Tank1m2.render(f5);
        this.Tank1m3.render(f5);
        this.Tank1m4.render(f5);
        this.Tank1m5.render(f5);
        this.Tank1m6.render(f5);
        this.Tank1m7.render(f5);
    }
}
