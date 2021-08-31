package me.pk2.adminsecure.config.webhook.object.embed;

public class EmbedObjectFooter implements EmbedObject {
    public String footer, url;
    public EmbedObjectFooter(String footer, String url) {
        this.footer = footer;
        this.url = url;
    }
}