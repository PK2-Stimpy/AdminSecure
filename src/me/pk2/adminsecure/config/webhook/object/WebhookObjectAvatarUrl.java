package me.pk2.adminsecure.config.webhook.object;

public class WebhookObjectAvatarUrl implements WebhookObject {
    public String url;
    public WebhookObjectAvatarUrl(String url) {
        this.url = url;
    }
}