package net.tropicraft.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEagleRay extends EntityTropicraftWaterMob {

    public static final int WING_JOINTS = 10;
    public static final int WING_CYCLE_TICKS = 60;
    public static final float PHASES = 0.33f;
    private float[] wingAmplitudes;
    private float[] prevWingAmplitudes;
    private int animationTicks;

    public EntityEagleRay(final World world) {
        super(world);
        this.wingAmplitudes = new float[10];
        this.prevWingAmplitudes = new float[10];
        this.setSize(1.0f, 0.25f);
        this.type = WaterMobType.OCEAN_DWELLER;
    }

    public EntityEagleRay(final World par1World, final WaterMobType type) {
        super(par1World, type);
        this.wingAmplitudes = new float[10];
        this.prevWingAmplitudes = new float[10];
        this.setSize(1.0f, 0.25f);
    }

    public Entity getEntityToAttack() {
        return null;
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10.0);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            if (this.animationTicks < 60) {
                ++this.animationTicks;
            } else {
                this.animationTicks = 0;
            }
            this.updateWingAmplitudes();
        }
    }

    private void updateWingAmplitudes() {
        final float[] temp = this.prevWingAmplitudes;
        this.prevWingAmplitudes = this.wingAmplitudes;
        this.wingAmplitudes = temp;
        for (int i = 1; i < 10; ++i) {
            this.wingAmplitudes[i] = this.amplitudeFunc((float) i);
        }
    }

    private float decayFunc(final float n) {
        return n / 9.0f;
    }

    private float amplitudeFunc(final float n) {
        final double angle = 6.283185307179586 * n / 9.0;
        return this.decayFunc(n) * MathHelper
            .sin((float) (this.getAnimationProgress() * 2.0f * 3.141592653589793 + 0.33000001311302185 * angle));
    }

    private float getAnimationProgress() {
        return this.animationTicks / 60.0f;
    }

    public float[] getWingAmplitudes() {
        return this.wingAmplitudes;
    }

    public float[] getPrevWingAmplitudes() {
        return this.prevWingAmplitudes;
    }

    public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }

    @Override
    protected int attackStrength() {
        return 0;
    }
}
