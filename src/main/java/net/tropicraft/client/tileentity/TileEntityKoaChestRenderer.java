package net.tropicraft.client.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityKoaChestRenderer extends TileEntitySpecialRenderer {

    private ModelChest chestModel;
    private ModelChest chestModelLarge;

    public TileEntityKoaChestRenderer() {
        this.chestModel = new ModelChest();
        this.chestModelLarge = (ModelChest) new ModelLargeChest();
    }

    public void renderChest(final TileEntityKoaChest tileentitybamboochest, final double d, final double d1,
        final double d2, final float f) {
        int i;
        if (tileentitybamboochest.getWorldObj() == null) {
            i = 0;
        } else {
            final Block block = tileentitybamboochest.getBlockType();
            i = tileentitybamboochest.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                try {
                    ((BlockChest) block).func_149954_e(
                        tileentitybamboochest.getWorldObj(),
                        tileentitybamboochest.xCoord,
                        tileentitybamboochest.yCoord,
                        tileentitybamboochest.zCoord);
                } catch (ClassCastException e) {
                    FMLLog.severe(
                        "Attempted to render a chest at %d,  %d, %d that was not a chest",
                        new Object[] { tileentitybamboochest.xCoord, tileentitybamboochest.yCoord,
                            tileentitybamboochest.zCoord });
                }
                i = tileentitybamboochest.getBlockMetadata();
            }
            tileentitybamboochest.checkForAdjacentChests();
        }
        if (tileentitybamboochest.adjacentChestZNeg != null || tileentitybamboochest.adjacentChestXNeg != null) {
            return;
        }
        ModelChest modelchest;
        if (tileentitybamboochest.adjacentChestXPos != null || tileentitybamboochest.adjacentChestZPos != null) {
            modelchest = this.chestModelLarge;
            TropicraftUtils.bindTextureBlock("largechest");
        } else {
            modelchest = this.chestModel;
            TropicraftUtils.bindTextureBlock("chest");
        }
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef((float) d, (float) d1 + 1.0f, (float) d2 + 1.0f);
        GL11.glScalef(1.0f, -1.0f, -1.0f);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        int j = 0;
        if (i == 2) {
            j = 180;
        }
        if (i == 3) {
            j = 0;
        }
        if (i == 4) {
            j = 90;
        }
        if (i == 5) {
            j = -90;
        }
        if (i == 2 && tileentitybamboochest.adjacentChestXPos != null) {
            GL11.glTranslatef(1.0f, 0.0f, 0.0f);
        }
        if (i == 5 && tileentitybamboochest.adjacentChestZPos != null) {
            GL11.glTranslatef(0.0f, 0.0f, -1.0f);
        }
        GL11.glRotatef((float) j, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        float f2 = tileentitybamboochest.prevLidAngle
            + (tileentitybamboochest.lidAngle - tileentitybamboochest.prevLidAngle) * f;
        if (tileentitybamboochest.adjacentChestZNeg != null) {
            final float f3 = tileentitybamboochest.adjacentChestZNeg.prevLidAngle
                + (tileentitybamboochest.adjacentChestZNeg.lidAngle
                    - tileentitybamboochest.adjacentChestZNeg.prevLidAngle) * f;
            if (f3 > f2) {
                f2 = f3;
            }
        }
        if (tileentitybamboochest.adjacentChestXNeg != null) {
            final float f4 = tileentitybamboochest.adjacentChestXNeg.prevLidAngle
                + (tileentitybamboochest.adjacentChestXNeg.lidAngle
                    - tileentitybamboochest.adjacentChestXNeg.prevLidAngle) * f;
            if (f4 > f2) {
                f2 = f4;
            }
        }
        f2 = 1.0f - f2;
        f2 = 1.0f - f2 * f2 * f2;
        modelchest.chestLid.rotateAngleX = -(f2 * 3.141593f / 2.0f);
        modelchest.renderAll();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderTileEntityAt(final TileEntity tileentity, final double d, final double d1, final double d2,
        final float f) {
        this.renderChest((TileEntityKoaChest) tileentity, d, d1, d2, f);
    }
}
