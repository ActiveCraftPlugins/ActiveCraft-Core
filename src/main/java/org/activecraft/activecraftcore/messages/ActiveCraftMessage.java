package org.activecraft.activecraftcore.messages;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException;
import org.activecraft.activecraftcore.utils.config.FileConfig;
import lombok.Getter;
import lombok.Setter;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException;
import org.activecraft.activecraftcore.utils.config.FileConfig;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.function.Predicate;

@Getter
public class ActiveCraftMessage {

    @Getter
    private static final HashMap<ActiveCraftPlugin, FileConfig> messageFileConfigs = new HashMap<>();

    private final HashMap<String, Language> availableLanguages = new HashMap<>();
    private final ActiveCraftPlugin plugin;
    private Language defaultPluginLanguage;
    private @Setter String defaultLanguageCode = "en";
    private @Setter ColorScheme colorScheme = ColorScheme.DEFAULT;
    private final HashMap<Language, MessageSupplier> messageAdapters = new HashMap<>();

    public ActiveCraftMessage(ActiveCraftPlugin plugin) {
        File msgFile = new File("plugins" + File.separator + plugin.getDataFolder().getName() + File.separator + "messages.yml");
        this.plugin = plugin;
        if (!msgFile.exists()) {
            if (plugin.getResource("messages.yml") != null) {
                plugin.saveResource("messages.yml", false);
            } else {
                plugin.warning("ActiveCraftMessage: Could not find messages.yml!");
            }
        }
        loadLanguages();
        loadMessageAdapters();
        FileConfig msgConfig = new FileConfig("messages.yml", plugin);
        defaultPluginLanguage = availableLanguages.get(msgConfig.getString("active-language", defaultLanguageCode));
        if (defaultPluginLanguage == null) {
            plugin.warning("ActiveCraftMessage: Could not find language! Please check \"active-language\" in your messages.yml!");
        }
        messageFileConfigs.put(plugin, msgConfig);
    }

    public void loadLanguages() {
        FileConfig msgConfig = new FileConfig("messages.yml", plugin);
        msgConfig.getKeys(false).stream()
                .filter(Predicate.not("active-language"::equals))
                .forEach(key -> availableLanguages.put(key, new Language(key, msgConfig.getString(key + ".language-name"))));
    }

    public void loadMessageAdapters() {
        availableLanguages.forEach((code, lang) -> messageAdapters.put(lang, new MessageSupplier(this, lang)));
    }

    public MessageSupplier getMessageAdapter(Language language) {
        return messageAdapters.get(language);
    }

    public void setDefaultPluginLanguage(Language language) throws InvalidLanguageException {
        if (!availableLanguages.containsValue(language))
            throw new InvalidLanguageException(language, plugin);
        messageFileConfigs.get(plugin).set("active-language", language.code());
        messageFileConfigs.get(plugin).saveAndReload();
        defaultPluginLanguage = language;
    }

    public static FileConfig getFileConfig(ActiveCraftPlugin plugin) {
        return messageFileConfigs.get(plugin);
    }

    public FileConfig getFileConfig() {
        return messageFileConfigs.get(plugin);
    }

    public static void reload(ActiveCraftPlugin plugin) {
        ActiveCraftMessage acm = plugin.getActiveCraftMessage();
        acm.getFileConfig().reload();
        acm.loadLanguages();
        acm.loadMessageAdapters();
    }

    // msg
    public String getMessage(String input) {
        return getMessage(input, plugin);
    }

    public String getMessage(String input, Language language) {
        return getMessage(input, plugin, language);
    }

    public String getMessage(String input, ChatColor color) {
        return getMessage(input, plugin, color);
    }

    public String getMessage(String input, ChatColor color, Language language) {
        return getMessage(input, plugin, color, language);
    }

    public static String getMessage(String input, ActiveCraftPlugin plugin) {
        return getMessage(input, plugin, plugin.getActiveCraftMessage().getColorScheme().primary());
    }

    public static String getMessage(String input, ActiveCraftPlugin plugin, Language language) {
        return getMessage(input, plugin, plugin.getActiveCraftMessage().getColorScheme().primary(), language);
    }

    public static String getMessage(String input, ActiveCraftPlugin plugin, ChatColor color) {
        return getMessage(input, plugin, color, null);
    }

    public static String getMessage(String input, ActiveCraftPlugin plugin, ChatColor color, Language language) {
        return (color == null ? "" : color) + getFileConfig(plugin).getString(
                (language == null ? plugin.getActiveCraftMessage().getDefaultPluginLanguage() : language).code()
                        + "." + input,
                "INVALID_STRING");
    }

    // raw msg
    public String getRawMessage(String input) {
        return getRawMessage(input, plugin);
    }

    public String getRawMessage(String input, Language language) {
        return getRawMessage(input, plugin, language);
    }

    public static String getRawMessage(String input, ActiveCraftPlugin plugin) {
        return getRawMessage(input, plugin, null);
    }

    public static String getRawMessage(String input, ActiveCraftPlugin plugin, Language language) {
        return getMessage(input, plugin, null, language);
    }

    // format
    public String getFormatted(String key, MessageFormatter formatter) {
        return getFormatted(key, plugin, formatter);
    }

    public String getFormatted(String key, MessageFormatter formatter, Language language) {
        return getFormatted(key, plugin, formatter, language);
    }

    public String getFormatted(String key, MessageFormatter formatter, ChatColor color) {
        return getFormatted(key, plugin, formatter, color);
    }


    public String getFormatted(String key, MessageFormatter formatter, ChatColor color, Language language) {
        return getFormatted(key, plugin, formatter, color, language);
    }

    public static String getFormatted(String key, ActiveCraftPlugin plugin, MessageFormatter formatter) {
        return getFormatted(key, plugin, formatter, plugin.getActiveCraftMessage().getColorScheme().primary());
    }

    public static String getFormatted(String key, ActiveCraftPlugin plugin, MessageFormatter formatter, Language language) {
        return getFormatted(key, plugin, formatter, plugin.getActiveCraftMessage().getColorScheme().primary(), language);
    }

    public static String getFormatted(String key, ActiveCraftPlugin plugin, MessageFormatter formatter, ChatColor color) {
        return getFormatted(key, plugin, formatter, color, null);
    }

    public static String getFormatted(String key, ActiveCraftPlugin plugin, MessageFormatter formatter, ChatColor color, Language language) {
        if (formatter == null)
            return getMessage(key, plugin, color, language);
        return formatter.format(getMessage(key, plugin, color, language));
    }
}