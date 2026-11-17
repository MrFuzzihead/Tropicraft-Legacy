package net.tropicraft.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.tropicraft.lint.SelfRecursionLint;

import org.junit.Assert;
import org.junit.Test;

/**
 * Fails the build if a method ends up calling its own overload with its own parameters - the issue #54 crash
 * ({@code StackOverflowError} inside {@code RenderEIH#doRender} once 6.2.6 rebuilt the renderer).
 * <p>
 * The compiler cannot catch this, the override is legal and the call is legal; it only blows up when that entity is
 * rendered. It also survives code review because the call looks like delegation. This test runs as part of
 * {@code ./gradlew test} and therefore in the "Build and test" CI workflow on every PR.
 * </p>
 * <p>
 * If a genuine pass-through recursion is ever needed (it essentially never is - add a base case instead), add
 * {@code path/to/File.java#methodName} to {@link #ALLOWED} with a comment explaining why.
 * </p>
 */
public class SelfRecursionGuardTest {

    /** {@code relative/path.java#methodName} entries for reviewed, intentional pass-through recursion. */
    private static final Set<String> ALLOWED = Collections
        .unmodifiableSet(new LinkedHashSet<>(Collections.emptyList()));

    @Test
    public void productionSourcesContainNoSelfDelegatingMethod() throws IOException {
        assertClean("src/main/java");
    }

    @Test
    public void gameTestSourcesContainNoSelfDelegatingMethod() throws IOException {
        assertClean("src/gameTest/java");
    }

    private static void assertClean(final String relativeSourceRoot) throws IOException {
        final Path root = projectRoot().resolve(relativeSourceRoot);
        if (!Files.isDirectory(root)) {
            return;
        }
        final List<SelfRecursionLint.Finding> unreviewed = new ArrayList<>();
        for (final SelfRecursionLint.Finding finding : SelfRecursionLint.scanDirectory(root)) {
            final String key = finding.file + "#" + finding.method;
            if (!ALLOWED.contains(key)) {
                unreviewed.add(finding);
            }
        }
        if (unreviewed.isEmpty()) {
            return;
        }
        final StringBuilder message = new StringBuilder(
            "A method calls itself with its own parameters, so it will recurse until the stack overflows.\n"
                + "Delegate to super (or a differently named helper) instead - see issue #54 / RenderEIH.\n");
        for (final SelfRecursionLint.Finding finding : unreviewed) {
            message.append("  ")
                .append(finding.describe())
                .append('\n');
        }
        Assert.fail(message.toString());
    }

    /** Walks up from the working directory until the Gradle settings file is found. */
    private static Path projectRoot() {
        final String override = System.getProperty("tropicraft.projectRoot");
        if (override != null && !override.isEmpty()) {
            return Paths.get(override);
        }
        Path directory = Paths.get("")
            .toAbsolutePath();
        for (final String marker : Arrays.asList("settings.gradle.kts", "settings.gradle")) {
            Path candidate = directory;
            while (candidate != null) {
                if (Files.isRegularFile(candidate.resolve(marker))) {
                    return candidate;
                }
                candidate = candidate.getParent();
            }
        }
        return directory;
    }
}
