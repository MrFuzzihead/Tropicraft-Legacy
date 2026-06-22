package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelIguana extends ModelBase {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer fLleg;
    public ModelRenderer rLleg;
    public ModelRenderer fRleg;
    public ModelRenderer rRlet;
    public ModelRenderer Back1;
    public ModelRenderer Back2;
    public ModelRenderer headTop2;
    public ModelRenderer headTop1;
    public ModelRenderer jaw;
    public ModelRenderer Back3;
    public ModelRenderer dewLap;
    public ModelRenderer tailBase;
    public ModelRenderer tailMid;
    public ModelRenderer miscPart;

    public ModelIguana() {
        (this.head = new ModelRenderer((ModelBase) this, 36, 23)).addBox(-2.5f, -2.0f, -6.0f, 5, 3, 6);
        this.head.setRotationPoint(0.0f, 20.0f, -6.0f);
        this.head.rotateAngleX = 0.0f;
        this.head.rotateAngleY = 0.0f;
        this.head.rotateAngleZ = 0.0f;
        this.head.mirror = false;
        (this.body = new ModelRenderer((ModelBase) this, 0, 16)).addBox(-2.5f, -1.5f, -7.5f, 5, 3, 13);
        this.body.setRotationPoint(0.0f, 21.5f, 1.0f);
        this.body.rotateAngleX = 0.0f;
        this.body.rotateAngleY = 0.0f;
        this.body.rotateAngleZ = 0.0f;
        this.body.mirror = false;
        (this.fLleg = new ModelRenderer((ModelBase) this, 24, 21)).addBox(0.0f, 0.0f, -1.5f, 2, 3, 3);
        this.fLleg.setRotationPoint(2.5f, 21.0f, -4.0f);
        this.fLleg.rotateAngleX = 0.0f;
        this.fLleg.rotateAngleY = 0.0f;
        this.fLleg.rotateAngleZ = 0.0f;
        this.fLleg.mirror = false;
        (this.rLleg = new ModelRenderer((ModelBase) this, 24, 21)).addBox(0.0f, 0.0f, -1.5f, 2, 3, 3);
        this.rLleg.setRotationPoint(2.5f, 21.0f, 4.0f);
        this.rLleg.rotateAngleX = 0.0f;
        this.rLleg.rotateAngleY = 0.0f;
        this.rLleg.rotateAngleZ = 0.0f;
        this.rLleg.mirror = false;
        (this.fRleg = new ModelRenderer((ModelBase) this, 0, 21)).addBox(-2.0f, 0.0f, -1.5f, 2, 3, 3);
        this.fRleg.setRotationPoint(-2.5f, 21.0f, -4.0f);
        this.fRleg.rotateAngleX = 0.0f;
        this.fRleg.rotateAngleY = 0.0f;
        this.fRleg.rotateAngleZ = 0.0f;
        this.fRleg.mirror = false;
        (this.rRlet = new ModelRenderer((ModelBase) this, 0, 21)).addBox(-2.0f, 0.0f, -1.5f, 2, 3, 3);
        this.rRlet.setRotationPoint(-2.5f, 21.0f, 4.0f);
        this.rRlet.rotateAngleX = 0.0f;
        this.rRlet.rotateAngleY = 0.0f;
        this.rRlet.rotateAngleZ = 0.0f;
        this.rRlet.mirror = false;
        (this.Back1 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-1.5f, -1.0f, 0.0f, 3, 1, 10);
        this.Back1.setRotationPoint(0.0f, 20.0f, -5.0f);
        this.Back1.rotateAngleX = 0.0f;
        this.Back1.rotateAngleY = 0.0f;
        this.Back1.rotateAngleZ = 0.0f;
        this.Back1.mirror = false;
        (this.Back2 = new ModelRenderer((ModelBase) this, 32, 0)).addBox(-0.5f, -1.0f, -3.0f, 1, 1, 6);
        this.Back2.setRotationPoint(0.0f, 19.0f, 0.0f);
        this.Back2.rotateAngleX = 0.0f;
        this.Back2.rotateAngleY = 0.0f;
        this.Back2.rotateAngleZ = 0.0f;
        this.Back2.mirror = false;
        (this.headTop2 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-0.5f, -4.0f, -4.0f, 1, 1, 2);
        this.headTop2.setRotationPoint(0.0f, 20.0f, -6.0f);
        this.headTop2.rotateAngleX = 0.0f;
        this.headTop2.rotateAngleY = 0.0f;
        this.headTop2.rotateAngleZ = 0.0f;
        this.headTop2.mirror = false;
        (this.headTop1 = new ModelRenderer((ModelBase) this, 32, 7)).addBox(-0.5f, -3.0f, -5.0f, 1, 1, 4);
        this.headTop1.setRotationPoint(0.0f, 20.0f, -6.0f);
        this.headTop1.rotateAngleX = 0.0f;
        this.headTop1.rotateAngleY = 0.0f;
        this.headTop1.rotateAngleZ = 0.0f;
        this.headTop1.mirror = false;
        (this.jaw = new ModelRenderer((ModelBase) this, 0, 11)).addBox(-1.0f, 1.0f, -4.0f, 2, 1, 4);
        this.jaw.setRotationPoint(0.0f, 20.0f, -6.0f);
        this.jaw.rotateAngleX = 0.0f;
        this.jaw.rotateAngleY = 0.0f;
        this.jaw.rotateAngleZ = 0.0f;
        this.jaw.mirror = false;
        (this.Back3 = new ModelRenderer((ModelBase) this, 32, 7)).addBox(-0.5f, 0.0f, -2.0f, 1, 1, 4);
        this.Back3.setRotationPoint(0.0f, 17.0f, 0.0f);
        this.Back3.rotateAngleX = 0.0f;
        this.Back3.rotateAngleY = 0.0f;
        this.Back3.rotateAngleZ = 0.0f;
        this.Back3.mirror = false;
        (this.dewLap = new ModelRenderer((ModelBase) this, 0, 4)).addBox(-0.5f, 2.0f, -3.0f, 1, 1, 3);
        this.dewLap.setRotationPoint(0.0f, 20.0f, -6.0f);
        this.dewLap.rotateAngleX = 0.0f;
        this.dewLap.rotateAngleY = 0.0f;
        this.dewLap.rotateAngleZ = 0.0f;
        this.dewLap.mirror = false;
        (this.tailBase = new ModelRenderer((ModelBase) this, 46, 0)).addBox(-1.5f, -0.5f, 0.0f, 3, 1, 6);
        this.tailBase.setRotationPoint(0.0f, 21.5f, 6.0f);
        this.tailBase.rotateAngleX = 0.0f;
        this.tailBase.rotateAngleY = 0.0f;
        this.tailBase.rotateAngleZ = 0.0f;
        this.tailBase.mirror = false;
        (this.tailMid = new ModelRenderer((ModelBase) this, 48, 7)).addBox(-1.0f, -0.5f, 0.0f, 2, 1, 6);
        (this.miscPart = new ModelRenderer((ModelBase) this, 52, 14)).addBox(-0.5f, -0.5f, 0.0f, 1, 1, 5);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, null);
        this.head.render(f5);
        this.body.render(f5);
        this.fLleg.render(f5);
        this.rLleg.render(f5);
        this.fRleg.render(f5);
        this.rRlet.render(f5);
        this.Back1.render(f5);
        this.Back2.render(f5);
        this.headTop2.render(f5);
        this.headTop1.render(f5);
        this.jaw.render(f5);
        this.Back3.render(f5);
        this.dewLap.render(f5);
        this.tailBase.render(f5);
        this.tailMid.render(f5);
        this.miscPart.render(f5);
    }

    public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
        final float f2) {
        this.fRleg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.75f * f1;
        this.fLleg.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.75f * f1;
        this.rRlet.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.75f * f1;
        this.rLleg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.75f * f1;
        this.tailBase.rotateAngleY = MathHelper.cos(f * 0.6662f) * 0.25f * f1;
        this.tailMid.setRotationPoint(
            0.0f - MathHelper.cos(this.tailBase.rotateAngleY + 1.570796f) * 6.0f,
            21.5f,
            12.0f + MathHelper.sin(this.tailBase.rotateAngleX + 3.14159f) * 6.0f);
        this.tailMid.rotateAngleY = this.tailBase.rotateAngleY + MathHelper.cos(f * 0.6662f) * 0.5f * f1;
        this.miscPart.setRotationPoint(
            0.0f - MathHelper.cos(this.tailMid.rotateAngleY + 1.570796f) * 6.0f,
            21.5f,
            18.0f + MathHelper.sin(this.tailMid.rotateAngleX + 3.14159f) * 6.0f);
        this.miscPart.rotateAngleY = this.tailMid.rotateAngleY + MathHelper.cos(f * 0.6662f) * 0.75f * f1;
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        this.head.rotateAngleX = f4 / 57.29578f;
        this.head.rotateAngleY = f3 / 57.29578f;
        this.jaw.rotateAngleX = this.head.rotateAngleX;
        this.jaw.rotateAngleY = this.head.rotateAngleY;
        this.headTop2.rotateAngleX = this.head.rotateAngleX;
        this.headTop2.rotateAngleY = this.head.rotateAngleY;
        this.headTop1.rotateAngleX = this.head.rotateAngleX;
        this.headTop1.rotateAngleY = this.head.rotateAngleY;
        this.dewLap.rotateAngleX = this.head.rotateAngleX;
        this.dewLap.rotateAngleY = this.head.rotateAngleY;
    }
}
