package me.pk2.adminsecure.config;

import me.pk2.adminsecure.config.webhook.ParsedWebhookObject;

public class ConfigDefault {
    public static String discord_webhook;
    public static class messages {
        public static String prefix;
        public static String
                auth_wait,
                auth_error,
                auth_request,
                auth_valid,
                auth_invalid;}
    public static class login_persistence {
        public static boolean enabled;
        public static int delay;}
    public static class commands {
        public static String[]
                auth_valid,
                auth_invalid;}
    public static class security_code {
        public static int max_number;
        public static String[] allowed_commands;
        public static class join_config {
            public static boolean
                    apply_blindness,
                    play_sound,
                    check_ip;
            public static class triggers {
                public static boolean
                        gamemode,
                        op;
                public static class permissions {
                    public static boolean enabled;
                    public static String[] perms;
                }
            }
        }
        public static class responses {
            public static ParsedWebhookObject
                    auth_request,
                    auth_valid,
                    auth_invalid;
        }
    }
}