package net.tropicraft.item;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockTropicraft extends ItemBlock {

    protected String[] names;
    protected Block block;

    public ItemBlockTropicraft(final Block block, final ArrayList<String> names) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.block = block;
        this.names = names.toArray(new String[names.size()]);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getMetadata(final int id) {
        return id;
    }

    public String getUnlocalizedName(final ItemStack itemstack) {
        if (this.names == null) {
            return super.getUnlocalizedName(itemstack);
        }
        final int i = MathHelper.clamp_int(itemstack.getItemDamage(), 0, this.names.length - 1);
        return super.getUnlocalizedName() + "_" + this.names[i];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int meta) {
        return this.block.getIcon(0, meta);
    }
}
