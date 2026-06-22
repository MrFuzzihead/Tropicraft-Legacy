package net.tropicraft.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.factory.TileEntityFactory;

public class BlockFirePit extends BlockTropicraft implements ITileEntityProvider {

    public BlockFirePit() {
        super(Material.circuits);
        this.setBlockTextureName("firePit");
        this.setBlockBoundsForItemRender();
        this.lightValue = 15;
    }

    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.05f, 0.0f, 0.05f, 0.95f, 0.1f, 0.95f);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return TileEntityFactory.getFirePitTE();
    }
}
