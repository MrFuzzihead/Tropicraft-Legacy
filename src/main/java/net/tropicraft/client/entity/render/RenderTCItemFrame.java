package net.tropicraft.client.entity.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTCItemFrame extends Render {

    private static final ResourceLocation mapBackgroundTextures;
    private final RenderBlocks field_147916_f;
    private final Minecraft field_147917_g;
    private IIcon field_94147_f;

    public RenderTCItemFrame() {
        this.field_147916_f = new RenderBlocks();
        this.field_147917_g = Minecraft.getMinecraft();
    }

    public void updateIcons(final IIconRegister p_94143_1_) {
        this.field_94147_f = p_94143_1_.registerIcon("itemframe_background");
    }

    public void doRender(final EntityItemFrame p_76986_1_, final double p_76986_2_, final double p_76986_4_,
        final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GL11.glPushMatrix();
        final double d3 = p_76986_1_.posX - p_76986_2_ - 0.5;
        final double d4 = p_76986_1_.posY - p_76986_4_ - 0.5;
        final double d5 = p_76986_1_.posZ - p_76986_6_ - 0.5;
        final int i = p_76986_1_.field_146063_b + Direction.offsetX[p_76986_1_.hangingDirection];
        final int j = p_76986_1_.field_146064_c;
        final int k = p_76986_1_.field_146062_d + Direction.offsetZ[p_76986_1_.hangingDirection];
        GL11.glTranslated(i - d3, j - d4, k - d5);
        if (p_76986_1_.getDisplayedItem() != null && p_76986_1_.getDisplayedItem()
            .getItem() == Items.filled_map) {
            this.func_147915_b(p_76986_1_);
        } else {
            this.renderFrameItemAsBlock(p_76986_1_);
        }
        this.func_82402_b(p_76986_1_);
        GL11.glPopMatrix();
        this.func_147914_a(
            p_76986_1_,
            p_76986_2_ + Direction.offsetX[p_76986_1_.hangingDirection] * 0.3f,
            p_76986_4_ - 0.25,
            p_76986_6_ + Direction.offsetZ[p_76986_1_.hangingDirection] * 0.3f);
    }

    protected ResourceLocation getEntityTexture(final EntityItemFrame p_110775_1_) {
        return null;
    }

    private void func_147915_b(final EntityItemFrame p_147915_1_) {
        GL11.glPushMatrix();
        GL11.glRotatef(p_147915_1_.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        final Block block = Blocks.planks;
        final float f = 0.0625f;
        final float f2 = 1.0f;
        final float f3 = f2 / 2.0f;
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3 + 0.0625f),
            (double) (0.5f - f3 + 0.0625f),
            (double) f,
            (double) (0.5f + f3 - 0.0625f),
            (double) (0.5f + f3 - 0.0625f));
        this.field_147916_f.setOverrideBlockTexture(this.field_94147_f);
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        this.field_147916_f.clearOverrideBlockTexture();
        this.field_147916_f.unlockBlockBounds();
        GL11.glPopMatrix();
        this.field_147916_f.setOverrideBlockTexture(Blocks.planks.getIcon(1, 2));
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3),
            (double) (0.5f - f3),
            (double) (f + 1.0E-4f),
            (double) (f + 0.5f - f3),
            (double) (0.5f + f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f + f3 - f),
            (double) (0.5f - f3),
            (double) (f + 1.0E-4f),
            (double) (0.5f + f3),
            (double) (0.5f + f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3),
            (double) (0.5f - f3),
            (double) f,
            (double) (0.5f + f3),
            (double) (f + 0.5f - f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3),
            (double) (0.5f + f3 - f),
            (double) f,
            (double) (0.5f + f3),
            (double) (0.5f + f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        this.field_147916_f.unlockBlockBounds();
        this.field_147916_f.clearOverrideBlockTexture();
        GL11.glPopMatrix();
    }

    private void renderFrameItemAsBlock(final EntityItemFrame p_82403_1_) {
        GL11.glPushMatrix();
        GL11.glRotatef(p_82403_1_.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        final Block block = Blocks.planks;
        final float f = 0.0625f;
        final float f2 = 0.75f;
        final float f3 = f2 / 2.0f;
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3 + 0.0625f),
            (double) (0.5f - f3 + 0.0625f),
            (double) (f * 0.5f),
            (double) (0.5f + f3 - 0.0625f),
            (double) (0.5f + f3 - 0.0625f));
        this.field_147916_f.setOverrideBlockTexture(this.field_94147_f);
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        this.field_147916_f.clearOverrideBlockTexture();
        this.field_147916_f.unlockBlockBounds();
        GL11.glPopMatrix();
        this.field_147916_f.setOverrideBlockTexture(Blocks.planks.getIcon(1, 2));
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3),
            (double) (0.5f - f3),
            (double) (f + 1.0E-4f),
            (double) (f + 0.5f - f3),
            (double) (0.5f + f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f + f3 - f),
            (double) (0.5f - f3),
            (double) (f + 1.0E-4f),
            (double) (0.5f + f3),
            (double) (0.5f + f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3),
            (double) (0.5f - f3),
            (double) f,
            (double) (0.5f + f3),
            (double) (f + 0.5f - f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.field_147916_f.overrideBlockBounds(
            0.0,
            (double) (0.5f - f3),
            (double) (0.5f + f3 - f),
            (double) f,
            (double) (0.5f + f3),
            (double) (0.5f + f3));
        this.field_147916_f.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
        this.field_147916_f.unlockBlockBounds();
        this.field_147916_f.clearOverrideBlockTexture();
        GL11.glPopMatrix();
    }

    private void func_82402_b(final EntityItemFrame p_82402_1_) {
        final ItemStack itemstack = p_82402_1_.getDisplayedItem();
        if (itemstack != null) {
            final EntityItem entityitem = new EntityItem(p_82402_1_.worldObj, 0.0, 0.0, 0.0, itemstack);
            final Item item = entityitem.getEntityItem()
                .getItem();
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0f;
            GL11.glPushMatrix();
            GL11.glTranslatef(
                -0.453125f * Direction.offsetX[p_82402_1_.hangingDirection],
                -0.18f,
                -0.453125f * Direction.offsetZ[p_82402_1_.hangingDirection]);
            GL11.glRotatef(180.0f + p_82402_1_.rotationYaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef((float) (-90 * p_82402_1_.getRotation()), 0.0f, 0.0f, 1.0f);
            switch (p_82402_1_.getRotation()) {
                case 1: {
                    GL11.glTranslatef(-0.16f, -0.16f, 0.0f);
                    break;
                }
                case 2: {
                    GL11.glTranslatef(0.0f, -0.32f, 0.0f);
                    break;
                }
                case 3: {
                    GL11.glTranslatef(0.16f, -0.16f, 0.0f);
                    break;
                }
            }
            if (item == Items.filled_map) {
                this.renderManager.renderEngine.bindTexture(RenderTCItemFrame.mapBackgroundTextures);
                final Tessellator tessellator = Tessellator.instance;
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                final float f = 0.0078125f;
                GL11.glScalef(f, f, f);
                switch (p_82402_1_.getRotation()) {
                    case 0: {
                        GL11.glTranslatef(-64.0f, -87.0f, -1.5f);
                        break;
                    }
                    case 1: {
                        GL11.glTranslatef(-66.5f, -84.5f, -1.5f);
                        break;
                    }
                    case 2: {
                        GL11.glTranslatef(-64.0f, -82.0f, -1.5f);
                        break;
                    }
                    case 3: {
                        GL11.glTranslatef(-61.5f, -84.5f, -1.5f);
                        break;
                    }
                }
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
                final MapData mapdata = Items.filled_map.getMapData(entityitem.getEntityItem(), p_82402_1_.worldObj);
                GL11.glTranslatef(0.0f, 0.0f, -1.0f);
                if (mapdata != null) {
                    this.field_147917_g.entityRenderer.getMapItemRenderer()
                        .func_148250_a(mapdata, true);
                }
            } else {
                if (item == Items.compass) {
                    final TextureManager texturemanager = Minecraft.getMinecraft()
                        .getTextureManager();
                    texturemanager.bindTexture(TextureMap.locationItemsTexture);
                    final TextureAtlasSprite textureatlassprite1 = ((TextureMap) texturemanager
                        .getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(
                            Items.compass.getIconIndex(entityitem.getEntityItem())
                                .getIconName());
                    if (textureatlassprite1 instanceof TextureCompass) {
                        final TextureCompass texturecompass = (TextureCompass) textureatlassprite1;
                        final double d0 = texturecompass.currentAngle;
                        final double d2 = texturecompass.angleDelta;
                        texturecompass.currentAngle = 0.0;
                        texturecompass.angleDelta = 0.0;
                        texturecompass.updateCompass(
                            p_82402_1_.worldObj,
                            p_82402_1_.posX,
                            p_82402_1_.posZ,
                            (double) MathHelper.wrapAngleTo180_float((float) (180 + p_82402_1_.hangingDirection * 90)),
                            false,
                            true);
                        texturecompass.currentAngle = d0;
                        texturecompass.angleDelta = d2;
                    }
                }
                RenderItem.renderInFrame = true;
                RenderManager.instance.renderEntityWithPosYaw((Entity) entityitem, 0.0, 0.0, 0.0, 0.0f, 0.0f);
                RenderItem.renderInFrame = false;
                if (item == Items.compass) {
                    final TextureAtlasSprite textureatlassprite2 = ((TextureMap) Minecraft.getMinecraft()
                        .getTextureManager()
                        .getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(
                            Items.compass.getIconIndex(entityitem.getEntityItem())
                                .getIconName());
                    if (textureatlassprite2.getFrameCount() > 0) {
                        textureatlassprite2.updateAnimation();
                    }
                }
            }
            GL11.glPopMatrix();
        }
    }

    protected void func_147914_a(final EntityItemFrame p_147914_1_, final double p_147914_2_, final double p_147914_4_,
        final double p_147914_6_) {
        if (Minecraft.isGuiEnabled() && p_147914_1_.getDisplayedItem() != null
            && p_147914_1_.getDisplayedItem()
                .hasDisplayName()
            && this.renderManager.field_147941_i == p_147914_1_) {
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            final double d3 = p_147914_1_.getDistanceSqToEntity((Entity) this.renderManager.livingPlayer);
            final float f3 = p_147914_1_.isSneaking() ? 32.0f : 64.0f;
            if (d3 < f3 * f3) {
                final String s = p_147914_1_.getDisplayedItem()
                    .getDisplayName();
                if (p_147914_1_.isSneaking()) {
                    final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef(
                        (float) p_147914_2_ + 0.0f,
                        (float) p_147914_4_ + p_147914_1_.height + 0.5f,
                        (float) p_147914_6_);
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
                } else {
                    this.func_147906_a((Entity) p_147914_1_, s, p_147914_2_, p_147914_4_, p_147914_6_, 64);
                }
            }
        }
    }

    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityItemFrame) p_110775_1_);
    }

    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_,
        final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityItemFrame) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    static {
        mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
    }
}
