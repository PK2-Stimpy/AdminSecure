package me.pk2.adminsecure.config.webhook.object;

public class WebhookObjectTts implements WebhookObject {
    public boolean tts;
    public WebhookObjectTts(boolean tts) {
        this.tts = tts;
    }
}