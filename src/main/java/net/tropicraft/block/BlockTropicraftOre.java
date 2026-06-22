package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class BlockTropicraftOre extends BlockTropicraft {

    private Random rand;

    public BlockTropicraftOre() {
        super(Material.rock);
        this.rand = new Random();
    }

    public Item getItemDropped(final int metadata, final Random p_149650_2_, final int fortune) {
        return TCItemRegistry.ore;
    }

    public int quantityDropped(final int meta, final int fortune, final Random random) {
        if (this == TCBlockRegistry.eudialyteOre) {
            return 4 + random.nextInt(3);
        }
        if (this == TCBlockRegistry.zirconOre) {
            return 2 + random.nextInt(2);
        }
        if (this == TCBlockRegistry.azuriteOre) {
            return 5 + random.nextInt(7);
        }
        return 1;
    }

    public int quantityDroppedWithBonus(final int metadata, final Random random) {
        if (metadata > 0 && Item.getItemFromBlock((Block) this) != this.getItemDropped(0, random, metadata)) {
            int j = random.nextInt(metadata + 2) - 1;
            if (j < 0) {
                j = 0;
            }
            return this.quantityDropped(metadata, 0, random) * (j + 1);
        }
        return this.quantityDropped(metadata, 0, random);
    }

    public int getExpDrop(final IBlockAccess world, final int metadata, final int fortune) {
        if (this.getItemDropped(metadata, this.rand, fortune) != Item.getItemFromBlock((Block) this)) {
            int j1 = 0;
            if (this == TCBlockRegistry.azuriteOre) {
                j1 = MathHelper.getRandomIntegerInRange(this.rand, 5, 7);
            } else if (this == TCBlockRegistry.eudialyteOre) {
                j1 = MathHelper.getRandomIntegerInRange(this.rand, 3, 5);
            } else if (this == TCBlockRegistry.zirconOre) {
                j1 = MathHelper.getRandomIntegerInRange(this.rand, 1, 3);
            }
            return j1;
        }
        return 0;
    }

    public int damageDropped(final int metadata) {
        if (this == TCBlockRegistry.eudialyteOre) {
            return 0;
        }
        if (this == TCBlockRegistry.zirconOre) {
            return 1;
        }
        if (this == TCBlockRegistry.azuriteOre) {
            return 2;
        }
        return 0;
    }
}
