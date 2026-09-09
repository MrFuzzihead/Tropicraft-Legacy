package net.tropicraft.client.gui;

import java.util.List;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.encyclopedia.Encyclopedia;
import net.tropicraft.encyclopedia.TropicalBook;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTropicalBook extends GuiScreen {

    private TropicalBook book;
    private int indexPage;
    private int selectedIndex;
    private TropicalBook.ContentMode contentMode;
    private int contentPage;
    private String coverBackground;
    private String pageBackground;
    private String closedTextureIndex;
    private String openTextureIndex;
    private RenderItem itemRenderer;
    private static final int buttonNextIndexPage = 2000;
    private static final int buttonPrevIndexPage = 2001;
    private static final int buttonBookCover = 2003;
    private static final int buttonCraftingPage = 2010;
    private static final int buttonInfoPage = 2011;
    private static final int buttonNextContentPage = 2012;
    private static final int buttonPrevContentPage = 2013;

    public GuiTropicalBook(final TropicalBook tropbook) {
        this.indexPage = -1;
        this.selectedIndex = 0;
        this.contentMode = TropicalBook.ContentMode.INFO;
        this.contentPage = 0;
        this.itemRenderer = new RenderItem();
        this.book = tropbook;
        this.coverBackground = tropbook.outsideTexture;
        this.pageBackground = tropbook.insideTexture;
        this.closedTextureIndex = tropbook.outsideTexture;
        this.openTextureIndex = tropbook.insideTexture;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void initGui() {
        this.addButtons();
    }

    protected void actionPerformed(final GuiButton guibutton) {
        switch (guibutton.id) {
            case 2003: {
                this.indexPage = 0;
                this.contentMode = TropicalBook.ContentMode.INFO;
                this.contentPage = 0;
                break;
            }
            case 2000: {
                ++this.indexPage;
                this.contentPage = 0;
                break;
            }
            case 2001: {
                --this.indexPage;
                this.contentPage = 0;
                break;
            }
            case 2010: {
                this.contentMode = TropicalBook.ContentMode.RECIPE;
                this.contentPage = 0;
                break;
            }
            case 2011: {
                this.contentMode = TropicalBook.ContentMode.INFO;
                this.contentPage = 0;
                break;
            }
            case 2012: {
                ++this.contentPage;
                break;
            }
            case 2013: {
                --this.contentPage;
                break;
            }
            default: {
                this.selectedIndex = guibutton.id;
                if (this.book.isPageVisible(this.selectedIndex) && !this.book.hasPageBeenRead(this.selectedIndex)) {
                    this.book.markPageAsRead(this.selectedIndex);
                }
                this.contentMode = TropicalBook.ContentMode.INFO;
                this.contentPage = 0;
                break;
            }
        }
    }

    private void addButtons() {
        this.buttonList.clear();
        if (this.indexPage == -1) {
            this.buttonList
                .add(new GuiClearButton(2003, 0, 0, this.width, this.height, "", 0, this.coverBackground, 4456448));
        } else {
            int indexPosition = 0;
            for (int entry = this.indexPage * this.book.entriesPerIndexPage(); entry
                < (this.indexPage + 1) * this.book.entriesPerIndexPage() && entry < this.book.getPageCount(); ++entry) {
                String pageTitle = this.book.getPageTitleNotVisible(entry);
                int color = 4456448;
                if (this.book.isPageVisible(entry)) {
                    pageTitle = this.book.getPageTitleByIndex(entry);
                    if (!this.book.hasPageBeenRead(entry)) {
                        color = 3355647;
                    }
                }
                this.buttonList.add(
                    new GuiClearButton(
                        entry,
                        this.width / 2 - 129,
                        this.height / 2 - 87 + indexPosition * 15,
                        90,
                        10,
                        pageTitle,
                        -1,
                        this.pageBackground,
                        color));
                ++indexPosition;
            }
            if (this.indexPage > 0) {
                this.buttonList.add(
                    new GuiClearButton(
                        2001,
                        this.width / 2 - 168,
                        this.height / 2 - 20,
                        11,
                        22,
                        "",
                        2,
                        this.pageBackground,
                        4456448));
            }
            if ((this.indexPage + 1) * this.book.entriesPerIndexPage() < this.book.getPageCount()) {
                this.buttonList.add(
                    new GuiClearButton(
                        2000,
                        this.width / 2 - 168,
                        this.height / 2 - 50,
                        11,
                        22,
                        "",
                        1,
                        this.pageBackground,
                        4456448));
            }
            if (this.indexPage >= 0) {
                if (this.book.hasRecipeList()) {
                    switch (this.contentMode) {
                        case INFO: {
                            final List<ShapedRecipes> recipes = ((Encyclopedia) this.book)
                                .getRecipesForEntry(this.selectedIndex);
                            if (recipes != null) {
                                this.buttonList.add(
                                    new GuiClearButton(
                                        2010,
                                        this.width / 2 + 158,
                                        this.height / 2 - 80,
                                        11,
                                        22,
                                        "aa",
                                        5,
                                        this.pageBackground,
                                        4456448));
                                break;
                            }
                            break;
                        }
                        case RECIPE: {
                            this.buttonList.add(
                                new GuiClearButton(
                                    2011,
                                    this.width / 2 + 158,
                                    this.height / 2 - 80,
                                    11,
                                    22,
                                    "",
                                    6,
                                    this.pageBackground,
                                    4456448));
                            break;
                        }
                    }
                }
                if (this.contentPage > 0) {
                    this.buttonList.add(
                        new GuiClearButton(
                            2013,
                            this.width / 2 + 158,
                            this.height / 2 - 20,
                            11,
                            22,
                            "",
                            4,
                            this.pageBackground,
                            4456448));
                }
                if ((this.contentPage + 1) * this.book.entriesPerContentPage(this.contentMode)
                    < this.book.getContentPageCount(this.selectedIndex, this.contentMode)) {
                    this.buttonList.add(
                        new GuiClearButton(
                            2012,
                            this.width / 2 + 158,
                            this.height / 2 - 50,
                            11,
                            22,
                            "",
                            3,
                            this.pageBackground,
                            4456448));
                }
            }
        }
    }

    public void addIcons() {
        int indexPosition = 0;
        for (int entry = this.indexPage * this.book.entriesPerIndexPage(); entry
            < (this.indexPage + 1) * this.book.entriesPerIndexPage(); ++entry) {
            if (entry >= this.book.getPageCount()) {
                return;
            }
            GL11.glPushMatrix();
            GL11.glDisable(2896);
            TropicraftUtils.bindTextureGui(this.openTextureIndex);
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            GL11.glTranslatef(this.width / 1.5f, this.height / 1.5f, 0.0f);
            this.drawTexturedModalRect(-195, -121 + indexPosition * 20, 3, 190, 18, 18);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            GL11.glTranslatef(this.width / 1.5f - 1.0f, this.height / 1.5f, 0.0f);
            GL11.glColor3f(0.0f, 0.0f, 0.0f);
            this.itemRenderer.renderWithColor = this.book.isPageVisible(entry);
            final ItemStack is = this.book.getPageItemStack(entry);
            if (is != null) {
                this.itemRenderer
                    .renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, is, -193, -120 + indexPosition * 20);
            }
            GL11.glPopMatrix();
            ++indexPosition;
        }
    }

    protected void mouseClicked(final int x, final int y, final int mousebutton) {
        if (mousebutton == 0) {
            for (int l = 0; l < this.buttonList.size(); ++l) {
                final GuiButton guibutton = (GuiButton) this.buttonList.get(l);
                if (guibutton.mousePressed(this.mc, x, y)) {
                    this.mc.getSoundHandler()
                        .playSound(
                            (ISound) PositionedSoundRecord
                                .func_147674_a(new ResourceLocation("tropicraft:pageFlip"), 1.0f));
                    this.actionPerformed(guibutton);
                }
            }
        }
    }

    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
                this.mc.displayGuiScreen((GuiScreen) new GuiInventory((EntityPlayer) this.mc.thePlayer));
                return;
            }
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
    }

    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        if (this.indexPage == -1) {
            final float f2 = 1.35f;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(2896);
            TropicraftUtils.bindTextureGui(this.closedTextureIndex);
            GL11.glTranslatef((float) (this.width / 2), (float) (this.height / 2), 0.0f);
            GL11.glScalef(f2, f2, f2);
            this.drawTexturedModalRect(-64, -86, 0, 0, 128, 173);
        } else {
            final float f3 = 1.35f;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(2896);
            TropicraftUtils.bindTextureGui(this.openTextureIndex);
            GL11.glTranslatef((float) (this.width / 2), (float) (this.height / 2), 0.0f);
            GL11.glScalef(f3, f3, f3);
            this.drawTexturedModalRect(-128, -88, 0, 0, 256, 176);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(2896);
            TropicraftUtils.bindTextureGui(this.openTextureIndex);
            this.drawTexturedModalRect(this.width / 2 - 162, this.height / 2 - 115, 145, 201, 111, 32);
            GL11.glPopMatrix();
            this.fontRendererObj.drawString("Table of Contents", this.width / 2 - 150, this.height / 2 - 110, 4456448);
            this.fontRendererObj
                .drawString("" + (1 + this.indexPage), this.width / 2 - 159, this.height / 2 + 93, 4456448);
            if (this.book.hasIndexIcons()) {
                this.addIcons();
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(2896);
            switch (this.contentMode) {
                case INFO: {
                    final String pageTitle = this.book.isPageVisible(this.selectedIndex)
                        ? this.book.getPageTitleByIndex(this.selectedIndex)
                        : "§nPage not found";
                    this.fontRendererObj.drawString(
                        pageTitle,
                        this.width / 2 + 150 - this.fontRendererObj.getStringWidth(pageTitle),
                        this.height / 2 - 110,
                        4456448);
                    this.fontRendererObj.drawSplitString(
                        "  " + (this.book.isPageVisible(this.selectedIndex)
                            ? this.book.getPageDescriptionsByIndex(this.selectedIndex)
                            : "???"),
                        this.width / 2 + 20,
                        this.height / 2 - 80,
                        135,
                        4456448);
                    break;
                }
                case RECIPE: {
                    this.fontRendererObj.drawString("Crafting", this.width / 2 + 110, this.height / 2 - 110, 4456448);
                    try {
                        this.printRecipes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(2896);
            TropicraftUtils.bindTextureGui(this.openTextureIndex);
            this.drawTexturedModalRect(this.width / 2 + 20, this.height / 2 - 115, 90, 201, 142, 32);
            GL11.glPopMatrix();
            if (this.book.hasIndexIcons()) {
                GL11.glPushMatrix();
                GL11.glScalef(1.5f, 1.5f, 1.5f);
                GL11.glTranslatef(this.width / 3.0f + 0.6f, this.height / 3.0f - 1.2f, 0.0f);
                GL11.glColor3f(0.0f, 0.0f, 0.0f);
                this.itemRenderer.renderWithColor = this.book.isPageVisible(this.selectedIndex);
                final ItemStack is = this.book.getPageItemStack(this.selectedIndex);
                if (is != null) {
                    GL11.glEnable(32826);
                    RenderHelper.enableGUIStandardItemLighting();
                    this.itemRenderer.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, is, 15, -73);
                    RenderHelper.disableStandardItemLighting();
                    GL11.glDisable(32826);
                }
                GL11.glPopMatrix();
            }
        }
        this.addButtons();
        super.drawScreen(i, j, f);
    }

    private void printRecipes() throws Exception {
        final List<ShapedRecipes> recipes = ((Encyclopedia) this.book).getRecipesForEntry(this.selectedIndex);
        if (recipes == null || recipes.isEmpty()) {
            return;
        }
        final int newx = this.width / 2 + 25;
        int newy = this.height / 2 - 80;
        int indexPosition = 0;
        for (int entry = this.contentPage * this.book.entriesPerContentPage(this.contentMode); entry
            < (this.contentPage + 1) * this.book.entriesPerContentPage(this.contentMode); ++entry) {
            if (entry >= this.book.getContentPageCount(this.selectedIndex, this.contentMode)) {
                return;
            }
            final Encyclopedia.RecipeEntry recipe = ((Encyclopedia) this.book).getFormattedRecipe(recipes.get(entry));
            TropicraftUtils.bindTextureGui(this.openTextureIndex);
            this.drawTexturedModalRect(newx - 3, newy - 3, 0, 187, 122, 60);
            final int offsetX = 18;
            final int offsetY = 18;
            for (int row = 0; row < recipe.height; ++row) {
                for (int col = 0; col < recipe.width; ++col) {
                    final int itemIndex = row * recipe.width + col;
                    if (recipe.ingredients[itemIndex] != null) {
                        final int renderX = newx + offsetX * col + 1;
                        final int renderY = newy + offsetY * row + 1;
                        GL11.glPushMatrix();
                        GL11.glEnable(32826);
                        RenderHelper.enableGUIStandardItemLighting();
                        this.itemRenderer.renderWithColor = true;
                        this.itemRenderer.renderItemIntoGUI(
                            this.fontRendererObj,
                            this.mc.renderEngine,
                            recipe.ingredients[itemIndex],
                            renderX,
                            renderY);
                        this.itemRenderer.renderWithColor = false;
                        RenderHelper.disableStandardItemLighting();
                        GL11.glDisable(32826);
                        GL11.glPopMatrix();
                    }
                }
            }
            for (int row = 0; row < recipe.height; ++row) {
                for (int col = 0; col < recipe.width; ++col) {
                    final int itemIndex = row * recipe.width + col;
                    final int renderX = newx + offsetX * col + 1;
                    final int renderY = newy + offsetY * row + 1;
                    this.checkMouseHover(recipe.ingredients[itemIndex], renderX, renderY, 18);
                }
            }
            GL11.glPushMatrix();
            GL11.glScalef(1.5f, 1.5f, 1.5f);
            GL11.glTranslatef(newx / 3.0f + 1.0f, newy / 3.0f - 0.75f, 0.0f);
            GL11.glEnable(32826);
            RenderHelper.enableGUIStandardItemLighting();
            this.itemRenderer.renderItemIntoGUI(
                this.fontRendererObj,
                this.mc.renderEngine,
                recipe.output,
                newx / 3 + 60,
                newy / 3 + 11);
            this.itemRenderer.renderItemOverlayIntoGUI(
                this.fontRendererObj,
                this.mc.renderEngine,
                recipe.output,
                newx / 3 + 60,
                newy / 3 + 11);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(32826);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.checkMouseHover(recipe.output, newx + 90, newy + 20, 25);
            GL11.glPopMatrix();
            ++indexPosition;
            newy += 62;
        }
    }

    private void checkMouseHover(final ItemStack itemstack, final int k, final int l, final int size) {
        float scale = 0.0f;
        switch (this.mc.gameSettings.guiScale) {
            case 1: {
                scale = 4.0f;
                break;
            }
            case 2: {
                scale = 2.0f;
                break;
            }
            case 3: {
                scale = 3.0f;
                break;
            }
            default: {
                scale = 2.0f;
                break;
            }
        }
        final int i = (int) (Mouse.getEventX() / scale);
        final int j = (int) (this.height - Mouse.getEventY() / scale);
        final boolean flag = i >= k && j >= l && i < k + size && j < l + size;
        if (itemstack != null && flag) {
            final String s = itemstack.getItem()
                .getItemStackDisplayName(itemstack);
            if (s.length() > 0) {
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                final int l2 = this.fontRendererObj.getStringWidth(s);
                final int i2 = i - l2 - 4;
                final int k2 = j;
                this.drawGradientRect(i2 - 3, k2 - 3, i2 + l2 + 3, k2 + 8 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawStringWithShadow(s, i2, k2, -1);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }
    }
}
