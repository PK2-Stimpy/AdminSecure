package me.pk2.adminsecure.config.webhook.object.embed;

public class EmbedObjectField implements EmbedObject {
    public String title, content;
    public boolean inline;
    public EmbedObjectField(String title, String content, boolean inline) {
        this.title = title;
        this.content = content;
        this.inline = inline;
    }
}