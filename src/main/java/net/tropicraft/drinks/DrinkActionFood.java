package net.tropicraft.drinks;

import net.minecraft.entity.player.EntityPlayer;

public class DrinkActionFood extends DrinkAction {

    public int healAmount;
    public float saturationModifier;

    public DrinkActionFood(final int healAmount, final float saturationModifier) {
        this.healAmount = healAmount;
        this.saturationModifier = saturationModifier;
    }

    public void onDrink(final EntityPlayer player) {
        player.getFoodStats()
            .addStats(this.healAmount, this.saturationModifier);
    }
}
