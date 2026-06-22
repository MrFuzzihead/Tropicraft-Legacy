package net.tropicraft.block;

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
import net.tropicraft.block.tileentity.TileEntityEIHMixer;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEIHMixer extends BlockTropicraft implements ITileEntityProvider {

    public BlockEIHMixer() {
        super(Material.rock);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.8f, 1.0f);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public int getRenderType() {
        return TCRenderIDs.eihMixer;
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

    public boolean onBlockActivated(final World world, final int x, final int y, final int z,
        final EntityPlayer entityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (world.isRemote) {
            return true;
        }
        final ItemStack stack = entityPlayer.getCurrentEquippedItem();
        final TileEntityEIHMixer mixer = (TileEntityEIHMixer) world.getTileEntity(x, y, z);
        if (mixer.isDoneMixing()) {
            mixer.retrieveResult();
            return true;
        }
        if (stack == null) {
            mixer.emptyMixer();
            return true;
        }
        final ItemStack ingredientStack = stack.copy();
        ingredientStack.stackSize = 1;
        if (mixer.addToMixer(ingredientStack)) {
            entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        }
        if (stack.getItem() == TCItemRegistry.bambooMug && mixer.canMix()) {
            mixer.startMixing();
            entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        }
        return true;
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

    public void breakBlock(final World world, final int x, final int y, final int z, final Block par5, final int par6) {
        if (!world.isRemote) {
            final TileEntityEIHMixer te = (TileEntityEIHMixer) world.getTileEntity(x, y, z);
            if (te.isDoneMixing()) {
                te.retrieveResult();
            } else {
                te.emptyMixer();
            }
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("tropicraft:chunk");
    }

    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
        return new TileEntityEIHMixer();
    }
}
