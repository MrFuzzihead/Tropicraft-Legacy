package net.tropicraft.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import CoroUtil.util.CoroUtilFile;
import cpw.mods.fml.common.FMLLog;

/**
 * Resolves the CoroUtil "build" schematics that Tropicraft's procedural structures are built from (currently
 * the Koa village).
 * <p>
 * CoroUtil's {@code build.world.Build} only reads schematics from disk, at
 * {@code <save folder>/TCSchematics/<name>.schematic}. That path predates the mod being distributed as a jar,
 * so a fresh install never has the file, and {@code Build.readNBT} swallows the resulting
 * {@code FileNotFoundException} behind a {@code printStackTrace} - which is how the Koa villages ended up
 * silently generating nothing at all.
 * <p>
 * The schematics are therefore shipped inside the mod jar under {@code assets/tropicraft/schematics/} and
 * extracted to the location CoroUtil expects, on first use. Extraction happens at most once per JVM and the
 * result is cached; if an extracted copy differs from the shipped one (e.g. after a mod update) it is
 * overwritten.
 * <p>
 * Must only be called while a server is running: {@link CoroUtilFile#getSaveFolderPath()} resolves to the
 * {@code .minecraft} directory in single player and to the working directory on a dedicated server.
 */
public final class TCSchematics {

    /** Directory CoroUtil's {@code Build} class reads schematics from, relative to the save folder. */
    public static final String FOLDER_NAME = "TCSchematics";
    public static final String EXTENSION = ".schematic";

    private static final String ASSET_PATH = "/assets/tropicraft/schematics/";
    private static final int BUFFER_SIZE = 8192;

    private static final Object LOCK = new Object();
    /** name -> base path, or null if the schematic is unusable. Populated once per JVM. */
    private static final Map<String, String> RESOLVED = new HashMap<String, String>();
    /** Names already reported as unusable, so a broken asset is logged once instead of once per attempt. */
    private static final Set<String> REPORTED = Collections.synchronizedSet(new HashSet<String>());

    private TCSchematics() {}

    /**
     * @return the path CoroUtil's {@code Build} expects, without the {@code .schematic} extension, or
     *         {@code null} if the schematic is not available. Callers must handle {@code null} by skipping the
     *         structure rather than constructing a {@code Build} from it.
     */
    public static String getBasePath(final String name) {
        synchronized (LOCK) {
            if (RESOLVED.containsKey(name)) {
                return RESOLVED.get(name);
            }
            final String basePath = CoroUtilFile.getSaveFolderPath() + FOLDER_NAME + File.separator + name;
            final String resolved = extractIfNeeded(name, new File(basePath + EXTENSION)) ? basePath : null;
            RESOLVED.put(name, resolved);
            return resolved;
        }
    }

    private static boolean extractIfNeeded(final String name, final File target) {
        final byte[] data = readResource(name);
        if (data == null) {
            report(name, "the mod jar does not contain a readable " + ASSET_PATH + name + EXTENSION);
            return false;
        }
        if (target.isFile() && Arrays.equals(data, readFile(target))) {
            return true;
        }
        final File dir = target.getParentFile();
        if (dir != null && !dir.isDirectory() && !dir.mkdirs()) {
            report(name, "could not create directory " + dir.getAbsolutePath());
            return false;
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(target);
            out.write(data);
            out.flush();
        } catch (final IOException e) {
            report(name, "could not write " + target.getAbsolutePath() + " (" + e + ")");
            return false;
        } finally {
            close(out);
        }
        FMLLog.info("[Tropicraft] Extracted schematic '%s' to %s", name, target.getAbsolutePath());
        return true;
    }

    private static byte[] readResource(final String name) {
        final InputStream in = TCSchematics.class.getResourceAsStream(ASSET_PATH + name + EXTENSION);
        if (in == null) {
            return null;
        }
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream(16 * 1024);
            final byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } catch (final IOException e) {
            return null;
        } finally {
            close(in);
        }
    }

    /** @return the file's contents, or an empty array if it cannot be read (forcing a re-extract). */
    private static byte[] readFile(final File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            final ByteArrayOutputStream out = new ByteArrayOutputStream((int) Math.min(file.length(), 1024 * 1024));
            final byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } catch (final IOException e) {
            return new byte[0];
        } finally {
            close(in);
        }
    }

    private static void close(final java.io.Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException ignored) {
                // nothing useful to do about it
            }
        }
    }

    private static void report(final String name, final String reason) {
        if (REPORTED.add(name)) {
            FMLLog.severe(
                "[Tropicraft] Schematic '%s' is unavailable: %s. Structures that depend on it will be skipped.",
                name,
                reason);
        }
    }
}
