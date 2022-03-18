package de.silencio.activecraftcore;

import de.silencio.activecraftcore.commands.ActiveCraftCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class ActiveCraftPluginManager {

    private final HashMap<String, CommandExecutor> commands = new HashMap<>();
    private final List<Listener> listeners = new ArrayList<>();
    private final Plugin plugin;

    public ActiveCraftPluginManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract void init();

    public void addCommands(ActiveCraftCommand... activeCraftCommands) {
        for (ActiveCraftCommand activeCraftCommand : activeCraftCommands)
            for (String command : activeCraftCommand.getCommands())
                commands.put(command, activeCraftCommand);
    }

    public void addListeners(Listener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    public void register() {
        for (Listener listener : listeners)
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        for (String cmd : commands.keySet()) {
            try {
                Bukkit.getPluginCommand(cmd).setExecutor(commands.get(cmd));
            } catch (NullPointerException e) {
                Bukkit.getLogger().severe("Error loading ActiveCraft-Command \"" + cmd + "\".");
                Bukkit.getLogger().severe("Please contact the developers about this issue.");
            }
        }
    }
}
