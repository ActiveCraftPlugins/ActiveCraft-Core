package org.activecraft.activecraftcore.utils.config

import org.activecraft.activecraftcore.ActiveCraftPlugin

object ConfigManager {
    private val configs: MutableMap<ActiveCraftPlugin, MutableSet<ActiveCraftConfig>> = mutableMapOf()

    fun reloadAll() {
        configs.values.forEach { config ->
            config.forEach{ it.reload() }
        }
    }

    fun reloadAll(plugin: ActiveCraftPlugin) {
        configs[plugin]?.forEach { it.reload() }
    }

    fun getConfigList(plugin: ActiveCraftPlugin): Set<ActiveCraftConfig>? {
        return configs[plugin]
    }

    fun getConfigs() = configs.toMap()

    fun registerConfig(plugin: ActiveCraftPlugin, config: ActiveCraftConfig) {
        configs.getOrPut(plugin) { mutableSetOf() }.add(config)
    }

    fun registerConfigs(plugin: ActiveCraftPlugin, vararg configs: ActiveCraftConfig) {
        configs.forEach { registerConfig(plugin, it) }
    }

    fun reloadMessageConfig(plugin: ActiveCraftPlugin) {
        plugin.activeCraftMessage?.messageFileConfig?.reload()
    }
}