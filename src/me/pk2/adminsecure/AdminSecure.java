package me.pk2.adminsecure;

import me.pk2.adminsecure.command.CommandHandler;
import me.pk2.adminsecure.config.ConfigParser;
import me.pk2.adminsecure.user.UserListener;
import me.pk2.adminsecure.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminSecure extends JavaPlugin {
    public static volatile AdminSecure INSTANCE;

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

        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        userManager = new UserManager();
        commandHandler = new CommandHandler();

        Bukkit.getPluginManager().registerEvents(userListener = new UserListener(), this);

        /* RUN JOIN PLAYER EVENTS JUST IN CASE OF /reload COMMAND */
        for(Player player : Bukkit.getOnlinePlayers()) {
            userManager.handleUserAdd(player);
            if(!userManager.containsUser(player.getName()))
                userListener.fullJoinSecurityCheck(player);
        }

        thread = new Thread(() -> {
            while(!getThread().isInterrupted()) {
                for (Player player : Bukkit.getOnlinePlayers())
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
        userManager = null;
        commandHandler = null;

        getThread().interrupt();
        while(getThread().isInterrupted()) {}
        getThread().stop();
    }
}