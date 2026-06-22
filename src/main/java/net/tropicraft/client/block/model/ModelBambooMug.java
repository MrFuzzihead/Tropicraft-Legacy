package net.tropicraft.client.block.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import org.lwjgl.opengl.GL11;

public class ModelBambooMug extends ModelBase {

    public ModelRenderer base;
    public ModelRenderer wall1;
    public ModelRenderer wall2;
    public ModelRenderer wall3;
    public ModelRenderer wall4;
    public ModelRenderer liquid;
    public ModelRenderer handletop;
    public ModelRenderer handlebottom;
    public ModelRenderer handle;
    public boolean renderLiquid;
    public int liquidColor;

    public ModelBambooMug() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        (this.base = new ModelRenderer((ModelBase) this, 10, 0)).addBox(-2.0f, 23.0f, -2.0f, 4, 1, 4);
        this.base.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.base.setTextureSize(64, 32);
        this.base.mirror = true;
        this.setRotation(this.base, 0.0f, 0.0f, 0.0f);
        this.wall1 = new ModelRenderer((ModelBase) this, 0, 10);
        this.wall1.mirror = true;
        this.wall1.addBox(-2.0f, 17.0f, -3.0f, 4, 6, 1);
        this.wall1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.wall1.setTextureSize(64, 32);
        this.setRotation(this.wall1, 0.0f, 0.0f, 0.0f);
        (this.wall2 = new ModelRenderer((ModelBase) this, 0, 10)).addBox(-2.0f, 17.0f, 2.0f, 4, 6, 1);
        this.wall2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.wall2.setTextureSize(64, 32);
        this.wall2.mirror = true;
        this.setRotation(this.wall2, 0.0f, 0.0f, 0.0f);
        (this.wall3 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(2.0f, 17.0f, -2.0f, 1, 6, 4);
        this.wall3.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.wall3.setTextureSize(64, 32);
        this.setRotation(this.wall3, 0.0f, 0.0f, 0.0f);
        this.wall3.mirror = true;
        (this.wall4 = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-3.0f, 17.0f, -2.0f, 1, 6, 4);
        this.wall4.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.wall4.setTextureSize(64, 32);
        this.wall4.mirror = true;
        this.setRotation(this.wall4, 0.0f, 0.0f, 0.0f);
        (this.liquid = new ModelRenderer((ModelBase) this, 10, 5)).addBox(-2.0f, 18.0f, -2.0f, 4, 1, 4);
        this.liquid.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.liquid.setTextureSize(64, 32);
        this.liquid.mirror = true;
        this.setRotation(this.liquid, 0.0f, 0.0f, 0.0f);
        (this.handletop = new ModelRenderer((ModelBase) this, 26, 0)).addBox(-1.0f, 18.0f, -4.0f, 2, 1, 1);
        this.handletop.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.handletop.setTextureSize(64, 32);
        this.handletop.mirror = true;
        this.setRotation(this.handletop, 0.0f, 0.0f, 0.0f);
        (this.handlebottom = new ModelRenderer((ModelBase) this, 26, 2)).addBox(-1.0f, 21.0f, -4.0f, 2, 1, 1);
        this.handlebottom.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.handlebottom.setTextureSize(64, 32);
        this.handlebottom.mirror = true;
        this.setRotation(this.handlebottom, 0.0f, 0.0f, 0.0f);
        (this.handle = new ModelRenderer((ModelBase) this, 32, 0)).addBox(-1.0f, 19.0f, -5.0f, 2, 2, 1);
        this.handle.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.handle.setTextureSize(64, 32);
        this.handle.mirror = true;
        this.setRotation(this.handle, 0.0f, 0.0f, 0.0f);
    }

    public void renderBambooMug() {
        final float f5 = 0.0625f;
        this.base.render(f5);
        this.wall1.render(f5);
        this.wall2.render(f5);
        this.wall3.render(f5);
        this.wall4.render(f5);
        this.handletop.render(f5);
        this.handlebottom.render(f5);
        this.handle.render(f5);
        if (this.renderLiquid) {
            final float red = (this.liquidColor >> 16 & 0xFF) / 255.0f;
            final float green = (this.liquidColor >> 8 & 0xFF) / 255.0f;
            final float blue = (this.liquidColor & 0xFF) / 255.0f;
            GL11.glDisable(3553);
            GL11.glColor3f(red, green, blue);
            this.liquid.render(f5);
            GL11.glEnable(3553);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
        }
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
