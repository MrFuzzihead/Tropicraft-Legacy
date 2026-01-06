package net.tropicraft.entity.hostile;

import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.entity.ai.jobs.*;

import CoroUtil.componentAI.jobSystem.*;

public class SpiderAdult extends SpiderBase {

    public boolean isMother;
    public int maxEggsToSpawn;

    public SpiderAdult(final World par1World) {
        super(par1World);
        this.isMother = false;
        this.maxEggsToSpawn = -1;
        this.setSize(1.4f, 0.9f);
        this.experienceValue = 6;
    }

    public void attackMelee(final Entity ent, final float dist) {
        ent.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this), this.isMother ? 6.0f : 4.0f);
    }

    @Override
    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.boundingBox.minY);
        final int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlock(i, j - 1, k) == Blocks.stone && super.getCanSpawnHere();
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher()
            .addObject(29, 0);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    protected boolean canDespawn() {
        return !this.isMother;
    }

    public IEntityLivingData onSpawnWithEgg(final IEntityLivingData par1EntityLivingBaseData) {
        final Random rand = new Random();
        if (rand.nextInt(4) == 0) {
            this.isMother = true;
        }
        this.maxEggsToSpawn = 4 + rand.nextInt(6);
        this.initSpiderType();
        return super.onSpawnWithEgg(par1EntityLivingBaseData);
    }

    public void initSpiderType() {
        if (this.isMother) {
            final JobEggManage job = new JobEggManage(this.agent.jobMan);
            job.eggSpawnMax = this.maxEggsToSpawn;
            this.agent.jobMan.addJob((JobBase) job);
            this.getDataWatcher()
                .updateObject(29, (this.isMother ? 1 : 0));
        }
    }

    public void readEntityFromNBT(final NBTTagCompound par1nbtTagCompound) {
        super.readEntityFromNBT(par1nbtTagCompound);
        this.isMother = par1nbtTagCompound.getBoolean("isMother");
        this.maxEggsToSpawn = par1nbtTagCompound.getInteger("maxEggsToSpawn");
        this.initSpiderType();
    }

    public void writeEntityToNBT(final NBTTagCompound par1nbtTagCompound) {
        super.writeEntityToNBT(par1nbtTagCompound);
        par1nbtTagCompound.setBoolean("isMother", this.isMother);
        par1nbtTagCompound.setInteger("maxEggsToSpawn", this.maxEggsToSpawn);
    }
}
