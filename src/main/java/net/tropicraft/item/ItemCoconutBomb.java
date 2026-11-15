package net.tropicraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.config.ConfigMisc;
import net.tropicraft.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemCoconutBomb extends ItemTropicraft {

    public ItemCoconutBomb() {
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer player) {
        // The whitelist used to run *after* --itemstack.stackSize, so a player who was not allowed to throw a bomb
        // still lost one, on the client as well as the server. Check first, and only let the server spend it.
        if (!isAllowed(player)) {
            if (!world.isRemote) {
                player.addChatMessage(
                    new ChatComponentText(StatCollector.translateToLocal("tropicraft.coconutBombWarning")));
            }
            return itemstack;
        }
        world.playSoundAtEntity(
            (Entity) player,
            "random.bow",
            0.5f,
            0.4f / (ItemCoconutBomb.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!world.isRemote) {
            world.spawnEntityInWorld((Entity) new EntityCoconutGrenade(world, player));
            --itemstack.stackSize;
        }
        return itemstack;
    }

    private static boolean isAllowed(final EntityPlayer player) {
        if (!ConfigMisc.coconutBombWhitelistEnabled) {
            return true;
        }
        return ConfigMisc.coconutBombWhitelistedUsers.contains(
            player.getGameProfile()
                .getName());
    }
}
