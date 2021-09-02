package me.pk2.adminsecure.user;

import me.pk2.adminsecure.config.ConfigDefault;
import me.pk2.adminsecure.util.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class User {
    public final String playerName;
    public final int code;

    public DiscordWebhook auth_request_object, auth_valid_object, auth_invalid_object = auth_valid_object = auth_request_object = null;

    public User(String playerName, int code) {
        this.playerName = playerName.toLowerCase();
        this.code = code;
    }
    public User(String playerName) { this(playerName.toLowerCase(), new Random().nextInt(ConfigDefault.security_code.max_number)); }

    public User(Player player, int code) { this(player.getName(), code); }
    public User(Player player) { this(player.getName()); }
}