package net.tropicraft.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.tropicraft.block.*;
import net.tropicraft.info.*;
import net.tropicraft.item.*;

import cpw.mods.fml.common.registry.*;

public class TCBlockRegistry {

    private static final Map<String, Class<? extends ItemBlock>> multiBlockMap;
    public static final BlockTropicraft chunkOHead;
    public static final BlockTropicraftStairs chunkStairs;
    public static final Block eudialyteOre;
    public static final Block zirconOre;
    public static final Block azuriteOre;
    public static final BlockTropicraft oreBlocks;
    public static final BlockTropicraft thatchBundle;
    public static final BlockTropicraft coral;
    public static final BlockTropicraft bambooBundle;
    public static final BlockTropicraft logs;
    public static final BlockTropicraft planks;
    public static final BlockTropicraftStairs bambooStairs;
    public static final BlockTropicraftStairs thatchStairs;
    public static final BlockTropicraftStairs palmStairs;
    public static final BlockTropicraftStairs mahoganyStairs;
    public static final BlockPineapple pineapple;
    public static final BlockTallFlowers tallFlowers;
    public static final BlockTropicraftFenceGate bambooFenceGate;
    public static final BlockTropicraftFenceGate palmFenceGate;
    public static final BlockTropicraftFence bambooFence;
    public static final BlockTropicraftFence palmFence;
    public static final BlockTropicraftSapling saplings;
    public static final BlockTropicraft coffeePlant;
    public static final BlockTropicraft tikiTorch;
    public static final BlockDoor bambooDoor;
    public static final BlockSlab singleSlabs;
    public static final BlockSlab doubleSlabs;
    public static final BlockTropicsWater tropicsWater;
    public static final BlockTropicraft rainStopper;
    public static final BlockTropicraftFlower flowers;
    public static final BlockTropicraftFlowerPot flowerPot;
    public static final Block firePit;
    public static final BlockCoconut coconut;
    public static final BlockPortalWall tropicsPortalWall;
    public static final BlockTropicsPortal tropicsPortal;
    public static final BlockBambooChest bambooChest;
    public static final BlockFruitLeaves fruitLeaves;
    public static final BlockPalmLeaves palmLeaves;
    public static final BlockRainforestLeaves rainforestLeaves;
    public static final BlockBambooChute bambooChute;
    public static final BlockFalling purifiedSand;
    public static final BlockMineralSands mineralSands;
    public static final BlockSifter sifter;
    public static final BlockCurareBowl curareBowl;
    public static final BlockBongoDrum bongoDrum;
    public static final BlockKoaChest koaChest;
    public static final BlockPurchasePlate purchasePlate;
    public static final BlockBambooMug bambooMug;
    public static final BlockEIHMixer eihMixer;

    public static void init() {
        registerBlock((Block) TCBlockRegistry.chunkOHead, "chunk");
        registerBlock((Block) TCBlockRegistry.chunkStairs, "chunkStairs");
        registerBlock(TCBlockRegistry.eudialyteOre, "oreEudialyte");
        registerBlock(TCBlockRegistry.zirconOre, "oreZircon");
        registerBlock(TCBlockRegistry.azuriteOre, "oreAzurite");
        registerMultiBlock((Block) TCBlockRegistry.oreBlocks, "blockOre", TCNames.oreBlockNames);
        registerBlock((Block) TCBlockRegistry.thatchBundle, "thatch");
        registerMultiBlock((Block) TCBlockRegistry.coral, "coral", TCNames.coralNames);
        registerBlock((Block) TCBlockRegistry.bambooBundle, "bambooBundle");
        registerMultiBlock((Block) TCBlockRegistry.logs, "log", TCNames.logNames);
        Blocks.fire.setFireInfo((Block) TCBlockRegistry.logs, 5, 5);
        registerMultiBlock((Block) TCBlockRegistry.planks, "plank", TCNames.plankNames);
        Blocks.fire.setFireInfo((Block) TCBlockRegistry.planks, 5, 5);
        registerBlock((Block) TCBlockRegistry.bambooStairs, "bambooStairs");
        registerBlock((Block) TCBlockRegistry.thatchStairs, "thatchStairs");
        registerBlock((Block) TCBlockRegistry.palmStairs, "palmStairs");
        registerBlock((Block) TCBlockRegistry.mahoganyStairs, "mahoganyStairs");
        registerMultiBlock((Block) TCBlockRegistry.tallFlowers, "tallFlower", TCNames.tallFlowerNames);
        registerMultiBlock((Block) TCBlockRegistry.pineapple, "pineapple", TCNames.pineappleNames);
        registerBlockNoName((Block) TCBlockRegistry.bambooFence, "bambooFence");
        registerBlockNoName((Block) TCBlockRegistry.palmFence, "palmFence");
        registerMultiBlock((Block) TCBlockRegistry.saplings, "sapling", TCNames.saplingNames);
        registerBlock((Block) TCBlockRegistry.coffeePlant, "coffeePlant");
        registerBlock((Block) TCBlockRegistry.bambooFenceGate, "bambooFenceGate");
        registerBlock((Block) TCBlockRegistry.palmFenceGate, "palmFenceGate");
        Blocks.fire.setFireInfo((Block) TCBlockRegistry.palmFenceGate, 5, 5);
        registerBlock((Block) TCBlockRegistry.tikiTorch, "tikiTorch");
        registerBlock((Block) TCBlockRegistry.bambooDoor, "bambooDoor");
        registerMultiBlock(
            (Block) TCBlockRegistry.singleSlabs,
            "singleSlabs",
            (Class<? extends ItemBlock>) ItemTropicraftSlab.class,
            new Object[] { TCBlockRegistry.singleSlabs, TCBlockRegistry.doubleSlabs, false });
        registerMultiBlock(
            (Block) TCBlockRegistry.doubleSlabs,
            "doubleSlabs",
            (Class<? extends ItemBlock>) ItemTropicraftSlab.class,
            new Object[] { TCBlockRegistry.doubleSlabs, TCBlockRegistry.singleSlabs, true });
        registerBlock((Block) TCBlockRegistry.tropicsWater, "tropicsWater");
        registerBlock((Block) TCBlockRegistry.rainStopper, "rainStopper");
        registerMultiBlock((Block) TCBlockRegistry.flowers, "flower", TCNames.flowerIndices);
        registerBlock((Block) TCBlockRegistry.flowerPot, "flowerPot");
        registerBlock((Block) TCBlockRegistry.coconut, "coconut");
        registerBlock(TCBlockRegistry.firePit, "firePit");
        registerBlock((Block) TCBlockRegistry.bambooChest, "bambooChest");
        registerBlock((Block) TCBlockRegistry.tropicsPortal, "portal");
        registerBlock((Block) TCBlockRegistry.tropicsPortalWall, "portalWall");
        registerMultiBlock((Block) TCBlockRegistry.fruitLeaves, "leaf", TCNames.fruitLeafNames);
        Blocks.fire.setFireInfo((Block) TCBlockRegistry.fruitLeaves, 30, 60);
        registerBlock((Block) TCBlockRegistry.palmLeaves, "leafPalm");
        Blocks.fire.setFireInfo((Block) TCBlockRegistry.palmLeaves, 30, 60);
        registerMultiBlock((Block) TCBlockRegistry.rainforestLeaves, "leafRainforest", TCNames.rainforestLeafNames);
        registerBlock((Block) TCBlockRegistry.bambooChute, "bambooChute");
        registerBlock((Block) TCBlockRegistry.purifiedSand, "purifiedSand");
        registerMultiBlock((Block) TCBlockRegistry.mineralSands, "mineralSand", TCNames.mineralSandNames);
        registerBlock((Block) TCBlockRegistry.sifter, "sifter");
        registerBlock((Block) TCBlockRegistry.curareBowl, "curareBowl");
        registerMultiBlock((Block) TCBlockRegistry.bongoDrum, "bongoDrum", TCNames.bongoDrumNames);
        registerBlock((Block) TCBlockRegistry.koaChest, "koaChest");
        registerBlock((Block) TCBlockRegistry.purchasePlate, "purchasePlate");
        registerBlock((Block) TCBlockRegistry.bambooMug, "bambooMug");
        registerBlock((Block) TCBlockRegistry.eihMixer, "eihMixer");
    }

    private static void registerMultiBlock(final Block block, final String name, final Class<? extends ItemBlock> c,
        final Object[] params) {
        GameRegistry.registerBlock(block, (Class) c, "tile." + name, params);
        block.setBlockName(name);
    }

    private static void registerMultiBlock(final Block block, final String name, final String[] names) {
        final List<String> namesList = new ArrayList<String>();
        Collections.addAll(namesList, names);
        final Class<? extends ItemBlock> clazz = (Class<? extends ItemBlock>) (TCBlockRegistry.multiBlockMap
            .containsKey(name) ? ((Class<? extends ItemBlock>) TCBlockRegistry.multiBlockMap.get(name))
                : ItemBlockTropicraft.class);
        GameRegistry.registerBlock(block, (Class) clazz, "tile." + name, new Object[] { namesList });
        block.setBlockName(name);
    }

    private static void registerBlock(final Block block, final String name) {
        GameRegistry.registerBlock(block, "tile." + name);
        block.setBlockName(name);
    }

    private static void registerBlockNoName(final Block block, final String name) {
        GameRegistry.registerBlock(block, "tile." + name);
    }

    static {
        (multiBlockMap = new HashMap<String, Class<? extends ItemBlock>>()).put("pineapple", ItemPineapple.class);
        TCBlockRegistry.multiBlockMap.put("tallFlower", (Class<? extends ItemBlock>) ItemTallFlowers.class);
        TCBlockRegistry.multiBlockMap.put("singleSlabs", (Class<? extends ItemBlock>) ItemSlab.class);
        TCBlockRegistry.multiBlockMap.put("doubleSlabs", (Class<? extends ItemBlock>) ItemSlab.class);
        chunkOHead = (BlockTropicraft) new BlockChunkOHead();
        chunkStairs = new BlockTropicraftStairs("chunkStairs", (Block) TCBlockRegistry.chunkOHead, 0);
        eudialyteOre = new BlockTropicraftOre().setHardness(3.0f)
            .setResistance(5.0f);
        zirconOre = new BlockTropicraftOre().setHardness(4.0f)
            .setResistance(5.0f);
        azuriteOre = new BlockTropicraftOre().setHardness(3.0f)
            .setResistance(5.0f);
        oreBlocks = (BlockTropicraft) new BlockTropicraftMulti(TCNames.oreBlockNames);
        thatchBundle = (BlockTropicraft) new BlockBundle("thatch");
        coral = (BlockTropicraft) new BlockCoral(TCNames.coralNames);
        bambooBundle = (BlockTropicraft) new BlockBundle("bambooBundle").setHardness(1.0f)
            .setResistance(0.1f);
        logs = (BlockTropicraft) new BlockTropicraftLog(TCNames.logNames);
        planks = (BlockTropicraft) new BlockTropicraftPlank(TCNames.plankNames);
        bambooStairs = new BlockTropicraftStairs("bambooStairs", (Block) TCBlockRegistry.bambooBundle, 0);
        thatchStairs = new BlockTropicraftStairs("thatchStairs", (Block) TCBlockRegistry.thatchBundle, 0);
        palmStairs = new BlockTropicraftStairs("palmStairs", (Block) TCBlockRegistry.planks, 0);
        mahoganyStairs = new BlockTropicraftStairs("mahoganyStairs", (Block) TCBlockRegistry.planks, 3);
        pineapple = new BlockPineapple(TCNames.pineappleNames);
        tallFlowers = new BlockTallFlowers(TCNames.tallFlowerNames);
        bambooFenceGate = new BlockTropicraftFenceGate(
            (Block) TCBlockRegistry.bambooBundle,
            0,
            "bambooFenceGate",
            Material.wood);
        palmFenceGate = new BlockTropicraftFenceGate((Block) TCBlockRegistry.planks, 1, "palmFenceGate", Material.wood);
        bambooFence = new BlockTropicraftFence(
            "bambooFence",
            "bambooBundle_Side",
            TCBlockRegistry.bambooFenceGate,
            Material.wood);
        palmFence = new BlockTropicraftFence(
            "palmFence",
            "plank_" + TCNames.plankNames[0],
            TCBlockRegistry.palmFenceGate,
            Material.wood);
        saplings = new BlockTropicraftSapling(TCNames.saplingNames);
        coffeePlant = (BlockTropicraft) new BlockCoffeePlant();
        tikiTorch = (BlockTropicraft) new BlockTikiTorch();
        bambooDoor = (BlockDoor) new BlockBambooDoor();
        singleSlabs = (BlockSlab) new BlockTropicraftSlab(false);
        doubleSlabs = (BlockSlab) new BlockTropicraftSlab(true);
        tropicsWater = new BlockTropicsWater(TCFluidRegistry.tropicsWater, Material.water);
        rainStopper = (BlockTropicraft) new BlockRainStopper();
        flowers = new BlockTropicraftFlower(TCNames.flowerIndices);
        flowerPot = new BlockTropicraftFlowerPot();
        firePit = (Block) new BlockFirePit();
        coconut = new BlockCoconut();
        tropicsPortalWall = new BlockPortalWall();
        tropicsPortal = new BlockTropicsPortal(TCFluidRegistry.tropicsPortal, Material.water);
        bambooChest = new BlockBambooChest();
        fruitLeaves = new BlockFruitLeaves();
        palmLeaves = new BlockPalmLeaves();
        rainforestLeaves = new BlockRainforestLeaves();
        bambooChute = new BlockBambooChute();
        purifiedSand = (BlockFalling) new BlockPurifiedSand();
        mineralSands = new BlockMineralSands();
        sifter = new BlockSifter();
        curareBowl = new BlockCurareBowl();
        bongoDrum = new BlockBongoDrum(TCNames.bongoDrumNames);
        koaChest = new BlockKoaChest();
        purchasePlate = new BlockPurchasePlate();
        bambooMug = new BlockBambooMug();
        eihMixer = new BlockEIHMixer();
    }
}
