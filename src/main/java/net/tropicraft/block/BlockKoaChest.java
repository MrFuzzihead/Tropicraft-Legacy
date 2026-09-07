package net.tropicraft.block;

import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKoaChest extends BlockChest {

    public BlockKoaChest() {
        super(0);
        this.setStepSound(BlockKoaChest.soundTypeWood);
        this.disableStats();
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public int getRenderType() {
        return -1;
    }

    public TileEntity createNewTileEntity(final World world, final int meta) {
        return (TileEntity) new TileEntityKoaChest();
    }

    public void onBlockAdded(final World world, final int i, final int j, final int k) {
        super.onBlockAdded(world, i, j, k);
    }

    public IIcon getIcon(final int i, final int j) {
        return this.blockIcon;
    }

    public float getPlayerRelativeBlockHardness(final EntityPlayer player, final World world, final int i, final int j,
        final int k) {
        final TileEntityKoaChest tile = (TileEntityKoaChest) world.getTileEntity(i, j, k);
        if (tile != null && tile.isUnbreakable()) {
            return 0.0f;
        }
        return super.getPlayerRelativeBlockHardness(player, world, i, j, k);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("tropicraft:bamboochest");
    }
}
