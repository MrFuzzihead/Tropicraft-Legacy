package net.tropicraft.encyclopedia;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Which encyclopedia pages a player has unlocked, and which of those they have read.
 * <p>
 * Deliberately pure: no file I/O and no client classes, so the storage questions (where the file lives, when it is
 * written) stay in {@link TropicalBook} and this part can be unit tested.
 * </p>
 */
public class PageProgress {

    /** Stored value for a page that is unlocked but has not been opened yet. */
    public static final byte VISIBLE = 1;
    /** Stored value for a page that has been opened. */
    public static final byte READ = 2;

    private final Map<String, Byte> pages = new HashMap<String, Byte>();

    public boolean isVisible(final String entry) {
        return this.value(entry) >= VISIBLE;
    }

    public boolean hasBeenRead(final String entry) {
        return this.value(entry) >= READ;
    }

    private byte value(final String entry) {
        if (entry == null) {
            return 0;
        }
        final Byte value = this.pages.get(entry);
        return value == null ? 0 : value;
    }

    /**
     * @return true if this changed anything. A page that has been read is never downgraded to merely visible - the
     *         old code overwrote the value unconditionally, so a second "new item" sighting could re-flag a page the
     *         player had already read.
     */
    public boolean markVisible(final String entry) {
        if (entry == null || this.value(entry) >= VISIBLE) {
            return false;
        }
        this.pages.put(entry, VISIBLE);
        return true;
    }

    /** @return true if this changed anything. */
    public boolean markRead(final String entry) {
        if (entry == null || this.value(entry) >= READ) {
            return false;
        }
        this.pages.put(entry, READ);
        return true;
    }

    public int size() {
        return this.pages.size();
    }

    /** Replaces the current state with the contents of {@code tag} (missing or empty tags mean "nothing unlocked"). */
    public void readFrom(final NBTTagCompound tag) {
        this.pages.clear();
        if (tag == null) {
            return;
        }
        for (final Object key : tag.func_150296_c()) {
            this.pages.put((String) key, tag.getByte((String) key));
        }
    }

    public NBTTagCompound writeTo(final NBTTagCompound tag) {
        for (final Map.Entry<String, Byte> entry : this.pages.entrySet()) {
            tag.setByte(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    /**
     * A stable, filesystem-safe file name for a world key such as {@code sp:New World}. Sanitising alone can map
     * different keys onto the same name, so the key's hash is appended.
     */
    public static String fileNameFor(final String worldKey) {
        final StringBuilder safe = new StringBuilder();
        for (int i = 0; i < worldKey.length(); ++i) {
            final char c = worldKey.charAt(i);
            safe.append(Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == '_' ? c : '_');
        }
        return safe + "-" + Integer.toHexString(worldKey.hashCode()) + ".dat";
    }
}
