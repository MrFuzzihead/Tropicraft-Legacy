package net.tropicraft.registry;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.tropicraft.fluid.FluidTropicsPortal;
import net.tropicraft.fluid.FluidTropicsWater;

public class TCFluidRegistry {

    public static final Fluid tropicsWater;
    public static final Fluid tropicsPortal;

    public static void init() {
        registerFluid(TCFluidRegistry.tropicsWater);
        registerFluid(TCFluidRegistry.tropicsPortal);
    }

    public static void postInit() {
        TCFluidRegistry.tropicsWater.setBlock((Block) TCBlockRegistry.tropicsWater)
            .setUnlocalizedName(TCBlockRegistry.tropicsWater.getUnlocalizedName());
        TCFluidRegistry.tropicsPortal.setBlock((Block) TCBlockRegistry.tropicsPortal)
            .setUnlocalizedName(TCBlockRegistry.tropicsPortal.getUnlocalizedName());
        FluidRegistry.registerFluid(TCFluidRegistry.tropicsWater);
        FluidContainerRegistry.registerFluidContainer(
            TCFluidRegistry.tropicsWater,
            new ItemStack((Item) TCItemRegistry.bucketTropicsWater),
            new ItemStack(Items.bucket));
    }

    private static void registerFluid(final Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
    }

    static {
        tropicsWater = (Fluid) new FluidTropicsWater("tropicsWater");
        tropicsPortal = (Fluid) new FluidTropicsPortal("tropicsPortal");
    }
}
