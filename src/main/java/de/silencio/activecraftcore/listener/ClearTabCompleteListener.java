package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ClearTabCompleteListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClickTab(PlayerCommandSendEvent e) {

        List<String> exceptionList = ConfigManager.getMainConfig().getHiddenCommandsAfterPluginNameExceptions();

        if (!ConfigManager.getMainConfig().isHideCommandsAfterPluginName()) return;

        List<String> pluginNames = new ArrayList<>();
        pluginNames.add("minecraft");
        pluginNames.add("bukkit");
        pluginNames.add("spigot");
        pluginNames.add("paper");
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            pluginNames.add(plugin.getName().toLowerCase());
        }

        List<String> toBeRemoved = new ArrayList<>();

        for (String command : e.getCommands()) {
            for (String pluginName : pluginNames) {
                if (command.startsWith(pluginName + ":")) {
                    if (!(exceptionList.contains(command) || exceptionList.contains(pluginName.replace(":", "")))) {
                       toBeRemoved.add(command);
                    }
                }
            }
        }

        for (String s : toBeRemoved) {
            e.getCommands().remove(s);
        }

        if (!e.getPlayer().hasPermission("activecraft.vanilla.plugins")) {
            e.getCommands().remove("pl");
            e.getCommands().remove("plugins");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.help")) {
            e.getCommands().remove("?");
            e.getCommands().remove("help");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.about")) {
            e.getCommands().remove("icanhasbukkit");
            e.getCommands().remove("about");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.list")) {
            e.getCommands().remove("list");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.me")) {
            e.getCommands().remove("me");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.teammsg")) {
            e.getCommands().remove("teammsg");
            e.getCommands().remove("tm");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.msg")) {
            e.getCommands().remove("tell");
            e.getCommands().remove("w");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.trigger")) {
            e.getCommands().remove("trigger");
        }
        if (!e.getPlayer().hasPermission("activecraft.vanilla.version")) {
            e.getCommands().remove("ver");
            e.getCommands().remove("version");
        }
    }
}