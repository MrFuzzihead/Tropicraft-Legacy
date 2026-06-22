package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropiVoronoiZoom extends GenLayerTropicraft {

    public Mode zoomMode;

    public GenLayerTropiVoronoiZoom(final long seed, final GenLayerTropicraft parent, final Mode zoomMode) {
        super(seed);
        super.parent = parent;
        this.zoomMode = zoomMode;
        this.setZoom(1);
    }

    public int[] getInts(int x, int y, final int width, final int length) {
        final int randomResolution = 1024;
        final double half = 0.5;
        final double almostTileSize = 3.6;
        final double tileSize = 4.0;
        x -= 2;
        y -= 2;
        final int scaledX = x >> 2;
        final int scaledY = y >> 2;
        final int scaledWidth = (width >> 2) + 2;
        final int scaledLength = (length >> 2) + 2;
        final int[] parentValues = this.parent.getInts(scaledX, scaledY, scaledWidth, scaledLength);
        final int bitshiftedWidth = scaledWidth - 1 << 2;
        final int bitshiftedLength = scaledLength - 1 << 2;
        final int[] aint1 = IntCache.getIntCache(bitshiftedWidth * bitshiftedLength);
        for (int j = 0; j < scaledLength - 1; ++j) {
            int i = 0;
            int baseValue = parentValues[i + 0 + (j + 0) * scaledWidth];
            int advancedValueJ = parentValues[i + 0 + (j + 1) * scaledWidth];
            while (i < scaledWidth - 1) {
                this.initChunkSeed((long) (i + scaledX << 2), (long) (j + scaledY << 2));
                final double offsetY = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                final double offsetX = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed((long) (i + scaledX + 1 << 2), (long) (j + scaledY << 2));
                final double offsetYY = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final double offsetXY = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed((long) (i + scaledX << 2), (long) (j + scaledY + 1 << 2));
                final double offsetYX = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                final double offsetXX = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                this.initChunkSeed((long) (i + scaledX + 1 << 2), (long) (j + scaledY + 1 << 2));
                final double offsetYXY = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final double offsetXXY = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final int advancedValueI = parentValues[i + 1 + (j + 0) * scaledWidth] & 0xFF;
                final int advancedValueIJ = parentValues[i + 1 + (j + 1) * scaledWidth] & 0xFF;
                for (int innerX = 0; innerX < 4; ++innerX) {
                    int index = ((j << 2) + innerX) * bitshiftedWidth + (i << 2);
                    for (int innerY = 0; innerY < 4; ++innerY) {
                        double baseDistance = 0.0;
                        double distanceY = 0.0;
                        double distanceX = 0.0;
                        double distanceXY = 0.0;
                        switch (this.zoomMode) {
                            case CARTESIAN: {
                                baseDistance = (innerX - offsetX) * (innerX - offsetX)
                                    + (innerY - offsetY) * (innerY - offsetY);
                                distanceY = (innerX - offsetXY) * (innerX - offsetXY)
                                    + (innerY - offsetYY) * (innerY - offsetYY);
                                distanceX = (innerX - offsetXX) * (innerX - offsetXX)
                                    + (innerY - offsetYX) * (innerY - offsetYX);
                                distanceXY = (innerX - offsetXXY) * (innerX - offsetXXY)
                                    + (innerY - offsetYXY) * (innerY - offsetYXY);
                                break;
                            }
                            case MANHATTAN: {
                                baseDistance = Math.abs(innerX - offsetX) + Math.abs(innerY - offsetY);
                                distanceY = Math.abs(innerX - offsetXY) + Math.abs(innerY - offsetYY);
                                distanceX = Math.abs(innerX - offsetXX) + Math.abs(innerY - offsetYX);
                                distanceXY = Math.abs(innerX - offsetXXY) + Math.abs(innerY - offsetYXY);
                                break;
                            }
                            default: {
                                baseDistance = (innerX - offsetX) * (innerX - offsetX)
                                    + (innerY - offsetY) * (innerY - offsetY);
                                distanceY = (innerX - offsetXY) * (innerX - offsetXY)
                                    + (innerY - offsetYY) * (innerY - offsetYY);
                                distanceX = (innerX - offsetXX) * (innerX - offsetXX)
                                    + (innerY - offsetYX) * (innerY - offsetYX);
                                distanceXY = (innerX - offsetXXY) * (innerX - offsetXXY)
                                    + (innerY - offsetYXY) * (innerY - offsetYXY);
                                break;
                            }
                        }
                        if (baseDistance < distanceY && baseDistance < distanceX && baseDistance < distanceXY) {
                            aint1[index++] = baseValue;
                        } else if (distanceY < baseDistance && distanceY < distanceX && distanceY < distanceXY) {
                            aint1[index++] = advancedValueI;
                        } else if (distanceX < baseDistance && distanceX < distanceY && distanceX < distanceXY) {
                            aint1[index++] = advancedValueJ;
                        } else {
                            aint1[index++] = advancedValueIJ;
                        }
                    }
                }
                baseValue = advancedValueI;
                advancedValueJ = advancedValueIJ;
                ++i;
            }
        }
        final int[] aint2 = IntCache.getIntCache(width * length);
        for (int i = 0; i < length; ++i) {
            System.arraycopy(aint1, (i + (y & 0x3)) * bitshiftedWidth + (x & 0x3), aint2, i * width, width);
        }
        return aint2;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
        this.parent.setZoom(zoom * 4);
    }

    public enum Mode {
        CARTESIAN,
        MANHATTAN;
    }
}
