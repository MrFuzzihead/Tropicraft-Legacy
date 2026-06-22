package net.tropicraft.item.scuba;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScubaTank extends ItemTropicraft {

    public ItemScubaTank() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.maxStackSize = 1;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    public void onCreated(final ItemStack par1ItemStack, final World par2World, final EntityPlayer par3EntityPlayer) {}

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list,
        final boolean par4) {
        final ItemScubaGear.AirType airType = (itemstack.getItemDamage() == 1) ? ItemScubaGear.AirType.TRIMIX
            : ItemScubaGear.AirType.REGULAR;
        final String airRemaining = this.getTagCompound(itemstack)
            .getInteger("AirContained") + " psi";
        list.add(
            ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airType")
                + ": "
                + ColorHelper.color(7)
                + airType.getDisplayName());
        list.add(
            ColorHelper.color(9) + StatCollector.translateToLocal(
                "gui.tropicraft:maxAirCapacity") + ": " + ColorHelper.color(7) + airType.getMaxCapacity() + " psi");
        list.add(
            ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airRemaining")
                + ": "
                + ColorHelper.color(7)
                + airRemaining);
        list.add(
            ColorHelper.color(9) + String.format(
                "%s: %s%.3f psi/sec",
                StatCollector.translateToLocal("gui.tropicraft:useEfficiency"),
                ColorHelper.color(7),
                airType.getUsageRate() * 20.0f));
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        final ItemStack singleTankRegular = new ItemStack(item, 1, 0);
        this.getTagCompound(singleTankRegular)
            .setInteger("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity());
        list.add(singleTankRegular);
        final ItemStack singleTankTrimix = new ItemStack(item, 1, 1);
        this.getTagCompound(singleTankTrimix)
            .setInteger("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity());
        list.add(singleTankTrimix);
        final ItemStack singleTankTrimix2 = new ItemStack(item, 1, 2);
        this.getTagCompound(singleTankTrimix2)
            .setInteger("AirContained", 0);
        list.add(singleTankTrimix2);
    }

    public NBTTagCompound getTagCompound(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }
}
