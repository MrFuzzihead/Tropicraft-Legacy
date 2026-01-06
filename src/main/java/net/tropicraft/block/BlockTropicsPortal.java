package net.tropicraft.block;

import java.util.*;

import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.*;
import net.tropicraft.registry.*;
import net.tropicraft.util.*;

import cpw.mods.fml.relauncher.*;

public class BlockTropicsPortal extends BlockFluidClassic {

    private static final int TIME_UNTIL_TELEPORT = 20;
    public int messageTick;

    public BlockTropicsPortal(final Fluid fluid, final Material material) {
        super(fluid, material);
        this.setCreativeTab((CreativeTabs) null);
        this.setTickRandomly(true);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0f);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", "tropicraft:", this.getActualName(super.getUnlocalizedName()));
    }

    protected String getActualName(final String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(46) + 1);
    }

    public void onEntityCollidedWithBlock(final World world, final int i, final int j, final int k,
        final Entity entity) {
        if (!world.isRemote && entity instanceof EntityPlayerMP) {
            final EntityPlayerMP player = (EntityPlayerMP) entity;
            entity.setAir(300);
            final EntityPlayerMP entityPlayerMP = player;
            ++entityPlayerMP.timeUntilPortal;
            if (player.timeUntilPortal > 20 && world.getBlockMetadata(i, j, k) == 1) {
                if (player.isPotionActive(Potion.confusion.id)) {
                    this.messageTick = 0;
                    player.timeUntilPortal = 0;
                    player.removePotionEffect(Potion.confusion.id);
                    TropicraftWorldUtils.teleportPlayer(player);
                } else {
                    ++this.messageTick;
                    player.timeUntilPortal = 0;
                    if (this.messageTick % 50 == 0) {
                        player.addChatMessage(
                            (IChatComponent) new ChatComponentText(
                                "You should drink a piña colada before teleporting!"));
                    }
                }
            }
        }
    }

    public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {}

    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y,
        final int z) {
        return false;
    }

    public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y,
        final int z, final double explosionX, final double explosionY, final double explosionZ) {
        return Float.MAX_VALUE;
    }

    public IIcon getIcon(final int side, final int meta) {
        return TCBlockRegistry.tropicsWater.getIcon(side, meta);
    }

    public void setBlockBoundsBasedOnState(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        if (iblockaccess.isAirBlock(i, j, k)) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess world, final int i, final int j, final int k,
        final int side) {
        final Material material = world.getBlock(i, j, k)
            .getMaterial();
        return material != this.blockMaterial && side == 1;
    }

    public int tickRate(final World par1World) {
        return 10;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int i, final int j, final int k, final Random random) {
        if (world.isRemote) {
            this.sparkle(world, i, j, k);
        }
    }

    @SideOnly(Side.CLIENT)
    private void sparkle(final World world, final int i, final int j, final int k) {
        final Random random = world.rand;
        final int maxCount = 2;
        if (world.getBlockMetadata(i, j, k) == 0 && world.isRemote) {
            for (int count = 0; count < maxCount; ++count) {
                world.spawnParticle(
                    "bubble",
                    i + random.nextDouble(),
                    j + random.nextDouble(),
                    k + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0);
            }
        }
        if (world.isAirBlock(i, j + 1, k) && world.isRemote) {
            for (int count = 0; count < maxCount; ++count) {
                world.spawnParticle("splash", i + random.nextDouble(), j + 0.9, k + random.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {}
}
