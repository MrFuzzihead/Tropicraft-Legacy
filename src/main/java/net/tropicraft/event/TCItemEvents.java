package net.tropicraft.event;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.tropicraft.item.tool.IUnderwaterTool;
import net.tropicraft.item.tool.ItemUnderwaterShovel;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCItemEvents {

    @SubscribeEvent
    public void handleBucketFillEvent(final FillBucketEvent event) {
        final ItemStack iHazBucket = new ItemStack((Item) TCItemRegistry.bucketTropicsWater);
        final World world = event.world;
        final int x = event.target.blockX;
        final int y = event.target.blockY;
        final int z = event.target.blockZ;
        final int meta = world.getBlockMetadata(x, y, z);
        final Fluid fluid = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));
        if (fluid != null && fluid == TCFluidRegistry.tropicsWater && meta == 0) {
            TCItemRegistry.bucketTropicsWater.fill(iHazBucket, new FluidStack(fluid, 1000), true);
            world.setBlockToAir(x, y, z);
            event.result = iHazBucket;
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void changeTropicsWaterFogDensity(final EntityViewRenderEvent.FogDensity event) {
        final int x = MathHelper.floor_double(event.entity.posX);
        final int y = MathHelper.ceiling_double_int(event.entity.posY + event.entity.height - 0.5);
        final int z = MathHelper.floor_double(event.entity.posZ);
        if (event.block.getMaterial()
            .isLiquid()
            && event.block.getUnlocalizedName()
                .equals(TCBlockRegistry.tropicsWater.getUnlocalizedName())) {
            event.density = 0.0115f;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void changeTropicsWaterFogColor(final EntityViewRenderEvent.FogColors event) {
        final int x = MathHelper.floor_double(event.entity.posX);
        final int y = MathHelper.ceiling_double_int(event.entity.posY + event.entity.height - 0.5);
        final int z = MathHelper.floor_double(event.entity.posZ);
        if (event.block.getMaterial()
            .isLiquid()
            && event.block.getUnlocalizedName()
                .equals(TCBlockRegistry.tropicsWater.getUnlocalizedName())) {
            event.red = 0.2f;
            event.green = 0.8f;
            event.blue = 0.5f;
        }
    }

    @SubscribeEvent
    public void handleUnderwaterTools(final PlayerEvent.BreakSpeed event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack itemstack = player.getHeldItem();
        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem() instanceof IUnderwaterTool) {
            if (this.isFullyUnderwater(player.worldObj, player)) {
                event.newSpeed = event.originalSpeed * (player.onGround ? 5.0f : 10.0f);
            } else {
                event.newSpeed = event.originalSpeed / 14.0f;
                if (itemstack.getItem() instanceof ItemUnderwaterShovel) {
                    event.newSpeed /= 5.0f;
                }
            }
        }
    }

    private boolean isFullyUnderwater(final World world, final EntityPlayer player) {
        return player.isInsideOfMaterial(Material.water);
    }
}
