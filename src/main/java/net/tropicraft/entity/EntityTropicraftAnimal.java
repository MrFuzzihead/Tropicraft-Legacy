package net.tropicraft.entity;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

public abstract class EntityTropicraftAnimal extends EntityAnimal {

    public EntityTropicraftAnimal(final World world) {
        super(world);
    }

    protected String tcSound(final String postfix) {
        return String.format("%s:%s", "tropicraft", postfix);
    }
}
