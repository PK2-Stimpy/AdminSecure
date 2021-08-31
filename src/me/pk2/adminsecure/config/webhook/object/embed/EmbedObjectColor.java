package me.pk2.adminsecure.config.webhook.object.embed;

import me.pk2.adminsecure.util.ColorUtil;

import java.awt.*;

public class EmbedObjectColor implements EmbedObject {
    public Color color;
    public EmbedObjectColor(Color color) { this.color = color; }
    public EmbedObjectColor(String color) { this.color = ColorUtil.getColorByName(color); }
}