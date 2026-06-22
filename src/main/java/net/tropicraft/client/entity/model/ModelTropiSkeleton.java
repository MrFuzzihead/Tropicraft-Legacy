package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;

public class ModelTropiSkeleton extends ModelZombie {

    public ModelTropiSkeleton() {
        final float var1 = 0.0f;
        (this.bipedRightArm = new ModelRenderer((ModelBase) this, 40, 16)).addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, var1);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
        this.bipedLeftArm = new ModelRenderer((ModelBase) this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, var1);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
        (this.bipedRightLeg = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, var1);
        this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f, 0.0f);
        this.bipedLeftLeg = new ModelRenderer((ModelBase) this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, var1);
        this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f, 0.0f);
        final ModelBox hulaSkirt = new ModelBox(this.bipedBody, 40, 0, -4.0f, 12.0f, -2.0f, 8, 3, 4, 0.0f);
        this.bipedBody.cubeList.add(hulaSkirt);
    }

    public void render(final Entity par1Entity, final float par2, final float par3, final float par4, final float par5,
        final float par6, final float par7) {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
        this.bipedHead.render(par7);
        this.bipedBody.render(par7);
        this.bipedRightArm.render(par7);
        this.bipedLeftArm.render(par7);
        this.bipedRightLeg.render(par7);
        this.bipedLeftLeg.render(par7);
    }
}
