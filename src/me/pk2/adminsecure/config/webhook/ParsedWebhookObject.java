package me.pk2.adminsecure.config.webhook;

import me.pk2.adminsecure.config.webhook.object.WebhookObject;

import java.util.ArrayList;

public class ParsedWebhookObject {
    public ArrayList<WebhookObject> webhookObjects;
    public ParsedWebhookObject() {
        this.webhookObjects = new ArrayList<>();
    }
}