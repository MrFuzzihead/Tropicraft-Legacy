package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public class ModelSpiderEgg extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer body1;
    public ModelRenderer body2;
    public ModelRenderer body3;
    public ModelRenderer body4;

    public ModelSpiderEgg() {
        (this.body = new ModelRenderer((ModelBase) this, 0, 8)).addBox(-1.0f, -2.0f, -4.0f, 2, 4, 9, 0.0f);
        this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.body.rotateAngleX = 0.0f;
        this.body.rotateAngleY = 3.141593f;
        this.body.rotateAngleZ = 0.0f;
        this.body.mirror = false;
        float bodyWidth = 10.0f;
        float bodyHeight = 5.0f;
        final float entHeight = 2.0f;
        (this.body3 = new ModelRenderer((ModelBase) this, 0, 8)).addBox(
            -bodyWidth / 2.0f,
            -bodyHeight / 2.0f,
            -bodyWidth / 2.0f,
            (int) bodyWidth,
            (int) bodyHeight,
            (int) bodyWidth,
            0.0f);
        this.body3.setRotationPoint(0.0f, 16.0f, 0.0f);
        bodyWidth = 8.0f;
        bodyHeight = 3.0f;
        (this.body2 = new ModelRenderer((ModelBase) this, 0, 8)).addBox(
            -bodyWidth / 2.0f,
            -bodyHeight / 2.0f - 4.0f,
            -bodyWidth / 2.0f,
            (int) bodyWidth,
            (int) bodyHeight,
            (int) bodyWidth,
            0.0f);
        this.body2.setRotationPoint(0.0f, 16.0f, 0.0f);
        (this.body4 = new ModelRenderer((ModelBase) this, 0, 8)).addBox(
            -bodyWidth / 2.0f,
            -bodyHeight / 2.0f + 4.0f,
            -bodyWidth / 2.0f,
            (int) bodyWidth,
            (int) bodyHeight,
            (int) bodyWidth,
            0.0f);
        this.body4.setRotationPoint(0.0f, 16.0f, 0.0f);
        bodyWidth = 6.0f;
        bodyHeight = 2.0f;
        (this.body1 = new ModelRenderer((ModelBase) this, 0, 8)).addBox(
            -bodyWidth / 2.0f,
            -bodyHeight / 2.0f - 6.0f,
            -bodyWidth / 2.0f,
            (int) bodyWidth,
            (int) bodyHeight,
            (int) bodyWidth,
            0.0f);
        this.body1.setRotationPoint(0.0f, 16.0f, 0.0f);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        float scale = 1.0f - (float) (Math.sin(entity.worldObj.getWorldTime() / 2.0f) * 0.05000000074505806);
        GL11.glPushMatrix();
        GL11.glScalef(scale, 1.0f, scale);
        this.body3.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        scale = 1.0f - (float) (Math.sin((entity.worldObj.getWorldTime() + 2.0f) / 2.0f) * 0.05000000074505806);
        GL11.glScalef(scale, 1.0f, scale);
        this.body2.render(f5);
        this.body4.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        scale = 1.0f - (float) (Math.sin((entity.worldObj.getWorldTime() + 4.0f) / 2.0f) * 0.05000000074505806);
        GL11.glScalef(scale, 1.0f, scale);
        this.body1.render(f5);
        GL11.glPopMatrix();
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }

    public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.setLivingAnimationStand(entityliving, f, f1, f2);
    }

    public void setLivingAnimationSit(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.body.setRotationPoint(0.0f, 20.0f, 0.0f);
        this.body.rotateAngleX = 0.9320058f;
        this.body.rotateAngleY = 3.141593f;
    }

    public void setLivingAnimationClimb(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.body.rotateAngleX = 1.570796f;
        this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
    }

    public void setLivingAnimationStand(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.body.rotateAngleY = 3.141593f;
        this.body.rotateAngleX = 0.0f;
    }
}
