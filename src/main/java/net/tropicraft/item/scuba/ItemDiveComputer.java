package net.tropicraft.item.scuba;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemDiveComputer extends ItemMap {

    public static final int UPDATE_RATE = 20;
    public int ticksUntilUpdate;

    public ItemDiveComputer() {
        this.ticksUntilUpdate = 20;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    public void onUpdate(final ItemStack itemstack, final World world, final Entity entity, final int par4,
        final boolean par5) {
        if (world.isRemote) {
            return;
        }
        if (this.ticksUntilUpdate <= 0) {
            if (!(entity instanceof EntityPlayer)) {
                return;
            }
            final EntityPlayer player = (EntityPlayer) entity;
            if (!this.isFullyUnderwater(world, player)) {
                return;
            }
            final ItemStack helmetStack = player.getEquipmentInSlot(4);
            final ItemStack chestplateStack = player.getEquipmentInSlot(3);
            final ItemStack leggingsStack = player.getEquipmentInSlot(2);
            final ItemStack flippersStack = player.getEquipmentInSlot(1);
            if (!this.armorCheck(world, player, helmetStack, chestplateStack, leggingsStack, flippersStack)) {
                return;
            }
            player.setAir(300);
            final float air = this.getTagCompound(chestplateStack)
                .getFloat("AirContained");
            final ItemScubaGear.AirType airType = (chestplateStack.getItemDamage() >= 2) ? ItemScubaGear.AirType.TRIMIX
                : ItemScubaGear.AirType.REGULAR;
            chestplateStack.getTagCompound()
                .setFloat("AirContained", air - airType.getUsageRate());
            final int currentDepth = MathHelper.floor_double(player.posY);
            if (currentDepth < chestplateStack.getTagCompound()
                .getInteger("MaxDepth")
                || chestplateStack.getTagCompound()
                    .getInteger("MaxDepth") == 0) {
                chestplateStack.getTagCompound()
                    .setInteger("MaxDepth", currentDepth);
            }
            int waterBlocksAbove = 0;
            int waterBlocksBelow = 0;
            int x;
            int y;
            int z;
            for (x = MathHelper.floor_double(player.posX), y = MathHelper
                .floor_double(player.posY + player.height - 0.5), z = MathHelper.floor_double(player.posZ); world
                    .getBlock(x, y + waterBlocksAbove + 1, z)
                    .getMaterial()
                    .isLiquid(); ++waterBlocksAbove) {}
            while (world.getBlock(x, y - waterBlocksBelow - 1, z)
                .getMaterial()
                .isLiquid()) {
                ++waterBlocksBelow;
            }
            chestplateStack.getTagCompound()
                .setInteger("WaterBlocksAbove", waterBlocksAbove);
            chestplateStack.getTagCompound()
                .setInteger("WaterBlocksBelow", waterBlocksBelow);
            this.ticksUntilUpdate = 20;
        } else {
            --this.ticksUntilUpdate;
        }
    }

    public NBTTagCompound getTagCompound(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    private boolean isFullyUnderwater(final World world, final EntityPlayer player) {
        final int x = MathHelper.ceiling_double_int(player.posX);
        final int y = MathHelper.ceiling_double_int(player.posY + player.height - 0.5);
        final int z = MathHelper.ceiling_double_int(player.posZ);
        return player.isInsideOfMaterial(Material.water);
    }

    private boolean armorCheck(final World world, final EntityPlayer player, final ItemStack helmetStack,
        final ItemStack chestplateStack, final ItemStack leggingsStack, final ItemStack flippersStack) {
        if (helmetStack == null || chestplateStack == null || leggingsStack == null || flippersStack == null) {
            return false;
        }
        if (!(helmetStack.getItem() instanceof ItemScubaHelmet)) {
            return false;
        }
        if (!(leggingsStack.getItem() instanceof ItemScubaLeggings)) {
            return false;
        }
        if (!(flippersStack.getItem() instanceof ItemScubaFlippers)) {
            return false;
        }
        if (!(chestplateStack.getItem() instanceof ItemScubaChestplateGear)) {
            return false;
        }
        final ItemScubaHelmet helmet = (ItemScubaHelmet) helmetStack.getItem();
        final ItemScubaLeggings leggings = (ItemScubaLeggings) leggingsStack.getItem();
        final ItemScubaFlippers flippers = (ItemScubaFlippers) flippersStack.getItem();
        final ItemScubaChestplateGear chestplate = (ItemScubaChestplateGear) chestplateStack.getItem();
        return helmet.scubaMaterial == leggings.scubaMaterial && leggings.scubaMaterial == flippers.scubaMaterial
            && flippers.scubaMaterial == chestplate.scubaMaterial;
    }
}
