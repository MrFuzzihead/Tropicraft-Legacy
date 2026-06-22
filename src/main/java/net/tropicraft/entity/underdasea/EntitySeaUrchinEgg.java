package net.tropicraft.entity.underdasea;

import net.minecraft.world.World;

public class EntitySeaUrchinEgg extends EntityEchinodermEgg {

    public EntitySeaUrchinEgg(final World world) {
        super(world);
    }

    public EntityEchinoderm createBaby() {
        return (EntityEchinoderm) new EntitySeaUrchin(this.worldObj, true);
    }
}
