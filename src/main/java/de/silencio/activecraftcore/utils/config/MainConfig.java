package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.messages.Language;

import java.util.List;

public record MainConfig(
        FileConfig fileConfig,
        String chatFormat, String joinFormat, String quitFormat,
        boolean timerTpa, boolean timerSpawn, boolean timerHome, boolean timerWarp,
        boolean defaultMuteEnabled, int defaultMuteDuration,
        boolean vanishTagEnabled, String vanishTagFormat,
        boolean announceAfk, String afkFormat,
        boolean lockdownEnabled, String lockdownModt, String lockdownKickMessage,
        boolean socialSpyToConsole, boolean silentMode, boolean dropAllExp, Language language,
        boolean hideCommandsAfterPluginName, List<String> hiddenCommandsAfterPluginNameExceptions,
        List<String> hiddenCommands) {

    public void set(String path, Object value) {
        FileConfig fileConfig = new FileConfig("config.yml");
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        ConfigManager.loadMainConfig();
    }
}
