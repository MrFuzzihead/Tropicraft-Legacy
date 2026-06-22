package net.tropicraft.encyclopedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TropicalBook {

    private File dataFile;
    public static File file;
    private HashMap<String, Byte> visiblePages;
    private HashMap<String, String> pageTitles;
    private HashMap<String, String> pageDescriptions;
    private List<String> sortedPages;
    public String outsideTexture;
    public String insideTexture;

    public TropicalBook(final String savedDataFile, final String contentsFile, final String outsideTex,
        final String insideTex) {
        this.visiblePages = new HashMap<String, Byte>();
        this.pageTitles = new HashMap<String, String>();
        this.pageDescriptions = new HashMap<String, String>();
        this.sortedPages = new ArrayList<String>();
        this.outsideTexture = outsideTex;
        this.insideTexture = insideTex;
        this.dataFile = new File(getClientSidePath(), savedDataFile);
        try {
            if (this.dataFile.canRead()) {
                final InputStream dataInput = new FileInputStream(this.dataFile);
                final NBTTagCompound data = CompressedStreamTools.readCompressed(dataInput);
                for (final Object tagName : data.func_150296_c()) {
                    final Byte b = data.getByte((String) tagName);
                    this.visiblePages.put((String) tagName, b);
                }
                dataInput.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final BufferedReader contents = new BufferedReader(
            new InputStreamReader(TropicalBook.class.getResourceAsStream(contentsFile)));
        try {
            String line;
            while ((line = contents.readLine()) != null) {
                if (!line.contains("=")) {
                    continue;
                }
                final String[] split = line.split("=", 2);
                final String name = split[0].trim();
                final String entry = split[1].trim();
                if (name.toLowerCase()
                    .endsWith(".title")) {
                    this.pageTitles.put(name.substring(0, name.length() - ".title".length()), entry);
                    this.sortedPages.add(name.substring(0, name.length() - ".title".length()));
                } else {
                    if (!name.toLowerCase()
                        .endsWith(".desc")) {
                        continue;
                    }
                    this.pageDescriptions.put(name.substring(0, name.length() - ".desc".length()), entry);
                }
            }
        } catch (IOException ex2) {
            Logger.getLogger(TropicalBook.class.getName())
                .log(Level.SEVERE, null, ex2);
        }
    }

    @SideOnly(Side.CLIENT)
    public static String getClientSidePath() {
        return FMLClientHandler.instance()
            .getClient().mcDataDir.getPath();
    }

    protected void saveData() {
        try {
            this.dataFile.createNewFile();
            if (this.dataFile.canWrite()) {
                final OutputStream dataOutput = new FileOutputStream(this.dataFile);
                final NBTTagCompound data = new NBTTagCompound();
                for (final String s : this.visiblePages.keySet()) {
                    data.setByte(s, (byte) this.visiblePages.get(s));
                }
                CompressedStreamTools.writeCompressed(data, dataOutput);
                dataOutput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TropicalBook.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }

    public boolean hasRecipeList() {
        return false;
    }

    public boolean isPageVisible(final String entry) {
        return this.visiblePages.containsKey(entry) && this.visiblePages.get(entry) > 0;
    }

    public boolean isPageVisible(final int i) {
        return this.isPageVisible(this.getPageName(i));
    }

    public boolean hasPageBeenRead(final String entry) {
        return this.visiblePages.containsKey(entry) && this.visiblePages.get(entry) > 1;
    }

    public boolean hasPageBeenRead(final int i) {
        return this.hasPageBeenRead(this.getPageName(i));
    }

    public void markPageAsNewlyVisible(final String entry) {
        this.visiblePages.put(entry, (byte) 1);
        this.saveData();
    }

    public void markPageAsNewlyVisible(final int i) {
        this.markPageAsNewlyVisible(this.getPageName(i));
    }

    public void markPageAsRead(final String entry) {
        this.visiblePages.put(entry, (byte) 2);
        this.saveData();
    }

    public void markPageAsRead(final int i) {
        this.markPageAsRead(this.getPageName(i));
    }

    public boolean pageExists(final String name) {
        return this.pageTitles.containsKey(name);
    }

    public abstract void updatePagesFromInventory(final InventoryPlayer p0);

    public int getPageCount() {
        return this.sortedPages.size();
    }

    public int getContentPageCount(final int page, final ContentMode mode) {
        return 1;
    }

    public int entriesPerIndexPage() {
        return 12;
    }

    public int entriesPerContentPage(final ContentMode mode) {
        return 1;
    }

    public boolean hasIndexIcons() {
        return false;
    }

    public ItemStack getPageItemStack(final int page) {
        return null;
    }

    protected String getPageName(final int i) {
        if (i >= 0 && i < this.sortedPages.size()) {
            return this.sortedPages.get(i);
        }
        return null;
    }

    public String getPageTitleNotVisible(final int i) {
        return "Page not found";
    }

    private String getPageTitleByName(final String name) {
        if (this.pageExists(name)) {
            return this.pageTitles.get(name);
        }
        return null;
    }

    public String getPageTitleByIndex(final int i) {
        return this.getPageTitleByName(this.getPageName(i));
    }

    private String getPageDescriptionByName(final String name) {
        if (this.pageExists(name)) {
            return this.pageDescriptions.get(name);
        }
        return null;
    }

    public String getPageDescriptionsByIndex(final int i) {
        return this.getPageDescriptionByName(this.getPageName(i));
    }

    protected List<String> getSortedPages() {
        return this.sortedPages;
    }

    static {
        TropicalBook.file = null;
    }

    public enum ContentMode {
        INFO,
        RECIPE;
    }
}
