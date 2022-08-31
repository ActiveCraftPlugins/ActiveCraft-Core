package de.cplaiz.activecraftcore;

import de.cplaiz.activecraftcore.commands.ActiveCraftCommand;
import de.cplaiz.activecraftcore.commands.ActiveCraftCommandCollection;
import de.cplaiz.activecraftcore.commands.ActiveCraftCommandv2;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PluginManager {

    private final ActiveCraftPlugin plugin;

    public PluginManager(ActiveCraftPlugin plugin) {
        this.plugin = plugin;
    }

    public void addCommands(ActiveCraftCommand... activeCraftCommands) {
        Arrays.stream(activeCraftCommands).forEach(activeCraftCommand -> {
            String cmd = activeCraftCommand.getCommandName();
            PluginCommand bukkitCommand = Bukkit.getPluginCommand(cmd);
            if (bukkitCommand == null) {
                Bukkit.getLogger().severe("Error loading ActiveCraft-Command \"" + cmd + "\".");
                return;
            }
            Bukkit.getPluginCommand(cmd).setExecutor(activeCraftCommand);
            plugin.getRegisteredCommands().put(activeCraftCommand.getCommandName(), activeCraftCommand);
        });
    }

    public void addCommands(ActiveCraftCommandv2... activeCraftCommands) {
        Arrays.stream(activeCraftCommands).forEach(activeCraftCommand -> {
            String cmd = activeCraftCommand.getCommandName();
            PluginCommand bukkitCommand = Bukkit.getPluginCommand(cmd);
            if (bukkitCommand == null) {
                Bukkit.getLogger().severe("Error loading ActiveCraft-Command \"" + cmd + "\".");
                return;
            }
            Bukkit.getPluginCommand(cmd).setExecutor(activeCraftCommand);
            //plugin.getRegisteredCommands().put(activeCraftCommand.getCommandName(), activeCraftCommand); todo: ändern wenn alles zu v2 migriert
        });
    }

    public void addCommandCollections(ActiveCraftCommandCollection... collections) {
        for (ActiveCraftCommandCollection collection : collections) {
            collection.forEach(this::addCommands);
        }
    }

    public void addListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
    }
}
