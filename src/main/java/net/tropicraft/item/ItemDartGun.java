package net.tropicraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.TropicraftUtils;

public class ItemDartGun extends ItemTropicraft {

    public ItemDartGun() {
        this.maxStackSize = 1;
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    public void getSubItems(final Item item, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(item, 1, 0));
    }

    public int getMaxItemUseDuration(final ItemStack itemstack) {
        return 25000;
    }

    public EnumAction getItemUseAction(final ItemStack itemstack) {
        return EnumAction.bow;
    }

    public void onPlayerStoppedUsing(final ItemStack itemstack, final World world, final EntityPlayer entityplayer,
        final int i) {
        final int j = this.getMaxItemUseDuration(itemstack) - i;
        float f = j / 20.0f;
        f = (f * f + f * 2.0f) / 3.0f;
        if (f < 0.1) {
            return;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        itemstack.damageItem(1, (EntityLivingBase) entityplayer);
        world.playSoundAtEntity(
            (Entity) entityplayer,
            "dartblow",
            1.0f,
            1.0f / (ItemDartGun.itemRand.nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        int slot = 0;
        slot = TropicraftUtils.getSlotOfItem(entityplayer.inventory, (Item) TCItemRegistry.dart);
        if (!world.isRemote && (slot > -1 || entityplayer.capabilities.isCreativeMode)) {
            ItemStack dartStack;
            if (slot > -1) {
                dartStack = entityplayer.inventory.mainInventory[slot];
            } else {
                dartStack = new ItemStack((Item) TCItemRegistry.dart, 1, world.rand.nextInt(ItemDart.dartNames.length));
            }
            if (!entityplayer.capabilities.isCreativeMode) {
                if (dartStack.stackSize > 1) {
                    final ItemStack itemStack = dartStack;
                    --itemStack.stackSize;
                } else {
                    entityplayer.inventory.mainInventory[slot] = null;
                    entityplayer.inventoryContainer.detectAndSendChanges();
                }
            }
            if (itemstack.getItemDamage() == 0) {
                final EntityDart entitydart = new EntityDart(
                    world,
                    (Entity) entityplayer,
                    f * 2.0f,
                    (short) dartStack.getItemDamage());
                world.spawnEntityInWorld((Entity) entitydart);
            } else {
                final EntityDart entitydart = new EntityDart(
                    world,
                    (Entity) entityplayer,
                    f * 2.0f,
                    (short) (itemstack.getItemDamage() - 1));
                world.spawnEntityInWorld((Entity) entitydart);
            }
        }
    }

    public ItemStack onEaten(final ItemStack par1ItemStack, final World par2World,
        final EntityPlayer par3EntityPlayer) {
        return par1ItemStack;
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }
}
