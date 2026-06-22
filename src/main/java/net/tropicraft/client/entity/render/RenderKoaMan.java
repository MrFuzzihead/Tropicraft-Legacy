package net.tropicraft.client.entity.render;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.tropicraft.client.entity.model.ModelKoaMan;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.entity.koa.EntityKoaShaman;
import net.tropicraft.entity.koa.EntityKoaTrader;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

public class RenderKoaMan extends RenderLiving {

    int counter;
    protected ModelKoaMan mainModel;
    protected ModelKoaMan renderPassModel;

    public RenderKoaMan(final ModelKoaMan modelbase, final float f) {
        super((ModelBase) modelbase, f);
        this.mainModel = modelbase;
        this.shadowSize = f;
        this.counter = 300;
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        if (entity instanceof EntityKoaShaman) {
            return TropicraftUtils.bindTextureEntity("koa/KoaShaman");
        }
        if (entity instanceof EntityKoaTrader) {
            return TropicraftUtils.bindTextureEntity("koa/KoaManTrader");
        }
        if (entity instanceof EntityKoaHunter) {
            return TropicraftUtils.bindTextureEntity("koa/KoaManHunter");
        }
        return TropicraftUtils.bindTextureEntity("koa/KoaMan3");
    }

    public void setRenderPassModel(final ModelKoaMan modelbase) {
        this.renderPassModel = modelbase;
    }

    protected void renderLivingAt(final EntityLiving entityliving, final double d, final double d1, final double d2) {
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
    }

    protected void rotateCorpse(final EntityLiving entityliving, final float f, final float f1, final float f2) {
        GL11.glRotatef(180.0f - f1, 0.0f, 1.0f, 0.0f);
        if (entityliving.deathTime > 0) {
            float f3 = (entityliving.deathTime + f2 - 1.0f) / 20.0f * 1.6f;
            f3 = MathHelper.sqrt_float(f3);
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            GL11.glRotatef(f3 * this.getDeathMaxRotation(entityliving), 0.0f, 0.0f, 1.0f);
        }
    }

    protected float renderSwingProgress(final EntityLiving entityliving, final float f) {
        return entityliving.getSwingProgress(f);
    }

    protected float handleRotationFloat(final EntityLiving entityliving, final float f) {
        return entityliving.ticksExisted + f;
    }

    protected int inheritRenderPass(final EntityLiving entityliving, final int i, final float f) {
        return this.shouldRenderPass(entityliving, i, f);
    }

    protected int shouldRenderPass(final EntityLiving entityliving, final int i, final float f) {
        return -1;
    }

    protected float getDeathMaxRotation(final EntityLiving entityliving) {
        return 90.0f;
    }

    protected int getColorMultiplier(final EntityLiving entityliving, final float f, final float f1) {
        return 0;
    }

    protected void preRenderCallback(final EntityLiving entityliving, final float f) {}

    protected void renderLivingLabel(final EntityLiving entityliving, final String s, final double d, final double d1,
        final double d2, final int i) {
        final float f = entityliving.getDistanceToEntity((Entity) this.renderManager.livingPlayer);
        if (f > i) {
            return;
        }
        final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
        final float f2 = 1.6f;
        final float f3 = 0.01666667f * f2;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.0f, (float) d1 + 2.3f, (float) d2);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-f3, -f3, f3);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        final Tessellator tessellator = Tessellator.instance;
        byte byte0 = 0;
        if (s != null && s.equals("deadmau5")) {
            byte0 = -10;
        }
        GL11.glDisable(3553);
        tessellator.startDrawingQuads();
        final int j = fontrenderer.getStringWidth(s) / 2;
        tessellator.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.25f);
        tessellator.addVertex((double) (-j - 1), (double) (-1 + byte0), 0.0);
        tessellator.addVertex((double) (-j - 1), (double) (8 + byte0), 0.0);
        tessellator.addVertex((double) (j + 1), (double) (8 + byte0), 0.0);
        tessellator.addVertex((double) (j + 1), (double) (-1 + byte0), 0.0);
        tessellator.draw();
        GL11.glEnable(3553);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, (int) byte0, 553648127);
        GL11.glDepthMask(true);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, (int) byte0, -1);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    protected void passSpecialRender(final EntityLiving entityliving, final double d, final double d1,
        final double d2) {
        if (entityliving instanceof EntityLiving
            && this.renderManager.livingPlayer.getDistanceToEntity((Entity) entityliving) < 12.0f) {
            this.renderName(entityliving, d, d1, d2);
        }
    }

    protected void renderName(final EntityLiving entitykoa, final double d, final double d1, final double d2) {
        if (Minecraft.isGuiEnabled() && entitykoa != this.renderManager.livingPlayer) {
            final float f = 1.6f;
            final float f2 = 0.01666667f * f;
            final float f3 = entitykoa.getDistanceToEntity((Entity) this.renderManager.livingPlayer);
            final float f4 = entitykoa.isSneaking() ? 32.0f : 64.0f;
            final String s = entitykoa.getCustomNameTag();
            if (f3 < f4) {
                if (!entitykoa.isSneaking()) {
                    if (entitykoa.isPlayerSleeping()) {
                        this.renderLivingLabel(entitykoa, s, d, d1 - 1.5, d2, 24);
                    }
                    this.renderLivingLabel(entitykoa, s, d, d1, d2, 24);
                } else {
                    final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) d + 0.0f, (float) d1 + 2.3f, (float) d2);
                    GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                    GL11.glRotatef(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GL11.glScalef(-f2, -f2, f2);
                    GL11.glDisable(2896);
                    GL11.glTranslatef(0.0f, 0.25f / f2, 0.0f);
                    GL11.glDepthMask(false);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    final Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(3553);
                    tessellator.startDrawingQuads();
                    final int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.25f);
                    tessellator.addVertex((double) (-i - 1), -1.0, 0.0);
                    tessellator.addVertex((double) (-i - 1), 8.0, 0.0);
                    tessellator.addVertex((double) (i + 1), 8.0, 0.0);
                    tessellator.addVertex((double) (i + 1), -1.0, 0.0);
                    tessellator.draw();
                    GL11.glEnable(3553);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GL11.glEnable(2896);
                    GL11.glDisable(3042);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    private void renderShadowOnBlock(final Block block, final double d, final double d1, final double d2, final int i,
        final int j, final int k, final float f, final float f1, final double d3, final double d4, final double d5) {
        final Tessellator tessellator = Tessellator.instance;
        if (!block.renderAsNormalBlock()) {
            return;
        }
        double d6 = (f - (d1 - (j + d4)) / 2.0) * 0.5
            * this.getWorldFromRenderManager()
                .getLightBrightness(i, j, k);
        if (d6 < 0.0) {
            return;
        }
        if (d6 > 1.0) {
            d6 = 1.0;
        }
        tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, (float) d6);
        final double d7 = i + block.getBlockBoundsMinX() + d3;
        final double d8 = i + block.getBlockBoundsMaxX() + d3;
        final double d9 = j + block.getBlockBoundsMinY() + d4 + 0.015625;
        final double d10 = k + block.getBlockBoundsMinZ() + d5;
        final double d11 = k + block.getBlockBoundsMaxZ() + d5;
        final float f2 = (float) ((d - d7) / 2.0 / f1 + 0.5);
        final float f3 = (float) ((d - d8) / 2.0 / f1 + 0.5);
        final float f4 = (float) ((d2 - d10) / 2.0 / f1 + 0.5);
        final float f5 = (float) ((d2 - d11) / 2.0 / f1 + 0.5);
        tessellator.addVertexWithUV(d7, d9, d10, (double) f2, (double) f4);
        tessellator.addVertexWithUV(d7, d9, d11, (double) f2, (double) f5);
        tessellator.addVertexWithUV(d8, d9, d11, (double) f3, (double) f5);
        tessellator.addVertexWithUV(d8, d9, d10, (double) f3, (double) f4);
    }

    protected void renderEquippedItems(final EntityLivingBase p_77029_1_, final float p_77029_2_) {
        super.renderEquippedItems(p_77029_1_, p_77029_2_);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        super.renderEquippedItems(p_77029_1_, p_77029_2_);
        final ItemStack itemstack = p_77029_1_.getHeldItem();
        final ItemStack itemstack2 = ((EntityLiving) p_77029_1_).func_130225_q(3);
        if (itemstack2 != null) {
            GL11.glPushMatrix();
            this.mainModel.bipedHead.postRender(0.0625f);
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
                this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack2, 0);
            } else if (item == Items.skull) {
                final float f1 = 1.0625f;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;
                if (itemstack2.hasTagCompound()) {
                    final NBTTagCompound nbttagcompound = itemstack2.getTagCompound();
                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    } else if (nbttagcompound.hasKey("SkullOwner", 8)
                        && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
                            gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
                        }
                }
                TileEntitySkullRenderer.field_147536_b
                    .func_152674_a(-0.5f, 0.0f, -0.5f, 1, 180.0f, itemstack2.getItemDamage(), gameprofile);
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
            this.mainModel.bipedRightArm.postRender(0.0625f);
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
                GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
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
                    this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, i);
                }
            } else {
                final int i = itemstack.getItem()
                    .getColorFromItemStack(itemstack, 0);
                final float f5 = (i >> 16 & 0xFF) / 255.0f;
                final float f2 = (i >> 8 & 0xFF) / 255.0f;
                final float f3 = (i & 0xFF) / 255.0f;
                GL11.glColor4f(f5, f2, f3, 1.0f);
                this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
            }
            GL11.glPopMatrix();
        }
    }

    protected void func_82422_c() {
        GL11.glTranslatef(0.0f, 0.1875f, 0.0f);
    }
}
