package net.tropicraft.registry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCCreativeTabRegistry {

    public static final CreativeTabs tabBlock;
    public static final CreativeTabs tabFood;
    public static final CreativeTabs tabTools;
    public static final CreativeTabs tabCombat;
    public static final CreativeTabs tabDecorations;
    public static final CreativeTabs tabMaterials;
    public static final CreativeTabs tabMusic;
    public static final CreativeTabs tabMisc;

    static {
        tabBlock = new CreativeTabBlockTC("buildingBlocks");
        tabFood = new CreativeTabFoodTC("food");
        tabTools = new CreativeTabToolsTC("tools");
        tabCombat = new CreativeTabCombatTC("combat");
        tabDecorations = new CreativeTabDecoTC("decorations");
        tabMaterials = new CreativeTabMaterialsTC("materials");
        tabMusic = new CreativeTabMusicTC("music");
        tabMisc = new CreativeTabMiscTC("misc");
    }

    public static class CreativeTabDecoTC extends CreativeTabs {

        public CreativeTabDecoTC(final String name) {
            super(name);
        }

        public Item getTabIconItem() {
            return (Item) TCItemRegistry.pearl;
        }
    }

    public static class CreativeTabMiscTC extends CreativeTabs {

        public CreativeTabMiscTC(final String name) {
            super(name);
        }

        public Item getTabIconItem() {
            return (Item) TCItemRegistry.fishingNet;
        }
    }

    public static class CreativeTabToolsTC extends CreativeTabs {

        public CreativeTabToolsTC(final String name) {
            super(name);
        }

        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return (Item) TCItemRegistry.pickaxeEudialyte;
        }
    }

    public static class CreativeTabCombatTC extends CreativeTabs {

        public CreativeTabCombatTC(final String name) {
            super(name);
        }

        public Item getTabIconItem() {
            return (Item) TCItemRegistry.swordZircon;
        }
    }

    public static class CreativeTabMaterialsTC extends CreativeTabs {

        public CreativeTabMaterialsTC(final String name) {
            super(name);
        }

        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return (Item) TCItemRegistry.fertilizer;
        }
    }

    public static class CreativeTabBlockTC extends CreativeTabs {

        public CreativeTabBlockTC(final String name) {
            super(name);
        }

        public Item getTabIconItem() {
            final ItemStack stack = new ItemStack((Block) TCBlockRegistry.bambooBundle);
            return stack.getItem();
        }
    }

    public static class CreativeTabFoodTC extends CreativeTabs {

        public CreativeTabFoodTC(final String par2Str) {
            super(par2Str);
        }

        public Item getTabIconItem() {
            return (Item) TCItemRegistry.lime;
        }
    }

    public static class CreativeTabMusicTC extends CreativeTabs {

        public CreativeTabMusicTC(final String par2Str) {
            super(par2Str);
        }

        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return TCItemRegistry.recordEasternIsles;
        }
    }
}
