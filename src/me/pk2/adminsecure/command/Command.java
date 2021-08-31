package me.pk2.adminsecure.command;

import me.pk2.adminsecure.command.exception.CommandErrorException;
import me.pk2.adminsecure.command.exception.CommandInvalidPermissionsException;
import me.pk2.adminsecure.command.exception.CommandInvalidSenderException;
import me.pk2.adminsecure.command.exception.CommandInvalidUsageException;
import me.pk2.adminsecure.config.ConfigDefault;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command {
    @SafeVarargs
    public static final Class<? extends CommandSender>[] buildSenders(Class<? extends CommandSender>... senders) { return senders; }

    public final String[] aliases;
    public final Class<? extends CommandSender>[] senders;
    public final String usage, permission;
    public Command(String permission, String usage, Class<? extends CommandSender>[] senders, String... aliases) {
        this.permission = permission;
        this.usage = usage;
        this.senders = senders;
        this.aliases = aliases;
    }
    public Command(String permission, String usage, Class<? extends CommandSender>[] senders, String alias) { this("", usage, senders, new String[]{alias}); }
    public Command(String usage, Class<? extends CommandSender>[] senders, String alias) { this("", usage, senders, alias); }
    public Command(String usage, Class<? extends CommandSender>[] senders, String... aliases) { this("", usage, senders, aliases); }
    public Command(String permission, String usage, String alias) { this(permission, usage, null, new String[] {alias}); }
    public Command(String permission, String usage, String... aliases) { this(permission, usage, null, aliases); }
    public Command(String usage, String alias) { this("", usage, new String[] {alias}); }
    public Command(String usage, String... aliases) { this("", usage, aliases); }

    public final void sendMessage(CommandSender sender, String message) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigDefault.messages.prefix + message)); }

    public abstract void handleCommand(CommandSender sender, String label, String[] args) throws CommandErrorException, CommandInvalidPermissionsException, CommandInvalidUsageException, CommandInvalidSenderException;
    public final void preHandleCommand(CommandSender sender, String label, String[] args) throws CommandErrorException, CommandInvalidPermissionsException, CommandInvalidUsageException, CommandInvalidSenderException {
        if(!permission.equalsIgnoreCase("") && !sender.hasPermission(permission))
            throw new CommandInvalidPermissionsException();
        if(senders != null) {
            boolean senderValid = false;
            for(Class<? extends CommandSender> commandSender : senders)
                if(sender.getClass().equals(commandSender))
                    senderValid = true;
            if(!senderValid)
                throw new CommandInvalidSenderException();
        }

        handleCommand(sender, label, args);
    }
}