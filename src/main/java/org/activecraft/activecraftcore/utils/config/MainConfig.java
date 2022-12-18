package org.activecraft.activecraftcore.utils.config;

import org.activecraft.activecraftcore.ActiveCraftCore;
import lombok.Getter;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

@Getter
public class MainConfig extends ActiveCraftConfig {

    // TODO: 29.08.2022 test ConfigValue stuff

    private String //chatFormat,
            //joinFormat, quitFormat,
            lockdownModt, oldModt,
            databaseHost, databaseUser, databasePassword, databaseDialect, databaseLocalPath, databaseNetworkPath;
    private int defaultMuteDuration, databasePort;
    private boolean timerTpa, timerSpawn, timerHome, timerWarp,
            vanishTagEnabled, announceAfk, lockedDown, lockChat,
            socialSpyToConsole, inSilentMode, dropAllExp,
            defaultMuteEnabled, hideCommandsAfterPluginName;
    private List<String> hiddenCommandsAfterPluginNameExceptions, hiddenCommands;
    private final ConfigValue<Boolean> sendJoinMessage = configValue(this, "send-join-message", false);
    private final ConfigValue<Boolean> sendQuitMessage = configValue(this, "send-quit-message", false);
    private final ConfigValue<Boolean> useCustomChatFormat = configValue(this, "use-custom-chat-format", false);

    public MainConfig() {
        super(new FileConfig("config.yml"));
        loadConfigValues();
    }

    @Override
    protected void load() {
        //chatFormat = fileConfig.getString("chat-format", "INVALID_STRING");
        //joinFormat = fileConfig.getString("join-format", "");
        //quitFormat = fileConfig.getString("quit-format", "");
        timerTpa = fileConfig.getBoolean("tpa-timer");
        timerSpawn = fileConfig.getBoolean("spawn-timer");
        timerHome = fileConfig.getBoolean("home-timer");
        timerWarp = fileConfig.getBoolean("warp-timer");
        defaultMuteEnabled = fileConfig.getBoolean("default-mute.enabled");
        defaultMuteDuration = fileConfig.getInt("default-mute.duration");
        vanishTagEnabled = fileConfig.getBoolean("vanish-tag.enabled");
        announceAfk = fileConfig.getBoolean("afk.announce");
        lockedDown = fileConfig.getBoolean("lockdown.enabled");
        lockdownModt = fileConfig.getString("lockdown.modt", "INVALID_STRING");
        oldModt = fileConfig.getString("lockdown.old-modt", "INVALID_STRING");
        socialSpyToConsole = fileConfig.getBoolean("socialspy-to-console");
        inSilentMode = fileConfig.getBoolean("silent-mode");
        dropAllExp = fileConfig.getBoolean("drop-all-exp");
        hideCommandsAfterPluginName = fileConfig.getBoolean("hide-commands-after-plugin-name.enabled");
        hiddenCommandsAfterPluginNameExceptions = fileConfig.getStringList("hide-commands-after-plugin-name.except");
        hiddenCommands = fileConfig.getStringList("hide-commands");
        lockChat = fileConfig.getBoolean("lock-chat");
        databaseDialect = fileConfig.getString("database.dialect");
        databaseLocalPath = fileConfig.getString("database.local-path");
        databaseHost = fileConfig.getString("database.host");
        databaseNetworkPath = fileConfig.getString("database.network-path");
        databasePort = fileConfig.getInt("database.port");
        databaseUser = fileConfig.getString("database.user");
        databasePassword = fileConfig.getString("database.password");

        loadFeatures();
    }

    public void loadFeatures() {
        ConfigurationSection configSection = fileConfig.getConfigurationSection("features");
        if (configSection == null) {
            ActiveCraftCore.getInstance().error("Error loading features from config.yml. All features will be disabled.");
            return;
        }
        for (String featureId : configSection.getKeys(false)) {
            Feature feature = Feature.fromIdentifier(featureId);
            ActiveCraftCore.getInstance().getFeatures().put(feature, fileConfig.getBoolean("features." + featureId));
        }
    }

    public boolean isInSilentMode() {
        return inSilentMode;
    }

    public boolean isDefaultMuteEnabled() {
        return defaultMuteEnabled;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseDialect() {
        return databaseDialect;
    }

    public String getDatabaseLocalPath() {
        return databaseLocalPath;
    }

    public String getDatabaseNetworkPath() {
        return databaseNetworkPath;
    }

    public int getDatabasePort() {
        return databasePort;
    }
}
