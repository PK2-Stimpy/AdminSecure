package me.pk2.adminsecure.user;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.config.ConfigDefault;
import me.pk2.adminsecure.config.ConfigParser;
import me.pk2.adminsecure.util.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserManager {
    public volatile ArrayList<String> frozenPlayers;
    public volatile ArrayList<String> verifiedPlayers;
    public volatile ArrayList<String> eliminationProcessPlayers;
    private ArrayList<User> users;
    public UserManager() {
        this.users = new ArrayList<>();
        this.frozenPlayers = new ArrayList<>();
        this.verifiedPlayers = new ArrayList<>();
        this.eliminationProcessPlayers = new ArrayList<>();
    }

    public User retrieveUser(String name) {
        for(User user : users)
            if(user.playerName.equalsIgnoreCase(name))
                return user;
        return null;
    }
    public boolean containsUser(String name) { return retrieveUser(name) != null; }

    public void handleUserAdd(Player player, boolean bypassPermission) {
        if((!player.hasPermission("adminsecure.code-verification") && !bypassPermission) || frozenPlayers.contains(player.getName().toLowerCase()) || verifiedPlayers.contains(player.getName()) || !player.isValid())
            return;
        if(ConfigDefault.security_code.join_config.check_ip && AdminSecure.INSTANCE.ipWhitelister.get(player) != null)
            return;

        this.frozenPlayers.add(player.getName().toLowerCase());
        User user = new User(player);
        users.add(user);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigDefault.messages.prefix + ConfigDefault.messages.auth_wait));

        /* Send webhook */
        String name = player.getName();
        new Thread(() -> {
            int code = user.code;

            DiscordWebhook webhook = ConfigParser.parseDWebhook(new DiscordWebhook(ConfigDefault.discord_webhook), ConfigDefault.security_code.responses.auth_request, name, String.valueOf(code));
            boolean sentSuccess = false;
            try {
                webhook.execute();
                sentSuccess = true;
            } catch (IOException exception) {
                exception.printStackTrace();
                if (Bukkit.getPlayer(name) != null)
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigDefault.messages.prefix + ConfigDefault.messages.auth_error));
            }

            while(sentSuccess && Bukkit.getPlayer(name) != null && containsUser(name) && ConfigDefault.login_persistence.enabled) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigDefault.messages.prefix + ConfigDefault.messages.auth_request));
                try { Thread.sleep(ConfigDefault.login_persistence.delay); } catch (InterruptedException exception) { exception.printStackTrace(); }
            }
        }).start();

        new Thread(() -> {
            while(player.isValid()) {
                AdminSecure.INSTANCE.userListener.fullJoinSecurityCheck(player);
                try { Thread.sleep(50); } catch(InterruptedException exception) { exception.printStackTrace(); }
            }
        }).start();

        /* BLINDNESS */
        if(ConfigDefault.security_code.join_config.apply_blindness)
            Bukkit.getScheduler().runTask(AdminSecure.INSTANCE, () -> player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 9)));
        /* SOUND */
        if(ConfigDefault.security_code.join_config.play_sound)
            Bukkit.getScheduler().runTask(AdminSecure.INSTANCE, () -> player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 10, 1));
    }

    public void handleUserAdd(Player player) { handleUserAdd(player, false); }

    public void handleUserRemove(Player player) {
        this.frozenPlayers.remove(player.getName().toLowerCase());
        for(int i = 0; i < users.size(); i++)
            if(users.get(i).playerName.equalsIgnoreCase(player.getName()))
                users.remove(i);
    }
}