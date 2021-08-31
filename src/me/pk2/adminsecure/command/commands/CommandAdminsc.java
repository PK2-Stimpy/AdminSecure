package me.pk2.adminsecure.command.commands;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.command.Command;
import me.pk2.adminsecure.command.exception.CommandErrorException;
import me.pk2.adminsecure.command.exception.CommandInvalidPermissionsException;
import me.pk2.adminsecure.command.exception.CommandInvalidSenderException;
import me.pk2.adminsecure.command.exception.CommandInvalidUsageException;
import me.pk2.adminsecure.config.ConfigDefault;
import me.pk2.adminsecure.config.ConfigParser;
import me.pk2.adminsecure.executor.ExecutorCentral;
import me.pk2.adminsecure.user.User;
import me.pk2.adminsecure.util.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;

public class CommandAdminsc extends Command {
    public CommandAdminsc() {
        super("adminsecure.command.adminsc", "<code>", buildSenders(Player.class), "adminsc");
    }

    @Override
    public void handleCommand(CommandSender sender, String label, String[] args) throws CommandErrorException, CommandInvalidPermissionsException, CommandInvalidUsageException, CommandInvalidSenderException {
        User user = AdminSecure.INSTANCE.userManager.retrieveUser(sender.getName());
        if(user == null)
            return;
        if(args.length < 1)
            throw new CommandInvalidUsageException();
        if(!args[0].matches("-?\\d+"))
            throw new CommandErrorException("Please input a valid number!");

        final String name = sender.getName();
        final int code = Integer.parseInt(args[0]);
        if(user.code != code) {
            ExecutorCentral.webhookExecutor.execute(() -> { try { ConfigParser.parseDWebhook(new DiscordWebhook(ConfigDefault.discord_webhook), ConfigDefault.security_code.responses.auth_invalid, name, Integer.toString(code)).execute(); } catch (IOException exception) { exception.printStackTrace(); }});

            sendMessage(sender, ConfigDefault.messages.auth_invalid);
            for(String command : ConfigDefault.commands.auth_invalid)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', command.replaceAll("%player_name%", sender.getName()).replaceAll("%code%", Integer.toString(user.code))));
            return;
        }

        AdminSecure.INSTANCE.userManager.verifiedPlayers.add(sender.getName());
        ExecutorCentral.webhookExecutor.execute(() -> { try { ConfigParser.parseDWebhook(new DiscordWebhook(ConfigDefault.discord_webhook), ConfigDefault.security_code.responses.auth_valid, name, Integer.toString(code)).execute(); } catch (IOException exception) { exception.printStackTrace(); }});
        AdminSecure.INSTANCE.userManager.handleUserRemove((Player)sender);
        sendMessage(sender, ConfigDefault.messages.auth_valid);
        for(String command : ConfigDefault.commands.auth_valid)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', command.replaceAll("%player_name%", sender.getName()).replaceAll("%code%", Integer.toString(user.code))));
        Player player = (Player)sender;
        if(player.isValid() && player.hasPotionEffect(PotionEffectType.BLINDNESS))
            player.removePotionEffect(PotionEffectType.BLINDNESS);
    }
}