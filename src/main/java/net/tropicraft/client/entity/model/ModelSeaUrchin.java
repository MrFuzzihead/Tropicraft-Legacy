package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelSeaUrchin extends ModelBase {

    public ModelRenderer base;
    public ModelRenderer top1;
    public ModelRenderer top2;
    public ModelRenderer front1;
    public ModelRenderer front2;
    public ModelRenderer left1;
    public ModelRenderer left2;
    public ModelRenderer back1;
    public ModelRenderer back2;
    public ModelRenderer right1;
    public ModelRenderer right2;
    public ModelRenderer bottom1;
    public ModelRenderer bottom2;
    public ModelRenderer spine;

    public ModelSeaUrchin() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.base = new ModelRenderer((ModelBase) this, 0, 0)).addBox(-3.0f, 16.0f, -3.0f, 6, 6, 6);
        this.base.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.base.setTextureSize(64, 64);
        this.base.mirror = true;
        this.setRotation(this.base, 0.0f, 0.0f, 0.0f);
        (this.top1 = new ModelRenderer((ModelBase) this, 0, 38)).addBox(-2.0f, 15.0f, -2.0f, 4, 1, 4);
        this.top1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.top1.setTextureSize(64, 64);
        this.top1.mirror = true;
        this.setRotation(this.top1, 0.0f, 0.0f, 0.0f);
        (this.top2 = new ModelRenderer((ModelBase) this, 16, 38)).addBox(-1.0f, 14.0f, -1.0f, 2, 1, 2);
        this.top2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.top2.setTextureSize(64, 64);
        this.top2.mirror = true;
        this.setRotation(this.top2, 0.0f, 0.0f, 0.0f);
        (this.front1 = new ModelRenderer((ModelBase) this, 0, 12)).addBox(-2.0f, 17.0f, -4.0f, 4, 4, 1);
        this.front1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.front1.setTextureSize(64, 64);
        this.front1.mirror = true;
        this.setRotation(this.front1, 0.0f, 0.0f, 0.0f);
        (this.front2 = new ModelRenderer((ModelBase) this, 10, 12)).addBox(-1.0f, 18.0f, -5.0f, 2, 2, 1);
        this.front2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.front2.setTextureSize(64, 64);
        this.front2.mirror = true;
        this.setRotation(this.front2, 0.0f, 0.0f, 0.0f);
        (this.left1 = new ModelRenderer((ModelBase) this, 0, 17)).addBox(3.0f, 17.0f, -2.0f, 1, 4, 4);
        this.left1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.left1.setTextureSize(64, 64);
        this.left1.mirror = true;
        this.setRotation(this.left1, 0.0f, 0.0f, 0.0f);
        (this.left2 = new ModelRenderer((ModelBase) this, 10, 17)).addBox(4.0f, 18.0f, -1.0f, 1, 2, 2);
        this.left2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.left2.setTextureSize(64, 64);
        this.left2.mirror = true;
        this.setRotation(this.left2, 0.0f, 0.0f, 0.0f);
        (this.back1 = new ModelRenderer((ModelBase) this, 0, 25)).addBox(-2.0f, 17.0f, 3.0f, 4, 4, 1);
        this.back1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.back1.setTextureSize(64, 64);
        this.back1.mirror = true;
        this.setRotation(this.back1, 0.0f, 0.0f, 0.0f);
        (this.back2 = new ModelRenderer((ModelBase) this, 10, 25)).addBox(-1.0f, 18.0f, 4.0f, 2, 2, 1);
        this.back2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.back2.setTextureSize(64, 64);
        this.back2.mirror = true;
        this.setRotation(this.back2, 0.0f, 0.0f, 0.0f);
        (this.right1 = new ModelRenderer((ModelBase) this, 0, 30)).addBox(-4.0f, 17.0f, -2.0f, 1, 4, 4);
        this.right1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.right1.setTextureSize(64, 64);
        this.right1.mirror = true;
        this.setRotation(this.right1, 0.0f, 0.0f, 0.0f);
        (this.right2 = new ModelRenderer((ModelBase) this, 10, 30)).addBox(-5.0f, 18.0f, -1.0f, 1, 2, 2);
        this.right2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.right2.setTextureSize(64, 64);
        this.right2.mirror = true;
        this.setRotation(this.right2, 0.0f, 0.0f, 0.0f);
        (this.bottom1 = new ModelRenderer((ModelBase) this, 0, 38)).addBox(-2.0f, 22.0f, -2.0f, 4, 1, 4);
        this.bottom1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.bottom1.setTextureSize(64, 64);
        this.bottom1.mirror = true;
        this.setRotation(this.bottom1, 0.0f, 0.0f, 0.0f);
        (this.bottom2 = new ModelRenderer((ModelBase) this, 16, 38)).addBox(-1.0f, 23.0f, -1.0f, 2, 1, 2);
        this.bottom2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.bottom2.setTextureSize(64, 64);
        this.bottom2.mirror = true;
        this.setRotation(this.bottom2, 0.0f, 0.0f, 0.0f);
        (this.spine = new ModelRenderer((ModelBase) this, 24, 0)).addBox(-0.5f, -9.0f, -0.5f, 1, 6, 1);
        this.spine.setRotationPoint(0.0f, 19.0f, 0.0f);
        this.spine.setTextureSize(64, 64);
        this.spine.mirror = true;
        this.setRotation(this.spine, 0.0f, 0.0f, 0.0f);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.base.render(f5);
        this.top1.render(f5);
        this.top2.render(f5);
        this.front1.render(f5);
        this.front2.render(f5);
        this.left1.render(f5);
        this.left2.render(f5);
        this.back1.render(f5);
        this.back2.render(f5);
        this.right1.render(f5);
        this.right2.render(f5);
        this.bottom1.render(f5);
        this.bottom2.render(f5);
        final int verticalSpines = 12;
        final int horizontalSpines = 12;
        for (int v = 0; v < verticalSpines; ++v) {
            for (int h = 0; h < horizontalSpines; ++h) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0f, 1.25f, 0.0f);
                GL11.glRotatef(360.0f * v / verticalSpines, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(360.0f * h / horizontalSpines, 1.0f, 0.0f, 0.0f);
                GL11.glTranslatef(0.0f, -0.4f, 0.0f);
                GL11.glScalef(0.33f, 1.0f, 0.33f);
                this.spine.render(f5);
                GL11.glPopMatrix();
            }
        }
    }

    private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
