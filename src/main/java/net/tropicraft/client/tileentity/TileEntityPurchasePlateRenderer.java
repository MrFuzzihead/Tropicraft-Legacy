package net.tropicraft.client.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class TileEntityPurchasePlateRenderer extends TileEntitySpecialRenderer {

    private Map entityHashMap;
    public long lifeTick;
    public long lastWorldTick;

    public TileEntityPurchasePlateRenderer() {
        this.entityHashMap = new HashMap();
        this.lifeTick = 0L;
        this.lastWorldTick = 0L;
        this.lifeTick = 0L;
    }

    public void renderTileEntityMobSpawner(final TileEntityPurchasePlate par1TileEntityMobSpawner, final double par2,
        final double par4, final double par6, final float par8) {
        if (par1TileEntityMobSpawner.tradeState == 1) {
            if (par1TileEntityMobSpawner.showToolTip) {
                this.renderLivingLabel(
                    "Offer Items: right click koa with item",
                    par2 + 0.5,
                    par4 + 0.8999999761581421,
                    par6 + 0.5,
                    0);
                this.renderLivingLabel(
                    "Cycle Items: right click plate",
                    par2 + 0.5,
                    par4 + 0.800000011920929,
                    par6 + 0.5,
                    0);
                this.renderLivingLabel(
                    "Buy Item: left click plate",
                    par2 + 0.5,
                    par4 + 0.699999988079071,
                    par6 + 0.5,
                    0);
            }
            String name = "???";
            final ItemEntry ie = ItemValues.itemsBuyable.get(par1TileEntityMobSpawner.itemIndex);
            final ItemStack is = ie.item;
            if (is != null) {
                name = is.getDisplayName();
                if (is.stackSize > 1) {
                    name = name + " x" + is.stackSize;
                }
                if (name.startsWith("item.")) {
                    name = name.substring(5);
                }
                this.renderLivingLabel("" + name, par2 + 0.5, par4 + 0.44999998807907104, par6 + 0.5, 0);
            }
        } else if (par1TileEntityMobSpawner.tradeState == 2) {
            this.renderLivingLabel("Confirm Trade", par2 + 0.5, par4 + 0.44999998807907104, par6 + 0.5, 0);
        }
        if (par1TileEntityMobSpawner.tradeState > 0) {
            this.renderBanner(par1TileEntityMobSpawner, par2, par4, par6, par8);
            if (this.lastWorldTick != par1TileEntityMobSpawner.getWorldObj()
                .getWorldTime()) {
                this.lastWorldTick = par1TileEntityMobSpawner.getWorldObj()
                    .getWorldTime();
                this.lifeTick += (long) (1.0 + 0.5 * FMLClientHandler.instance()
                    .getClient().thePlayer.getDistance(
                        (double) par1TileEntityMobSpawner.xCoord,
                        (double) par1TileEntityMobSpawner.yCoord,
                        (double) par1TileEntityMobSpawner.zCoord)
                    / 2.0);
                ++par1TileEntityMobSpawner.angle;
                if (par1TileEntityMobSpawner.angle >= 360.0f) {
                    par1TileEntityMobSpawner.angle -= 360.0f;
                }
            }
            this.renderEntityItem2(par1TileEntityMobSpawner, par2, par4, par6, par8);
        }
    }

    public void renderEntityItem2(final TileEntityPurchasePlate par1TileEntityMobSpawner, final double par2,
        final double par4, final double par6, final float par8) {
        GL11.glPushMatrix();
        final float var10 = 0.3f;
        final float yOffset = MathHelper.sin((this.lifeTick + par8) / 10.0f + par1TileEntityMobSpawner.startOffset)
            * 0.03f + 0.0f;
        GL11.glTranslatef((float) par2 + 0.5f, (float) par4 - 0.1f + yOffset + 0.75f * var10, (float) par6 + 0.5f);
        final float var11 = ((this.lifeTick + par8) / 20.0f + par1TileEntityMobSpawner.startOffset) * 57.295776f;
        GL11.glRotatef(-var11, 0.0f, 1.0f, 0.0f);
        final float var12 = var11;
        GL11.glScalef(var10, var10, var10);
        final ItemStack is = ItemValues.itemsBuyable.get(par1TileEntityMobSpawner.itemIndex).item;
        if (is != null) {
            RenderManager.instance.itemRenderer.renderItem(
                (EntityLivingBase) FMLClientHandler.instance()
                    .getClient().thePlayer,
                is,
                0);
        } else {
            System.out.println(
                "tropicraft tradeblock renderable item is null, bug? itemindex: " + par1TileEntityMobSpawner.itemIndex);
        }
        GL11.glPopMatrix();
    }

    public void renderEntityItem() {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
        final float scale = 0.0078125f;
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f);
        final ResourceLocation res = new ResourceLocation("textures/map/map_background.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(res);
        final Tessellator tessellator = Tessellator.instance;
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        tessellator.startDrawingQuads();
        final byte margin = 7;
        tessellator.addVertexWithUV((double) (0 - margin), (double) (128 + margin), 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((double) (128 + margin), (double) (128 + margin), 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV((double) (128 + margin), (double) (0 - margin), 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV((double) (0 - margin), (double) (0 - margin), 0.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(final TileEntity par1TileEntity, final double par2, final double par4,
        final double par6, final float par8) {
        this.renderTileEntityMobSpawner((TileEntityPurchasePlate) par1TileEntity, par2, par4, par6, par8);
    }

    protected void renderBanner(final TileEntityPurchasePlate par1TileEntityMobSpawner, final double par3,
        final double par5, final double par7, final float par8) {
        final String par2Str = "Credit: " + par1TileEntityMobSpawner.credit;
        final String par2Str2 = "Cost: " + ItemValues.itemsBuyable.get(par1TileEntityMobSpawner.itemIndex).value;
        final FontRenderer var11 = RenderManager.instance.getFontRenderer();
        final float var12 = 1.2f;
        final float var13 = 0.016f * var12;
        float x = 0.5f;
        float z = 0.0f;
        for (float angle = -180.0f; angle <= 90.0f; angle += 90.0f) {
            z = (x = 0.0f);
            if (angle == -180.0f) {
                z = 0.501f;
            } else if (angle == -90.0f) {
                x = 0.501f;
            } else if (angle == 0.0f) {
                z = -0.501f;
            } else if (angle == 90.0f) {
                x = -0.501f;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((float) par3 + 0.5f + x, (float) par5 - 0.1f, (float) par7 + 0.5f + z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
            GL11.glScalef(-var13, -var13, var13);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Tessellator var14 = Tessellator.instance;
            byte var15 = 0;
            GL11.glDisable(3553);
            var14.startDrawingQuads();
            int var16 = var11.getStringWidth(par2Str2) / 2;
            var14.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.25f);
            var14.addVertex((double) (-var16 - 1), (double) (-1 + var15), 0.0);
            var14.addVertex((double) (-var16 - 1), (double) (8 + var15), 0.0);
            var14.addVertex((double) (var16 + 1), (double) (8 + var15), 0.0);
            var14.addVertex((double) (var16 + 1), (double) (-1 + var15), 0.0);
            var14.draw();
            GL11.glEnable(3553);
            var11.drawString(par2Str2, -var11.getStringWidth(par2Str2) / 2, (int) var15, 553648127);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            var11.drawString(par2Str2, -var11.getStringWidth(par2Str2) / 2, (int) var15, -1);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef((float) par3 + 0.5f + x, (float) par5 - 0.3f, (float) par7 + 0.5f + z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
            GL11.glScalef(-var13, -var13, var13);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            var14 = Tessellator.instance;
            var15 = 0;
            GL11.glDisable(3553);
            var14.startDrawingQuads();
            var16 = var11.getStringWidth(par2Str) / 2;
            var14.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.25f);
            var14.addVertex((double) (-var16 - 1), (double) (-1 + var15), 0.0);
            var14.addVertex((double) (-var16 - 1), (double) (8 + var15), 0.0);
            var14.addVertex((double) (var16 + 1), (double) (8 + var15), 0.0);
            var14.addVertex((double) (var16 + 1), (double) (-1 + var15), 0.0);
            var14.draw();
            GL11.glEnable(3553);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, (int) var15, 553648127);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, (int) var15, -1);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }

    protected void renderLivingLabel(final String par2Str, final double par3, final double par5, final double par7,
        final int par9) {
        final FontRenderer var11 = RenderManager.instance.getFontRenderer();
        final float var12 = 0.8f;
        final float var13 = 0.016666668f * var12;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par3 + 0.0f, (float) par5 + 0.2f, (float) par7);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var13, -var13, var13);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        final Tessellator var14 = Tessellator.instance;
        final byte var15 = 0;
        GL11.glDisable(3553);
        var14.startDrawingQuads();
        final int var16 = var11.getStringWidth(par2Str) / 2;
        var14.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.25f);
        var14.addVertex((double) (-var16 - 1), (double) (-1 + var15), 0.0);
        var14.addVertex((double) (-var16 - 1), (double) (8 + var15), 0.0);
        var14.addVertex((double) (var16 + 1), (double) (8 + var15), 0.0);
        var14.addVertex((double) (var16 + 1), (double) (-1 + var15), 0.0);
        var14.draw();
        GL11.glEnable(3553);
        var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, (int) var15, 553648127);
        GL11.glDepthMask(true);
        var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, (int) var15, -1);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
