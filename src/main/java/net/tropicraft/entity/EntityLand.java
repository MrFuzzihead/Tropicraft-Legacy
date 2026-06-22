package net.tropicraft.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public abstract class EntityLand extends EntityCreature {

    public EntityLand(final World world) {
        super(world);
        this.setSize(0.7f, 1.95f);
    }

    public boolean isAIEnabled() {
        return true;
    }

    protected String tcSound(final String postfix) {
        return String.format("%s:%s", "tropicraft", postfix);
    }

    public enum EnumTropiMobType {
        WATER,
        FIRE,
        REPTILE;
    }
}
