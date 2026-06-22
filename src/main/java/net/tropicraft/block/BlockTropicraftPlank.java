package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockTropicraftPlank extends BlockTropicraftMulti {

    public BlockTropicraftPlank(final String[] names) {
        super(names, Material.wood);
        this.setBlockTextureName("plank");
        this.disableStats();
        this.setHardness(2.0f);
        this.setStepSound(BlockTropicraftPlank.soundTypeWood);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }
}
