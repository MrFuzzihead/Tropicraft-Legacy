package net.tropicraft.item.scuba;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

public class ItemScubaLeggings extends ItemScubaGear {

    public ItemScubaLeggings(final ItemArmor.ArmorMaterial material, final ItemScubaGear.ScubaMaterial scubaMaterial,
        final int renderIndex, final int armorType) {
        super(material, scubaMaterial, renderIndex, armorType);
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

    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {}
}
