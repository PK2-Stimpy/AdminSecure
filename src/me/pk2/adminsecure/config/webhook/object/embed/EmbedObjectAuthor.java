package me.pk2.adminsecure.config.webhook.object.embed;

public class EmbedObjectAuthor implements EmbedObject {
    public String author, url, imageUrl;
    public EmbedObjectAuthor(String author, String url, String imageUrl) {
        this.author = author;
        this.url = url;
        this.imageUrl = imageUrl;
    }
}