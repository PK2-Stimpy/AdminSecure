package me.pk2.adminsecure.command.commands;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.command.Command;
import me.pk2.adminsecure.command.exception.CommandErrorException;
import me.pk2.adminsecure.command.exception.CommandInvalidPermissionsException;
import me.pk2.adminsecure.command.exception.CommandInvalidSenderException;
import me.pk2.adminsecure.command.exception.CommandInvalidUsageException;
import org.bukkit.command.CommandSender;

public class CommandReload extends Command {
    public CommandReload() {
        super("adminsecure.command.reload", "adminsc-reload");
    }

    @Override
    public void handleCommand(CommandSender sender, String label, String[] args) throws CommandErrorException, CommandInvalidPermissionsException, CommandInvalidUsageException, CommandInvalidSenderException {
        sendMessage(sender, "&eReloading config...");
        AdminSecure.INSTANCE.reloadConfig();
        sendMessage(sender, "&aConfig reloaded!");
    }
}