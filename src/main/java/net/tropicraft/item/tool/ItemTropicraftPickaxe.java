package net.tropicraft.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.tropicraft.block.BlockTropicraftOre;
import net.tropicraft.registry.TCBlockRegistry;

import com.google.common.collect.Sets;

public class ItemTropicraftPickaxe extends ItemTropicraftTool {

    private static final Set<Block> blocksEffectiveAgainst;

    public ItemTropicraftPickaxe(final Item.ToolMaterial toolMaterial, final String textureName) {
        super(2.0f, toolMaterial, ItemTropicraftPickaxe.blocksEffectiveAgainst);
        this.setTextureName(textureName);
    }

    public boolean func_150897_b(final Block block) {
        if (!(block instanceof BlockTropicraftOre)) {
            return (block
                == Blocks.obsidian)
                    ? (this.toolMaterial.getHarvestLevel() == 3)
                    : ((block != Blocks.diamond_block && block != Blocks.diamond_ore)
                        ? ((block != Blocks.emerald_ore && block != Blocks.emerald_block)
                            ? ((block != Blocks.gold_block && block != Blocks.gold_ore)
                                ? ((block != Blocks.iron_block && block != Blocks.iron_ore)
                                    ? ((block != Blocks.lapis_block && block != Blocks.lapis_ore)
                                        ? ((block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore)
                                            ? (block.getMaterial() == Material.rock
                                                || block.getMaterial() == Material.iron
                                                || block.getMaterial() == Material.anvil)
                                            : (this.toolMaterial.getHarvestLevel() >= 2))
                                        : (this.toolMaterial.getHarvestLevel() >= 1))
                                    : (this.toolMaterial.getHarvestLevel() >= 1))
                                : (this.toolMaterial.getHarvestLevel() >= 2))
                            : (this.toolMaterial.getHarvestLevel() >= 2))
                        : (this.toolMaterial.getHarvestLevel() >= 2));
        }
        if (block == TCBlockRegistry.azuriteOre) {
            return this.toolMaterial.getHarvestLevel() == 3;
        }
        return this.toolMaterial.getHarvestLevel() > 1;
    }

    public float func_150893_a(final ItemStack stack, final Block block) {
        return (block.getMaterial() != Material.iron && block.getMaterial() != Material.anvil
            && block.getMaterial() != Material.rock) ? super.func_150893_a(stack, block)
                : this.efficiencyOnProperMaterial;
    }

    static {
        blocksEffectiveAgainst = Sets.newHashSet(
            Blocks.cobblestone,
            (Block) Blocks.double_stone_slab,
            (Block) Blocks.stone_slab,
            Blocks.stone,
            Blocks.sandstone,
            Blocks.mossy_cobblestone,
            Blocks.iron_ore,
            Blocks.iron_block,
            Blocks.coal_ore,
            Blocks.gold_block,
            Blocks.gold_ore,
            Blocks.diamond_ore,
            Blocks.diamond_block,
            Blocks.ice,
            Blocks.netherrack,
            Blocks.lapis_ore,
            Blocks.lapis_block,
            Blocks.redstone_ore,
            Blocks.lit_redstone_ore,
            Blocks.rail,
            Blocks.detector_rail,
            Blocks.golden_rail,
            Blocks.activator_rail,
            TCBlockRegistry.azuriteOre,
            (Block) TCBlockRegistry.oreBlocks,
            TCBlockRegistry.eudialyteOre,
            TCBlockRegistry.zirconOre);
    }
}
