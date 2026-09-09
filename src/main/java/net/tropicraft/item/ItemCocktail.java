package net.tropicraft.item;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityBambooMug;
import net.tropicraft.drinks.ColorMixer;
import net.tropicraft.drinks.Drink;
import net.tropicraft.drinks.Ingredient;
import net.tropicraft.drinks.MixerRecipe;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCDrinkMixerRegistry;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCocktail extends ItemTropicraft {

    private static final int DEFAULT_COLOR = 15973942;
    private IIcon contentsIcon;

    public ItemCocktail(final CreativeTabs tabs) {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setCreativeTab(TCCreativeTabRegistry.tabFood);
        this.setContainerItem(TCItemRegistry.bambooMug);
    }

    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List,
        final boolean par4) {
        if (par1ItemStack.stackTagCompound == null) {
            return;
        }
        final NBTTagList ingredients = par1ItemStack.stackTagCompound.getTagList("Ingredients", 10);
        for (int i = 0; i < ingredients.tagCount(); ++i) {
            final NBTTagCompound ingredient = ingredients.getCompoundTagAt(i);
            final int id = ingredient.getByte("IngredientID");
            final String ingredientName = Ingredient.ingredientsList[id].getIngredient()
                .getDisplayName();
            final int ingredientColor = Ingredient.ingredientsList[id].getColor();
            par3List.add(ingredientName);
        }
        final Drink drink = Drink.drinkList[par1ItemStack.stackTagCompound.getByte("DrinkID")];
        if (drink != null) {
            par3List.add("§o" + drink.displayName);
        }
    }

    public void getSubItems(final Item item, final CreativeTabs par2CreativeTabs, final List list) {
        for (final MixerRecipe recipe : TCDrinkMixerRegistry.getInstance()
            .getRecipes()) {
            list.add(makeCocktail(recipe));
        }
    }

    public boolean getShareTag() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int par1, final int par2) {
        return (par2 == 0) ? this.itemIcon : this.contentsIcon;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
        if (par2 == 0) {
            return 16777215;
        }
        return getCocktailColor(par1ItemStack);
    }

    public static int getCocktailColor(final ItemStack stack) {
        if (stack.stackTagCompound == null) {
            return 15973942;
        }
        if (stack.stackTagCompound.hasKey("Color")) {
            return stack.stackTagCompound.getInteger("Color");
        }
        return 15973942;
    }

    public static ItemStack makeCocktail(final MixerRecipe recipe) {
        final ItemStack stack = new ItemStack(TCItemRegistry.cocktail);
        final NBTTagCompound nbt = new NBTTagCompound();
        final Drink drink = recipe.getCraftingResult();
        nbt.setByte("DrinkID", (byte) drink.drinkId);
        final NBTTagList tagList = new NBTTagList();
        Ingredient primary = null;
        final List<Ingredient> additives = new LinkedList<Ingredient>();
        for (final Ingredient ingredient : recipe.getIngredients()) {
            final NBTTagCompound ingredientNbt = new NBTTagCompound();
            ingredientNbt.setByte("IngredientID", (byte) ingredient.ingredientId);
            tagList.appendTag((NBTBase) ingredientNbt);
            if (ingredient.isPrimary()) {
                primary = ingredient;
            } else {
                additives.add(ingredient);
            }
        }
        nbt.setTag("Ingredients", (NBTBase) tagList);
        int color = (primary == null) ? 15973942 : primary.getColor();
        for (final Ingredient additive : additives) {
            color = ColorMixer.getInstance()
                .alphaBlendRGBA(color, additive.getColor(), additive.getAlpha());
        }
        nbt.setInteger("Color", color);
        stack.stackTagCompound = nbt;
        return stack;
    }

    public static ItemStack makeCocktail(final Ingredient[] ingredients) {
        final ItemStack stack = new ItemStack(TCItemRegistry.cocktail);
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("DrinkID", (byte) 0);
        final NBTTagList tagList = new NBTTagList();
        Ingredient primary = null;
        final List<Ingredient> additives = new LinkedList<Ingredient>();
        for (final Ingredient ingredient : ingredients) {
            final NBTTagCompound ingredientNbt = new NBTTagCompound();
            ingredientNbt.setByte("IngredientID", (byte) ingredient.ingredientId);
            tagList.appendTag((NBTBase) ingredientNbt);
            if (ingredient.isPrimary()) {
                primary = ingredient;
            } else {
                additives.add(ingredient);
            }
        }
        nbt.setTag("Ingredients", (NBTBase) tagList);
        int color = (primary == null) ? 15973942 : primary.getColor();
        for (final Ingredient additive : additives) {
            color = ColorMixer.getInstance()
                .alphaBlendRGBA(color, additive.getColor(), additive.getAlpha());
        }
        nbt.setInteger("Color", color);
        stack.stackTagCompound = nbt;
        return stack;
    }

    public static Ingredient[] getIngredients(final ItemStack stack) {
        if (stack.getItem() != TCItemRegistry.cocktail || !stack.hasTagCompound()) {
            return new Ingredient[0];
        }
        final NBTTagCompound nbt = stack.getTagCompound();
        final NBTTagList tagList = nbt.getTagList("Ingredients", 10);
        final Ingredient[] ingredients = new Ingredient[tagList.tagCount()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            final int id = tagList.getCompoundTagAt(i)
                .getByte("IngredientID");
            ingredients[i] = Ingredient.ingredientsList[id];
        }
        return ingredients;
    }

    public static Drink getDrink(final ItemStack stack) {
        if (stack.getItem() != TCItemRegistry.cocktail || !stack.hasTagCompound()) {
            return null;
        }
        final NBTTagCompound nbt = stack.getTagCompound();
        return Drink.drinkList[nbt.getByte("DrinkID")];
    }

    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World,
        int par4, int par5, int par6, int par7, final float par8, final float par9, final float par10) {
        final Block var11 = par3World.getBlock(par4, par5, par6);
        if (var11 == Blocks.snow) {
            par7 = 1;
        } else if (var11 != Blocks.vine && var11 != Blocks.tallgrass
            && var11 != Blocks.deadbush
            && (var11 == null || !var11.isReplaceable((IBlockAccess) par3World, par4, par5, par6))) {
                if (par7 == 0) {
                    --par5;
                } else if (par7 == 1) {
                    ++par5;
                } else if (par7 == 2) {
                    --par6;
                } else if (par7 == 3) {
                    ++par6;
                } else if (par7 == 4) {
                    --par4;
                } else if (par7 == 5) {
                    ++par4;
                }
            }
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        }
        if (par3World.canPlaceEntityOnSide(
            (Block) TCBlockRegistry.bambooMug,
            par4,
            par5,
            par6,
            false,
            par7,
            (Entity) par2EntityPlayer,
            (ItemStack) null)) {
            final Block var12 = (Block) TCBlockRegistry.bambooMug;
            final int var13 = this.getMetadata(par1ItemStack.getItemDamage());
            final int var14 = var12.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);
            if (this.placeBlockAt(
                par1ItemStack,
                par2EntityPlayer,
                par3World,
                par4,
                par5,
                par6,
                par7,
                par8,
                par9,
                par10,
                var14)) {
                par3World.playSoundEffect(
                    (double) (par4 + 0.5f),
                    (double) (par5 + 0.5f),
                    (double) (par6 + 0.5f),
                    var12.stepSound.func_150496_b(),
                    (var12.stepSound.getVolume() + 1.0f) / 2.0f,
                    var12.stepSound.getPitch() * 0.8f);
                --par1ItemStack.stackSize;
            }
            return true;
        }
        return false;
    }

    public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final int x,
        final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ,
        final int metadata) {
        if (!world.setBlock(x, y, z, (Block) TCBlockRegistry.bambooMug, metadata, 2)) {
            return false;
        }
        if (world.getBlock(x, y, z) == TCBlockRegistry.bambooMug) {
            TCBlockRegistry.bambooMug.onBlockPlacedBy(world, x, y, z, (EntityLivingBase) player, (ItemStack) null);
            TCBlockRegistry.bambooMug.onPostBlockPlaced(world, x, y, z, metadata);
            final TileEntityBambooMug mug = (TileEntityBambooMug) world.getTileEntity(x, y, z);
            mug.setCocktail(stack.copy());
            final int var6 = MathHelper.floor_double(player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
            int meta = 2;
            if (var6 == 0) {
                meta = 2;
            } else if (var6 == 1) {
                meta = 5;
            } else if (var6 == 2) {
                meta = 3;
            } else if (var6 == 3) {
                meta = 4;
            }
            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        }
        return true;
    }

    public int getMaxItemUseDuration(final ItemStack par1ItemStack) {
        return 32;
    }

    public EnumAction getItemUseAction(final ItemStack par1ItemStack) {
        return EnumAction.drink;
    }

    public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World,
        final EntityPlayer par3EntityPlayer) {
        final Drink drink = getDrink(par1ItemStack);
        if (drink != null) {
            if (!par3EntityPlayer.canEat(drink.alwaysEdible)) {
                return par1ItemStack;
            }
        } else if (!par3EntityPlayer.canEat(false)) {
            return par1ItemStack;
        }
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }

    public ItemStack onEaten(final ItemStack par1ItemStack, final World par2World,
        final EntityPlayer par3EntityPlayer) {
        par2World.playSoundAtEntity(
            (Entity) par3EntityPlayer,
            "random.burp",
            0.5f,
            par2World.rand.nextFloat() * 0.1f + 0.9f);
        for (final Ingredient ingredient : getIngredients(par1ItemStack)) {
            ingredient.onDrink(par3EntityPlayer);
        }
        final Drink drink = getDrink(par1ItemStack);
        if (drink != null) {
            drink.onDrink(par3EntityPlayer);
        }
        return new ItemStack(TCItemRegistry.bambooMug);
    }

    @Override
    public void registerIcons(final IIconRegister iconRegistry) {
        super.registerIcons(iconRegistry);
        this.contentsIcon = iconRegistry.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1)
                + "contents");
    }
}
