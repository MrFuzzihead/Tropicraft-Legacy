package net.tropicraft.item.armor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.tropicraft.registry.TCItemRegistry;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFireArmor extends ItemTropicraftArmor {

    public ItemFireArmor(final ItemArmor.ArmorMaterial material, final int renderIndex, final int armorType) {
        super(material, renderIndex, armorType);
        this.setMaxDamage(300);
    }

    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {
        if (world.isRemote) {
            this.clientTick(player);
        } else {
            if (player.isBurning()) {
                player.extinguish();
            }
            final int factor = (int) (40.0 / (0.001 + this.getSunBrightness(world, 1.0f)));
            if (world.getWorldTime() % factor == 0L && world.canBlockSeeTheSky(
                MathHelper.floor_double(player.posX),
                MathHelper.floor_double(player.posY + 1.0),
                MathHelper.floor_double(player.posZ))) {
                itemStack.damageItem(-1, (EntityLivingBase) player);
            }
        }
    }

    private float getSunBrightness(final World world, final float par1) {
        final float f1 = world.getCelestialAngle(par1);
        float f2 = 1.0f - (MathHelper.cos(f1 * 3.1415927f * 2.0f) * 2.0f + 0.2f);
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        f2 = 1.0f - f2;
        f2 *= (float) (1.0 - world.getRainStrength(par1) * 5.0f / 16.0);
        f2 *= (float) (1.0 - world.getWeightedThunderStrength(par1) * 5.0f / 16.0);
        return f2 * 0.8f + 0.2f;
    }

    @Override
    public void damageArmor(final EntityLivingBase player, final ItemStack stack, final DamageSource source,
        final int damage, final int slot) {
        if (source == DamageSource.inFire || source == DamageSource.lava) {
            stack.damageItem(damage, player);
        }
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armor,
        final DamageSource source, final double damage, final int slot) {
        if (source == DamageSource.inFire || source == DamageSource.lava) {
            return new ISpecialArmor.ArmorProperties(10, 2.147483647E9, Integer.MAX_VALUE);
        }
        return new ISpecialArmor.ArmorProperties(10, 0.15, Integer.MAX_VALUE);
    }

    @SideOnly(Side.CLIENT)
    public static void getFXLayers() {
        Field field = null;
        try {
            field = EffectRenderer.class.getDeclaredField("fxLayers");
            field.setAccessible(true);
            ItemFireArmor.fxLayers = (List[]) field.get(
                FMLClientHandler.instance()
                    .getClient().effectRenderer);
        } catch (Exception ex3) {
            try {
                field = EffectRenderer.class.getDeclaredField("fxLayers");
                field.setAccessible(true);
                ItemFireArmor.fxLayers = (List[]) field.get(
                    FMLClientHandler.instance()
                        .getClient().effectRenderer);
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientTick(final EntityPlayer player) {
        if (ItemFireArmor.fxLayers == null) {
            getFXLayers();
        }
        float range = 0.2f;
        final float speed = 0.08f;
        final Random rand = new Random();
        final World worldRef = player.worldObj;
        int extraRand = 0;
        final double plSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
        if (plSpeed < 0.10000000149011612) {
            extraRand = 7;
        }
        if (this == TCItemRegistry.fireBoots) {
            boolean onLava = false;
            boolean inLava = false;
            final int x = 0;
            final int z = 0;
            if (player.motionY < 0.0) {
                final Block block = player.worldObj.getBlock(
                    MathHelper.floor_double(player.posX + x),
                    MathHelper.floor_double(player.posY - 2.0),
                    MathHelper.floor_double(player.posZ + z));
                if (block != null && block.getMaterial() == Material.lava) {
                    onLava = true;
                }
            }
            final Block block2 = player.worldObj.getBlock(
                MathHelper.floor_double(player.posX + x),
                MathHelper.floor_double(player.posY - 1.0),
                MathHelper.floor_double(player.posZ + z));
            if (block2 != null && block2.getMaterial() == Material.lava) {
                inLava = true;
            }
            if (onLava && !inLava) {
                player.motionY = 0.0;
                player.onGround = true;
            }
            if (inLava && plSpeed < 0.4) {
                player.motionX *= 1.5;
                player.motionY *= 1.5;
                player.motionZ *= 1.5;
            }
            final float look = (float) (player.worldObj.getWorldTime() * (10 + (onLava ? 10 : 0)));
            final double dist = 1.0;
            final double gatherX = player.posX;
            final double gatherY = player.boundingBox.minY;
            final double gatherZ = player.posZ;
            double motionX = rand.nextFloat() * speed - speed / 2.0f;
            double motionZ = rand.nextFloat() * speed - speed / 2.0f;
            for (int i = 0; i < 1 + (onLava ? 5 : 0); ++i) {
                motionX = -Math.sin(look / 180.0f * 3.1415927f) * Math.cos(0.0) * (speed + 0.1 * rand.nextDouble());
                motionZ = Math.cos(look / 180.0f * 3.1415927f) * Math.cos(0.0) * (speed + 0.1 * rand.nextDouble());
                String particle = "flame";
                if (rand.nextInt(2) == 0) {
                    particle = "smoke";
                }
                if (onLava || rand.nextInt(1 + extraRand) == 0) {
                    player.worldObj.spawnParticle(
                        particle,
                        gatherX + (rand.nextFloat() * range - range / 2.0f),
                        gatherY + (rand.nextFloat() * range - range / 2.0f),
                        gatherZ + (rand.nextFloat() * range - range / 2.0f),
                        player.motionX + motionX,
                        0.009999999776482582,
                        player.motionZ + motionZ);
                    player.worldObj.spawnParticle(
                        particle,
                        gatherX + (rand.nextFloat() * range - range / 2.0f),
                        gatherY + (rand.nextFloat() * range - range / 2.0f),
                        gatherZ + (rand.nextFloat() * range - range / 2.0f),
                        player.motionX - motionX,
                        0.009999999776482582,
                        player.motionZ - motionZ);
                }
            }
        } else if (this == TCItemRegistry.fireLeggings) {
            String particle2 = "flame";
            if (rand.nextInt(2) == 0) {
                particle2 = "smoke";
            }
            if (rand.nextInt(3 + extraRand) == 0) {
                player.worldObj.spawnParticle(
                    particle2,
                    player.posX + (rand.nextFloat() * range - range / 2.0f),
                    player.posY - 0.800000011920929 + (rand.nextFloat() * range - range / 2.0f),
                    player.posZ + (rand.nextFloat() * range - range / 2.0f),
                    (double) (rand.nextFloat() * speed - speed / 2.0f),
                    -0.05000000074505806,
                    (double) (rand.nextFloat() * speed - speed / 2.0f));
            }
        } else if (this == TCItemRegistry.fireChestplate) {
            final float look2 = -180.0f;
            final double dist2 = 0.5;
            final double gatherX2 = player.posX + -Math.sin((player.rotationYaw + look2) / 180.0f * 3.1415927f)
                * Math.cos(player.rotationPitch / 180.0f * 3.1415927f)
                * dist2;
            final double gatherZ2 = player.posZ + Math.cos((player.rotationYaw + look2) / 180.0f * 3.1415927f)
                * Math.cos(player.rotationPitch / 180.0f * 3.1415927f)
                * dist2;
            String particle3 = "flame";
            if (rand.nextInt(2) == 0) {
                particle3 = "smoke";
            }
            if (rand.nextInt(3 + extraRand) == 0) {
                player.worldObj.spawnParticle(
                    particle3,
                    gatherX2 + (rand.nextFloat() * range - range / 2.0f),
                    player.posY - 0.4000000059604645 + (rand.nextFloat() * range - range / 2.0f),
                    gatherZ2 + (rand.nextFloat() * range - range / 2.0f),
                    (double) (rand.nextFloat() * speed - speed / 2.0f),
                    -0.009999999776482582,
                    (double) (rand.nextFloat() * speed - speed / 2.0f));
            }
        } else if (this == TCItemRegistry.fireHelmet) {
            final float look2 = -180.0f;
            final double dist2 = 0.5;
            range = 2.0f;
            final double gatherX2 = player.posX + -Math.sin((player.rotationYaw + look2) / 180.0f * 3.1415927f)
                * Math.cos(player.rotationPitch / 180.0f * 3.1415927f)
                * dist2;
            final double gatherZ2 = player.posZ + Math.cos((player.rotationYaw + look2) / 180.0f * 3.1415927f)
                * Math.cos(player.rotationPitch / 180.0f * 3.1415927f)
                * dist2;
            String particle3 = "flame";
            if (rand.nextInt(2) == 0) {
                particle3 = "smoke";
            }
            if (rand.nextInt(2) == 0) {
                player.worldObj.spawnParticle(
                    particle3,
                    gatherX2 + (rand.nextFloat() * range - range / 2.0f),
                    player.posY + 0.699999988079071,
                    gatherZ2 + (rand.nextFloat() * range - range / 2.0f),
                    (double) (rand.nextFloat() * speed - speed / 2.0f),
                    -0.009999999776482582,
                    (double) (rand.nextFloat() * speed - speed / 2.0f));
            }
            if (ItemFireArmor.fxLayers != null) {
                for (int layer = 0; layer < 4; ++layer) {
                    for (int j = 0; j < ItemFireArmor.fxLayers[layer].size(); ++j) {
                        final EntityFX entity1 = (EntityFX) ItemFireArmor.fxLayers[layer].get(j);
                        if (entity1 instanceof EntityFlameFX && player.getDistanceToEntity((Entity) entity1) < 4.0f
                            && entity1.posY > player.posY) {
                            if (player.getDistanceToEntity((Entity) entity1) < 2.0f) {}
                            if (player.getDistanceToEntity((Entity) entity1) < 2.0f || rand.nextInt(5) == 0) {
                                this.moveEnt((Entity) entity1, (Entity) player, false);
                            }
                        }
                    }
                }
            }
        }
    }

    public void moveEnt(final Entity ent, final Entity center, final boolean shield) {
        final float look = 0.0f;
        final double dist = 0.10000000149011612;
        final double gatherX = center.posX + -Math.sin((center.rotationYaw + look) / 180.0f * 3.1415927f)
            * Math.cos(center.rotationPitch / 180.0f * 3.1415927f)
            * dist;
        final double gatherY = center.posY + 0.8
            + -MathHelper.sin(center.rotationPitch / 180.0f * 3.1415927f) * dist
            - 0.0;
        final double gatherZ = center.posZ + Math.cos((center.rotationYaw + look) / 180.0f * 3.1415927f)
            * Math.cos(center.rotationPitch / 180.0f * 3.1415927f)
            * dist;
        final double entDist = ent.getDistance(gatherX, gatherY, gatherZ);
        if (entDist > -1.0) {
            final double vecX = gatherX - ent.posX;
            final double vecY = gatherY - ent.posY;
            final double vecZ = gatherZ - ent.posZ;
            final double var1 = 1.0;
            final double var2 = MathHelper.sqrt_double(vecX * vecX + vecY * vecY + vecZ * vecZ);
            final int maxDist = 10;
            int adjDist = (int) entDist - maxDist;
            if (adjDist < 0) {
                adjDist = 10;
            }
            final double newspeed;
            double speed = newspeed = 0.02;
            float pitch = (float) (Math.atan2(vecX, vecZ) * 180.0 / 3.1415927410125732);
            float angle;
            float f;
            for (f = (angle = (float) (Math.atan2(vecZ, vecX) * 180.0
                / 3.1415927410125732)), angle += 180.0f, angle = f; angle < 0.0f; angle += 360.0f) {}
            while (angle >= 360.0f) {
                angle -= 360.0f;
            }
            for (pitch = pitch; pitch < 0.0f; pitch += 180.0f) {}
            while (pitch >= 180.0f) {
                pitch -= 180.0f;
            }
            if (!shield) {
                angle -= 15.0f;
            } else if (shield) {
                angle -= 40.0f;
                speed = 0.03;
                if (ent.worldObj.rand.nextInt(5) == 0) {
                    angle += 20.0f;
                    speed = 0.05999999865889549;
                }
            }
            final float rad_angle = angle * 0.01745329f;
            final float rad_pitch = pitch * 0.01745329f * 2.0f;
            final float uhh = 1.0f;
            final float newY = uhh * (float) Math.sin(rad_pitch);
            float projection = uhh * (float) Math.cos(rad_pitch);
            projection = 1.0f;
            final float newX = projection * (float) Math.cos(rad_angle);
            final float newZ = projection * (float) Math.sin(rad_angle);
            final float newVecX = newX / uhh;
            final float newVecY = newY / uhh;
            final float newVecZ = newZ / uhh;
            ent.motionY += vecY / var2 * speed;
            ent.motionX += newVecX * speed;
            ent.motionZ += newVecZ * speed;
            if (shield) {
                if (shield && ent.worldObj.rand.nextInt(10) == 0) {
                    ent.motionY += 0.05000000074505806;
                }
            }
        }
    }
}
