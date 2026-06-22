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

public class ItemFlippers extends ItemArmor {

    private boolean hasFlippers;
    private String modArmorName;

    public ItemFlippers(final ItemArmor.ArmorMaterial material, final int renderIndex, final int armorType) {
        super(material, renderIndex, armorType);
        this.hasFlippers = false;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
        this.setMaxDamage(0);
        this.maxStackSize = 1;
        this.modArmorName = material.name();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int par1, final int par2) {
        return this.itemIcon;
    }

    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {
        final ItemStack item0 = player.inventory.armorInventory[0];
        if (item0 != null && item0.getItem() != null && item0.getItem() == TCItemRegistry.flippers) {
            if (player.isInsideOfMaterial(Material.water)) {
                player.capabilities.isFlying = true;
                player.setAIMoveSpeed(player.capabilities.getWalkSpeed());
                if (item0.isItemEnchanted()) {
                    player.moveFlying(1.0E-4f, 1.0E-4f, 1.0E-11f);
                    player.motionX /= 1.06999999;
                    player.motionZ /= 1.06999999;
                    player.moveEntityWithHeading(-1.0E-4f, -1.0E-4f);
                } else {
                    player.moveFlying(1.0E-4f, 1.0E-4f, 1.0E-11f);
                    player.motionX /= 1.26999999;
                    player.motionZ /= 1.26999999;
                    player.moveEntityWithHeading(-1.0E-4f, -1.0E-4f);
                }
                player.moveEntityWithHeading(-1.0E-4f, -1.0E-4f);
            } else {
                player.setAIMoveSpeed((float) (player.getAIMoveSpeed() / 1.33333));
                player.capabilities.isFlying = false;
            }
            this.hasFlippers = true;
        }
        if ((item0 == null || item0.getItem() != TCItemRegistry.flippers) && this.hasFlippers) {
            if (!player.capabilities.isCreativeMode && player.capabilities.isFlying) {
                player.capabilities.isFlying = false;
            }
            this.hasFlippers = false;
        }
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
