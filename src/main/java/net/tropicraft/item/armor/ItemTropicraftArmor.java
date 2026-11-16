package net.tropicraft.item.armor;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.tropicraft.util.TropicraftNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftArmor extends ItemArmor implements ISpecialArmor {

    private String modArmorName;
    @SideOnly(Side.CLIENT)
    public static List[] fxLayers;

    public ItemTropicraftArmor(final ItemArmor.ArmorMaterial material, final int renderIndex, final int armorType) {
        super(material, renderIndex, armorType);
        this.modArmorName = material.name();
    }

    public String getUnlocalizedName() {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    public String getUnlocalizedName(final ItemStack itemStack) {
        return TropicraftNames.item(super.getUnlocalizedName());
    }

    protected String getTexturePath(final String name) {
        return "tropicraft:textures/models/armor/" + name;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1));
    }

    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return this.getTexturePath(String.format("%s_layer_" + ((slot == 2) ? 2 : 1) + ".png", this.modArmorName));
    }

    public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armor,
        final DamageSource source, final double damage, final int slot) {
        return new ISpecialArmor.ArmorProperties(10, (source == DamageSource.inFire) ? 1.0 : 0.3, Integer.MAX_VALUE);
    }

    public int getArmorDisplay(final EntityPlayer player, final ItemStack armor, final int slot) {
        return 3;
    }

    public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source,
        final int damage, final int slot) {
        stack.damageItem(damage, entity);
    }
}
