package net.tropicraft.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntitySeaUrchin extends EntityEchinoderm {

    public static final float BABY_SIZE = 0.25f;
    public static final float ADULT_SIZE = 0.5f;
    public static final float BABY_YOFFSET = 0.125f;
    public static final float ADULT_YOFFSET = 0.25f;

    public EntitySeaUrchin(final World world) {
        super(world);
        this.experienceValue = 5;
    }

    public EntitySeaUrchin(final World world, final boolean baby) {
        super(world, baby);
        this.experienceValue = 5;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10.0);
    }

    protected void updateWanderPath() {}

    public boolean attackEntityFrom(final DamageSource source, final float amt) {
        if (source.getDamageType()
            .equals("player")) {
            final Entity ent = source.getEntity();
            if (ent instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) ent;
                if (player.getHeldItem() == null) {
                    player.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this), 2.0f);
                }
            }
        }
        return super.attackEntityFrom(source, amt);
    }

    public void applyEntityCollision(final Entity ent) {
        super.applyEntityCollision(ent);
        if (!this.worldObj.isRemote && ent instanceof EntityLiving
            && !(ent instanceof EntitySeaUrchin)
            && !(ent instanceof EntitySeaUrchinEgg)) {
            ent.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this), 2.0f);
        }
    }

    protected Item getDropItem() {
        return (Item) (this.isChild() ? null : TCItemRegistry.seaUrchinRoe);
    }

    public EntityEchinodermEgg createEgg() {
        return new EntitySeaUrchinEgg(this.worldObj);
    }

    public float getBabyWidth() {
        return 0.25f;
    }

    public float getAdultWidth() {
        return 0.5f;
    }

    public float getBabyHeight() {
        return 0.25f;
    }

    public float getAdultHeight() {
        return 0.5f;
    }

    public float getBabyYOffset() {
        return 0.125f;
    }

    public float getAdultYOffset() {
        return 0.25f;
    }
}
