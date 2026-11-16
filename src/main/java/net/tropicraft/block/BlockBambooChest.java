package net.tropicraft.block;

import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooChest extends BlockChest {

    public BlockBambooChest() {
        super(0);
        this.setStepSound(BlockBambooChest.soundTypeWood);
        this.disableStats();
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }

    public int getRenderType() {
        return TCRenderIDs.bambooChest;
    }

    public TileEntity createNewTileEntity(final World world, final int i) {
        return new TileEntityBambooChest();
    }

    public IIcon getIcon(final int i, final int j) {
        return this.blockIcon;
    }

    public float getPlayerRelativeBlockHardness(final EntityPlayer player, final World world, final int i, final int j,
        final int k) {
        final TileEntityBambooChest tile = (TileEntityBambooChest) world.getTileEntity(i, j, k);
        if (tile != null && tile.isUnbreakable()) {
            return 0.0f;
        }
        return super.getPlayerRelativeBlockHardness(player, world, i, j, k);
    }

    public String getUnlocalizedName() {
        return TropicraftNames.block(super.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister
            .registerIcon(String.format("%s", TropicraftNames.afterFirstDot(this.getUnlocalizedName())));
    }
}
