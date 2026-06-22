package net.tropicraft.client.tileentity;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityBambooMug;
import net.tropicraft.client.block.model.ModelBambooMug;
import net.tropicraft.client.entity.model.ModelUmbrella;
import net.tropicraft.drinks.Drink;
import net.tropicraft.item.ItemCocktail;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TileEntityBambooMugRenderer extends TileEntitySpecialRenderer {

    private ModelBambooMug modelBambooMug;
    private ModelUmbrella modelUmbrella;

    public TileEntityBambooMugRenderer() {
        this.modelBambooMug = new ModelBambooMug();
        this.modelUmbrella = new ModelUmbrella();
    }

    public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z,
        final float partialTicks) {
        final TileEntityBambooMug mug = (TileEntityBambooMug) tileentity;
        TropicraftUtils.bindTextureTE("bamboomug");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
        final int meta = mug.getMetadata();
        if (meta == 2) {
            GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
        } else if (meta == 3) {
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        } else if (meta == 4) {
            GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
        } else if (meta == 5) {
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        }
        if (!mug.isEmpty()) {
            this.modelBambooMug.renderLiquid = true;
            this.modelBambooMug.liquidColor = ItemCocktail.getCocktailColor(mug.cocktail);
        } else {
            this.modelBambooMug.renderLiquid = false;
        }
        this.modelBambooMug.renderBambooMug();
        if (!mug.isEmpty()) {
            final Drink drink = ItemCocktail.getDrink(mug.cocktail);
            if (drink != null && drink.hasUmbrella) {
                GL11.glTranslatef(0.0f, 1.175f, 0.0f);
                GL11.glRotatef(30.0f, 1.0f, 0.0f, 1.0f);
                GL11.glScalef(0.25f, 0.25f, 0.25f);
                final float red = ColorHelper.getRed(11743532);
                final float green = ColorHelper.getGreen(11743532);
                final float blue = ColorHelper.getBlue(11743532);
                Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftUtils.getTextureEntity("umbrellaLayer"));
                this.modelUmbrella.renderUmbrella();
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glTexEnvf(8960, 8704, 3042.0f);
                final FloatBuffer color = BufferUtils.createFloatBuffer(4)
                    .put(new float[] { red, green, blue, 1.0f });
                color.position(0);
                GL11.glTexEnv(8960, 8705, color);
                Minecraft.getMinecraft().renderEngine
                    .bindTexture(TropicraftUtils.getTextureEntity("umbrellaColorLayer"));
                this.modelUmbrella.renderUmbrella();
                GL11.glDisable(3042);
                GL11.glTexEnvf(8960, 8704, 8448.0f);
                GL11.glPopMatrix();
            }
        }
        GL11.glPopMatrix();
    }
}
