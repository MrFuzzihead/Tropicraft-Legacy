package net.tropicraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.Tropicraft;
import net.tropicraft.client.gui.GuiTropicalBook;
import net.tropicraft.encyclopedia.TropicalBook;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropBook extends ItemTropicraft {

    private String bookName;

    public ItemTropBook(final TropicalBook book, final String name) {
        this.bookName = name;
        this.maxStackSize = 1;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    public ItemTropBook(final String name) {
        this(null, name);
    }

    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer entityplayer) {
        if (world.isRemote && this.getTropBook() != null) {
            System.err.println("Gui");
            this.getTropBook()
                .updatePagesFromInventory(entityplayer.inventory);
            FMLCommonHandler.instance()
                .showGuiScreen((Object) new GuiTropicalBook(this.getTropBook()));
        }
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    private TropicalBook getTropBook() {
        return (TropicalBook) Tropicraft.encyclopedia;
    }
}
