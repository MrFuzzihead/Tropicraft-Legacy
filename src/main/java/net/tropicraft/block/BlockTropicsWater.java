package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicsWater extends BlockFluidClassic {

    @SideOnly(Side.CLIENT)
    public static IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    public static IIcon flowingIcon;

    public BlockTropicsWater(final Fluid fluid, final Material material) {
        super(fluid, material);
        this.lightOpacity = 0;
        this.setCreativeTab(null);
        this.displacements.put(TCBlockRegistry.coral, false);
        this.displacements.put(TCBlockRegistry.bambooFence, false);
        this.setRenderPass(1);
    }

    public void registerBlockIcons(final IIconRegister iconRegister) {
        BlockTropicsWater.stillIcon = iconRegister.registerIcon("tropicraft:tropicsWater");
        BlockTropicsWater.flowingIcon = iconRegister.registerIcon("tropicraft:flowingTropicsWater");
        TCFluidRegistry.tropicsWater.setIcons(BlockTropicsWater.stillIcon, BlockTropicsWater.flowingIcon);
    }

    public IIcon getIcon(final int side, final int meta) {
        return (side != 0 && side != 1) ? BlockTropicsWater.flowingIcon : BlockTropicsWater.stillIcon;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z,
        final int side) {
        final Material material = world.getBlock(x, y, z)
            .getMaterial();
        return material != this.blockMaterial && (side == 1 || super.shouldSideBeRendered(world, x, y, z, side));
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.getBlockMetadata(x, y, z) > 0 && world.getBlock(x, y - 1, z)
            .getMaterial() != Material.air) {
            int neighbourSources = 0;
            neighbourSources += IsNeighbourSource(world, x + 1, y, z) ? 1 : 0;
            neighbourSources += IsNeighbourSource(world, x - 1, y, z) ? 1 : 0;
            neighbourSources += IsNeighbourSource(world, x, y, z + 1) ? 1 : 0;
            neighbourSources += IsNeighbourSource(world, x, y, z - 1) ? 1 : 0;

            if (neighbourSources >= 2) {
                world.setBlock(x, y, z, this, 0, 3);
            }
        }

        super.updateTick(world, x, y, z, rand);
    }

    private boolean IsNeighbourSource(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == 0;
    }
}
