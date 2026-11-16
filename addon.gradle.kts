// Extra build configuration for the GTNH/RFG build.
//
// Tropicraft has no third party libraries to embed, so this file is entirely the test harness:
//
//   * JUnit + Mockito unit tests in `src/test/java` with JaCoCo coverage (`./gradlew test jacocoTestReport`)
//   * a non-shipped in-game test mod in `src/gameTest/java` (`./gradlew gameTestServer`)
//   * coverage from that in-game run, both merged into the unit report where the class files still match and
//     reported on their own against the class files the agent actually saw.
//
// It mirrors the harness in MrFuzzihead/PeripheralsPlusPlus and MrFuzzihead/OpenPeripheral.

import java.math.BigDecimal

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.process.CommandLineArgumentProvider
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Attaches the JaCoCo agent to a forked game.
 *
 * <p>
 * The `Test` tasks get the agent from the jacoco plugin; a `JavaExec` run task needs it as an explicit
 * `-javaagent` argument. The agent jar is declared as a `@Classpath` input so Gradle resolves it with the task (in
 * configuration-cache terms this is a task input, not a configuration-time resolution).
 * </p>
 */
abstract class JacocoAgentArgumentProvider : CommandLineArgumentProvider {

    @get:Classpath
    abstract val agent: ConfigurableFileCollection

    @get:Input
    abstract val destFile: Property<String>

    @get:Input
    abstract val includes: Property<String>

    /**
     * Where the agent writes every class file it instrumented, exactly as it saw them. In a Forge dev launch the
     * classes reach the agent after FML's transformer chain, so these are not necessarily the files in
     * `build/classes` - reporting the in-game exec file against these is what makes that data usable.
     */
    @get:Input
    abstract val classDumpDir: Property<String>

    override fun asArguments(): Iterable<String> {
        val jar = extractAgent()
        return listOf(
            "-javaagent:" + jar.absolutePath +
                "=destfile=" + destFile.get() +
                ",append=false,dumponexit=true" +
                ",includes=" + includes.get() +
                ",classdumpdir=" + classDumpDir.get()
        )
    }

    /**
     * The `jacocoAgent` configuration resolves to the JaCoCo *distribution* jar, which has no `Premain-Class` of its
     * own: the real agent is `jacocoagent.jar` inside it. Gradle's own JaCoCo support unpacks it the same way.
     */
    private fun extractAgent(): java.io.File {
        val distribution = agent.files.single()
        val target = java.io.File(destFile.get()).parentFile.resolve("jacocoagent.jar")
        java.util.zip.ZipFile(distribution).use { zip ->
            val entry = zip.getEntry("jacocoagent.jar") ?: return distribution
            target.parentFile?.mkdirs()
            zip.getInputStream(entry).use { input -> target.outputStream().use { output -> input.copyTo(output) } }
        }
        return target
    }
}

// ---------------------------------------------------------------------------------------------------------
// Unit tests + coverage
// ---------------------------------------------------------------------------------------------------------

apply(plugin = "jacoco")

project.extensions.configure(JacocoPluginExtension::class.java) {
    toolVersion = "0.8.16"
}

tasks.withType<Test>().configureEach {
    extensions.configure(JacocoTaskExtension::class.java) {
        isEnabled = true
    }
    // The mod targets Java 8 bytecode and runs on Java 8 clients; run the suite on the same generation. Mockito 4's
    // inline mock maker also behaves best there.
    javaLauncher.set(
        project.extensions.getByType(JavaToolchainService::class.java).launcherFor {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    )
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

// Tests need the compileOnly dependencies too: production classes extend Minecraft/Forge types that only live on the
// compileOnly classpath, so a test cannot even name those classes without it.
val mainSourceSet = project.extensions.getByType(SourceSetContainer::class.java).getByName("main")
configurations.named("testCompileClasspath") {
    extendsFrom(configurations.named("compileOnly").get())
}
configurations.named("testRuntimeClasspath") {
    extendsFrom(configurations.named("compileOnly").get())
}

val gameTestExecFile = project.layout.buildDirectory.file("jacoco/gameTest.exec")
val gameTestClassDump = project.layout.buildDirectory.dir("jacoco/gameTest-classes")
val gameTestReportFile = project.layout.buildDirectory.file("gametest/report.txt")
val unitExecFile = project.layout.buildDirectory.file("jacoco/test.exec")

/**
 * Everything that is not meaningfully testable headless, or that is not Tropicraft's code at all:
 * the generated version holder, the client rendering layer, the mixin classes (they only do anything inside a
 * transformed vanilla class) and the test mod itself.
 */
fun jacocoExcludes() = listOf(
    "net/tropicraft/Tags.class",
    "net/tropicraft/client/**",
    "net/tropicraft/mixins/**",
    "net/tropicraft/gametest/**"
)

fun jacocoClassDirs() = project.files(mainSourceSet.output.classesDirs).asFileTree.matching {
    exclude(jacocoExcludes())
}

// Unit tests only. A game test run cannot be merged in: measured on this project, 253 of the 256 Tropicraft classes
// the agent instrumented in a dev launch differ from the files in build/classes, because FML round-trips every class
// through ASM on the way in, so the class ids in gameTest.exec do not match the class files on disk. The in-game
// number is produced by `jacocoGameTestReport` instead, against the class files the agent actually saw.
tasks.named<JacocoReport>("jacocoTestReport") {
    group = "verification"
    description = "Generates HTML and XML JaCoCo coverage reports from the unit test run"
    // The exec file is an output of `test`; Gradle requires the dependency to be declared explicitly because the
    // data is passed as a plain file collection rather than through the test task's jacoco extension.
    dependsOn(tasks.named("test"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(jacocoClassDirs())
    sourceDirectories.setFrom(mainSourceSet.allJava.srcDirs)
    executionData.setFrom(project.files(unitExecFile).filter { it.exists() })
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    group = "verification"
    description = "Fails the build when unit test coverage drops below the configured floor"
    dependsOn(tasks.named("test"))

    classDirectories.setFrom(jacocoClassDirs())
    sourceDirectories.setFrom(mainSourceSet.allJava.srcDirs)
    // Unit tests only: the floor has to be deterministic, and it is the suite that is expected to grow.
    executionData.setFrom(project.files(unitExecFile).filter { it.exists() })

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                // A regression guard at the current baseline (76 of 14384 measurable lines), not a target. Raise it
                // whenever the unit suite grows; the in-game coverage is reported separately.
                minimum = BigDecimal("0.005")
            }
        }
    }
}

// The one package with a real suite, held to a real floor. Separate task so the rule applies to the economy
// classes only - a BUNDLE rule with an `includes` filter would silently pass if the pattern stopped matching.
val economyCoverageVerification = tasks.register<JacocoCoverageVerification>("jacocoEconomyCoverageVerification") {
    group = "verification"
    description = "Fails the build when the Koa economy package drops below its coverage floor"
    dependsOn(tasks.named("test"))

    classDirectories.setFrom(
        project.files(mainSourceSet.output.classesDirs).asFileTree.matching {
            include("net/tropicraft/economy/**")
        }
    )
    sourceDirectories.setFrom(mainSourceSet.allJava.srcDirs)
    executionData.setFrom(project.files(unitExecFile).filter { it.exists() })

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal("0.90")
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = BigDecimal("0.80")
            }
        }
    }
}

// `check` runs the tests and enforces the floors, so CI catches coverage regressions.
tasks.named("check").configure {
    dependsOn(tasks.named("jacocoTestCoverageVerification"), economyCoverageVerification)
}

// ---------------------------------------------------------------------------------------------------------
// In-game test mod (never shipped)
// ---------------------------------------------------------------------------------------------------------

val gameTestSourceSet = project.extensions.getByType(SourceSetContainer::class.java).create("gameTest")

configurations.getByName(gameTestSourceSet.compileClasspathConfigurationName)
    .extendsFrom(configurations.getByName("compileClasspath"))
configurations.getByName(gameTestSourceSet.runtimeClasspathConfigurationName)
    .extendsFrom(configurations.getByName("runtimeClasspath"))
gameTestSourceSet.compileClasspath += mainSourceSet.output
gameTestSourceSet.compileClasspath += mainSourceSet.compileClasspath
gameTestSourceSet.runtimeClasspath += mainSourceSet.output
gameTestSourceSet.runtimeClasspath += mainSourceSet.runtimeClasspath
// The test mod is plain Java 8. Leaving Jabel's compiler plugin on this source set's classpath makes javac auto-load
// it through ServiceLoader, which then needs ByteBuddy's agent (not present here).
gameTestSourceSet.compileClasspath = gameTestSourceSet.compileClasspath
    .filter { !it.name.contains("jabel") }

val isGameTestRun = project.hasProperty("gameTest") ||
    gradle.startParameter.taskNames.any { it.contains("gameTestServer") }

if (isGameTestRun) {
    // Only Tropicraft's own package is instrumented, and the class dump records the exact bytes the agent saw, which
    // is what makes the in-game exec file reportable at all (see jacocoGameTestReport).
    val jacocoProvider = objects.newInstance(JacocoAgentArgumentProvider::class.java)
    jacocoProvider.agent.from(configurations.named("jacocoAgent"))
    jacocoProvider.destFile.set(
        gameTestExecFile.map { it.asFile.absolutePath }
    )
    jacocoProvider.classDumpDir.set(
        gameTestClassDump.map { it.asFile.absolutePath }
    )
    jacocoProvider.includes.set("net.tropicraft.*")

    tasks.withType<JavaExec>().configureEach {
        if (name.startsWith("runServer") || name.startsWith("runClient")) {
            classpath += gameTestSourceSet.output
            systemProperty("tropicraft.gameTest", "true")
            // Captured as plain strings: a closure that reads a script property makes the task hold a reference to
            // the build script, which the configuration cache refuses to serialise.
            val reportPath = gameTestReportFile.get().asFile.absolutePath
            systemProperty("tropicraft.gameTest.report", reportPath)
            // Clear any previous result before the game starts, so a run that dies early cannot read as a pass.
            doFirst { java.io.File(reportPath).delete() }
            // A reused dev world can reference blocks from mods that are no longer installed; the game test is not
            // about that, so answer FML's "continue anyway?" prompt up front instead of blocking on stdin.
            systemProperty("fml.queryResult", "confirm")
            jvmArgumentProviders.add(jacocoProvider)
        }
    }
}

tasks.named("check").configure {
    dependsOn(gameTestSourceSet.classesTaskName)
}

// ---------------------------------------------------------------------------------------------------------
// In-game coverage report
// ---------------------------------------------------------------------------------------------------------
// `gameTestServer` writes build/jacoco/gameTest.exec plus the class files the agent instrumented. Reporting against
// those is the accurate in-game number - and the only one that works, since FML's transformer chain rewrites the
// classes on the way in (measured here: 253 of 256 differ from build/classes), so the ids in the exec file cannot
// match the class files on disk.
//
// JaCoCo's `classdumpdir` names each file `<name>.<classid>.class`, which a report cannot read, so the dump is
// copied with the id stripped out first.
val normalizeGameTestClasses = tasks.register<Sync>("normalizeGameTestClasses") {
    group = "verification"
    description = "Copies the agent's class dump into the layout a JaCoCo report expects"
    from(gameTestClassDump) {
        include("net/tropicraft/**/*.class")
        rename("(.+)\\.[0-9a-f]+\\.class", "$1.class")
    }
    into(project.layout.buildDirectory.dir("jacoco/gameTest-classes-normalized"))
    // The same class can be defined twice with different bytes (two loaders, or a redefinition), which leaves
    // two files on the same name once the id is stripped. The report only needs one of them.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<JacocoReport>("jacocoGameTestReport") {
    group = "verification"
    description = "Reports coverage from the in-game test run, using the class files the agent instrumented"
    dependsOn(normalizeGameTestClasses)
    // No `onlyIf` closure: an empty execution-data collection makes JaCoCo skip the task, and keeps the
    // configuration cache happy.
    executionData.setFrom(project.files(gameTestExecFile).filter { it.exists() })

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
        xml.outputLocation.set(project.layout.buildDirectory.file("reports/jacoco/gameTest/jacocoGameTestReport.xml"))
        html.outputLocation.set(project.layout.buildDirectory.dir("reports/jacoco/gameTest/html"))
    }

    classDirectories.setFrom(
        project
            .files(project.layout.buildDirectory.dir("jacoco/gameTest-classes-normalized"))
            .asFileTree
            .matching { exclude(jacocoExcludes()) }
    )
    sourceDirectories.setFrom(mainSourceSet.allJava.srcDirs)
}

tasks.register("gameTestServer") {
    group = "verification"
    description = "Boots a dedicated server, runs the in-game test mod, and exits with its result"
    val reportFile = gameTestReportFile.get().asFile
    doLast {
        if (!reportFile.isFile) {
            throw GradleException(
                "The in-game test mod never reported a result. The server most likely crashed during startup - " +
                    "see run/server/logs/latest.log and run/server/crash-reports/."
            )
        }
        val result = reportFile.readText().trim()
        if (!result.startsWith("OK")) {
            throw GradleException("In-game tests failed: $result (see the [TC-GAMETEST] lines in the log)")
        }
        logger.lifecycle("[TC-GAMETEST] $result")
    }
    if (isGameTestRun && tasks.names.contains("runServer")) dependsOn(tasks.named("runServer"))
    finalizedBy(tasks.named("jacocoGameTestReport"))
}

// ---------------------------------------------------------------------------------------------------------
// Combined coverage summary
// ---------------------------------------------------------------------------------------------------------
// The two runs cannot share one JaCoCo report: a dev launch rewrites Tropicraft's classes on the way in (measured:
// 253 of 256 differ from build/classes), so the class ids in the two exec files belong to different class files.
// What can be combined is the outcome - "was this source line executed by any run" - which is what this prints.
tasks.register("jacocoCoverageSummary") {
    group = "verification"
    description = "Prints unit, in-game and combined (union of executed lines) coverage"
    // Never read a stale unit report: jacocoTestReport depends on `test`. The in-game report is only refreshed by
    // gameTestServer, so its age is reported below rather than silently trusted.
    dependsOn(tasks.named("jacocoTestReport"))

    val unitXmlPath = project.layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml")
        .get().asFile.absolutePath
    val gameXmlPath = project.layout.buildDirectory.file("reports/jacoco/gameTest/jacocoGameTestReport.xml")
        .get().asFile.absolutePath
    val summaryPath = project.layout.buildDirectory.file("reports/jacoco/summary.txt").get().asFile.absolutePath
    val gameTestExecPath = gameTestExecFile.get().asFile.absolutePath

    doLast {
        // Local functions and only plain captured values, so the action does not reference the build script.
        // Each source file maps to "executable lines" and "lines executed", both as bit sets of line numbers.
        fun readPerSourceFile(file: java.io.File): Map<String, Pair<java.util.BitSet, java.util.BitSet>> {
            val result = HashMap<String, Pair<java.util.BitSet, java.util.BitSet>>()
            if (!file.isFile) {
                return result
            }
            val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
            // JaCoCo's XML carries a DOCTYPE pointing at report.dtd; resolving it would need that file next to the
            // report, and the report parses fine without it.
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false)
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false)
            factory.isExpandEntityReferences = false
            val document = factory.newDocumentBuilder().parse(file)
            val sourceFiles = document.getElementsByTagName("sourcefile")
            for (i in 0 until sourceFiles.length) {
                val sourceFile = sourceFiles.item(i) as org.w3c.dom.Element
                val entry = result.getOrPut(sourceFile.getAttribute("name")) {
                    Pair(java.util.BitSet(), java.util.BitSet())
                }
                val lines = sourceFile.getElementsByTagName("line")
                for (j in 0 until lines.length) {
                    val line = lines.item(j) as org.w3c.dom.Element
                    val nr = line.getAttribute("nr").toIntOrNull() ?: continue
                    val missed = line.getAttribute("mi").toIntOrNull() ?: 0
                    val covered = line.getAttribute("ci").toIntOrNull() ?: 0
                    if (missed + covered > 0) {
                        entry.first.set(nr)
                    }
                    if (covered > 0) {
                        entry.second.set(nr)
                    }
                }
            }
            return result
        }

        fun percent(value: Int, total: Int) =
            if (total == 0) "n/a" else String.format(java.util.Locale.ROOT, "%.1f%%", 100.0 * value / total)

        val unit = readPerSourceFile(java.io.File(unitXmlPath))
        val game = readPerSourceFile(java.io.File(gameXmlPath))
        val gameXmlFile = java.io.File(gameXmlPath)
        val gameExecFile = java.io.File(gameTestExecPath)
        val gameNote = when {
            game.isEmpty() -> " (no gameTestServer run yet)"
            gameExecFile.isFile && gameExecFile.lastModified() > gameXmlFile.lastModified() -> " (STALE: rerun gameTestServer)"
            else -> ""
        }
        if (unit.isEmpty() && game.isEmpty()) {
            throw GradleException("No coverage reports found; run `test jacocoTestReport` or `gameTestServer` first.")
        }
        val executable = java.util.BitSet()
        val unitCovered = java.util.BitSet()
        val gameCovered = java.util.BitSet()
        val union = java.util.BitSet()
        var offset = 0
        for (name in (unit.keys + game.keys).toSortedSet()) {
            val unitEntry = unit[name]
            val gameEntry = game[name]
            // Line numbers are only meaningful within one file, so give each file its own slice of the bit sets.
            var count = 0
            unitEntry?.first?.let { count = maxOf(count, it.length()) }
            gameEntry?.first?.let { count = maxOf(count, it.length()) }
            unitEntry?.first?.let { bits -> for (k in 0 until bits.length()) if (bits.get(k)) executable.set(offset + k) }
            gameEntry?.first?.let { bits -> for (k in 0 until bits.length()) if (bits.get(k)) executable.set(offset + k) }
            unitEntry?.second?.let { bits -> for (k in 0 until bits.length()) if (bits.get(k)) unitCovered.set(offset + k) }
            gameEntry?.second?.let { bits ->
                for (k in 0 until bits.length()) if (bits.get(k)) {
                    gameCovered.set(offset + k)
                    union.set(offset + k)
                }
            }
            for (k in 0 until count) if (unitCovered.get(offset + k)) union.set(offset + k)
            offset += count + 1
        }
        val total = executable.cardinality()
        val lines = listOf(
            "jacoco coverage (executed source lines)",
            "  unit tests : ${unitCovered.cardinality()} (${percent(unitCovered.cardinality(), total)})",
            "  in-game    : ${gameCovered.cardinality()} (${percent(gameCovered.cardinality(), total)})$gameNote",
            "  combined   : ${union.cardinality()} (${percent(union.cardinality(), total)}) of $total measurable"
        )
        lines.forEach { logger.lifecycle(it) }
        val target = java.io.File(summaryPath)
        target.parentFile?.mkdirs()
        target.writeText(lines.joinToString(java.lang.System.lineSeparator(), postfix = java.lang.System.lineSeparator()))
    }
}
