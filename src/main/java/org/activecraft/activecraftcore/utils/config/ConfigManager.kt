package org.activecraft.activecraftcore.utils.config

import lombok.Getter
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.messages.ActiveCraftMessage
import java.util.*
import java.util.function.Consumer

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
    fun registerConfig(plugin: ActiveCraftPlugin, config: ActiveCraftConfig) {
        configs.getOrPut(plugin) { mutableSetOf() }.add(config)
    }

    @JvmStatic
    fun registerConfigs(plugin: ActiveCraftPlugin, vararg configs: ActiveCraftConfig) {
        configs.forEach { registerConfig(plugin, it) }
    }

    @JvmStatic
    fun reloadMessageConfig(plugin: ActiveCraftPlugin) {
        ActiveCraftMessage.getFileConfig(plugin).reload()
    }
}