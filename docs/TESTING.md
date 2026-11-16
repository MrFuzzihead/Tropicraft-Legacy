# Testing

Three things are wired up, all of them in `addon.gradle.kts` (the GTNH convention applies it automatically):

| Layer | Where | Command |
|---|---|---|
| JUnit 4 + Mockito unit tests | `src/test/java` | `./gradlew test` |
| In-game test mod (not shipped) | `src/gameTest/java` | `./gradlew gameTestServer` |
| Coverage (unit, in-game, combined) | `build/reports/jacoco/` | `./gradlew jacocoTestReport jacocoCoverageSummary` |

`./gradlew check` runs the unit tests, the coverage floors, spotless and the licence checks, and compiles the game
test mod. It does **not** boot the game; that is a separate command because it needs a server directory and takes
about 30 seconds.

## Unit tests

* JUnit 4.13.2 and Mockito 4.11.0 (`mockito-core` + `mockito-inline`). Mockito 4 is the last line that supports Java
  8, and the `Test` tasks are pinned to a Java 8 toolchain to match what the mod targets: a Java 8 JDK must be
  installed (auto-detected from the usual locations).
* `testCompileClasspath`/`testRuntimeClasspath` extend `compileOnly`, because production classes extend Minecraft and
  Forge types that only exist on that configuration - without it a test cannot even load the class under test.
* Two things about the 1.7.10 runtime are worth knowing before writing a test:
  * `new ItemStack(item)` does not store the item directly. `ItemStack.getItem()` resolves through
    `Item.delegate`, which the `Item` **constructor** populates from the item registry, so `new Item()` behaves like
    a registered item and `Mockito.mock(Item.class)` does not (`delegate` is null, `getItem()` returns null, every
    comparison then matches). Use real `new Item()` instances.
  * `ItemValues.items`/`itemsBuyable` are process-wide state filled at mod init; tests that add entries must restore
    the lists (see `ItemValuesTest`).

Coverage floors live in `addon.gradle.kts`:

* `jacocoTestCoverageVerification` - 0.5 % line coverage over the whole mod. That is the measured baseline, not a
  target; raise it when the suite grows.
* `jacocoEconomyCoverageVerification` - 90 % line / 80 % branch over `net.tropicraft.economy`, the one package with a
  real suite.

Excluded from coverage because they cannot be measured meaningfully headless: `Tags` (generated),
`net.tropicraft.client.**`, `net.tropicraft.mixins.**` and `net.tropicraft.gametest.**`.

## In-game tests

`src/gameTest/java` is a separate, never-shipped `@Mod` (`tropicraftgametest`). It boots a dedicated server, runs its
checks at `FMLServerStartedEvent`, writes the result to `build/gametest/report.txt` and exits. The Gradle task reads
that file rather than the JVM exit code, because FML boots a *crashed* server with status 0 - a run that dies before
reporting fails the build with "the in-game test mod never reported a result".

Prerequisites (once):

```sh
mkdir -p run/server && echo "eula=true" > run/server/eula.txt
```

Then:

```sh
./gradlew gameTestServer
```

Each run starts from a fresh `run/server/world` (the task deletes it first): the checks assert on worldgen state,
and chunks left over from a previous run are already generated and decorated.

What the current checks cover - all of it needs the real game:

| Check | Guards |
|---|---|
| the village schematic resolves, is extracted, and parses through CoroUtil | the P0 where villages silently generated nothing |
| a `TownKoaVillage` queues a build job | the same path end to end |
| the Koa pearl, every shell variant and fish resolve in `ItemValues` | the identity-comparison bug that made trading impossible |
| `EntityDart.getPotionId` bounds-checks unknown types | the `ArrayIndexOutOfBoundsException` from an NBT `dartType` |
| an ocean column does not sand over inland columns | the per-column state leak in `replaceBlocksForBiome` |
| every Tropicraft block registry name is `tropicraft:tile.<name>` | the invariant the village schematic's `blockTranslationMap` depends on |
| every Tropicraft entity has a unique network id and round-trips through the entity registry | accidental renumbering, duplicate registrations |
| a Koa chest fills its household, replaces dead members, and does not re-spawn while its members are out of range | the chest spawning a fresh pair on every visit while the old pair was still alive |
| a tropical fish ticks | the fish state machine after the dead school-spawn path was removed |
| every Tropicraft item/block is named `item.tropicraft:*` / `tile.tropicraft:*` | the item/block naming convention, i.e. every `getUnlocalizedName` in the mod |
| the crafting/smelting recipes involving Tropicraft are unchanged | a count + CRC32 fingerprint of every such recipe, listed in `build/gametest/recipes-*.txt` |
| a creative player's hit does not become a Koa's target / a creative player is not an enemy | target acquisition, including CoroUtil's retaliation path (#50) |
| a passive animal is not a hostile mob | `IMob` classification (spawn caps, peaceful handling) |
| hostile mobs do not survive peaceful | the peaceful rule across all four hostile mob families |
| deferred decoration flushes when its neighbourhood loads | the deferred-decoration gate and the throttled sweep |
| the shutdown flush decorates parked chunks | the shutdown decoration flush, through `ChunkProviderServer.loadChunk` |
| the Tropicraft dimension generates | worldgen smoke test |

To add a check, add a method to `ServerGameTests` and call it from `runAll()`; use the `assertTrue(name, condition,
detail)` helper so the log, the exit code and the report agree. Anything that only needs to be *computed* belongs in
`src/test` instead - it is far cheaper to run.

## Coverage from the game run

The game test run is started with the JaCoCo agent (`addon.gradle.kts`, `-PgameTest` block) which writes:

* `build/jacoco/gameTest.exec` - the coverage data,
* `build/jacoco/gameTest-classes/` - every class the agent instrumented, as `<name>.<classid>.class`.

Those class files are the point: in a Forge dev launch the classes reach the agent *after* FML's transformer chain,
so 253 of the 256 Tropicraft classes differ from the files in `build/classes` (measured on this project). Their
JaCoCo ids therefore do not match the on-disk class files, which is why the in-game data cannot simply be merged into
the unit report - it would match nothing but the three classes FML left alone.

So there are two reports and one summary:

```sh
./gradlew jacocoTestReport        # build/reports/jacoco/test/html/        - unit tests
./gradlew gameTestServer          # also runs jacocoGameTestReport
                                  # build/reports/jacoco/gameTest/html/    - in-game, against the dumped classes
./gradlew jacocoCoverageSummary   # build/reports/jacoco/summary.txt      - unit, in-game and combined
```

`jacocoGameTestReport` normalises the dump (`normalizeGameTestClasses` strips the class id from the file names) and
reports against it. `jacocoCoverageSummary` computes the union of executed source lines, which is the closest thing
to a single "covered by any test" number that two different class-id spaces allow. Snapshot from the P1 batches
(it shrinks when code is deleted; it says `STALE` if `gameTest.exec` is newer than the report it is reading):

```
jacoco coverage (executed source lines)
  unit tests : 233 (1.5%)
  in-game    : 3046 (19.4%)
  combined   : 3223 (20.5%) of 15691 measurable

Note: nothing in this harness renders anything. Renderer changes (`RenderWaterMobBase`) and model/animation changes
(`ModelKoaMan`, `ModelTropiCreeper`, `ModelAshen`) are verified by hand in a client; the checklist for the last such
change is in CODE-REVIEW.md §20.4.
```

The in-game run is most of the coverage because a dedicated server loads and touches a large part of the mod while
starting up. That is real (those lines executed) but also shallow - the useful signal is in the depth of the checks,
which is why the in-game suite asserts behaviour rather than just booting.

## CI

`.github/workflows/build-and-test.yml` runs the GTNH reusable workflow, which runs `check` - so unit tests, the
coverage floors, spotless and the game test compilation gate every PR. The game test itself is not part of CI yet:
it needs a server directory and about half a minute of boot time, so it is worth adding as a separate job before it
is required.
