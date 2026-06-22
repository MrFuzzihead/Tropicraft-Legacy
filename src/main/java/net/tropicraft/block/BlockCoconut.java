package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockCoconut extends BlockTropicraft {

    public BlockCoconut() {
        super(Material.gourd);
        final float f = 0.225f;
        this.setBlockBounds(0.5f - f, f, 0.5f - f, 0.5f + f, 1.0f - f, 0.5f + f);
        this.setCreativeTab(TCCreativeTabRegistry.tabFood);
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return 1;
    }

    public Item getItemDropped(final int meta, final Random rand, final int unused) {
        return null;
    }
}
