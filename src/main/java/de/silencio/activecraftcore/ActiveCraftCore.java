package de.silencio.activecraftcore;

import de.silencio.activecraftcore.guicreator.Gui;
import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiData;
import de.silencio.activecraftcore.guis.ProfileMenu;
import de.silencio.activecraftcore.manager.DialogueManager;
import de.silencio.activecraftcore.messages.ActiveCraftMessage;
import de.silencio.activecraftcore.messages.Language;
import de.silencio.activecraftcore.messages.MiscMessage;
import de.silencio.activecraftcore.playermanagement.PlayerQueue;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import de.silencio.activecraftcore.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public final class ActiveCraftCore extends JavaPlugin {

    private static ActiveCraftCore plugin;
    private static Language language;
    private static ActiveCraftMessage activeCraftMessage;
    private static final HashMap<Player, Profile> msgPlayerStoring = new HashMap<>();
    private static final HashMap<Player, ProfileMenu> profileMenuList = new HashMap<>();
    private static final HashMap<String, Profile> profiles = new HashMap<>();
    private static final HashMap<GuiCreator, GuiData> guiDataMap = new HashMap<>();
    private static final HashMap<Integer, Gui> guiList = new HashMap<>();
    private static final HashMap<Player, Location> lastLocMap = new HashMap<>();
    private static final HashMap<CommandSender, DialogueManager> dialogueManagerList = new HashMap<>();

    public ActiveCraftCore() {
        plugin = this;
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
        PluginManager.init();

        // start playtime timer
        startTimer();

        // check for new plugin version
        new UpdateChecker(this, 95488).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version))
                getLogger().info("There is a new update available.");
        });

        log("Plugin loaded.");

        System.out.println(ConfigManager.mainConfig);
    }

    private void loadWarpPermissions() {
        for (String s : ConfigManager.warpsConfig.warps().keySet()) {
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

    public void log(String text) {
        Bukkit.getLogger().log(Level.INFO, text);
    }

    public static ActiveCraftCore getPlugin() {
        return plugin;
    }

    public void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = getProfile(player);

                int minutes = profile.getPlaytimeMinutes();
                int hours = profile.getPlaytimeHours();
                minutes++;

                profile.set(Profile.Value.PLAYTIME_MINUTES, minutes < 60 ? minutes : 0);
                profile.set(Profile.Value.PLAYTIME_HOURS, minutes == 60 ? hours++ : hours);

                if (minutes + hours * 60 >= ConfigManager.mainConfig.defaultMuteDuration() && ConfigManager.mainConfig.defaultMuteDuration() >= 0) {
                    if (profile.isDefaultmuted()) {
                        player.sendMessage(MiscMessage.DEFAULT_MUTE_REMOVE());
                        profile.set(Profile.Value.DEFAULTMUTED, false);
                    }
                }
            }
        }, 20 * 60, 20 * 60);
    }

    public static Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        ConfigManager.mainConfig.set("language", language.getCode().toLowerCase());
        ActiveCraftCore.language = language;
    }

    public static ActiveCraftMessage getActiveCraftMessage() {
        return activeCraftMessage;
    }

    public static void setActiveCraftMessage(ActiveCraftMessage activeCraftMessage) {
        ActiveCraftCore.activeCraftMessage = activeCraftMessage;
    }

    public static HashMap<CommandSender, DialogueManager> getDialogueManagerList() {
        return dialogueManagerList;
    }

    public static HashMap<Player, Location> getLastLocMap() {
        return lastLocMap;
    }

    public static HashMap<GuiCreator, GuiData> getGuiDataMap() {
        return guiDataMap;
    }

    public static HashMap<Integer, Gui> getGuiList() {
        return guiList;
    }

    public static Gui getGuiById(int id) {
        return ActiveCraftCore.guiList.get(id);
    }

    public static HashMap<Player, ProfileMenu> getProfileMenuList() {
        return profileMenuList;
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

    public static String getUUIDByPlayername(String playername) {
        getPlayerlist().get(playername.toLowerCase());
        return null;
    }

    public static HashMap<Player, Profile> getMsgPlayerStoring() {
        return msgPlayerStoring;
    }

    public static void createProfiles() {
        profiles.clear();
        for (String playername : getPlayerlist().keySet())
            if (new File(ActiveCraftCore.getPlugin().getDataFolder() + File.separator + "playerdata" + File.separator + playername + ".yml").exists())
                profiles.put(playername, new Profile(playername));
    }

    public static Profile getProfile(String playername) {
        Profile profile = profiles.get(playername.toLowerCase());
        if (profile == null) return null;
        profile.refresh();
        return profile;
    }

    public static Profile getProfile(Player player) {
        return getProfile(player.getName());
    }

    public static Profile getProfile(CommandSender sender) {
        return getProfile(sender.getName());
    }

    public static HashMap<String, Profile> getProfiles() {
        return profiles;
    }

    public void loadConfigs() {
        saveDefaultConfig();
        ConfigManager.loadConfigs();
        File playerdataDir = new File(getDataFolder() + File.separator + "playerdata" + File.separator);
        if (!playerdataDir.exists())
            playerdataDir.mkdir();

        File messagesYML = new File(getDataFolder(), "messages.yml");
        if (!messagesYML.exists())
            saveResource("messages.yml", false);

        language = ConfigManager.mainConfig.language();
        activeCraftMessage = new ActiveCraftMessage();
    }
}
