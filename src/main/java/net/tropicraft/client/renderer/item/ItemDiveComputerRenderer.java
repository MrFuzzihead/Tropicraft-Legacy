package net.tropicraft.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.tropicraft.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class ItemDiveComputerRenderer implements IItemRenderer {

    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.FIRST_PERSON_MAP;
    }

    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item,
        final IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final EntityPlayer player = (EntityPlayer) data[0];
        final TextureManager textureManager = (TextureManager) data[1];
        final MapData mapData = (MapData) data[2];
        final ItemStack chestplate = player.getEquipmentInSlot(3);
        if (chestplate != null && chestplate.getItem() instanceof ItemScubaChestplateGear) {
            float airRemaining = (float) this.getTagCompound(chestplate)
                .getInteger("AirRemaining");
            final int currentDepth = MathHelper.floor_double(player.posY);
            final int maxDepth = this.getTagCompound(chestplate)
                .getInteger("MaxDepth");
            airRemaining = chestplate.getTagCompound()
                .getFloat("AirContained");
            final int blocksAbove = chestplate.getTagCompound()
                .getInteger("WaterBlocksAbove");
            final int blocksBelow = chestplate.getTagCompound()
                .getInteger("WaterBlocksBelow");
            final ItemScubaChestplateGear gear = (ItemScubaChestplateGear) chestplate.getItem();
            float timeRemaining = airRemaining / gear.getAirType(chestplate)
                .getUsageRate();
            final String timeUnits = (timeRemaining <= 60.0f) ? "secs" : "mins";
            timeRemaining = ((timeRemaining <= 60.0f) ? timeRemaining : (timeRemaining / 60.0f));
            final float airTemp = player.worldObj.getBiomeGenForCoords(
                MathHelper.floor_double(player.posX),
                MathHelper.floor_double(player.posZ)).temperature;
            final int width = Minecraft.getMinecraft().displayWidth;
            final int height = Minecraft.getMinecraft().displayHeight;
            final float yaw = player.rotationYaw;
            final int heading = MathHelper.floor_double(yaw * 4.0f / 360.0f + 0.5) & 0x3;
            GL11.glPushMatrix();
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3008);
            textureManager.bindTexture(TropicraftUtils.getTextureGui("diveComputerBackground"));
            final Tessellator tessellator = Tessellator.instance;
            GL11.glNormal3f(0.0f, 0.0f, -1.0f);
            tessellator.startDrawingQuads();
            final byte b0 = 16;
            int other = 140;
            tessellator.addVertexWithUV((double) (0 - b0), (double) (other + b0), 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV((double) (other + b0), (double) (other + b0), 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV((double) (other + b0), (double) (0 - b0), 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV((double) (0 - b0), (double) (0 - b0), 0.0, 0.0, 0.0);
            tessellator.draw();
            GL11.glPushMatrix();
            GL11.glNormal3f(0.0f, 0.0f, 1.0f);
            GL11.glScalef(0.45f, 0.45f, 1.0f);
            GL11.glTranslatef(150.0f, 150.0f, 0.0f);
            GL11.glRotatef(yaw + 180.0f, 0.0f, 0.0f, -1.0f);
            textureManager.bindTexture(TropicraftUtils.getTextureGui("compassBackground"));
            tessellator.startDrawingQuads();
            final int offset = -75;
            other = 150;
            tessellator.addVertexWithUV((double) (0 + offset), (double) (other + offset), 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV((double) (other + offset), (double) (other + offset), 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV((double) (other + offset), (double) (0 + offset), 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV((double) (0 + offset), (double) (0 + offset), 0.0, 0.0, 0.0);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glScalef(1.3f, 1.3f, 1.0f);
            Minecraft.getMinecraft().fontRenderer.drawString(String.format("%.0f", airRemaining), 70, 14, 52446);
            this.drawString(blocksAbove, 46, 79, 12303359);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(0.6f, 0.6f, 1.0f);
            this.drawString(String.format("%.0f %s", timeRemaining, timeUnits), 29, 30, 16182034);
            this.drawString(TropicraftUtils.translateGUI("maxDepth") + ": " + maxDepth, 194, 150, -1);
            this.drawString(airTemp + " F", 6, 150, -1);
            GL11.glPopMatrix();
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            this.drawString("psi", 245, 47, 16777215);
            this.drawString("Air", 206, 24, 16777215);
            this.drawString(TropicraftUtils.translateGUI("timeRemaining"), 34, 24, 16777215);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }

    private boolean isFullyUnderwater(final EntityPlayer player) {
        final int x = MathHelper.ceiling_double_int(player.posX);
        final int y = MathHelper.ceiling_double_int(player.posY + player.height - 2.299999952316284);
        final int z = MathHelper.ceiling_double_int(player.posZ);
        return player.worldObj.getBlock(x, y, z)
            .getMaterial()
            .isLiquid();
    }

    public NBTTagCompound getTagCompound(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    private void drawString(final Object text, final int x, final int y, final int color) {
        Minecraft.getMinecraft().fontRenderer.drawString(String.valueOf(text), x, y, color);
    }
}
