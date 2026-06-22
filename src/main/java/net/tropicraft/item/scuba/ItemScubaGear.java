package net.tropicraft.item.scuba;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.item.armor.ItemTropicraftArmor;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemScubaGear extends ItemTropicraftArmor {

    protected ScubaMaterial scubaMaterial;

    public ItemScubaGear(final ItemArmor.ArmorMaterial material, final ScubaMaterial scubaMaterial,
        final int renderIndex, final int armorType) {
        super(material, renderIndex, armorType);
        this.scubaMaterial = scubaMaterial;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "tropicraft:textures/models/armor/scubaGearPink.png";
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(final EntityLivingBase entityLiving, final ItemStack itemstack,
        final int armorSlot) {
        if (itemstack == null) {
            return null;
        }
        return null;
    }

    public NBTTagCompound getTagCompound(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    public abstract void onArmorTick(final World p0, final EntityPlayer p1, final ItemStack p2);

    public enum ScubaMaterial {

        DRY(0, "dry", "Dry"),
        WET(35, "wet", "Wet");

        private int maxDepth;
        private String imagePrefix;
        private String displayName;

        private ScubaMaterial(final int maxDepth, final String imagePrefix, final String displayName) {
            this.maxDepth = maxDepth;
            this.imagePrefix = imagePrefix;
            this.displayName = displayName;
        }

        public int getMaxDepth() {
            return this.maxDepth;
        }

        public String getImagePrefix() {
            return this.imagePrefix;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    public enum AirType {

        REGULAR(3200, 0.005f, "Regular"),
        TRIMIX(3200, 1.185f, "Trimix");

        private int maxCapacity;
        private float usageRate;
        private String displayName;

        private AirType(final int maxCapacity, final float usageRate, final String displayName) {
            this.maxCapacity = maxCapacity;
            this.usageRate = usageRate;
            this.displayName = displayName;
        }

        public int getMaxCapacity() {
            return this.maxCapacity;
        }

        public float getUsageRate() {
            return this.usageRate;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }
}
