package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.messages.Language;
import lombok.Getter;

import java.util.List;

@Getter
public class MainConfig extends ActiveCraftConfig {

    private String chatFormat, joinFormat, quitFormat,
            lockdownModt, oldModt, lockdownKickMessage,
            vanishTagFormat, afkFormat;
    private int defaultMuteDuration;
    private boolean timerTpa, timerSpawn, timerHome, timerWarp,
            vanishTagEnabled, announceAfk, lockedDown,
            socialSpyToConsole, inSilentMode, dropAllExp,
            defaultMuteEnabled, hideCommandsAfterPluginName;
    Language language;
    List<String> hiddenCommandsAfterPluginNameExceptions, hiddenCommands;

    public MainConfig() {
        super(new FileConfig("config.yml"));
    }

    @Override
    protected void load() {
        chatFormat = fileConfig.getString("chat-format", "INVALID_STRING");
        joinFormat = fileConfig.getString("join-format", "INVALID_STRING");
        quitFormat = fileConfig.getString("quit-format", "INVALID_STRING");
        timerTpa = fileConfig.getBoolean("tpa-timer");
        timerSpawn = fileConfig.getBoolean("spawn-timer");
        timerHome = fileConfig.getBoolean("home-timer");
        timerWarp = fileConfig.getBoolean("warp-timer");
        defaultMuteEnabled = fileConfig.getBoolean("default-mute.enabled");
        defaultMuteDuration = fileConfig.getInt("default-mute.duration");
        vanishTagEnabled = fileConfig.getBoolean("vanish-tag.enabled");
        vanishTagFormat = fileConfig.getString("vanish-tag.format", "INVALID_STRING");
        announceAfk = fileConfig.getBoolean("afk.announce");
        afkFormat = fileConfig.getString("afk.format", "INVALID_STRING");
        lockedDown = fileConfig.getBoolean("lockdown.enabled");
        lockdownModt = fileConfig.getString("lockdown.modt", "INVALID_STRING");
        oldModt = fileConfig.getString("lockdown.old-modt", "INVALID_STRING");
        lockdownKickMessage = fileConfig.getString("lockdown.kick-message", "INVALID_STRING");
        socialSpyToConsole = fileConfig.getBoolean("socialspy-to-console");
        inSilentMode = fileConfig.getBoolean("silent-mode");
        dropAllExp = fileConfig.getBoolean("drop-all-exp");
        language = Language.valueOf(fileConfig.getString("language", "INVALID_STRING").toUpperCase());
        hideCommandsAfterPluginName = fileConfig.getBoolean("hide-commands-after-plugin-name.enabled");
        hiddenCommandsAfterPluginNameExceptions = fileConfig.getStringList("hide-commands-after-plugin-name.except");
        hiddenCommands = fileConfig.getStringList("hide-commands");
    }
}
