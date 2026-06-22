package net.tropicraft.item.scuba;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.tropicraft.util.ColorHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScubaChestplateGear extends ItemScubaGear {

    public ItemScubaChestplateGear(final ItemArmor.ArmorMaterial material, final ScubaMaterial scubaMaterial,
        final int renderIndex, final int armorType) {
        super(material, scubaMaterial, renderIndex, armorType);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list,
        final boolean par4) {
        final AirType airType = (itemstack.getItemDamage() >= 2) ? AirType.TRIMIX : AirType.REGULAR;
        final String airRemaining = this.getTagCompound(itemstack)
            .getFloat("AirContained") + " psi";
        final String numTanks = String.valueOf((itemstack.getItemDamage() % 2 != 0) ? 2 : 1);
        final String suitType = this.scubaMaterial.getDisplayName();
        list.add(
            ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:suitType")
                + ": "
                + ColorHelper.color(7)
                + suitType);
        list.add(
            ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airType")
                + ": "
                + ColorHelper.color(7)
                + airType.getDisplayName());
        list.add(
            ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:numTanks")
                + ": "
                + ColorHelper.color(7)
                + numTanks);
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

    public AirType getAirType(final ItemStack itemstack) {
        return (itemstack.getItemDamage() >= 2) ? AirType.TRIMIX : AirType.REGULAR;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        final ItemStack singleTankRegular = new ItemStack(item, 1, 0);
        this.getTagCompound(singleTankRegular)
            .setFloat("AirContained", (float) AirType.REGULAR.getMaxCapacity());
        list.add(singleTankRegular);
        final ItemStack doubleTankRegular = new ItemStack(item, 1, 1);
        this.getTagCompound(doubleTankRegular)
            .setFloat("AirContained", (float) (AirType.REGULAR.getMaxCapacity() * 2));
        list.add(doubleTankRegular);
        final ItemStack singleTankTrimix = new ItemStack(item, 1, 2);
        this.getTagCompound(singleTankTrimix)
            .setFloat("AirContained", (float) AirType.TRIMIX.getMaxCapacity());
        list.add(singleTankTrimix);
        final ItemStack doubleTankTrimix = new ItemStack(item, 1, 3);
        this.getTagCompound(doubleTankTrimix)
            .setFloat("AirContained", (float) (AirType.TRIMIX.getMaxCapacity() * 2));
        list.add(doubleTankTrimix);
    }

    public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armor,
        final DamageSource source, final double damage, final int slot) {
        return null;
    }

    public int getArmorDisplay(final EntityPlayer player, final ItemStack armor, final int slot) {
        return 0;
    }

    public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source,
        final int damage, final int slot) {}

    @Override
    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemstack) {}
}
