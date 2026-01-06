package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenCoffeePlant extends TCGenBase {

    public WorldGenCoffeePlant(final World world, final Random rand) {
        super(world, rand);
    }

    public boolean generate(final int x, final int y, final int z) {
        final int nx = generateRandomOffset(x);
        final int nz = generateRandomOffset(z);

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

    private int generateRandomOffset(int value) {
        int offset = this.rand.nextInt(8) - this.rand.nextInt(8);
        return Math.min(Math.max(value + offset, 0), 15);
    }

    private boolean isValidLocation(int x, int y, int z) {
        return this.worldObj.isAirBlock(x, y, z) && this.worldObj.getBlock(x, y - 1, z) == Blocks.grass;
    }

    private ForgeDirection findViableDirection(int x, int y, int z) {
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
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
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
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
        for (final ForgeDirection surroundingDir : ForgeDirection.VALID_DIRECTIONS) {
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
        Chunk chunk = worldObj.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (!chunk.isChunkLoaded) {
            return;
        }

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
}
