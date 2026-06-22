package net.tropicraft.item;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.registry.TCCreativeTabRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMobEgg extends ItemTropicraftMulti {

    public static String[] names;

    public ItemMobEgg(final String[] imageNames) {
        super(imageNames);
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
        if (ItemMobEgg.names.length != imageNames.length) {
            throw new IllegalArgumentException(
                "A Tropicraft developer failed to make the number of mob egg names in ItemMobEgg match up with the number of mob egg names in TCItemRegistry!");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int var4 = 0; var4 < ItemMobEgg.names.length; ++var4) {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    public boolean onItemUse(final ItemStack itemstack, final EntityPlayer entityplayer, final World world, int i,
        int j, int k, final int l, final float par8, final float par9, final float par10) {
        if (!world.isRemote) {
            String s = "";
            switch (itemstack.getItemDamage()) {
                case 0: {
                    s = "Iguana";
                    break;
                }
                case 1: {
                    s = "Starfish";
                    break;
                }
                case 2: {
                    s = "TreeFrogGreen";
                    break;
                }
                case 3: {
                    s = "TreeFrogRed";
                    break;
                }
                case 4: {
                    s = "TreeFrogYellow";
                    break;
                }
                case 5: {
                    s = "TreeFrogBlue";
                    break;
                }
                case 6: {
                    s = "Easter Island Head";
                    break;
                }
                case 7: {
                    s = "Marlin";
                    break;
                }
                case 8: {
                    s = "Tropical Fish";
                    if (!world.isRemote) {
                        final EntityTropicalFish fish = new EntityTropicalFish(world);
                        fish.setLocationAndAngles(i + 0.5, (double) j, k + 5.5, 0.0f, 0.0f);
                        world.spawnEntityInWorld((Entity) fish);
                        return true;
                    }
                    break;
                }
                case 9: {
                    s = "AshenHunter";
                    break;
                }
                case 10: {
                    s = "SeaTurtleEgg";
                    break;
                }
                case 11: {
                    s = "MOW";
                    break;
                }
                case 12: {
                    s = "VMonkey";
                    break;
                }
                case 13: {
                    s = "Koa Man";
                    break;
                }
                case 14: {
                    s = "TropiCreeper";
                    break;
                }
                case 15: {
                    s = "TropiSkeleton";
                    break;
                }
                case 16: {
                    s = "EagleRay";
                    break;
                }
                case 17: {
                    s = "Failgull";
                    break;
                }
                case 18: {
                    s = "SeaUrchin";
                    break;
                }
                default: {
                    return true;
                }
            }
            if (s.equals("Koa Man")) {
                final Random rand = new Random();
                final int choice = rand.nextInt(2);
                s = "KoaHunter";
                if (choice == 1) {
                    s = "KoaFisher";
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("tropicraft");
            sb.append(".");
            sb.append(s);
            s = sb.toString();
            i += Facing.offsetsXForSide[l];
            j += Facing.offsetsYForSide[l];
            k += Facing.offsetsZForSide[l];
            final Entity entity = EntityList.createEntityByName(s, world);
            if (entity != null) {
                if (!entityplayer.capabilities.isCreativeMode) {
                    --itemstack.stackSize;
                }
                entity.setLocationAndAngles(i + 0.5, (double) j, k + 0.5, 0.0f, 0.0f);
                if (entity instanceof EntityTropicalFish) {
                    ((EntityTropicalFish) entity).disableDespawning();
                }
                if (!world.isRemote) {
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLiving) entity).onSpawnWithEgg((IEntityLivingData) null);
                    }
                    world.spawnEntityInWorld(entity);
                }
            } else {
                System.out.println("Error spawning: " + s);
            }
        }
        return true;
    }

    static {
        ItemMobEgg.names = new String[] { "Iguana Egg", "Starfish Egg", "Green Frog Spawn", "Red Frog Spawn",
            "Yellow Frog Spawn", "Blue Frog Spawn", "Eye of Head", "Marlin Spawn", "Tropical Fish Spawn", "Ashen Ash",
            "Turtle Egg", "Man o' War", "Monkey's Paw", "Koa Headband", "TropiCreeper Egg", "TropiSkelly Skirt",
            "Spotted Eagle Ray", "Failgull", "Sea Urchin" };
    }
}
