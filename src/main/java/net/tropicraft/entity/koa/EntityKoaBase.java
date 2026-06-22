package net.tropicraft.entity.koa;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;

import CoroUtil.componentAI.AIAgent;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobHunt;
import CoroUtil.diplomacy.DiplomacyHelper;
import CoroUtil.diplomacy.TeamTypes;

public class EntityKoaBase extends EntityCoroAI {

    public boolean wasInWater;
    public int inWaterTick;
    public int outWaterTick;
    public float waterOffsetY;
    public String koaName;
    public final String[] tribalMaleNames;

    public EntityKoaBase(final World par1World) {
        super(par1World);
        this.wasInWater = false;
        this.waterOffsetY = 0.0f;
        this.koaName = "";
        this.tribalMaleNames = new String[] { "Akamu", "Ekewaka", "Ikaika", "Iukini", "Kai", "Kaimana", "Kaimi",
            "Kanoa", "Kapena", "Keahi", "Keaweaheulu", "Kekipi", "Kekoa", "Konani", "Makani", "Mano", "Nahele" };
        this.agent.jobMan.clearJobs();
        final JobBase jb = (JobBase) new JobHunt(this.agent.jobMan);
        jb.dontStrayFromHome = true;
        this.agent.jobMan.addPrimaryJob(jb);
        this.agent.collideResistPathing = 0.7f;
        this.agent.dipl_info = TeamTypes.getType("koa");
        this.agent.entInv.grabItems = true;
        this.agent.scanForHomeChest = true;
        this.agent.shouldFixBadYPathing = true;
        final AIAgent agent = this.agent;
        final AIAgent agent2 = this.agent;
        final AIAgent agent3 = this.agent;
        final float n = 0.9f;
        this.entityCollisionReduction = n;
        agent3.collideResistPathing = n;
        agent2.collideResistFormation = n;
        agent.collideResistClose = n;
        this.experienceValue = 15;
        this.func_110163_bv();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.agent.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(30.0);
    }

    public void readEntityFromNBT(final NBTTagCompound par1nbtTagCompound) {
        super.readEntityFromNBT(par1nbtTagCompound);
        this.koaName = par1nbtTagCompound.getString("koaName");
    }

    public void writeEntityToNBT(final NBTTagCompound par1nbtTagCompound) {
        super.writeEntityToNBT(par1nbtTagCompound);
        par1nbtTagCompound.setString("koaName", this.koaName);
    }

    public void checkNewAgent() {
        if (this.agent == null) {
            this.agent = new AIAgent((ICoroAI) this, true);
        }
    }

    protected boolean canDespawn() {
        return false;
    }

    public boolean isEnemy(final Entity ent) {
        return DiplomacyHelper.shouldTargetEnt((ICoroAI) this, ent);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.isInWater()) {
            if (this.outWaterTick != 0) {
                this.outWaterTick = 0;
            }
            ++this.inWaterTick;
        } else {
            if (this.inWaterTick != 0) {
                this.inWaterTick = 0;
            }
            ++this.outWaterTick;
        }
        if (!this.isInWater() && this.wasInWater && this.isCollidedHorizontally) {
            this.motionY = 0.5400000214576721;
        }
        this.wasInWater = this.isInWater();
        if (this.isInWater() && this.motionY < -0.001) {
            this.motionY = -0.001;
        }
        if (this.isInWater() && this.isCollidedHorizontally) {
            this.motionX *= 1.2000000476837158;
            this.motionZ *= 1.2000000476837158;
        }
    }
}
