package me.pk2.adminsecure.util;

import java.awt.*;

public class ColorUtil {
    public static Color getColorByName(String name) {
        try {
            return (Color)Color.class.getField(name.toUpperCase()).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}