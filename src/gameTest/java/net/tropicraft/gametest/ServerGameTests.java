package net.tropicraft.gametest;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.CRC32;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.entity.hostile.EntityAshenHunter;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.entity.hostile.EntityTropiCreeper;
import net.tropicraft.entity.koa.EntityKoaBase;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.underdasea.EntityEagleRay;
import net.tropicraft.entity.underdasea.EntityMarlin;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.entity.underdasea.EntityTropicraftWaterMob;
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
import net.tropicraft.world.worldgen.WorldGenCoral;

import com.mojang.authlib.GameProfile;

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
        itemNamesFollowTheRegisterConvention();
        blockNamesFollowTheRegisterConvention();
        craftingAndSmeltingRecipesAreUnchanged();
        creativePlayersAreNotTargeted();
        passiveAnimalsAreNotHostileMobs();
        hostileMobsDoNotSurvivePeaceful();
        waterMobsActuallySwim();
        mineralSandsBlendAcrossChunks();
        coralReefsSpanChunks();
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

    /** Measured baseline of the recipe fingerprint; see craftingAndSmeltingRecipesAreUnchanged. */
    private static final int CRAFTING_RECIPES = 139;
    private static final String CRAFTING_RECIPES_CRC = "2e72ec27";
    private static final int SMELTING_RECIPES = 6;
    private static final String SMELTING_RECIPES_CRC = "4be27cea";

    private static final Set<String> ITEM_NAME_EXCEPTIONS = new HashSet<String>(
        Arrays.asList("tropicraft:fishingRodTropical", "tropicraft:bucketTropicsWater"));

    /**
     * Every Tropicraft item's unlocalized name must be {@code item.<registry name>} - i.e. {@code
     * item.tropicraft:flippers} for {@code tropicraft:flippers}. That is what the lang file and every
     * {@code getUnlocalizedName} override in the mod have to agree on; the item-naming refactor moves the shared
     * logic into one helper, so this pins the outcome.
     * <p>
     * Item blocks are excluded: their name belongs to the block ({@code tile.*}), which follows its own convention.
     * </p>
     */
    private static void itemNamesFollowTheRegisterConvention() {
        final List<String> wrong = new ArrayList<String>();
        int counted = 0;
        final Set<String> keys = Item.itemRegistry.getKeys();
        for (final Object key : keys) {
            final String name = (String) key;
            if (!name.startsWith("tropicraft:")) {
                continue;
            }
            final Object registered = Item.itemRegistry.getObject(name);
            if (!(registered instanceof Item) || registered instanceof ItemBlock) {
                continue;
            }
            ++counted;
            final String expected = "item." + name;
            final String actual = ((Item) registered).getUnlocalizedName();
            // Two long-standing exceptions: both are registered by Tropicraft but report an un-namespaced unlocalized
            // name, and their lang keys (item.fishingRodTropical.name, item.bucketTropicsWater.name) exist in that
            // form. Renaming them would break every resource pack that overrides those two keys, so they stay.
            if (ITEM_NAME_EXCEPTIONS.contains(name)) {
                continue;
            }
            if (!expected.equals(actual)) {
                wrong.add(name + " -> " + actual);
            }
        }
        assertTrue(
            "every Tropicraft item is named item.<registry name>",
            counted > 50 && wrong.isEmpty(),
            counted + " checked, wrong: " + wrong);
    }

    /**
     * Blocks follow the same idea as items: the registry name is {@code tropicraft:tile.<n>} (see
     * {@code TCBlockRegistry.registerBlock}'s {@code "tile." + name}) and the unlocalized name a Tropicraft block
     * reports is {@code tile.tropicraft:<n>}. Blocks that do not override {@code getUnlocalizedName} at all are
     * listed separately rather than failed: they are the ones that never joined the convention.
     */
    private static void blockNamesFollowTheRegisterConvention() {
        final List<String> wrong = new ArrayList<String>();
        final List<String> unconverted = new ArrayList<String>();
        int counted = 0;
        final Set<String> keys = Block.blockRegistry.getKeys();
        for (final Object key : keys) {
            final String registryName = (String) key;
            if (!registryName.startsWith("tropicraft:tile.")) {
                continue;
            }
            final Object registered = Block.blockRegistry.getObject(registryName);
            if (!(registered instanceof Block)) {
                continue;
            }
            ++counted;
            final String expected = "tile.tropicraft:" + registryName.substring("tropicraft:tile.".length());
            final String actual = ((Block) registered).getUnlocalizedName();
            // The pineapple plant is part of the tall flower family and deliberately shares its naming scheme:
            // the item appends "_Pineapple", giving the lang key tile.tropicraft:tallFlower_Pineapple.name.
            if ("tropicraft:tile.pineapple".equals(registryName) && "tile.tropicraft:tallFlower".equals(actual)) {
                continue;
            }
            if (expected.equals(actual)) {
                continue;
            }
            if (("tile." + registryName.substring("tropicraft:tile.".length())).equals(actual)) {
                unconverted.add(registryName);
            } else {
                wrong.add(registryName + " -> " + actual);
            }
        }
        assertTrue(
            "every Tropicraft block is named tile.tropicraft:<name>",
            counted > 30 && wrong.isEmpty(),
            counted + " checked, wrong: " + wrong + ", not using the convention: " + unconverted);
    }

    // ---------------------------------------------------------------------------------------------------------
    // Recipes
    // ---------------------------------------------------------------------------------------------------------

    /**
     * A fingerprint of every crafting and smelting recipe that involves Tropicraft, printed as a count plus a CRC of
     * the sorted listing. The registry refactor (R4) moves recipe registration around and introduces helpers for the
     * repeated patterns; this is what proves the recipes themselves did not move, and a failure dumps the full listing
     * to the report directory so a diff is possible.
     */
    private static void craftingAndSmeltingRecipesAreUnchanged() {
        try {
            final List<String> crafting = new ArrayList<String>();
            for (final Object o : CraftingManager.getInstance()
                .getRecipeList()) {
                if (!(o instanceof IRecipe)) {
                    continue;
                }
                final IRecipe recipe = (IRecipe) o;
                final ItemStack output = recipe.getRecipeOutput();
                if (output == null || output.getItem() == null) {
                    continue;
                }
                final String signature = recipeSignature(recipe, output);
                if (signature != null) {
                    crafting.add(signature);
                }
            }
            Collections.sort(crafting);
            dumpToReport("recipes-crafting.txt", crafting);

            final List<String> smelting = new ArrayList<String>();
            for (final Object entry : FurnaceRecipes.smelting()
                .getSmeltingList()
                .entrySet()) {
                final Map.Entry<?, ?> pair = (Map.Entry<?, ?>) entry;
                final String input = nameOf((ItemStack) pair.getKey());
                final String result = nameOf((ItemStack) pair.getValue());
                if (input.startsWith("tropicraft:") || result.startsWith("tropicraft:")) {
                    smelting.add(input + " -> " + result);
                }
            }
            Collections.sort(smelting);
            dumpToReport("recipes-smelting.txt", smelting);

            assertTrue(
                "the crafting recipes involving Tropicraft are unchanged",
                crafting.size() == CRAFTING_RECIPES && CRAFTING_RECIPES_CRC.equals(crcOf(crafting)),
                crafting.size() + " recipes, crc "
                    + crcOf(crafting)
                    + " (expected "
                    + CRAFTING_RECIPES
                    + ", crc "
                    + CRAFTING_RECIPES_CRC
                    + "; the listing is in build/gametest/recipes-crafting.txt)");
            assertTrue(
                "the smelting recipes involving Tropicraft are unchanged",
                smelting.size() == SMELTING_RECIPES && SMELTING_RECIPES_CRC.equals(crcOf(smelting)),
                smelting.size() + " recipes, crc "
                    + crcOf(smelting)
                    + " (expected "
                    + SMELTING_RECIPES
                    + ", crc "
                    + SMELTING_RECIPES_CRC
                    + "; the listing is in build/gametest/recipes-smelting.txt)");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("the crafting recipes involving Tropicraft are unchanged", t.toString());
        }
    }

    /** @return a stable description of a recipe, or null when it involves no Tropicraft item or is of unknown shape */
    private static String recipeSignature(final IRecipe recipe, final ItemStack output) {
        final String outName = nameOf(output);
        final StringBuilder signature = new StringBuilder(outName).append(" x")
            .append(output.stackSize);
        boolean tropicraft = outName.startsWith("tropicraft:");
        if (recipe instanceof ShapedRecipes) {
            final ShapedRecipes shaped = (ShapedRecipes) recipe;
            signature.append(" shaped ")
                .append(shaped.recipeWidth)
                .append('x')
                .append(shaped.recipeHeight);
            for (final ItemStack ingredient : shaped.recipeItems) {
                final String name = nameOf(ingredient);
                tropicraft |= name.startsWith("tropicraft:");
                signature.append(" [")
                    .append(name)
                    .append(']');
            }
        } else if (recipe instanceof ShapelessRecipes) {
            final List<String> ingredients = new ArrayList<String>();
            for (final Object ingredient : ((ShapelessRecipes) recipe).recipeItems) {
                final String name = nameOf((ItemStack) ingredient);
                tropicraft |= name.startsWith("tropicraft:");
                ingredients.add(name);
            }
            Collections.sort(ingredients);
            signature.append(" shapeless ")
                .append(ingredients);
        } else {
            return null;
        }
        return tropicraft ? signature.toString() : null;
    }

    private static String nameOf(final ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return "-";
        }
        final Object name = Item.itemRegistry.getNameForObject(stack.getItem());
        return (name == null ? "?" : name) + "@" + stack.getItemDamage();
    }

    private static String crcOf(final List<String> lines) {
        try {
            final CRC32 crc = new CRC32();
            for (final String line : lines) {
                crc.update(line.getBytes("UTF-8"));
                crc.update(10);
            }
            return Long.toHexString(crc.getValue());
        } catch (final java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void dumpToReport(final String fileName, final List<String> lines) {
        final String report = System.getProperty(GameTestMod.REPORT_PROPERTY);
        if (report == null) {
            return;
        }
        final File file = new File(new File(report).getParentFile(), fileName);
        try {
            final java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8");
            try {
                for (final String line : lines) {
                    writer.println(line);
                }
            } finally {
                writer.close();
            }
        } catch (final Throwable ignored) {
            // best effort: the assertion message carries the numbers
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Targeting and mob classification (#50)
    // ---------------------------------------------------------------------------------------------------------

    /**
     * A creative player must never become a mob's target, including by hitting it. CoroUtil's {@code JobHunt} records
     * the attacker before anything asks whether it is an enemy, so a creative player's stray hit used to put them on
     * the target list and the whole village would hunt them (issue #50).
     */
    private static void creativePlayersAreNotTargeted() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("a creative player is not targeted", "the Tropicraft dimension did not load");
            return;
        }
        final EnumDifficulty difficulty = tropics.difficultySetting;
        try {
            final FakePlayer creative = FakePlayerFactory.get(
                tropics,
                new GameProfile(
                    UUID.nameUUIDFromBytes("tropicraft-gametest-creative".getBytes("UTF-8")),
                    "TCGameTest"));
            creative.capabilities.isCreativeMode = true;
            creative.capabilities.disableDamage = true;

            final EntityKoaHunter koa = new EntityKoaHunter(tropics);
            koa.setPosition(8.5, 100.0, 8.5);
            tropics.spawnEntityInWorld(koa);

            // Hitting it is the path that bypasses isEnemy entirely.
            koa.attackEntityFrom(DamageSource.causePlayerDamage(creative), 1.0f);
            koa.onLivingUpdate();
            final Entity target = koa.getAIAgent().entityToAttack;
            assertTrue(
                "a creative player's hit does not become a Koa's target",
                target != creative,
                "the Koa is now targeting " + target);

            // And asking directly must not consider them an enemy either.
            assertTrue(
                "a creative player is not an enemy",
                !koa.isEnemy(creative) && !((EntityTropiCreeper) new EntityTropiCreeper(tropics)).isEnemy(creative),
                "isEnemy agreed that the creative player is an enemy");

            // A survival player is still an ordinary potential enemy for the non-Koa families.
            final FakePlayer survival = FakePlayerFactory.get(
                tropics,
                new GameProfile(
                    UUID.nameUUIDFromBytes("tropicraft-gametest-survival".getBytes("UTF-8")),
                    "TCGameTest2"));
            survival.capabilities.isCreativeMode = false;
            survival.capabilities.disableDamage = false;
            assertTrue(
                "a survival player is still a potential enemy",
                new EntityTropiCreeper(tropics).isEnemy(survival),
                "isEnemy said a survival player is not an enemy");
            koa.setDead();
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("a creative player is not targeted", t.toString());
        } finally {
            tropics.difficultySetting = difficulty;
        }
    }

    /** A passive animal must not be classified as a hostile mob; that is what {@code IMob} means to the spawn code. */
    private static void passiveAnimalsAreNotHostileMobs() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("a passive animal is not a hostile mob", "the Tropicraft dimension did not load");
            return;
        }
        try {
            final EntityIguana iguana = new EntityIguana(tropics);
            assertTrue(
                "a passive animal is not a hostile mob",
                !(((Object) iguana) instanceof IMob),
                "EntityIguana is still declared as IMob");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("a passive animal is not a hostile mob", t.toString());
        }
    }

    /**
     * Peaceful must remove hostile mobs of every family, not just the CoroAI ones. The CoroAI base class kills itself,
     * but the Tropicraft mobs that do not share it (the Easter Island Head, the Ashen) have to handle it themselves.
     */
    private static void hostileMobsDoNotSurvivePeaceful() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("hostile mobs do not survive peaceful", "the Tropicraft dimension did not load");
            return;
        }
        final EnumDifficulty difficulty = tropics.difficultySetting;
        try {
            tropics.difficultySetting = EnumDifficulty.PEACEFUL;
            final EntityEIH eih = new EntityEIH(tropics);
            eih.setPosition(8.5, 100.0, 8.5);
            tropics.spawnEntityInWorld(eih);
            eih.onLivingUpdate();
            assertTrue("the Easter Island Head does not survive peaceful", eih.isDead, "it is still alive");

            final EntityAshenHunter ashen = new EntityAshenHunter(tropics);
            ashen.setPosition(8.5, 100.0, 8.5);
            tropics.spawnEntityInWorld(ashen);
            ashen.onLivingUpdate();
            assertTrue("the Ashen does not survive peaceful", ashen.isDead, "it is still alive");

            final EntityKoaHunter koa = new EntityKoaHunter(tropics);
            koa.setPosition(8.5, 100.0, 8.5);
            tropics.spawnEntityInWorld(koa);
            koa.onLivingUpdate();
            assertTrue("the Koa do not survive peaceful", koa.isDead, "it is still alive");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("hostile mobs do not survive peaceful", t.toString());
        } finally {
            tropics.difficultySetting = difficulty;
        }
    }

    /**
     * Every water mob has to actually move in water.
     * <p>
     * The eagle ray (1.0 x 0.25) did not, at all: vanilla tests "in water" with {@code boundingBox.expand(0, -0.4, 0)},
     * which turns inside out for an entity shorter than 0.8 blocks, so the ray was never wet and every swim branch in
     * {@code EntityTropicraftWaterMob} - all of which are gated on {@code isInWater()} - was skipped.
     * </p>
     */
    private static void waterMobsActuallySwim() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("water mobs swim", "the Tropicraft dimension did not load");
            return;
        }
        final int bx = 200;
        final int by = 50;
        final int bz = 200;
        try {
            tropics.getChunkFromChunkCoords(bx >> 4, bz >> 4);
            for (int x = -4; x <= 4; ++x) {
                for (int z = -4; z <= 4; ++z) {
                    tropics.setBlock(bx + x, by - 1, bz + z, Blocks.stone);
                    for (int y = 0; y <= 5; ++y) {
                        tropics.setBlock(bx + x, by + y, bz + z, TCBlockRegistry.tropicsWater);
                    }
                }
            }
            assertSwims(tropics, "an eagle ray", new EntityEagleRay(tropics), bx, by + 2, bz);
            assertSwims(tropics, "a marlin", new EntityMarlin(tropics), bx, by + 2, bz);
            assertSwims(tropics, "a seahorse", new EntitySeahorse(tropics), bx, by + 2, bz);
            assertSwims(tropics, "a tropical fish", new EntityTropicalFish(tropics), bx, by + 2, bz);
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("water mobs swim", t.toString());
        }
    }

    /** Drops one mob in the pool and ticks it; it has to be wet at some point and to cover some distance. */
    private static void assertSwims(final WorldServer world, final String label, final EntityTropicraftWaterMob mob,
        final int x, final int y, final int z) {
        mob.setPosition(x + 0.5, y, z + 0.5);
        world.spawnEntityInWorld(mob);
        final double startX = mob.posX;
        final double startY = mob.posY;
        final double startZ = mob.posZ;
        boolean everWet = false;
        for (int i = 0; i < 200; ++i) {
            mob.onUpdate();
            everWet |= mob.isInWater();
        }
        // All three axes: a ray mostly swims up and down towards its target depth, and a frozen one does not move in
        // any of them.
        final double moved = Math.sqrt(
            (mob.posX - startX) * (mob.posX - startX) + (mob.posY - startY) * (mob.posY - startY)
                + (mob.posZ - startZ) * (mob.posZ - startZ));
        assertTrue(
            label + " swims",
            // The seahorse is a slow drifter (about 1.5 blocks in 200 ticks); the failure this catches - the eagle
            // ray - is 0.00, so a low bar is enough and keeps the check off the seahorse's noise.
            everWet && moved > 0.75,
            "moved " + String.format("%.2f", moved)
                + " blocks, was wet: "
                + everWet
                + ", ended at "
                + mob.posX
                + "/"
                + mob.posY
                + "/"
                + mob.posZ
                + ", height "
                + mob.height);
        mob.setDead();
    }

    /**
     * The decorative mineral sands have to blend, not come in 16×16 squares (B28). Generates a synthetic beach through
     * the real {@code replaceBlocksForBiome} for a run of chunks and checks that the run contains more than one value
     * (a patch edge somewhere) and that at least one chunk mixes values internally (a fade).
     */
    private static void mineralSandsBlendAcrossChunks() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("mineral sands blend across chunks", "the Tropicraft dimension did not load");
            return;
        }
        try {
            final ChunkProviderTropicraft provider = providerOf(tropics);
            if (provider == null) {
                fail("mineral sands blend across chunks", "no Tropicraft chunk provider");
                return;
            }
            final BiomeGenBase beach = BiomeGenTropicraft.tropicsBeach;
            final Block[] blocks = new Block[65536];
            final byte[] metas = new byte[65536];
            final BiomeGenBase[] biomes = new BiomeGenBase[256];
            for (int i = 0; i < biomes.length; ++i) {
                biomes[i] = beach;
            }

            // The field depends on the world seed, so a fixed run of chunks may legitimately be all plain sand.
            // Sweep a few unrelated runs and require the field to show its structure somewhere.
            boolean sawMix = false;
            final java.util.Set<Integer> seen = new java.util.HashSet<Integer>();
            final StringBuilder trace = new StringBuilder();
            for (int attempt = 0; attempt < 16 && !sawMix; ++attempt) {
                // A run of chunks along x, so a patch cannot stay inside one chunk and the run must cross an edge.
                final int originX = 1000 + attempt * 37;
                final int originZ = 1000 + attempt * 23;
                trace.append(" attempt")
                    .append(attempt)
                    .append('@')
                    .append(originX)
                    .append(',')
                    .append(originZ)
                    .append(':');
                for (int chunk = 0; chunk < 12; ++chunk) {
                    buildBeach(blocks);
                    provider.replaceBlocksForBiome(originX + chunk, originZ, blocks, metas, biomes);
                    final java.util.Set<Integer> valuesInChunk = new java.util.HashSet<Integer>();
                    for (int x = 0; x < 16; ++x) {
                        for (int z = 0; z < 16; ++z) {
                            // topBlock is left alone on the surface; the sand fill sits just under it.
                            final int index = x * 256 * 16 | z * 256 | 62;
                            valuesInChunk.add((int) metas[index]);
                        }
                    }
                    seen.addAll(valuesInChunk);
                    if (valuesInChunk.size() > 1) {
                        sawMix = true;
                    }
                    trace.append(valuesInChunk);
                }
            }
            assertTrue(
                "mineral sands blend across chunks",
                seen.size() > 1 && sawMix,
                "values seen: " + seen + ", any chunk mixing: " + sawMix + trace);
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("mineral sands blend across chunks", t.toString());
        }
    }

    /** A flat sand beach: stone below y=60, sand at 60..63, air above. */
    private static void buildBeach(final Block[] blocks) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < 128; ++y) {
                    final Block block;
                    if (y <= 60) {
                        block = Blocks.stone;
                    } else if (y <= 63) {
                        block = Blocks.sand;
                    } else {
                        block = Blocks.air;
                    }
                    blocks[x * 256 * 16 | z * 256 | y] = block;
                }
            }
        }
    }

    /**
     * A reef has to reach outside the chunk it was rolled in; a generator pinned to ±8 blocks around a point inside one
     * chunk produced single-chunk clusters.
     */
    private static void coralReefsSpanChunks() {
        final WorldServer tropics = TropicraftWorldUtils.tropics();
        if (tropics == null) {
            fail("coral reefs span chunks", "the Tropicraft dimension did not load");
            return;
        }
        final int chunkX = 64;
        final int chunkZ = 64;
        try {
            // A flat sea floor with a deep enough water column for coral's canBlockStay (water above and below).
            for (int x = -24; x <= 24; ++x) {
                for (int z = -24; z <= 24; ++z) {
                    final int worldX = chunkX * 16 + x;
                    final int worldZ = chunkZ * 16 + z;
                    tropics.setBlock(worldX, 62, worldZ, Blocks.sand);
                    for (int y = 63; y <= 66; ++y) {
                        tropics.setBlock(worldX, y, worldZ, TCBlockRegistry.tropicsWater);
                    }
                }
            }
            final java.util.Random rand = new java.util.Random(1234L);
            for (int attempt = 0; attempt < 40; ++attempt) {
                new WorldGenCoral(24, 6).generate(tropics, rand, chunkX * 16 + 8, 64, chunkZ * 16 + 8);
            }
            final java.util.Set<Long> chunksWithCoral = new java.util.HashSet<Long>();
            for (int x = -24; x <= 24; ++x) {
                for (int z = -24; z <= 24; ++z) {
                    for (int y = 61; y <= 66; ++y) {
                        final int worldX = chunkX * 16 + x;
                        final int worldZ = chunkZ * 16 + z;
                        if (tropics.getBlock(worldX, y, worldZ) == TCBlockRegistry.coral) {
                            chunksWithCoral.add((long) (worldX >> 4) * 1000L + (worldZ >> 4));
                        }
                    }
                }
            }
            assertTrue(
                "coral reefs span chunks",
                chunksWithCoral.size() > 1,
                "coral appeared in " + chunksWithCoral.size() + " chunk(s)");
        } catch (final Throwable t) {
            t.printStackTrace();
            fail("coral reefs span chunks", t.toString());
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
