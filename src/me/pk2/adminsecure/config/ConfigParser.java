package me.pk2.adminsecure.config;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.config.webhook.ParsedWebhookObject;
import me.pk2.adminsecure.config.webhook.object.*;
import me.pk2.adminsecure.config.webhook.object.embed.*;
import me.pk2.adminsecure.util.ColorUtil;
import me.pk2.adminsecure.util.DiscordWebhook;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;
import java.util.logging.Logger;

public class ConfigParser {
    private static String parseNameCode(String string, String name, String code) { return string.replaceAll("%player_name%", name).replaceAll("%new_code%", code); }

    private static EmbedObject parseESection(ConfigurationSection section) {
        switch(section.getString("type")) {
            case "AUTHOR":
                return new EmbedObjectAuthor(
                        section.getString("content.author"),
                        section.getString("content.url"),
                        section.getString("content.imageUrl")
                );
            case "COLOR":
                return new EmbedObjectColor(ColorUtil.getColorByName(section.getString("content")));
            case "DESCRIPTION":
                return new EmbedObjectDescription(section.getString("content"));
            case "FIELD":
                return new EmbedObjectField(
                        section.getString("content.title"),
                        section.getString("content.content"),
                        section.getBoolean("content.inline")
                );
            case "FOOTER":
                return new EmbedObjectFooter(
                        section.getString("content.footer"),
                        section.getString("content.url")
                );
            case "IMAGE":
                return new EmbedObjectImage(section.getString("content"));
            case "THUMBNAIL":
                return new EmbedObjectThumbnail(section.getString("content"));
            case "TITLE":
                return new EmbedObjectTitle(section.getString("content"));
            case "URL":
                return new EmbedObjectUrl(section.getString("content"));
        }
        return null;
    }
    private static WebhookObject parseWSection(ConfigurationSection section) {
        switch(section.getString("type")) {
            case "AVATAR_URL":
                return (new WebhookObjectAvatarUrl(section.getString("content")));
            case "CONTENT":
                return (new WebhookObjectContent(section.getString("content")));
            case "EMBED": {
                Set<String> embedKeys = section.getConfigurationSection("content").getKeys(false);

                WebhookObjectEmbed objectEmbed = new WebhookObjectEmbed();
                for(int i = 0; i < embedKeys.size(); i++)
                    objectEmbed.embedObjects.add(parseESection(section.getConfigurationSection("content." + i)));
                return objectEmbed;
            }

            case "TTS":
                return (new WebhookObjectTts(section.getBoolean("content")));
            case "USERNAME":
                return (new WebhookObjectUsername(section.getString("content")));
        }

        return null;
    }

    public static DiscordWebhook parseDWebhook(DiscordWebhook webhook, ParsedWebhookObject object, String name, String code) {
        for(WebhookObject webhookObject : object.webhookObjects)
            if(webhookObject instanceof WebhookObjectAvatarUrl)
                webhook.setAvatarUrl(parseNameCode(((WebhookObjectAvatarUrl) webhookObject).url, name, code));
            else if(webhookObject instanceof WebhookObjectContent)
                webhook.setContent(parseNameCode(((WebhookObjectContent) webhookObject).content, name, code));
            else if(webhookObject instanceof WebhookObjectEmbed) {
                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                for (EmbedObject eObject : ((WebhookObjectEmbed) webhookObject).embedObjects)
                    if (eObject instanceof EmbedObjectAuthor)
                        embedObject.setAuthor(parseNameCode(((EmbedObjectAuthor) eObject).author, name, code), parseNameCode(((EmbedObjectAuthor) eObject).url, name, code), parseNameCode(((EmbedObjectAuthor) eObject).imageUrl, name, code));
                    else if(eObject instanceof EmbedObjectColor)
                        embedObject.setColor(((EmbedObjectColor) eObject).color);
                    else if(eObject instanceof EmbedObjectDescription)
                        embedObject.setDescription(parseNameCode(((EmbedObjectDescription) eObject).description, name, code));
                    else if(eObject instanceof EmbedObjectField)
                        embedObject.addField(parseNameCode(((EmbedObjectField) eObject).title, name, code), parseNameCode(((EmbedObjectField) eObject).content, name, code), ((EmbedObjectField) eObject).inline);
                    else if(eObject instanceof EmbedObjectFooter)
                        embedObject.setFooter(parseNameCode(((EmbedObjectFooter) eObject).footer, name, code), parseNameCode(((EmbedObjectFooter) eObject).url, name, code));
                    else if(eObject instanceof EmbedObjectImage)
                        embedObject.setImage(parseNameCode(((EmbedObjectImage) eObject).url, name, code));
                    else if(eObject instanceof EmbedObjectThumbnail)
                        embedObject.setThumbnail(parseNameCode(((EmbedObjectThumbnail) eObject).thumbnail, name, code));
                    else if(eObject instanceof EmbedObjectTitle)
                        embedObject.setTitle(parseNameCode(((EmbedObjectTitle) eObject).title, name, code));
                    else if(eObject instanceof EmbedObjectUrl)
                        embedObject.setUrl(parseNameCode(((EmbedObjectUrl) eObject).url, name, code));

                webhook.addEmbed(embedObject);
            } else if(webhookObject instanceof WebhookObjectTts)
                webhook.setTts(((WebhookObjectTts) webhookObject).tts);
            else if(webhookObject instanceof WebhookObjectUsername)
                webhook.setUsername(parseNameCode(((WebhookObjectUsername) webhookObject).username, name, code));
        return webhook;
    }

    public static void parse(FileConfiguration configuration) {
        Logger logger = AdminSecure.INSTANCE.getLogger();
        logger.info("Loading config:");

        /* */
        logger.info("discord_webhook... ");
        ConfigDefault.discord_webhook = configuration.getString("discord_webhook");
        /* MESSAGES */
        logger.info("messages... ");
        ConfigDefault.messages.prefix = configuration.getString("messages.prefix");
        ConfigDefault.messages.auth_wait = configuration.getString("messages.auth_wait");
        ConfigDefault.messages.auth_error = configuration.getString("messages.auth_error");
        ConfigDefault.messages.auth_request = configuration.getString("messages.auth_request");
        ConfigDefault.messages.auth_valid = configuration.getString("messages.auth_valid");
        ConfigDefault.messages.auth_invalid = configuration.getString("messages.auth_invalid");
        /* LOGIN_PERSISTENCE */
        logger.info("login_persistence... ");
        ConfigDefault.login_persistence.enabled = configuration.getBoolean("login_persistence.enabled");
        ConfigDefault.login_persistence.delay = configuration.getInt("login_persistence.delay");
        /* COMMANDS */
        logger.info("commands... ");
        ConfigDefault.commands.auth_valid = configuration.getStringList("commands.auth_valid").toArray(new String[0]);
        ConfigDefault.commands.auth_invalid = configuration.getStringList("commands.auth_invalid").toArray(new String[0]);
        /* SECURITY_CODE */
        logger.info("security_code... ");
        ConfigDefault.security_code.max_number = configuration.getInt("security_code.max_number");
        ConfigDefault.security_code.allowed_commands = configuration.getStringList("security_code.allowed_commands").toArray(new String[0]);
        /* SECURITY_CODE JOIN_CONFIG */
        ConfigDefault.security_code.join_config.apply_blindness = configuration.getBoolean("security_code.join_config.apply_blindness");
        ConfigDefault.security_code.join_config.play_sound = configuration.getBoolean("security_code.join_config.play_sound");
        /* SECURITY_CODE JOIN_CONFIG TRIGGERS */
        ConfigDefault.security_code.join_config.triggers.gamemode = configuration.getBoolean("security_code.join_config.triggers.gamemode");
        ConfigDefault.security_code.join_config.triggers.op = configuration.getBoolean("security_code.join_config.triggers.op");
        /* SECURITY_CODE JOIN_CONFIG TRIGGERS PERMISSIONS */
        ConfigDefault.security_code.join_config.triggers.permissions.enabled = configuration.getBoolean("security_code.join_config.triggers.permissions.enabled");
        ConfigDefault.security_code.join_config.triggers.permissions.perms = configuration.getStringList("security_code.join_config.triggers.permissions.perms").toArray(new String[0]);
        /* SECURITY_CODE RESPONSES */
        ParsedWebhookObject auth_request = new ParsedWebhookObject();
        Set<String> auth_request_keys = configuration.getConfigurationSection("security_code.responses.auth_request.content").getKeys(false);
        for(int i = 0; i < auth_request_keys.size(); i++)
            auth_request.webhookObjects.add(parseWSection(configuration.getConfigurationSection("security_code.responses.auth_request.content." + i)));

        ParsedWebhookObject auth_valid = new ParsedWebhookObject();
        Set<String> auth_valid_keys = configuration.getConfigurationSection("security_code.responses.auth_valid.content").getKeys(false);
        for(int i = 0; i < auth_valid_keys.size(); i++)
            auth_valid.webhookObjects.add(parseWSection(configuration.getConfigurationSection("security_code.responses.auth_valid.content." + i)));

        ParsedWebhookObject auth_invalid = new ParsedWebhookObject();
        Set<String> auth_invalid_keys = configuration.getConfigurationSection("security_code.responses.auth_invalid.content").getKeys(false);
        for(int i = 0; i < auth_invalid_keys.size(); i++)
            auth_invalid.webhookObjects.add(parseWSection(configuration.getConfigurationSection("security_code.responses.auth_invalid.content." + i)));

        ConfigDefault.security_code.responses.auth_request = auth_request;
        ConfigDefault.security_code.responses.auth_valid = auth_valid;
        ConfigDefault.security_code.responses.auth_invalid = auth_invalid;

        logger.info("Done loading everything!");
    }
}