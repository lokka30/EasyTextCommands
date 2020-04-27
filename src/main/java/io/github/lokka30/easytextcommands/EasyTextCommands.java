package io.github.lokka30.easytextcommands;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.FlatFile;
import io.github.lokka30.easytextcommands.commands.ETCCommand;
import io.github.lokka30.easytextcommands.listeners.CommandListener;
import io.github.lokka30.easytextcommands.utils.LogLevel;
import io.github.lokka30.easytextcommands.utils.UpdateChecker;
import io.github.lokka30.easytextcommands.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public class EasyTextCommands extends JavaPlugin {

    private Utils utils;
    private PluginManager pluginManager;
    private FlatFile settings;
    private FlatFile messages;

    @Override
    public void onLoad() {
        utils = new Utils(this);
        pluginManager = getServer().getPluginManager();
    }

    @Override
    public void onEnable() {
        utils.log(LogLevel.INFO, "&8+---+ &fEnable Started &8+---+");
        final long startTime = System.currentTimeMillis();

        utils.log(LogLevel.INFO, "&8(&31/6&8)&7 Checking compatibility...");
        checkCompatibility();

        utils.log(LogLevel.INFO, "&8(&32/6&8)&7 Loading files...");
        loadFiles();

        utils.log(LogLevel.INFO, "&8(&33/6&8)&7 Loading custom commands...");
        utils.reloadEnabledCommands();

        utils.log(LogLevel.INFO, "&8(&34/6&8)&7 Registering events...");
        registerEvents();

        utils.log(LogLevel.INFO, "&8(&35/6&8)&7 Registering commands...");
        registerCommands();

        utils.log(LogLevel.INFO, "&8(&36/6&8)&7 Starting metrics...");
        new Metrics(this, 7331);

        final long totalTime = System.currentTimeMillis() - startTime;
        utils.log(LogLevel.INFO, "&8+---+ &fEnable Finished (took " + totalTime + "ms) &8+---+");

        checkForUpdates();
    }

    private void checkCompatibility() {
        //Check version.
        boolean isSupportedVersion = false;
        for (String supportedVersion : utils.getSupportedServerVersions()) {
            if (getServer().getVersion().contains(supportedVersion)) {
                isSupportedVersion = true;
            }
        }
        if (!isSupportedVersion) {
            utils.log(LogLevel.INFO, "You are running an &cunsupported server version&7! Support will not be given if you run outside of the supported specifications.");
        }
    }

    private void loadFiles() {
        final String path = "plugins/Guilded/";
        settings = LightningBuilder
                .fromFile(new File(path + "settings"))
                .addInputStreamFromResource("settings.yml")
                .createYaml();
        messages = LightningBuilder
                .fromFile(new File(path + "messages"))
                .addInputStreamFromResource("messages.yml")
                .createYaml();

        //Check if they exist
        final File settingsFile = new File(path + "settings.yml");
        final File messagesFile = new File(path + "messages.yml");

        if (!(settingsFile.exists() && !settingsFile.isDirectory())) {
            utils.log(LogLevel.INFO, "File &bsettings.yml&7 doesn't exist. Creating it now.");
            saveResource("settings.yml", false);
        }

        if (!(messagesFile.exists() && !messagesFile.isDirectory())) {
            utils.log(LogLevel.INFO, "File &bmessages.yml&7 doesn't exist. Creating it now.");
            saveResource("messages.yml", false);
        }

        //Check their versions
        if (settings.get("file-version", 0) != utils.getLatestSettingsFileVersion()) {
            utils.log(LogLevel.SEVERE, "File &bsettings.yml&7 is out of date! Errors are likely to occur! Reset it or merge the old values to the new file.");
        }

        if (messages.get("file-version", 0) != utils.getLatestMessagesFileVersion()) {
            utils.log(LogLevel.SEVERE, "File &bmessages.yml&7 is out of date! Errors are likely to occur! Reset it or merge the old values to the new file.");
        }
    }

    private void registerEvents() {
        pluginManager.registerEvents(new CommandListener(this), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("easytextcommands")).setExecutor(new ETCCommand(this));
    }

    private void checkForUpdates() {
        //TODO Change Resource ID and remove "update-checker-bypass"
        if (!settings.get("update-checker-unimplemented-bypass", false)) {
            utils.log(LogLevel.INFO, "&8(&3Update Checker&8) &7The update checker has not been implemented yet.");
            return;
        }
        if (settings.get("use-update-checker", true)) {
            utils.log(LogLevel.INFO, "&8(&3Update Checker&8) &7Checking for updates...");
            new UpdateChecker(this, 12345).getVersion(version -> {
                if (getDescription().getVersion().equalsIgnoreCase(version)) {
                    utils.log(LogLevel.INFO, "&8(&3Update Checker&8) &7Thanks, you're running the latest version.");
                } else {
                    utils.log(LogLevel.WARNING, "&8(&3Update Checker&8) &7There's a new update available, '&b" + version + "&7'. You're running '&b" + getDescription().getVersion() + "&7'.");
                }
            });
        }
    }

    @Override
    public void onDisable() {
        utils.log(LogLevel.INFO, "&8+---+ &fDisable Started &8+---+");
        final long startTime = System.currentTimeMillis();

        //If any disable methods need to be added, put them here

        final long totalTime = System.currentTimeMillis() - startTime;
        utils.log(LogLevel.INFO, "&8+---+ &fDisable Finished (took " + totalTime + "ms) &8+---+");
    }

    public FlatFile getSettings() {
        return settings;
    }

    public FlatFile getMessages() {
        return messages;
    }

    public Utils getUtils() {
        return utils;
    }
}
