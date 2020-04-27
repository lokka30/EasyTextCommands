package io.github.lokka30.easytextcommands.commands;

import io.github.lokka30.easytextcommands.EasyTextCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ETCCommand implements CommandExecutor {

    private EasyTextCommands instance;
    public ETCCommand(final EasyTextCommands instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command cmd, @NotNull final String label, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(instance.getUtils().colorize("&b&lEasyTextCommands: &7This server is running &bETC v" + instance.getDescription().getVersion() + "&7, developed by &3lokka30&7."));
            if (sender.hasPermission("easytextcommands.reload")) {
                sender.sendMessage(instance.getUtils().colorize("&b&lEasyTextCommands: &7To reload the enabled commands list, simply use &b/" + label + " reload&7."));
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("easytextcommands.reload")) {
                    sender.sendMessage(instance.getUtils().prefix(instance.getMessages().get("etc-reload-started", "Started reloading...")));
                    instance.getUtils().reloadEnabledCommands();
                    sender.sendMessage(instance.getUtils().prefix(instance.getMessages().get("etc-reload-complete", "...Reload complete")));
                } else {
                    sender.sendMessage(instance.getUtils().prefix(instance.getMessages().get("no-permission", "No permission")));
                }
            } else {
                sender.sendMessage(instance.getUtils().colorize("&b&lEasyTextCommands: &7Invalid argument '&b%arg%&7'. Usage: &b/" + label + " [reload]&7.")
                    .replaceAll("%arg%", args[0]));
            }
        } else {
            sender.sendMessage(instance.getUtils().colorize("&b&lEasyTextCommands: &7Too many arguments. Usage: &b/" + label));
        }
        return true;
    }
}
