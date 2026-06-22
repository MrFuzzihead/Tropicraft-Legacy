package net.tropicraft.drinks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class DrinkActionPotion extends DrinkAction {

    public int potionId;
    public int duration;
    public int amplifier;

    public DrinkActionPotion(final int potionId, final int duration, final int amplifier) {
        this.potionId = potionId;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public void onDrink(final EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(this.potionId, this.duration * 20, this.amplifier));
    }
}
