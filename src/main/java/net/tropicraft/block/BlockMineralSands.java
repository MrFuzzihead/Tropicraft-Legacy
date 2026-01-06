package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.info.*;
import net.tropicraft.registry.*;
import net.tropicraft.util.*;

import cpw.mods.fml.relauncher.*;

public class BlockMineralSands extends BlockFalling {

    public BlockMineralSands() {
        super(Material.sand);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
        this.setHardness(0.5f);
    }

    public int damageDropped(final int p_149692_1_) {
        return p_149692_1_;
    }

    public Item getItemDropped(final int meta, final Random rand, final int unused) {
        return Item.getItemFromBlock((Block) TCBlockRegistry.mineralSands);
    }

    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity) {
        final int metadata = world.getBlockMetadata(x, y, z);
        if (metadata != 2) {
            return;
        }
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            final ItemStack stack = player.getEquipmentInSlot(1);
            if (stack == null) {
                player.attackEntityFrom(DamageSource.lava, 0.5f);
            }
        } else {
            entity.attackEntityFrom(DamageSource.lava, 0.5f);
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < TCNames.mineralSandNames.length; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(final int meta) {
        final int color = CoralColors.getColor(meta);
        return (meta == 2) ? color : (color | ColorizerGrass.getGrassColor(0.1, 1.0));
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess world, final int x, final int y, final int z) {
        final int metadata = world.getBlockMetadata(x, y, z);
        return this.getRenderColor(metadata);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(String.format("%s", this.getActualName(this.getUnlocalizedName())));
    }
}
