package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBundle extends BlockTropicraft {

    private IIcon end;
    private IIcon side;

    public BlockBundle(final String name) {
        super(Material.plants);
        this.setBlockTextureName(name);
        this.setBlockName(name);
    }

    public int onBlockPlaced(final World world, final int x, final int y, final int z, final int side, final float hitX,
        final float hitY, final float hitZ, final int metadata) {
        final int j1 = metadata & 0x1;
        byte b0 = 0;
        switch (side) {
            case 0:
            case 1: {
                b0 = 0;
                break;
            }
            case 2:
            case 3: {
                b0 = 8;
                break;
            }
            case 4:
            case 5: {
                b0 = 4;
                break;
            }
        }
        return j1 | b0;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int b_side, final int meta) {
        return (b_side == 0 || b_side == 1) ? this.end : this.side;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.side = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + "Side");
        this.end = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "_"
                + "End");
        this.blockIcon = this.side;
    }
}
