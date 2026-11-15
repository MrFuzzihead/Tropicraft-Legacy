# Tropicraft-Legacy — Codebase Review

**Scope:** `src/main/java` (380 files, ~40,400 LOC), `src/main/resources`, build files, dependency graph.
**Date:** review of branch `feature-Refactors` @ `55d9983` + working-tree change to `dependencies.gradle`.
**Method:** static read of every package, targeted greps for known 1.7.10 hazards, decompiler-residue scan,
duplicate-method-body scan, and an audit of the CoroUtil dependency (including the cached artifact and the
`CoroUtil-Legacy` fork in the neighbouring workspace).

Priority key: **P0** = broken feature / data loss / crash, **P1** = correctness or measurable perf,
**P2** = dead code & maintenance debt, **P3** = polish / features.

> **Status:** the four P0 items (B1, B2, B3, B4) have been fixed, and a test harness (unit tests, an in-game test
> mod, coverage) now guards them — see §11 and `docs/TESTING.md`. §3 has the revised diagnosis of B1; the original
> write-up attributed the silent village failure to the wrong exception. Three further bugs were found while wiring
> the harness (§11.6).

---

## 1. Executive summary

The mod is in better shape than a typical 1.7.10 decompile: the recent work (deferred chunk decoration,
the Tropics clock/weather mixins, the command rework) is well structured and heavily documented. The debt
is concentrated in three places:

1. **A hard CoroUtil dependency** that reaches into eight CoroUtil subsystems (AI jobs, pathfinding,
   world simulation, a schematic build engine, config, particles, quests, throwables) and whose
   *public API leaks into Tropicraft's own public API*. This is the single biggest architectural
   liability; see §8 and §9.
2. **Decompiler residue everywhere** — 43 obvious redundant aliasing locals of the form
   `final X y2 = this.y;` (plus more that don't fit the pattern), float literals written as doubles
   (`0.10000000149011612`), duplicated render `doRender` bodies, six copies of the same
   `getUnlocalizedName` logic, and a dozen always-true/always-false leftovers that are sometimes *real*
   condition bugs (see B7, B8).
3. **Debug output in production** — 42 `System.out/System.err` sites, a `Tropicraft.dbg()` that is
   hard-wired `consoleDebug = true`, and a shipped `System.out.println("VERIFY THIS CODE IN hookInteract()")`.

The good news: most P0/P1 items are small, local, and independent. A focused two-day pass clears all of
§3 and §4; the refactors in §6 are opportunistic; only the CoroUtil work in §9 is a project.

**Top five things to do first**

| # | Item | Why |
|---|------|-----|
| 1 | Koa villages cannot generate (B1) | Advertised feature is silently dead; CoroUtil's swallowed exception hides it, and #33's guard never fires |
| 2 | `implementation` → `api` for CoroUtil (§8) | Published API references CoroUtil types; downstream builds break |
| 3 | `ChunkProviderTropicraft` `flag`/`sandMetadata` leak across columns (B3) | Wrong blocks written into the world, permanently saved |
| 4 | `EntityDart` potion array + `dartType` bounds (B2) | `ArrayIndexOutOfBoundsException` from an unvalidated NBT value |
| 5 | `ItemEntry.matchesItem` reference equality (B4) | The whole Koa trading economy cannot match a player-held item |

---

## 2. Priority index

| ID | Priority | Area | One-liner |
|----|----------|------|-----------|
| B1 | P0 | worldgen | Koa villages need an unshipped `TCSchematics/koavillage.schematic`; CoroUtil swallows the error, so no village is ever built |
| B2 | P0 | entity | `EntityDart.potions` rebuilt per-instance in `entityInit()`; `dartType` from NBT indexes it unchecked |
| B3 | P0 | worldgen | `replaceBlocksForBiome`: `flag` never reset per column, `sandMetadata` mutated for oceans |
| B4 | P0 | economy | `ItemEntry.matchesItem` compares `ItemStack` by reference |
| B4b | P1 | economy | `ItemValues.getItemEntry` linear scan + blanket catch; unbounded `itemIndex` from NBT |
| B6 | P1 | chunk | `BlockSand.fallInstantly` left `true` on a decoration throw (global side effect) |
| B7 | P1 | AI | `JobFish`: `fish.getHealth() < 0.0f` never true; `rand.nextInt(1) == 0` always true |
| B8 | P1 | entity | `EntityTropicalFish` school spawning loop is empty — schools never spawn |
| B10 | P1 | events | `EffectHelper.tick()` runs twice per tick in single-player (client + server tick events) |
| B11 | P1 | TE | `TileEntityKoaChest`: village quota counts only *loaded* members and `addToList` drops extras → duplicate Koa |
| B12 | P1 | book | Encyclopedia progress is stored in `<.minecraft>/eTsave.dat`, shared across all worlds |
| B9 | P2 | registry | `EntityTropiCreeper` registered twice (silently skipped by FML); mod-local entity IDs are positional |
| B13 | P2 | chunk | `flushPending()` allocates + does 81 `chunkExists` per pending chunk per tick |
| B5 | P2 | util | `TropicraftUtils.getTopWaterBlockY` is an unbounded downward loop (already unreferenced) |
| B14 | P2 | chunk | Provisioning in `flushAllPendingForSave` reaches into `ChunkProviderServer` internals |
| B15 | P2 | misc | 42 `System.out/err`, `Tropicraft.dbg` always on, "VERIFY THIS CODE" shipped |
| B16 | P2 | misc | 43 decompiler alias locals, unused locals/fields, empty `if`s, 21 blanket `catch (Exception)` |
| B17 | P2 | render | `RenderMarlin`/`RenderSeaTurtle`/`RenderWaterMob` each copy ~150 lines of `RenderLiving.doRender` |
| B18 | P2 | items | `getUnlocalizedName`/`getActualName` duplicated in 6 item classes |
| B19 | P2 | code | `TCInfo` unreferenced + hard-coded stale version `v6.0.5` |
| B20 | P2 | code | `TCPacketEvents.onPacketFromClient` is a no-op handler; `getSelfUsername` unused |
| B21 | P3 | build | `mcmod.info` declares no dependency on CoroUtil; dimension id hard-coded `-127` |
| B22 | P3 | feature | Village/quest systems, JEI-style recipe book, config GUI, etc. (see §7) |

Issue-tracker follow-ups (detail in §10):

| ID | Priority | Issue | One-liner |
|----|----------|-------|-----------|
| B23 | P1 | #45 | `ItemCoconutBomb` spends the bomb *before* the whitelist check, so refused players lose it |
| B24 | P1 | #9 | Mahogany leaves' fancy icon is `leafKapok`; `leafMahogany.png` exists and is used for Kapok's fast icon |
| B25 | P2 | #3 | Two duplicate lang keys; `tropiFrame` is labelled "Bamboo Item Frame" in game |
| B26 | P2 | #3, #10 | 152 `TCNames` string constants are never referenced; `aqua*` names exist without any item |
| B27 | P1 | #50 | Koa lose the creative-mode filter; `JobHunt.hookHit` retaliation bypasses `isEnemy`; peaceful handling only exists in `EntityCoroAI` |

---

## 3. P0 — Correctness

### B1. Koa villages cannot generate, and nothing reports it

**Status: fixed.** The 2015 `koavillage.schematic` is now shipped as an asset and extracted on first use, and
both failure modes (missing asset, unreadable asset) now abort the village loudly instead of queueing an empty
build. See §11.1.

`BiomeGenTropicsBeach.decorate` (line 28) calls `TownKoaVillageGenHelper.hookTryGenVillage`, which constructs
a `TownKoaVillage` and calls `genSchematic()`:

```java
// TownKoaVillage.java:61
final Build mainStructureData = new Build(
    this.spawn.posX, this.spawn.posY + yOffset, this.spawn.posZ,
    CoroUtilFile.getSaveFolderPath() + "TCSchematics" + File.separator + "koavillage");
```

`Build`'s constructor calls `readNBT(file)`, which reads
`<save folder>/TCSchematics/koavillage.schematic`. `CoroUtilFile.getSaveFolderPath()` resolves to the
`.minecraft` directory (SP) or the server working directory, so the file it wants is
`<.minecraft>/TCSchematics/koavillage.schematic`. **Nothing in Tropicraft shipped that file**
(`grep -r koavillage` over the repo and over the CoroUtil jar → one hit, the reference above), and there was
no copy-on-first-run logic anywhere. §11.1 adds both.

The failure is *silent*, which is why it has survived: `Build.readNBT` wraps its whole body in
`try { ... } catch (Exception ex) { ex.printStackTrace(); }` (`build/world/Build.java:175-315`). A missing
file therefore prints a stack trace to stderr and returns normally, leaving a `Build` with **null block
arrays and `map_sizeX/Y/Z == 0`**. That zero-size build job is still queued in
`BuildServerTicks.buildMan`, so the village registers itself, its spawn locations and its
`ISimulationTickable`, but **not one block is ever placed**. Nothing throws, so the
`catch (Throwable)` guard added in `39ba3a2` never even fires.

An important consequence: the crash in #30 was `NoSuchMethodError` on `WorldDirector.addTickingLocation`,
which is the *last* statement of `hookTryGenVillage` — proving `initFirstTime()` (and therefore
`new Build(...)`) had already returned normally on the reporter's machine. The CoroUtil API update in #33
fixed a genuine crash, but it could not have fixed village generation, because nothing was throwing.

One caveat worth checking first: the *original* 1.7.10 CurseForge distribution may have shipped
`TCSchematics/` as a folder **beside** the jar (mod distributions of that era sometimes did). If so, the
asset exists and only the *installer* is missing it — the fix is then "recover the original asset and ship
it", which is easier than option 2 below.

Also in that loop: `int y = world.getTopSolidOrLiquidBlock(j, l); if (y < 63) y = 64;` — **`y` is never
used** (leftover from the pre-`39ba3a2` code).

**Fix (pick one):**
1. Ship `<resources>/assets/tropicraft/schematics/koavillage.schematic`, copy it to
   `TCSchematics/koavillage.schematic` on first load (recording the asset's provenance/licence), and treat a
   `Build` whose `map_sizeY == 0` as a hard failure, or
2. Generate villages with Java worldgen (the mod already does this well for home trees, EIH, volcanoes),
   which also drops `build.world.*` from the dependency surface — see §9 Phase 5.

Either way: stop relying on CoroUtil's swallowed exception. Check the file exists before constructing the
`Build` and log **once per session** rather than per chunk.

**Applied:** option 1 (§11.1).

### B2. `EntityDart`: static array initialised per entity, indexed unchecked

**Status: fixed.** See §11.2.

```java
// EntityDart.java:319 — inside entityInit(), an instance method
EntityDart.potions = new int[] { Potion.blindness.id, ... };
```

* `entityInit()` runs for **every** dart created → allocates a 7-element array per projectile, and writes
  a shared static from an instance method (so the array's lifetime and contents are tied to whatever
  entity happened to be constructed last).
* `potions[this.dartType]` (line 199) is indexed with `dartType`, which is `readEntityFromNBT` →
  `getShort("dartType")` with **no range check**. A corrupted or hand-edited NBT tag (or a future dart
  type added to the item without extending the array) throws `ArrayIndexOutOfBoundsException` inside
  entity update — a world-corrupting crash on load.

**Fix:** `private static final int[] POTIONS = {...}` at class level (or better, a `DartType` enum with the
potion and the effect), and clamp/skip unknown types.

Also here: `if (!this.worldObj.isRemote) {}` (line 120, empty), an override of `setDead()` that only calls
`super`, a hard-coded `(short) 200` where `MAX_HIT_TIME` exists, and a `System.out.println` inside the hit
path (line 185).

### B3. `ChunkProviderTropicraft.replaceBlocksForBiome` — state leaks across columns

**Status: fixed.** See §11.3.

```java
// :342-343 — declared once for the whole 16x16 chunk
int a = -1;
boolean flag = false;
for (int l = 0; l < 16; ++l) {
  for (int i1 = 0; i1 < 16; ++i1) {
    ...
    for (int l2 = 127; l2 >= 0; --l2) { ... }
    a = -1;          // reset per column
    // flag is NOT reset
  }
}
```

Vanilla `ChunkProviderGenerate` declares both `flag` and `k` **inside** the per-column loop. Here `flag`
survives into the next column, so a column whose first solid block sits at `y >= 66` inherits the previous
column's "we are under the sea, use sand" state and gets `topBlock`/`fillerBlock` replaced with
`btop` everywhere. Effect: patches of beach/sand where grass/dirt should be, at exactly the
`y >= 66` boundary — and it is written to disk.

Second leak, same method:

```java
if (biome == BiomeGenTropicraft.tropicsOcean) {
    btop = biome.sandBlock;
    sandMetadata = 0;     // :354 — mutates the per-chunk random sand variant
}
```

`sandMetadata` is chosen once per chunk from `rand.nextInt(200)` (0-3 = mineral sands). After the first
ocean column it is permanently `0`, so any land column processed afterwards gets `metas[...] = 0`
regardless of the rolled variant. Also, `metas[i2] = (byte) sandMetadata` is applied to *stone* and
`fillerBlock` writes too, not just the sand surface.

**Fix:** move `a` and `flag` into the `i1` loop, use a local `columnSandMeta` copy, and only write
metadata when the block actually is a sand variant.

### B4. `ItemEntry.matchesItem` — `ItemStack` reference equality

**Status: fixed.** See §11.4.

```java
// economy/ItemEntry.java:23-27
public boolean matchesItem(final ItemStack parItem) {
    return (!this.compareNBT || this.item.isItemEqual(parItem))
        && (!this.compareDamage || this.item.getItemDamage() == parItem.getItemDamage())
        && this.item == parItem;          // <-- identity, not equality
}
```

`this.item` is the prototype stack registered in `ItemValues`; `parItem` is whatever a player is holding.
They are never the same object, so `matchesItem` returns `false` for every real lookup. Downstream:
`JobTrade.hookInteract` refuses to accept any item (`ItemValues.getItemEntry(is) != null` is never true), so
nothing can ever be offered; and if something ever is, `getOfferedItemsValue()` NPEs on the first stack it
cannot match and the `catch (Exception) { printStackTrace(); }` swallows it. **The Koa trade economy is
effectively non-functional, and silently so.**

```java
// economy/ItemEntry.java:29-35
public int getTotalValue(final ItemStack parItem) { ...
    } else if (this.item.stackSize > 1) {}   // empty branch
```

**Fix:** `this.item.getItem() == parItem.getItem()` (plus the existing damage/NBT checks), delete the
empty branch, and make `getTotalValue` total by construction. While in there: `ItemValues.getItemEntry`
is a linear scan wrapped in `try { } catch (Exception) { printStackTrace(); }` — replace with a
`HashMap<Item, List<ItemEntry>>` index and drop the catch.

### B4b. `ItemEntry` / `ItemValues` API hygiene

`ItemValues.items` / `itemsBuyable` are `public static ArrayList` (mutable, no invariants).
`getIsValuedItem`/`getIsBuyableItem`/`getItemEntry` overloads are fine, but `getItemEntry(ItemStack)` has
no null check, and `TileEntityPurchasePlate.onClicked` does
`ItemValues.itemsBuyable.get(this.itemIndex)` with only a wrap-around guard — an out-of-range
`itemIndex` loaded from NBT throws.

---

## 4. P1 — Correctness / performance

### B5. `TropicraftUtils.getTopWaterBlockY` — unbounded loop

```java
// util/TropicraftUtils.java:75
for (y = world.getHeightValue(xCoord, zCoord);
     world.getBlock(xCoord, y, zCoord).getMaterial() != Material.water; --y) {}
```

If there is no water at or below the height map (or `getHeightValue` returns 0), the loop walks to
`y < 0`. `World.getBlock` with a negative Y hits `ExtendedBlockStorage` with a negative index → exception,
or in the best case spins. Currently **unreferenced** (only definition), i.e. already dead code — delete it
or bound it to `y > 0` and return `-1`.

### B6. `BlockSand.fallInstantly` not restored on throw

```java
// ChunkProviderTropicraft.doPopulate:409-421
BlockSand.fallInstantly = true;
...
BlockSand.fallInstantly = false;
} finally {
    --this.decoratingDepth;
```

`BlockSand.fallInstantly` is a **global static** that stops sand/gravel falling in *every* world. The
`finally` restores `decoratingDepth` but not the flag, so one decoration exception disables gravity blocks
for the rest of the session. Move the flag into the `finally` (and capture/restore the previous value).

### B7. `JobFish` — two conditions that can never fire

```java
// :193-196
this.ai.entInv.performLeftClick((Entity) fish, 0.0f);
if (fish.getHealth() < 0.0f) {   // health is never negative
    this.catchFish();
}

// :168
if (this.getFishCount() > 4 || (rand.nextInt(1) == 0 && this.getFishCount() >= 2)) {
```

* `getHealth() < 0.0f` → should be `<= 0.0f` (or `isDead`). As written the Koa fisherman never lands a
  fish by damaging it, which is the only path that reaches the "go home and deposit" state early.
* `rand.nextInt(1) == 0` is always `true` → the second branch always taken.
* `final boolean transferred = false;` (:338) unused; `final AIInventory entInv2 = this.ai.entInv;` (:183)
  and `entInv2` (:302) unused locals; `new Random()` per tick (:87) instead of `this.ent.worldObj.rand`.

### B8. `EntityTropicalFish` — school spawning is an empty loop

```java
// :212-220
if (this.getShouldSpawnSchool()) {
    final int maxInSchool = 7, minInSchool = 4;
    for (int numToSpawn = new Random().nextInt(...) + minInSchool - 1, i = 0; i < numToSpawn; ++i) {
        if (!this.worldObj.isRemote) {}      // <-- nothing happens
    }
    this.setShouldSpawnSchool(false);
}
```

The loop body is empty, so a school never spawns and the datawatcher flag is just cleared. Either
implement the spawn (`new EntityTropicalFish(this)` — the copy-constructor already exists and even
positions itself) or delete the whole flag path. Related, same file/neighbours:
`onFishingUpdate` has `if (this.rand.nextInt(odds) != 0 || !this.worldObj.isRemote) {}` (no-op), the
copy-constructor calls `new Random()` inside a `do { } while (!getCanSpawnHere())` (unbounded retry loop +
allocation per iteration), and `checkForLeader()` is O(n²) over every fish in a 20³ box every tick.

### B9. Entity registry — duplicate entry and positional IDs

```java
// TCEntityRegistry.java:70 and :72
registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);
registerEntity(EntityTropiSkeleton.class, "TropiSkeleton", 80, 3, true);
registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);   // duplicate
```

FML catches this one: `EntityRegistry.doModEntityRegistration` logs *"Skipping automatic mod %s entity
registration for already registered class %s"* at FINE and returns, so the world is not corrupted — the
second line is simply dead, and it still burns an id from `entityID++`. Delete it.

```java
private static int entityID;   // 0,1,2,... in call order
```

This is the real (if slow-burning) problem. The numeric mod entity id is what
`FMLMessage.EntitySpawnMessage` carries and what the client resolves through
`EntityRegistry.lookupModSpawn(ModContainer, id)`, and it is what `EntityRegistry.addSpawn` and
`tryTrackingEntity` key on. Because ids are assigned by *call order*, inserting, removing or reordering a
`registerEntity` line silently re-points every subsequent id. Saved entities themselves are keyed by name
in 1.7.10, so existing worlds survive — but any client/server jar mismatch, any branch that cherry-picks a
registration line, and any future packet that relies on the numeric id will disagree about what an id
means. For a project with multiple forks and PR branches (this one has several), that is a real hazard.

**Fix:** replace the counter with a hand-written, never-reused id constant per entity
(`registerEntity(EntityChair.class, "beachChair", 0, 120, 10, true)`), delete the duplicate line, and add a
comment forbidding reuse. This is a 40-line mechanical change with high long-term value.

### B10. `EffectHelper.tick()` runs twice in single-player

`TCMiscEvents` registers one instance on the Forge bus and the FML bus (`Tropicraft.init`) and ticks
`EffectHelper` from both `ClientTickEvent.END` (:54) and `ServerTickEvent.END` (:67). In single-player the
integrated server and client are the same JVM and both events fire, so `EffectHelper.listEntities` is
iterated twice per tick → effects (dart paralysis) last half as long in SP as on a dedicated server.
Both ticks are legitimate on their respective side, so the fix is to keep **one list per side** (keyed on
`Side`, or check `worldObj.isRemote` when adding an entry) rather than guarding one of the events.

Related: `EffectHelper.listEntities` is a `public static List` (mutable, no null checks),
`EffectEntry.tickServerAI`/`tickClientPlayer` are byte-for-byte duplicates apart from the cast, and
`EffectEntry.init()`/`deinit()` are empty while `deinit()` is also called by `EffectHelper` *and* by
`EffectEntry.tick()`.

### B11. `TileEntityKoaChest.getHomeKoaCount()`

```java
// :110-149
int j = 0;
j = 0;                                   // duplicate
while (j < this.entIDs.length) {
    final Entity ent = (Entity) ents.get(i);
    if (ent instanceof EntityKoaBase && ent != null && ...) {   // null check after instanceof = dead
        ++existing;
        if (ent instanceof EntityKoaHunter) ++this.hunters;
        if (ent instanceof EntityKoaFisher) { ++this.fishers; break; }
        final int dsdf = 0;                  // unused
        break;
    } else { ++j; }
}
if (j < this.entIDs.length) {}           // empty
```

The dead locals are the obvious part; the interesting part is the quota logic. `getHomeKoaCount()` counts
only members that are **currently loaded** (it scans a 160×80×160 AABB), while `spawnKoa()` tops the village
up to `housePop_hunters`/`housePop_fishers`. `addToList()` silently does nothing when both `entIDs` slots are
already taken by members that are merely in unloaded chunks — so the quota stays short, `spawnKoa` keeps
spawning, and the extras are never registered. Worth verifying in game, but the code path allows Koa to
accumulate around a chest.

Also in `TileEntityKoaChest`: `spawnKoa` computes `koaCount` and `spawnCount` and uses neither; `var6` (an
`EntityLiving` cast of a `EntityKoaBase`) can never be `null` yet is checked; `getCanSpawnHere(Entity)`
computes `b2` and never uses it; `readFromNBT`/`writeToNBT` wrap plain `getInteger`/`setInteger` calls in
`catch (Exception) { printStackTrace(); }`.

### B12. Encyclopedia progress is a single file in the game directory

`TropicalBook` writes `eTsave.dat` to `FMLClientHandler.getClient().mcDataDir`. Consequences:

* progress is **shared between all worlds and all saves** (read the book in world A, it's fully unlocked
  in world B);
* a dedicated server / other client never sees it (fine, it's client UI, but undocumented);
* `saveData()` is called on *every* page-visibility change → a compressed NBT file rewritten per page
  reveal, on the render/input path.

**Fix:** store it per-world (e.g. `WorldSavedData` on the client `World`, or a file under
`saves/<world>/tropicraft/`), and debounce writes.

Also in `TropicalBook`: `TropicalBook.class.getResourceAsStream(contentsFile)` is dereferenced without a
null check (a missing/misnamed lang file → NPE at startup), both `FileInputStream`/`FileOutputStream` are
closed manually (no try-with-resources; a throw leaks the handle), `public static File file` is unused,
`getPageTitleNotVisible(int i)` ignores its argument, and it mixes `java.util.logging` with `FMLLog`.

### B13. `ChunkProviderTropicraft.flushPending()` — per-tick sweep

```java
// :442 — called from unloadQueuedChunks(), i.e. every tick
do {
    progress = false;
    for (final ChunkCoordIntPair key : new ArrayList<>(this.pendingDecoration)) {
        if (this.isAreaLoaded(key.chunkXPos, key.chunkZPos, DECOR_RADIUS)) { ... progress = true; }
    }
} while (progress);
```

`isAreaLoaded` for `DECOR_RADIUS = 4` performs 81 `chunkExists` lookups. With *n* pending chunks that is
`81·n` map lookups **per tick**, plus a full `ArrayList` copy per outer iteration, and the outer `do/while`
re-scans the whole set whenever anything succeeded (O(n²) in the worst case).

**Fix:** index pending chunks by the ring they are waiting on — e.g. a `HashMap<ChunkCoordIntPair,
Integer>` counting loaded neighbours, decremented from the chunk-load hook, and only attempt decoration
when the count hits `(2r+1)²`. At minimum: keep the `ArrayList` allocation out of the loop and break out of
the loop early when nothing changed.

### B14. `flushAllPendingForSave()` writes into `ChunkProviderServer` internals

```java
// :517
final Chunk chunk = this.provideChunk(nx, nz);
serverProvider.loadedChunkHashMap.add(ChunkCoordIntPair.chunkXZ2Int(nx, nz), chunk);
serverProvider.loadedChunks.add(chunk);
chunk.onChunkLoad();
```

This bypasses `ChunkProviderServer.loadChunk` (no `chunkLoadOverride`/`loadChunk` event, no queue/unload
accounting, no `Chunk.setChunkModified` bookkeeping), and mutates two public Collections from outside.
It works today but is exactly the kind of thing that breaks under a chunk-loading mod (Chunk Pregenerator,
Thermos forks, etc.). Prefer `serverProvider.loadChunk(nx, nz)` (which is what the deferral was trying to
*avoid*, but only once, at shutdown) or drive it through `ForgeChunkManager` tickets.

Note also `chunkExists()` in this class always returns `true` — correct for a generator, but it means
`isAreaLoaded` is only meaningful because `worldObj.getChunkProvider()` is the wrapper. Worth a comment;
a future refactor that hands the generator itself to `isAreaLoaded` silently disables all deferral.

### B15–B20. Dead code & decompiler residue

* **`TCInfo`** (`info/TCInfo.java`) — **completely unreferenced**, and `VERSION = "v6.0.5"` is stale
  (gradle ships 6.2.x). Delete, or make it read `Tags.VERSION`.
* **`TCPacketEvents.onPacketFromClient`** — parses the packet then `if (packetCommand.equals("")) {}`.
  A registered, authenticated-reachable no-op handler. Delete it and stop registering the channel for
  client→server traffic. `getSelfUsername()` is unused. The client handler prints every packet command to
  stdout.
* **`TCNames`** — 200 lines of `public static final String` plus arrays; nothing wrong with it, but it is
  the only reason `info/` exists as a package. Consider `TropicraftNames` next to the registries.
* **`TileEntityFactory`** — every method is `return (TileEntity) new TileEntityX();` (redundant cast) and
  adds nothing over `new TileEntityX()`. Inline it.
* **`java.util.Random` allocations on hot paths** — `TileEntityCurareBowl:97` (`new Random().nextInt(1)`,
  always `0`), `EntityTropiCreeper:126`, `TileEntityFirePit`, `TileEntityPurchasePlate`,
  `TileEntitySifter`, `JobEggHatch`, `JobEggManage`, `JobFish`, `JobTrade`, `SpiderAdult`, `ItemMobEgg`,
  `ItemFireArmor`, `EntityTropicalFish`. Use `worldObj.rand` / a per-TE `rand`.
* **21 blanket `catch (Exception) { printStackTrace(); }`** around code that cannot throw
  (`TileEntityKoaChest.readFromNBT`, `TileEntityPurchasePlate.readFromNBT/writeToNBT`,
  `Encyclopedia.getFormattedRecipe`, `ItemValues.getItemEntry`, `JobTrade.*`,
  `EntityTropicraftWaterMob.fishDebug`). These convert bugs into silent misbehaviour (B4 is the poster
  child) — remove them or narrow to the real exception.
* **43 obvious decompiler alias locals** of the form `final AIAgent agent2 = this.agent; final AIAgent
  agent3 = this.agent;` (e.g. `EntityKoaBase:44-51`, `JobFish:183/302`, `EffectEntry:80-86`,
  `TileEntityKoaChest:206`) plus dozens more that don't match that exact shape, and float literals written
  as doubles (`0.5400000214576721`, `1.2000000476837158`, `0.10000000149011612`,
  `0.007499999832361937`). These are pure noise and make real numeric intent unreadable.
* Empty `if`/`else` no-ops: `EntityDart:120`, `EntityTropicalFish`, `EntityTropicraftWaterMob`,
  `JobTrade.onIdleTickAct`, `TileEntityKoaChest:155` (`b2`), `ChunkProviderTropicraft:228`
  (`final boolean flag = false;`), `TileEntityPurchasePlate`.
* Unnecessary `(Class)` / `(Entity)` / `(Block)` / `(Item)` / `(IInventory)` casts — hundreds of them;
  they are artefacts of the 1.5-era generics-less code and can be stripped mechanically
  (`TropicraftWorldUtils:17`, `ClientProxy` block handlers, `JobFish`, `JobTrade`).
* **`Tropicraft.dbg()`** — `final boolean consoleDebug = true;` inside the method. Two call sites
  (`JobTrade`). Either delete it or route through `FMLLog.fine` behind a config key.

---

## 5. Build, packaging & metadata

| Item | Finding | Action |
|------|---------|--------|
| `dependencies.gradle` | CoroUtil declared `implementation` — see §8 | change to `api` |
| `mcmod.info` | `"dependencies": []` while the mod requires CoroUtil (`CoroAI`) and UniMixins | add `"dependencies": ["CoroAI", "unimixins"]` (the mod-list UI shows this) |
| `src/main/java/net/tropicraft/info/TCInfo.java` | hard-coded `VERSION = "v6.0.5"` | delete (B19) |
| `enableModernJavaSyntax = jabel` | Jabel downgrades **syntax only**, not APIs. Anything past Java 8 in the *stdlib* (`List.of`, `Map.of`, `Optional.isEmpty`, records' generated methods) compiles fine and then throws `NoSuchMethodError` on a Java 8 runtime. Worth a one-line note in the README so nobody adds `List.of(...)` in a PR. | note in the README, or move to `jvmDowngrader` if you actually need newer APIs |
| `jitpack.yml` | `setupCIWorkspace` only; no tag present in the repo | either finish JitPack setup (see §9.4) or remove the file |
| `TropicraftWorldUtils.TROPICS_DIMENSION_ID = -127` | hard-coded dimension id | make it a config value with a documented default (and validate `DimensionManager` registration success) |
| CI | `build-and-test.yml` triggers on `master`/`main` only; the working branch is `feature-Refactors` | expect no CI on feature branches until a PR exists |

---

## 6. P2/P3 — Refactors worth doing

### R1. Collapse the three duplicated `doRender` implementations (−~400 LOC)

`RenderMarlin`, `RenderSeaTurtle` and `RenderWaterMob` each extend `RenderLiving` and then copy vanilla's
`RenderLiving.doRender` almost verbatim (glint pass, `hurtTime` pass, `inheritRenderPass` loop,
`catch (Exception) { printStackTrace(); }`) — three near-identical ~150-line blocks. Extract
`abstract class RenderWaterMobBase extends RenderLiving` with the shared body, or find the one thing the
copy actually changes and call `super.doRender` instead. As a bonus, the `catch (Exception)` around the
render pass hides every model bug; log once instead.

### R2. Unify `getUnlocalizedName` (−~60 LOC)

`ItemTropicraft` already implements the exact `getActualName` logic; `ItemTropicraftArmor`, `ItemFlippers`,
`ItemSnorkel`, `ItemBambooChute`, `ItemTropicraftFood`, `ItemTropicraftLeafballNew` each copy it. Extract a
small `IUnlocalizedTropicraft` interface with a `default` method (defaults are Java 8, so no blocking
issue) or a static helper in `util/`, and have `ItemTropicraftArmor` implement it.

### R3. Projectile base class

`EntityFireBall`, `EntityIceBall`, `EntityTameBall` share a 23-line and a 19-line identical method
(`onUpdate` death-tick block and the particle-count loop). `EntityTropicraftLeafballNew` shares the
`EntityThrowableUsefull` parent. When CoroUtil's `EntityThrowableUsefull` is replaced (§9.3 Phase 3), these
three plus the leafball should share one `EntityTropicraftThrowable`.

### R4. Registry tidy-up

* `TCItemRegistry` — one 300-line static initialiser; split into `initBlockItems()`, `initFood()`,
  `initTools()`, `initArmor()`, `initMisc()` for reviewability. Several entries are
  `(Item) new ItemX()` casts (R2-style cleanup).
* `TCCraftingRegistry` — 670 lines of `addShapedRecipe`; wrap the repeated
  `new Object[] {"X X","XXX", ...}` patterns in small helpers (`ringRecipe`, `toolsRecipe`,
  `storageRecipe`) to cut it roughly in half and make recipe audits possible.
* `TCRenderIDs` — `public static int` fields assigned from `ClientProxy`. They are effectively
  `final` after init; keep, but document the "assigned once in preInit" contract (or fold into an enum).

### R5. `Encyclopedia` — `ItemStack` as a map key

`HashMap<ItemStack, List<ShapedRecipes>>` (`Encyclopedia:18`) relies on `ItemStack` not overriding
`equals`/`hashCode` in 1.7.10, i.e. on identity. Every lookup therefore has to fall back to a linear
`isItemEqual` scan over `keySet()` (three such scans, one per rendered page). Re-key on
`(Item, damage)` or a small record and the scans disappear.

### R6. `ItemCocktail` / `ColorMixer`

Not audited in depth, but `ItemCocktail` (327 LOC) mixes colour-mixing, block placement, container
replacement and NBT writing; `ColorMixer` (300 LOC) duplicates colour maths that `ColorHelper` already
provides (`getRed/getGreen/getBlue/getColor`). Worth one consolidation pass.

---

## 7. Feature opportunities

Ordered by value/effort for a 1.7.10 legacy fork:

1. **Village fix + regeneration command** (`/tc village locate`, `/tc village respawn`) — B1 is the
   highest-visibility bug; a command makes it testable.
2. **Trade UI fix** (B4) — the Koa economy is a headline feature and currently inert.
3. **Config for dimension id + meteor/volcano toggles** (P3; `ConfigBiomes`/`ConfigGenRates` already
   exist, so this is a 30-minute job).
4. **`/tc time` observability** — the new command prints every loaded dimension's clock; extend it with
   a `get` for other dimensions and a `sync` to copy the overworld's weather (useful for pack devs
   migrating existing worlds).
5. **Book/encyclopedia**: per-world progress (B12) and a "recipes I can make now" filter — the data is
   already in `Encyclopedia.getRecipesForEntry`.
6. **`/tc version`** echoing `Tags.VERSION` — one line, and it makes bug reports actionable.
7. **Scuba/armour**: `ItemFireArmor` (330 LOC) has a reflective damage-type path; verify it against
   `DamageSource` subclasses and consider replacing reflection with a small registry.

---

## 8. CoroUtil: is the dependency type/tag right?

### The coordinate

```groovy
implementation("curse.maven:coroutil-237749:2388794")
```

| Part | Verdict |
|------|---------|
| Project `237749` | ✅ correct — CurseForge project "CoroUtil", modid `coroutil` / Forge modid `CoroAI` |
| File `2388794` | ✅ correct and current — `coroutil-1.7.10-1.1.6.jar`, the newest 1.7.10 file (Mar 2017). 1.1.5, 1.1.5-hw and 1.1.4 are older; everything newer is 1.12+/1.20+ |
| Jar format | ✅ usable as-is — the artifact keeps MCP class names (`net.minecraft.*`) with SRG member names (`func_*`), which is the normal 1.7.10 shipping format, so **no `rfg.deobf(...)` wrapper is needed** (and adding one is worse: RFG would try to re-map an already-MCP jar) |
| `@Mod` dependency string | ✅ `required-after:CoroAI` is right — the jar's `@Mod(modid = "CoroAI")`; note its own `mcmod.info` says `coroutil`, so the Forge modid is the counter-intuitive one. Keep `CoroAI`. |

### The configuration: `implementation` is **wrong** — use `api`

`gradle.properties` sets `useModGroupForPublishing = true`, so this project is published as a library and
its `dependencies.gradle` comments define the contract:

> `api(...)`: if you use the types from this dependency in the public API of this mod.
> Available at runtime and compile time for mods depending on this mod.

Tropicraft **does** expose CoroUtil types in its public API:

| Public API member | CoroUtil type |
|---|---|
| `EntityCoroAI implements ICoroAI, IMob` | `CoroUtil.componentAI.ICoroAI` |
| `EntityCoroAI.agent` (public field) | `CoroUtil.componentAI.AIAgent` |
| `EntityCoroAI.getAIAgent()` | `AIAgent` |
| `EntityKoaBase`, `SpiderBase`, `SpiderEgg`, `EntityTropiCreeper`, `EntityTropiSkeleton` | inherit the above |
| `TileEntityPurchasePlate.tradeKoa` (public field) | `ICoroAI` |
| `ItemTropicraftLeafballNew extends ItemTropicraftLeafball` | `CoroUtil.entity.ItemTropicraftLeafball` |
| `EntityFireBall`/`EntityIceBall`/`EntityTameBall`/`EntityTropicraftLeafballNew` | `CoroUtil.entity.EntityThrowableUsefull` |
| `EntityLostMask implements WindHandler` | `CoroUtil.api.weather.WindHandler` |
| `TileEntityKoaChest` (public methods) | `ICoroAI`, `OldUtil` |
| `Job*` classes (public) | `JobBase`, `JobManager`, `JobHunt`, … |

With `implementation`, Gradle's `java-library` plugin puts the dependency only on `runtimeElements`; the
published POM gets `runtime` scope, and a mod that compiles against `Tropicraft` will fail with
`cannot access CoroUtil.componentAI.AIAgent` the moment it touches `EntityKoaBase` or `getAIAgent()`.

Good news: `curse.maven` coordinates **do** resolve for GTNH-style consumers — the GTNH settings
convention's well-known repositories include CurseMaven (`https://cursemaven.com`, group `curse.maven`),
and this project has `includeWellKnownRepositories = true`. So `api("curse.maven:…")` is valid and will
work for the GTNH ecosystem.

### Recommended change

```groovy
dependencies {
    runtimeOnlyNonPublishable("com.github.GTNewHorizons:NotEnoughItems:2.9.7-GTNH:dev")
    // CoroUtil types are part of Tropicraft's public API (EntityCoroAI/ICoroAI/AIAgent,
    // EntityThrowableUsefull, ItemTropicraftLeafball), so consumers need it at compile time.
    api("curse.maven:coroutil-237749:2388794")
}
```

If you would rather not publish a CurseForge coordinate in your POM (it forces non-GTNH consumers to add
`cursemaven.com` themselves), the equivalent but more portable form once you publish your own fork is:

```groovy
api("com.github.MrFuzzihead:CoroUtil-Legacy:<tag>")   // JitPack; resolves for GTNH consumers
```

Two related notes:

* **Reproducibility.** `curse.maven` file IDs are immutable, so the build is reproducible — but the
  artifact is frozen at 2017 and you cannot fix bugs in it. That is the real argument for §9.
* **`rfg.deobf` is not needed** and must not be added: the jar is already in MCP-class/SRG-member form.
  If a future CoroUtil file ships differently, the symptom will be compile errors on vanilla method
  names, not a version mismatch.

---

## 9. What it would take to phase CoroUtil out

### 9.1 What you actually depend on

Not a single library call — eight subsystems, ~25k LOC in total, of which a large part is unused. Files
that import `CoroUtil.*`, `modconfig.*` or `extendedrenderer.*`: **44** (39 CoroUtil, 4 `modconfig`,
6 `extendedrenderer`, with one file in two of them).

| CoroUtil package | LOC | Files | Tropicraft uses it for | Coupling |
|---|---:|---:|---|---|
| `CoroUtil.componentAI` (`AIAgent`, `ICoroAI`) | 1,181 | 2 | base of *every* CoroAI mob | **hard** |
| `CoroUtil.componentAI.jobSystem` | 2,356 | 12 | 9 `Job*` classes, `JobHunt`, `JobFormation`, `JobPlay`, `JobTamable` | **hard** |
| `CoroUtil.pathfinding` (`PFQueue`) | 2,277 | 8 | `JobHuntAshen`, `AIAgent` internals | **hard** |
| `CoroUtil.world` (`WorldDirector`, `ManagedLocation`, `TownObject`, `SpawnLocationData`) | 3,043 | 18 | Koa villages | **hard** |
| `build.*` (`BuildManager`, `Build`, `BuildJob`, `BuildServerTicks`) | 3,558 | 26 | village schematic placement | **hard** |
| `CoroUtil.quest` | 966 | 7 | `JobQuestGiver` only | medium |
| `CoroUtil.entity` (subset) | ~4,440 | 31 | `EntityTropicalFishHook`, `EntityThrowableUsefull`, `ItemTropicraftLeafball`, `ItemTropicalFishingRod` | medium |
| `modconfig` | 2,718 | 14 | the mod's **config system** (`ConfigMod`, `IConfigCategory`, `@ConfigComment`) | medium |
| `CoroUtil.util` | 1,398 | 16 | 6 tiny helpers (`CoroUtilBlock`, `CoroUtilItem`, `CoroUtilEntity`, `CoroUtilInventory`, `CoroUtilFile`) | easy |
| `extendedrenderer` | 1,869 | 17 | particle system (`EntityRotFX`, `ParticleRegistry`, `ParticleBehaviors`, `EntityIconFX`) | medium |
| `CoroUtil.OldUtil` | 432 | 1 | **one method**: `getEntByPersistantID` | easy |
| `CoroUtil.packet` (`PacketHelper`) | 306 | 4 | 2 NBT packet helpers | easy |
| `CoroUtil.diplomacy` | 195 | 3 | `TeamTypes.getType`, `DiplomacyHelper.shouldTargetEnt` | medium |
| `CoroUtil.inventory` (`AIInventory`) | 553 | 2 | Koa item handling | medium |
| `CoroUtil.api.weather` (`WindHandler`) | 20 | 2 | marker interface on `EntityLostMask` | free |
| `CoroUtil.forge` (`CoroAI.modID`) | 1,197 | 10 | a **string constant** | free |

### 9.2 The legal question comes first

CoroUtil's CurseForge licence is **All Rights Reserved**. The `CoroUtil-Legacy` fork in
`../CoroUtil-Legacy` carries a copy of the GTNH ExampleMod `LICENSE-template` (unfilled MIT boilerplate),
which does **not** grant a licence — it is scaffolding. So:

* **Copying/renaming CoroUtil source into `net.tropicraft.*` requires written permission from Corosus.**
  That is the cheapest path by far (~9k LOC that you already understand), and it is worth just asking;
  Corosus is active and Tropicraft is one of his own historical downstreams.
* **Clean-room reimplementation** avoids that but is strictly more work.
* **Keeping the dependency but building your own fork** is neither, and is the pragmatic middle.

### 9.3 The plan

Each phase below is independently shippable. After each one, run a *behavioural* diff (spawn a koa village,
one of each mob, a dart hit, the book) — these systems are hard to unit test.

#### Phase 0 — free wins (½ day, no behaviour change)

Drops 5 of the package rows above and all of the "silly" imports:

| Replace | With |
|---|---|
| `CoroUtil.forge.CoroAI.modID` | the literal `"coroutil"` (it is only used as a `WorldDirector` map key) |
| `CoroUtil.api.weather.WindHandler` | a `net.tropicraft.api.IWindHandler` marker (2 methods) |
| `CoroUtil.util.CoroUtilBlock.isAir` | `world.getBlock(x,y,z).isAir(world,x,y,z)` (or `Material.air`) |
| `CoroUtil.util.CoroUtilItem.tossItemFromEntity` | 15-line helper using `EntityItem` |
| `CoroUtil.util.CoroUtilEntity` (`getName`, `canCoordBeSeenFromFeet`) | 2 helpers, or `world.rayTraceBlocks` |
| `CoroUtil.util.CoroUtilInventory` (`getItemCount`, `isChest`, `chestTryTransfer`) | 40-line helper over `IInventory` |
| `CoroUtil.OldUtil.getEntByPersistantID` | scan `world.loadedEntityList` for `getPersistentID()` (≈15 lines, that is all `OldUtil` does) |
| `CoroUtil.packet.PacketHelper` | `ByteBufUtils.writeTag/readTag` + `FMLProxyPacket` |
| `TickEvent` plumbing / `ExtendedRenderer.rotEffRenderer` clear-on-menu | iterate your own particle list |

Result: Tropicraft no longer references `CoroUtil.util`, `CoroUtil.OldUtil`, `CoroUtil.packet`,
`CoroUtil.api.weather` or `CoroUtil.forge`; ~200 LOC of your own code replaces ~330 LOC of dependency API
surface. (Those packages stay on the classpath until Phases 4–5, because CoroUtil's own code uses them —
what changes is that *your* code no longer does, which is what makes the later phases tractable.)

#### Phase 1 — config system (1–2 days)

`Tropicraft.preInit` registers three `modconfig` categories; `ConfigMisc/ConfigBiomes/ConfigGenRates`
implement `IConfigCategory` and annotate fields with `@ConfigComment`.

Replace with Forge's `net.minecraftforge.common.config.Configuration` (already on the classpath, no new
dependency):

1. one `Configuration` per file in `<config>/Tropicraft/`, same file names so existing packs keep their
   values;
2. `@ConfigComment` → `property.comment = ...` (mechanical, the annotation values are already
   multi-line strings);
3. `hookUpdatedValues()` → an `onConfigChanged(Property)` / `config.save()` hook;
4. delete the `IConfigCategory` plumbing and the static `COCONUT_BOMB_WHITELIST` duplication (the
   `hookUpdatedValues` split-on-comma-or-space logic can move into a typed getter).

Saves **2,718 LOC** of dependency and removes an unusual config format from the mod's support surface.

#### Phase 2 — particles (2–3 days)

`EntityFireBall`/`EntityIceBall`/`EntityTameBall`/`EntityTropicraftLeafballNew`, `TileEntityFirePit` and
`TCMiscEvents` use `extendedrenderer.particle.{ParticleRegistry, behavior.ParticleBehaviors,
entity.EntityRotFX, entity.EntityIconFX}`.

Rewrite as ordinary `EntityFX` subclasses registered through `EffectRenderer.addEffect(...)`. The
`EntityRotFX` API is essentially `setScale/setColor/setParticleAge/...` — a 150-line `TropicraftFX` base
covers it. `ParticleBehaviors` (motion attractors/repulsors) is only used for the fireball trails and can
be inlined as a per-tick `motionX/Y/Z` update.

Saves **1,869 LOC**.

#### Phase 3 — throwables, items, fishing hook (3–5 days)

* `EntityThrowableUsefull` → own `EntityTropicraftThrowable extends EntityThrowable` (4 subclasses, mostly
  mechanical; this also enables R3, collapsing the triplicated `onUpdate`).
* `ItemTropicraftLeafball` → a 30-line base item (or fold into `ItemTropicraftLeafballNew`).
* `ItemTropicalFishingRod` → own copy of vanilla `ItemFishingRod` (~80 lines).
* `EntityTropicalFishHook` → this is the expensive one: a custom bobber entity with its own `onUpdate`,
  bite logic and a renderer (`RenderFishHook`), wired into `JobFish` and `AIInventory.fishEntity`.
  Rewrite on top of vanilla `EntityFishHook`, or keep this one class vendored (it is ~350 LOC and the
  cleanest thing to carve out).

Saves most of `CoroUtil.entity` (~4.4k LOC, though only a handful of its classes are used).

#### Phase 4 — the AI engine (the big one, 3–6 weeks)

This is where the real coupling is: `AIAgent` (1,155) + `JobBase` (986) + `JobManager` + `JobHunt`/
`JobFormation`/`JobPlay`/`JobTamable` + `PFQueue` (2,277) + `AIInventory` + `EnumActState`/`EnumJobState`.
It defines the behaviour of all 15+ mobs and it is in your public API.

**Option A (recommended): post 1.7.10 `EntityAITasks`.** Vanilla's `EntityAIBase` covers this domain well:

| CoroUtil job | Vanilla / custom replacement |
|---|---|
| `JobHunt` | `EntityAIAttackOnCollide` + `EntityAINearestAttackableTarget` |
| `JobHuntAshen` (ranged + `PFQueue`) | `EntityAIRangedAttack` + `PathNavigate` |
| `JobFormation` | custom `EntityAIBase` (~80 LOC: follow leader offset) |
| `JobFish` | custom `EntityAIBase` (~250 LOC, the state machine ports 1:1) |
| `JobTrade` | custom `EntityAIBase` + interaction hook (~150) |
| `JobQuestGiver` | delete — `JobQuestGiver` only reads `PlayerQuestManager` state and does nothing with it |
| `JobEggHatch` / `JobEggManage` | custom `EntityAIBase` (~100 each) |
| `JobSleep` / `JobPlay` / `JobTamable` | custom `EntityAIBase` (~50 each) |
| `JobAttackTargetShare` | vanilla `EntityAINearestAttackableTarget` on a shared target field |
| `PFQueue` | `PathNavigate` + `EntityNavigation` |
| `AIInventory` | plain `IInventory` on the entity (or `EntityLiving` equipment slots) |
| `ICoroAI` | `ITropicraftAgent` (your interface, ~9 methods) — **breaks add-ons**, document it |
| `AIAgent.dipl_info` / `DiplomacyHelper` | a 60-line `TeamRegistry` (team → set of target teams) |

**Option B: vendor a trimmed copy.** Take `AIAgent` + `JobBase` + `JobManager` + `JobHunt` +
`PathPointEx` only, strip the unused job classes, and move them to `net.tropicraft.compat.ai`.
~3,000 LOC. Fast, but it is a copy of ARR code inside a MIT mod — **needs Corosus's permission**, and it
makes the code harder to maintain than `EntityAITasks`.

Either way: this is a multi-week, high-regression-risk change that touches ~25 files and every mob. Do it
*last*, and only if the CoroUtil dependency is actually costing you something (it is not, today — CurseForge
file IDs are immutable and stable).

#### Phase 5 — Koa villages (the other big one, 2–4 weeks)

Villages need: `WorldDirector` (per-world ticking registry), `ManagedLocation`/`TownObject` (persistent
village state), `SpawnLocationData` (member spawn points), and `build.*` (schematic placement).

1. `WorldDirector` → a `WorldSavedData` village registry ticked from `TCMiscEvents.tickServer` (the hook
   already exists — `TCMiscEvents.worldLoad` currently creates the `WorldDirector` there). Persist with
   `getInitNBTTileEntity`/`readFromNBT` equivalents you already override in `TownKoaVillage`.
2. `ManagedLocation`/`TownObject` → own `TropicraftVillage` class with the fields you actually use
   (`spawn`, `direction`, `spawnLocations`, `members`, `areaWidth/Length/Height`).
3. `build.*` + the missing `TCSchematics/koavillage` asset → **generate the village from Java** (the mod
   already has `WorldGenHomeTree`, `WorldGenEIH`, `WorldGenTualang` as precedent) or ship a schematic and
   write a ~200-line NBT structure loader. This step also fixes B1 permanently and removes the
   "schematic lives in the .minecraft folder" installer requirement.
4. `TownKoaVillageGenHelper.rotateNew` (`BuildManager.rotateNew`) → 20 lines of coordinate rotation, which
   you need anyway for `getRotatedCoordsWithRelFromCorner`.

Saves **6,601 LOC** (world + build) and unblocks the `quest` and `inventory` packages.

### 9.4 The pragmatic alternative: own the fork instead of removing it

`../CoroUtil-Legacy` already exists, is up to date with upstream, and has your own commits
(*"Fix for particles freezing in position if dayNightCycle rule is off"*, *"Removed a couple of square
roots"*). Finishing that project is a **couple of days**, and it buys most of the benefit of removal.

Its current state (checked in the neighbouring checkout):

* never built (`build/` absent), so the GTNH conversion has not been compile-tested yet;
* `gradle.properties` is still the ExampleMod template: `modName = MyMod`, `modId = mymodid`,
  `modGroup = com.myname.mymodid`, `usesMixins = false`;
* no tags, so JitPack has nothing to resolve yet.

Steps:

1. `gradle.properties`: `modName = CoroUtil`, `modId = coroutil`, `modGroup = com.github.MrFuzzihead`.
   **Keep `@Mod(modid = "CoroAI")` in `CoroUtil/forge/CoroAI.java`** — that is the Forge modid every
   downstream mod (including Tropicraft's `required-after:CoroAI`) depends on, and the one thing that must
   not change. `mcmod.info` already says `"modid": "coroutil"`, which is consistent with the fork's
   internal `CoroAI.modID` string.
2. Get it compiling under the current GTNH toolchain (`./gradlew build`) and smoke-test in a dev client —
   this is the only real unknown, the source is from 2017.
3. Tag (`v1.1.7-1.7.10`) and push. JitPack publishes `com.github.MrFuzzihead:CoroUtil-Legacy:<tag>`; JitPack
   is a well-known repository for GTNH consumers, and the module cache already shows you consume
   `com.github.MrFuzzihead:*` artifacts, so the plumbing is proven.
4. Switch `dependencies.gradle` to `api("com.github.MrFuzzihead:CoroUtil-Legacy:<tag>")`.
5. Now you can *actually fix* the bugs in CoroUtil itself (`WorldDirectorManager.instance()` returns `null`
   on the client, `OldUtil`'s linear scan, the `build` engine's `System.out` spam, `CoroUtilFile`'s
   `getSaveFolderPath()` writing into the `.minecraft` root) and shave packages out of the fork
   (`modconfig`, `extendedrenderer`, `build`) as you replace them, without breaking the class names your
   code still uses.

This is the recommended sequencing: **fix the Gradle tag now (§8), publish the fork next, then phase the
packages out in the order above** — with the `JobQuestGiver`/`quest` and `modconfig` packages being the
cheapest first removals after Phase 0/1.

### 9.5 Effort summary

| Phase | Work | Dependency removed | Risk |
|---|---|---|---|
| 0 | ½ day | ~1.2k LOC, 5 packages | none |
| 1 | 1–2 days | 2.7k LOC (`modconfig`), removes the config format from support | low (config migration) |
| 2 | 2–3 days | 1.9k LOC (`extendedrenderer`) | low |
| 3 | 3–5 days | ~4.4k LOC (`CoroUtil.entity`) | medium (fishing hook) |
| 4 | 3–6 weeks | ~6.4k LOC (`componentAI`, `jobSystem`, `pathfinding`, `inventory`, `diplomacy`) | **high** |
| 5 | 2–4 weeks | ~6.6k LOC (`world`, `build`, `quest`) + fixes B1 | **high** |

Doing 0–3 removes roughly a third of the dependency for ~2 weeks of work and **no gameplay risk**. Phases
4–5 are a rewrite of the mod's core; treat them as a separate project with playtesting milestones, not as
a cleanup task.

---

## 10. Cross-reference with the issue tracker

Checked against <https://github.com/MrFuzzihead/Tropicraft-Legacy/issues> (7 open issues + draft PR #25,
48 closed). These findings overlap with tracked issues; where the review adds a root cause or a fix, it is
raised as a new item so it can be attached to the issue.

### 10.0 Issue-by-issue

| Issue | Relation to this review |
|---|---|
| **#30** crash on Koa village generation, **#33** the fix | **B1 corrects the conclusion.** #33 fixed a real `NoSuchMethodError`, but villages still cannot generate: the schematic asset is missing and CoroUtil's `Build.readNBT` swallows the exception. See §3 B1. |
| **#34** Koa villages too rare | Cannot be tuned today — no village is ever built (B1). See §10.1. |
| **#49** bring the village tools back | `village_try` / `village_new` / `village_regen` all end in `new Build(...)`, so they would silently do nothing for the same reason as B1. They are the right **test harness** for the fix — worth landing *with* it. |
| **#50** mobs chase in creative/peaceful | Two concrete candidate causes found — **B27**. |
| **#45** coconut bomb whitelist should be a config | The config key **already exists** (`ConfigMisc.coconutBombWhitelist`); what is missing is an on/off switch defaulting to *off*, plus **B23** (the item is consumed before the check). |
| **#10** unused Souzium Aqua tool set | Confirmed: the only remnants are four dead `TCNames` constants and one unused texture — **B26**. |
| **#9** mahogany leaves use the kapok texture | **B24** — one-character fix, exact line identified. |
| **#3** / **#28** missing/changed lang keys | Two duplicate keys with conflicting values (**B25**), plus 152 dead `TCNames` constants and lang entries for items that do not exist (**B26**). |
| **#36** / **#38** cascading worldgen / worldgen lag | B3, B6, B13 and B14 are all follow-ups in the file those PRs created (`ChunkProviderTropicraft`). B6 in particular is a bug the deferral code introduced. |
| **#37** `WorldGenForestAltarRuin` reading raw coords | Already fixed. Worth re-running the cascade detector (the `CASCADING CHUNK GENERATION DETECTED` log) — it is the cheapest regression test for the whole worldgen stack. |
| **#27** update information on mod | **B19** — `info/TCInfo.java` is unreferenced *and* still says `v6.0.5`; `mcmod.info` still declares `"dependencies": []`. Both are the housekeeping #27 started. |
| **#4** bamboo flower pot | `BlockTropicraftFlowerPot` still carries `System.out.println("Flower pot was null!")`, and `TileEntityFactory.getFlowerPotTE()` is a cast-only wrapper — §4 B15. |
| **#31** marlins stuck after jumping | The `isSurfacing` fix is in. The adjacent no-op in `EntityTropicraftWaterMob.onFishingUpdate` (`if (rand.nextInt(odds) != 0 || !isRemote) {}`) is still there — same family as B8. |
| **#11**/**#26**, **#12**/**#17**, **#7**/**#23**, **#13**/**#19**, **#14**/**#20**, **#5**/**#18**, **#6**/**#24**, **#8**/**#21** | No overlap with this review; nothing to add. |

### 10.1 Villages (#34, #49)

Both issues assume generation works. It does not (B1), so:

* **#34** cannot be evaluated by hand until `village_try` produces a village. The tuning surface is
  `TownKoaVillageGenHelper.isClear` / `getBestGenDirection` (the sand-plus-open-water site geometry),
  `BiomeGenTropicsBeach.VILLAGE_CHANCE` (1 in 10 per beach chunk, 3 attempts) and the 128-block
  minimum-distance check in `hookTryGenVillage`. Order of work: fix B1 → verify with #49's `village_try` →
  *then* tune.
* **#49** is the right prerequisite for landing B1, and its own notes are worth keeping: `village_clear`
  removes every ticking location in the dimension, and `village_repopulate` was an empty loop (the same shape
  as B8's empty school-spawn loop). If they come back they want their own `/tc village` root at permission
  level 4, not level 2.

### B23. `ItemCoconutBomb` spends the bomb before checking the whitelist (#45)

```java
// ItemCoconutBomb.java:20-21
public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer player) {
    --itemstack.stackSize;                       // <-- spent unconditionally
```

A player who is not whitelisted loses the bomb **and** gets told they may not use it. The decrement also
runs client-side as well as server-side.

For #45 the fix is small: add `ConfigMisc.coconutBombWhitelistEnabled` (default `false` → everyone may throw
bombs), move the decrement inside the allowed branch, and delete the unused private `getName(EntityPlayer)`
(:41). Note `hookUpdatedValues()` builds `coconutBombWhitelistedUsers` from an empty string as `[""]` — it
works by accident; an explicit `isEmpty()` check is clearer.

### B24. Mahogany leaves use the Kapok texture (#9) — one-line fix

```java
// BlockRainforestLeaves.java:106
imageNames = new String[][] { { "leafKapok", "leafMahogany" }, { "leafKapok", "leafMahoganyFast" },
    { "leafFruit", "leafFruitFast" } };
```

`icons[0]` is the fancy-graphics layer, `icons[1]` the fast one (vanilla `BlockLeaves`), so:

| meta | name | fancy icon | fast icon |
|---|---|---|---|
| 0 | Kapok | `leafKapok` | `leafMahogany` ← swapped |
| 1 | Mahogany | `leafKapok` ← swapped | `leafMahoganyFast` |
| 2 | Fruit | `leafFruit` | `leafFruitFast` |

`leafMahogany.png` **exists** in `assets/tropicraft/textures/blocks/`. The Kapok and Mahogany icon columns are
swapped, which is exactly the reported symptom (Mahogany looks like Kapok unless Fast graphics is on).
Expected fix:

```java
imageNames = new String[][] { { "leafKapok", "leafKapok" }, { "leafMahogany", "leafMahoganyFast" },
    { "leafFruit", "leafFruitFast" } };
```

### B25. Two duplicated lang keys, one with conflicting values (#3)

`assets/tropicraft/lang/en_US.lang` has 294 keys, 2 of them duplicated:

```
line 218: item.tropicraft:koaFrame.name=Koa Item Frame
line 223: item.tropicraft:koaFrame.name=Koa Item Frame        (harmless duplicate)

line 219: item.tropicraft:tropiFrame.name=Tropics Item Frame
line 222: item.tropicraft:tropiFrame.name=Bamboo Item Frame   (wins — wrong)
```

The Tropics Item Frame is therefore labelled "Bamboo Item Frame" in game. Delete lines 222–223 (or
218–219). Worth adding a duplicate-key check to CI while in here — it is a 5-line script, and this is the
third bug in the localization family (#18, #28).

### B26. 152 dead `TCNames` constants, and lang keys for items that never existed (#3, #10)

`TCNames` declares 152 plain `String` constants (`chunkOHead`, `frogLeg`, `aquaAxe`, …). **None of them is
referenced** — every registry passes its own literal (`registerBlock(chunkOHead, "chunk")`,
`registerItem(bambooMug, "bambooMug")`). Only the `String[]` fields (`oreBlockNames`,
`rainforestLeafNames`, …) are actually used.

So the file is a 200-line naming table that nothing reads — and it is where the Souzium Aqua tools (#10)
still live: `aquaAxe` / `aquaHoe` / `aquaPickaxe` / `aquaShovel` are declared, `underwaterOre.png` is
present, and there is **no** tool material, item class or registration for any of them. For #10 those four
constants are the starting point, not existing code: `EnumHelper.addToolMaterial("souzium", …)` plus four
`ItemTropicraftTool` subclasses whose dig speed scales with `player.isInWater()`.

Either delete the 152 constants (keeping only the arrays) or make the registries use them. Right now the two
definitions can disagree, and `TCNames.aqua*` proves they already do.

### B27. Creative / peaceful targeting is inconsistent between mob families (#50)

Four different mechanisms, only two of which check creative mode:

| Mob family | Base class | Creative filter | Peaceful handling |
|---|---|---|---|
| Koa (Hunter/Fisher/Trader/Shaman) | `EntityCoroAI` → `EntityKoaBase` | **lost**: `EntityKoaBase.isEnemy()` overrides `EntityCoroAI.isEnemy()` and delegates to `DiplomacyHelper.shouldTargetEnt(ICoroAI, Entity)`, whose first overload has **no `EntityPlayer` branch at all** (the `IBTAgent` overload does check `capabilities.isCreativeMode`) | ✔ inherited from `EntityCoroAI.onLivingUpdate()` (`PEACEFUL → setDead()`) |
| `EntityTropiCreeper`, `EntityTropiSkeleton`, spiders | `EntityCoroAI` | ✔ `!(capabilities.isCreativeMode)` | ✔ |
| `EntityAshen` / `EntityAshenHunter` | vanilla `EntityMob` + `EntityAINearestAttackableTarget` | ✔ via vanilla `EntityAITarget.isSuitableTarget` | ✘ no `EntityCoroAI.onLivingUpdate` |
| `EntityEIH` | `EntityLand implements IMob` | n/a (no AI tasks) | ✘ |
| `EntityIguana` | `EntityTropicraftAnimal implements IMob` | n/a | **a passive animal is declared `IMob`**, so it counts as a hostile mob for spawn caps and is subject to peaceful/`IMob` cleanup |
| red/blue/yellow `EntityTreeFrog` | `EntityTropicraftAnimal` | n/a | hostile frogs are "animals" to the spawn/despawn code |

Plus the retaliation path, which bypasses `isEnemy` entirely: CoroUtil's `JobHunt.hookHit`
(`componentAI/jobSystem/JobHunt.java:73`) does `ent.entityToAttack = ds.getEntity()` with no check, and
`JobAttackTargetShare` then hands that target to every nearby mob of the same class. So a creative player who
swats a Koa can be chased by the whole village indefinitely — which matches #50's video.

Suggested order: (1) make `EntityKoaBase.isEnemy` defer to `super.isEnemy` for players instead of replacing
it, (2) gate `JobHunt.hookHit` / `JobAttackTargetShare` on `isEnemy`, (3) hoist the `PEACEFUL` check out of
`EntityCoroAI.onLivingUpdate` into something every hostile mob inherits (or add it to `EntityAshen` /
`EntityEIH`), (4) drop `implements IMob` from `EntityIguana`.

---

## 11. P0 fixes applied

All four P0 items are fixed on `feature-Refactors`. Nothing outside the P0 scope was changed except where a
P0 fix would otherwise leave a reachable crash behind it (noted per item).

Build state: `./gradlew spotlessApply compileJava assemble --offline` passes; the schematic is present in the
produced jar at `assets/tropicraft/schematics/koavillage.schematic` and byte-identical to the source asset.

### 11.1 Koa villages (B1)

**The asset.** `koavillage.schematic` (86×16×76, CoroUtil "newFormat" `version 1.1`) now ships at
`src/main/resources/assets/tropicraft/schematics/koavillage.schematic`. A `README.md` next to it records its
provenance and the one thing that must not be "cleaned up" (see below).

**`.gitattributes`.** Added `*.[sS][cC][hH][eE][mM][aA][tT][iI][cC] binary`. Without it the catch-all
`* text eol=lf encoding=utf-8` rule applies to a gzip file, and git mangles it on checkout.

**New `net.tropicraft.util.TCSchematics`.** CoroUtil only reads schematics from disk, so the asset is copied
to `<save folder>/TCSchematics/koavillage.schematic` on first use:

* once per JVM (result cached, including failure), so chunk population does no file I/O after the first call;
* overwrites the extracted copy when it differs from the shipped one, so a mod update actually takes effect;
* logs one `FMLLog.severe` explaining the reason and does not retry when the asset is missing or unreadable.

**`TownKoaVillage.genSchematic()`** now returns `boolean`, resolves the path through `TCSchematics`, and
rejects the build when CoroUtil hands back `levelData == null` or `map_sizeY <= 0` — the two ways
`Build.readNBT` reports failure silently. `genStructure()` stores the result; `isStructureQueued()` exposes it.

**`TownKoaVillageGenHelper.hookTryGenVillage`** bails out (returning `false`) when the schematic is
unavailable, and refuses to `addTickingLocation` a village whose build never queued — previously that left a
permanent empty town in the world director. Two debug `System.out.println` calls removed.

**`BiomeGenTropicsBeach.decorate`** drops the dead `getTopSolidOrLiquidBlock` block (`y` was computed and
never used — the call uses `getTerrainHeightAt`), replaces the per-chunk `System.err.println` with a
once-per-session `FMLLog.log(Level.ERROR, t, ...)` including the stack trace, and keeps `catch (Throwable)`
deliberately (a failure escaping into chunk population leaves the chunk broken on every load — that is what
#30 was; the bug was the silence, not the breadth).

#### The `tile.` prefix trap (worth knowing before touching this asset)

`blockTranslationMap` stores `tropicraft:tile.chunk`, not `tropicraft:chunk`. That looks like corruption and
is not: `TCBlockRegistry` registers blocks as `GameRegistry.registerBlock(block, "tile." + name)` **and then**
`block.setBlockName(name)`, so the *registry* name keeps the `tile.` prefix (FML's `GameData.addPrefix`
produces `tropicraft:tile.chunk`) while the *unlocalized* name is `tile.chunk`. `CoroUtilBlock.getNameByBlock`
uses the registry name, so the file's keys are already correct.

This was verified by disassembling `GameRegistry.registerBlock`, `GameData.register/addPrefix` and
`CoroUtilBlock.getNameByBlock` from the exact forge/coroutil artifacts the build resolves, and by checking
that all 15 block ids the village actually uses map to names that exist in the registry. An earlier attempt
to "fix" the prefixes would have turned every Koa block into air, leaving only glass, ladders and air — i.e.
an empty beach. The rule of thumb: verify with the `missing a mapping in this schematic for: <id>` log line
in a real client, not by eye.

### 11.2 `EntityDart` (B2)

* `private static final int[] POTIONS` at class level; the per-entity allocation and static write in
  `entityInit()` are gone.
* `getPotionId(dartType)` returns `-1` for anything outside `1..POTIONS.length-1` (0 is the paralysis effect
  handled by `EffectHelper`), so a hand-edited `dartType` NBT tag no longer throws inside entity update.
* Removed: the empty `if (!this.worldObj.isRemote) {}`, the `System.out.println` in the hit path, and the
  `setDead()` override that only called `super` (its `@SideOnly(Side.CLIENT)` neighbour kept its annotation).
* `(short) 200` in `entityInit` now uses the existing `MAX_HIT_TIME`.

### 11.3 `ChunkProviderTropicraft.replaceBlocksForBiome` (B3)

* `a` and `flag` moved inside the per-column loop, matching vanilla `ChunkProviderGenerate`.
* `sandMetadata` is no longer mutated by ocean columns: each column uses a local `columnSandMetadata`
  (ocean forces 0 *for that column only*).
* `metas[]` is only written for the sand variant; grass/dirt/stone no longer receive mineral-sand metadata.
* Dropped the unused `k`/`d` locals.

No random calls were added or removed, and the `a == 0 && l2 < 66` condition is untouched, so worldgen output
is identical except for the columns the leaks corrupted (#36/#38 accuracy is preserved).

### 11.4 `ItemEntry` / trading (B4)

* `matchesItem` compares `this.item.getItem() == parItem.getItem()` instead of object identity, and
  null-checks both sides.
* `getTotalValue`: removed the empty `else if (this.item.stackSize > 1) {}` branch, added a null guard.
* `ItemValues.getItemEntry`: null guard, and the `try/catch(Exception)` that hid failures is gone.
* `JobTrade.getOfferedItemsValue` skips unmatched stacks instead of NPE-ing into a swallowed exception
  (which used to display a nonsense credit).
* `JobTrade.tradeConfirmCallback` guards a null plate/trader and an out-of-range `itemsBuyable` index.
* `TileEntityPurchasePlate.readFromNBT` clamps `itemIndex` — three renderer call sites index
  `itemsBuyable` with it directly, and the value comes from the save file.

### 11.5 Still to verify in game

1. **Village placement height.** The schematic's deck is at local y=1 and it extends to y=14, so building
   from `getTerrainHeightAt` with `yOffset = 0` should put the deck one block above the beach. `surfaceOffset`
   (9) is *not* a placement knob — `BuildJob`'s alignment code is behind `if (false)`. If the deck sits wrong,
   `yOffset` in `TownKoaVillage.genSchematic` is the single knob.
2. **`TileEntities`.** The schematic ships 1.9 KB of tile entities (koa chest, purchase plate, bamboo chest).
   Confirm they appear where the build places them.
3. **Village density (#34)** can only be tuned now that villages actually build; use `village_try` (#49) to
   iterate.
4. **Watch the log** on a fresh world for `missing a mapping in this schematic for:` — any id printed there is a
   block the running pack cannot resolve, and it will be built as air.

### 11.6 Found while wiring the test harness

Three more bugs surfaced the moment the code was exercised outside a released jar.

**Dev runs have been dead since the CoroUtil dependency came from CurseForge.** `dependencies.gradle` declared
`api("curse.maven:coroutil-237749:2388794")`, which is a *production* jar: it calls Minecraft through SRG names
(`MinecraftServer.func_71276_C`, `Minecraft.func_71410_x`, ...) while the RFG dev runtime is MCP-named. The result is
`NoSuchMethodError` from `modconfig.ConfigMod.getSaveFolderPath` during `CoroAI`/`BuildMod` preInit, which errored
those mods and Tropicraft with them — the client crash report in `run/client/crash-reports/` from before this work
is the same failure. Fixed by adding `devOnlyNonPublishable(rfg.deobf("curse.maven:coroutil-237749:2388794"))` for
the dev runtime; `api` stays as the compile/publish surface.

**Four projectile classes narrowed the visibility of a CoroUtil override.** `EntityFireBall`, `EntityIceBall`,
`EntityTameBall` and `EntityTropicraftLeafballNew` declared `protected void readEntityFromNBT/writeEntityToNBT`
while `EntityThrowableUsefull` declares both `public`. That is an illegal override (javac rejects it, and the JVM
would throw at `invokevirtual` resolution time), it was simply invisible because the production jar hides it behind
SRG names. Widened to `public`.

**`ItemEntry.buyable` was never assigned.** `ItemValues.addEntry(...)` recorded buyability only by adding the entry
to `itemsBuyable`, never by setting the flag the lookup actually reads, so `getItemEntry(stack, true)` and
`getIsBuyableItem(stack)` always returned `false`/`null`. No production code calls them today - which is why nobody
noticed - but they are the API the purchase plate and trade jobs are meant to use. Fixed, and covered by
`ItemValuesTest`.

---

## Appendix A — quick-win checklist

- [x] Ship `koavillage.schematic` + `TCSchematics` extractor; fail loudly on a bad build (B1) — §11.1
- [x] Dev runtime: `devOnlyNonPublishable(rfg.deobf(...))` for coroutil, or no dev run works at all (§11.6)
- [ ] `dependencies.gradle`: `implementation` → `api` for coroutil (§8) — already `api`; the remaining item is
      keeping it `api` if the deobf path is ever cleaned up
- [ ] `mcmod.info`: add `CoroAI`, `unimixins` dependencies; drop stale keys
- [ ] Delete `info/TCInfo.java`, `util/TropicraftUtils.getTopWaterBlockY`, `TileEntityFactory`
- [ ] Delete duplicate `registerEntity(EntityTropiCreeper…)`; convert entity IDs to literals (B9)
- [x] `ItemEntry.matchesItem`: reference → `getItem()` comparison; delete the empty `else if` (§11.4)
- [x] `EntityDart`: `static final int[] POTIONS`, bounds-check `dartType`, delete the empty `if` (§11.2)
- [x] `ChunkProviderTropicraft`: reset `flag` per column, local `sandMetadata` (§11.3); `fallInstantly` in
      `finally` (B6) still open
- [ ] `JobFish`: `< 0.0f` → `<= 0.0f`, delete `rand.nextInt(1)`, use `worldObj.rand`
- [ ] `EffectHelper`: tick on one side only
- [ ] `TropicalBook`: per-world save file, null-check the resource stream, try-with-resources
- [ ] `Tropicraft.dbg`: delete or gate behind a config key
- [ ] Replace the 42 `System.out/err` with `FMLLog` at appropriate levels
- [ ] Replace `catch (Exception) { printStackTrace(); }` (20 sites) with scoped handling
- [ ] Extract `RenderWaterMobBase` for the three duplicated `doRender`s (R1)
- [ ] Extract `IUnlocalizedTropicraft` for the six duplicated `getUnlocalizedName`s (R2)
- [ ] `BlockRainforestLeaves.imageNames[1][0]` → `"leafMahogany"` and `[0][1]` → `"leafKapok"` (B24, #9)
- [ ] Delete the two duplicate `*Frame` lang lines (B25, #3)
- [ ] `ItemCoconutBomb`: move `--stackSize` inside the whitelist branch; add an enabled toggle (B23, #45)

## Appendix B — things that are already good and should stay that way

* `ChunkProviderTropicraft`'s deferred-decoration gate and the rationale comment — this is the correct
  fix for cascading worldgen, and the cascade detector is a genuinely useful diagnostic.
* The `TCTimeAndWeatherData` + `MixinDerivedWorldInfo` + `MixinWorldServerMulti` design: registering the
  existing `WorldInfo` instead of replacing it, `required: false` mixin configs, and the
  `isAnswering()` probe are all the right calls.
* `Mixins.java` (`IMixins`/`MixinBuilder` with `Phase.EARLY`) and `EarlyMixinsLoader` — correct GTNH
  pattern, no hand-maintained mixin JSON list.
* The `/tc` command tree (`CommandTropicraft` + per-subcommand `ICommand`s, permission level 2, tab
  completion) — clean and consistent with vanilla.
* `EntityKoaBase`/`EntityCoroAI`'s null-tolerance around `agent` (`checkNewAgent()` on NBT load,
  `entityInit`, `interact`) — this is what keeps old worlds loading.
