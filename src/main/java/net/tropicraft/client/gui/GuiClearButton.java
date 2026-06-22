package net.tropicraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiClearButton extends GuiButton {

    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean drawButton;
    public int type;
    public String buttonImageLoc;
    public int color;

    public GuiClearButton(final int i, final int j, final int k, final String s) {
        this(i, j, k, 45, 45, s);
    }

    public GuiClearButton(final int i, final int j, final int k, final int l, final int i1, final String s) {
        super(i, j, k, l, i1, "");
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.drawButton = true;
        this.id = i;
        this.xPosition = j;
        this.yPosition = k;
        this.width = l;
        this.height = i1;
        this.displayString = s;
    }

    public GuiClearButton(final int i, final int j, final int k, final int l, final int i1, final String s,
        final int Type, final String img, final int TextColor) {
        super(i, j, k, l, i1, "");
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.drawButton = true;
        this.id = i;
        this.xPosition = j;
        this.yPosition = k;
        this.width = l;
        this.height = i1;
        this.displayString = s;
        this.type = Type;
        this.buttonImageLoc = img;
        this.color = TextColor;
    }

    public void initGui() {}

    public int getHoverState(final boolean flag) {
        byte byte0 = 1;
        if (!this.enabled) {
            byte0 = 0;
        } else if (flag) {
            byte0 = 2;
        }
        return byte0;
    }

    public void drawButton(final Minecraft minecraft, final int i, final int j) {
        if (!this.drawButton) {
            return;
        }
        final boolean flag = i >= this.xPosition && j >= this.yPosition
            && i < this.xPosition + this.width
            && j < this.yPosition + this.height;
        final FontRenderer fontrenderer = minecraft.fontRenderer;
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glPushMatrix();
        TropicraftUtils.bindTextureGui(this.buttonImageLoc);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final int k = this.getHoverState(flag);
        GL11.glScalef(1.35f, 1.35f, 1.35f);
        this.drawTexturedModalRect(
            (int) (this.xPosition / 1.35),
            (int) (this.yPosition / 1.35),
            this.type * 16 + 128 + (k - 1) * 8,
            240,
            8,
            16);
        GL11.glPopMatrix();
        this.mouseDragged(minecraft, i, j);
        if (!this.enabled) {
            fontrenderer
                .drawString(this.displayString, this.xPosition, this.yPosition + (this.height - 8) / 2, -6250336);
        } else if (flag) {
            fontrenderer
                .drawString(this.displayString, this.xPosition, this.yPosition + (this.height - 8) / 2, 10027008);
        } else {
            fontrenderer
                .drawString(this.displayString, this.xPosition, this.yPosition + (this.height - 8) / 2, this.color);
        }
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }

    protected void mouseDragged(final Minecraft minecraft, final int i, final int j) {}

    public void mouseReleased(final int i, final int j) {}

    public boolean mousePressed(final Minecraft minecraft, final int i, final int j) {
        return this.enabled && this.drawButton
            && i >= this.xPosition
            && j >= this.yPosition
            && i < this.xPosition + this.width
            && j < this.yPosition + this.height;
    }
}
