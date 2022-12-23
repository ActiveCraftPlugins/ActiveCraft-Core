package org.activecraft.activecraftcore.messages

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.StartupException
import org.activecraft.activecraftcore.utils.config.FileConfig
import org.bukkit.ChatColor
import java.io.File

class ActiveCraftMessage @JvmOverloads constructor(
    val plugin: ActiveCraftPlugin,
    private val messageConfigFilePath: String = "messages.yml"
) {

    val messageFileConfig: FileConfig
    val availableLanguages = HashMap<String, Language>()
    var defaultPluginLanguage: Language
    var defaultLanguageCode = "en"
    var colorScheme = ColorScheme()
    val messageSuppliers = mutableListOf<MessageSupplier>()

    init {
        val msgFile =
            File("plugins" + File.separator + plugin.dataFolder.name + File.separator + messageConfigFilePath)
        if (!msgFile.exists())
            saveMessageConfig()
        loadLanguages()
        loadMessageSuppliers()
        messageFileConfig =
            FileConfig(messageConfigFilePath, plugin)
        val defLang = getLanguageByCode(messageFileConfig.getString("active-language", defaultLanguageCode)!!)
            ?: throw StartupException(
                true,
                "Unable to initialize ActiveCraftMessage: " +
                        "Could not find language! Please check \"active-language\" in your messages.yml!"
            )
        defaultPluginLanguage = defLang
    }

    companion object {
        @JvmStatic
        fun getMessage(
            key: String,
            plugin: ActiveCraftPlugin,
            color: ChatColor? = plugin.activeCraftMessage!!.colorScheme.primary,
            language: Language = plugin.activeCraftMessage!!.defaultPluginLanguage
        ): String {
            return (color ?: "").toString() + plugin.activeCraftMessage!!.messageFileConfig.getString(
                language.code
                        + "." + key,
                "INVALID_STRING"
            )
        }

        @JvmStatic
        fun getRawMessage(
            key: String,
            plugin: ActiveCraftPlugin,
            language: Language = plugin.activeCraftMessage!!.defaultPluginLanguage
        ) =
            getMessage(key = key, plugin = plugin, color = null, language = language)

        @JvmStatic
        fun getFormatted(
            key: String,
            plugin: ActiveCraftPlugin,
            color: ChatColor? = plugin.activeCraftMessage!!.colorScheme.primary,
            formatter: MessageFormatter,
            language: Language = plugin.activeCraftMessage!!.defaultPluginLanguage
        ) =
            formatter.format(getMessage(key = key, plugin = plugin, color = color, language = language))
    }

    @JvmOverloads
    fun getMessage(
        key: String,
        color: ChatColor? = colorScheme.primary,
        language: Language = defaultPluginLanguage
    ) = getMessage(key = key, plugin = plugin, color = color, language = language)

    @JvmOverloads
    fun getRawMessage(key: String, language: Language = defaultPluginLanguage) =
        getRawMessage(key = key, plugin = plugin, language = language)

    @JvmOverloads
    fun getFormatted(
        key: String,
        color: ChatColor? = colorScheme.primary,
        formatter: MessageFormatter,
        language: Language = defaultPluginLanguage
    ) =
        getFormatted(key = key, plugin = plugin, color = color, formatter = formatter, language = language)


    fun getLanguageByCode(code: String) = availableLanguages[code]

    private fun saveMessageConfig() {
        if (plugin.getResource(messageConfigFilePath) == null) {
            throw StartupException(
                true,
                "Unable to initialize ActiveCraftMessage: " +
                        "Could not find messages.yml!"
            )
        }
        plugin.saveResource(messageConfigFilePath, false)
    }

    private fun loadLanguages() {
        val msgConfig =
            FileConfig(messageConfigFilePath, plugin)
        msgConfig.getKeys(false)
            .filter { !it.equals("active-language") }
            .forEach {
                availableLanguages[it] = Language(it, msgConfig.getString("$it.language-name") ?: return@forEach)
            }
    }

    private fun loadMessageSuppliers() = availableLanguages.forEach {
        messageSuppliers.add(MessageSupplier(this, it.value))
    }

    fun getMessageSupplier(language: Language) = messageSuppliers.find { it.language == language }

    fun getDefaultMessageSupplier() = messageSuppliers.find { it.language == defaultPluginLanguage }
}