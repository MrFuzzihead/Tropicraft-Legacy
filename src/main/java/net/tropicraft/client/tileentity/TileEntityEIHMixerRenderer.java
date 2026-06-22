package net.tropicraft.client.tileentity;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityEIHMixer;
import net.tropicraft.client.block.model.ModelBambooMug;
import net.tropicraft.client.entity.model.ModelEIHMixer;
import net.tropicraft.item.ItemCocktail;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class TileEntityEIHMixerRenderer extends TileEntitySpecialRenderer {

    private ModelEIHMixer modelMixer;
    private ModelBambooMug modelBambooMug;
    private EntityItem dummyEntityItem;
    private RenderItem renderItem;

    public TileEntityEIHMixerRenderer() {
        this.modelMixer = new ModelEIHMixer();
        this.modelBambooMug = new ModelBambooMug();
        this.dummyEntityItem = new EntityItem((World) null, 0.0, 0.0, 0.0, new ItemStack(Items.sugar));
        (this.renderItem = new RenderItem()).setRenderManager(RenderManager.instance);
    }

    private void renderEIHMixer(final TileEntityEIHMixer te, final double x, final double y, final double z,
        final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
        if (te.getWorldObj() == null) {
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        } else {
            final int meta = te.getBlockMetadata();
            if (meta == 2) {
                GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
            } else if (meta == 3) {
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            } else if (meta == 4) {
                GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
            } else if (meta == 5) {
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        if (te.isMixing()) {
            final float angle = MathHelper.sin((float) (157.07963267948966 * te.ticks / 80.0)) * 15.0f;
            GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
        }
        TropicraftUtils.bindTextureTE("eihmixer");
        this.modelMixer.renderEIHMixer();
        final ItemStack[] ingredients = te.getIngredients();
        if (!te.isDoneMixing()) {
            if (ingredients[0] != null) {
                GL11.glPushMatrix();
                GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
                GL11.glTranslatef(0.3f, -0.5f, 0.05f);
                GL11.glRotatef(RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f);
                this.dummyEntityItem.setEntityItemStack(ingredients[0]);
                this.renderItem.doRender(this.dummyEntityItem, 0.0, 0.0, 0.0, 0.0f, 0.0f);
                GL11.glPopMatrix();
            }
            if (ingredients[1] != null) {
                GL11.glPushMatrix();
                GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
                GL11.glTranslatef(-0.3f, -0.5f, 0.05f);
                GL11.glRotatef(RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f);
                this.dummyEntityItem.setEntityItemStack(ingredients[1]);
                this.renderItem.doRender(this.dummyEntityItem, 0.0, 0.0, 0.0, 0.0f, 0.0f);
                GL11.glPopMatrix();
            }
        }
        if (te.isMixing()) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.2f, -0.25f, 0.0f);
            if (te.isDoneMixing()) {
                this.modelBambooMug.renderLiquid = true;
                this.modelBambooMug.liquidColor = ItemCocktail.getCocktailColor(te.result);
            } else {
                this.modelBambooMug.renderLiquid = false;
            }
            TropicraftUtils.bindTextureTE("bamboomug");
            this.modelBambooMug.renderBambooMug();
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
        final float var8) {
        this.renderEIHMixer((TileEntityEIHMixer) var1, var2, var4, var6, var8);
    }
}
