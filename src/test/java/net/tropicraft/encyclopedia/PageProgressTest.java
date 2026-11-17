package net.tropicraft.encyclopedia;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers {@link PageProgress}, the encyclopedia's per-page unlock state.
 * <p>
 * Two behaviours here are regressions from the old in-line code: {@code markPageAsNewlyVisible} wrote the value
 * unconditionally, so a page the player had already read could be flagged "new" again, and every mark rewrote and
 * recompressed the whole save file on the input path (that part is now {@code TropicalBook}'s debounce, not testable
 * here).
 */
public class PageProgressTest {

    @Test
    public void nothingIsVisibleOrReadByDefault() {
        final PageProgress progress = new PageProgress();
        Assert.assertFalse(progress.isVisible("apple"));
        Assert.assertFalse(progress.hasBeenRead("apple"));
        Assert.assertEquals(0, progress.size());
    }

    @Test
    public void aVisiblePageIsNotReadYet() {
        final PageProgress progress = new PageProgress();
        Assert.assertTrue(progress.markVisible("apple"));
        Assert.assertTrue(progress.isVisible("apple"));
        Assert.assertFalse(progress.hasBeenRead("apple"));
    }

    @Test
    public void markingVisibleTwiceChangesNothingTheSecondTime() {
        final PageProgress progress = new PageProgress();
        progress.markVisible("apple");
        Assert.assertFalse(progress.markVisible("apple"));
        Assert.assertEquals(1, progress.size());
    }

    @Test
    public void aReadPageBecomesRead() {
        final PageProgress progress = new PageProgress();
        progress.markVisible("apple");
        Assert.assertTrue(progress.markRead("apple"));
        Assert.assertTrue(progress.isVisible("apple"));
        Assert.assertTrue(progress.hasBeenRead("apple"));
        Assert.assertFalse(progress.markRead("apple"));
    }

    @Test
    public void aReadPageIsNotDowngradedBackToNew() {
        final PageProgress progress = new PageProgress();
        progress.markVisible("apple");
        progress.markRead("apple");
        Assert.assertFalse("already read, so this is not a change", progress.markVisible("apple"));
        Assert.assertTrue(progress.hasBeenRead("apple"));
    }

    @Test
    public void nullEntriesAreIgnored() {
        final PageProgress progress = new PageProgress();
        Assert.assertFalse(progress.markVisible(null));
        Assert.assertFalse(progress.markRead(null));
        Assert.assertFalse(progress.isVisible(null));
        Assert.assertFalse(progress.hasBeenRead(null));
        Assert.assertEquals(0, progress.size());
    }

    @Test
    public void progressSurvivesAnNbtRoundTrip() {
        final PageProgress written = new PageProgress();
        written.markVisible("apple");
        written.markVisible("pear");
        written.markRead("pear");

        final PageProgress read = new PageProgress();
        read.readFrom(written.writeTo(new NBTTagCompound()));

        Assert.assertEquals(written.size(), read.size());
        Assert.assertTrue(read.isVisible("apple"));
        Assert.assertFalse(read.hasBeenRead("apple"));
        Assert.assertTrue(read.hasBeenRead("pear"));
        Assert.assertFalse(read.isVisible("banana"));
    }

    @Test
    public void readingAnEmptyTagClearsEverything() {
        final PageProgress progress = new PageProgress();
        progress.markRead("apple");
        progress.readFrom(new NBTTagCompound());
        Assert.assertEquals(0, progress.size());
        Assert.assertFalse(progress.isVisible("apple"));
    }

    @Test
    public void readingANullTagClearsEverything() {
        final PageProgress progress = new PageProgress();
        progress.markRead("apple");
        progress.readFrom(null);
        Assert.assertEquals(0, progress.size());
    }

    @Test
    public void fileNamesAreSafeAndStable() {
        final String worldName = PageProgress.fileNameFor("sp:My World/2024");
        Assert.assertTrue(worldName, worldName.endsWith(".dat"));
        Assert.assertFalse("must not contain a path separator", worldName.contains("/"));
        Assert.assertFalse("must not contain a space", worldName.contains(" "));
        Assert.assertEquals(worldName, PageProgress.fileNameFor("sp:My World/2024"));
    }

    @Test
    public void fileNamesDoNotCollideWhenSanitisingDoes() {
        // These two differ only by characters that are all replaced with underscores.
        Assert.assertNotEquals(PageProgress.fileNameFor("sp:a b"), PageProgress.fileNameFor("sp:a/b"));
    }
}
