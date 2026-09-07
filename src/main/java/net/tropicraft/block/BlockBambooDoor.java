package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooDoor extends BlockDoor {

    @SideOnly(Side.CLIENT)
    private IIcon[] images;

    public BlockBambooDoor() {
        super(Material.plants);
        this.setBlockTextureName("bambooDoor");
        this.setStepSound(BlockBambooDoor.soundTypeWood);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return this.images[0];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4,
        final int par5) {
        if (par5 != 1 && par5 != 0) {
            final int i1 = this.func_150012_g(par1IBlockAccess, par2, par3, par4);
            final int j1 = i1 & 0x3;
            final boolean flag = (i1 & 0x4) != 0x0;
            boolean flag2 = false;
            final boolean flag3 = (i1 & 0x8) != 0x0;
            if (flag) {
                if (j1 == 0 && par5 == 2) {
                    flag2 = !flag2;
                } else if (j1 == 1 && par5 == 5) {
                    flag2 = !flag2;
                } else if (j1 == 2 && par5 == 3) {
                    flag2 = !flag2;
                } else if (j1 == 3 && par5 == 4) {
                    flag2 = !flag2;
                }
            } else {
                if (j1 == 0 && par5 == 5) {
                    flag2 = !flag2;
                } else if (j1 == 1 && par5 == 3) {
                    flag2 = !flag2;
                } else if (j1 == 2 && par5 == 4) {
                    flag2 = !flag2;
                } else if (j1 == 3 && par5 == 2) {
                    flag2 = !flag2;
                }
                if ((i1 & 0x10) != 0x0) {
                    flag2 = !flag2;
                }
            }
            return this.images[0 + (flag2 ? 2 : 0) + (flag3 ? 1 : 0)];
        }
        return this.images[0];
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World world, final int x, final int y, final int z) {
        return TCItemRegistry.bambooDoor;
    }

    public Item getItemDropped(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return ((p_149650_1_ & 0x8) != 0x0) ? null : TCItemRegistry.bambooDoor;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        (this.images = new IIcon[4])[0] = register.registerIcon("tropicraft:" + this.getTextureName() + "_Bottom");
        this.images[1] = register.registerIcon("tropicraft:" + this.getTextureName() + "_Top");
        this.images[2] = (IIcon) new IconFlipped(this.images[0], true, false);
        this.images[3] = (IIcon) new IconFlipped(this.images[1], true, false);
    }
}
