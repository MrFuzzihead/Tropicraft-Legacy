package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockTropicraftStairs extends BlockStairs {

    public BlockTropicraftStairs(final String name, final Block block, final int meta) {
        super(block, meta);
        this.setBlockName(name);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }
}
