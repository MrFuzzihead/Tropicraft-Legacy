package net.tropicraft.item.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemScaleArmor extends ItemTropicraftArmor {

    public ItemScaleArmor(final ItemArmor.ArmorMaterial material, final int renderIndex, final int armorType) {
        super(material, renderIndex, armorType);
    }

    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {}
}
