package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemTikiTorch extends ItemTropicraft {

    public ItemTikiTorch() {
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
    }

    public boolean onItemUse(final ItemStack itemstack, final EntityPlayer player, final World world, int x, int y,
        int z, int side, final float hitX, final float hitY, final float hitZ) {
        final Block block = world.getBlock(x, y, z);
        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 0x7) < 1) {
            side = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass
            && block != Blocks.deadbush
            && !block.isReplaceable((IBlockAccess) world, x, y, z)) {
                if (side == 0) {
                    --y;
                }
                if (side == 1) {
                    ++y;
                }
                if (side == 2) {
                    --z;
                }
                if (side == 3) {
                    ++z;
                }
                if (side == 4) {
                    --x;
                }
                if (side == 5) {
                    ++x;
                }
            }
        if (itemstack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, itemstack)) {
            return false;
        }
        if (y == 255 && block.getMaterial()
            .isSolid()) {
            return false;
        }
        if (!world.canPlaceEntityOnSide(block, x, y, z, false, side, (Entity) player, itemstack)) {
            return false;
        }
        final Block blockBelow = world.getBlock(x, y - 1, z);
        if (blockBelow instanceof BlockFence) {
            world.setBlock(x, y, z, (Block) TCBlockRegistry.tikiTorch, 0, 3);
            world.playSoundEffect(
                (double) (x + 0.5f),
                (double) (y + 0.5f),
                (double) (z + 0.5f),
                TCBlockRegistry.tikiTorch.stepSound.soundName,
                (TCBlockRegistry.tikiTorch.stepSound.getVolume() + 1.0f) / 2.0f,
                TCBlockRegistry.tikiTorch.stepSound.getPitch() * 0.8f);
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            --itemstack.stackSize;
            return true;
        }
        if (world.isAirBlock(x, y + 1, z) && world.isAirBlock(x, y + 2, z)) {
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            world.setBlock(x, y, z, (Block) TCBlockRegistry.tikiTorch, 1, 3);
            world.setBlock(x, y + 1, z, (Block) TCBlockRegistry.tikiTorch, 1, 3);
            world.setBlock(x, y + 2, z, (Block) TCBlockRegistry.tikiTorch, 0, 3);
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            world.playSoundEffect(
                (double) (x + 0.5f),
                (double) (y + 0.5f),
                (double) (z + 0.5f),
                TCBlockRegistry.tikiTorch.stepSound.soundName,
                (TCBlockRegistry.tikiTorch.stepSound.getVolume() + 1.0f) / 2.0f,
                TCBlockRegistry.tikiTorch.stepSound.getPitch() * 0.8f);
            --itemstack.stackSize;
            return true;
        }
        return true;
    }
}
