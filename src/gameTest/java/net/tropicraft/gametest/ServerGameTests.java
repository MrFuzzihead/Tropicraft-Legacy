package net.tropicraft.gametest;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;
import net.tropicraft.util.TCSchematics;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.chunk.ChunkProviderTropicraft;
import net.tropicraft.world.location.TownKoaVillage;

import build.world.Build;

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
        villageSchematicLoads();
        villageBuildQueues();
        economyValuesResolveToRealItems();
        dartPotionIdsAreBoundsChecked();
        mineralSandDoesNotLeakFromAnOceanColumn();
        tropicraftBlocksKeepTheirRegistryPrefix();
        tropicsChunksGenerate();
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
