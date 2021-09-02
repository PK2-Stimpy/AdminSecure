package me.pk2.adminsecure;

import me.pk2.adminsecure.command.CommandHandler;
import me.pk2.adminsecure.config.ConfigParser;
import me.pk2.adminsecure.user.UserListener;
import me.pk2.adminsecure.user.UserManager;
import me.pk2.adminsecure.user.ip.IPWhitelister;
import me.pk2.adminsecure.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminSecure extends JavaPlugin {
    public static volatile AdminSecure INSTANCE;

    public IPWhitelister ipWhitelister;

    public UserManager userManager;
    public CommandHandler commandHandler;

    public UserListener userListener;

    private Thread thread;
    public Thread getThread() { return thread; }

    public void reloadConfig() {
        super.reloadConfig();
        ConfigParser.parse(getConfig());
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        Metrics metrics = new Metrics(this, 12666); /* 666 */

        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        ipWhitelister = new IPWhitelister();
        userManager = new UserManager();
        commandHandler = new CommandHandler();

        Bukkit.getPluginManager().registerEvents(userListener = new UserListener(), this);

        /* RUN JOIN PLAYER EVENTS JUST IN CASE OF /reload COMMAND */
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.isDead())
                return;

            userManager.handleUserAdd(player);
            if(!userManager.containsUser(player.getName()))
                userListener.fullJoinSecurityCheck(player);
        }

        thread = new Thread(() -> {
            while(!getThread().isInterrupted()) {
                for (Player player : Bukkit.getOnlinePlayers())
                    if(!player.isDead())
                        userListener.fullJoinSecurityCheck(player);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException exception) { exception.printStackTrace(); }
            }
        });
        thread.start();
    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers())
            userManager.handleUserRemove(player);
        ipWhitelister.shutdown();

        ipWhitelister = null;
        userManager = null;
        commandHandler = null;

        getThread().interrupt();
        while(getThread().isInterrupted()) {}
        getThread().stop();
    }
}