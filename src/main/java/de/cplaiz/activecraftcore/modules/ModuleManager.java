package de.cplaiz.activecraftcore.modules;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.exceptions.ModuleException;
import de.cplaiz.activecraftcore.exceptions.OperationFailureException;
import de.cplaiz.activecraftcore.messages.Errors;
import de.cplaiz.activecraftcore.utils.WebReader;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ModuleManager {

    @Getter
    private static final Set<Module> modules = new HashSet<>();
    private static final String ACPREFIX = "ActiveCraft-";

    static {
        updateModules();
        Bukkit.getScheduler().runTaskTimer(ActiveCraftCore.getInstance(), ModuleManager::updateModules, 20 * 60 * 60, 20 * 60 * 60);
    }

    public static void updateModules() {
        modules.clear();
        try {
            HashMap<String, Integer> acVersionMap = WebReader.getACVersionMap();
            for (String moduleName : acVersionMap.keySet()) {
                if (moduleName.equals(ACPREFIX + "Core")) continue;
                int pluginId = acVersionMap.get(moduleName);
                Map<?, ?> data = WebReader.readAsMap(new URL("https://api.spiget.org/v2/resources/" + pluginId));
                modules.add(new Module(
                        ((String) data.get("name")).replace("ActiveCraft-", ""),
                        pluginId,
                        ((String) data.get("tag")),
                        new URL("https://spigotmc.org/" + ((String) ((Map) data.get("file")).get("url")).split("download\\?")[0]),
                        ((Map) data.get("file")).get("externalUrl") != null ? new URL((String) ((Map) data.get("file")).get("externalUrl")) : new URL("https://spigotmc.org/" + data.get("url"))
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void enable(String moduleName) throws ModuleException {
        Plugin plugin = getLoadedPlugin(moduleName);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    public static void load(String moduleName) throws ModuleException {
        checkNotLoaded(moduleName);
        File pluginsDir = new File("plugins");
        if (!pluginsDir.isDirectory())
            pluginsDir.mkdir();
        File pluginFile = null;
        for (File f : pluginsDir.listFiles()) {
            if (!f.getName().endsWith(".jar")) continue;
            PluginDescriptionFile desc;
            try {
                desc = ActiveCraftCore.getInstance().getPluginLoader().getPluginDescription(f);
            } catch (InvalidDescriptionException ignored) {
                throw new ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED);
            }
            if (!desc.getName().equalsIgnoreCase(ACPREFIX + moduleName)) continue;
            pluginFile = f;
            break;
        }
        try {
            Bukkit.getPluginManager().loadPlugin(pluginFile);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            throw new ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED);
        }
    }

    public static void install(String moduleName) throws OperationFailureException, ModuleException {
        File f = null;
        try {
            f = getInstalled(moduleName);
        } catch (ModuleException ignored) {
        }
        if (f != null) {
            throw new ModuleException(moduleName, ModuleException.ErrorType.ALREADY_INSTALLED);
        }
        try {
            Module module = getModule(moduleName);
            WebReader.downloadFile(module.downloadUrl(),"plugins" + File.separator + ACPREFIX + module.name() + ".jar");
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationFailureException(Errors.OPERATION_FAILED());
        }
    }

    public static void disable(String moduleName) throws ModuleException {
        Bukkit.getPluginManager().disablePlugin(getEnabledPlugin(moduleName));
    }

    public static Plugin getLoadedPlugin(String moduleName) throws ModuleException {
        getModule(moduleName);
        Plugin plugin = Bukkit.getPluginManager().getPlugin(ACPREFIX + moduleName);
        if (plugin == null)
            throw new ModuleException(moduleName, ModuleException.ErrorType.NOT_LOADED);
        return plugin;
    }

    public static void checkNotLoaded(String moduleName) throws ModuleException {
        getModule(moduleName);
        if (Bukkit.getPluginManager().getPlugin(ACPREFIX + moduleName) != null)
            throw new ModuleException(moduleName, ModuleException.ErrorType.ALREADY_LOADED);
    }

    public static Plugin getEnabledPlugin(String moduleName) throws ModuleException {
        Plugin plugin = getLoadedPlugin(moduleName);
        if (!plugin.isEnabled())
            throw new ModuleException(moduleName, ModuleException.ErrorType.NOT_ENABLED);
        return plugin;
    }

    public static Plugin getDisabledPlugin(String moduleName) throws ModuleException {
        Plugin plugin = getLoadedPlugin(moduleName);
        if (plugin.isEnabled())
            throw new ModuleException(moduleName, ModuleException.ErrorType.ALREADY_ENABLED);
        return plugin;
    }

    public static File getInstalled(String moduleName) throws ModuleException {
        File pluginsDir = new File("plugins");
        if (!pluginsDir.isDirectory())
            pluginsDir.mkdir();
        File pluginFile = null;
        for (File f : pluginsDir.listFiles()) {
            if (!f.getName().endsWith(".jar")) continue;
            PluginDescriptionFile desc;
            try {
                desc = ActiveCraftCore.getInstance().getPluginLoader().getPluginDescription(f);
            } catch (InvalidDescriptionException ignored) {
                throw new ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED);
            }
            if (!desc.getName().equalsIgnoreCase(ACPREFIX + moduleName)) continue;
            return f;
        }
        throw new ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED);
    }

    public static Module getModule(String moduleName) throws ModuleException {
        return modules.stream()
                .filter(module -> module.name().equals(moduleName))
                .findFirst().orElseThrow(() -> new ModuleException(moduleName, ModuleException.ErrorType.DOES_NOT_EXIST));
    }

}
