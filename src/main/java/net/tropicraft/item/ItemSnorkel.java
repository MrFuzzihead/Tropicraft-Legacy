package net.tropicraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSnorkel extends ItemArmor {

    private boolean outOfWater;
    private boolean outOfWaterLast;
    private String modArmorName;

    public ItemSnorkel(final ItemArmor.ArmorMaterial material, final int renderIndex, final int armorType) {
        super(material, renderIndex, armorType);
        this.outOfWater = true;
        this.outOfWaterLast = true;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
        this.setMaxDamage(0);
        this.maxStackSize = 1;
        this.modArmorName = material.name();
    }

    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {
        final ItemStack item3 = player.inventory.armorInventory[3];
        if (item3 != null && item3.getItem() != null && item3.getItem() == TCItemRegistry.snorkel) {
            this.outOfWater = (!player.isInsideOfMaterial(Material.water) || !player.isInWater());
            if (this.outOfWaterLast && this.outOfWater) {
                player.setAir(300);
            }
            if (this.outOfWaterLast && !this.outOfWater) {
                player.setAir(1200);
                this.outOfWaterLast = false;
            }
            if (this.outOfWater) {
                this.outOfWaterLast = true;
            }
        } else if (player.getAir() > 300 && (item3 == null || item3.getItem() != TCItemRegistry.snorkel)) {
            player.setAir(300);
        } else {
            this.outOfWater = true;
            this.outOfWaterLast = true;
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int par1, final int par2) {
        return this.itemIcon;
    }

    public String getUnlocalizedName() {
        return String.format("item.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    public String getUnlocalizedName(final ItemStack itemStack) {
        return String.format("item.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
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
}
