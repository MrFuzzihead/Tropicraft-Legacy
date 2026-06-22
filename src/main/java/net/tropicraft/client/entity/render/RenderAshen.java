package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelAshen;
import net.tropicraft.entity.hostile.EntityAshen;
import net.tropicraft.entity.hostile.EntityAshenHunter;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderAshen extends RenderLiving {

    private MaskRenderer mask;
    private ModelAshen modelAshen;

    public RenderAshen(final ModelBase modelbase, final float f) {
        super(modelbase, f);
        this.modelAshen = (ModelAshen) modelbase;
        this.mask = new MaskRenderer();
    }

    public void renderAshen(final EntityAshen entityAshen, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.modelAshen.swinging = false;
        this.modelAshen.actionState = entityAshen.getActionState();
        super.doRender((EntityLiving) entityAshen, d, d1, d2, f, f1);
    }

    public void doRender(final EntityLiving entityliving, final double d, final double d1, final double d2,
        final float f, final float f1) {
        this.renderAshen((EntityAshen) entityliving, d, d1, d2, f, f1);
    }

    public void doRender(final Entity entity, final double d, final double d1, final double d2, final float f,
        final float f1) {
        this.renderAshen((EntityAshen) entity, d, d1, d2, f, f1);
    }

    private void renderMask(final EntityLivingBase entityliving) {
        GL11.glPushMatrix();
        this.modelAshen.head.postRender(0.045f);
        TropicraftUtils.bindTextureEntity("ashen/mask");
        GL11.glTranslatef(-0.03125f, 0.40625f + MathHelper.sin(((EntityAshen) entityliving).bobber), 0.18f);
        this.mask.renderMask(((EntityAshen) entityliving).getMaskType());
        GL11.glPopMatrix();
    }

    protected void renderEquippedItems(final EntityLivingBase entityliving, final float f) {
        if (entityliving instanceof EntityAshenHunter && ((EntityAshen) entityliving).getActionState() == 2) {
            GL11.glPushMatrix();
            this.modelAshen.leftArm.postRender(0.0625f);
            GL11.glTranslatef(-0.35f, -0.45f, -0.225f);
            GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            final float scale = 0.3f;
            GL11.glScalef(scale, scale, scale);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.modelAshen.rightArm.postRender(0.0625f);
            GL11.glTranslatef(0.4f, -0.3f, -0.245f);
            GL11.glRotatef(130.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(5.0f, 0.0f, 0.0f, 1.0f);
            GL11.glScalef(scale, scale, scale);
            RenderManager.instance.itemRenderer
                .renderItem(entityliving, new ItemStack((Item) TCItemRegistry.blowGun), 0);
            GL11.glPopMatrix();
        }
        if (((EntityAshen) entityliving).hasMask()) {
            this.renderMask(entityliving);
        }
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("ashen/nativetext");
    }
}
