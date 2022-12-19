package org.activecraft.activecraftcore.utils.config

import org.bukkit.plugin.Plugin

abstract class ActiveCraftConfig(@JvmField val fileConfig: FileConfig) {
    protected var configValues: MutableSet<ConfigValue<*>> = mutableSetOf()
    protected fun <T> configValue(configPath: String): ConfigValue<T?> {
        return configValue(configPath, null)
    }

    protected fun <T> configValue(
        configPath: String,
        defaultValue: T
    ): ConfigValue<T> {
        return ConfigValue(this, configPath, defaultValue).also { configValues.add(it) }
    }

    private fun <T> registerConfigValue(configPath: String, defaultValue: T): ConfigValue<T> {
        val configValue = ConfigValue(this, configPath, defaultValue)
        configValues.add(configValue)
        return configValue
    }

    @JvmOverloads
    constructor(fileName: String, plugin: Plugin? = null, vararg configValues: ConfigValue<*>) : this(
        FileConfig(
            fileName,
            plugin
        )
    )

    init {
        reload()
    }

    @JvmOverloads
    fun set(path: String, value: Any?, reload: Boolean = true) {
        fileConfig[path] = value
        fileConfig.saveConfig()
        if (reload) reload()
    }

    operator fun set(path: String, value: Any?) = set(path, value, true)

    fun reload() {
        fileConfig.reload()
        load()
        loadConfigValues()
    }

    protected abstract fun load()
    protected fun loadConfigValues() {
        for (configValue in configValues) {
            configValue.load()
        }
    }
}