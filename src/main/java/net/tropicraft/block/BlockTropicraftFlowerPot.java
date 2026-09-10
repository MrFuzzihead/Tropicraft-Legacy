package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftFlowerPot extends BlockTropicraft implements ITileEntityProvider {

    public BlockTropicraftFlowerPot() {
        super(Material.circuits);
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
        this.setBlockTextureName("flowerPot");
        this.setBlockBoundsForItemRender();
    }

    public void setBlockBoundsForItemRender() {
        final float f = 0.375f;
        final float f2 = f / 2.0f;
        this.setBlockBounds(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, f, 0.5f + f2);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return TCRenderIDs.flowerPot;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4,
        final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        final ItemStack var10 = par5EntityPlayer.inventory.getCurrentItem();
        if (var10 == null) {
            return false;
        }
        if (par1World.getBlockMetadata(par2, par3, par4) != 0) {
            return false;
        }
        final int var11 = getMetaForPlant(var10);
        if (var11 > 0) {
            final TileEntityTropicraftFlowerPot pot = (TileEntityTropicraftFlowerPot) par1World
                .getTileEntity(par2, par3, par4);
            if (pot == null) {
                System.out.println("Flower pot was null!");
            }
            pot.setFlowerID((short) var11);
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var11, 2);
            par1World.markBlockForUpdate(par2, par3, par4);
            if (!par5EntityPlayer.capabilities.isCreativeMode) {
                final ItemStack itemStack = var10;
                if (--itemStack.stackSize <= 0) {
                    par5EntityPlayer.inventory
                        .setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack) null);
                }
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World world, final int x, final int y, final int z) {
        final ItemStack var5 = getPlantForMeta(world.getBlockMetadata(x, y, z));
        return (Item) ((var5 == null) ? Item.getItemFromBlock(this) : var5.getItem());
    }

    public boolean canPlaceBlockAt(final World par1World, final int par2, final int par3, final int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4)
            && World.doesBlockHaveSolidTopSurface((IBlockAccess) par1World, par2, par3 - 1, par4);
    }

    public void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4,
        final Block block) {
        if (!World.doesBlockHaveSolidTopSurface((IBlockAccess) par1World, par2, par3 - 1, par4)) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    public void dropBlockAsItemWithChance(final World par1World, final int par2, final int par3, final int par4,
        final int par5, final float par6, final int par7) {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);
        if (par5 > 0) {
            final ItemStack var8 = getPlantForMeta(par5);
            if (var8 != null) {
                this.dropBlockAsItem(par1World, par2, par3, par4, var8);
            }
        }
    }

    public int getDamageValue(final World par1World, final int par2, final int par3, final int par4) {
        final ItemStack var5 = getPlantForMeta(par1World.getBlockMetadata(par2, par3, par4));
        return (var5 == null) ? 0 : var5.getItemDamage();
    }

    @SideOnly(Side.CLIENT)
    public boolean isFlowerPot() {
        return true;
    }

    public static ItemStack getPlantForMeta(final int meta) {
        switch (meta) {
            case 1: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 0);
            }
            case 2: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 1);
            }
            case 3: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 2);
            }
            case 4: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 3);
            }
            case 5: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 4);
            }
            case 6: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 5);
            }
            case 7: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 6);
            }
            case 8: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 7);
            }
            case 9: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 8);
            }
            case 10: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 9);
            }
            case 11: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 10);
            }
            case 12: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 11);
            }
            case 13: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 12);
            }
            case 14: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 13);
            }
            case 15: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 14);
            }
            case 16: {
                return new ItemStack((Block) TCBlockRegistry.flowers, 1, 15);
            }
            case 17: {
                return new ItemStack((Block) TCBlockRegistry.tallFlowers, 1, 8);
            }
            case 18: {
                return new ItemStack((Block) TCBlockRegistry.pineapple, 1, 8);
            }
            case 19: {
                return new ItemStack((Block) TCBlockRegistry.saplings, 1, 0);
            }
            case 20: {
                return new ItemStack((Block) TCBlockRegistry.saplings, 1, 1);
            }
            case 21: {
                return new ItemStack((Block) TCBlockRegistry.saplings, 1, 2);
            }
            case 22: {
                return new ItemStack((Block) TCBlockRegistry.saplings, 1, 3);
            }
            case 23: {
                return new ItemStack((Block) TCBlockRegistry.saplings, 1, 4);
            }
            case 24: {
                return new ItemStack((Block) Blocks.red_flower, 1, 0);
            }
            default: {
                return null;
            }
        }
    }

    public static int getMetaForPlant(final ItemStack itemstack) {
        final Item item = itemstack.getItem();
        final int damage = itemstack.getItemDamage() + 1;
        if (item == Item.getItemFromBlock((Block) TCBlockRegistry.flowers)) {
            return damage;
        }
        if (item == Item.getItemFromBlock((Block) TCBlockRegistry.tallFlowers)) {
            return 17;
        }
        if (item == Item.getItemFromBlock((Block) TCBlockRegistry.pineapple)) {
            return 18;
        }
        if (item == Item.getItemFromBlock((Block) TCBlockRegistry.saplings)) {
            return 19 + damage - 1;
        }
        if (item == Item.getItemFromBlock((Block) Blocks.red_flower)) {
            return 25 + damage - 1;
        }
        if (item == Item.getItemFromBlock((Block) Blocks.yellow_flower)) {
            return 34 + damage - 1;
        }
        return 0;
    }

    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return TileEntityFactory.getFlowerPotTE();
    }
}
