package net.tropicraft.client.entity.render;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.tropicraft.client.entity.model.ModelTropiSkeleton;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderTropiSkeleton extends RenderBiped {

    public RenderTropiSkeleton(final float par1) {
        super((ModelBiped) new ModelTropiSkeleton(), par1);
    }

    protected void renderEquippedItems(final EntityLiving par1EntityLiving, final float par2) {
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        super.renderEquippedItems(par1EntityLiving, par2);
        final ItemStack itemstack = par1EntityLiving.getHeldItem();
        final ItemStack itemstack2 = par1EntityLiving.func_130225_q(3);
        if (itemstack2 != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625f);
            final Item item = itemstack2.getItem();
            final IItemRenderer customRenderer = MinecraftForgeClient
                .getItemRenderer(itemstack2, IItemRenderer.ItemRenderType.EQUIPPED);
            final boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
                IItemRenderer.ItemRenderType.EQUIPPED,
                itemstack2,
                IItemRenderer.ItemRendererHelper.BLOCK_3D);
            if (item instanceof ItemBlock) {
                if (is3D || RenderBlocks.renderItemIn3d(
                    Block.getBlockFromItem(item)
                        .getRenderType())) {
                    final float f1 = 0.625f;
                    GL11.glTranslatef(0.0f, -0.25f, 0.0f);
                    GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                    GL11.glScalef(f1, -f1, -f1);
                }
                this.renderManager.itemRenderer.renderItem((EntityLivingBase) par1EntityLiving, itemstack2, 0);
            }
            GL11.glPopMatrix();
        }
        if (itemstack != null && itemstack.getItem() != null) {
            final Item item = itemstack.getItem();
            GL11.glPushMatrix();
            if (this.mainModel.isChild) {
                final float f1 = 0.5f;
                GL11.glTranslatef(0.0f, 0.625f, 0.0f);
                GL11.glRotatef(-20.0f, -1.0f, 0.0f, 0.0f);
                GL11.glScalef(f1, f1, f1);
            }
            this.modelBipedMain.bipedRightArm.postRender(0.0625f);
            GL11.glTranslatef(-0.0625f, 0.4375f, 0.0625f);
            final IItemRenderer customRenderer = MinecraftForgeClient
                .getItemRenderer(itemstack, IItemRenderer.ItemRenderType.EQUIPPED);
            final boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
                IItemRenderer.ItemRenderType.EQUIPPED,
                itemstack,
                IItemRenderer.ItemRendererHelper.BLOCK_3D);
            if (item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(
                Block.getBlockFromItem(item)
                    .getRenderType()))) {
                float f1 = 0.5f;
                GL11.glTranslatef(0.0f, 0.1875f, -0.3125f);
                f1 *= 0.75f;
                GL11.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-f1, -f1, f1);
            } else if (item == Items.bow) {
                final float f1 = 0.625f;
                GL11.glTranslatef(0.0f, 0.125f, 0.3125f);
                GL11.glRotatef(-20.0f, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            } else if (item.isFull3D()) {
                final float f1 = 0.625f;
                if (item.shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef(0.0f, -0.125f, 0.0f);
                }
                this.func_82422_c();
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(200.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            } else {
                final float f1 = 0.375f;
                GL11.glTranslatef(0.25f, 0.1875f, -0.1875f);
                GL11.glScalef(f1, f1, f1);
                GL11.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(20.0f, 0.0f, 0.0f, 1.0f);
            }
            if (itemstack.getItem()
                .requiresMultipleRenderPasses()) {
                for (int i = 0; i < itemstack.getItem()
                    .getRenderPasses(itemstack.getItemDamage()); ++i) {
                    final int j = itemstack.getItem()
                        .getColorFromItemStack(itemstack, i);
                    final float f2 = (j >> 16 & 0xFF) / 255.0f;
                    final float f3 = (j >> 8 & 0xFF) / 255.0f;
                    final float f4 = (j & 0xFF) / 255.0f;
                    GL11.glColor4f(f2, f3, f4, 1.0f);
                    this.renderManager.itemRenderer.renderItem((EntityLivingBase) par1EntityLiving, itemstack, i);
                }
            } else {
                final int i = itemstack.getItem()
                    .getColorFromItemStack(itemstack, 0);
                final float f5 = (i >> 16 & 0xFF) / 255.0f;
                final float f2 = (i >> 8 & 0xFF) / 255.0f;
                final float f3 = (i & 0xFF) / 255.0f;
                GL11.glColor4f(f5, f2, f3, 1.0f);
                this.renderManager.itemRenderer.renderItem((EntityLivingBase) par1EntityLiving, itemstack, 0);
            }
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TropicraftUtils.bindTextureEntity("skeleton");
    }
}
