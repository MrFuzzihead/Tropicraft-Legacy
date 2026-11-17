package net.tropicraft.block.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.drinks.Ingredient;
import net.tropicraft.item.ItemCocktail;
import net.tropicraft.registry.TCDrinkMixerRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class TileEntityEIHMixer extends TileEntity {

    public int ticks;
    public static final int TICKS_TO_MIX = 80;
    public ItemStack[] ingredients;
    public boolean mixing;
    public ItemStack result;

    public TileEntityEIHMixer() {
        this.mixing = false;
        this.ingredients = new ItemStack[2];
    }

    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.ticks = nbt.getInteger("MixTicks");
        this.mixing = nbt.getBoolean("Mixing");
        if (nbt.hasKey("Ingredient1")) {
            this.ingredients[0] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Ingredient1"));
        } else {
            this.ingredients[0] = null;
        }
        if (nbt.hasKey("Ingredient2")) {
            this.ingredients[1] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Ingredient2"));
        } else {
            this.ingredients[1] = null;
        }
        if (nbt.hasKey("Result")) {
            this.result = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Result"));
        } else {
            this.result = null;
        }
    }

    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("MixTicks", this.ticks);
        nbt.setBoolean("Mixing", this.mixing);
        if (this.ingredients[0] != null) {
            final NBTTagCompound var4 = new NBTTagCompound();
            this.ingredients[0].writeToNBT(var4);
            nbt.setTag("Ingredient1", (NBTBase) var4);
        }
        if (this.ingredients[1] != null) {
            final NBTTagCompound var4 = new NBTTagCompound();
            this.ingredients[1].writeToNBT(var4);
            nbt.setTag("Ingredient2", (NBTBase) var4);
        }
        if (this.result != null) {
            final NBTTagCompound var4 = new NBTTagCompound();
            this.result.writeToNBT(var4);
            nbt.setTag("Result", (NBTBase) var4);
        }
    }

    public void updateEntity() {
        if (this.ticks < 80 && this.mixing) {
            ++this.ticks;
            if (this.ticks == 80) {
                this.finishMixing();
            }
        }
    }

    public boolean isDoneMixing() {
        return this.result != null;
    }

    public ItemStack[] getIngredients() {
        return this.ingredients;
    }

    public Ingredient[] listAllIngredients() {
        final List<Ingredient> is = new ArrayList<Ingredient>();
        if (this.ingredients[0] == null || this.ingredients[1] == null) {
            return null;
        }
        is.addAll(listIngredients(this.ingredients[0]));
        is.addAll(listIngredients(this.ingredients[1]));
        Collections.sort(is);
        return is.toArray(new Ingredient[is.size()]);
    }

    public static Ingredient findMatchingIngredient(final ItemStack stack) {
        for (final Ingredient ingredient : Ingredient.ingredientsList) {
            if (ingredient != null) {
                if (ItemStack.areItemStacksEqual(ingredient.getIngredient(), stack)) {
                    return ingredient;
                }
            }
        }
        return null;
    }

    public static List<Ingredient> listIngredients(final ItemStack stack) {
        final List<Ingredient> is = new ArrayList<Ingredient>();
        if (stack != null && stack.getItem() != null && stack.getItem() == TCItemRegistry.cocktail) {
            for (final Ingredient ingredient : ItemCocktail.getIngredients(stack)) {
                is.add(ingredient);
            }
        } else {
            final Ingredient i = findMatchingIngredient(stack);
            is.add(i);
        }
        return is;
    }

    public void startMixing() {
        this.ticks = 0;
        this.mixing = true;
        this.sync();
    }

    public void emptyMixer() {
        if (this.ingredients[0] != null) {
            final EntityItem item = new EntityItem(
                this.worldObj,
                (double) this.xCoord,
                (double) this.yCoord,
                (double) this.zCoord,
                this.ingredients[0]);
            this.worldObj.spawnEntityInWorld(item);
            this.ingredients[0] = null;
        }
        if (this.ingredients[1] != null) {
            final EntityItem item = new EntityItem(
                this.worldObj,
                (double) this.xCoord,
                (double) this.yCoord,
                (double) this.zCoord,
                this.ingredients[1]);
            this.worldObj.spawnEntityInWorld(item);
            this.ingredients[1] = null;
        }
        this.ticks = 80;
        this.mixing = false;
        this.sync();
    }

    public void retrieveResult() {
        EntityItem e = new EntityItem(
            this.worldObj,
            (double) this.xCoord,
            (double) this.yCoord,
            (double) this.zCoord,
            this.result);
        this.worldObj.spawnEntityInWorld(e);
        final ItemStack container1 = this.ingredients[0].getItem()
            .getContainerItem(this.ingredients[0]);
        if (container1 != null) {
            e = new EntityItem(
                this.worldObj,
                (double) this.xCoord,
                (double) this.yCoord,
                (double) this.zCoord,
                container1);
            this.worldObj.spawnEntityInWorld(e);
        }
        final ItemStack container2 = this.ingredients[1].getItem()
            .getContainerItem(this.ingredients[1]);
        if (container2 != null) {
            e = new EntityItem(
                this.worldObj,
                (double) this.xCoord,
                (double) this.yCoord,
                (double) this.zCoord,
                container2);
            this.worldObj.spawnEntityInWorld(e);
        }
        this.ingredients[0] = null;
        this.ingredients[1] = null;
        this.result = null;
        this.ticks = 0;
        this.mixing = false;
        this.sync();
    }

    public void finishMixing() {
        this.result = this.getResult(this.getIngredients());
        this.sync();
    }

    /**
     * Puts an ingredient into the first free slot.
     * <p>
     * The order the player clicks in must not matter, because the recipe table matches a recipe in either order
     * ({@link TCDrinkMixerRegistry#getResult(ItemStack[])}) and the ingredients are sorted by id before the drink is
     * built anyway. The mixer used to insist on a primary ingredient first and an additive second, which silently
     * refused perfectly valid input: an additive-first pair such as "Sugar + Lime" - the order the recipes are written
     * in and the order the drink mixer's own tutorial gives - did nothing at all, and a recipe whose two ingredients
     * are both additives (Pineapple Cubes + Coconut Chunk) could not even be started.
     * </p>
     * <p>
     * What a drink really cannot have is two primary ingredients, so that, rather than the slot the ingredient lands
     * in, is the rule enforced here.
     * </p>
     */
    public boolean addToMixer(final ItemStack ingredient) {
        if (ingredient == null || ingredient.getItem() == null) {
            return false;
        }
        final int slot = (this.ingredients[0] == null) ? 0 : ((this.ingredients[1] == null) ? 1 : -1);
        if (slot < 0) {
            return false;
        }
        if (ingredient.getItem() == TCItemRegistry.cocktail) {
            // A cocktail already carries the primary ingredient of the drink it came from, so it can start a mix but
            // never extend one.
            if (slot != 0) {
                return false;
            }
            this.ingredients[slot] = ingredient;
            this.sync();
            return true;
        }
        final Ingredient added = findMatchingIngredient(ingredient);
        if (added == null) {
            // Not a drink ingredient at all (the bamboo mug, tools, dirt...).
            return false;
        }
        for (final ItemStack existing : this.ingredients) {
            if (existing == null) {
                continue;
            }
            for (final Ingredient present : listIngredients(existing)) {
                if (present == null) {
                    continue;
                }
                if (present == added || (present.isPrimary() && added.isPrimary())) {
                    return false;
                }
            }
        }
        this.ingredients[slot] = ingredient;
        this.sync();
        return true;
    }

    public boolean isMixing() {
        return this.mixing;
    }

    private boolean isMixerFull() {
        return this.ingredients[0] != null && this.ingredients[1] != null;
    }

    public boolean canMix() {
        return !this.mixing && this.isMixerFull();
    }

    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public void sync() {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return (Packet) new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }

    public ItemStack getResult(final ItemStack[] ingredients2) {
        return TCDrinkMixerRegistry.getInstance()
            .getResult(ingredients2);
    }
}
