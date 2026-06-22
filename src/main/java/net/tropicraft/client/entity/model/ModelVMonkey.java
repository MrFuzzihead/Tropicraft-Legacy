package net.tropicraft.client.entity.model;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.tropicraft.entity.passive.VMonkey;

public class ModelVMonkey extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer lLegUpper;
    public ModelRenderer rLegUpper;
    public ModelRenderer rArmUpper;
    public ModelRenderer lArmUpper;
    public ModelRenderer tailBase;
    public ModelRenderer tailMid;
    public ModelRenderer tailTop;
    public ModelRenderer rArmLower;
    public ModelRenderer lArmLower;
    public ModelRenderer lLegLower;
    public ModelRenderer rLegLower;
    public ModelRenderer Face;
    public ModelRenderer Head;
    protected Random rand;
    public float herps;

    public ModelVMonkey() {
        (this.body = new ModelRenderer((ModelBase) this, 0, 8)).addBox(-1.0f, -2.0f, -4.0f, 2, 4, 9, 0.0f);
        this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.body.rotateAngleX = 0.0f;
        this.body.rotateAngleY = 3.141593f;
        this.body.rotateAngleZ = 0.0f;
        this.body.mirror = false;
        (this.lLegUpper = new ModelRenderer((ModelBase) this, 7, 0)).addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.lLegUpper.setRotationPoint(-1.0f, 14.0f, -3.5f);
        this.lLegUpper.rotateAngleX = 0.0f;
        this.lLegUpper.rotateAngleY = 0.0f;
        this.lLegUpper.rotateAngleZ = 0.0f;
        this.lLegUpper.mirror = false;
        (this.rLegUpper = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.rLegUpper.setRotationPoint(1.0f, 14.0f, -3.5f);
        this.rLegUpper.rotateAngleX = 0.0f;
        this.rLegUpper.rotateAngleY = 0.0f;
        this.rLegUpper.rotateAngleZ = 0.0f;
        this.rLegUpper.mirror = false;
        (this.rArmUpper = new ModelRenderer((ModelBase) this, 0, 0)).addBox(0.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.rArmUpper.setRotationPoint(1.0f, 14.0f, 3.5f);
        this.rArmUpper.rotateAngleX = 0.0f;
        this.rArmUpper.rotateAngleY = 0.0f;
        this.rArmUpper.rotateAngleZ = 0.0f;
        this.rArmUpper.mirror = false;
        (this.lArmUpper = new ModelRenderer((ModelBase) this, 7, 0)).addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.lArmUpper.setRotationPoint(-1.0f, 14.0f, 3.5f);
        this.lArmUpper.rotateAngleX = 0.0f;
        this.lArmUpper.rotateAngleY = 0.0f;
        this.lArmUpper.rotateAngleZ = 0.0f;
        this.lArmUpper.mirror = false;
        (this.tailBase = new ModelRenderer((ModelBase) this, 20, 27)).addBox(-0.5f, -4.0f, -0.5f, 1, 3, 1, 0.0f);
        this.tailBase.setRotationPoint(0.0f, 15.0f, 3.5f);
        this.tailBase.rotateAngleX = 0.0f;
        this.tailBase.rotateAngleY = 3.141593f;
        this.tailBase.rotateAngleZ = 0.0f;
        this.tailBase.mirror = false;
        (this.tailMid = new ModelRenderer((ModelBase) this, 20, 24)).addBox(-0.5f, -2.0f, -0.5f, 1, 2, 1, 0.0f);
        this.tailMid.setRotationPoint(0.0f, 11.0f, 3.5f);
        this.tailMid.rotateAngleX = 0.0f;
        this.tailMid.rotateAngleY = 3.141593f;
        this.tailMid.rotateAngleZ = 0.0f;
        this.tailMid.mirror = false;
        (this.tailTop = new ModelRenderer((ModelBase) this, 20, 21)).addBox(-0.5f, -2.0f, -0.5f, 1, 2, 1, 0.0f);
        this.tailTop.setRotationPoint(0.0f, 9.0f, 3.5f);
        this.tailTop.rotateAngleX = 0.0f;
        this.tailTop.rotateAngleY = 3.141593f;
        this.tailTop.rotateAngleZ = 0.0f;
        this.tailTop.mirror = false;
        (this.rArmLower = new ModelRenderer((ModelBase) this, 0, 7)).addBox(0.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.rArmLower.setRotationPoint(1.0f, 19.0f, 3.5f);
        this.rArmLower.rotateAngleX = 0.0f;
        this.rArmLower.rotateAngleY = 0.0f;
        this.rArmLower.rotateAngleZ = 0.0f;
        this.rArmLower.mirror = false;
        (this.lArmLower = new ModelRenderer((ModelBase) this, 12, 0)).addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.lArmLower.setRotationPoint(-1.0f, 19.0f, 3.5f);
        this.lArmLower.rotateAngleX = 0.0f;
        this.lArmLower.rotateAngleY = 0.0f;
        this.lArmLower.rotateAngleZ = 0.0f;
        this.lArmLower.mirror = false;
        (this.lLegLower = new ModelRenderer((ModelBase) this, 12, 0)).addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.lLegLower.setRotationPoint(-1.0f, 19.0f, -3.5f);
        this.lLegLower.rotateAngleX = 0.0f;
        this.lLegLower.rotateAngleY = 0.0f;
        this.lLegLower.rotateAngleZ = 0.0f;
        this.lLegLower.mirror = false;
        (this.rLegLower = new ModelRenderer((ModelBase) this, 0, 7)).addBox(0.0f, 0.0f, -0.5f, 1, 5, 1, 0.0f);
        this.rLegLower.setRotationPoint(1.0f, 19.0f, -3.5f);
        this.rLegLower.rotateAngleX = 0.0f;
        this.rLegLower.rotateAngleY = 0.0f;
        this.rLegLower.rotateAngleZ = 0.0f;
        this.rLegLower.mirror = false;
        (this.Face = new ModelRenderer((ModelBase) this, 0, 25)).addBox(-2.0f, -1.0f, 0.0f, 4, 4, 3, 0.0f);
        this.Face.setRotationPoint(0.0f, 15.0f, -5.0f);
        this.Face.rotateAngleX = 0.0f;
        this.Face.rotateAngleY = 3.141593f;
        this.Face.rotateAngleZ = 0.0f;
        this.Face.mirror = false;
        (this.Head = new ModelRenderer((ModelBase) this, 25, 25)).addBox(-3.0f, -2.0f, 0.0f, 6, 5, 2, 0.0f);
        this.Head.setRotationPoint(0.0f, 15.0f, -5.0f);
        this.Head.rotateAngleX = 0.0f;
        this.Head.rotateAngleY = 3.141593f;
        this.Head.rotateAngleZ = 0.0f;
        this.Head.mirror = false;
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.body.render(f5);
        this.lLegUpper.render(f5);
        this.rLegUpper.render(f5);
        this.rArmUpper.render(f5);
        this.lArmUpper.render(f5);
        this.tailBase.render(f5);
        this.tailMid.render(f5);
        this.tailTop.render(f5);
        this.rArmLower.render(f5);
        this.lArmLower.render(f5);
        this.lLegLower.render(f5);
        this.rLegLower.render(f5);
        this.Face.render(f5);
        this.Head.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        this.Face.rotateAngleX = f4 / 57.29578f + this.herps;
        this.Face.rotateAngleY = f3 / 57.29578f + 3.141593f;
        this.Head.rotateAngleX = this.Face.rotateAngleX;
        this.Head.rotateAngleY = this.Face.rotateAngleY;
    }

    public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        final VMonkey entityvmonkey = (VMonkey) entityliving;
        if (entityvmonkey.isSitting) {
            this.body.setRotationPoint(0.0f, 20.0f, 0.0f);
            this.body.rotateAngleX = 0.9320058f;
            this.body.rotateAngleY = 3.141593f;
            this.lLegUpper.setRotationPoint(-1.0f, 16.0f, -1.5f);
            this.lLegUpper.rotateAngleX = -0.2792527f;
            this.rLegUpper.setRotationPoint(1.0f, 16.0f, -1.5f);
            this.rLegUpper.rotateAngleX = -0.2792527f;
            this.rLegUpper.rotateAngleY = 0.005817764f;
            this.rArmUpper.setRotationPoint(1.0f, 22.0f, 3.5f);
            this.rArmUpper.rotateAngleX = -2.142101f;
            this.lArmUpper.setRotationPoint(-1.0f, 22.0f, 3.5f);
            this.lArmUpper.rotateAngleX = -2.142043f;
            this.tailBase.setRotationPoint(0.0f, 22.0f, 2.466667f);
            this.tailBase.rotateAngleX = 1.902409f;
            this.tailBase.rotateAngleY = 3.141593f;
            this.tailMid.setRotationPoint(0.0f, 23.3f, 5.966667f);
            this.tailMid.rotateAngleX = 1.570796f;
            this.tailMid.rotateAngleY = 2.111848f;
            this.tailMid.rotateAngleZ = -0.2617994f;
            this.tailTop.setRotationPoint(-1.0f, 23.2f, 7.0f);
            this.tailTop.rotateAngleX = 1.570796f;
            this.tailTop.rotateAngleY = 0.8377581f;
            this.tailTop.rotateAngleZ = 0.01745329f;
            this.rArmLower.setRotationPoint(1.0f, 19.0f, -0.5f);
            this.rArmLower.rotateAngleX = -0.1489348f;
            this.lArmLower.setRotationPoint(-1.0f, 19.0f, -0.3f);
            this.lArmLower.rotateAngleX = -0.1492257f;
            this.lLegLower.setRotationPoint(-1.0f, 21.0f, -2.8f);
            this.lLegLower.rotateAngleX = -0.9599311f;
            this.rLegLower.setRotationPoint(1.0f, 21.0f, -2.833333f);
            this.rLegLower.rotateAngleX = -0.9599311f;
            this.Face.setRotationPoint(0.0f, 15.0f, -3.0f);
            this.Head.setRotationPoint(0.0f, 15.0f, -3.0f);
            this.herps = 0.0f;
        } else if (entityvmonkey.isClimbing) {
            this.body.rotateAngleX = 1.570796f;
            this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
            this.lLegUpper.setRotationPoint(-1.0f, 12.0f, 2.0f);
            this.rLegUpper.setRotationPoint(1.0f, 12.0f, 2.0f);
            this.rArmUpper.setRotationPoint(1.0f, 19.5f, 2.0f);
            this.lArmUpper.setRotationPoint(-1.0f, 19.5f, 2.0f);
            this.tailBase.setRotationPoint(0.0f, 19.5f, 0.5f);
            this.tailBase.rotateAngleX = 1.570796f;
            this.tailBase.rotateAngleY = 3.141593f;
            this.tailMid.setRotationPoint(0.0f, 19.5f, 4.5f);
            this.tailMid.rotateAngleX = 1.570796f;
            this.tailMid.rotateAngleY = 3.141593f;
            this.tailTop.setRotationPoint(0.0f, 19.5f, 6.5f);
            this.tailTop.rotateAngleX = 1.570796f;
            this.tailTop.rotateAngleY = 3.141593f;
            this.rArmLower.setRotationPoint(1.0f, 19.5f, -3.0f);
            this.lArmLower.setRotationPoint(-1.0f, 19.5f, -3.0f);
            this.lLegLower.setRotationPoint(-1.0f, 12.0f, -3.0f);
            this.rLegLower.setRotationPoint(1.0f, 12.0f, -3.0f);
            this.Face.setRotationPoint(0.0f, 11.0f, 1.0f);
            this.herps = 1.570796f;
            this.Head.setRotationPoint(0.0f, 11.0f, 1.0f);
            this.Head.rotateAngleX = 1.570796f;
            this.rLegUpper.rotateAngleX = MathHelper.cos(f * 0.5f) * 0.75f * f2 - 1.570796f;
            this.rArmUpper.rotateAngleX = MathHelper.cos(f * 0.5f) * 0.75f * f2 - 1.570796f;
            this.lArmUpper.rotateAngleX = MathHelper.cos(f * 0.5f) * 0.75f * f2 - 1.570796f;
            this.lLegUpper.rotateAngleX = MathHelper.cos(f * 0.5f) * 0.75f * f2 - 1.570796f;
            this.rLegLower.setRotationPoint(
                1.0f,
                12.0f + MathHelper.cos(this.rLegUpper.rotateAngleX) * 5.0f,
                -3.0f - (5.0f + MathHelper.sin(this.rLegUpper.rotateAngleX) * 5.0f));
            this.rArmLower.setRotationPoint(
                1.0f,
                19.5f + MathHelper.cos(this.rArmUpper.rotateAngleX) * 5.0f,
                -3.0f - (5.0f + MathHelper.sin(this.rArmUpper.rotateAngleX) * 5.0f));
            this.lArmLower.setRotationPoint(
                -1.0f,
                19.5f + MathHelper.cos(this.lArmUpper.rotateAngleX) * 5.0f,
                -3.0f - (5.0f + MathHelper.sin(this.lArmUpper.rotateAngleX) * 5.0f));
            this.lLegLower.setRotationPoint(
                -1.0f,
                12.0f + MathHelper.cos(this.lLegUpper.rotateAngleX) * 5.0f,
                -3.0f - (5.0f + MathHelper.sin(this.lLegUpper.rotateAngleX) * 5.0f));
            this.rLegLower.rotateAngleX = this.rLegUpper.rotateAngleX - 0.6981317f;
            this.rArmLower.rotateAngleX = this.rArmUpper.rotateAngleX + 0.6981317f;
            this.lLegLower.rotateAngleX = this.lLegUpper.rotateAngleX - 0.6981317f;
            this.lArmLower.rotateAngleX = this.lArmUpper.rotateAngleX + 0.6981317f;
        } else {
            this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
            this.body.rotateAngleY = 3.141593f;
            this.body.rotateAngleX = 0.0f;
            this.lLegUpper.setRotationPoint(-1.0f, 14.0f, -3.5f);
            this.rLegUpper.setRotationPoint(1.0f, 14.0f, -3.5f);
            this.rArmUpper.setRotationPoint(1.0f, 14.0f, 3.5f);
            this.lArmUpper.setRotationPoint(-1.0f, 14.0f, 3.5f);
            this.tailBase.setRotationPoint(0.0f, 15.0f, 3.5f);
            this.tailBase.rotateAngleX = 0.0f;
            this.tailBase.rotateAngleY = 3.141593f;
            this.tailBase.rotateAngleZ = 0.0f;
            this.tailMid.setRotationPoint(0.0f, 11.0f, 3.5f);
            this.tailMid.rotateAngleX = 0.0f;
            this.tailMid.rotateAngleY = 3.141593f;
            this.tailMid.rotateAngleZ = 0.0f;
            this.tailTop.setRotationPoint(0.0f, 9.0f, 3.5f);
            this.tailTop.rotateAngleX = 0.0f;
            this.tailTop.rotateAngleY = 3.141593f;
            this.tailTop.rotateAngleZ = 0.0f;
            this.Face.setRotationPoint(0.0f, 15.0f, -5.0f);
            this.Head.setRotationPoint(0.0f, 15.0f, -5.0f);
            this.rLegUpper.rotateAngleX = MathHelper.cos(f * 0.6662f) * 0.75f * f1;
            this.rArmUpper.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 0.75f * f1;
            this.lLegUpper.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 0.75f * f1;
            this.lArmUpper.rotateAngleX = MathHelper.cos(f * 0.6662f) * 0.75f * f1;
            this.rLegLower.setRotationPoint(
                1.0f,
                19.0f - (5.0f - MathHelper.sin(this.rLegUpper.rotateAngleX + 1.5707964f) * 5.0f),
                -3.5f - MathHelper.cos(this.rLegUpper.rotateAngleX + 1.5707964f) * 5.0f);
            this.rArmLower.setRotationPoint(
                1.0f,
                19.0f - (5.0f - MathHelper.sin(this.rArmUpper.rotateAngleX + 1.5707964f) * 5.0f),
                3.5f - MathHelper.cos(this.rArmUpper.rotateAngleX + 1.5707964f) * 5.0f);
            this.lArmLower.setRotationPoint(
                -1.0f,
                19.0f - (5.0f - MathHelper.sin(this.lArmUpper.rotateAngleX + 1.5707964f) * 5.0f),
                3.5f - MathHelper.cos(this.lArmUpper.rotateAngleX + 1.5707964f) * 5.0f);
            this.lLegLower.setRotationPoint(
                -1.0f,
                19.0f - (5.0f - MathHelper.sin(this.lLegUpper.rotateAngleX + 1.5707964f) * 5.0f),
                -3.5f - MathHelper.cos(this.lLegUpper.rotateAngleX + 1.5707964f) * 5.0f);
            this.rLegLower.rotateAngleX = this.rLegUpper.rotateAngleX;
            this.rArmLower.rotateAngleX = this.rArmUpper.rotateAngleX;
            this.lLegLower.rotateAngleX = this.lLegUpper.rotateAngleX;
            this.lArmLower.rotateAngleX = this.lArmUpper.rotateAngleX;
            this.tailBase.rotateAngleX = MathHelper.cos(f * 0.6662f) * 0.5f * f1;
            this.tailBase.rotateAngleZ = MathHelper.cos(f * 0.6662f) * 0.5f * f1;
            this.tailMid.setRotationPoint(
                0.0f - MathHelper.cos(this.tailBase.rotateAngleZ + 1.5707964f) * 3.0f,
                11.0f + (3.0f - MathHelper.sin(this.tailBase.rotateAngleX + 1.5707964f) * 3.0f),
                3.5f - MathHelper.cos(this.tailBase.rotateAngleX + 1.5707964f) * 3.0f);
            this.tailMid.rotateAngleX = this.tailBase.rotateAngleX + MathHelper.cos(f * 0.6662f) * 0.75f * f1;
            this.tailMid.rotateAngleZ = this.tailBase.rotateAngleZ + MathHelper.cos(f * 0.6662f) * 0.75f * f1;
            this.tailTop.setRotationPoint(
                0.0f - MathHelper.cos(this.tailMid.rotateAngleZ + 1.5707964f) * 2.0f,
                9.0f + (2.0f - MathHelper.sin(this.tailMid.rotateAngleX + 1.5707964f) * 2.0f),
                3.5f - MathHelper.cos(this.tailMid.rotateAngleX + 1.5707964f) * 2.0f);
            this.tailTop.rotateAngleX = this.tailMid.rotateAngleX + MathHelper.cos(f * 0.6662f) * 1.75f * f1;
            this.tailTop.rotateAngleZ = this.tailMid.rotateAngleX + MathHelper.cos(f * 0.6662f) * 1.75f * f1;
            this.herps = 0.0f;
        }
    }
}
