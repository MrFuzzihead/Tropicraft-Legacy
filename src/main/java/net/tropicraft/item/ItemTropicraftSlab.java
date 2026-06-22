package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.tropicraft.block.BlockTropicraftSlab;

public class ItemTropicraftSlab extends ItemSlab {

    public ItemTropicraftSlab(final Block block, final BlockTropicraftSlab slab1, final BlockTropicraftSlab slab2,
        final Boolean isFullSlab) {
        super(block, (BlockSlab) slab1, (BlockSlab) slab2, (boolean) isFullSlab);
    }
}
