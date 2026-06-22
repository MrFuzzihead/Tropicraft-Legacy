package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftOre extends ItemTropicraftMulti {

    @SideOnly(Side.CLIENT)
    private IIcon[] unrefinedIcons;

    public ItemTropicraftOre(final String[] names) {
        super(names);
        this.setCreativeTab(TCCreativeTabRegistry.tabMaterials);
    }

    public String getUnlocalizedName(final ItemStack stack) {
        final int i = MathHelper.clamp_int(stack.getItemDamage(), 0, this.names.length - 1);
        return super.getUnlocalizedName() + "_" + this.names[i];
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list,
        final boolean par4) {
        final int damage = itemstack.getItemDamage();
        if (damage == 5 || damage == 6) {
            final float refinedPercentage = this.getTagCompound(itemstack)
                .getFloat("AmtRefined");
            list.add(
                ColorHelper.color(3) + StatCollector.translateToLocal("gui.tropicraft:amtRefined")
                    + ": "
                    + refinedPercentage
                    + "%");
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < 5; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
        final ItemStack u1 = new ItemStack(item, 1, 5);
        this.getTagCompound(u1)
            .setFloat("AmtRefined", 0.0f);
        final ItemStack u2 = new ItemStack(item, 1, 5);
        this.getTagCompound(u2)
            .setFloat("AmtRefined", 33.333f);
        final ItemStack u3 = new ItemStack(item, 1, 5);
        this.getTagCompound(u3)
            .setFloat("AmtRefined", 66.667f);
        final ItemStack refined = new ItemStack(item, 1, 6);
        this.getTagCompound(refined)
            .setFloat("AmtRefined", 100.0f);
        list.add(u1);
        list.add(u2);
        list.add(u3);
        list.add(refined);
        list.add(new ItemStack(item, 1, 7));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(final ItemStack stack) {
        final int damage = stack.getItemDamage();
        final float refinedAmt = this.getTagCompound(stack)
            .getFloat("AmtRefined");
        if (refinedAmt > 32.0f && refinedAmt < 34.0f) {
            return this.unrefinedIcons[0];
        }
        if (refinedAmt > 65.0f && refinedAmt < 67.0f) {
            return this.unrefinedIcons[1];
        }
        return this.getIconFromDamage(damage);
    }

    private int getUnrefinedIndex(final ItemStack stack) {
        final float refinedAmt = this.getTagCompound(stack)
            .getFloat("AmtRefined");
        if (refinedAmt > 32.0f && refinedAmt < 34.0f) {
            return 1;
        }
        if (refinedAmt > 65.0f && refinedAmt < 67.0f) {
            return 2;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.icons = new IIcon[this.names.length];
        for (int i = 0; i < this.names.length; ++i) {
            this.icons[i] = iconRegister.registerIcon(
                this.getUnlocalizedName()
                    .substring(
                        this.getUnlocalizedName()
                            .indexOf(".") + 1)
                    + "_"
                    + this.names[i]);
        }
        this.unrefinedIcons = new IIcon[2];
        for (int i = 0; i < 2; ++i) {
            this.unrefinedIcons[i] = iconRegister.registerIcon(
                this.getUnlocalizedName()
                    .substring(
                        this.getUnlocalizedName()
                            .indexOf(".") + 1)
                    + "_"
                    + this.names[5]
                    + ""
                    + (i + 1));
        }
    }
}
