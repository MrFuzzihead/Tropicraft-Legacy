package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenCoffeePlant extends TCGenBase {

    private static final ForgeDirection[] cardinalDirections;

    public WorldGenCoffeePlant(final World world, final Random rand) {
        super(world, rand);
    }

    public boolean generate(final int x, final int y, final int z) {
        final int nx = x + this.rand.nextInt(8) - this.rand.nextInt(8);
        final int nz = z + this.rand.nextInt(8) - this.rand.nextInt(8);

        if (!isValidLocation(nx, y, nz)) {
            return false;
        }

        ForgeDirection viableDirection = findViableDirection(nx, y, nz);

        if (viableDirection == ForgeDirection.UNKNOWN) {
            return false;
        }

        placeBlocks(nx, y, nz, viableDirection);

        return true;
    }

    private boolean isValidLocation(int x, int y, int z) {
        return this.worldObj.isAirBlock(x, y, z) && this.worldObj.getBlock(x, y - 1, z) == Blocks.grass;
    }

    private ForgeDirection findViableDirection(int x, int y, int z) {
        for (final ForgeDirection dir : WorldGenCoffeePlant.cardinalDirections) {
            final int neighborx = x + dir.offsetX;
            final int neighborz = z + dir.offsetZ;
            if (this.worldObj.getBlock(neighborx, y - 1, neighborz)
                .getMaterial() == Material.water) {
                return dir;
            }
        }
        return checkSurrounded(x, y, z);
    }

    private ForgeDirection checkSurrounded(int x, int y, int z) {
        for (final ForgeDirection dir : WorldGenCoffeePlant.cardinalDirections) {
            final int neighborx = x + dir.offsetX;
            final int neighborz = z + dir.offsetZ;
            if (this.worldObj.isAirBlock(neighborx, y, neighborz)
                && this.worldObj.getBlock(neighborx, y - 1, neighborz) == Blocks.grass
                && !this.worldObj.isAirBlock(neighborx, y - 2, neighborz)
                && isSurrounded(neighborx, y, neighborz)) {
                return dir;
            }
        }
        return ForgeDirection.UNKNOWN;
    }

    private boolean isSurrounded(int x, int y, int z) {
        for (final ForgeDirection surroundingDir : WorldGenCoffeePlant.cardinalDirections) {
            final int surroundingx = x + surroundingDir.offsetX;
            final int surroundingz = z + surroundingDir.offsetZ;
            if (!this.worldObj.isAirBlock(surroundingx, y, surroundingz)
                || this.worldObj.getBlock(surroundingx, y - 1, surroundingz) != Blocks.grass) {
                return false;
            }
        }
        return true;
    }

    private void placeBlocks(int x, int y, int z, ForgeDirection direction) {
        this.worldObj.setBlock(
            x + direction.offsetX,
            y - 1,
            z + direction.offsetZ,
            Blocks.water,
            0,
            WorldGenCoffeePlant.blockGenNotifyFlag);
        this.worldObj.setBlock(x, y - 1, z, Blocks.farmland, 7, WorldGenCoffeePlant.blockGenNotifyFlag);
        for (int i = 0; i < 3 && this.worldObj.isAirBlock(x, y + i, z); ++i) {
            this.worldObj.setBlock(x, y + i, z, TCBlockRegistry.coffeePlant, 6, WorldGenCoffeePlant.blockGenNotifyFlag);
        }
    }

    static {
        cardinalDirections = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH,
            ForgeDirection.WEST };
    }
}
