package net.tropicraft.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.tropicraft.Tropicraft;
import net.tropicraft.curare.CurareMix;
import net.tropicraft.curare.CurareMixRegistry;
import net.tropicraft.curare.CurareType;
import net.tropicraft.drinks.Drink;
import net.tropicraft.drinks.MixerRecipes;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemCurare;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCCraftingRegistry {

    public static void init() {
        addRecipes();
        initCurareMixes();
        initDartRecipes(true);
    }

    private static void initCurareMixes() {
        CurareMixRegistry.getInstance()
            .registerRecipe(new CurareMix(CurareType.confusion, new ItemStack[] { flower(7), flower(8), flower(9) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(
                new CurareMix(
                    CurareType.harm,
                    new ItemStack[] { flower(7), flower(7), flower(7), flower(7), flower(7), flower(7), flower(8),
                        flower(9), flower(2) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(new CurareMix(CurareType.hunger, new ItemStack[] { flower(0), flower(1), flower(2) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(new CurareMix(CurareType.hunger, new ItemStack[] { flower(0), flower(1), flower(3) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(new CurareMix(CurareType.hunger, new ItemStack[] { flower(0), flower(1), flower(4) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(
                new CurareMix(CurareType.moveSlowdown, new ItemStack[] { flower(7), flower(1), flower(2) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(
                new CurareMix(CurareType.poison, new ItemStack[] { flower(7), flower(8), flower(4), flower(7) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(
                new CurareMix(CurareType.poison, new ItemStack[] { flower(7), flower(9), flower(4), flower(7) }));
        CurareMixRegistry.getInstance()
            .registerRecipe(new CurareMix(CurareType.weakness, new ItemStack[] { flower(1), flower(3), flower(4) }));
    }

    private static ItemStack flower(final int damage) {
        return new ItemStack((Block) TCBlockRegistry.flowers, 1, damage);
    }

    private static void initDartRecipes(final boolean isServer) {
        createRecipe(
            isServer,
            true,
            new ItemStack((Item) TCItemRegistry.blowGun, 1, 0),
            new Object[] { "X  ", " I ", "  X", 'X', TCItemRegistry.bambooStick, 'I',
                new ItemStack((Item) TCItemRegistry.curare, 1, 0) });
        createRecipe(
            isServer,
            true,
            new ItemStack((Item) TCItemRegistry.dart, 4),
            new Object[] { "XI", " C", 'X', Items.iron_ingot, 'I', TCItemRegistry.poisonFrogSkin, 'C', Items.feather });
        createRecipe(
            isServer,
            true,
            new ItemStack((Item) TCItemRegistry.dart, 4),
            new Object[] { "X ", "IC", 'X', Items.iron_ingot, 'I', TCItemRegistry.poisonFrogSkin, 'C', Items.feather });
        for (int damage = 0; damage < ItemCurare.effectNames.length; ++damage) {
            createRecipe(
                isServer,
                true,
                new ItemStack((Item) TCItemRegistry.dart, 4, damage),
                new Object[] { "XI", " C", 'X', Items.iron_ingot, 'I',
                    new ItemStack((Item) TCItemRegistry.curare, 1, damage), 'C', Items.feather });
            createRecipe(
                isServer,
                true,
                new ItemStack((Item) TCItemRegistry.dart, 4, damage),
                new Object[] { "X ", "IC", 'X', Items.iron_ingot, 'I',
                    new ItemStack((Item) TCItemRegistry.curare, 1, damage), 'C', Items.feather });
        }
    }

    private static void createOreBlockRecipe(final int i, final int j) {
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.oreBlocks, 1, i),
            new Object[] { "%%%", "%%%", "%%%", '%', new ItemStack((Item) TCItemRegistry.ore, 1, j) });
    }

    @SideOnly(Side.CLIENT)
    public static void addToEncyclopedia(final ItemStack itemstack, final Object[] obj) {
        Tropicraft.encyclopedia.includeRecipe(itemstack, obj);
    }

    public static void createRecipe(final boolean addToEncyclopedia, final ItemStack itemstack, final Object[] obj) {
        if (addToEncyclopedia && FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            addToEncyclopedia(itemstack, obj);
        }
        GameRegistry.addRecipe(itemstack, obj);
    }

    public static void createRecipe(final boolean isServer, final boolean addToEncyclopedia, final ItemStack itemstack,
        final Object[] obj) {
        if (addToEncyclopedia && FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            addToEncyclopedia(itemstack, obj);
        }
        GameRegistry.addRecipe(itemstack, obj);
    }

    public static void addItemsToEncyclopedia() {
        Tropicraft.encyclopedia.includeItem("acaivine", new ItemStack((Block) TCBlockRegistry.flowers, 1, 9));
        Tropicraft.encyclopedia.includeItem("anemone", new ItemStack((Block) TCBlockRegistry.flowers, 1, 4));
        Tropicraft.encyclopedia.includeItem("anthuriumo", new ItemStack((Block) TCBlockRegistry.flowers, 1, 5));
        Tropicraft.encyclopedia.includeItem("anthuriumr", new ItemStack((Block) TCBlockRegistry.flowers, 1, 6));
        Tropicraft.encyclopedia.includeItem("azurite", new ItemStack((Item) TCItemRegistry.ore, 1, 2));
        Tropicraft.encyclopedia.includeItem("bamboo", new ItemStack(TCItemRegistry.bambooChute));
        Tropicraft.encyclopedia.includeItem("bamboomug", new ItemStack(TCItemRegistry.bambooMug));
        Tropicraft.encyclopedia.includeItem("bambooblock", new ItemStack((Block) TCBlockRegistry.bambooBundle));
        Tropicraft.encyclopedia.includeItem("bamboochest", new ItemStack((Block) TCBlockRegistry.bambooChest));
        Tropicraft.encyclopedia.includeItem("bamboodoor", new ItemStack((Item) TCItemRegistry.bambooDoor));
        Tropicraft.encyclopedia.includeItem("bamboospear", new ItemStack(TCItemRegistry.bambooSpear));
        Tropicraft.encyclopedia.includeItem("bamboostick", new ItemStack((Item) TCItemRegistry.bambooStick));
        for (int i = 0; i < 5; ++i) {
            Tropicraft.encyclopedia.includeItem("beachchair", new ItemStack((Item) TCItemRegistry.chair, 1, i));
            Tropicraft.encyclopedia.includeItem("beachumbrella", new ItemStack((Item) TCItemRegistry.umbrella, 1, i));
        }
        Tropicraft.encyclopedia.includeItem("blackcoffee", MixerRecipes.getItemStack(Drink.blackCoffee));
        Tropicraft.encyclopedia.includeItem("blacksand", new ItemStack((Block) TCBlockRegistry.mineralSands, 1, 2));
        Tropicraft.encyclopedia.includeItem("blowgun", new ItemStack((Item) TCItemRegistry.blowGun, 1, 0));
        Tropicraft.encyclopedia.includeItem("bromeliad", new ItemStack((Block) TCBlockRegistry.flowers, 1, 14));
        Tropicraft.encyclopedia.includeItem("caipirinha", MixerRecipes.getItemStack(Drink.caipirinha));
        Tropicraft.encyclopedia.includeItem("canna", new ItemStack((Block) TCBlockRegistry.flowers, 1, 3));
        Tropicraft.encyclopedia.includeItem("chunkohead", new ItemStack((Block) TCBlockRegistry.chunkOHead));
        Tropicraft.encyclopedia.includeItem("coconut", new ItemStack((Block) TCBlockRegistry.coconut));
        Tropicraft.encyclopedia.includeItem("coconutchunks", new ItemStack((Item) TCItemRegistry.coconutChunk));
        Tropicraft.encyclopedia.includeItem("coconutbomb", new ItemStack((Item) TCItemRegistry.coconutBomb));
        Tropicraft.encyclopedia.includeItem("coffeebean", new ItemStack((Item) TCItemRegistry.coffeeBean));
        Tropicraft.encyclopedia.includeItem("commelina", new ItemStack((Block) TCBlockRegistry.flowers, 1, 0));
        for (int i = 0; i < TCNames.coralNames.length; ++i) {
            Tropicraft.encyclopedia.includeItem("coral", new ItemStack((Block) TCBlockRegistry.coral, 1, i));
        }
        Tropicraft.encyclopedia.includeItem("coralsand", new ItemStack((Block) TCBlockRegistry.mineralSands, 1, 0));
        Tropicraft.encyclopedia.includeItem("crocosmia", new ItemStack((Block) TCBlockRegistry.flowers, 1, 1));
        Tropicraft.encyclopedia.includeItem("croton", new ItemStack((Block) TCBlockRegistry.flowers, 1, 10));
        for (int i = 0; i < ItemCurare.effectNames.length; ++i) {
            Tropicraft.encyclopedia.includeItem("curare", new ItemStack((Item) TCItemRegistry.curare, 1, i));
        }
        Tropicraft.encyclopedia.includeItem("curarebowl", new ItemStack((Block) TCBlockRegistry.curareBowl));
        Tropicraft.encyclopedia.includeItem("dagger", new ItemStack(TCItemRegistry.dagger));
        for (int i = 0; i < ItemCurare.effectNames.length; ++i) {
            Tropicraft.encyclopedia.includeItem("dart", new ItemStack((Item) TCItemRegistry.dart, 1, i));
        }
        Tropicraft.encyclopedia.includeItem("dracaena", new ItemStack((Block) TCBlockRegistry.flowers, 1, 11));
        Tropicraft.encyclopedia.includeItem("easternisles", new ItemStack(TCItemRegistry.recordEasternIsles));
        Tropicraft.encyclopedia.includeItem("encyclopedia", new ItemStack(TCItemRegistry.encTropica));
        Tropicraft.encyclopedia.includeItem("eudialyte", new ItemStack((Item) TCItemRegistry.ore, 1, 0));
        Tropicraft.encyclopedia.includeItem("fern", new ItemStack((Block) TCBlockRegistry.flowers, 1, 12));
        Tropicraft.encyclopedia.includeItem("fertilizer", new ItemStack((Item) TCItemRegistry.fertilizer));
        Tropicraft.encyclopedia.includeItem("fireboots", new ItemStack((Item) TCItemRegistry.fireBoots));
        Tropicraft.encyclopedia.includeItem("firechestplate", new ItemStack((Item) TCItemRegistry.fireChestplate));
        Tropicraft.encyclopedia.includeItem("firehelm", new ItemStack((Item) TCItemRegistry.fireHelmet));
        Tropicraft.encyclopedia.includeItem("fireleggings", new ItemStack((Item) TCItemRegistry.fireLeggings));
        Tropicraft.encyclopedia.includeItem("fishbucket", new ItemStack((Item) TCItemRegistry.fishBucket));
        Tropicraft.encyclopedia.includeItem("fishingnet", new ItemStack((Item) TCItemRegistry.fishingNet));
        Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(TCItemRegistry.flippers));
        Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(Items.leather));
        Tropicraft.encyclopedia
            .includeItem("flowerpot", new ItemStack(Item.getItemFromBlock(TCBlockRegistry.flowerPot)));
        Tropicraft.encyclopedia.includeItem("froglegs", new ItemStack((Item) TCItemRegistry.frogLeg));
        Tropicraft.encyclopedia.includeItem("froglegscooked", new ItemStack((Item) TCItemRegistry.cookedFrogLeg));
        Tropicraft.encyclopedia.includeItem("frogskin", new ItemStack((Item) TCItemRegistry.poisonFrogSkin));
        Tropicraft.encyclopedia.includeItem("froxconch", new ItemStack(TCItemRegistry.shells, 1, 1));
        Tropicraft.encyclopedia.includeItem("grapefruit", new ItemStack((Item) TCItemRegistry.grapefruit));
        Tropicraft.encyclopedia.includeItem("grapefruitsapling", new ItemStack((Block) TCBlockRegistry.saplings, 1, 1));
        Tropicraft.encyclopedia.includeItem("greensand", new ItemStack((Block) TCBlockRegistry.mineralSands, 1, 1));
        Tropicraft.encyclopedia.includeItem("iggyscale", new ItemStack((Item) TCItemRegistry.scale));
        Tropicraft.encyclopedia.includeItem("iris", new ItemStack((Block) TCBlockRegistry.tallFlowers, 1, 1));
        Tropicraft.encyclopedia.includeItem("kapok", new ItemStack((Block) TCBlockRegistry.rainforestLeaves, 1, 0));
        Tropicraft.encyclopedia.includeItem("koachest", new ItemStack((Block) TCBlockRegistry.koaChest));
        Tropicraft.encyclopedia.includeItem("leafball", new ItemStack(TCItemRegistry.leafBall));
        Tropicraft.encyclopedia.includeItem("leather", new ItemStack(Items.leather));
        Tropicraft.encyclopedia.includeItem("lemon", new ItemStack((Item) TCItemRegistry.lemon));
        Tropicraft.encyclopedia.includeItem("lemonade", MixerRecipes.getItemStack(Drink.lemonade));
        Tropicraft.encyclopedia.includeItem("lemonsapling", new ItemStack((Block) TCBlockRegistry.saplings, 1, 2));
        Tropicraft.encyclopedia.includeItem("lime", new ItemStack((Item) TCItemRegistry.lime));
        Tropicraft.encyclopedia.includeItem("limeade", MixerRecipes.getItemStack(Drink.limeade));
        Tropicraft.encyclopedia.includeItem("limesapling", new ItemStack((Block) TCBlockRegistry.saplings, 1, 4));
        Tropicraft.encyclopedia.includeItem("lowtide", new ItemStack(TCItemRegistry.recordLowTide));
        Tropicraft.encyclopedia.includeItem("magicmushroom", new ItemStack((Block) TCBlockRegistry.flowers, 1, 7));
        Tropicraft.encyclopedia.includeItem("mahogany", new ItemStack((Block) TCBlockRegistry.logs, 1, 1));
        Tropicraft.encyclopedia.includeItem("marlinmeat", new ItemStack((Item) TCItemRegistry.freshMarlin));
        Tropicraft.encyclopedia.includeItem("marlincooked", new ItemStack((Item) TCItemRegistry.searedMarlin));
        Tropicraft.encyclopedia.includeItem("mineralsand", new ItemStack((Block) TCBlockRegistry.mineralSands, 1, 3));
        Tropicraft.encyclopedia.includeItem("mixer", new ItemStack((Block) TCBlockRegistry.eihMixer));
        Tropicraft.encyclopedia.includeItem("orange", new ItemStack((Item) TCItemRegistry.orange));
        Tropicraft.encyclopedia.includeItem("orangeade", MixerRecipes.getItemStack(Drink.orangeade));
        Tropicraft.encyclopedia.includeItem("orangesapling", new ItemStack((Block) TCBlockRegistry.saplings, 1, 3));
        Tropicraft.encyclopedia.includeItem("orchid", new ItemStack((Block) TCBlockRegistry.flowers, 1, 2));
        Tropicraft.encyclopedia.includeItem("pabshell", new ItemStack(TCItemRegistry.shells, 1, 2));
        Tropicraft.encyclopedia.includeItem("palmplanks", new ItemStack((Block) TCBlockRegistry.planks, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmwood", new ItemStack((Block) TCBlockRegistry.logs, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmsapling", new ItemStack((Block) TCBlockRegistry.saplings, 1, 0));
        Tropicraft.encyclopedia.includeItem("pathos", new ItemStack((Block) TCBlockRegistry.flowers, 1, 8));
        Tropicraft.encyclopedia.includeItem("pearlb", new ItemStack((Item) TCItemRegistry.pearl, 1, 1));
        Tropicraft.encyclopedia.includeItem("pearlw", new ItemStack((Item) TCItemRegistry.pearl, 1, 0));
        Tropicraft.encyclopedia.includeItem("pineapple", new ItemStack((Block) TCBlockRegistry.pineapple, 1, 8));
        Tropicraft.encyclopedia.includeItem("pineapplecubes", new ItemStack((Item) TCItemRegistry.pineappleCubes));
        Tropicraft.encyclopedia.includeItem("pinacolada", MixerRecipes.getItemStack(Drink.pinaColada));
        Tropicraft.encyclopedia.includeItem("portalstarter", new ItemStack(TCItemRegistry.portalEnchanter));
        Tropicraft.encyclopedia.includeItem("purifiedsand", new ItemStack((Block) TCBlockRegistry.purifiedSand));
        Tropicraft.encyclopedia.includeItem("reeds", new ItemStack(Items.reeds));
        Tropicraft.encyclopedia.includeItem("rubenautilus", new ItemStack(TCItemRegistry.shells, 1, 3));
        Tropicraft.encyclopedia.includeItem("scaleboots", new ItemStack((Item) TCItemRegistry.scaleBoots));
        Tropicraft.encyclopedia.includeItem("scalechestplate", new ItemStack((Item) TCItemRegistry.scaleChestplate));
        Tropicraft.encyclopedia.includeItem("scalehelm", new ItemStack((Item) TCItemRegistry.scaleHelmet));
        Tropicraft.encyclopedia.includeItem("scaleleggings", new ItemStack((Item) TCItemRegistry.scaleLeggings));
        Tropicraft.encyclopedia.includeItem("seaurchinroe", new ItemStack((Item) TCItemRegistry.seaUrchinRoe));
        Tropicraft.encyclopedia.includeItem("sifter", new ItemStack((Block) TCBlockRegistry.sifter));
        Tropicraft.encyclopedia.includeItem("smeltedzircon", new ItemStack((Item) TCItemRegistry.ore, 1, 4));
        Tropicraft.encyclopedia.includeItem("snaretrap", new ItemStack(TCItemRegistry.snareTrap));
        Tropicraft.encyclopedia.includeItem("snorkel", new ItemStack(TCItemRegistry.snorkel));
        Tropicraft.encyclopedia.includeItem("solonoxshell", new ItemStack(TCItemRegistry.shells, 1, 0));
        Tropicraft.encyclopedia.includeItem("starfishshell", new ItemStack(TCItemRegistry.shells, 1, 4));
        Tropicraft.encyclopedia.includeItem("summering", new ItemStack(TCItemRegistry.recordSummering));
        Tropicraft.encyclopedia.includeItem("tikitorch", new ItemStack((Item) TCItemRegistry.tikiTorch));
        Tropicraft.encyclopedia.includeItem("thatchblock", new ItemStack((Block) TCBlockRegistry.thatchBundle, 1, 0));
        Tropicraft.encyclopedia.includeItem("thetribe", new ItemStack(TCItemRegistry.recordTheTribe));
        Tropicraft.encyclopedia.includeItem("tradewinds", new ItemStack(TCItemRegistry.recordTradeWinds));
        Tropicraft.encyclopedia.includeItem("tropiframe", new ItemStack(TCItemRegistry.tropiFrame));
        Tropicraft.encyclopedia.includeItem("turtleshell", new ItemStack(TCItemRegistry.shells, 1, 5));
        Tropicraft.encyclopedia.includeItem("waterwand", new ItemStack((Item) TCItemRegistry.waterWand));
        Tropicraft.encyclopedia.includeItem("zircon", new ItemStack((Item) TCItemRegistry.ore, 1, 1));
        Tropicraft.encyclopedia.includeItem("zirconium", new ItemStack((Item) TCItemRegistry.ore, 1, 3));
    }

    public static void addRecipes() {
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.thatchBundle, 1, 0),
            new Object[] { "XX", "XX", 'X', Items.reeds });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.bambooBundle, 1),
            new Object[] { "XX", "XX", 'X', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.thatchStairs, 1),
            new Object[] { "X ", "XX", 'X', Items.reeds });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.thatchStairs, 4),
            new Object[] { "X ", "XX", 'X', new ItemStack((Block) TCBlockRegistry.thatchBundle, 1, 0) });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.bambooStairs, 1),
            new Object[] { "X ", "XX", 'X', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.bambooStairs, 4),
            new Object[] { "X ", "XX", 'X', TCBlockRegistry.bambooBundle });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.singleSlabs, 1, 1),
            new Object[] { "XX", 'X', Items.reeds });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.singleSlabs, 1, 1),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.thatchBundle, 1, 0) });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.singleSlabs, 1, 0),
            new Object[] { "XX", 'X', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.singleSlabs, 2, 0),
            new Object[] { "X", 'X', TCBlockRegistry.bambooBundle });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.planks, 4, 0),
            new Object[] { "#", '#', new ItemStack((Block) TCBlockRegistry.logs, 1, 0) });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.bambooMug, 1),
            new Object[] { "X X", "X X", "XXX", 'X', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            MixerRecipes.getItemStack(Drink.pinaColada),
            new Object[] { "X", "Y", "Z", 'X', TCItemRegistry.coconutChunk, 'Y',
                new ItemStack((Block) TCBlockRegistry.pineapple, 1, 8).getItem(), 'Z', TCItemRegistry.bambooMug });
        createRecipe(
            true,
            MixerRecipes.getItemStack(Drink.pinaColada),
            new Object[] { "Y", "X", "Z", 'X', TCItemRegistry.coconutChunk, 'Y',
                new ItemStack((Block) TCBlockRegistry.pineapple, 1, 8).getItem(), 'Z', TCItemRegistry.bambooMug });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.scaleHelmet, 1),
            new Object[] { "XXX", "X X", 'X', TCItemRegistry.scale });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.scaleChestplate, 1),
            new Object[] { "X X", "XXX", "XXX", 'X', TCItemRegistry.scale });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.scaleLeggings, 1),
            new Object[] { "XXX", "X X", "X X", 'X', TCItemRegistry.scale });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.scaleBoots, 1),
            new Object[] { "X X", "X X", 'X', TCItemRegistry.scale });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.coconutBomb, 1),
            new Object[] { " X ", "XYX", " X ", 'X', Items.gunpowder, 'Y', TCBlockRegistry.coconut });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.tikiTorch, 2),
            new Object[] { "Y  ", " X ", "  X", 'Y', Items.coal, 'X', TCItemRegistry.bambooStick });
        createRecipe(
            false,
            new ItemStack((Item) TCItemRegistry.tikiTorch, 2),
            new Object[] { "Y  ", " X ", "  X", 'Y', new ItemStack(Items.coal, 1, 1), 'X',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.bambooFence, 2),
            new Object[] { "XXX", "XXX", 'X', TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.bambooFenceGate, 1),
            new Object[] { "XIX", "XIX", 'X', TCItemRegistry.bambooStick, 'I', TCBlockRegistry.bambooBundle });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.bambooSpear, 1),
            new Object[] { "X ", " X", 'X', TCItemRegistry.bambooStick });
        for (int i = 0; i < 16; ++i) {
            createRecipe(
                true,
                new ItemStack((Item) TCItemRegistry.chair, 1, BlockColored.func_150031_c(i)),
                new Object[] { "XIX", "XIX", "XIX", 'X', TCItemRegistry.bambooStick, 'I',
                    new ItemStack(Blocks.wool, 1, i) });
            createRecipe(
                true,
                new ItemStack((Item) TCItemRegistry.umbrella, 1, BlockColored.func_150031_c(i)),
                new Object[] { "XXX", " I ", " I ", 'X', new ItemStack(Blocks.wool, 1, i), 'I',
                    TCItemRegistry.bambooStick });
        }
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.singleSlabs, 2, 2),
            new Object[] { "X", 'X', TCBlockRegistry.chunkOHead });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.chunkStairs, 4),
            new Object[] { "X  ", "XX ", "XXX", 'X', TCBlockRegistry.chunkOHead });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.singleSlabs, 2, 3),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.palmStairs, 4),
            new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack(Blocks.crafting_table, 1),
            new Object[] { "II", "II", 'I', new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack(Blocks.crafting_table, 1),
            new Object[] { "II", "II", 'I', new ItemStack((Block) TCBlockRegistry.planks, 1, 1) });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.pearl, 1, 0),
            new Object[] { "I", 'I', new ItemStack(TCItemRegistry.shells, 1, 0) });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.pearl, 1, 1),
            new Object[] { "I", 'I', new ItemStack(TCItemRegistry.shells, 1, 1) });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.pickaxeZircon),
            new Object[] { "XXX", " I ", " I ", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 1), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.axeZircon),
            new Object[] { "XX", "XI ", " I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 1), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.hoeZircon),
            new Object[] { "XX", " I", " I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 1), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.swordZircon),
            new Object[] { "X", "X", "I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 1), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.shovelZircon),
            new Object[] { "X", "I", "I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 1), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.pickaxeZirconium),
            new Object[] { "XXX", " I ", " I ", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 3), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.axeZirconium),
            new Object[] { "XX", "XI", " I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 3), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.hoeZirconium),
            new Object[] { "XX", " I", " I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 3), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.swordZirconium),
            new Object[] { "X", "X", "I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 3), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.shovelZirconium),
            new Object[] { "X", "I", "I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 3), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.pickaxeEudialyte),
            new Object[] { "XXX", " I ", " I ", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 0), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.axeEudialyte),
            new Object[] { "XX", "XI", " I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 0), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.hoeEudialyte),
            new Object[] { "XX", " I", " I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 0), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.swordEudialyte),
            new Object[] { "X", "X", "I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 0), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.shovelEudialyte),
            new Object[] { "X", "I", "I", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 0), 'I',
                TCItemRegistry.bambooStick });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.pineappleCubes),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.tallFlowers, 1, 9) });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.flippers),
            new Object[] { "XIX", "X X", 'X', Items.leather, 'I', new ItemStack(Items.dye, 1, 4) });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.snorkel),
            new Object[] { "X  ", "XII", 'X', TCItemRegistry.bambooChute, 'I', Blocks.glass_pane });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.sifter),
            new Object[] { "XXX", "XIX", "XXX", 'X', Blocks.planks, 'I', Blocks.glass_pane });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.dagger),
            new Object[] { "X", "X", "I", 'X', TCBlockRegistry.chunkOHead, 'I',
                new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.fishingNet),
            new Object[] { "  X", " XI", "XII", 'X', TCItemRegistry.bambooChute, 'I', Items.string });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.fertilizer, 3),
            new Object[] { "XI", 'X', new ItemStack((Block) TCBlockRegistry.flowers, 1, 7), 'I',
                new ItemStack((Block) TCBlockRegistry.flowers, 1, 10) });
        createRecipe(
            true,
            new ItemStack(Items.dye, 4, 5),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.tallFlowers, 1, 15) });
        createRecipe(
            true,
            new ItemStack(Items.dye, 2, 1),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.flowers, 1, 6) });
        createRecipe(
            true,
            new ItemStack(Items.dye, 2, 14),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.flowers, 1, 5) });
        createRecipe(
            true,
            new ItemStack(Items.dye, 2, 12),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.flowers, 1, 0) });
        createRecipe(
            true,
            new ItemStack(Items.dye, 2, 2),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.flowers, 1, 12) });
        createRecipe(
            true,
            new ItemStack(Items.dye, 2, 11),
            new Object[] { "X", 'X', new ItemStack((Block) TCBlockRegistry.flowers, 1, 3) });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.bambooChest, 1),
            new Object[] { "XXX", "X X", "XXX", 'X', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            new ItemStack(Items.stick, 4),
            new Object[] { "#", "#", '#', new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack(Items.wooden_pickaxe),
            new Object[] { "XXX", " # ", " # ", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0), '#',
                Items.stick });
        createRecipe(
            true,
            new ItemStack(Items.wooden_shovel),
            new Object[] { "X", "#", "#", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0), '#', Items.stick });
        createRecipe(
            true,
            new ItemStack(Items.wooden_axe),
            new Object[] { "XX", "X#", " #", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0), '#',
                Items.stick });
        createRecipe(
            true,
            new ItemStack(Items.wooden_hoe),
            new Object[] { "XX", " #", " #", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0), '#',
                Items.stick });
        createRecipe(
            true,
            new ItemStack(Items.wooden_sword),
            new Object[] { "X", "X", "#", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0), '#', Items.stick });
        createRecipe(
            true,
            new ItemStack(Blocks.planks, 4, 3),
            new Object[] { "#", '#', new ItemStack((Block) TCBlockRegistry.logs, 1, 1) });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.encTropica, 1),
            new Object[] { "###", "#$#", "###", '#', TCItemRegistry.bambooChute, '$', Items.book });
        createRecipe(
            true,
            new ItemStack(Items.wooden_door, 1),
            new Object[] { "XX", "XX", "XX", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack(Blocks.trapdoor, 2),
            new Object[] { "XXX", "XXX", 'X', new ItemStack((Block) TCBlockRegistry.planks, 1, 0) });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.bambooDoor),
            new Object[] { "XX", "YY", "XX", 'X', TCBlockRegistry.bambooBundle, 'Y', TCBlockRegistry.thatchBundle });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.waterWand),
            new Object[] { "  X", " Y ", "Y  ", 'X', new ItemStack((Item) TCItemRegistry.ore, 1, 2), 'Y',
                Items.gold_ingot });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.snareTrap),
            new Object[] { "  X", "XX ", "XX ", 'X', Items.string });
        createRecipe(
            true,
            new ItemStack(Item.getItemFromBlock(TCBlockRegistry.flowerPot)),
            new Object[] { "# #", " # ", '#', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.coffeeBean, 1, 0),
            new Object[] { "X", 'X', new ItemStack((Item) TCItemRegistry.coffeeBean, 0, 2) });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.tropiFrame, 1),
            new Object[] { "###", "#X#", "###", '#', TCItemRegistry.bambooChute, 'X', Items.leather });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.portalEnchanter, 1),
            new Object[] { "%@#", "#@%", " @ ", '@', TCItemRegistry.bambooStick, '#',
                new ItemStack((Item) TCItemRegistry.ore, 1, 2), '%', new ItemStack((Item) TCItemRegistry.ore, 1, 3) });
        createRecipe(
            true,
            new ItemStack(TCItemRegistry.portalEnchanter, 1),
            new Object[] { "#@%", "%@#", " @ ", '@', TCItemRegistry.bambooStick, '#',
                new ItemStack((Item) TCItemRegistry.ore, 1, 2), '%', new ItemStack((Item) TCItemRegistry.ore, 1, 3) });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.bambooStick, 4),
            new Object[] { "#", "#", '#', TCItemRegistry.bambooChute });
        createRecipe(
            true,
            new ItemStack((Item) TCItemRegistry.ore, 1, 3),
            new Object[] { "###", "#$#", "###", '$', Items.diamond, '#',
                new ItemStack((Item) TCItemRegistry.ore, 1, 4) });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.koaChest, 1),
            new Object[] { "###", "#X#", "###", '#', new ItemStack((Item) TCItemRegistry.ore, 1, 3), 'X',
                TCBlockRegistry.bambooChest });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.eihMixer),
            new Object[] { "XXX", "X X", "XXX", 'X', TCBlockRegistry.chunkOHead });
        createRecipe(
            true,
            new ItemStack((Block) TCBlockRegistry.curareBowl),
            new Object[] { "X X", " X ", 'X', TCBlockRegistry.chunkOHead });
        createOreBlockRecipe(2, 0);
        createOreBlockRecipe(3, 1);
        createOreBlockRecipe(4, 2);
        createOreBlockRecipe(5, 3);
        GameRegistry.addShapelessRecipe(
            new ItemStack((Item) TCItemRegistry.fertilizer, 3),
            new Object[] { new ItemStack((Block) TCBlockRegistry.flowers, 1, 7),
                new ItemStack((Block) TCBlockRegistry.flowers, 1, 10) });
        GameRegistry.addShapelessRecipe(
            MixerRecipes.getItemStack(Drink.pinaColada),
            new Object[] { TCItemRegistry.coconutChunk, TCItemRegistry.pineappleCubes, TCItemRegistry.bambooMug });
        GameRegistry
            .addSmelting((Item) TCItemRegistry.frogLeg, new ItemStack((Item) TCItemRegistry.cookedFrogLeg), 3.0f);
        GameRegistry.addSmelting((Block) TCBlockRegistry.purifiedSand, new ItemStack(Blocks.glass), 4.0f);
        GameRegistry
            .addSmelting((Item) TCItemRegistry.freshMarlin, new ItemStack((Item) TCItemRegistry.searedMarlin), 6.0f);
        GameRegistry.addSmelting((Block) TCBlockRegistry.logs, new ItemStack(Items.coal, 1, 1), 3.0f);
        FurnaceRecipes.smelting()
            .func_151396_a(
                (Item) TCItemRegistry.coffeeBean,
                new ItemStack((Item) TCItemRegistry.coffeeBean, 1, 1),
                0.15f);
        FurnaceRecipes.smelting()
            .func_151394_a(
                new ItemStack((Item) TCItemRegistry.ore, 1, 1),
                new ItemStack((Item) TCItemRegistry.ore, 1, 4),
                3.0f);
        GameRegistry.registerFuelHandler((IFuelHandler) new IFuelHandler() {

            public int getBurnTime(final ItemStack fuel) {
                if (fuel.getItem() != null && fuel.getItem() instanceof ItemBlock
                    && Block.getBlockFromItem(fuel.getItem()) == TCBlockRegistry.singleSlabs
                    && fuel.getItemDamage() == 3) {
                    return 150;
                }
                return 0;
            }
        });
    }
}
