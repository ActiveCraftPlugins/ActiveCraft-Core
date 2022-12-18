package org.activecraft.activecraftcore;

import org.activecraft.activecraftcore.commands.ActiveCraftCommand;
import org.activecraft.activecraftcore.commands.CommandExceptionProcessor;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.StartupException;
import org.activecraft.activecraftcore.messages.ActiveCraftMessage;
import org.activecraft.activecraftcore.utils.UpdateChecker;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

@Getter
public abstract class ActiveCraftPlugin extends JavaPlugin {

    private
    static final Set<ActiveCraftPlugin> installedPlugins = new HashSet<>();

    protected ActiveCraftMessage activeCraftMessage;
    protected org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage activeCraftMessagev2;
    protected PluginManager pluginManager;
    protected final int spigotId;
    protected final int bStatsId;
    protected Metrics metrics;
    protected final String permissionGroup;
    protected HashMap<String, ActiveCraftCommand> registeredCommands = new HashMap<>();
    protected CommandExceptionProcessor commandExceptionProcessor;


    public ActiveCraftPlugin() {
        this(0);
    }

    public ActiveCraftPlugin(boolean useActiveCraftMessage) {
        this(0, useActiveCraftMessage);
    }

    public ActiveCraftPlugin(String permissionGroup) {
        this(0, 0, permissionGroup);
    }

    public ActiveCraftPlugin(String permissionGroup, boolean useActiveCraftMessage) {
        this(0, 0, permissionGroup, useActiveCraftMessage);
    }

    public ActiveCraftPlugin(int spigotId) {
        this(spigotId, 0);
    }

    public ActiveCraftPlugin(int spigotId, boolean useActiveCraftMessage) {
        this(spigotId, 0, useActiveCraftMessage);
    }

    public ActiveCraftPlugin(int spigotId, String permissionGroup) {
        this(spigotId, 0, permissionGroup);
    }

    public ActiveCraftPlugin(int spigotId, String permissionGroup, boolean useActiveCraftMessage) {
        this(spigotId, 0, permissionGroup, useActiveCraftMessage);
    }

    public ActiveCraftPlugin(int spigotId, int bStatsId) {
        this(spigotId, bStatsId, "activecraft");
    }

    public ActiveCraftPlugin(int spigotId, int bStatsId, boolean useActiveCraftMessage) {
        this(spigotId, bStatsId, "activecraft", useActiveCraftMessage);
    }

    public ActiveCraftPlugin(int spigotId, int bStatsId, String permissionGroup) {
        this(spigotId, bStatsId, permissionGroup, true);
    }

    public ActiveCraftPlugin(int spigotId, int bStatsId, String permissionGroup, boolean useActiveCraftMessage) {
        this.spigotId = spigotId;
        this.bStatsId = bStatsId;
        this.permissionGroup = permissionGroup;
        if (bStatsId != 0)
            this.metrics = new Metrics(this, bStatsId);
        if (useActiveCraftMessage) {
            //try {
                this.activeCraftMessage = new ActiveCraftMessage(this);
                this.activeCraftMessagev2 = new org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage(this);
            //} catch (StartupException e) {
            //    error(e.getMessage());
            //    this.activeCraftMessage = null;
            //}
        }
        this.pluginManager = new PluginManager(this);
        this.commandExceptionProcessor = new CommandExceptionProcessor(this);
        installedPlugins.add(this);
    }


    public static ActiveCraftPlugin of(String name) {
        return installedPlugins.stream()
                .filter(plugin -> plugin.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public static ActiveCraftPlugin of(Plugin plugin) {
        assert plugin != null;
        return of(plugin.getName());
    }

    public void onEnable() {
        registerConfigs();
        try {
            onPluginEnabled();
        } catch (StartupException e) {
            error("Startup error" + (e.getMessage().equals("") ? "" : ": " + e.getMessage()));
            if (e.isShutdown()) {
                this.setEnabled(false);
                return;
            }
        }
        register();
    }

    public void onDisable() {
        onPluginDisabled();
    }

    public abstract void onPluginEnabled() throws StartupException;

    public abstract void onPluginDisabled();

    public void log(Level level, String text) {
        log(this, level, text);
    }

    public static void log(Plugin plugin, Level level, String text) {
        plugin.getLogger().log(level, text);
    }

    public void log(String text) {
        log(Level.INFO, text);
    }

    public static void log(Plugin plugin, String text) {
        log(plugin, Level.INFO, text);
    }

    public void error(String text) {
        log(Level.SEVERE, text);
    }

    public static void error(Plugin plugin, String text) {
        log(plugin, Level.SEVERE, text);
    }

    public void warning(String text) {
        log(Level.WARNING, text);
    }

    public static void warning(Plugin plugin, String text) {
        log(plugin, Level.WARNING, text);
    }

    public static void bukkitLog(Level level, String text) {
        Bukkit.getLogger().log(level, text);
    }

    public static void bukkitLog(String text) {
        Bukkit.getLogger().log(Level.INFO, text);
    }

    public static ActiveCraftPlugin getActiveCraftPlugin(String name) {
        return installedPlugins.stream().filter(plugin -> plugin.getName().equals(name)).findAny().orElse(null);
    }

    public static boolean isCompatibleVersion(String version, String minVersion, String maxVersion) {
        String[] minSplit = minVersion.split("\\.");
        String[] maxSplit = maxVersion.split("\\.");
        String[] verSplit = version.split("\\.");
        if (minSplit.length != maxSplit.length && maxSplit.length != verSplit.length)
            throw new IllegalArgumentException("Invalid version string");
        StringBuilder minVersionNumber = new StringBuilder();
        StringBuilder maxVersionNumber = new StringBuilder();
        StringBuilder verVersionNumber = new StringBuilder();
        for (int i = 0; i < minSplit.length; i++) {
            int minLen = minSplit[i].length();
            int maxLen = maxSplit[i].length();
            int verLen = verSplit[i].length();
            int longest = Math.max(Math.max(minLen, maxLen), verLen);
            minVersionNumber.append(fillWithZero(minSplit[i], longest));
            maxVersionNumber.append(fillWithZero(maxSplit[i], longest));
            verVersionNumber.append(fillWithZero(verSplit[i], longest));
        }
        try {
            long min = Long.parseLong(minVersionNumber.toString().replace("_", "").replace("*", "0"));
            long max = Long.parseLong(maxVersionNumber.toString().replace("_", "").replace("*", "9"));
            long ver = Long.parseLong(verVersionNumber.toString().replace("_", ""));
            if (max == 0)
                return ver >= min;
            if (min == 0)
                return ver <= max;
            if (min > max)
                throw new IllegalArgumentException("Invalid version string");
            return ver >= min && ver <= max;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version string");
        }
    }

    public static boolean isDependancyPresent(String dependency) {
        return Bukkit.getPluginManager().isPluginEnabled(dependency);
    }

    public static boolean isDependancyPresent(String dependency, String minVersion) {
        if (!Bukkit.getPluginManager().isPluginEnabled(dependency))
            return false;
        return !isCompatibleVersion(Bukkit.getPluginManager().getPlugin(dependency).getDescription().getVersion(), minVersion, minVersion.replaceAll("[1-9a-zA-Z]", "0"));
    }

    public static boolean isDependancyPresent(String dependency, String minVersion, String maxVersion) {
        if (!Bukkit.getPluginManager().isPluginEnabled(dependency))
            return false;
        return !isCompatibleVersion(Bukkit.getPluginManager().getPlugin(dependency).getDescription().getVersion(), minVersion, maxVersion);
    }

    public void checkForUpdate() {
        assert spigotId != 0;
        new UpdateChecker(this, spigotId).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is a new update available.");
                getLogger().info("Download it at: https://www.spigotmc.org/resources/" + spigotId);
                ActiveCraftCore.getInstance().getPluginManager().addListeners(new Listener() {

                    private static boolean alreadyNotified = false;

                    @EventHandler
                    public void onOperatorJoin(PlayerJoinEvent event) {
                        Player player = event.getPlayer();
                        if (!player.isOp() || alreadyNotified) return;
                        ComponentBuilder builder = new ComponentBuilder();
                        builder.append(new TextComponent(ChatColor.GOLD + getName()
                                + ChatColor.DARK_AQUA + " - " + ChatColor.RED + "Update Available. "));
                        builder.append(new TextComponent(ChatColor.RED + "\nCurrent: " + ChatColor.DARK_RED
                                + getDescription().getVersion() + ChatColor.RED
                                + " Newest: " + ChatColor.DARK_RED + version + ChatColor.RED + "."));
                        TextComponent linkComponent = new TextComponent();
                        linkComponent.setText(ChatColor.AQUA + " [Link]");
                        linkComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to open link")));
                        linkComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + getName().toLowerCase() + "." + spigotId));
                        builder.append(linkComponent);
                        player.sendMessage(builder.create());
                        alreadyNotified = true;
                    }
                });
            }
        });
    }

    protected static void disableIfDependancyMissing(ActiveCraftPlugin activeCraftPlugin, String dependency, String minVersion, String maxVersion) throws StartupException {
        if (!isDependancyPresent(dependency)) {
            activeCraftPlugin.error("*** " + dependency + " is not installed or not enabled! ***");
            activeCraftPlugin.error("*** This plugin will be disabled! ***");
            throw new StartupException("Dependancy missing");
        } else if (!isCompatibleVersion(Bukkit.getPluginManager().getPlugin(dependency).getDescription().getVersion(), minVersion, maxVersion)) {
            activeCraftPlugin.error("*** The version of " + dependency + " you are using is incompatible with "
                    + activeCraftPlugin.getName() + " v" + activeCraftPlugin.getDescription().getVersion() + "! ***");
            activeCraftPlugin.error("*** Please use " + dependency + " " +
                    (isAnyVersion(minVersion) ? "until" : minVersion) +
                    (!isAnyVersion(minVersion) && !isAnyVersion(maxVersion) ? " - " : " ") +
                    (isAnyVersion(maxVersion) ? "or later" : maxVersion) + " ***");
            activeCraftPlugin.error("*** This plugin will be disabled! ***");
            throw new StartupException("Incompatible version");
        }
    }

    protected void disableIfDependancyMissing(String dependency, String minVersion) throws StartupException {
        disableIfDependancyMissing(this, dependency, minVersion, minVersion.replaceAll("[1-9a-zA-Z]", "0"));
    }

    protected void disableIfDependancyMissing(String dependency, String minVersion, String maxVersion) throws StartupException {
        disableIfDependancyMissing(this, dependency, minVersion, maxVersion);
    }

    protected abstract void registerConfigs();

    protected abstract void register();

    private static String fillWithZero(String target, int max) {
        StringBuilder targetBuilder = new StringBuilder(target);
        for (int i = target.length(); i < max; i++) {
            targetBuilder.insert(0, "0");
        }
        return targetBuilder.toString();
    }

    private static boolean isAnyVersion(String version) {
        return version.replace(".", "").replace("0", "").equals("");
    }

    public static Set<ActiveCraftPlugin> getInstalledPlugins() {
        return installedPlugins;
    }

    public org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage getActiveCraftMessagev2() {
        return activeCraftMessagev2;
    }

    public String getPermissionGroup() {
        return permissionGroup;
    }

    public CommandExceptionProcessor getCommandExceptionProcessor() {
        return commandExceptionProcessor;
    }
}