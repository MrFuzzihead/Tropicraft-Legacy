package net.tropicraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemTCItemFrame extends ItemHangingEntity {

    private final Class hangingEntityClass;
    private boolean shouldDropContents;

    public ItemTCItemFrame(final Class clazz, final boolean shouldDropContents) {
        super(clazz);
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
        this.hangingEntityClass = clazz;
        this.shouldDropContents = shouldDropContents;
    }

    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World,
        final int par4, final int par5, final int par6, final int par7, final float par8, final float par9,
        final float par10) {
        if (par7 == 0) {
            return false;
        }
        if (par7 == 1) {
            return false;
        }
        final int i1 = Direction.facingToDirection[par7];
        final EntityTCItemFrame entityhanging = this.createHangingEntity(par3World, par4, par5, par6, i1);
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        }
        if (entityhanging != null && entityhanging.onValidSurface()) {
            if (!par3World.isRemote) {
                par3World.spawnEntityInWorld((Entity) entityhanging);
            }
            --par1ItemStack.stackSize;
        }
        return true;
    }

    private EntityTCItemFrame createHangingEntity(final World par1World, final int par2, final int par3, final int par4,
        final int par5) {
        return new EntityTCItemFrame(par1World, par2, par3, par4, par5, this.shouldDropContents);
    }

    public void registerIcons(final IIconRegister iconRegistry) {
        this.itemIcon = iconRegistry.registerIcon("tropicraft:itemframe");
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
}
