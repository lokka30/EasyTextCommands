package io.github.lokka30.easytextcommands.utils;

import io.github.lokka30.easytextcommands.EasyTextCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Utils {

    private EasyTextCommands instance;
    public Utils(final EasyTextCommands instance) {
        this.instance = instance;
    }

    private List<String> enabledCommands;

    public String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public String prefix(final String msg) {
        final String prefix = instance.getMessages().get("prefix", "&b&lEasyTextCommands:&7");
        return colorize(msg.replaceFirst("%prefix%", prefix));
    }

    public void log(final LogLevel logLevel, final String msg) {
        final String prefixedMsg = colorize("&b&lEasyTextCommands: &7" + msg);
        final Logger logger = Bukkit.getLogger();
        switch(logLevel) {
            case INFO:
                logger.info(prefixedMsg);
                break;
            case WARNING:
                logger.warning(prefixedMsg);
                break;
            case SEVERE:
                logger.severe(prefixedMsg);
                break;
            default:
                log(LogLevel.SEVERE, "Unknown LogLevel '&b" + logLevel + "&7'. Message: " + msg);
        }
    }

    public int getLatestSettingsFileVersion() {
        return 1;
    }

    public int getLatestMessagesFileVersion() {
        return 1;
    }

    public List<String> getSupportedServerVersions() {
        return Arrays.asList("1.15", "1.14", "1.13", "1.12", "1.11", "1.10", "1.9", "1.8", "1.7");
    }

    public void reloadEnabledCommands() {
        enabledCommands = instance.getSettings().get("enabled-commands", new ArrayList<>());
    }

    public List<String> getEnabledCommands() {
        return enabledCommands;
    }
}
