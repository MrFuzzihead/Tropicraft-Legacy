package net.tropicraft.client.tileentity;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.client.block.model.ModelCurareBowl;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class TileEntityCurareBowlRenderer extends TileEntitySpecialRenderer {

    ModelCurareBowl modelBowl;
    private EntityItem dummyEntityItem;
    private RenderItem renderItem;

    public TileEntityCurareBowlRenderer() {
        this.modelBowl = new ModelCurareBowl();
        this.dummyEntityItem = new EntityItem((World) null, 0.0, 0.0, 0.0, new ItemStack(Items.sugar));
        (this.renderItem = new RenderItem()).setRenderManager(RenderManager.instance);
    }

    private void renderBowl(final TileEntityCurareBowl bowl, final double x, final double y, final double z,
        final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
        final List<ItemStack> ingredients = (List<ItemStack>) bowl.getIngredientList();
        if (ingredients != null) {
            final int count = ingredients.size();
            final float turnRate = 1.0f;
            float startAngle;
            if (bowl.pestleTicks > 0) {
                final int ticksPerPestleClick = 5;
                final float progress = bowl.pestleTicks / (float) ticksPerPestleClick;
                startAngle = (float) (turnRate * 2.0f * 3.141592653589793 * progress);
            } else {
                startAngle = 0.0f;
            }
            for (int i = 0; i < count; ++i) {
                GL11.glPushMatrix();
                final float angle = (float) (6.283185307179586 * (float) i / count);
                final float radius = 0.666f;
                final float offsetx = MathHelper.sin(startAngle + angle) * radius;
                final float offsetz = MathHelper.cos(startAngle + angle) * radius;
                GL11.glTranslatef(-0.05f, 1.4f, 0.0f);
                GL11.glScalef(0.333f, 0.333f, 0.333f);
                GL11.glTranslatef(offsetx, 0.0f, offsetz);
                GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(180.0f, 1.0f, 0.0f, 1.0f);
                GL11.glRotatef(RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f);
                this.dummyEntityItem.setEntityItemStack((ItemStack) ingredients.get(i));
                this.renderItem.doRender(this.dummyEntityItem, 0.0, 0.0, 0.0, 0.0f, 0.0f);
                GL11.glPopMatrix();
            }
        }
        TropicraftUtils.bindTextureBlock("curareBowlModel");
        this.modelBowl.renderBowl();
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
        final float var8) {
        this.renderBowl((TileEntityCurareBowl) var1, var2, var4, var6, var8);
    }
}
