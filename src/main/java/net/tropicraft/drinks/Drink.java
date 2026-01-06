package net.tropicraft.drinks;

import java.util.*;

import net.minecraft.entity.player.*;
import net.minecraft.potion.*;

public class Drink {

    public static final Drink[] drinkList;
    public static final Drink lemonade;
    public static final Drink limeade;
    public static final Drink orangeade;
    public static final Drink caipirinha;
    public static final Drink blackCoffee;
    public static final Drink pinaColada;
    public int drinkId;
    public int color;
    public String displayName;
    public boolean alwaysEdible;
    public boolean hasUmbrella;
    public List<DrinkAction> actions;

    public Drink(final int id, final int color, final String displayName) {
        this.actions = new ArrayList<DrinkAction>();
        Drink.drinkList[id] = this;
        this.drinkId = id;
        this.color = color;
        this.displayName = displayName;
    }

    public Drink setHasUmbrella(final boolean has) {
        this.hasUmbrella = has;
        return this;
    }

    public Drink setAlwaysEdible(final boolean edible) {
        this.alwaysEdible = edible;
        return this;
    }

    public Drink addAction(final DrinkAction action) {
        this.actions.add(action);
        return this;
    }

    public void onDrink(final EntityPlayer player) {
        for (final DrinkAction action : this.actions) {
            action.onDrink(player);
        }
    }

    static {
        drinkList = new Drink[128];
        lemonade = new Drink(1, 16440129, "Lemonade").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
        limeade = new Drink(2, 8710282, "Limeade").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
        orangeade = new Drink(3, 15973942, "Orangeade").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
        caipirinha = new Drink(4, 9764662, "Caipirinha").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1))
            .setHasUmbrella(true);
        blackCoffee = new Drink(5, 6833196, "Black Coffee");
        pinaColada = new Drink(6, 15662848, "Piña Colada").addAction(new DrinkActionPotion(Potion.confusion.id, 10, 0))
            .setAlwaysEdible(true);
    }
}
