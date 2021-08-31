package me.pk2.adminsecure.command.commands;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.command.Command;
import me.pk2.adminsecure.command.exception.CommandErrorException;
import me.pk2.adminsecure.command.exception.CommandInvalidPermissionsException;
import me.pk2.adminsecure.command.exception.CommandInvalidSenderException;
import me.pk2.adminsecure.command.exception.CommandInvalidUsageException;
import org.bukkit.command.CommandSender;

public class CommandAdminSecure extends Command {
    public CommandAdminSecure() {
        super("", "adminsecure");
    }

    @Override
    public void handleCommand(CommandSender sender, String label, String[] args) throws CommandErrorException, CommandInvalidPermissionsException, CommandInvalidUsageException, CommandInvalidSenderException {
        sendMessage(sender, "&6This server is protected with &eAdminSecure v" + AdminSecure.INSTANCE.getDescription().getVersion() + "&6 developed by &ePK2_Stimpy");
    }
}