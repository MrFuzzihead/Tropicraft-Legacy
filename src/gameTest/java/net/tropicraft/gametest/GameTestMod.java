package net.tropicraft.gametest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.minecraft.server.MinecraftServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

/**
 * Test-only mod: a separate, non-shipped {@code @Mod} that asserts Tropicraft works against the real game.
 * <p>
 * It lives in the {@code gameTest} source set and is only put on the classpath by {@code ./gradlew gameTestServer}
 * (or {@code -PgameTest}), so it never reaches the published jar. Run it with:
 *
 * <pre>
 * mkdir -p run/server &amp;&amp; echo "eula=true" &gt; run/server/eula.txt
 * ./gradlew gameTestServer
 * </pre>
 * <p>
 * The assertions run on the server thread at {@link FMLServerStartedEvent}, where the overworld and the Tropicraft
 * dimension exist and block mutations are legal. The result is both printed and written to the file named by
 * {@code -Dtropicraft.gameTest.report}, and the Gradle task reads that file: the JVM exit code is not a reliable
 * signal (FML boots a crashed server with status 0), and a run that never reaches this event must fail the build.
 * </p>
 * <p>
 * It only runs when the {@code tropicraft.gameTest} system property is set, which the Gradle wiring does: if the
 * class is ever left on a development classpath it must not terminate a server somebody is actually using.
 * </p>
 */
@Mod(
    modid = GameTestMod.MOD_ID,
    name = "Tropicraft Game Tests",
    version = "1.0",
    acceptableRemoteVersions = "*",
    dependencies = "required-after:tropicraft")
public class GameTestMod {

    public static final String MOD_ID = "tropicraftgametest";
    static final String PROPERTY = "tropicraft.gameTest";
    static final String REPORT_PROPERTY = "tropicraft.gameTest.report";
    static final String OK_MARKER = "TROPICRAFT_GAMETEST_OK";
    static final String FAIL_MARKER = "TROPICRAFT_GAMETEST_FAILED";

    private boolean ran;

    @Mod.EventHandler
    public void onServerStarted(final FMLServerStartedEvent event) {
        if (this.ran) {
            return;
        }
        this.ran = true;
        if (!"true".equals(System.getProperty(PROPERTY))) {
            System.out.println("[TC-GAMETEST] not running: -D" + PROPERTY + "=true was not set");
            return;
        }
        if (MinecraftServer.getServer() == null) {
            finish("ERROR no server instance", 1);
            return;
        }

        int failures;
        int checks;
        try {
            failures = ServerGameTests.runAll();
            checks = ServerGameTests.checkCount();
        } catch (final Throwable t) {
            t.printStackTrace();
            finish("ERROR " + t, 1);
            return;
        }

        System.out.println("[TC-GAMETEST] " + (checks - failures) + "/" + checks + " checks passed");
        if (failures > 0) {
            finish("FAILED " + failures + " of " + checks + " checks", 1);
        } else {
            finish("OK " + checks + " checks", 0);
        }
    }

    private static void finish(final String result, final int status) {
        if (status == 0) {
            System.out.println(OK_MARKER + " (" + result + ")");
        } else {
            System.out.println(FAIL_MARKER + ": " + result);
        }
        writeReport(result);
        // halt=false: let FML shut the world down, which flushes the JaCoCo dump on exit.
        FMLCommonHandler.instance()
            .exitJava(status, false);
    }

    private static void writeReport(final String result) {
        final String path = System.getProperty(REPORT_PROPERTY);
        if (path == null) {
            return;
        }
        try {
            final File file = new File(path);
            final File parent = file.getParentFile();
            if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
                System.out.println("[TC-GAMETEST] could not create " + parent);
                return;
            }
            final Writer writer = new FileWriter(file);
            try {
                writer.write(result + "\n");
            } finally {
                writer.close();
            }
        } catch (final IOException e) {
            System.out.println("[TC-GAMETEST] could not write the report file: " + e);
        }
    }
}
