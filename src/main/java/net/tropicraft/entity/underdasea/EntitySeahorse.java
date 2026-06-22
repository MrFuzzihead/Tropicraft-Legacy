package net.tropicraft.entity.underdasea;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySeahorse extends EntityTropicraftWaterMob {

    private static final int DATAWATCHER_COLOR = 30;

    public EntitySeahorse(final World par1World) {
        super(par1World);
        this.type = WaterMobType.OCEAN_DWELLER;
        this.setSize(0.75f, 1.1f);
    }

    public EntitySeahorse(final World par1World, final int color) {
        this(par1World);
        this.setColor(color);
    }

    @Override
    public void entityInit() {
        this.dataWatcher.addObject(30, 0);
        super.entityInit();
    }

    public void writeEntityToNBT(final NBTTagCompound nbt) {
        nbt.setInteger("Color", this.getColor());
        super.writeEntityToNBT(nbt);
    }

    public void readEntityFromNBT(final NBTTagCompound nbt) {
        this.setColor(nbt.getInteger("Color"));
        super.readEntityFromNBT(nbt);
    }

    public int getColor() {
        return this.dataWatcher.getWatchableObjectInt(30);
    }

    public void setColor(final int color) {
        this.dataWatcher.updateObject(30, color);
    }

    public String getColorName() {
        switch (this.getColor()) {
            case 0: {
                return "razz";
            }
            case 1: {
                return "blue";
            }
            case 2: {
                return "cyan";
            }
            case 3: {
                return "yellow";
            }
            case 4: {
                return "green";
            }
            case 5: {
                return "orange";
            }
            default: {
                return "razz";
            }
        }
    }

    @Override
    protected int attackStrength() {
        return 0;
    }
}
