package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftZoom extends GenLayerTropicraft {

    public GenLayerTropicraftZoom(final long par1, final GenLayerTropicraft parent) {
        super(par1);
        super.parent = parent;
        this.setZoom(1);
    }

    public int[] getInts(final int x, final int y, final int width, final int length) {
        final int x2 = x >> 1;
        final int y2 = y >> 1;
        final int width2 = (width >> 1) + 2;
        final int length2 = (length >> 1) + 2;
        final int[] parentMap = this.parent.getInts(x2, y2, width2, length2);
        final int reshiftedWidth = width2 - 1 << 1;
        final int reshiftedLength = length2 - 1 << 1;
        final int[] resultMap = IntCache.getIntCache(reshiftedWidth * reshiftedLength);
        for (int j = 0; j < length2 - 1; ++j) {
            int index = (j << 1) * reshiftedWidth;
            int i = 0;
            int backXY = parentMap[i + 0 + (j + 0) * width2];
            int backX = parentMap[i + 0 + (j + 1) * width2];
            while (i < width2 - 1) {
                this.initChunkSeed((long) (i + x2 << 1), (long) (j + y2 << 1));
                final int backY = parentMap[i + 1 + (j + 0) * width2];
                final int cur = parentMap[i + 1 + (j + 1) * width2];
                resultMap[index] = backXY;
                resultMap[index++ + reshiftedWidth] = this.selectRandom(new int[] { backXY, backX });
                resultMap[index] = this.selectRandom(new int[] { backXY, backY });
                resultMap[index++ + reshiftedWidth] = this.selectModeOrRandom(backXY, backY, backX, cur);
                backXY = backY;
                backX = cur;
                ++i;
            }
        }
        final int[] aint2 = IntCache.getIntCache(width * length);
        for (int index = 0; index < length; ++index) {
            System.arraycopy(resultMap, (index + (y & 0x1)) * reshiftedWidth + (x & 0x1), aint2, index * width, width);
        }
        return aint2;
    }

    public static GenLayerTropicraft magnify(final long seed, final GenLayerTropicraft parent, final int zoomAmount) {
        GenLayerTropicraft zoomLayers = parent;
        for (int zoom = 0; zoom < zoomAmount; ++zoom) {
            zoomLayers = new GenLayerTropicraftZoom(seed + zoom, zoomLayers);
        }
        return zoomLayers;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
        this.parent.setZoom(zoom * 2);
    }
}
