package net.tropicraft.util;

import java.util.ArrayList;

import net.minecraft.item.ItemDye;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

public class ColorHelper {

    private static BiMap<Integer, Integer> woolValues;
    private static ArrayList<Integer> colorValues;
    public static int DEFAULT_VALUE;
    public static final char COLOR_CHARACTER = '�';

    public static void init() {
        for (final int color : ItemDye.field_150922_c) {
            ColorHelper.colorValues.add(color);
        }
        ColorHelper.DEFAULT_VALUE = ColorHelper.colorValues.get(0);
        for (int i = 0; i < ColorHelper.colorValues.size(); ++i) {
            ColorHelper.woolValues.put(ItemDye.field_150922_c[i], i);
        }
    }

    public static String color(final int val) {
        return '�' + Integer.toHexString(val);
    }

    public static int getNumColors() {
        return ColorHelper.woolValues.keySet()
            .size();
    }

    public static int getColorFromDamage(final int damage) {
        return ColorHelper.colorValues.get(damage);
    }

    public static int getDamageFromColor(final int color) {
        return (int) ColorHelper.woolValues.get((Object) color);
    }

    public static float getRed(final int color) {
        return (color >> 16 & 0xFF) / 255.0f;
    }

    public static float getGreen(final int color) {
        return (color >> 8 & 0xFF) / 255.0f;
    }

    public static float getBlue(final int color) {
        return (color & 0xFF) / 255.0f;
    }

    public static int getColor(final float red, final float green, final float blue) {
        return (int) (red * 255.0f) << 16 | (int) (green * 255.0f) << 8 | (int) (blue * 255.0f);
    }

    public static int getColor(final float[] rgb) {
        return getColor(rgb[0], rgb[1], rgb[2]);
    }

    static {
        ColorHelper.woolValues = HashBiMap.create();
        ColorHelper.colorValues = Lists.newArrayList();
    }
}
