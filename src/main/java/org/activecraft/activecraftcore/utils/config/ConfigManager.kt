package org.activecraft.activecraftcore.utils.config

import org.activecraft.activecraftcore.ActiveCraftPlugin

object ConfigManager {
    private val configs: MutableMap<ActiveCraftPlugin, MutableSet<ActiveCraftConfig>> = mutableMapOf()

    @JvmStatic
    fun reloadAll() {
        configs.values.forEach { config ->
            config.forEach{ it.reload() }
        }
    }

    @JvmStatic
    fun reloadAll(plugin: ActiveCraftPlugin) {
        configs[plugin]?.forEach { it.reload() }
    }

    @JvmStatic
    fun getConfigList(plugin: ActiveCraftPlugin): Set<ActiveCraftConfig>? {
        return configs[plugin]
    }

    @JvmStatic
    fun getConfigs() = configs.toMap()

    @JvmStatic
    fun registerConfig(plugin: ActiveCraftPlugin, config: ActiveCraftConfig) {
        configs.getOrPut(plugin) { mutableSetOf() }.add(config)
    }

    @JvmStatic
    fun registerConfigs(plugin: ActiveCraftPlugin, vararg configs: ActiveCraftConfig) {
        configs.forEach { registerConfig(plugin, it) }
    }

    @JvmStatic
    fun reloadMessageConfig(plugin: ActiveCraftPlugin) {
        plugin.activeCraftMessage?.messageFileConfig?.reload()
    }
}