package net.tropicraft.entity.hostile;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.tropicraft.entity.*;
import net.tropicraft.registry.*;

import CoroUtil.componentAI.*;
import CoroUtil.util.*;
import cpw.mods.fml.relauncher.*;

public class EntityTropiCreeper extends EntityCoroAI implements IMob {

    public AIAgent agent;
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime;
    private int explosionRadius;

    public EntityTropiCreeper(final World par1World) {
        super(par1World);
        this.fuseTime = 30;
        this.explosionRadius = 3;
        this.experienceValue = 8;
        if (this.agent == null) {
            this.agent = new AIAgent(this, false);
        }
        this.agent.moveLeadTicks = 0;
        this.agent.shouldAvoid = false;
    }

    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.boundingBox.minY);
        final int k = MathHelper.floor_double(this.posZ);
        return !CoroUtilBlock.isAir(this.worldObj.getBlock(i, j - 1, k))
            && this.worldObj.getFullBlockLightValue(i, j, k) > 8
            && super.getCanSpawnHere();
    }

    public void attackMelee(final Entity ent, final float dist) {
        this.setCreeperState(1);
    }

    public boolean isAIEnabled() {
        return true;
    }

    public int getMaxSafePointTries() {
        return (this.getAttackTarget() == null) ? 3 : (3 + ((int) this.getHealth() - 1));
    }

    protected void fall(final float par1) {
        super.fall(par1);
        this.timeSinceIgnited += (int) (par1 * 1.5f);
        if (this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }
    }

    public void writeEntityToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        if (this.dataWatcher.getWatchableObjectInt(17) == 1) {
            par1NBTTagCompound.setBoolean("powered", true);
        }
        par1NBTTagCompound.setShort("Fuse", (short) this.fuseTime);
        par1NBTTagCompound.setInteger("ExplosionRadius", this.explosionRadius);
    }

    public void readEntityFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.dataWatcher.updateObject(17, (par1NBTTagCompound.getBoolean("powered") ? 1 : 0));
        if (par1NBTTagCompound.hasKey("Fuse")) {
            this.fuseTime = par1NBTTagCompound.getShort("Fuse");
        }
        if (par1NBTTagCompound.hasKey("ExplosionRadius")) {
            this.explosionRadius = par1NBTTagCompound.getInteger("ExplosionRadius");
        }
    }

    public void onUpdate() {
        if (this.isEntityAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            final int i = this.getCreeperState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound("random.fuse", 1.0f, 0.5f);
            }
            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }
            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                if (!this.worldObj.isRemote) {
                    final boolean flag = this.worldObj.getGameRules()
                        .getGameRuleBooleanValue("mobGriefing");
                    this.onDeathBySelf();
                    this.setDead();
                }
            }
        }
        super.onUpdate();
    }

    public void onDeath(final DamageSource par1DamageSource) {
        super.onDeath(par1DamageSource);
        if (!(par1DamageSource.getEntity() instanceof EntitySkeleton)) {
            if (par1DamageSource.getEntity() instanceof EntityPlayer) {
                int y = this.worldObj.getHeightValue((int) this.posX, (int) this.posZ);
                final int xo = this.rand.nextInt(3) + 4;
                final int zo = xo + this.rand.nextInt(3) - new Random().nextInt(3);
                for (int x = (int) this.posX - xo; x < (int) this.posX + xo; ++x) {
                    for (int z = (int) this.posZ - zo; z < (int) this.posZ + zo; ++z) {
                        y = this.worldObj.getHeightValue(x, z);
                        final Block block = this.worldObj.getBlock(x, y - 1, z);
                        if (block.getMaterial() != Material.water && !CoroUtilBlock.isAir(block)) {
                            this.worldObj.setBlock(x, y, z, TCBlockRegistry.flowers, this.rand.nextInt(7), 3);
                        }
                    }
                }
            }
        }
    }

    public void onDeathBySelf() {
        int y;
        final int xo = this.rand.nextInt(3) + 2;
        final int zo = this.rand.nextInt(3) + 2;
        for (int x = (int) this.posX - xo; x < (int) this.posX + xo; ++x) {
            for (int z = (int) this.posZ - zo; z < (int) this.posZ + zo; ++z) {
                if (this.rand.nextInt(3) != 0) {
                    for (y = (int) this.posY + 3; y > (int) this.posY - 3
                        && (!this.worldObj.isAirBlock(x, y, z) || !this.worldObj.getBlock(x, y - 1, z)
                            .isOpaqueCube()); --y) {}
                    if (y != (int) this.posY - 3) {
                        if (this.rand.nextInt(10) < 6 && this.worldObj.getBlock(x, y, z) != TCBlockRegistry.bambooBundle
                            && this.worldObj.getBlock(x, y, z) != TCBlockRegistry.coconut) {
                            final int flowerType = this.rand.nextInt(7);
                            if (TCBlockRegistry.flowers.canPlaceBlockAt(this.worldObj, x, y, z)) {
                                this.worldObj.setBlock(x, y, z, TCBlockRegistry.flowers, flowerType, 3);
                            } else {
                                this.entityDropItem(new ItemStack(TCBlockRegistry.flowers, 1, flowerType), 0.5f);
                            }
                        } else if (this.rand.nextInt(10) < 7
                            && !CoroUtilBlock.isEqual(this.worldObj.getBlock(x, y, z), TCBlockRegistry.bambooBundle)
                            && !CoroUtilBlock.isEqual(this.worldObj.getBlock(x, y, z), TCBlockRegistry.coconut)
                            && CoroUtilBlock.isEqual(this.worldObj.getBlock(x, y - 1, z), Blocks.grass)) {
                                this.worldObj.setBlock(x, y, z, Blocks.tallgrass, 1, 3);
                            } else if (this.rand.nextInt(10) < 8
                                && this.worldObj.getBlock(x, y - 1, z) != TCBlockRegistry.bambooBundle
                                && !CoroUtilBlock.isAir(this.worldObj.getBlock(x, y - 1, z))) {
                                    final int palmTreeType = this.rand.nextInt(3);
                                    final WorldGenerator gen = null;
                                }
                    }
                }
            }
        }
    }

    protected String getHurtSound() {
        return "mob.creeper.say";
    }

    protected String getDeathSound() {
        return "mob.creeper.death";
    }

    public boolean attackEntityAsMob(final Entity par1Entity) {
        return true;
    }

    public boolean getPowered() {
        return this.dataWatcher.getWatchableObjectInt(17) == 1;
    }

    @SideOnly(Side.CLIENT)
    public float getCreeperFlashIntensity(final float par1) {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * par1) / (this.fuseTime - 2);
    }

    public int getCreeperState() {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setCreeperState(final int par1) {
        this.dataWatcher.updateObject(16, par1);
    }

    public void onStruckByLightning(final EntityLightningBolt par1EntityLightningBolt) {
        super.onStruckByLightning(par1EntityLightningBolt);
        this.dataWatcher.updateObject(17, 1);
    }

    public void updateAITasks() {
        super.updateAITasks();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, -1);
        this.dataWatcher.addObject(17, 0);
    }
}
