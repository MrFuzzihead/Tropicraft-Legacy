package net.tropicraft.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Covers {@link ColorHelper#alphaBlend}, the only part of the old {@code drinks/ColorMixer} anything actually called,
 * plus the getters it is built on.
 */
public class ColorHelperTest {

    @Test
    public void componentsAreExtractedAndPackedBack() {
        Assert.assertEquals(0x12, (int) (ColorHelper.getRed(0x123456) * 255.0f));
        Assert.assertEquals(0x34, (int) (ColorHelper.getGreen(0x123456) * 255.0f));
        Assert.assertEquals(0x56, (int) (ColorHelper.getBlue(0x123456) * 255.0f));
        Assert.assertEquals(0x123456, ColorHelper.getColor(0x12 / 255.0f, 0x34 / 255.0f, 0x56 / 255.0f));
    }

    @Test
    public void aZeroAlphaKeepsTheBackground() {
        Assert.assertEquals(0xFF0000, ColorHelper.alphaBlend(0xFF0000, 0x0000FF, 0.0f));
    }

    @Test
    public void aFullAlphaTakesTheForeground() {
        Assert.assertEquals(0x0000FF, ColorHelper.alphaBlend(0xFF0000, 0x0000FF, 1.0f));
    }

    @Test
    public void aHalfAlphaIsInBetween() {
        final int blended = ColorHelper.alphaBlend(0x000000, 0xFFFFFF, 0.5f);
        Assert.assertEquals(0x7F7F7F, blended);
    }

    @Test
    public void theResultCarriesNoAlphaChannel() {
        // It is an item colour, not a pixel: the high byte must stay clear even when the inputs have alpha set.
        final int blended = ColorHelper.alphaBlend(0x80FF0000, 0x800000FF, 0.5f);
        Assert.assertEquals(0, blended >>> 24);
    }
}
