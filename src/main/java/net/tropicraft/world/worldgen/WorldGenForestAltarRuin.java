package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class WorldGenForestAltarRuin extends TCDirectionalGen {

    public WorldGenForestAltarRuin(final World world, final Random random) {
        super(world, random, random.nextInt(4));
    }

    public boolean generate(final int i, int j, final int k) {
        this.setOrigin(i, k);
        final int width = (this.rand.nextInt(2) + 3) * 2;
        final int length = this.rand.nextInt(6) + 10;
        final int halfWidth = width / 2;
        boolean hasGennedTunnel = false;

        int terrainHeight;
        for (int z = 0; z < length; ++z) {
            for (int x = 0; x < width; ++x) {
                terrainHeight = this.getTerrainHeightWithDir(x, z);
                if (terrainHeight < j) {
                    j = terrainHeight;
                }
            }
        }

        for (int y = j; this.worldObj.getBlock(i, y, k) != Blocks.air; ++y) {
            if (this.worldObj.getBlock(i, y, k) == TCBlockRegistry.tropicsWater) {
                return false;
            }
        }

        for (int z = 0; z < length; ++z) {
            int x = 0;
            while (x < width) {
                for (int y2 = 0; y2 < 4; ++y2) {
                    terrainHeight = j + y2;
                    if (this.worldObj.getBlock(x, terrainHeight, z) == TCBlockRegistry.logs) {
                        ++x;
                    } else if (this.rand.nextInt(4) != 0) {
                        this.placeBlockWithDir(x, terrainHeight, z, Blocks.air, 0);
                    }
                }

                terrainHeight = j;
                this.placeBlockWithDir(x, terrainHeight, z, TCBlockRegistry.logs, 1);

                if (z == 0) {
                    if (x == 0 || x == width - 1) {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.palmFence, 0);
                        this.placeBlockWithDir(x, j + 2, z, TCBlockRegistry.palmFence, 0);
                    } else {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.singleSlabs, 2);
                        this.placeBlockWithDir(x, j + 2, z, TCBlockRegistry.palmFence, 0);

                    }
                } else if (z == 1) {
                    if (x == 0 || x == width - 1) {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.palmFence, 0);
                    } else if (x == 1 || x == width - 2) {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.chunkOHead, 0);
                        this.placeBlockWithDir(x, j + 2, z, TCBlockRegistry.flowerPot, 0);
                        final TileEntityTropicraftFlowerPot pot = (TileEntityTropicraftFlowerPot) this
                            .getTEWithDir(x, j + 2, z);
                        if (pot != null) {
                            pot.setFlowerID((short) (this.rand.nextInt(13) + 1));
                        }
                    } else if (x == halfWidth - 1 || x == halfWidth) {
                        this.placeBlockWithDir(x, j, z, Blocks.netherrack, 0);
                        this.placeBlockWithDir(x, j + 1, z, Blocks.fire, 0);
                    } else {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.singleSlabs, 2);
                    }
                } else if (z == 2) {
                    if (x == 0 || x == width - 1) {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.palmFence, 0);
                    } else {
                        this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.singleSlabs, 2);
                    }
                } else if (z % 2 == 1 && (x == 0 || x == width - 1)) {
                    this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.palmFence, 0);
                } else if (x != 0 && x != width - 1 && !(x == halfWidth - 1 || x == halfWidth)) {
                    this.placeBlockWithDir(x, j + 1, z, TCBlockRegistry.palmStairs, this.dir);
                    if (!hasGennedTunnel) {
                        this.generateTunnel(x, j, z);
                        hasGennedTunnel = true;
                    }
                }
                x++;
            }
        }
        return true;
    }

    private void generateTunnel(int i, int j, int k) {
        final int depth = this.rand.nextInt(5) + 8;
        for (int y = 0; y < depth; ++y) {
            this.placeBlockWithDir(i, j - y, k, Blocks.air, 0);
        }
        j -= depth;
        final int length = this.rand.nextInt(20) + 30;
        int dir = this.rand.nextInt(4);
        for (int x = 0; x < length; ++x) {
            switch (dir) {
                case 0: {
                    ++i;
                    break;
                }
                case 1: {
                    ++k;
                    break;
                }
                case 2: {
                    --i;
                    break;
                }
                case 3: {
                    --k;
                    break;
                }
            }
            if (this.rand.nextInt(3) == 0) {
                j += this.rand.nextInt(3) - 1;
            }
            this.placeBlockWithDir(i, j, k, Blocks.air, 0);
            this.placeBlockWithDir(i, j + 1, k, Blocks.air, 0);
            this.placeBlockWithDir(i, j + 2, k, Blocks.air, 0);
            if (this.rand.nextInt(5) == 0) {
                dir = this.rand.nextInt(4);
            }
        }
        this.placeBlockWithDir(i, j, k, TCBlockRegistry.bambooChest, 0);
        final TileEntityBambooChest chest = (TileEntityBambooChest) this.getTEWithDir(i, j, k);
        if (chest != null) {
            chest.setInventorySlotContents(0, this.randLoot());
        }
    }

    public ItemStack randLoot() {
        final int picker = this.rand.nextInt(18);
        if (picker < 6) {
            return new ItemStack(TCBlockRegistry.bambooChute, this.rand.nextInt(20) + 1);
        }
        if (picker < 10) {
            return new ItemStack(TCItemRegistry.scale, this.rand.nextInt(3) + 1);
        }
        if (picker < 12) {
            return new ItemStack(TCBlockRegistry.thatchBundle, this.rand.nextInt(20) + 1);
        }
        if (picker < 14) {
            return new ItemStack(TCItemRegistry.cookedFrogLeg, this.rand.nextInt(4) + 1);
        }
        return new ItemStack(TCItemRegistry.blowGun, 1);
    }
}
