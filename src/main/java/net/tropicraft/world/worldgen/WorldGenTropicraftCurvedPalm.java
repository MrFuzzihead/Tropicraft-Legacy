package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTropicraftCurvedPalm extends TCDirectionalGen {

    private static final int TOP_OFFSET = 3;
    private static final Block woodID;
    private static final Block leafID;

    public WorldGenTropicraftCurvedPalm(final World world, final Random rand) {
        super(world, rand);
    }

    public boolean generate(final int i, final int j, final int k) {
        final Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
        if (blockUnder != Blocks.sand) {
            return false;
        }
        final int height = 9 + this.rand.nextInt(3);
        final int dir = this.pickDirection(i, j, k);
        this.setDir(dir);
        this.setOrigin(i, k);
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < height; ++y) {
                if (this.getBlockWithDir(x, y + j, 0) != Blocks.air) {
                    return false;
                }
            }
        }
        for (int x = 0; x < 9; ++x) {
            for (int z = 0; z < 9; ++z) {
                for (int y2 = height - 3; y2 < height + 4; ++y2) {
                    if (this.getBlockWithDir(x + 3, y2 + j, z) != Blocks.air) {
                        return false;
                    }
                }
            }
        }
        int x = 0;
        for (int y = 0; y < height; ++y) {
            this.placeBlockWithDir(x, y + j, 0, WorldGenTropicraftCurvedPalm.woodID, 0);
            if (y == 0 || y == 1 || y == 3) {
                ++x;
                this.placeBlockWithDir(x, y + j, 0, WorldGenTropicraftCurvedPalm.woodID, 0);
            }
        }
        this.setOrigin(this.getActualXAt(3, 0), this.getActualZAt(3, 0));
        for (int y3 = 1; y3 < 5; ++y3) {
            if (y3 == 4) {
                this.placeBlockWithDir(1, y3 + j + height - 1, 0, WorldGenTropicraftCurvedPalm.leafID, 0);
            } else {
                this.placeBlockWithDir(0, y3 + j + height - 1, 0, WorldGenTropicraftCurvedPalm.leafID, 0);
            }
        }
        for (int curDir = 0; curDir < 4; ++curDir) {
            this.setDir(curDir);
            int y = height - 1;
            this.placeBlockWithDir(1, y - 1 + j, 1, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(2, y - 2 + j, 1, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(1, y - 2 + j, 2, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(2, y - 3 + j, 2, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(1, y + 1 + j, 1, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(2, y + 2 + j, 1, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(1, y + 2 + j, 2, WorldGenTropicraftCurvedPalm.leafID, 0);
            this.placeBlockWithDir(2, y + 3 + j, 2, WorldGenTropicraftCurvedPalm.leafID, 0);
            for (int x2 = 1; x2 < 5; ++x2) {
                if (x2 == 4) {
                    --y;
                }
                this.placeBlockWithDir(x2, y + j, 0, WorldGenTropicraftCurvedPalm.leafID, 0);
            }
        }
        return true;
    }

    public int findWater(final int i, final int j, final int k) {
        int iPos = 0;
        int iNeg = 0;
        int kPos = 0;
        int kNeg = 0;
        while (iPos < 10 && this.worldObj.getBlock(i + iPos, 62, k)
            .getMaterial() != Material.water) {
            ++iPos;
        }
        while (iNeg > -10 && this.worldObj.getBlock(i + iNeg, 62, k)
            .getMaterial() != Material.water) {
            --iNeg;
        }
        while (kPos < 10 && this.worldObj.getBlock(i, 62, k + kPos)
            .getMaterial() != Material.water) {
            ++kPos;
        }
        while (kNeg > -10 && this.worldObj.getBlock(i, 62, k + kNeg)
            .getMaterial() != Material.water) {
            --kNeg;
        }
        if (iPos < Math.abs(iNeg) && iPos < kPos && iPos < Math.abs(kNeg)) {
            return 2;
        }
        if (Math.abs(iNeg) < iPos && Math.abs(iNeg) < kPos && Math.abs(iNeg) < Math.abs(kNeg)) {
            return 3;
        }
        if (kPos < Math.abs(iNeg) && kPos < iPos && kPos < Math.abs(kNeg)) {
            return 0;
        }
        if (Math.abs(kNeg) < Math.abs(iNeg) && Math.abs(kNeg) < iPos && Math.abs(kNeg) < kPos) {
            return 1;
        }
        if (iPos < 10 && iPos == Math.abs(iNeg)) {
            return this.rand.nextInt(2) + 1;
        }
        if (iPos < 10 && iPos == kPos) {
            if (this.rand.nextInt(2) + 1 == 1) {
                return 2;
            }
            return 0;
        } else if (iPos < 10 && iPos == Math.abs(kNeg)) {
            if (this.rand.nextInt(2) + 1 == 1) {
                return 2;
            }
            return 1;
        } else if (kPos < 10 && Math.abs(iNeg) == kPos) {
            if (this.rand.nextInt(2) + 1 == 1) {
                return 3;
            }
            return 0;
        } else if (Math.abs(iNeg) < 10 && Math.abs(iNeg) == Math.abs(kNeg)) {
            if (this.rand.nextInt(2) + 1 == 1) {
                return 3;
            }
            return 1;
        } else {
            if (kPos >= 10 || kPos != Math.abs(kNeg)) {
                return -1;
            }
            if (this.rand.nextInt(2) + 1 == 1) {
                return 0;
            }
            return 1;
        }
    }

    public int pickDirection(final int i, final int j, final int k) {
        final int direction = this.findWater(i, j, k);
        if (direction != -1) {
            return direction;
        }
        return this.rand.nextInt(4) + 1;
    }

    static {
        woodID = (Block) TCBlockRegistry.logs;
        leafID = (Block) TCBlockRegistry.palmLeaves;
    }
}
