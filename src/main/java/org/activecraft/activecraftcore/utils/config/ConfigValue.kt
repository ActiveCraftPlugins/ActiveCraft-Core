package org.activecraft.activecraftcore.utils.config

class ConfigValue<T> private constructor(
    val activeCraftConfig: ActiveCraftConfig,
    val configPath: String,
    val defaultValue: T? = null
) {

    companion object {
        @JvmStatic
        @JvmOverloads
        fun <T> registerNew(activeCraftConfig: ActiveCraftConfig, configPath: String, defaultValue: T? = null): ConfigValue<T> {
            val configValue = ConfigValue(activeCraftConfig, configPath, defaultValue)
            activeCraftConfig.configValues.add(configValue)
            return configValue
        }
    }

    var value: T? = null
        private set

    fun writeValue(value: T) {
        activeCraftConfig[configPath] = value
    }

    fun loadValue() {
        value = try {
            activeCraftConfig.fileConfig.get(configPath) as T
        } catch (_: ClassCastException) {
            defaultValue
        }
    }
}