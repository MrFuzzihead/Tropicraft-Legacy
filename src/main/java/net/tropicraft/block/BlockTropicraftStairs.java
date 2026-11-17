package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftNames;

public class BlockTropicraftStairs extends BlockStairs {

    public BlockTropicraftStairs(final String name, final Block block, final int meta) {
        super(block, meta);
        this.setBlockName(name);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public String getUnlocalizedName() {
        return TropicraftNames.block(super.getUnlocalizedName());
    }

}
