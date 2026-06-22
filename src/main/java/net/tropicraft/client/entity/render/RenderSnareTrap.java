package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.entity.placeable.EntitySnareTrap;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSnareTrap extends Render {

    public RenderSnareTrap() {
        this.shadowSize = 0.0f;
    }

    public void doRender(final Entity var1, final double var2, final double var4, final double var6, final float var8,
        final float var9) {
        this.doRenderTrap((EntitySnareTrap) var1, var2, var4, var6, var8, var9);
    }

    private void doRenderTrap(final EntitySnareTrap trap, final double x, final double y, final double z,
        final float yaw, final float ticks) {
        final double n = trap.trapPosX;
        final RenderManager renderManager = this.renderManager;
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = trap.trapPosY;
        final RenderManager renderManager2 = this.renderManager;
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = trap.trapPosZ;
        final RenderManager renderManager3 = this.renderManager;
        final double zPos = n3 - RenderManager.renderPosZ;
        final int height = trap.trapHeight;
        final ForgeDirection dir = trap.trapDirection;
        final boolean full = trap.getFull();
        final float entityHeight = trap.getEntityHeight();
        final float vOff = 0.05f;
        final float hOff = 0.2f;
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        final Tessellator tessellator = Tessellator.instance;
        if (!full) {
            tessellator.startDrawing(3);
            tessellator.setColorRGBA_F(0.75f, 0.75f, 0.75f, 0.25f);
            tessellator.addVertex(xPos + hOff, yPos + vOff, zPos + hOff);
            tessellator.addVertex(xPos + 1.0 - hOff, yPos + vOff, zPos + hOff);
            tessellator.addVertex(xPos + 1.0 - hOff, yPos + vOff, zPos + 1.0 - hOff);
            tessellator.addVertex(xPos + hOff, yPos + vOff, zPos + 1.0 - hOff);
            tessellator.addVertex(xPos + hOff, yPos + vOff, zPos + hOff);
            tessellator.draw();
            tessellator.startDrawing(3);
            tessellator.setColorRGBA_F(0.75f, 0.75f, 0.75f, 0.5f);
            final double startX = xPos + 0.5 - dir.offsetX * (1.0f - 2.0f * hOff) / 2.0;
            final double startY = yPos + vOff;
            final double startZ = zPos + 0.5 - dir.offsetZ * (1.0f - 2.0f * hOff) / 2.0;
            final double dx = xPos + 0.5 - dir.offsetX / 2.0 - startX;
            final double dy = yPos + height - 0.5 - startY;
            final double dz = zPos + 0.5 - dir.offsetZ / 2.0 - startZ;
            for (int steps = 5, i = 0; i <= steps; ++i) {
                final float step = i / (float) steps;
                tessellator.addVertex(startX + step * dx, startY + step * dy, startZ + step * dz);
            }
            tessellator.draw();
        } else {
            tessellator.startDrawing(3);
            tessellator.setColorRGBA_F(0.75f, 0.75f, 0.75f, 0.5f);
            final double startX = xPos + 0.5;
            final double startY = yPos + entityHeight;
            final double startZ = zPos + 0.5;
            final double dx = xPos + 0.5 - dir.offsetX / 2.0 - startX;
            final double dy = yPos + height - 0.5 - startY;
            final double dz = zPos + 0.5 - dir.offsetZ / 2.0 - startZ;
            for (int steps = 5, i = 0; i <= steps; ++i) {
                final float step = i / (float) steps;
                tessellator.addVertex(startX + step * dx, startY + step * dy, startZ + step * dz);
            }
            tessellator.draw();
        }
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }

    protected ResourceLocation getEntityTexture(final Entity entity) {
        return null;
    }
}
