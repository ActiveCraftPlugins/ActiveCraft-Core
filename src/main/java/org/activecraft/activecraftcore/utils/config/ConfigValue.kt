package org.activecraft.activecraftcore.utils.config

import kotlin.reflect.KProperty

class ConfigValue<T> constructor(
    private val activeCraftConfig: ActiveCraftConfig,
    private val configPath: String,
    private val defaultValue: T
) {

    private var value: T = defaultValue

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getValue()
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, newValue: T) {
        setValue(newValue)
    }

    fun getValue() = value ?: load()

    fun setValue(newValue: T) {
        activeCraftConfig.set(configPath, newValue)
        value = newValue
    }

    fun load(): T {
        value = runCatching { activeCraftConfig.fileConfig.get(configPath) as T }.getOrElse { defaultValue }
        return value
    }
}