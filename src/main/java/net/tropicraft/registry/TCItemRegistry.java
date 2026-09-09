package net.tropicraft.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemBambooChute;
import net.tropicraft.item.ItemBambooDoor;
import net.tropicraft.item.ItemCocktail;
import net.tropicraft.item.ItemCoconutBomb;
import net.tropicraft.item.ItemCoffeeBean;
import net.tropicraft.item.ItemCurare;
import net.tropicraft.item.ItemDagger;
import net.tropicraft.item.ItemDart;
import net.tropicraft.item.ItemDartGun;
import net.tropicraft.item.ItemFertilizer;
import net.tropicraft.item.ItemFishBucket;
import net.tropicraft.item.ItemFlippers;
import net.tropicraft.item.ItemMobEgg;
import net.tropicraft.item.ItemPortalEnchanter;
import net.tropicraft.item.ItemShell;
import net.tropicraft.item.ItemSnareTrap;
import net.tropicraft.item.ItemSnorkel;
import net.tropicraft.item.ItemTCItemFrame;
import net.tropicraft.item.ItemTikiTorch;
import net.tropicraft.item.ItemTropBook;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.item.ItemTropicraftFood;
import net.tropicraft.item.ItemTropicraftLeafballNew;
import net.tropicraft.item.ItemTropicraftMulti;
import net.tropicraft.item.ItemTropicraftMusicDisk;
import net.tropicraft.item.ItemTropicraftOre;
import net.tropicraft.item.ItemTropicsWaterBucket;
import net.tropicraft.item.ItemWaterWand;
import net.tropicraft.item.armor.ItemAshenMask;
import net.tropicraft.item.armor.ItemFireArmor;
import net.tropicraft.item.armor.ItemScaleArmor;
import net.tropicraft.item.armor.ItemTropicraftArmor;
import net.tropicraft.item.placeable.ItemChair;
import net.tropicraft.item.placeable.ItemUmbrella;
import net.tropicraft.item.tool.ItemTropicraftAxe;
import net.tropicraft.item.tool.ItemTropicraftHoe;
import net.tropicraft.item.tool.ItemTropicraftPickaxe;
import net.tropicraft.item.tool.ItemTropicraftShovel;
import net.tropicraft.item.tool.ItemTropicraftSword;
import net.tropicraft.item.tool.ItemTropicraftTool;

import CoroUtil.entity.ItemTropicalFishingRod;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCItemRegistry {

    public static final ItemTropicraft frogLeg;
    public static final ItemTropicraftFood cookedFrogLeg;
    public static final ItemTropicraft poisonFrogSkin;
    public static final ItemTropicraftFood freshMarlin;
    public static final ItemTropicraftFood searedMarlin;
    public static final ItemTropicraftFood grapefruit;
    public static final ItemTropicraftFood lemon;
    public static final ItemTropicraftFood lime;
    public static final ItemTropicraftFood orange;
    public static final ItemTropicraft scale;
    public static final ItemTropicraftFood coconutChunk;
    public static final ItemTropicraftFood pineappleCubes;
    public static final ItemTropicraft bambooStick;
    public static final ItemTropicraftFood seaUrchinRoe;
    public static final ItemTropicraft pearl;
    public static final ItemTropicraft ore;
    public static final ItemTropicraft waterWand;
    public static final ItemTropicraft fishingNet;
    public static final ItemTropicraft coffeeBean;
    public static final ItemArmor.ArmorMaterial materialScaleArmor;
    public static final ItemTropicraftArmor scaleBoots;
    public static final ItemTropicraftArmor scaleLeggings;
    public static final ItemTropicraftArmor scaleChestplate;
    public static final ItemTropicraftArmor scaleHelmet;
    public static final ItemArmor.ArmorMaterial materialFireArmor;
    public static final ItemTropicraftArmor fireBoots;
    public static final ItemTropicraftArmor fireLeggings;
    public static final ItemTropicraftArmor fireChestplate;
    public static final ItemTropicraftArmor fireHelmet;
    public static final ItemArmor.ArmorMaterial materialMaskArmor;
    public static final ItemTropicraftArmor ashenMask;
    public static Item.ToolMaterial materialZirconTools;
    public static Item.ToolMaterial materialEudialyteTools;
    public static Item.ToolMaterial materialZirconiumTools;
    public static final ItemHoe hoeEudialyte;
    public static final ItemHoe hoeZircon;
    public static final ItemHoe hoeZirconium;
    public static final ItemTropicraftTool shovelEudialyte;
    public static final ItemTropicraftTool shovelZircon;
    public static final ItemTropicraftTool shovelZirconium;
    public static final ItemTropicraftTool pickaxeEudialyte;
    public static final ItemTropicraftTool pickaxeZircon;
    public static final ItemTropicraftTool pickaxeZirconium;
    public static final ItemTropicraftTool axeEudialyte;
    public static final ItemTropicraftTool axeZircon;
    public static final ItemTropicraftTool axeZirconium;
    public static final ItemSword swordEudialyte;
    public static final ItemSword swordZircon;
    public static final ItemSword swordZirconium;
    public static final ItemTropicraft tikiTorch;
    public static final ItemTropicraft bambooDoor;
    public static final ItemTropicsWaterBucket bucketTropicsWater;
    public static final ItemFishBucket fishBucket;
    public static final ItemChair chair;
    public static final ItemUmbrella umbrella;
    public static final ItemFertilizer fertilizer;
    public static final ItemTropicraft coconutBomb;
    public static final ItemCurare curare;
    public static final ItemDart dart;
    public static final ItemDartGun blowGun;
    public static final Item shells;
    public static Item.ToolMaterial materialBambooTools;
    public static final Item bambooSpear;
    public static Item leafBall;
    public static Item dagger;
    public static Item fishingRodTropical;
    public static Item bambooChute;
    public static final ItemArmor.ArmorMaterial materialSnorkelGear;
    public static Item flippers;
    public static Item snorkel;
    public static Item recordBuriedTreasure;
    public static Item recordEasternIsles;
    public static Item recordLowTide;
    public static Item recordSummering;
    public static Item recordTheTribe;
    public static Item recordTradeWinds;
    public static Item portalEnchanter;
    public static Item bambooMug;
    public static Item tropiFrame;
    public static Item koaFrame;
    public static Item cocktail;
    public static Item snareTrap;
    public static Item encTropica;
    public static Item mobEgg;

    public static void init() {
        registerItem((Item) TCItemRegistry.frogLeg, "frogLeg");
        registerItem((Item) TCItemRegistry.cookedFrogLeg, "cookedFrogLeg");
        registerItem((Item) TCItemRegistry.poisonFrogSkin, "poisonFrogSkin");
        registerItem((Item) TCItemRegistry.freshMarlin, "freshMarlin");
        registerItem((Item) TCItemRegistry.searedMarlin, "searedMarlin");
        registerItem((Item) TCItemRegistry.grapefruit, "grapefruit");
        registerItem((Item) TCItemRegistry.lemon, "lemon");
        registerItem((Item) TCItemRegistry.lime, "lime");
        registerItem((Item) TCItemRegistry.orange, "orange");
        registerItem((Item) TCItemRegistry.scale, "scale");
        registerItem((Item) TCItemRegistry.coconutChunk, "coconutChunk");
        registerItem((Item) TCItemRegistry.pineappleCubes, "pineappleCubes");
        registerItem((Item) TCItemRegistry.bambooStick, "bambooStick");
        registerItem((Item) TCItemRegistry.seaUrchinRoe, "seaUrchinRoe");
        registerItem((Item) TCItemRegistry.pearl, "pearl");
        registerItem((Item) TCItemRegistry.ore, "ore");
        registerItem((Item) TCItemRegistry.waterWand, "waterWand");
        registerItem((Item) TCItemRegistry.fishingNet, "fishingNet");
        registerItem((Item) TCItemRegistry.coffeeBean, "coffeeBean");
        registerItem((Item) TCItemRegistry.scaleBoots, "scaleBoots");
        registerItem((Item) TCItemRegistry.scaleLeggings, "scaleLeggings");
        registerItem((Item) TCItemRegistry.scaleChestplate, "scaleChestplate");
        registerItem((Item) TCItemRegistry.scaleHelmet, "scaleHelmet");
        registerItem((Item) TCItemRegistry.fireBoots, "fireBoots");
        registerItem((Item) TCItemRegistry.fireLeggings, "fireLeggings");
        registerItem((Item) TCItemRegistry.fireChestplate, "fireChestplate");
        registerItem((Item) TCItemRegistry.fireHelmet, "fireHelmet");
        registerItem((Item) TCItemRegistry.axeEudialyte, "axeEudialyte");
        registerItem((Item) TCItemRegistry.hoeEudialyte, "hoeEudialyte");
        registerItem((Item) TCItemRegistry.pickaxeEudialyte, "pickaxeEudialyte");
        registerItem((Item) TCItemRegistry.shovelEudialyte, "shovelEudialyte");
        registerItem((Item) TCItemRegistry.swordEudialyte, "swordEudialyte");
        registerItem((Item) TCItemRegistry.axeZircon, "axeZircon");
        registerItem((Item) TCItemRegistry.hoeZircon, "hoeZircon");
        registerItem((Item) TCItemRegistry.pickaxeZircon, "pickaxeZircon");
        registerItem((Item) TCItemRegistry.shovelZircon, "shovelZircon");
        registerItem((Item) TCItemRegistry.swordZircon, "swordZircon");
        registerItem((Item) TCItemRegistry.tikiTorch, "tikiTorch");
        registerItem((Item) TCItemRegistry.bambooDoor, "bambooDoor");
        registerItem((Item) TCItemRegistry.bucketTropicsWater, "bucketTropicsWater");
        registerItem((Item) TCItemRegistry.chair, "chair");
        registerItem((Item) TCItemRegistry.umbrella, "umbrella");
        registerItem((Item) TCItemRegistry.fertilizer, "fertilizer");
        registerItem((Item) TCItemRegistry.curare, "curare");
        registerItem((Item) TCItemRegistry.dart, "dart");
        registerItem((Item) TCItemRegistry.blowGun, "blowGun");
        registerItem((Item) TCItemRegistry.axeZirconium, "axeZirconium");
        registerItem((Item) TCItemRegistry.hoeZirconium, "hoeZirconium");
        registerItem((Item) TCItemRegistry.pickaxeZirconium, "pickaxeZirconium");
        registerItem((Item) TCItemRegistry.shovelZirconium, "shovelZirconium");
        registerItem((Item) TCItemRegistry.swordZirconium, "swordZirconium");
        registerItem(TCItemRegistry.shells, "shell");
        registerItem(TCItemRegistry.bambooSpear, "bambooSpear");
        registerItem((Item) TCItemRegistry.coconutBomb, "coconutBomb");
        registerItem(TCItemRegistry.bambooChute, "bambooChute");
        registerItem(TCItemRegistry.snorkel, "snorkel");
        registerItem(TCItemRegistry.flippers, "flippers");
        registerItem(TCItemRegistry.dagger, "dagger");
        registerItem(TCItemRegistry.fishingRodTropical, "fishingRodTropical");
        registerItem(TCItemRegistry.recordBuriedTreasure, "record_buriedtreasure");
        registerItem(TCItemRegistry.recordEasternIsles, "record_easternisles");
        registerItem(TCItemRegistry.recordLowTide, "record_lowtide");
        registerItem(TCItemRegistry.recordSummering, "record_summering");
        registerItem(TCItemRegistry.recordTheTribe, "record_thetribe");
        registerItem(TCItemRegistry.recordTradeWinds, "record_tradewinds");
        registerItem(TCItemRegistry.portalEnchanter, "portalEnchanter");
        registerItem(TCItemRegistry.bambooMug, "bambooMug");
        registerItem(TCItemRegistry.tropiFrame, "tropiFrame");
        registerItem(TCItemRegistry.koaFrame, "koaFrame");
        registerItem(TCItemRegistry.cocktail, "cocktail");
        registerItem(TCItemRegistry.leafBall, "leafBall");
        registerItem((Item) TCItemRegistry.fishBucket, "fishBucket");
        registerItem(TCItemRegistry.snareTrap, "snareTrap");
        registerItem(TCItemRegistry.encTropica, "encTropica");
        registerItem(TCItemRegistry.mobEgg, "egg");
    }

    private static void registerItem(final Item item, final String name) {
        GameRegistry.registerItem(item, name);
        item.setUnlocalizedName(name);
    }

    public static String[] getShellImageNames() {
        return new String[] { "shell_solo", "shell_frox", "shell_pab", "shell_rube", "shell_starfish", "shell_turtle" };
    }

    public static String[] getMaskDisplayNames() {
        return new String[] { "Square Zord", "Horn Monkey", "Oblongatron", "Headinator", "Square Horn", "Screw Attack",
            "The Brain", "Bat Boy", "Ashen Mask", "Ashen Mask", "Ashen Mask", "Ashen Mask", "Ashen Mask" };
    }

    public static String[] getMaskImageNames() {
        final String[] strArr = new String[getMaskDisplayNames().length];
        for (int i = 0; i < strArr.length; ++i) {
            strArr[i] = "mask_" + i;
        }
        return strArr;
    }

    static {
        frogLeg = (ItemTropicraft) new ItemTropicraft().setMaxStackSize(64);
        cookedFrogLeg = new ItemTropicraftFood(2, 0.15f);
        poisonFrogSkin = (ItemTropicraft) new ItemTropicraft().setMaxStackSize(64);
        freshMarlin = new ItemTropicraftFood(2, 0.3f);
        searedMarlin = new ItemTropicraftFood(8, 0.65f);
        grapefruit = new ItemTropicraftFood(2, 0.2f);
        lemon = new ItemTropicraftFood(2, 0.2f);
        lime = new ItemTropicraftFood(2, 0.2f);
        orange = new ItemTropicraftFood(2, 0.2f);
        scale = (ItemTropicraft) new ItemTropicraft().setMaxStackSize(64);
        coconutChunk = new ItemTropicraftFood(1, 0.1f);
        pineappleCubes = new ItemTropicraftFood(1, 0.1f);
        bambooStick = (ItemTropicraft) new ItemTropicraft().setMaxStackSize(64);
        seaUrchinRoe = new ItemTropicraftFood(3, 0.3f);
        pearl = (ItemTropicraft) new ItemTropicraftMulti(TCNames.pearlNames);
        ore = (ItemTropicraft) new ItemTropicraftOre(TCNames.oreNames);
        waterWand = (ItemTropicraft) new ItemWaterWand();
        fishingNet = new ItemTropicraft();
        coffeeBean = (ItemTropicraft) new ItemCoffeeBean().setMaxStackSize(64);
        materialScaleArmor = EnumHelper.addArmorMaterial("scale", 18, new int[] { 2, 6, 5, 2 }, 9);
        scaleBoots = (ItemTropicraftArmor) new ItemScaleArmor(TCItemRegistry.materialScaleArmor, 0, 3);
        scaleLeggings = (ItemTropicraftArmor) new ItemScaleArmor(TCItemRegistry.materialScaleArmor, 0, 2);
        scaleChestplate = (ItemTropicraftArmor) new ItemScaleArmor(TCItemRegistry.materialScaleArmor, 0, 1);
        scaleHelmet = (ItemTropicraftArmor) new ItemScaleArmor(TCItemRegistry.materialScaleArmor, 0, 0);
        materialFireArmor = EnumHelper.addArmorMaterial("fire", 12, new int[] { 2, 4, 5, 6 }, 9);
        fireBoots = (ItemTropicraftArmor) new ItemFireArmor(TCItemRegistry.materialFireArmor, 0, 3);
        fireLeggings = (ItemTropicraftArmor) new ItemFireArmor(TCItemRegistry.materialFireArmor, 0, 2);
        fireChestplate = (ItemTropicraftArmor) new ItemFireArmor(TCItemRegistry.materialFireArmor, 0, 1);
        fireHelmet = (ItemTropicraftArmor) new ItemFireArmor(TCItemRegistry.materialFireArmor, 0, 0);
        materialMaskArmor = EnumHelper.addArmorMaterial("mask", 18, new int[] { 2, 6, 5, 2 }, 9);
        ashenMask = (ItemTropicraftArmor) new ItemAshenMask(
            TCItemRegistry.materialMaskArmor,
            0,
            0,
            getMaskDisplayNames(),
            getMaskImageNames()).setCreativeTab(TCCreativeTabRegistry.tabDecorations);
        TCItemRegistry.materialZirconTools = EnumHelper.addToolMaterial("zircon", 2, 500, 6.5f, 2.5f, 14);
        TCItemRegistry.materialEudialyteTools = EnumHelper.addToolMaterial("eudialyte", 2, 750, 5.5f, 1.5f, 14);
        TCItemRegistry.materialZirconiumTools = EnumHelper.addToolMaterial("zirconium", 3, 1800, 8.5f, 3.5f, 10);
        hoeEudialyte = (ItemHoe) new ItemTropicraftHoe(TCItemRegistry.materialEudialyteTools, "hoeEudialyte");
        hoeZircon = (ItemHoe) new ItemTropicraftHoe(TCItemRegistry.materialZirconTools, "hoeZircon");
        hoeZirconium = (ItemHoe) new ItemTropicraftHoe(TCItemRegistry.materialZirconiumTools, "hoeZirconium");
        shovelEudialyte = (ItemTropicraftTool) new ItemTropicraftShovel(
            TCItemRegistry.materialEudialyteTools,
            "shovelEudialyte");
        shovelZircon = (ItemTropicraftTool) new ItemTropicraftShovel(
            TCItemRegistry.materialZirconTools,
            "shovelZircon");
        shovelZirconium = (ItemTropicraftTool) new ItemTropicraftShovel(
            TCItemRegistry.materialZirconiumTools,
            "shovelZirconium");
        pickaxeEudialyte = (ItemTropicraftTool) new ItemTropicraftPickaxe(
            TCItemRegistry.materialEudialyteTools,
            "pickaxeEudialyte");
        pickaxeZircon = (ItemTropicraftTool) new ItemTropicraftPickaxe(
            TCItemRegistry.materialZirconTools,
            "pickaxeZircon");
        pickaxeZirconium = (ItemTropicraftTool) new ItemTropicraftPickaxe(
            TCItemRegistry.materialZirconiumTools,
            "pickaxeZirconium");
        axeEudialyte = (ItemTropicraftTool) new ItemTropicraftAxe(
            TCItemRegistry.materialEudialyteTools,
            "axeEudialyte");
        axeZircon = (ItemTropicraftTool) new ItemTropicraftAxe(TCItemRegistry.materialZirconTools, "axeZircon");
        axeZirconium = (ItemTropicraftTool) new ItemTropicraftAxe(
            TCItemRegistry.materialZirconiumTools,
            "axeZirconium");
        swordEudialyte = (ItemSword) new ItemTropicraftSword(TCItemRegistry.materialEudialyteTools, "swordEudialyte");
        swordZircon = (ItemSword) new ItemTropicraftSword(TCItemRegistry.materialZirconTools, "swordZircon");
        swordZirconium = (ItemSword) new ItemTropicraftSword(TCItemRegistry.materialZirconiumTools, "swordZirconium");
        tikiTorch = (ItemTropicraft) new ItemTikiTorch();
        bambooDoor = (ItemTropicraft) new ItemBambooDoor();
        bucketTropicsWater = new ItemTropicsWaterBucket();
        fishBucket = new ItemFishBucket();
        chair = new ItemChair();
        umbrella = new ItemUmbrella();
        fertilizer = new ItemFertilizer();
        coconutBomb = (ItemTropicraft) new ItemCoconutBomb().setMaxStackSize(64);
        curare = new ItemCurare();
        dart = new ItemDart();
        blowGun = new ItemDartGun();
        shells = (Item) new ItemShell(TCNames.shellNames);
        TCItemRegistry.materialBambooTools = EnumHelper.addToolMaterial("bamboo", 1, 110, 1.2f, 1.0f, 6);
        bambooSpear = (Item) new ItemTropicraftSword(TCItemRegistry.materialBambooTools, "bambooSpear");
        TCItemRegistry.leafBall = new ItemTropicraftLeafballNew().setUnlocalizedName("leaf_green")
            .setCreativeTab(TCCreativeTabRegistry.tabCombat);
        TCItemRegistry.dagger = new ItemDagger(TCItemRegistry.materialZirconTools).setUnlocalizedName("dagger");
        TCItemRegistry.fishingRodTropical = new ItemTropicalFishingRod().setUnlocalizedName("FishingRodTropical");
        TCItemRegistry.bambooChute = new ItemBambooChute((Block) TCBlockRegistry.bambooChute)
            .setUnlocalizedName("BambooChute");
        materialSnorkelGear = EnumHelper.addArmorMaterial("watergear", 40, new int[] { 2, 4, 5, 6 }, 9);
        TCItemRegistry.flippers = (Item) new ItemFlippers(TCItemRegistry.materialSnorkelGear, 0, 3);
        TCItemRegistry.snorkel = (Item) new ItemSnorkel(TCItemRegistry.materialSnorkelGear, 0, 0);
        TCItemRegistry.recordBuriedTreasure = new ItemTropicraftMusicDisk(
            "buriedtreasure",
            "buriedtreasure",
            "Punchaface").setUnlocalizedName("Buried Treasure");
        TCItemRegistry.recordEasternIsles = new ItemTropicraftMusicDisk("easternisles", "easternisles", "Frox")
            .setUnlocalizedName("Eastern Isles");
        TCItemRegistry.recordLowTide = new ItemTropicraftMusicDisk("lowtide", "lowtide", "Punchaface")
            .setUnlocalizedName("Low Tide");
        TCItemRegistry.recordSummering = new ItemTropicraftMusicDisk("summering", "summering", "Billy Christiansen")
            .setUnlocalizedName("Summering");
        TCItemRegistry.recordTheTribe = new ItemTropicraftMusicDisk("thetribe", "thetribe", "Emile Van Krieken")
            .setUnlocalizedName("The Tribe");
        TCItemRegistry.recordTradeWinds = new ItemTropicraftMusicDisk("tradewinds", "tradewinds", "Frox")
            .setUnlocalizedName("Trade Winds");
        TCItemRegistry.portalEnchanter = (Item) new ItemPortalEnchanter();
        TCItemRegistry.bambooMug = new ItemTropicraft().setMaxStackSize(16);
        TCItemRegistry.tropiFrame = new ItemTCItemFrame((Class) EntityTCItemFrame.class, true)
            .setUnlocalizedName("tropiFrame");
        TCItemRegistry.koaFrame = new ItemTCItemFrame((Class) EntityTCItemFrame.class, false)
            .setUnlocalizedName("koaFrame");
        TCItemRegistry.cocktail = (Item) new ItemCocktail(TCCreativeTabRegistry.tabFood);
        TCItemRegistry.snareTrap = new ItemSnareTrap().setUnlocalizedName("snareTrap");
        TCItemRegistry.encTropica = new ItemTropBook("encTropica").setUnlocalizedName("encTropica");
        TCItemRegistry.mobEgg = (Item) new ItemMobEgg(TCNames.eggTextureNames);
    }
}
