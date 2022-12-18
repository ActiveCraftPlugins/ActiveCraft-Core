package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.UpdateChecker;
import org.activecraft.activecraftcore.utils.WebReader;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.UpdateChecker;
import org.activecraft.activecraftcore.utils.WebReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ACVersionCommand extends ActiveCraftCommand {

    public ACVersionCommand(ActiveCraftPlugin plugin) {
        super("acversion",  plugin, "version");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.EQUAL, 0);
        HashMap<String, Integer> plugins = WebReader.getACVersionMap();
        ArrayList<String> keys = new ArrayList<>(plugins.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(key);
            if (plugin == null) {
                sendMessage(sender, ChatColor.GOLD + key + ChatColor.DARK_AQUA + " - " + ChatColor.GRAY + "Not installed.");
                continue;
            }
            new UpdateChecker(plugin, plugins.get(key)).getVersion(version -> {
                if (!plugin.getDescription().getVersion().equals(version)) {
                    ComponentBuilder builder = new ComponentBuilder();
                    builder.append(new TextComponent(ChatColor.GOLD + key + ChatColor.DARK_AQUA + " - " + ChatColor.RED + "Update Available. "));
                    builder.append(new TextComponent(ChatColor.RED + "\nCurrent: " + ChatColor.DARK_RED + plugin.getDescription().getVersion() + ChatColor.RED + " Newest: " + ChatColor.DARK_RED + version + ChatColor.RED + "."));
                    TextComponent linkComponent = new TextComponent();
                    linkComponent.setText(ChatColor.AQUA + " [Link]");
                    linkComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to open link")));
                    linkComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + plugin.getName().toLowerCase() + "." + plugins.get(key)));
                    builder.append(linkComponent);
                    sendMessage(sender, builder.create());
                } else
                    sendMessage(sender, ChatColor.GOLD + key + ChatColor.DARK_AQUA + " - " + ChatColor.GREEN + "Latest.");
            });
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
