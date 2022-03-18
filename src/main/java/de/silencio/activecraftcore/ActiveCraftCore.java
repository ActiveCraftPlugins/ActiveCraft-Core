package de.silencio.activecraftcore;

import de.silencio.activecraftcore.guicreator.Gui;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.manager.DialogueManager;
import de.silencio.activecraftcore.messages.Language;
import de.silencio.activecraftcore.messages.MiscMessage;
import de.silencio.activecraftcore.playermanagement.PlayerQueue;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.UpdateChecker;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@ToString
public final class ActiveCraftCore extends JavaPlugin {

    private @Getter static ActiveCraftCore plugin;
    private @Getter static Language language;
    private @Getter static final HashMap<Player, ProfileMenu> profileMenuList = new HashMap<>();
    private @Getter static final HashMap<String, Profile> profiles = new HashMap<>();
    private @Getter static final HashMap<Integer, Gui> guiList = new HashMap<>();
    private @Getter static final HashMap<Player, Location> lastLocMap = new HashMap<>();
    private @Getter static final HashMap<CommandSender, DialogueManager> dialogueManagerList = new HashMap<>();
    private @Getter static ACCPluginManager pluginManager;

    public ActiveCraftCore() {
        plugin = this;
        pluginManager = new ACCPluginManager(ActiveCraftCore.getPlugin());
    }

    @Override
    public void onEnable() {
        // load from config
        loadConfigs();
        loadWarpPermissions();

        // Playermanagement
        createProfiles();
        PlayerQueue.initialize();

        // Initialize bStats
        Metrics metrics = new Metrics(this, 12627);

        // Load commmands and events
        pluginManager.init();

        // start playtime timer
        startTimer();

        // check for new plugin version
        new UpdateChecker(this, 95488).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version))
                getLogger().info("There is a new update available.");
        });

        log("Plugin loaded.");
    }

    private void loadWarpPermissions() {
        for (String s : ConfigManager.getWarpsConfig().getWarps().keySet()) {
            Map<String, Boolean> childMap = new HashMap<>();
            childMap.put("activecraft.warp.self", true);
            childMap.put("activecraft.warp", true);
            if (Bukkit.getPluginManager().getPermission("activecraft.warp.self." + s) != null)
                Bukkit.getPluginManager().addPermission(new Permission("activecraft.warp.self." + s, "Permission to warp yourself to a specific warp.", PermissionDefault.OP, childMap));
            childMap.clear();
            childMap.put("activecraft.warp.others", true);

            if (Bukkit.getPluginManager().getPermission("activecraft.warp.others." + s) != null)
                childMap.put("activecraft.warp", true);
            Bukkit.getPluginManager().addPermission(new Permission("activecraft.warp.others." + s, "Permission to warp another player to a specific warp.", PermissionDefault.OP, childMap));
        }
    }

    @Override
    public void onDisable() {
        log("Plugin unloaded.");
    }

    public static void log(String text) {
        log(Level.INFO, text);
    }
    public static void log(Level level, String text) {
        ActiveCraftCore.getPlugin().getLogger().log(level, text);
    }

    public void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = Profile.fromPlayer(player);

                int minutes = profile.getPlaytimeMinutes();
                int hours = profile.getPlaytimeHours();
                minutes++;

                profile.set(Profile.Value.PLAYTIME_MINUTES, minutes < 60 ? minutes : 0);
                profile.set(Profile.Value.PLAYTIME_HOURS, minutes == 60 ? hours++ : hours);

                if (minutes + hours * 60 >= ConfigManager.getMainConfig().getDefaultMuteDuration() && ConfigManager.getMainConfig().getDefaultMuteDuration() >= 0) {
                    if (profile.isDefaultmuted()) {
                        player.sendMessage(MiscMessage.DEFAULT_MUTE_REMOVE());
                        profile.set(Profile.Value.DEFAULTMUTED, false);
                    }
                }
            }
        }, 20 * 60, 20 * 60);
    }

    public void setLanguage(Language language) {
        ConfigManager.getMainConfig().set("language", language.getCode().toLowerCase());
        ActiveCraftCore.language = language;
    }

    public static Gui getGuiById(int id) {
        return ActiveCraftCore.guiList.get(id);
    }

    public static Map<String, UUID> getPlayerlist() {
        FileConfig playerlistConfig = new FileConfig("playerlist.yml");
        Map<String, UUID> playerlist = new HashMap<>();
        for (String key : playerlistConfig.getKeys(false))
            playerlist.put(playerlistConfig.getString(key), UUID.fromString(key));
        return playerlist;
    }

    public static String getPlayernameByUUID(String uuid) {
        return new FileConfig("playerlist.yml").getString(uuid);
    }

    public static String getPlayernameByUUID(UUID uuid) {
        return getPlayernameByUUID(uuid.toString());
    }

    public static UUID getUUIDByPlayername(String playername) {
        return getPlayerlist().get(playername.toLowerCase());
    }

    public static void createProfiles() {
        profiles.clear();
        for (String playername : getPlayerlist().keySet())
            if (new File(ActiveCraftCore.getPlugin().getDataFolder() + File.separator + "playerdata" + File.separator + playername + ".yml").exists())
                profiles.put(playername, new Profile(playername));
    }

    public void loadConfigs() {
        saveDefaultConfig();
        ConfigManager.reloadAll();
        File playerdataDir = new File(getDataFolder() + File.separator + "playerdata" + File.separator);
        if (!playerdataDir.exists())
            playerdataDir.mkdir();

        File messagesYML = new File(getDataFolder(), "messages.yml");
        if (!messagesYML.exists())
            saveResource("messages.yml", false);

        language = ConfigManager.getMainConfig().getLanguage();
        ConfigManager.loadMessageConfig();
    }
}
