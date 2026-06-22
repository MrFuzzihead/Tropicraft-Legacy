package net.tropicraft.entity.underdasea;

import net.minecraft.world.World;

public class EntityStarfishEgg extends EntityEchinodermEgg {

    private StarfishType starfishType;

    public EntityStarfishEgg(final World par1World) {
        super(par1World);
        this.starfishType = StarfishType.values()[this.rand.nextInt(StarfishType.values().length)];
    }

    public EntityStarfishEgg(final World world, final StarfishType starfishType) {
        this(world);
        this.starfishType = starfishType;
    }

    public EntityEchinoderm createBaby() {
        final EntityStarfish baby = new EntityStarfish(this.worldObj, true);
        baby.setStarfishType(this.starfishType);
        return (EntityEchinoderm) baby;
    }
}
