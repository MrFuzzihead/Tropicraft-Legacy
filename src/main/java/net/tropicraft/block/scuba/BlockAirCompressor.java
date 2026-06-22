package net.tropicraft.block.scuba;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockAirCompressor extends BlockTropicraft implements ITileEntityProvider {

    public BlockAirCompressor() {
        super(Material.rock);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.8f, 1.0f);
        this.isBlockContainer = true;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    public int getRenderType() {
        return TCRenderIDs.airCompressor;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderBlockPass() {
        return 0;
    }

    public void onBlockPlacedBy(final World par1World, final int par2, final int par3, final int par4,
        final EntityLivingBase e, final ItemStack par6ItemStack) {
        final int var6 = MathHelper.floor_double(e.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        int meta = 0;
        if (var6 == 0) {
            meta = 2;
        } else if (var6 == 1) {
            meta = 5;
        } else if (var6 == 2) {
            meta = 3;
        } else if (var6 == 3) {
            meta = 4;
        }
        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 2);
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z,
        final EntityPlayer entityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (world.isRemote) {
            return true;
        }
        final ItemStack stack = entityPlayer.getCurrentEquippedItem();
        final TileEntityAirCompressor mixer = (TileEntityAirCompressor) world.getTileEntity(x, y, z);
        if (mixer.isDoneCompressing()) {
            mixer.ejectTank();
            return true;
        }
        if (stack == null) {
            mixer.ejectTank();
            return true;
        }
        final ItemStack ingredientStack = stack.copy();
        ingredientStack.stackSize = 1;
        if (mixer.addTank(ingredientStack)) {
            entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        }
        return true;
    }

    public void breakBlock(final World world, final int x, final int y, final int z, final Block block,
        final int meta) {
        if (!world.isRemote) {
            final TileEntityAirCompressor te = (TileEntityAirCompressor) world.getTileEntity(x, y, z);
            te.ejectTank();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public int tickRate(final World world) {
        return 4;
    }

    public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
        if (!world.isRemote) {}
    }

    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return TileEntityFactory.getAirCompressorTE();
    }

    public void registerBlockIcons(final IIconRegister iconRegistry) {
        this.blockIcon = iconRegistry.registerIcon("tropicraft:chunk");
    }
}
