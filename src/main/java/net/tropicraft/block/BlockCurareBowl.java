package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.registry.TCBlockRegistry;

public class BlockCurareBowl extends BlockTropicraft implements ITileEntityProvider {

    public BlockCurareBowl() {
        super(Material.rock);
    }

    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return TileEntityFactory.getCurareBowlTE();
    }

    public void breakBlock(final World par1World, final int par2, final int par3, final int par4, final Block par5,
        final int par6) {
        final TileEntityCurareBowl bowl = (TileEntityCurareBowl) par1World.getTileEntity(par2, par3, par4);
        if (bowl != null && !par1World.isRemote) {
            if (!bowl.hasMetMaxNumClicks()) {
                for (int var8 = 0; var8 < bowl.getIngredients().length; ++var8) {
                    final ItemStack var9 = bowl.getIngredientList()
                        .get(var8);
                    if (var9 != null) {
                        final float var10 = par1World.rand.nextFloat() * 0.8f + 0.1f;
                        final float var11 = par1World.rand.nextFloat() * 0.8f + 0.1f;
                        final EntityItem item = new EntityItem(
                            par1World,
                            (double) (par2 + var10),
                            (double) (par3 + var10),
                            (double) (par4 + var11),
                            var9);
                        par1World.spawnEntityInWorld((Entity) item);
                    }
                }
            } else {
                bowl.dropResult();
            }
        }
        par1World.removeTileEntity(par2, par3, par4);
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canPlaceBlockAt(final World par1World, final int par2, final int par3, final int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4)
            && World.doesBlockHaveSolidTopSurface((IBlockAccess) par1World, par2, par3 - 1, par4);
    }

    public int getRenderType() {
        return -1;
    }

    public boolean onBlockActivated(final World world, final int i, final int j, final int k,
        final EntityPlayer entityplayer, final int doop, final float f1, final float f2, final float f3) {
        if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null
            && entityplayer.getCurrentEquippedItem()
                .getItem() != null) {
            final TileEntityCurareBowl bowl = (TileEntityCurareBowl) world.getTileEntity(i, j, k);
            if (entityplayer.getCurrentEquippedItem()
                .getItem() == Items.stick) {
                if (bowl.isBowlFull()) {
                    bowl.incrementNumClicks();
                    if (bowl.hasMetMaxNumClicks()) {
                        bowl.resetClicks();
                        bowl.dropResult();
                    }
                }
            } else if (entityplayer.getCurrentEquippedItem()
                .getItem() == Item.getItemFromBlock((Block) TCBlockRegistry.flowers)) {
                    bowl.addIngredient(entityplayer.getCurrentEquippedItem());
                    if (!entityplayer.capabilities.isCreativeMode) {
                        final ItemStack getCurrentEquippedItem = entityplayer.getCurrentEquippedItem();
                        --getCurrentEquippedItem.stackSize;
                    }
                }
        }
        return true;
    }
}
