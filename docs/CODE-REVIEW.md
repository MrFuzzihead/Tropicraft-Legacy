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
| B28 | P2 | worldgen | Coral and the decorative mineral sands are rolled **per chunk**, so they appear as 16×16 squares in the terrain (§4) |
| B29 | P1 | entity | The eagle ray never moves: its 0.25-block height breaks vanilla's "am I in water" test (§4) |

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

**Status: fixed.** Deleted (`§12`).

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

**Status: fixed.** Restored from a captured previous value in the `finally` (`§12`).

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

**Status: fixed.** (`§12`)

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

**Status: resolved by deletion** — see §14.2 for why implementing it was the wrong call.

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

**Status: fixed.** Explicit ids, duplicate removed, fail-fast guard, and an in-game check (`§12`).

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

**Status: fixed.** Ticked per side (`§12`).

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

**Status: fixed** — see §14.1, with an in-game repro that fails on the old code.

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

**Status: fixed** — see §16.

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

**Status: fixed** — see §15.1.

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

**Status: fixed** — see §15.2.

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

* **`TCInfo`** (`info/TCInfo.java`) — **deleted** (§13): unreferenced, and `VERSION = "v6.0.5"` was stale.
* **`TCPacketEvents.onPacketFromClient`** — parses the packet then `if (packetCommand.equals("")) {}`.
  A registered, authenticated-reachable no-op handler. Delete it and stop registering the channel for
  client→server traffic. `getSelfUsername()` is unused. The client handler prints every packet command to
  stdout.
* **`TCNames`** — the 152 unused `String` constants are gone (§13); only the arrays remain, so the file is now
  51 lines. `info/` still exists for it alone.
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

### B28. (P2) Coral and the decorative mineral sands generate in single-chunk squares

**Status: fixed** — see §21.1 (sands now come from a coordinate noise with dithered edges, reefs spread across
chunks).

Both features key off a *per-chunk* random roll, so their output is bounded by chunk borders and reads as a 16×16
checkerboard in the terrain. Reported after a play test: "coral and foamy sands spawn only in single chunks which
makes them look very bad".

**Mineral sands (Foamy Sands and friends).** `ChunkProviderTropicraft.replaceBlocksForBiome` rolls the decorative
variant once per chunk:

```java
final int sandType = this.rand.nextInt(200);   // :324
```

0–3 pick `TCBlockRegistry.mineralSands` with metadata 0–3 (`Coral`, `Green` = "Foamy Sands", `Black`, `Mineral`),
anything else is plain sand. Every beach and ocean column in that chunk is then filled from that single
`sandBlock` / `sandMetadata`, so a chunk is either all one colour or none, and neighbouring chunks roll
independently. At 2% of chunks per variant, beaches come out as scattered solid tiles of green/black/coral sand.

**Coral.** `BiomeGenTropicsOcean.decorate` runs `new WorldGenCoral().generate(...)` at most once per chunk (1 in 5),
anchored inside the chunk (`x + 6 + rand.nextInt(4)`); `WorldGenCoral` then makes 12 attempts within ±8 blocks of that
point. So every reef is a single 12-block cluster pinned to one chunk. Note `BiomeGenTropicraft.decorate` (:99) runs
the same roll when the biome at those coordinates is the ocean, so an ocean chunk actually gets two independent
rolls.

**Fix sketch.** Sands: roll the variant per column or per small cell, or derive it from a low-frequency noise so a
patch is larger than a chunk and fades at the edges; the block and metadata already support it. Coral: take the reef
centre from a coarse noise (or let `WorldGenCoral` spread its attempts over 32–48 blocks around the chunk origin) so
a reef spans several chunks. Both are local changes to the decorate path, and both want a harness check
("non-plain-sand blocks per chunk", "coral clusters per 4×4 chunk area") so the change can be verified.

### B29. The eagle ray never swims — a 0.25-block hitbox defeats vanilla's water test

Reported from a play test: "stingrays don't swim, they just idle in the water". Not expected, and the cause is
vanilla geometry rather than the ray's AI.

`Entity.handleWaterMovement` decides wetness with

```java
this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0, -0.4000000059604645D, 0.0D).contract(...),
    Material.water, this);
```

and `AxisAlignedBB.expand` is **symmetric**: for a negative argument it moves `minY` *up* and `maxY` *down*. For any
entity shorter than 0.8 blocks the box therefore turns inside out (`minY` ends up above `maxY`), and
`World.isMaterialInBB` then iterates an empty y range and reports no water.

`EntityEagleRay` is 1.0 × **0.25** (`setSize(1.0f, 0.25f)`), so it is never wet. Every swim branch in
`EntityTropicraftWaterMob` is gated on `isInWater()` (the motion assignment, the dive/surface logic, the
out-of-water flop), so the ray does literally nothing - it hangs in the water exactly where it spawned.

Worth noting the failure is *position dependent*, which is why it looks like a dead AI rather than a geometry bug:
the tropical fish is 0.60 tall and also gets an inside-out box, but its inverted box still floors into the same block
row, so the query happens to find water. The ray's does not. The invariant is "an entity shorter than 0.8 blocks has a
broken water test, and whether it bites depends on where it is standing".

**Fix.** `EntityTropicraftWaterMob.handleWaterMovement` now falls back to the unexpanded box when vanilla says no,
setting `inWater`/`fallDistance` itself. Only reached when the vanilla test failed, so the normal case is untouched
and the fluid push cannot be applied twice.

**Coverage.** Four in-game checks ("an eagle ray / a marlin / a seahorse / a tropical fish swims"): each mob is
dropped into a built pool and ticked 200 times; it has to be wet at some point and to cover more than 1.5 blocks.
The ray's check fails on the old code with `moved 0.00 blocks, was wet: false, height 0.25`.

---

## 5. Build, packaging & metadata

| Item | Finding | Action |
|------|---------|--------|
| `dependencies.gradle` | CoroUtil declared `implementation` — see §8 | already `api`; the dev runtime needs `api(rfg.deobf(...))`, §11.6 |
| `mcmod.info` | `"dependencies": []` while the mod requires CoroUtil (`CoroAI`) and UniMixins | **done**, §19.2 |
| `src/main/java/net/tropicraft/info/TCInfo.java` | hard-coded `VERSION = "v6.0.5"` | delete (B19) |
| `enableModernJavaSyntax = jabel` | Jabel downgrades **syntax only**, not APIs. Anything past Java 8 in the *stdlib* (`List.of`, `Map.of`, `Optional.isEmpty`, records' generated methods) compiles fine and then throws `NoSuchMethodError` on a Java 8 runtime. | **done**: noted in the README, §19.2 |
| `jitpack.yml` | `setupCIWorkspace` only; no tag present in the repo | left alone by decision |
| `TropicraftWorldUtils.TROPICS_DIMENSION_ID = -127` | hard-coded dimension id | **done**: config + registration validation, §19.2 |
| CI | `build-and-test.yml` triggers on `master`/`main` only; the working branch is `feature-Refactors` | expect no CI on feature branches until a PR exists |

---

## 6. P2/P3 — Refactors worth doing

### R1. Collapse the three duplicated `doRender` implementations (−~400 LOC)

**Status: done** — see §20.1. `RenderWaterMobBase` now holds one copy of the body for all four water-mob renderers. Server-side suites are green; the render path itself is verified by hand (§20.4).

`RenderMarlin`, `RenderSeaTurtle` and `RenderWaterMob` each extend `RenderLiving` and then copy vanilla's
`RenderLiving.doRender` almost verbatim (glint pass, `hurtTime` pass, `inheritRenderPass` loop,
`catch (Exception) { printStackTrace(); }`) — three near-identical ~150-line blocks. Extract
`abstract class RenderWaterMobBase extends RenderLiving` with the shared body, or find the one thing the
copy actually changes and call `super.doRender` instead. As a bonus, the `catch (Exception)` around the
render pass hides every model bug; log once instead.

### R2. Unify `getUnlocalizedName` (−~60 LOC)

**Status: done** — and it was ~35 classes, not 6. See §17.1.

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

**Status: done** — see §18.

* `TCItemRegistry` — one 300-line static initialiser; split into `initBlockItems()`, `initFood()`,
  `initTools()`, `initArmor()`, `initMisc()` for reviewability. Several entries are
  `(Item) new ItemX()` casts (R2-style cleanup).
* `TCCraftingRegistry` — 670 lines of `addShapedRecipe`; wrap the repeated
  `new Object[] {"X X","XXX", ...}` patterns in small helpers (`ringRecipe`, `toolsRecipe`,
  `storageRecipe`) to cut it roughly in half and make recipe audits possible.
* `TCRenderIDs` — `public static int` fields assigned from `ClientProxy`. They are effectively
  `final` after init; keep, but document the "assigned once in preInit" contract (or fold into an enum).

### R5. `Encyclopedia` — `ItemStack` as a map key

**Status: done.** See §17.2.

`HashMap<ItemStack, List<ShapedRecipes>>` (`Encyclopedia:18`) relies on `ItemStack` not overriding
`equals`/`hashCode` in 1.7.10, i.e. on identity. Every lookup therefore has to fall back to a linear
`isItemEqual` scan over `keySet()` (three such scans, one per rendered page). Re-key on
`(Item, damage)` or a small record and the scans disappear.

### R6. `ItemCocktail` / `ColorMixer`

**Status: open.** See §17.4.

Not audited in depth, but `ItemCocktail` (327 LOC) mixes colour-mixing, block placement, container
replacement and NBT writing; `ColorMixer` (300 LOC) duplicates colour maths that `ColorHelper` already
provides (`getRed/getGreen/getBlue/getColor`). Worth one consolidation pass.

---

## 7. Feature opportunities

Ordered by value/effort for a 1.7.10 legacy fork:

1. **Village fix + regeneration command** — the fix landed in §11.1; the commands came back as `/tc village
   locate|list|try|regen` in §21.2.
2. ~~Trade UI fix (B4)~~ — done, §11.4.
3. **Config for dimension id + meteor/volcano toggles** — dimension id in §19.2, volcano toggle in §21.3 (there is
   no meteor feature in the mod).
4. **`/tc time` observability** — `get` / `set <time>` / `sync` in §21.2.
5. **Book/encyclopedia**: per-world progress done (§16.1); the "recipes I can make now" filter is **still open**
   (client GUI work, needs a visual check).
6. **`/tc version`** — done, §21.2.
7. **Scuba/armour**: the reflection was the real problem and is gone (§21.3); the damage-type handling turned out to
   be fine.

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

**Status: fixed.** (`§12`)

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

**Status: fixed.** (`§12`)

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

**Status: fixed.** (`§12`)

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

**Status: fixed** — see §19.1, with in-game checks written first (three of them failed on the old code).

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
is the same failure. Fixed by declaring it as `api(rfg.deobf("curse.maven:coroutil-237749:2388794"))`, so the
compile *and* dev-runtime classpaths get the remapped jar. Note the knock-on: compiling against the deobfuscated jar
is what made the next bug visible, because it is the first time `EntityThrowableUsefull`'s real signatures were
visible to javac.

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

## 12. P1 fixes applied (batch 1)

Eight of the smaller P1 items, each with the reasoning that decided the fix. Everything here is covered by
`./gradlew check` (and the entity registry by the in-game suite, now 18 checks).

**B5 — `TropicraftUtils.getTopWaterBlockY` deleted.** It was unreferenced, and its loop walked to `y < 0` (a
negative `World.getBlock` index) whenever the column had no water. Nothing to fix, so it is gone.

**B6 — `BlockSand.fallInstantly`.** `doPopulate` now captures the previous value and restores it in the `finally`.
The comment in the code says why it matters here specifically: `populate()` can also be driven from
`unloadQueuedChunks()`, so a single decoration exception used to disable gravity blocks for the rest of the session.

**B7 — `JobFish`.**
* `fish.getHealth() < 0.0f` → `<= 0.0f || isDead`: health is never negative, so the Koa fisherman never caught the
  fish it had just hit, and never reached the deposit path early.
* `rand.nextInt(1) == 0` → `rand.nextInt(EARLY_RETURN_CHANCE) == 0` with `EARLY_RETURN_CHANCE = 10`. `nextInt(1)`
  meant "always", i.e. the fisherman went home after two fish instead of up to five. The 1-in-10 is a judgement
  call - the original constant is unrecoverable - and it is named and commented so it can be tuned.
* `new Random()` per tick → `this.ent.worldObj.rand`, and the unused `entInv2` (×2) and `transferred` locals are gone.

**B9 — entity network ids.** The duplicate `EntityTropiCreeper` line is deleted and every entity now has a
hand-written id as the first argument:

```java
registerEntity(EntityTropiCreeper.class, 16, "TropiCreeper", 80, 3, true);
```

The ids are **exactly the ones the counter produced**, including the hole at 18 that the duplicate burned - that
keeps today's client/server id mapping intact while making it impossible to renumber by accident. `registerEntity`
now throws if a class is registered twice or an id is reused, so the next duplicate fails at startup instead of
silently burning an id, and `TCEntityRegistry.getRegistered()` exposes the map for the in-game test, which asserts
uniqueness and that every id round-trips through `EntityRegistry.lookupModSpawn`.

**B10 — `EffectHelper`.** `tick()` became `tick(Side)` and only advances entries whose `worldObj.isRemote` matches
the side. In single player the integrated server and the client share the static list and both tick handlers drained
all of it, so **every** effect (not just the player's paralysis) counted down twice per tick and lasted half as long
as on a dedicated server.

**B23 — coconut bomb (#45).** `ConfigMisc.coconutBombWhitelistEnabled` (default `false`, i.e. everybody can throw
one) gates the whitelist, the whitelist is parsed correctly (`""` used to become `[""]`; `"a, b"` and `"a b"` both
work now, covered by `ConfigMiscTest`), and the bomb is only spent on the server, only when the throw is allowed. The
unused `getName(EntityPlayer)` is gone.

**B24 — mahogany leaves (#9).** Kapok and Mahogany had their icon columns swapped; there is no `leafKapokFast`
texture, so Kapok reuses its own icon:

```java
imageNames = new String[][] { { "leafKapok", "leafKapok" }, { "leafMahogany", "leafMahoganyFast" },
    { "leafFruit", "leafFruitFast" } };
```

**B25 — duplicated lang keys (#3).** `item.tropicraft:tropiFrame.name` was defined twice, the second time as
"Bamboo Item Frame", which won - the Tropics Item Frame was labelled wrong in game. `koaFrame` was duplicated with
the same value. Both duplicates are gone and the file has zero duplicate keys.

Note on #45 and #3: these are the two issue-tracker items that were cheapest to close, and both were user-visible
(the bomb that ate itself, and a wrong item name). #9 was a two-character fix once the icon columns were read
correctly.

### 12.1 Still open in P1

| Item | Why it is not in this batch |
|---|---|
| ~~B8 fish schools~~ | Done in §14.2. |
| ~~B11 Koa chest quota~~ | Done in §14.1. |
| **B12** encyclopedia save file | Changes the on-disk format and the render-path write frequency; needs a decision on where per-world client data should live. |
| ~~B13 / B14~~ pending-decoration sweep and save flush | Done in §15. |
| **B15–B20** dead code & residue | Big mechanical diff (TCNames, `TCInfo`, `TCPacketEvents`, `TileEntityFactory`, 43 alias locals). Better as one dedicated, no-behaviour-change commit. |

---

## 13. Dead code & residue (batch 2)

The mechanical half of P1: everything here is a deletion or a local simplification, no behaviour intended to
change. `./gradlew check` and the 18-check in-game suite both pass.

| Removed | Why |
|---|---|
| `info/TCInfo.java` | Zero references, and its `VERSION` still claimed `v6.0.5` while the build ships 6.2.x. A second source of truth for the mod id and the proxy class names that nothing read. |
| `factory/TileEntityFactory.java` | Seven `return (TileEntity) new TileEntityX();` wrappers. Inlined at the seven call sites (`createNewTileEntity`), package deleted. |
| `TCNames`' 152 constants | Every plain `String` in the file was unreferenced - the registries pass their own literals (`registerBlock(chunkOHead, "chunk")`). Only the `String[]` arrays are used, and they are all still there (`TCNames` went from 203 lines to 51). |
| `TCPacketEvents.onPacketFromClient` | A registered handler for an authenticated client→server packet that parsed the payload and then did `if (packetCommand.equals("")) {}`. The channel stays registered for the server→client effect packets. |
| `TCPacketEvents.getSelfUsername` | Unused. |
| `System.out.println` of every packet command | Client log spam on every Tropicraft packet. |
| `Tropicraft.dbg` | `final boolean consoleDebug = true;` inside the method, two call sites. |
| request `new Random()` × 13 | Now `worldObj.rand` / `this.rand` / `itemRand`. The worst was `TileEntityCurareBowl`'s `new Random().nextInt(1)` (always 0) and `JobFish`'s per-tick allocation. `TileEntityPurchasePlate` keeps one static instance because its constructor runs before the TE has a world. |
| `TileEntityKoaChest` dead locals | `j = 0; j = 0;`, `final int dsdf = 0;`, `if (j < this.entIDs.length) {}`, and the `b2` colliding-box computation that `getCanSpawnHere` never used. |
| Empty `if` statements | `EntityDart` (P0 batch), `EntityTropicraftWaterMob.onFishingUpdate`, `JobTrade.onIdleTickAct`, `TileEntityKoaChest`, `ChunkProviderTropicraft` (`flag`/`flag2`), `RenderFishHook`, `BlockAirCompressor.updateTick` (the whole override), `EntityAIMeleeAndRangedAttack`. |
| Blanket `catch (Exception) { printStackTrace(); }` around NBT getters/setters | `TileEntityKoaChest.read/writeFromNBT`, `TileEntityPurchasePlate.read/writeFromNBT`. `NBTTagCompound.getInteger` cannot throw; the catch only hid mistakes. |

The sweep took `System.out`/`System.err` from 42 sites to 19, `printStackTrace()` from 21 to 10, and decompiler alias
locals (`final X y2 = this.y;`) from 43 to 17.

### 13.1 What is deliberately left

* **Alias locals and `(Class)`/`(Block)`/`(Item)` casts.** 17 alias locals and a few hundred redundant casts remain.
  They are noise, but rewriting them mechanically rewrites code that is otherwise fine, and the 1.7.10 generics are
  weak enough that a "harmless" cast removal occasionally changes which overload is selected. Worth doing file by
  file when touching a file for another reason, not as a sweep.
* **Float literals written as doubles** (`0.10000000149011612` → `0.1`). Same reasoning: the literal *is* the exact
  `(double)(float)` value, so shortening it changes the double and only stays equal after a float cast. Fine in a
  motion multiplier, not something to change blindly.
* **`BlockTropicraftOre.rand`.** One `Random` per ore block instance (three blocks) for `getExpDrop`, where the
  vanilla signature only hands out an `IBlockAccess`. Not a hot path; left alone.
* **B8's school-spawning loop.** Still empty (`EntityTropicalFish:212`, with `new Random()` inside the loop header).
  Deleting the dead code would mean deleting the feature; it needs the real implementation and in-game tuning, which
  is the next batch.

---

## 14. P1 fixes applied (batch 3)

Both of these were done repro-first: the in-game suite grew a check that **fails on the old code** before the fix
went in.

### 14.1 Koa chest household (B11)

The suspicion in the original write-up was right, but the mechanism is worse than "the quota is short":

* `getHomeKoaCount()` counts members in a 160×80×160 box, i.e. only the ones that are loaded *and* close.
* `addToList()` **silently did nothing** when both slots were full, and `spawnKoa()` incremented its own
  `hunters`/`fishers` counters *before* calling it, so nothing noticed.
* `updateList()` cleared the slot of any member it could not resolve, and `OldUtil.getEntByPersistantID` only scans
  `world.loadedEntityList`.

So any chest whose Koa had wandered out of the scan box spawned a fresh pair on its next 5-minute tick, and the new
pair was never registered: the world gained two untracked Koa per visit, forever.

The fix makes the **registered slots** the quota:

```java
int free = this.housePop - this.occupiedSlots();
if (free <= 0) return;
```

`addToList` now reports whether it got a slot (and the caller drops the Koa instead of spawning something nothing
tracks), `updateList` only clears a slot when a *loaded* member is provably dead (an unloaded member keeps its slot -
that is the whole point), and `getHomeKoaCount` is documented as the live count it actually is, used only to pick
which type to spawn next.

Trade-off, deliberate and documented in the code: a Koa that dies while its chunk is unloaded keeps its slot, so
that chest will not replace it. That is bounded (two slots) and much better than unbounded growth; resolving an
unloaded persistent id would require loading the chunk.

In-game checks added (`koaChestKeepsItsHousehold`):

| Check | Old code |
|---|---|
| a Koa chest fills its household | passes |
| a Koa chest replaces dead members | passes |
| a Koa chest does not re-spawn while its members are out of range | **fails: 2 → 4 Koa** |

### 14.2 Tropical fish schools (B8) — resolved by deletion

The empty loop was not the whole story:

* `EntityTropicalFish(World)` calls `setShouldSpawnSchool(true)` in the constructor, so **every** naturally spawned
  fish, bucket release and mob-egg spawn reaches that block on its first tick.
* Nothing anywhere caps the population, and the school members would not re-spawn (`readEntityFromNBT` and the copy
  constructor both clear the flag).

Implementing the spawn as written would therefore have multiplied every fish spawn by 4-7 with no cap - a population
explosion, not a feature. On top of that the copy constructor it would call places itself inside
`do { ... } while (!this.getCanSpawnHere());` with a fresh position each iteration: an unbounded loop inside entity
tick if the fish is somewhere it cannot spawn.

So the dead path is gone: the `SHOULD_SPAWN_DATAWATCHER` slot, its accessors, all five call sites, the loop, and the
copy constructor that existed only to serve it. Fish spawn singly, which is what already happened, and the code no
longer pretends otherwise. If schools are wanted later they need a population cap, a spawn budget and a bounded
placement retry - a feature, not a fix.

While in the file: `checkForLeader()` ran `getEntitiesWithinAABB` over a 20³ box **every tick** for every fish that
was a leader or had no leader. It now runs on the same 40-tick cadence the rest of that method already uses.

An in-game check (`tropicalFishTicksWithoutItsSchoolPath`) drives `updateEntityActionState` twice through reflection
so the remaining state machine stays honest.

---

## 15. P1 fixes applied (batch 4) — the deferred-decoration sweep

### 15.1 B13: the per-tick sweep

The old shape, called from `unloadQueuedChunks()` **every tick**:

```java
do {
    progress = false;
    for (final ChunkCoordIntPair key : new ArrayList<>(this.pendingDecoration)) {   // allocation
        if (this.isAreaLoaded(key.chunkXPos, key.chunkZPos, DECOR_RADIUS)) {        // 81 lookups
            ...
        }
    }
} while (progress);                                                                  // O(n^2)
```

Three separate costs: a fresh `ArrayList` per pass, 81 `chunkExists` lookups per pending chunk per pass, and a
full re-scan after every pass that decorated anything. With *n* pending chunks that is up to 81·n² lookups in a
single tick.

Now:

* one sweep every `PENDING_SWEEP_INTERVAL` (5) ticks - `populate()` still flushes immediately when the chunk it was
  called for is ready, so this throttle only delays the "the rest of the neighbourhood finished loading a moment
  later" path, which is invisible at 20 tps;
* one pass gathers the ready keys into a **reused** field, then decorates them, so no allocation and no
  `ConcurrentModificationException` if decoration generates a chunk (the case the cascade detector reports);
* at most `MAX_SWEEP_PASSES` (4) passes, so a chunk that frees more chunks cannot loop unbounded;
* a `sweepingPending` guard so a nested `populate()` cannot re-enter the sweep.

### 15.2 B14: the save flush

```java
final Chunk chunk = this.provideChunk(nx, nz);
serverProvider.loadedChunkHashMap.add(ChunkCoordIntPair.chunkXZ2Int(nx, nz), chunk);
serverProvider.loadedChunks.add(chunk);
chunk.onChunkLoad();
```

now goes through `serverProvider.loadChunk(nx, nz)`: the chunk load event fires, the server's own bookkeeping runs,
and chunk-loading mods (Chunk Pregenerator, Thermos forks) see a normal load. It is a once-per-shutdown path, so the
extra cost does not matter. Each chunk is also wrapped in a `catch (Exception)` that logs and continues, so one bad
chunk cannot abort the shutdown save.

While there: the static `activeProviders` set had no removal path, so every provider ever created (including a
client's after reconnecting) stayed referenced and would be flushed at the next shutdown. `WorldEvent.Unload` now
calls `ChunkProviderTropicraft.onWorldUnloaded`, which drops the provider for that world.

`chunkExists` also gained the comment the review asked for: it returns `true` always, so `isAreaLoaded` is only
meaningful because it asks the `ChunkProviderServer` wrapper - pointing it at the generator itself would silently
disable the entire gate.

### 15.3 How this is covered

These two are perf/robustness fixes, not user-visible bugs, so the checks are regression guards rather than repros
(unlike §14.1):

| Check | What it pins |
|---|---|
| deferred decoration flushes when its neighbourhood loads | a chunk whose 2x2 exists but 9x9 does not parks itself, and the throttled sweep decorates it once the ring is loaded |
| the shutdown flush decorates parked chunks | `flushAllProvidersForSave()` forces the neighbourhood through `ChunkProviderServer.loadChunk` and decorates, without throwing |

Both park their chunk deterministically by generating three neighbours *first* and the target last: vanilla's
`Chunk.populateChunk` only runs `populate()` when the 2x2 neighbourhood already exists, which is exactly the state a
player's chunk reaches in normal play.

---

## 16. P1 fixes applied (batch 5) — encyclopedia progress and the last residue

### 16.1 B12: encyclopedia progress

The old design: one `eTsave.dat` in the game directory, read in the constructor, rewritten (whole file, recompressed)
by `saveData()` on **every** `markPageAsNewlyVisible`/`markPageAsRead`, i.e. on the input path, and shared between
every save and every server.

Three pieces now:

* **`PageProgress`** (new, pure): the per-page state, with NBT read/write and no client classes, so it is unit
  tested. It also fixes a second bug found while writing the tests: `markPageAsNewlyVisible` wrote the value
  unconditionally, so a page the player had **read** could be flagged "new" again. `markVisible` now only upgrades.
* **Per-world storage**: progress is written to
  `<mcDataDir>/tropicraft/encyclopedia/<key>-<hash>.dat`, where the key is `sp:<save folder name>` in single player
  and `mp:<server address>` on a server. The client cannot know a server's world name, so for multiplayer it is per
  server, which is the closest thing to "per world" that exists client-side. The binding is resolved lazily (the book
  is constructed at mod init, before any world exists) and re-resolved when the key changes, flushing the previous
  world first.
* **Debounced writes**: marks set a dirty flag; `flushIfDirty()` writes at most once every 2 s and is driven by
  `TCMiscEvents.tickClient`; `flush()` runs on the client world unload so leaving a world cannot lose the last two
  seconds.

The old shared file is imported once: the first world opened after the update inherits it, and the file is renamed to
`eTsave.dat.imported` so the next world does not inherit it too.

Also in `TropicalBook`: the contents file's `getResourceAsStream` is null-checked with a clear message (a missing
lang file used to be an NPE at startup), streams are closed with try-with-resources, the `java.util.logging` /
`FMLLog` mix is now FMLLog throughout, the unused `public static File file` is gone, and the placeholder title method
lost the parameter it ignored (`getPageTitleNotVisible(int)` → `getHiddenPageTitle()`).

### 16.2 The rest of the residue

`src/main/java` now has **no `System.out`/`System.err` and no `printStackTrace()`**. The 19 prints became `FMLLog`
calls at the right level or were deleted (pure debug noise like `"t2"`, `"watwat"`, `"mount"`, `"sharing
targetting"`, `"meta 8"`, a per-frame renderer print, a `VERIFY THIS CODE` marker that was verified in the P0 batch).
Two were bugs rather than noise:

* `BlockTropicraftFlowerPot` printed `"Flower pot was null!"` and then **dereferenced the null** - it now skips the
  tile entity update instead of throwing.
* `JobTrade.returnCredit` wrapped a loop in `catch (Exception) { printStackTrace(); }`; a null trader is the only way
  it can fail, and the catch then dropped the player's offerings. It now returns early and keeps them.

The remaining eight `printStackTrace()` calls were renderers and the packet handler: render failures log at debug
(they would otherwise fire every frame), a broken encyclopedia recipe page warns once per session, a malformed packet
warns, and the fire armour's reflection fallback logs at severe because failing it means no particles at all.

### 16.3 Correction: the "43 alias locals" are not all noise

I tried to inline the remaining 17 decompiler aliases with a script and reverted it, because the pattern is not what
the review assumed. In `ModelKoaMan` the alias names deliberately do **not** match the field they read:

```java
final ModelRenderer leaf2 = this.leaf3;   // "leaf2" is the field leaf3
final ModelRenderer leaf3 = this.leaf2;   // and "leaf3" is the field leaf2
```

A rewrite that assumes `alias == this.<same name>` swaps two leaf renderers, and even a rewrite that uses the
initialiser expression has to know that the value is a *snapshot*: several blocks read `bipedHead.rotateAngleY` once
and then assign it to eleven renderers. They are correct as written, they are confined to model/animation code, and
they should be cleaned one file at a time with the pose logic in front of you - not swept.

### 16.4 Where P1 ended up

| Item | State |
|---|---|
| B5, B6, B7, B8, B9, B10, B11, B12, B13, B14 | fixed (§11–§16) |
| B15: `TCInfo`, `TCNames` constants, `TileEntityFactory`, `TCPacketEvents` no-op, `Tropicraft.dbg` | deleted (§13) |
| B15: `System.out`/`err`, blanket `printStackTrace`, empty `if`s, `new Random()` on hot paths | done (§13, §16.2) |
| B15: alias locals | **deferred on purpose** (§16.3) |
| B15: redundant `(Class)`/`(Item)`/`(Block)` casts, float literals written as doubles | deferred as noise; a mechanical sweep risks changing overload resolution and double rounding (§13.1) |

---

## 17. P2 batch 1 — naming and the encyclopedia index

Both refactors were done with a characterization check in place first: the in-game suite grew assertions that pin
the *observable outcome* (the names every item and block reports, and what a recipe lookup returns), the baseline was
recorded, the code changed, and the same assertions had to stay green.

### 17.1 R2: one naming helper instead of ~35 copies

The review said six classes; `grep` found the `getUnlocalizedName()` + `getActualName(String)` pair copied into **35**
classes - 19 blocks and 16 items. They were byte-identical apart from whether they build an `item.` or a `tile.` name
and whether the source string is `super.getUnlocalizedName()` or a texture-name helper.

`net.tropicraft.util.TropicraftNames` now holds the whole convention (`item(rawName)`, `block(rawName)`,
`afterFirstDot(name)`), and every class delegates in one line. The note about the interface-with-a-default-method
idea, for the record: it cannot work. `getUnlocalizedName` is declared by `Item`/`Block`, and a default method loses
to an inherited class method, so it would never run.

**How it was checked.** Two new in-game assertions, added *before* the change:

| Check | Baseline | After |
|---|---|---|
| every Tropicraft item is named `item.<registry name>` | 75 checked, 2 exceptions | identical |
| every Tropicraft block is named `tile.tropicraft:<name>` | 49 checked, 1 exception, 3 not converted | identical |

The exceptions are real and pre-existing, and are now allowlisted *with their reason* rather than silently skipped:
`tropicraft:fishingRodTropical` and `tropicraft:bucketTropicsWater` report an un-namespaced name (their lang keys
exist in that form; renaming them would break every resource pack that overrides those two lines), and
`tropicraft:tile.pineapple` deliberately shares the tall-flower naming scheme, because the item appends
`_Pineapple` to build `tile.tropicraft:tallFlower_Pineapple.name`. Three blocks (`bambooDoor`, `tropicsWater`,
`bambooMug`) never joined the convention at all; the check reports them separately instead of failing, so they stay
visible without turning into noise.

Unit tests cover the helper's own edge cases, including the one that matters: a name with no dot is returned
unchanged (`indexOf` returns -1, so the old code took the whole string) - several texture paths depend on it.

### 17.2 R5: the encyclopedia recipe index

`HashMap<ItemStack, List<ShapedRecipes>>` relied on `ItemStack` not overriding `equals`/`hashCode` in 1.7.10, i.e. on
identity. Every lookup therefore had to fall back to a linear `isItemEqual` scan over the key set - two per rendered
recipe page - and `includeRecipe` could only append to an existing entry by scanning for it. A third identity-keyed
collection (`HashSet<ItemStack>` for a recipe's ingredients) had the same problem from the other direction: each
lookup had to scan it to deduplicate.

Both are now keyed by a private `ItemKey` (item + damage, which is exactly what `isItemEqual` compares), so:

* `includeRecipe` appends with one `map.get`;
* `getRecipesForEntry` is one `map.get` per entry item instead of a scan per entry item;
* the ingredient set is a `LinkedHashMap<ItemKey, ItemStack>`, which also preserves the order the ingredients are
  first seen instead of hash order.

Five unit tests pin the semantics, which are unchanged: an equal-but-not-identical stack finds the recipes (that is
what the scans existed for), damage is part of the identity, different items never share recipes, a recipe with two
ingredients is listed once for each of them (existing behaviour: `getRecipesForEntry` concatenates the recipes of
every item the page shows), and a duplicate ingredient is indexed once.

One accepted behaviour change: the order recipes are listed in can differ from before, because it used to be
identity-hash order. The old order was arbitrary, not meaningful.

### 17.3 What was verified

`./gradlew check` (51 unit tests, coverage floors, spotless, licence) and `./gradlew gameTestServer` (26 in-game
checks) are green; unit coverage went from 121 to 232 executed lines with the new tests. The naming checks are the
important part: they are the only thing in this repository that would notice if a `getUnlocalizedName` refactor
renamed an item.

### 17.4 Still open, and why

* **R1 (renderers)** needs a client. The three `doRender` copies only run on the client, and this harness is a
  dedicated server: nothing in it can tell a correct `super.doRender` call from a subtly wrong one, so the "no
  regressions" guarantee the rest of this work has would be missing. Do it in a branch that can be eyeballed in-game
  (fish, turtles and marlins are all easy to spawn).
* **R4 (registries)** is mechanical but large. `TCCraftingRegistry` helper extraction deserves a recipe
  characterization check first (registered recipe count, plus the output of a handful of named recipes), which is
  cheap to add and would make the extraction safe - next batch.
* **R6 (cocktail/colour mixer)** needs the same treatment and is the least understood of the three.

---

## 18. P2 batch 2 — the recipe registry (R4)

### 18.1 The check came first

Before touching the registry, the in-game suite grew a recipe fingerprint: every crafting and smelting recipe that
touches a Tropicraft item is reduced to a stable one-line description (output, count, shape, ingredients in slot
order), the list is sorted, and the check asserts both the count and a CRC32 of the listing:

```
139 crafting recipes, crc 2e72ec27
  6 smelting recipes, crc 4be27cea
```

The full listing is written to `build/gametest/recipes-crafting.txt` and `recipes-smelting.txt` on every run, so a
failure can be diffed instead of guessed at. Both numbers were recorded before the refactor and are unchanged
after it.

That the check actually bites was verified by perturbing one recipe (dart output count `4` → `5`): the run failed
with `crc f71d3c14 (expected 2e72ec27)` and the change reverted.

(A caution from writing it: the first green-ish run *failed* with matching values because the strings were compared
with `==`. The failure message prints both values, which is what made that visible in one look - worth the couple of
extra lines in the message.)

### 18.2 What changed in `TCCraftingRegistry`

* **A `recipe(output, pattern...)` shorthand** for what 86 call sites were spelling out as
  `createRecipe(true, output, new Object[] { ... })`. The script that rewrote them parsed balanced parentheses and
  asserted the first argument was the literal `true` (the "also add to the encyclopedia" flag), so the flag cannot
  have changed by accident.
* **The dead `isServer` flag.** `createRecipe(boolean isServer, boolean addToEncyclopedia, ...)` ignored `isServer`
  and was byte-identical to the three-argument overload; `initDartRecipes(boolean isServer)` passed it down. Both
  gone.
* **61 redundant `(Item)`/`(Block)` casts** removed from `TCItemRegistry` (the fields have concrete types, so the
  overload resolution is unchanged - the recipe fingerprint and the item-name check both confirm it).

`TCCraftingRegistry` went from 670 to 432 lines, and the recipe bodies are now one line each instead of four.

### 18.3 What was deliberately not done

* **Splitting `TCItemRegistry.init()` into `initFood()`/`initTools()`/…** The review asked for it "for
  reviewability", but the list is not grouped by category - splitting it either reorders the registrations (which
  changes the `Item.itemRegistry` id order every other mod sees at startup) or produces arbitrary contiguous blocks
  that are harder to follow than the list. Instead the method documents the ordering contract, which is the part a
  reviewer actually needs to know.
* **`ringRecipe`/`toolsRecipe`/`storageRecipe` helpers.** The repetition in the registry is not actually shaped like
  those patterns: recipes differ in ingredient, shape *and* result count, so the "helpers" would take as many
  arguments as the recipe itself and only hide the shape. The one honest repetition - the four-line `createRecipe`
  wrapper - is what `recipe(...)` removes. If a future pass wants those helpers, the fingerprint above makes it
  safe.

`TCRenderIDs` got the "assigned once in preInit, meaningless on a dedicated server" contract documented, which was
the last item in R4.

### 18.4 Verification

`check` green (51 unit tests, coverage floors, spotless, licence) and `gameTestServer` green with **28** checks,
including the two new recipe fingerprints. In-game coverage rose from 3127 to 3293 executed lines because the check
itself walks the recipe list; the recipe registry refactor removed ~240 lines from the mod.

---

## 19. Batch: targeting (#50), build metadata, and the cast noise

### 19.1 B27 — creative and peaceful targeting

Five in-game checks were written first; **three of them failed on the old code**, which is what makes the fixes
worth trusting:

| Check | Old code |
|---|---|
| a creative player's hit does not become a Koa's target | **FAIL** — the Koa targeted the FakePlayer that hit it |
| a creative player is not an enemy | passed (the Koa simply ignored players) |
| a passive animal is not a hostile mob | **FAIL** — `EntityIguana implements IMob` |
| the Easter Island Head does not survive peaceful | **FAIL** — still alive |
| the Ashen does not survive peaceful | **FAIL** — still alive |
| the Koa do not survive peaceful | passed (inherited from `EntityCoroAI`) |

The root causes and what changed:

* **Retaliation bypassed every filter.** CoroUtil's `AIAgent` has an expiry: when `retaliateTicks` runs out it drops
  the target *if* `isEnemy` says no. Until then, though, `JobHunt.hookHit` has already recorded the attacker, and
  `JobAttackTargetShare` hands that target to every mob within 24 blocks. `EntityCoroAI.onLivingUpdate` now clears a
  creative player from both `entityToAttack` and `retaliateEntity` every tick - the same two fields CoroUtil itself
  clears on expiry, so this follows the library's own semantics instead of fighting them.
* **`EntityKoaBase.isEnemy` replaced the creative check.** It overrode `EntityCoroAI.isEnemy` with a bare diplomacy
  call. Koa stay neutral towards players (that is the design), but a creative player now returns false before the
  diplomacy call.
* **`JobExtraTargetting`** assigned `getClosestVulnerablePlayerToEntity` straight into `entityToAttack` with no
  `isEnemy` check. It now asks first.
* **`EntityIguana`** no longer declares `IMob`; it is an animal.
* **Peaceful.** `EntityCoroAI`'s mobs removed themselves, but `EntityEIH` and the Ashen family do not inherit that
  and **vanilla does not sweep hostile mobs when the difficulty changes** (measured: a spawned EIH survived a
  peaceful tick, which also settles the bytecode question the review left open). Both now remove themselves through
  a shared `TropicraftUtils.isPeaceful(world)`.

One deliberate non-change: vanilla's own `EntityMob`/`EntityAITarget` already handles creative players for the Ashen
family, so they only needed the peaceful half.

### 19.2 Build & metadata

* `mcmod.info` now declares `"dependencies": ["CoroAI", "unimixins"]`, which is what the mod list shows and what the
  `@Mod` annotation already requires.
* The README notes that Jabel downgrades **syntax only, not APIs** - a `List.of(...)` compiles and then
  `NoSuchMethodError`s on Java 8.
* `TROPICS_DIMENSION_ID` is now `ConfigMisc.tropicsDimensionId` (default -127). Two things worth knowing about how
  it is wired: the field is **not** `final`, because a `static final int` with a constant initialiser is inlined at
  every use site and a config value would never reach them; and `initializeDimension()` checks both
  `registerProviderType` and `isDimensionRegistered` and logs at severe, so a collision with another mod shows up in
  the log instead of as a dimension that silently does not exist.
* `jitpack.yml` left alone, as agreed.

### 19.3 Noise

**510 redundant casts** removed across 72 files - 319 `(Block)`, 171 `(Item)`, 18 `(Class)`, 2 `(IInventory)`.
Verified by the full suite; the two that had to go back (`ItemStack((Item) aobj[i + 1])` in `Encyclopedia`) were
caught by the compiler, because the vanilla recipe array is `Object[]` and `ItemStack` has separate `Item` and
`Block` constructors - a good reminder that "redundant cast" and "unnecessary cast" are not the same thing.

**3 unused alias locals** removed (`RenderSnareTrap`'s three `renderManager` copies).

Deliberately left: the other 91 `final X y = this.z;` locals, because they are *used* - they are the decompiler's
local-copy idiom in model/animation code, and §16.3 has the full argument for why a sweep corrupts them. Also left:
float literals written as doubles, for the rounding reason in §13.1, and the `(Entity)`/`(EntityLivingBase)` casts,
where 256 more removals would buy little but carry the real risk (silently selecting a different overload).

### 19.4 Verification

`check` green (51 unit tests, coverage floors, spotless), `gameTestServer` green with **35** checks - six of them
about targeting and mob classification. The recipe fingerprint and the item/block naming checks also stayed
identical through the 510-cast change, which is what confirms none of those casts were load-bearing.

---

## 20. R1, and the last of the noise

### 20.1 R1 — one render body instead of four

The three copies were not identical, but the only part that differed was the orientation block. R1 as it turns out
had a fourth copy hiding in plain sight: `RenderTropicalFish` extends `RenderWaterMob` and is the only thing that
uses it, so every water mob in the mod was rendering through its own copy of vanilla's
`RendererLivingEntity#doRender`.

`RenderWaterMobBase` now owns that body once - glint pass, hurt/death overlay, the four render passes, the lighting
texture juggling - and every entry point vanilla could use (`doRender(Entity)`, `doRender(EntityLivingBase)`,
`doRender(EntityLiving)`, `func_110827_b`) routes straight to it, so the body cannot run twice or fall through to
vanilla's version. Each subclass implements one method:

```java
protected abstract void orientWaterMob(EntityLivingBase entity, float rotation, float yawOffset, float pitch,
    float partialTicks);
```

| Class | Before | After |
|---|---|---|
| `RenderWaterMobBase` | — | 253 (the shared body) |
| `RenderWaterMob` | 174 | 38 |
| `RenderMarlin` | 207 | 61 |
| `RenderSeaTurtle` | 188 | 48 |
| `RenderTropicalFish` | 59 | 59 |
| **total** | **628** | **459** |

Behaviour that was deliberately preserved, because a renderer is exactly where "looks the same" is the only test:

* `RenderWaterMob` only calls `rotateCorpse` while the mob is in water or surfacing, and flips it 90° onto its side
  when not - the two conditions are exact complements, so the `if`/`if` became `if`/`else`.
* `RenderMarlin` calls `rotateCorpse` unconditionally, uses the **raw** `rotationPitch` in water (not the
  interpolated one) and the `motionY * 45°` arc out of it, and rolls from `outOfWaterTick * 4°` up to 90° -
  except while on a hook, where it snaps straight to 90°.
* `RenderSeaTurtle` uses the **interpolated** pitch, and only in water.
* The `inWater` flag each model uses for its animation is still set on every render path (moved from the removed
  `renderXxx` methods into a `renderWaterMob` override).
* The name tag pass is still not run - only `passSpecialRender` - which is what the copies did.
* The render exception is still swallowed, but logged at debug rather than `printStackTrace()` per frame.

### 20.2 Float literals written as doubles

109 literals like `0.10000000149011612` became `0.1f`. This is the one noise item that can be made *provably*
safe rather than merely "looks fine": a literal is rewritten only when
`(double)(float)value == value` **exactly**, in which case the float literal promotes back to the identical double
in every context - argument, field assignment, `Math` call, whatever.

That test matters. My first pass compared the values *as floats* instead, which also accepts constants that are
meant to be doubles: `3.141592653589793` (Math.PI) sneaked through as `3.1415927f`, which is a different double and
would have changed every `Math.sin`/`Math.toRadians` result downstream. The strict round-trip predicate excludes
those, so 80 literals were rewritten rather than 109, and Math.PI stayed.

### 20.3 The alias locals

64 `final ModelRenderer alias = this.field;` declarations were inlined in `ModelKoaMan` (55), `ModelAshen` (3) and
`ModelTropiCreeper` (6). The rule was deliberately narrow: a declaration is removed only when *every* remaining use
is `<alias>.<field> = ...`, i.e. the alias is a plain reference copy. That is exactly the case the review got wrong
(§16.3) - and note the aliasing is not a rename: `final ModelRenderer leaf2 = this.leaf3;`. Inlining rewrites uses
to the field in the initialiser, so `leaf2.rotateAngleX = ...` becomes `this.leaf3.rotateAngleX = ...`.

The 29 remaining `final float rotateAngleX = this.head.rotateAngleX;` locals stay: those copy a *value*, and the
model assigns it to several renderers afterwards. Removing them would re-read a value that may have been written in
between - the exact trap that made the earlier sweep wrong.

### 20.4 Manual verification checklist

The unit and in-game suites say nothing about pixels, so this is the part that needs eyes. Nothing here should look
different from 6.2.5:

| What | Where | Looks right when |
|---|---|---|
| Tropical fish | spawn a few, net them out of water | upright in water; flips onto its side out of water; the mask renders on body **and** tail |
| Marlin | in water, then let one jump/leave the water | nose follows its pitch in water; out of water it arcs by its fall and rolls onto its side; a **fished** marlin flops at 90° immediately |
| Sea turtle | adult vs a young one | pitch follows the body only in water; scale grows with age |
| Hurt flash | hit any of the three | red overlay, then gone again |
| Koa | look at a Koa's leaf decoration and headband | leaves and headband tilt with the head in all three axes |
| Creeper | look at the three hat layers | hats tilt with the head |
| Ashen | watch its arms | arms swing with movement |
| Any mob | walk around a bit | no visual errors in the log; nothing frozen or double-drawn |

If something is wrong, the two halves can be undone independently without touching each other:

```sh
# renderers only (the first four rows of the table above)
git checkout cb73bf6 -- src/main/java/net/tropicraft/client/entity/render/

# models only (the Koa / creeper / Ashen rows)
git checkout cb73bf6 -- src/main/java/net/tropicraft/client/entity/model/
```

---

## 21. Play-test batch: blended sands, reefs, and the §7 features

### 21.1 B28 — blending the decorative worldgen

The two problems were independent, so they got independent fixes.

**Mineral sands.** The variant was one `rand.nextInt(200)` per chunk applied to every beach and ocean column of that
chunk. It is now `MineralSandField.variantAt(seed, x, z)` - a pure function of the world coordinates:

* a low frequency *patch* field (40-block lattice) decides where mineral sand may appear at all, so a patch is a few
  chunks across;
* the outer band of the patch is dithered per column, so the edge speckles into ordinary sand instead of ending on a
  line;
* inside a patch a finer *variant* field (14-block lattice) picks which of the four sands, so a patch is mottled
  rather than one flat colour.

The patch field lands roughly 20% of beach columns on mineral sand (the old per-chunk roll averaged 8%), spread over
a few hundred blocks of coast, with the four variants in an uneven but all-present mix.

**Reefs.** `WorldGenCoral` took its spread as a constant ±8 blocks around a point inside one chunk, so a reef could
not leave that chunk; `BiomeGenTropicsOcean` now asks for a ±24 block spread anchored at the chunk centre. While
there: the base `BiomeGenTropicraft.decorate` ran the same 1-in-5 coral roll *as well* for ocean chunks, so every
ocean chunk rolled for a reef twice - that duplicate is gone.

**Coverage.** Seven unit tests on `MineralSandField` (deterministic, clustered rather than speckled, all variants
present, patch edges exist, plain sand still dominant) plus the one that pins the actual regression:
`aChunkBorderIsNotSpecial` - the rate at which neighbouring columns differ across a chunk border must match the rate
inside a chunk. With the old per-chunk roll that test is 0 inside a chunk and 1 at the border. In game:
`mineral sands blend across chunks` (a synthetic beach through the real `replaceBlocksForBiome` over 12 chunks, which
must show a mix inside at least one chunk) and `coral reefs span chunks` (a built sea floor, coral has to land in more
than one chunk).

### 21.2 The §7 commands

* **`/tc version`** — prints `Tropicraft <Tags.VERSION> (Minecraft 1.7.10, server)`. Level 0, because a version string
  is not privileged.
* **`/tc time [get|set <time>|sync]`** — `get` prints the listing as before, `set` moves only the Tropics clock, and
  `sync` copies the overworld's clock and weather into the Tropics, for a pack dev migrating a world that predates
  separate clocks.
* **`/tc village <locate|list|try|regen>`** — the issue #49 tools, rebuilt rather than restored: `list` and `locate`
  describe the villages the world director is simulating, `try` runs `TownKoaVillageGenHelper.hookTryGenVillage` at
  your feet (the harness #34 needs), and `regen` re-queues the nearest village's structure. They check operator
  permission themselves at level 4, because `/tc` as a whole only requires 2 and a sub-command cannot rely on the
  root's check. `village_clear` and `village_repopulate` are deliberately **not** back: the first removed every
  ticking location in the dimension, the second was an empty loop.

While wiring these: `TropicraftWorldUtils.tropics()` and the `sync` path both assumed a running server and threw an
NPE without one; both now return null / report cleanly.

### 21.3 The rest of §7

* **Volcano toggle** — `ConfigGenRates.genVolcanoes` (default on) gates `MapGenVolcano`. There is no meteor feature in
  the mod, so that half of the review's item was a guess.
* **`ItemFireArmor`** — the review worried about the damage-type handling, which turned out to be fine; the *reflection*
  was the bug. `getFXLayers` looked the field up by the name `"fxLayers"`, which is the **development** name: in a
  release jar it is `field_78477_e`, so the lookup failed, the list stayed null and the chestplate's particle
  redirection silently never ran in production. It now finds the renderer's one `List[]` field by type, which cannot
  be obfuscated away, and the identical double `try/catch` plus an empty `if` are gone.

Coverage for all of this: eleven mock-sender unit tests (`CommandTest`) covering the new commands' messages, usage,
tab completion, permission levels and failure paths, plus the worldgen tests above.

---

## Appendix A — quick-win checklist

- [x] Ship `koavillage.schematic` + `TCSchematics` extractor; fail loudly on a bad build (B1) — §11.1
- [x] Dev runtime: `devOnlyNonPublishable(rfg.deobf(...))` for coroutil, or no dev run works at all (§11.6)
- [x] `dependencies.gradle`: `api` for coroutil was already right (§8); the dev runtime needs
      `api(rfg.deobf(...))` (§11.6)
- [x] `mcmod.info`: add `CoroAI`, `unimixins` dependencies (§19.2)
- [x] Delete `info/TCInfo.java`, `TileEntityFactory` (`getTopWaterBlockY` is gone, §12)
- [x] Delete duplicate `registerEntity(EntityTropiCreeper…)`; convert entity IDs to literals (B9, §12)
- [x] `ItemEntry.matchesItem`: reference → `getItem()` comparison; delete the empty `else if` (§11.4)
- [x] `EntityDart`: `static final int[] POTIONS`, bounds-check `dartType`, delete the empty `if` (§11.2)
- [x] `ChunkProviderTropicraft`: reset `flag` per column, local `sandMetadata` (§11.3), `fallInstantly` in
      `finally` (§12)
- [x] `JobFish`: `< 0.0f` → `<= 0.0f`, delete `rand.nextInt(1)`, use `worldObj.rand` (§12)
- [x] `EffectHelper`: tick on one side only (§12)
- [x] `TropicalBook`: per-world save file, null-check the resource stream, try-with-resources (§16.1)
- [x] `Tropicraft.dbg`: delete or gate behind a config key (§13)
- [x] Replace the remaining `System.out/err` with `FMLLog` at appropriate levels (42 → 0 in `src/main/java`, §16.2)
- [x] Replace the remaining `catch (Exception) { printStackTrace(); }` (21 → 0 in `src/main/java`, §16.2)
- [x] Extract `RenderWaterMobBase` for the three duplicated `doRender`s (R1) — §20.1, hand-verified per §20.4
- [x] Extract the duplicated `getUnlocalizedName` logic (R2 shipped as `TropicraftNames`, §17.1)
- [x] `BlockRainforestLeaves.imageNames[1][0]` → `"leafMahogany"` and `[0][1]` → `"leafKapok"` (B24, #9, §12)
- [x] Delete the two duplicate `*Frame` lang lines (B25, #3, §12)
- [x] `ItemCoconutBomb`: move `--stackSize` inside the whitelist branch; add an enabled toggle (B23, #45, §12)

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
