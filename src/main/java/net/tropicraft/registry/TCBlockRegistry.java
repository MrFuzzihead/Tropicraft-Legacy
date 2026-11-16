package net.tropicraft.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.tropicraft.block.BlockBambooChest;
import net.tropicraft.block.BlockBambooChute;
import net.tropicraft.block.BlockBambooDoor;
import net.tropicraft.block.BlockBambooMug;
import net.tropicraft.block.BlockBongoDrum;
import net.tropicraft.block.BlockBundle;
import net.tropicraft.block.BlockChunkOHead;
import net.tropicraft.block.BlockCoconut;
import net.tropicraft.block.BlockCoffeePlant;
import net.tropicraft.block.BlockCoral;
import net.tropicraft.block.BlockCurareBowl;
import net.tropicraft.block.BlockEIHMixer;
import net.tropicraft.block.BlockFirePit;
import net.tropicraft.block.BlockFruitLeaves;
import net.tropicraft.block.BlockKoaChest;
import net.tropicraft.block.BlockMineralSands;
import net.tropicraft.block.BlockPalmLeaves;
import net.tropicraft.block.BlockPineapple;
import net.tropicraft.block.BlockPortalWall;
import net.tropicraft.block.BlockPurchasePlate;
import net.tropicraft.block.BlockPurifiedSand;
import net.tropicraft.block.BlockRainStopper;
import net.tropicraft.block.BlockRainforestLeaves;
import net.tropicraft.block.BlockSifter;
import net.tropicraft.block.BlockTallFlowers;
import net.tropicraft.block.BlockTikiTorch;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.BlockTropicraftFence;
import net.tropicraft.block.BlockTropicraftFenceGate;
import net.tropicraft.block.BlockTropicraftFlower;
import net.tropicraft.block.BlockTropicraftFlowerPot;
import net.tropicraft.block.BlockTropicraftLog;
import net.tropicraft.block.BlockTropicraftMulti;
import net.tropicraft.block.BlockTropicraftOre;
import net.tropicraft.block.BlockTropicraftPlank;
import net.tropicraft.block.BlockTropicraftSapling;
import net.tropicraft.block.BlockTropicraftSlab;
import net.tropicraft.block.BlockTropicraftStairs;
import net.tropicraft.block.BlockTropicsPortal;
import net.tropicraft.block.BlockTropicsWater;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemBlockTropicraft;
import net.tropicraft.item.ItemFlowerPot;
import net.tropicraft.item.ItemPineapple;
import net.tropicraft.item.ItemTallFlowers;
import net.tropicraft.item.ItemTropicraftSlab;

import cpw.mods.fml.common.registry.GameRegistry;

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
        registerBlock(TCBlockRegistry.chunkOHead, "chunk");
        registerBlock(TCBlockRegistry.chunkStairs, "chunkStairs");
        registerBlock(TCBlockRegistry.eudialyteOre, "oreEudialyte");
        registerBlock(TCBlockRegistry.zirconOre, "oreZircon");
        registerBlock(TCBlockRegistry.azuriteOre, "oreAzurite");
        registerMultiBlock(TCBlockRegistry.oreBlocks, "blockOre", TCNames.oreBlockNames);
        registerBlock(TCBlockRegistry.thatchBundle, "thatch");
        Blocks.fire.setFireInfo(TCBlockRegistry.thatchBundle, 60, 20);
        registerMultiBlock(TCBlockRegistry.coral, "coral", TCNames.coralNames);
        registerBlock(TCBlockRegistry.bambooBundle, "bambooBundle");
        Blocks.fire.setFireInfo(TCBlockRegistry.bambooBundle, 5, 20);
        registerMultiBlock(TCBlockRegistry.logs, "log", TCNames.logNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.logs, 5, 5);
        registerMultiBlock(TCBlockRegistry.planks, "plank", TCNames.plankNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.planks, 5, 5);
        registerBlock(TCBlockRegistry.bambooStairs, "bambooStairs");
        Blocks.fire.setFireInfo(TCBlockRegistry.bambooStairs, 5, 20);
        registerBlock(TCBlockRegistry.thatchStairs, "thatchStairs");
        Blocks.fire.setFireInfo(TCBlockRegistry.thatchStairs, 60, 20);
        registerBlock(TCBlockRegistry.palmStairs, "palmStairs");
        Blocks.fire.setFireInfo(TCBlockRegistry.palmStairs, 5, 20);
        registerBlock(TCBlockRegistry.mahoganyStairs, "mahoganyStairs");
        Blocks.fire.setFireInfo(TCBlockRegistry.mahoganyStairs, 5, 20);
        registerMultiBlock(TCBlockRegistry.tallFlowers, "tallFlower", TCNames.tallFlowerNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.tallFlowers, 60, 100);
        registerMultiBlock(TCBlockRegistry.pineapple, "pineapple", TCNames.pineappleNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.pineapple, 60, 100);
        registerBlockNoName(TCBlockRegistry.bambooFence, "bambooFence");
        Blocks.fire.setFireInfo(TCBlockRegistry.bambooFence, 5, 20);
        registerBlockNoName(TCBlockRegistry.palmFence, "palmFence");
        Blocks.fire.setFireInfo(TCBlockRegistry.palmFence, 5, 20);
        registerMultiBlock(TCBlockRegistry.saplings, "sapling", TCNames.saplingNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.saplings, 30, 60);
        registerBlock(TCBlockRegistry.coffeePlant, "coffeePlant");
        Blocks.fire.setFireInfo(TCBlockRegistry.coffeePlant, 60, 100);
        registerBlock(TCBlockRegistry.bambooFenceGate, "bambooFenceGate");
        Blocks.fire.setFireInfo(TCBlockRegistry.bambooFenceGate, 5, 20);
        registerBlock(TCBlockRegistry.palmFenceGate, "palmFenceGate");
        Blocks.fire.setFireInfo(TCBlockRegistry.palmFenceGate, 5, 20);
        registerBlock(TCBlockRegistry.tikiTorch, "tikiTorch");
        registerBlock(TCBlockRegistry.bambooDoor, "bambooDoor");
        Blocks.fire.setFireInfo(TCBlockRegistry.bambooDoor, 5, 20);
        // NOTE: Slabs are intentionally not registered as flammable here. The Forest Altar Ruin uses
        // the "Chunk" (metadata 2) slab as a non-flammable buffer around its eternal netherrack
        // fire; if flammable slab metadata is ever added, that variant must stay non-flammable.
        registerMultiBlock(
            TCBlockRegistry.singleSlabs,
            "singleSlabs",
            (Class<? extends ItemBlock>) ItemTropicraftSlab.class,
            new Object[] { TCBlockRegistry.singleSlabs, TCBlockRegistry.doubleSlabs, false });
        registerMultiBlock(
            TCBlockRegistry.doubleSlabs,
            "doubleSlabs",
            (Class<? extends ItemBlock>) ItemTropicraftSlab.class,
            new Object[] { TCBlockRegistry.doubleSlabs, TCBlockRegistry.singleSlabs, true });
        registerBlock(TCBlockRegistry.tropicsWater, "tropicsWater");
        registerBlock(TCBlockRegistry.rainStopper, "rainStopper");
        registerMultiBlock(TCBlockRegistry.flowers, "flower", TCNames.flowerIndices);
        Blocks.fire.setFireInfo(TCBlockRegistry.flowers, 60, 100);
        registerBlock(TCBlockRegistry.flowerPot, ItemFlowerPot.class, "flowerPot");
        registerBlock(TCBlockRegistry.coconut, "coconut");
        registerBlock(TCBlockRegistry.firePit, "firePit");
        registerBlock(TCBlockRegistry.bambooChest, "bambooChest");
        registerBlock(TCBlockRegistry.tropicsPortal, "portal");
        registerBlock(TCBlockRegistry.tropicsPortalWall, "portalWall");
        registerMultiBlock(TCBlockRegistry.fruitLeaves, "leaf", TCNames.fruitLeafNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.fruitLeaves, 30, 60);
        registerBlock(TCBlockRegistry.palmLeaves, "leafPalm");
        Blocks.fire.setFireInfo(TCBlockRegistry.palmLeaves, 30, 60);
        registerMultiBlock(TCBlockRegistry.rainforestLeaves, "leafRainforest", TCNames.rainforestLeafNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.rainforestLeaves, 30, 60);
        registerBlock(TCBlockRegistry.bambooChute, "bambooChute");
        Blocks.fire.setFireInfo(TCBlockRegistry.bambooChute, 60, 100);
        registerBlock(TCBlockRegistry.purifiedSand, "purifiedSand");
        registerMultiBlock(TCBlockRegistry.mineralSands, "mineralSand", TCNames.mineralSandNames);
        registerBlock(TCBlockRegistry.sifter, "sifter");
        registerBlock(TCBlockRegistry.curareBowl, "curareBowl");
        registerMultiBlock(TCBlockRegistry.bongoDrum, "bongoDrum", TCNames.bongoDrumNames);
        Blocks.fire.setFireInfo(TCBlockRegistry.bongoDrum, 5, 20);
        registerBlock(TCBlockRegistry.koaChest, "koaChest");
        registerBlock(TCBlockRegistry.purchasePlate, "purchasePlate");
        registerBlock(TCBlockRegistry.bambooMug, "bambooMug");
        registerBlock(TCBlockRegistry.eihMixer, "eihMixer");
    }

    private static void registerMultiBlock(final Block block, final String name, final Class<? extends ItemBlock> c,
        final Object[] params) {
        GameRegistry.registerBlock(block, c, "tile." + name, params);
        block.setBlockName(name);
    }

    private static void registerMultiBlock(final Block block, final String name, final String[] names) {
        final List<String> namesList = new ArrayList<String>();
        Collections.addAll(namesList, names);
        final Class<? extends ItemBlock> clazz = (Class<? extends ItemBlock>) (TCBlockRegistry.multiBlockMap
            .containsKey(name) ? ((Class<? extends ItemBlock>) TCBlockRegistry.multiBlockMap.get(name))
                : ItemBlockTropicraft.class);
        GameRegistry.registerBlock(block, clazz, "tile." + name, new Object[] { namesList });
        block.setBlockName(name);
    }

    private static void registerBlock(final Block block, final String name) {
        GameRegistry.registerBlock(block, "tile." + name);
        block.setBlockName(name);
    }

    private static void registerBlock(final Block block, final Class<? extends ItemBlock> itemClass,
        final String name) {
        GameRegistry.registerBlock(block, itemClass, "tile." + name);
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
        chunkStairs = new BlockTropicraftStairs("chunkStairs", TCBlockRegistry.chunkOHead, 0);
        eudialyteOre = new BlockTropicraftOre().setHardness(3.0f)
            .setResistance(5.0f);
        zirconOre = new BlockTropicraftOre().setHardness(4.0f)
            .setResistance(5.0f);
        azuriteOre = new BlockTropicraftOre().setHardness(3.0f)
            .setResistance(5.0f);
        oreBlocks = (BlockTropicraft) new BlockTropicraftMulti(TCNames.oreBlockNames);
        thatchBundle = (BlockTropicraft) new BlockBundle("thatch").setStepSound(Block.soundTypeGrass);
        coral = (BlockTropicraft) new BlockCoral(TCNames.coralNames);
        bambooBundle = (BlockTropicraft) new BlockBundle("bambooBundle").setHardness(1.0f)
            .setResistance(0.1f)
            .setStepSound(Block.soundTypeWood);
        logs = (BlockTropicraft) new BlockTropicraftLog(TCNames.logNames);
        planks = (BlockTropicraft) new BlockTropicraftPlank(TCNames.plankNames);
        bambooStairs = new BlockTropicraftStairs("bambooStairs", TCBlockRegistry.bambooBundle, 0);
        thatchStairs = new BlockTropicraftStairs("thatchStairs", TCBlockRegistry.thatchBundle, 0);
        palmStairs = new BlockTropicraftStairs("palmStairs", TCBlockRegistry.planks, 0);
        mahoganyStairs = new BlockTropicraftStairs("mahoganyStairs", TCBlockRegistry.planks, 3);
        pineapple = new BlockPineapple(TCNames.pineappleNames);
        tallFlowers = new BlockTallFlowers(TCNames.tallFlowerNames);
        bambooFenceGate = new BlockTropicraftFenceGate(
            TCBlockRegistry.bambooBundle,
            0,
            "bambooFenceGate",
            Material.wood);
        palmFenceGate = new BlockTropicraftFenceGate(TCBlockRegistry.planks, 1, "palmFenceGate", Material.wood);
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
        firePit = new BlockFirePit();
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
