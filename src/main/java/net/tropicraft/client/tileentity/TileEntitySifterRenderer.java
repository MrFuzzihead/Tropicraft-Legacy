package net.tropicraft.client.tileentity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntitySifter;

import org.lwjgl.opengl.GL11;

public class TileEntitySifterRenderer extends TileEntitySpecialRenderer {

    private Entity item;

    public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
        final float var8) {
        this.renderSifter((TileEntitySifter) var1, var2, var4, var6, var8);
    }

    public void renderSifter(final TileEntitySifter sifter, final double x, final double y, final double z,
        final float rotation) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        if (this.item == null && sifter.isSifting()) {
            this.item = (Entity) new EntityItem(sifter.getWorldObj());
            ((EntityItem) this.item).setEntityItemStack(sifter.siftItem.copy());
        }
        if (this.item != null) {
            this.item.setWorld(sifter.getWorldObj());
            final float f1 = 0.4375f;
            GL11.glTranslatef(0.0f, 0.7f, 0.0f);
            GL11.glScalef(f1 * 3.0f, f1 * 3.0f, f1 * 3.0f);
            GL11.glRotatef((float) (sifter.yaw2 + (sifter.yaw - sifter.yaw2) * rotation) * 10.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-20.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslatef(0.0f, -0.4f, 0.0f);
            this.item.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
            if (sifter.isSifting()) {
                RenderManager.instance.renderEntityWithPosYaw(this.item, 0.0, 0.0, 0.0, 0.0f, rotation);
            } else {
                this.item = null;
            }
        }
        GL11.glPopMatrix();
    }
}
