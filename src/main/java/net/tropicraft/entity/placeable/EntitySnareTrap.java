package net.tropicraft.entity.placeable;

import java.util.List;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.*;
import net.tropicraft.*;
import net.tropicraft.registry.*;
import net.tropicraft.util.*;

import CoroUtil.packet.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.relauncher.*;
import io.netty.buffer.*;

public class EntitySnareTrap extends Entity implements IEntityAdditionalSpawnData {

    private static final int FULL_ID = 16;
    private static final int USERNAME_ID = 17;
    private static final int ENTITY_HEIGHT_ID = 18;
    public static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 6;
    public static final int SCAN_INTERVAL = 5;
    public EntityLivingBase caughtEntity;
    private EntityAITasks caughtEntityAITTasks;
    private float caughtEntityMoveSpeed;
    public int trapPosX;
    public int trapPosY;
    public int trapPosZ;
    public int trapHeight;
    public ForgeDirection trapDirection;
    private boolean prevFull;
    private int scanCoolDown;

    public EntitySnareTrap(final World par1World) {
        super(par1World);
        this.scanCoolDown = 5;
        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
    }

    public EntitySnareTrap(final World world, final int x, final int y, final int z, final int height,
        final ForgeDirection dir) {
        this(world);
        this.trapPosX = x;
        this.trapPosY = y;
        this.trapPosZ = z;
        this.trapHeight = height;
        this.trapDirection = dir;
        this.setSize(0.6f, height - 0.5f);
        this.setLocationAndAngles(x + 0.5, (double) y, z + 0.5, 0.0f, 0.0f);
    }

    public Block getHookBlock(final World world) {
        return world.getBlock(
            this.trapPosX - this.trapDirection.offsetX,
            this.trapPosY + this.trapHeight - 1,
            this.trapPosZ - this.trapDirection.offsetZ);
    }

    public Block getBottomBlock(final World world) {
        return world.getBlock(this.trapPosX, this.trapPosY - 1, this.trapPosZ);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean canBePushed() {
        return false;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.worldObj.isRemote) {
            final boolean newFull = this.getFull();
            if (this.prevFull != newFull) {
                this.prevFull = newFull;
                if (newFull) {
                    this.setSize(0.6f, this.trapHeight - this.getEntityHeight() - 0.5f);
                } else {
                    this.setSize(0.6f, this.trapHeight - 0.5f);
                }
                this.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0f, 0.0f);
            }
            final EntityPlayer player = this.getClientPlayer();
            if (this.getUsername()
                .equals(
                    player.getGameProfile()
                        .getName())) {
                player.setLocationAndAngles(
                    this.trapPosX + 0.5,
                    (double) this.trapPosY,
                    this.trapPosZ + 0.5,
                    player.rotationYaw,
                    player.rotationPitch);
            }
            return;
        }
        if (this.caughtEntity == null) {
            --this.scanCoolDown;
            if (this.scanCoolDown == 0) {
                this.scanCoolDown = 5;
                final List<Entity> ents = this.worldObj
                    .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.0, 5.0, 5.0));
                for (final Entity obj : ents) {
                    final Entity ent = obj;
                    if (ent instanceof EntityLivingBase && ((EntityLivingBase) ent).isEntityAlive()
                        && ent.posX >= this.trapPosX
                        && ent.posX <= this.trapPosX + 1
                        && ent.posZ >= this.trapPosZ
                        && ent.posZ <= this.trapPosZ + 1
                        && Math.abs(this.trapPosY - ent.posY) < 0.01) {
                        boolean alreadyTrapped = false;
                        for (final Object trapObj : ents) {
                            if (!(trapObj instanceof EntitySnareTrap)) {
                                continue;
                            }
                            final EntitySnareTrap trap = (EntitySnareTrap) trapObj;
                            if (trap.caughtEntity == ent) {
                                alreadyTrapped = true;
                                break;
                            }
                        }
                        if (alreadyTrapped) {
                            continue;
                        }
                        this.catchEntity((EntityLivingBase) ent);
                    }
                }
            }
        } else if (!this.caughtEntity.isEntityAlive()) {
            this.releaseEntity();
        } else {
            this.caughtEntity.setLocationAndAngles(
                this.trapPosX + 0.5,
                (double) this.trapPosY,
                this.trapPosZ + 0.5,
                this.caughtEntity.rotationYaw,
                this.caughtEntity.rotationPitch);
            this.caughtEntity.motionX = 0.0;
            this.caughtEntity.motionY = 0.0;
            this.caughtEntity.motionZ = 0.0;
        }
        if (this.worldObj.getBlock(
            this.trapPosX - this.trapDirection.offsetX,
            this.trapPosY + this.trapHeight - 1,
            this.trapPosZ - this.trapDirection.offsetZ) != this.getHookBlock(this.worldObj)
            || this.worldObj.getBlock(this.trapPosX, this.trapPosY - 1, this.trapPosZ)
                != this.getBottomBlock(this.worldObj)) {
            if (this.caughtEntity != null) {
                this.releaseEntity();
            }
            this.dropItem(TCItemRegistry.snareTrap, 1);
            this.setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getClientPlayer() {
        return (EntityPlayer) Minecraft.getMinecraft().thePlayer;
    }

    public void catchEntity(final EntityLivingBase ent) {
        this.caughtEntity = ent;
        this.setFull(true);
        this.setEntityHeight(ent.height + ent.yOffset);
        this.worldObj
            .playSoundAtEntity((Entity) this, "random.bow", 1.0f, 1.0f / (this.rand.nextFloat() * 0.4f + 1.2f));
        this.setSize(0.6f, this.trapHeight - ent.height - ent.yOffset - 0.5f);
        this.setLocationAndAngles(
            this.trapPosX + 0.5,
            (double) (this.trapPosY + ent.height + ent.yOffset),
            this.trapPosZ + 0.5,
            0.0f,
            0.0f);
        if (ent instanceof EntityPlayer) {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("packetCommand", "effectAdd");
            nbt.setInteger("effectID", 0);
            nbt.setInteger("effectTime", -1);
            Tropicraft.eventChannel
                .sendTo(PacketHelper.getNBTPacket(nbt, Tropicraft.eventChannelName), (EntityPlayerMP) ent);
        } else if (ent instanceof EntityLiving) {
            EffectHelper.addEntry(ent, -1);
        }
    }

    public void releaseEntity() {
        this.scanCoolDown = 5;
        this.setFull(false);
        this.setUsername("");
        this.setSize(0.6f, this.trapHeight - 0.5f);
        this.setLocationAndAngles(this.trapPosX + 0.5, (double) this.trapPosY, this.trapPosZ + 0.5, 0.0f, 0.0f);
        if (this.caughtEntity instanceof EntityPlayer) {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("packetCommand", "effectRemove");
            Tropicraft.eventChannel.sendTo(
                PacketHelper.getNBTPacket(nbt, Tropicraft.eventChannelName),
                (EntityPlayerMP) this.caughtEntity);
        } else {
            EffectHelper.removeEntry(this.caughtEntity);
        }
        this.caughtEntity = null;
    }

    public boolean attackEntityFrom(final DamageSource source, final float dmg) {
        if (this.worldObj.isRemote) {
            return true;
        }
        this.worldObj.playSoundAtEntity((Entity) this, "note.harp", 1.0f, 2.0f - this.rand.nextFloat() / 2.0f);
        if (this.caughtEntity != null && source.getEntity() == this.caughtEntity && this.rand.nextFloat() > 0.1f) {
            return false;
        }
        if (this.caughtEntity != null) {
            this.releaseEntity();
        }
        final Entity sourceEnt = source.getEntity();
        if (!(sourceEnt instanceof EntityPlayer) || !((EntityPlayer) sourceEnt).capabilities.isCreativeMode) {
            this.dropItem(TCItemRegistry.snareTrap, 1);
        }
        this.setDead();
        return true;
    }

    protected void entityInit() {
        this.dataWatcher.addObject(FULL_ID, (byte) 0);
        this.dataWatcher.addObject(USERNAME_ID, "");
        this.dataWatcher.addObject(ENTITY_HEIGHT_ID, 0);
    }

    public void setEntityHeight(final float height) {
        this.dataWatcher.updateObject(ENTITY_HEIGHT_ID, (int) (height * 100.0f));
    }

    public float getEntityHeight() {
        return this.dataWatcher.getWatchableObjectInt(18) / 100.0f;
    }

    public void setUsername(final String username) {
        this.dataWatcher.updateObject(USERNAME_ID, username);
    }

    public String getUsername() {
        return this.dataWatcher.getWatchableObjectString(17);
    }

    public void setFull(final boolean full) {
        this.dataWatcher.updateObject(FULL_ID, (byte) (full ? 1 : 0));
    }

    public boolean getFull() {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    protected void readEntityFromNBT(final NBTTagCompound var1) {
        this.trapHeight = var1.getInteger("Height");
        this.trapDirection = ForgeDirection.getOrientation((int) var1.getByte("Direction"));
        this.trapPosX = var1.getInteger("XPos");
        this.trapPosY = var1.getInteger("YPos");
        this.trapPosZ = var1.getInteger("ZPos");
    }

    protected void writeEntityToNBT(final NBTTagCompound var1) {
        var1.setInteger("Height", this.trapHeight);
        var1.setByte("Direction", (byte) this.trapDirection.ordinal());
        var1.setInteger("XPos", this.trapPosX);
        var1.setInteger("YPos", this.trapPosY);
        var1.setInteger("ZPos", this.trapPosZ);
    }

    public void writeSpawnData(final ByteBuf data) {
        data.writeInt(this.trapPosX);
        data.writeInt(this.trapPosY);
        data.writeInt(this.trapPosZ);
        data.writeInt(this.trapHeight);
        data.writeByte(this.trapDirection.ordinal());
    }

    public void readSpawnData(final ByteBuf data) {
        this.trapPosX = data.readInt();
        this.trapPosY = data.readInt();
        this.trapPosZ = data.readInt();
        this.trapHeight = data.readInt();
        this.trapDirection = ForgeDirection.getOrientation((int) data.readByte());
        this.setSize(0.6f, this.trapHeight - 0.5f);
        this.setLocationAndAngles(this.trapPosX + 0.5, (double) this.trapPosY, this.trapPosZ + 0.5, 0.0f, 0.0f);
    }
}
