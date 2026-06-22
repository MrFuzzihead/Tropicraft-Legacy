package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemFertilizer extends ItemTropicraft {

    public ItemFertilizer() {
        this.maxStackSize = 64;
        this.setCreativeTab(TCCreativeTabRegistry.tabMaterials);
    }

    public boolean onItemUse(final ItemStack itemstack, final EntityPlayer entityplayer, final World world, final int i,
        final int j, final int k, final int l, final float par8, final float par9, final float par10) {
        final Block block = world.getBlock(i, j, k);
        if (block == TCBlockRegistry.saplings) {
            if (!world.isRemote) {
                TCBlockRegistry.saplings.markOrGrowMarked(world, i, j, k, world.rand);
                --itemstack.stackSize;
            }
            return true;
        }
        boolean decrementStack = false;
        if (block instanceof IGrowable) {
            final IGrowable igrowable = (IGrowable) block;
            if (igrowable.func_149851_a(world, i, j, k, world.isRemote) && !world.isRemote
                && igrowable.func_149852_a(world, world.rand, i, j, k)) {
                decrementStack = true;
                igrowable.func_149853_b(world, world.rand, i, j, k);
            }
        }
        if (block == Blocks.grass) {
            if (!world.isRemote) {
                decrementStack = true;
                int j2 = 0;
                Label_0481_Outer: while (j2 < 128) {
                    int k2 = i;
                    int l2 = j + 1;
                    int i2 = k;
                    int j3 = 0;
                    while (true) {
                        while (j3 < j2 / 16) {
                            k2 += ItemFertilizer.itemRand.nextInt(3) - 1;
                            l2 += (ItemFertilizer.itemRand.nextInt(3) - 1) * ItemFertilizer.itemRand.nextInt(3) / 2;
                            i2 += ItemFertilizer.itemRand.nextInt(3) - 1;
                            if (world.getBlock(k2, l2 - 1, i2) == Blocks.grass) {
                                if (!world.isBlockNormalCubeDefault(k2, l2, i2, true)) {
                                    ++j3;
                                    continue Label_0481_Outer;
                                }
                            }
                            ++j2;
                            continue Label_0481_Outer;
                        }
                        if (!world.isAirBlock(k2, l2, i2)) {
                            continue;
                        }
                        if (ItemFertilizer.itemRand.nextInt(9) == 0) {
                            world.setBlock(
                                k2,
                                l2,
                                i2,
                                (Block) TCBlockRegistry.flowers,
                                ItemFertilizer.itemRand.nextInt(16),
                                3);
                            continue;
                        }
                        if (ItemFertilizer.itemRand.nextInt(9) == 0) {
                            world.setBlock(
                                k2,
                                l2,
                                i2,
                                (Block) TCBlockRegistry.flowers,
                                ItemFertilizer.itemRand.nextInt(16),
                                3);
                            continue;
                        }
                        if (ItemFertilizer.itemRand.nextInt(9) == 0) {
                            world.setBlock(k2, l2, i2, (Block) TCBlockRegistry.pineapple, 7, 3);
                            world.setBlock(k2, l2 + 1, i2, (Block) TCBlockRegistry.pineapple, 8, 3);
                            continue;
                        }
                        if (ItemFertilizer.itemRand.nextInt(8) == 0) {
                            world.setBlock(k2, l2, i2, (Block) TCBlockRegistry.tallFlowers, 0, 3);
                            world.setBlock(k2, l2 + 1, i2, (Block) TCBlockRegistry.tallFlowers, 1, 3);
                        }
                        continue;
                    }
                }
            }
            if (decrementStack) {
                --itemstack.stackSize;
            }
            return true;
        }
        if (decrementStack) {
            --itemstack.stackSize;
        }
        return false;
    }
}
