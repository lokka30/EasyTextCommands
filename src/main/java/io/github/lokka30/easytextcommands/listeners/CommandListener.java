package io.github.lokka30.easytextcommands.listeners;

import io.github.lokka30.easytextcommands.EasyTextCommands;
import io.github.lokka30.easytextcommands.utils.LogLevel;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandListener implements Listener {

    private EasyTextCommands instance;
    public CommandListener(final EasyTextCommands instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onChat(final PlayerCommandPreprocessEvent event) {
        final String command = event.getMessage().replaceFirst("/", "");
        final Player player = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        switch (event.getMessage().toLowerCase()) {
            case "/easytextcommands":
            case "/etc":
            case "/easytextcmds":
                return;
            default:
                if (instance.getUtils().getEnabledCommands().contains(command)) {
                    event.setCancelled(true);

                    final String commandPath = "commands." + command + ".";

                    final String permission = instance.getSettings().get(commandPath + "permission", null);
                    if (permission != null) {
                        if (!player.hasPermission(permission)) {
                            player.sendMessage(instance.getUtils().prefix(instance.getMessages().get("no-permission", "no perm")));
                            return;
                        }
                    }

                    final List<String> text = instance.getSettings().get(commandPath + "text", new ArrayList<>());
                    if (text != null && text.size() != 0) {
                        for (String msg : text) {
                            player.sendMessage(instance.getUtils().colorize(msg));
                        }
                    }

                    final String soundIdString = instance.getSettings().get(commandPath + "sound.id", null);
                    if(soundIdString != null) {
                        Sound soundId;
                        try {
                            soundId = Sound.valueOf(soundIdString);
                        } catch(IllegalArgumentException exception) {
                            instance.getUtils().log(LogLevel.SEVERE, "Unknown sound id '&b" + soundIdString + "&7' in command '&b" + command + "&7'!");
                            soundId = Sound.BLOCK_ANVIL_FALL;
                        }

                        float volume;
                        try {
                            volume = instance.getSettings().get(commandPath + "sound.volume", 1.0F);
                        } catch(NumberFormatException exception) {
                            instance.getUtils().log(LogLevel.SEVERE, "Invalid volume number '&b" + instance.getSettings().get(commandPath + "sound.volume", "Unknown") + "&7' in command '&b" + command + "&7'!");
                            volume = 1.0F;
                        }

                        float pitch;
                        try {
                            pitch = instance.getSettings().get(commandPath + "sound.pitch", 1.0F);
                        } catch(NumberFormatException exception) {
                            instance.getUtils().log(LogLevel.SEVERE, "Invalid pitch number '&b" + instance.getSettings().get(commandPath + "sound.pitch", "Unknown") + "&7' in command '&b" + command + "&7'!");
                            pitch = 1.0F;
                        }

                        player.playSound(player.getLocation(), soundId, volume, pitch);
                    }

                    //Add anythinge else here if needed
                }
        }
    }

}
