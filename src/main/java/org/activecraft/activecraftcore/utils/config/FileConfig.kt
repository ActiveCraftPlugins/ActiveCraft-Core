package org.activecraft.activecraftcore.utils.config

import org.activecraft.activecraftcore.ActiveCraftCore
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class FileConfig(private val folder: String, private val filename: String) : YamlConfiguration() {

    private val fullPath: String = "plugins" + File.separator + folder + File.separator + filename

    init {
        reload()
    }

    @JvmOverloads
    constructor(filename: String, plugin: Plugin? = null) : this(
        (plugin ?: ActiveCraftCore.INSTANCE).dataFolder.name,
        filename
    )

    fun saveConfig() {
        try {
            save(this.fullPath)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun reload() {
        try {
            load(this.fullPath)
        } catch (_: InvalidConfigurationException) {
        } catch (ex: IOException) {
            if (ex !is FileNotFoundException) ex.printStackTrace()
        }
    }

    fun saveAndReload() {
        saveConfig()
        reload()
    }

    fun set() {

    }

    val sections: Set<ConfigurationSection>
        get() = getKeys(false).mapNotNull { getConfigurationSection(it) }.toSet()
}