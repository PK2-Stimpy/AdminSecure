package me.pk2.adminsecure.command;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.command.commands.CommandAdminSecure;
import me.pk2.adminsecure.command.commands.CommandAdminsc;
import me.pk2.adminsecure.command.commands.CommandReload;
import me.pk2.adminsecure.command.exception.CommandErrorException;
import me.pk2.adminsecure.command.exception.CommandInvalidPermissionsException;
import me.pk2.adminsecure.command.exception.CommandInvalidSenderException;
import me.pk2.adminsecure.command.exception.CommandInvalidUsageException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandHandler implements CommandExecutor {
    public ArrayList<Command> commands;
    public CommandHandler() {
        commands = new ArrayList<>();
        commands.add(new CommandAdminsc());
        commands.add(new CommandAdminSecure());
        commands.add(new CommandReload());

        /* REGISTER COMMANDS */
        for(Command command : commands)
            for(String alias : command.aliases)
                AdminSecure.INSTANCE.getCommand(alias).setExecutor(this);
    }

    public void handleCommandCall(Command command, CommandSender commandSender, String alias, String[] args) {
        try {
            command.handleCommand(commandSender, alias, args);
        } catch (CommandErrorException exception) { commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a &r\n&eThere was an error running this command! \n&c" + exception.getMessage() + "\n&a &r"));
        } catch (CommandInvalidPermissionsException exception) { commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid permissions!"));
        } catch (CommandInvalidUsageException exception) { commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/" + alias + " " + command.usage));
        } catch (CommandInvalidSenderException exception) { commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are forbidden to run this command!")); }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        for(Command command : commands)
            for(String alias : command.aliases)
                if(alias.equalsIgnoreCase(label))
                    handleCommandCall(command, commandSender, alias, args);
        return true;
    }
}