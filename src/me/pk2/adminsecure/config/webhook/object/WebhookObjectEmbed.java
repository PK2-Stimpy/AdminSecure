package me.pk2.adminsecure.config.webhook.object;

import me.pk2.adminsecure.config.webhook.object.embed.EmbedObject;

import java.util.ArrayList;

public class WebhookObjectEmbed implements WebhookObject {
    public ArrayList<EmbedObject> embedObjects;
    public WebhookObjectEmbed() {
        this.embedObjects = new ArrayList<>();
    }
}