package net.tropicraft.encyclopedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.integrated.IntegratedServer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The shared state and content behind one encyclopedia.
 * <p>
 * Progress (which pages are unlocked, which have been read) lives in {@link PageProgress} and is stored per world
 * under {@code <mcDataDir>/tropicraft/encyclopedia/}: it used to be a single {@code eTsave.dat} in the game directory,
 * shared by every save and every server, read the book in one world and it was unlocked in all of them. Writes are
 * debounced - the old code rewrote and recompressed the whole file on <em>every</em> page reveal, on the input path.
 * </p>
 */
public abstract class TropicalBook {

    /** Writes are coalesced over this window; the client tick flushes anything still dirty. */
    private static final long SAVE_DEBOUNCE_MILLIS = 2000L;

    private final String legacyFileName;
    private final String contentsFile;

    /** Progress for {@link #boundWorldKey}, or an empty state when not in a world. */
    private PageProgress progress = new PageProgress();
    /** World the current {@link #progress} belongs to, or null when it has not been bound yet. */
    private String boundWorldKey;
    private boolean dirty;
    private long lastWriteMillis;

    private final HashMap<String, String> pageTitles = new HashMap<String, String>();
    private final HashMap<String, String> pageDescriptions = new HashMap<String, String>();
    private final List<String> sortedPages = new ArrayList<String>();

    public String outsideTexture;
    public String insideTexture;

    public TropicalBook(final String savedDataFile, final String contentsFile, final String outsideTex,
        final String insideTex) {
        this.legacyFileName = savedDataFile;
        this.contentsFile = contentsFile;
        this.outsideTexture = outsideTex;
        this.insideTexture = insideTex;
        this.readContents(contentsFile);
    }

    private void readContents(final String contentsFile) {
        final InputStream stream = TropicalBook.class.getResourceAsStream(contentsFile);
        if (stream == null) {
            FMLLog.severe(
                "[Tropicraft] Encyclopedia contents '%s' is missing from the jar; the book will have no pages.",
                contentsFile);
            return;
        }
        try {
            final BufferedReader contents = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            try {
                String line;
                while ((line = contents.readLine()) != null) {
                    if (!line.contains("=")) {
                        continue;
                    }
                    final String[] split = line.split("=", 2);
                    final String name = split[0].trim();
                    final String entry = split[1].trim();
                    final String lower = name.toLowerCase();
                    if (lower.endsWith(".title")) {
                        final String page = name.substring(0, name.length() - ".title".length());
                        this.pageTitles.put(page, entry);
                        this.sortedPages.add(page);
                    } else if (lower.endsWith(".desc")) {
                        this.pageDescriptions.put(name.substring(0, name.length() - ".desc".length()), entry);
                    }
                }
            } finally {
                contents.close();
            }
        } catch (final IOException e) {
            FMLLog.severe("[Tropicraft] Could not read encyclopedia contents '%s': %s", contentsFile, e);
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Per-world progress
    // ---------------------------------------------------------------------------------------------------------

    /**
     * The progress for the world that is currently open, binding (and flushing/loading) on a world change.
     */
    private PageProgress progress() {
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            this.bindToCurrentWorld();
        }
        return this.progress;
    }

    @SideOnly(Side.CLIENT)
    private void bindToCurrentWorld() {
        final String key = currentWorldKey();
        if (key == null || key.equals(this.boundWorldKey)) {
            // Not in a world (main menu): keep whatever was loaded, but there is nowhere to write it.
            return;
        }
        this.flush();
        this.boundWorldKey = key;
        this.progress = new PageProgress();
        this.dirty = false;
        final File file = progressFile(key);
        if (file.isFile()) {
            this.readProgress(file);
        } else {
            this.migrateLegacyProgress(file);
        }
    }

    /** @return a stable per-world key, or null when no world is open. */
    @SideOnly(Side.CLIENT)
    private static String currentWorldKey() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.theWorld == null) {
            return null;
        }
        final IntegratedServer integrated = minecraft.getIntegratedServer();
        if (integrated != null) {
            return "sp:" + integrated.getFolderName();
        }
        final ServerData server = minecraft.func_147104_D();
        // A client cannot know which world it is playing on a server, so progress there is per server address.
        return "mp:" + (server == null ? "unknown" : server.serverIP);
    }

    @SideOnly(Side.CLIENT)
    private static File progressFile(final String worldKey) {
        return new File(progressDirectory(), PageProgress.fileNameFor(worldKey));
    }

    @SideOnly(Side.CLIENT)
    private static File progressDirectory() {
        return new File(
            FMLClientHandler.instance()
                .getClient().mcDataDir,
            "tropicraft" + File.separator + "encyclopedia");
    }

    @SideOnly(Side.CLIENT)
    private void readProgress(final File file) {
        try {
            final InputStream input = new FileInputStream(file);
            try {
                this.progress.readFrom(CompressedStreamTools.readCompressed(input));
            } finally {
                input.close();
            }
        } catch (final IOException e) {
            FMLLog.severe("[Tropicraft] Could not read encyclopedia progress from %s: %s", file, e);
        }
    }

    /**
     * One-time import of the old shared {@code eTsave.dat}: the first world opened after the update inherits it, and
     * the old file is renamed so the next world does not inherit it as well.
     */
    @SideOnly(Side.CLIENT)
    private void migrateLegacyProgress(final File target) {
        final File legacy = new File(
            FMLClientHandler.instance()
                .getClient().mcDataDir,
            this.legacyFileName);
        if (!legacy.isFile()) {
            return;
        }
        this.readProgress(legacy);
        this.dirty = true;
        this.flush();
        final File renamed = new File(legacy.getParentFile(), legacy.getName() + ".imported");
        if (legacy.renameTo(renamed)) {
            FMLLog.info(
                "[Tropicraft] Imported the old shared encyclopedia progress into %s (the old file is now %s).",
                target.getName(),
                renamed.getName());
        } else {
            FMLLog.warning(
                "[Tropicraft] Imported the old shared encyclopedia progress but could not rename %s; delete it so the "
                    + "next world does not import it too.",
                legacy);
        }
    }

    /** Writes the progress if it has changed and the debounce window has passed. Called from the client tick. */
    public void flushIfDirty() {
        if (!this.dirty || System.currentTimeMillis() - this.lastWriteMillis < SAVE_DEBOUNCE_MILLIS) {
            return;
        }
        this.flush();
    }

    /** Writes the progress if it has changed, immediately. */
    public void flush() {
        if (!this.dirty) {
            return;
        }
        if (!FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            return;
        }
        this.writeProgress();
    }

    @SideOnly(Side.CLIENT)
    private void writeProgress() {
        this.dirty = false;
        this.lastWriteMillis = System.currentTimeMillis();
        if (this.boundWorldKey == null) {
            // Nothing to attach the progress to yet; it will be written when a world binds.
            this.dirty = true;
            return;
        }
        final File file = progressFile(this.boundWorldKey);
        final File directory = file.getParentFile();
        if (directory != null && !directory.isDirectory() && !directory.mkdirs()) {
            FMLLog.severe("[Tropicraft] Could not create encyclopedia progress directory %s", directory);
            return;
        }
        try {
            final FileOutputStream output = new FileOutputStream(file);
            try {
                CompressedStreamTools.writeCompressed(this.progress.writeTo(new NBTTagCompound()), output);
            } finally {
                output.close();
            }
        } catch (final IOException e) {
            FMLLog.severe("[Tropicraft] Could not write encyclopedia progress to %s: %s", file, e);
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Pages
    // ---------------------------------------------------------------------------------------------------------

    public boolean hasRecipeList() {
        return false;
    }

    public boolean isPageVisible(final String entry) {
        return this.progress()
            .isVisible(entry);
    }

    public boolean isPageVisible(final int i) {
        return this.isPageVisible(this.getPageName(i));
    }

    public boolean hasPageBeenRead(final String entry) {
        return this.progress()
            .hasBeenRead(entry);
    }

    public boolean hasPageBeenRead(final int i) {
        return this.hasPageBeenRead(this.getPageName(i));
    }

    public void markPageAsNewlyVisible(final String entry) {
        if (this.progress()
            .markVisible(entry)) {
            this.dirty = true;
            this.flushIfDirty();
        }
    }

    public void markPageAsNewlyVisible(final int i) {
        this.markPageAsNewlyVisible(this.getPageName(i));
    }

    public void markPageAsRead(final String entry) {
        if (this.progress()
            .markRead(entry)) {
            this.dirty = true;
            this.flushIfDirty();
        }
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

    /** Placeholder title for a page the player has not unlocked; the entry's name must not leak. */
    public String getHiddenPageTitle() {
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

    public enum ContentMode {
        INFO,
        RECIPE;
    }
}
