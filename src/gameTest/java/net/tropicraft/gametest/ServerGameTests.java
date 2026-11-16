package net.tropicraft.gametest;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.entity.koa.EntityKoaBase;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCEntityRegistry;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;
import net.tropicraft.util.TCSchematics;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.chunk.ChunkProviderTropicraft;
import net.tropicraft.world.location.TownKoaVillage;

import build.world.Build;
import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * The in-game assertions. Everything here needs the real game: registered blocks and items, a live
 * {@link WorldServer}, CoroUtil's build engine and Tropicraft's own chunk provider. Pure logic is covered by the
 * JUnit suite in {@code src/test/java}.
 * <p>
 * Each check logs a {@code [TC-GAMETEST] ok}/{@code FAIL} line, and the process exit code matches the result.
 * </p>
 */
public final class ServerGameTests {

    private static final String FAIL = "[TC-GAMETEST] FAIL ";
    private static final String PASS = "[TC-GAMETEST] ok   ";

    private static int failures;
    private static int checks;

    private ServerGameTests() {}

    public static int checkCount() {
        return checks;
    }

    /**
     * Runs every assertion.
     *
     * @return the number of failed assertions
     */
    public static int runAll() {
        // First: it is the only check that wants a nearly empty pending-decoration set (the shutdown flush walks
        // everything that is parked, and forcing the ring around each parked chunk is the expensive part).
        decorationFlushForSaveDecoratesParkedChunks();
        villageSchematicLoads();
        villageBuildQueues();
        economyValuesResolveToRealItems();
        dartPotionIdsAreBoundsChecked();
        mineralSandDoesNotLeakFromAnOceanColumn();
        tropicraftBlocksKeepTheirRegistryPrefix();
        entityIdsAreUniqueAndRoundTrip();
        koaChestKeepsItsHousehold();
        tropicalFishTicksWithoutItsSchoolPath();
        tropicsChunksGenerate();
        deferredDecorationFlushesWhenItsNeighbourhoodLoads();
        return failures;
    }

    // ---------------------------------------------------------------------------------------------------------
    // The Koa village (regression: it silently generated nothing at all)
    // ---------------------------------------------------------------------------------------------------------

    private static void villageSchematicLoads() {
        String basePath = null;
        try {
            basePath = TCSchematics.getBasePath("koavillage");
        } catch (final Throwable t) {
            fail("village schematic resolves", t.toString());
            return;
        }
        if (basePath == null) {
            fail("village schematic resolves", "TCSchematics.getBasePath returned null");
            return;
        }
        ok("village schematic resolves");

        final File file = new File(basePath + TCSchematics.EXTENSION);
        assertTrue("village schematic is extracted", file.isFile(), "no file at " + file);

        final Build build = new Build(0, 64, 0, basePath);
        assertTrue(
            "village schematic parses through CoroUtil",
            build.levelData != null && build.map_sizeX == 86 && build.map_sizeY == 16 && build.map_sizeZ == 76,
            "levelData=" + (build.levelData != null)
                + " size="
                + build.map_sizeX
                + "x"
                + build.map_sizeY
                + "x"
                + build.map_sizeZ);
    }

    private static void villageBuildQueues() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("village queues a build job", "the Tropicraft dimension did not load");
            return;
        }
        try {
            // Make sure the area the build targets exists before handing it to the build manager.
            tropics.getChunkFromChunkCoords(0, 0);
            final TownKoaVillage village = new TownKoaVillage();
            village.initData(4242, TropicraftWorldUtils.TROPICS_DIMENSION_ID, new ChunkCoordinates(8, 64, 8));
            village.direction = 0;
            village.genStructure();
            assertTrue(
                "village queues a build job",
                village.isStructureQueued(),
                "genStructure() did not queue anything - the schematic is unreadable");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("village queues a build job", t.toString());
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // The Koa economy (regression: matchesItem compared stacks by identity, so nothing could ever be traded)
    // ---------------------------------------------------------------------------------------------------------

    private static void economyValuesResolveToRealItems() {
        final ItemStack currency = TCKoaCurrencyRegistry.currency;
        assertTrue(
            "the Koa pearl is valued",
            currency != null && ItemValues.getItemEntry(new ItemStack(currency.getItem(), 1, 0)) != null,
            "ItemValues.getValue returned null for the currency item");

        int matched = 0;
        for (int meta = 0; meta < TCNames.shellNames.length; ++meta) {
            if (ItemValues.getItemEntry(new ItemStack(TCItemRegistry.shells, 1, meta)) != null) {
                ++matched;
            }
        }
        assertTrue(
            "every shell variant is valued",
            matched == TCNames.shellNames.length,
            matched + "/" + TCNames.shellNames.length + " shells matched");

        final ItemEntry fish = ItemValues.getItemEntry(new ItemStack(net.minecraft.init.Items.fish));
        assertTrue("fish is a buyable trade good", fish != null && fish.buyable, "no buyable entry for fish");
    }

    // ---------------------------------------------------------------------------------------------------------
    // Darts (regression: an unvalidated dartType indexed the potion table out of bounds)
    // ---------------------------------------------------------------------------------------------------------

    private static void dartPotionIdsAreBoundsChecked() {
        try {
            final Method getPotionId = EntityDart.class.getDeclaredMethod("getPotionId", int.class);
            getPotionId.setAccessible(true);
            final int known = (Integer) getPotionId.invoke(null, 1);
            final int paralysis = (Integer) getPotionId.invoke(null, 0);
            final int unknown = (Integer) getPotionId.invoke(null, 99);
            final int negative = (Integer) getPotionId.invoke(null, -1);
            assertTrue("dart potion ids resolve", known > 0, "type 1 resolved to " + known);
            assertTrue(
                "unknown dart types do not resolve to a potion",
                unknown < 0 && negative < 0 && paralysis < 0,
                "99 -> " + unknown + ", -1 -> " + negative + ", 0 -> " + paralysis);
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("dart potion ids resolve", t.toString());
            return;
        }

        final WorldServer overworld = MinecraftServer.getServer()
            .worldServerForDimension(0);
        if (overworld == null) {
            fail("a dart with an unknown type ticks", "no overworld");
            return;
        }
        try {
            final EntityDart dart = new EntityDart(overworld);
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("dartType", (short) 99);
            dart.readEntityFromNBT(tag);
            dart.setPosition(0.5, 250, 0.5);
            dart.motionY = -0.05;
            dart.onUpdate();
            ok("a dart with an unknown type ticks");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("a dart with an unknown type ticks", t.toString());
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Terrain (regression: the column flag and the sand metadata leaked across columns of a chunk)
    // ---------------------------------------------------------------------------------------------------------

    private static void mineralSandDoesNotLeakFromAnOceanColumn() {
        final WorldServer overworld = MinecraftServer.getServer()
            .worldServerForDimension(0);
        if (overworld == null) {
            fail("an ocean column does not sand over inland columns", "no overworld");
            return;
        }
        try {
            final ChunkProviderTropicraft provider = new ChunkProviderTropicraft(overworld, 1L, false);
            final Block[] blocks = new Block[65536];
            final byte[] metas = new byte[65536];
            final BiomeGenBase[] biomes = new BiomeGenBase[256];
            final BiomeGenBase land = BiomeGenTropicraft.tropics;
            final BiomeGenBase ocean = BiomeGenTropicraft.tropicsOcean;

            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    // Column 0 is processed first, so it is the one whose state used to leak into the rest.
                    final boolean isOcean = x == 0 && z == 0;
                    biomes[x + z * 16] = isOcean ? ocean : land;
                    for (int y = 0; y < 128; ++y) {
                        blocks[x << 12 | z << 8 | y] = columnBlock(isOcean, y);
                    }
                }
            }

            provider.replaceBlocksForBiome(0, 0, blocks, metas, biomes);

            assertTrue(
                "the ocean column became sand",
                blocks[63] == Blocks.sand,
                "ocean surface y=63 is " + blocks[63]);

            int wrongTop = 0;
            int wrongMeta = 0;
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    if (x == 0 && z == 0) {
                        continue;
                    }
                    final int index = x << 12 | z << 8 | 70;
                    if (blocks[index] != land.topBlock) {
                        ++wrongTop;
                    }
                    if (metas[index] != 0) {
                        ++wrongMeta;
                    }
                }
            }
            assertTrue(
                "an ocean column does not sand over inland columns",
                wrongTop == 0,
                wrongTop + " inland columns lost their grass surface");
            assertTrue(
                "inland columns do not inherit the sand metadata",
                wrongMeta == 0,
                wrongMeta + " inland columns were written with a non-zero data value");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("an ocean column does not sand over inland columns", t.toString());
        }
    }

    /** Synthetic terrain: sea floor at 38, water to 62, sand at 63; inland stone to 65, dirt to 69, grass at 70. */
    private static Block columnBlock(final boolean isOcean, final int y) {
        if (isOcean) {
            if (y <= 38) {
                return Blocks.stone;
            }
            if (y <= 62) {
                return TCBlockRegistry.tropicsWater;
            }
            return y == 63 ? Blocks.sand : Blocks.air;
        }
        if (y <= 65) {
            return Blocks.stone;
        }
        if (y <= 69) {
            return Blocks.dirt;
        }
        return y == 70 ? Blocks.grass : Blocks.air;
    }

    // ---------------------------------------------------------------------------------------------------------
    // Registry naming (the village schematic keys depend on it, see TCSchematicsAssetTest)
    // ---------------------------------------------------------------------------------------------------------

    private static void tropicraftBlocksKeepTheirRegistryPrefix() {
        final List<String> unstyled = new ArrayList<String>();
        int counted = 0;
        int total = 0;
        final Set<String> keys = Block.blockRegistry.getKeys();
        for (final Object key : keys) {
            final String name = (String) key;
            if (!name.startsWith("tropicraft:")) {
                continue;
            }
            ++total;
            if (name.startsWith("tropicraft:tile.")) {
                ++counted;
            } else {
                unstyled.add(name);
            }
        }
        assertTrue(
            "every Tropicraft block is registered as tropicraft:tile.<name>",
            total > 0 && unstyled.isEmpty(),
            counted + "/" + total + " kept the prefix, non-conforming: " + unstyled);
    }

    // ---------------------------------------------------------------------------------------------------------
    // Entity registration
    // ---------------------------------------------------------------------------------------------------------

    /**
     * The numeric mod entity id travels in {@code FMLMessage.EntitySpawnMessage} and is what the client resolves
     * through {@code EntityRegistry.lookupModSpawn}, so ids have to be unique and stable across both sides. They used
     * to be assigned by call order, which silently re-pointed every following id whenever a registration line was
     * inserted, removed or reordered - and a duplicated line burned one.
     */
    private static void entityIdsAreUniqueAndRoundTrip() {
        try {
            final Map<Class<? extends Entity>, Integer> registered = TCEntityRegistry.getRegistered();
            if (registered.isEmpty()) {
                fail("entity ids are unique", "no entities were registered at all");
                return;
            }
            final Set<Integer> ids = new HashSet<Integer>(registered.values());
            assertTrue(
                "every Tropicraft entity has a unique network id",
                ids.size() == registered.size(),
                registered.size() + " entities but only " + ids.size() + " distinct ids");

            int roundTripped = 0;
            final List<String> mismatched = new ArrayList<String>();
            for (final Map.Entry<Class<? extends Entity>, Integer> entry : registered.entrySet()) {
                final EntityRegistry.EntityRegistration registration = EntityRegistry.instance()
                    .lookupModSpawn(entry.getKey(), false);
                if (registration != null && registration.getModEntityId() == entry.getValue()
                    && registration.getEntityClass() == entry.getKey()) {
                    ++roundTripped;
                } else {
                    mismatched.add(
                        entry.getKey()
                            .getSimpleName() + "="
                            + entry.getValue()
                            + (registration == null ? " (not registered)"
                                : " (registry says " + registration.getModEntityId() + ")"));
                }
            }
            assertTrue(
                "every entity id round-trips through the FML registry",
                roundTripped == registered.size(),
                roundTripped + "/" + registered.size() + " ok, mismatched: " + mismatched);
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("every Tropicraft entity has a unique network id", t.toString());
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Koa chest household
    // ---------------------------------------------------------------------------------------------------------

    /**
     * A Koa chest keeps {@code housePop} members. It used to decide that from a live scan of the world, and to drop
     * the ids of anything spawned while both slots were taken - so a chest whose members had wandered more than 160
     * blocks away spawned a fresh pair on every visit, while the old pair was still alive and untracked.
     */
    private static void koaChestKeepsItsHousehold() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("a Koa chest keeps its household", "the Tropicraft dimension did not load");
            return;
        }
        final int x = 64;
        final int y = 100;
        final int z = 64;
        try {
            tropics.getChunkFromChunkCoords(x >> 4, z >> 4);
            tropics.getChunkFromChunkCoords((x + 200) >> 4, z >> 4);
            final int before = countKoa(tropics);
            tropics.setBlock(x, y, z, TCBlockRegistry.koaChest);
            final TileEntity tile = tropics.getTileEntity(x, y, z);
            if (!(tile instanceof TileEntityKoaChest)) {
                fail("a Koa chest keeps its household", "placing the chest did not create its tile entity");
                return;
            }
            final TileEntityKoaChest chest = (TileEntityKoaChest) tile;

            chest.spawnKoa(tropics);
            assertTrue(
                "a Koa chest fills its household",
                occupiedSlots(chest) == chest.housePop && countKoa(tropics) - before == chest.housePop,
                occupiedSlots(chest) + "/" + chest.housePop + " slots, " + (countKoa(tropics) - before) + " spawned");

            // Members that die while loaded are replaced.
            for (final EntityKoaBase member : ownedMembers(tropics, chest)) {
                member.setDead();
            }
            chest.spawnKoa(tropics);
            assertTrue(
                "a Koa chest replaces dead members",
                occupiedSlots(chest) == chest.housePop && ownedMembers(tropics, chest).size() == chest.housePop,
                occupiedSlots(
                    chest) + "/" + chest.housePop + " slots, " + ownedMembers(tropics, chest).size() + " alive");

            // Members that are merely out of the 160 block scan are still members: this is the regression.
            final int afterReplacement = countKoa(tropics);
            for (final EntityKoaBase member : ownedMembers(tropics, chest)) {
                member.setPosition(member.posX + 200.0, member.posY, member.posZ);
            }
            chest.spawnKoa(tropics);
            assertTrue(
                "a Koa chest does not re-spawn while its members are out of range",
                countKoa(tropics) == afterReplacement && occupiedSlots(chest) == chest.housePop,
                "the world went from " + afterReplacement + " to " + countKoa(tropics) + " Koa");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("a Koa chest keeps its household", t.toString());
        }
    }

    private static int countKoa(final WorldServer world) {
        int count = 0;
        for (final Object o : world.loadedEntityList) {
            if (o instanceof EntityKoaBase && !((EntityKoaBase) o).isDead) {
                ++count;
            }
        }
        return count;
    }

    private static List<EntityKoaBase> ownedMembers(final WorldServer world, final TileEntityKoaChest chest) {
        final List<EntityKoaBase> members = new ArrayList<EntityKoaBase>();
        for (final Object o : world.loadedEntityList) {
            if (!(o instanceof EntityKoaBase)) {
                continue;
            }
            final EntityKoaBase koa = (EntityKoaBase) o;
            if (koa.isDead) {
                continue;
            }
            final int id = koa.getAIAgent().entID;
            for (final int slot : chest.entIDs) {
                if (slot != -1 && slot == id) {
                    members.add(koa);
                    break;
                }
            }
        }
        return members;
    }

    private static int occupiedSlots(final TileEntityKoaChest chest) {
        int occupied = 0;
        for (final int slot : chest.entIDs) {
            if (slot != -1) {
                ++occupied;
            }
        }
        return occupied;
    }

    /**
     * The school-spawning block in {@code updateEntityActionState} was an empty loop behind a datawatcher flag that
     * every naturally constructed fish set to true, so it cleared itself on the first tick and did nothing. It is
     * gone now (implementing it would have made every fish spawn 3-6 more with no population cap, and the copy
     * constructor it called placed itself in an unbounded {@code do/while (!getCanSpawnHere())}). This keeps the
     * remaining state machine honest.
     */
    private static void tropicalFishTicksWithoutItsSchoolPath() {
        final WorldServer overworld = MinecraftServer.getServer()
            .worldServerForDimension(0);
        if (overworld == null) {
            fail("a tropical fish ticks", "no overworld");
            return;
        }
        try {
            final EntityTropicalFish fish = new EntityTropicalFish(overworld);
            fish.setPosition(8.5, 62.0, 8.5);
            final Method update = EntityTropicalFish.class.getDeclaredMethod("updateEntityActionState");
            update.setAccessible(true);
            update.invoke(fish);
            update.invoke(fish);
            assertTrue("a tropical fish ticks", !fish.isDead, "the fish died on its own update");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("a tropical fish ticks", t.toString());
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // World generation smoke test
    // ---------------------------------------------------------------------------------------------------------

    private static void tropicsChunksGenerate() {
        try {
            final WorldServer tropics = TropicraftWorldUtils.tropics();
            if (tropics == null) {
                fail("the Tropicraft dimension generates", "the dimension did not load");
                return;
            }
            int generated = 0;
            for (int chunkX = 0; chunkX < 2; ++chunkX) {
                for (int chunkZ = 0; chunkZ < 2; ++chunkZ) {
                    if (tropics.getChunkFromChunkCoords(chunkX, chunkZ) != null) {
                        ++generated;
                    }
                }
            }
            assertTrue(
                "the Tropicraft dimension generates",
                generated == 4,
                generated + "/4 chunks generated near the origin");
            final int height = tropics.getHeightValue(8, 8);
            assertTrue("generated terrain has a surface", height > 0 && height < 200, "height at 8,8 is " + height);
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("the Tropicraft dimension generates", t.toString());
        }
    }

    /**
     * The deferred-decoration gate: a chunk whose 9x9 neighbourhood is not loaded yet must park itself, and the
     * sweep (now throttled to one pass every few ticks) must decorate it once the ring exists. Chunks generated by
     * {@code tropicsChunksGenerate} are parked for exactly this reason.
     */
    private static void deferredDecorationFlushesWhenItsNeighbourhoodLoads() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        final ChunkProviderTropicraft provider = providerOf(tropics);
        if (provider == null) {
            fail("deferred decoration flushes when its neighbourhood loads", "no Tropicraft chunk provider");
            return;
        }
        try {
            final int parkedX = 300;
            final int parkedZ = 300;
            parkChunk(tropics, parkedX, parkedZ);
            if (!provider.isDecorationPending(parkedX, parkedZ)) {
                fail(
                    "deferred decoration flushes when its neighbourhood loads",
                    "chunk " + parkedX
                        + ","
                        + parkedZ
                        + " did not park; "
                        + provider.getPendingDecorationCount()
                        + " chunks are pending");
                return;
            }
            // Loading the ring is what the gate waits for; the outermost chunks of the ring park themselves in
            // turn, which is expected - only the target is under test.
            for (int dx = -4; dx <= 4; ++dx) {
                for (int dz = -4; dz <= 4; ++dz) {
                    tropics.getChunkFromChunkCoords(parkedX + dx, parkedZ + dz);
                }
            }
            for (int attempt = 0; attempt < 12 && provider.isDecorationPending(parkedX, parkedZ); ++attempt) {
                provider.unloadQueuedChunks();
            }
            assertTrue(
                "deferred decoration flushes when its neighbourhood loads",
                !provider.isDecorationPending(parkedX, parkedZ),
                "chunk " + parkedX
                    + ","
                    + parkedZ
                    + " is still parked with "
                    + provider.getPendingDecorationCount()
                    + " chunks pending");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("deferred decoration flushes when its neighbourhood loads", t.toString());
        }
    }

    /**
     * The shutdown flush: chunks still parked when the server stops are forced ready and decorated, so no chunk is
     * ever saved with terrain but without its decorations. Runs first so the parked set is small - it has to
     * generate a 9x9 neighbourhood per parked chunk.
     */
    private static void decorationFlushForSaveDecoratesParkedChunks() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        final ChunkProviderTropicraft provider = providerOf(tropics);
        if (provider == null) {
            fail("the shutdown flush decorates parked chunks", "no Tropicraft chunk provider");
            return;
        }
        // Vanilla only runs populate() for a chunk whose 2x2 neighbourhood already exists, so generate the three
        // neighbours first and the target last: that is the state a player's chunk normally reaches, with its 9x9
        // ring still incomplete.
        final int parkedX = 400;
        final int parkedZ = 400;
        try {
            parkChunk(tropics, parkedX, parkedZ);
            if (!provider.isDecorationPending(parkedX, parkedZ)) {
                fail(
                    "the shutdown flush decorates parked chunks",
                    "chunk " + parkedX
                        + ","
                        + parkedZ
                        + " did not park; "
                        + provider.getPendingDecorationCount()
                        + " chunks are pending");
                return;
            }
            ChunkProviderTropicraft.flushAllProvidersForSave();
            assertTrue(
                "the shutdown flush decorates parked chunks",
                !provider.isDecorationPending(parkedX, parkedZ),
                "chunk " + parkedX + "," + parkedZ + " is still parked after the flush");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("the shutdown flush decorates parked chunks", t.toString());
        }
    }

    /**
     * Generates {@code (x, z)} after its three north/east neighbours, which is the state where vanilla runs
     * {@code Chunk.populateChunk} for it (it requires the 2x2 to exist) while the 9x9 ring the deferred gate wants
     * is still missing - i.e. exactly the situation that parks a chunk.
     */
    private static void parkChunk(final WorldServer world, final int x, final int z) {
        world.getChunkFromChunkCoords(x + 1, z);
        world.getChunkFromChunkCoords(x, z + 1);
        world.getChunkFromChunkCoords(x + 1, z + 1);
        world.getChunkFromChunkCoords(x, z);
    }

    private static ChunkProviderTropicraft providerOf(final WorldServer world) {
        if (world == null) {
            return null;
        }
        final IChunkProvider provider = world.getChunkProvider();
        if (provider instanceof ChunkProviderServer) {
            final IChunkProvider inner = ((ChunkProviderServer) provider).currentChunkProvider;
            if (inner instanceof ChunkProviderTropicraft) {
                return (ChunkProviderTropicraft) inner;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------
    // Reporting
    // ---------------------------------------------------------------------------------------------------------

    private static void ok(final String name) {
        ++checks;
        System.out.println(PASS + name);
    }

    private static void fail(final String name, final String detail) {
        ++checks;
        ++failures;
        System.out.println(FAIL + name + ": " + detail);
    }

    private static void assertTrue(final String name, final boolean condition, final String detail) {
        if (condition) {
            ok(name);
        } else {
            fail(name, detail);
        }
    }
}
