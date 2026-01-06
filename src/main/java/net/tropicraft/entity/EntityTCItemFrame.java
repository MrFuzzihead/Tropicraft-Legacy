package net.tropicraft.entity;

import java.util.List;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.tropicraft.registry.*;

import cpw.mods.fml.common.registry.*;
import io.netty.buffer.*;

public class EntityTCItemFrame extends EntityItemFrame implements IEntityAdditionalSpawnData {

    private float itemDropChance;
    public int xPosition;
    public int yPosition;
    public int zPosition;

    public EntityTCItemFrame(final World world) {
        super(world);
        this.itemDropChance = 1.0f;
        this.setShouldDropContents(false);
    }

    public EntityTCItemFrame(final World par1World, final int par2, final int par3, final int par4, final int par5,
        final boolean shouldDropContents) {
        super(par1World, par2, par3, par4, par5);
        this.itemDropChance = 1.0f;
        this.xPosition = par2;
        this.yPosition = par3;
        this.zPosition = par4;
        this.setShouldDropContents(shouldDropContents);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte) 0);
    }

    public void onBroken(final Entity par1Entity) {
        if (!this.getShouldDropContents()) {
            this.entityDropItem(new ItemStack(TCItemRegistry.koaFrame), 0.0f);
        } else {
            this.entityDropItem(new ItemStack(TCItemRegistry.tropiFrame), 0.0f);
            ItemStack var1 = this.getDisplayedItem();
            if (var1 != null && this.rand.nextFloat() < this.itemDropChance) {
                var1 = var1.copy();
                var1.setItemFrame((EntityItemFrame) null);
                this.entityDropItem(var1, 0.0f);
            }
        }
    }

    public void writeEntityToNBT(final NBTTagCompound par1NBTTagCompound) {
        if (this.getDisplayedItem() != null) {
            par1NBTTagCompound.setTag(
                "Item",
                (NBTBase) this.getDisplayedItem()
                    .writeToNBT(new NBTTagCompound()));
            par1NBTTagCompound.setByte("ItemRotation", (byte) this.getRotation());
            par1NBTTagCompound.setFloat("ItemDropChance", this.itemDropChance);
            par1NBTTagCompound.setBoolean("ShouldDropContents", this.getShouldDropContents());
        }
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    public void readEntityFromNBT(final NBTTagCompound par1NBTTagCompound) {
        final NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Item");
        if (var2 != null && !var2.hasNoTags()) {
            this.setDisplayedItem(ItemStack.loadItemStackFromNBT(var2));
            this.setItemRotation((int) par1NBTTagCompound.getByte("ItemRotation"));
            this.setShouldDropContents(par1NBTTagCompound.getBoolean("ShouldDropContents"));
            if (par1NBTTagCompound.hasKey("ItemDropChance")) {
                this.itemDropChance = par1NBTTagCompound.getFloat("ItemDropChance");
            }
        }
        super.readEntityFromNBT(par1NBTTagCompound);
    }

    public boolean getShouldDropContents() {
        return this.getDataWatcher()
            .getWatchableObjectByte(16) == 1;
    }

    public void setShouldDropContents(final boolean par1) {
        this.dataWatcher.updateObject(16, (byte) (par1 ? 1 : 0));
    }

    public void onUpdate() {
        super.onUpdate();
    }

    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes((Entity) this, this.boundingBox)
            .isEmpty()) {
            return false;
        }
        final int var1 = Math.max(1, this.getWidthPixels() / 16);
        final int var2 = Math.max(1, this.getHeightPixels() / 16);
        int var3 = this.field_146063_b;
        int var4 = this.field_146064_c;
        int var5 = this.field_146062_d;
        if (this.hangingDirection == 2) {
            var3 = MathHelper.floor_double(this.posX - this.getWidthPixels() / 32.0f);
        }
        if (this.hangingDirection == 1) {
            var5 = MathHelper.floor_double(this.posZ - this.getWidthPixels() / 32.0f);
        }
        if (this.hangingDirection == 0) {
            var3 = MathHelper.floor_double(this.posX - this.getWidthPixels() / 32.0f);
        }
        if (this.hangingDirection == 3) {
            var5 = MathHelper.floor_double(this.posZ - this.getWidthPixels() / 32.0f);
        }
        var4 = MathHelper.floor_double(this.posY - this.getHeightPixels() / 32.0f);
        for (int var6 = 0; var6 < var1; ++var6) {
            for (int var7 = 0; var7 < var2; ++var7) {
                Material var8;
                Block blawk;
                if (this.hangingDirection != 2 && this.hangingDirection != 0) {
                    var8 = this.worldObj.getBlock(this.field_146063_b, var4 + var7, var5 + var6)
                        .getMaterial();
                    blawk = this.worldObj.getBlock(this.field_146063_b, var4 + var7, var5 + var6);
                } else {
                    var8 = this.worldObj.getBlock(var3 + var6, var4 + var7, this.field_146062_d)
                        .getMaterial();
                    blawk = this.worldObj.getBlock(var3 + var6, var4 + var7, this.field_146062_d);
                }
                if (!var8.isSolid() && blawk != null && blawk != TCBlockRegistry.bambooChute) {
                    return false;
                }
            }
        }
        final List<?> var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
        for (final Object var11 : var9) {
            if (var11 instanceof EntityHanging) {
                return false;
            }
        }
        return true;
    }

    public void writeSpawnData(final ByteBuf data) {
        data.writeInt(this.field_146063_b);
        data.writeInt(this.field_146064_c);
        data.writeInt(this.field_146062_d);
        data.writeByte(this.hangingDirection);
        data.writeBoolean(this.getShouldDropContents());
    }

    public void readSpawnData(final ByteBuf data) {
        this.field_146063_b = data.readInt();
        this.field_146064_c = data.readInt();
        this.field_146062_d = data.readInt();
        this.setDirection(data.readByte());
        this.setShouldDropContents(data.readBoolean());
    }
}
