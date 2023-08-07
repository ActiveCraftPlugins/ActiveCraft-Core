package org.activecraft.activecraftcore.modules

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.exceptions.ModuleException
import org.activecraft.activecraftcore.exceptions.OperationFailureException
import org.activecraft.activecraftcore.utils.WebReader.aCVersionMap
import org.activecraft.activecraftcore.utils.WebReader.downloadFile
import org.activecraft.activecraftcore.utils.WebReader.readAsMap
import org.bukkit.Bukkit
import org.bukkit.plugin.InvalidDescriptionException
import org.bukkit.plugin.InvalidPluginException
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import java.io.File
import java.io.IOException
import java.net.URL

object ModuleManager {
    val modules: MutableSet<Module> = HashSet()
    private const val ACPREFIX = "ActiveCraft-"

    init {
        updateModules()
        Bukkit.getScheduler().runTaskTimer(
            ActiveCraftCore.INSTANCE,
            Runnable { updateModules() },
            (20 * 60 * 60).toLong(),
            (20 * 60 * 60).toLong()
        )
    }

    fun updateModules() {
        modules.clear()
        try {
            val acVersionMap: Map<String, Int> = aCVersionMap
            for (moduleName in acVersionMap.keys) {
                if (moduleName == ACPREFIX + "Core") continue
                val pluginId = acVersionMap[moduleName]!!
                val data = readAsMap(URL("https://api.spiget.org/v2/resources/$pluginId"))
                modules.add(Module(
                    (data["name"] as String?)!!.replace("ActiveCraft-", ""),
                    pluginId,
                    data["tag"] as String?,
                    URL(
                        "https://spigotmc.org/" + ((data["file"] as Map<*, *>?)!!["url"] as String?)!!.split("download\\?".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0]),
                    if ((data["file"] as Map<*, *>?)!!["externalUrl"] != null) URL((data["file"] as Map<*, *>?)!!["externalUrl"] as String?) else URL(
                        "https://spigotmc.org/" + data["url"]
                    )
                ))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(ModuleException::class)
    fun enable(moduleName: String) {
        val plugin = getLoadedPlugin(moduleName)
        Bukkit.getPluginManager().enablePlugin(plugin)
    }

    @Throws(ModuleException::class)
    fun load(moduleName: String) {
        checkNotLoaded(moduleName)
        val pluginsDir = File("plugins")
        if (!pluginsDir.isDirectory) pluginsDir.mkdir()
        var pluginFile: File? = null
        for (f in pluginsDir.listFiles()!!) {
            if (!f.name.endsWith(".jar")) continue
            val desc: PluginDescriptionFile = try {
                ActiveCraftCore.INSTANCE.pluginLoader.getPluginDescription(f)
            } catch (ignored: InvalidDescriptionException) {
                throw ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED)
            }
            if (!desc.name.equals(ACPREFIX + moduleName, ignoreCase = true)) continue
            pluginFile = f
            break
        }
        try {
            Bukkit.getPluginManager().loadPlugin(pluginFile!!)
        } catch (e: InvalidPluginException) {
            throw ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED)
        } catch (e: InvalidDescriptionException) {
            throw ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED)
        }
    }

    @Throws(OperationFailureException::class, ModuleException::class)
    fun install(moduleName: String) {
        var f: File? = null
        try {
            f = getInstalled(moduleName)
        } catch (ignored: ModuleException) {
        }
        if (f != null) {
            throw ModuleException(moduleName, ModuleException.ErrorType.ALREADY_INSTALLED)
        }
        try {
            val module = getModule(moduleName)
            downloadFile(module.downloadUrl, "plugins" + File.separator + ACPREFIX + module.name + ".jar")
        } catch (e: IOException) {
            e.printStackTrace()
            throw OperationFailureException()
        }
    }

    @Throws(ModuleException::class)
    fun disable(moduleName: String) {
        Bukkit.getPluginManager().disablePlugin(getEnabledPlugin(moduleName))
    }

    @Throws(ModuleException::class)
    fun getLoadedPlugin(moduleName: String): Plugin {
        getModule(moduleName)
        return Bukkit.getPluginManager().getPlugin(ACPREFIX + moduleName)
            ?: throw ModuleException(moduleName, ModuleException.ErrorType.NOT_LOADED)
    }

    @Throws(ModuleException::class)
    fun checkNotLoaded(moduleName: String) {
        getModule(moduleName)
        if (Bukkit.getPluginManager().getPlugin(ACPREFIX + moduleName) != null) throw ModuleException(
            moduleName,
            ModuleException.ErrorType.ALREADY_LOADED
        )
    }

    @Throws(ModuleException::class)
    fun getEnabledPlugin(moduleName: String): Plugin {
        val plugin = getLoadedPlugin(moduleName)
        if (!plugin.isEnabled) throw ModuleException(moduleName, ModuleException.ErrorType.NOT_ENABLED)
        return plugin
    }

    @Throws(ModuleException::class)
    fun getDisabledPlugin(moduleName: String): Plugin {
        val plugin = getLoadedPlugin(moduleName)
        if (plugin.isEnabled) throw ModuleException(moduleName, ModuleException.ErrorType.ALREADY_ENABLED)
        return plugin
    }

    @Throws(ModuleException::class)
    fun getInstalled(moduleName: String): File {
        val pluginsDir = File("plugins")
        if (!pluginsDir.isDirectory) pluginsDir.mkdir()
        val pluginFile: File? = null
        for (f in pluginsDir.listFiles()!!) {
            if (!f.name.endsWith(".jar")) continue
            val desc: PluginDescriptionFile = try {
                ActiveCraftCore.INSTANCE.pluginLoader.getPluginDescription(f)
            } catch (ignored: InvalidDescriptionException) {
                throw ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED)
            }
            if (!desc.name.equals(ACPREFIX + moduleName, ignoreCase = true)) continue
            return f
        }
        throw ModuleException(moduleName, ModuleException.ErrorType.NOT_INSTALLED)
    }

    @Throws(ModuleException::class)
    fun getModule(moduleName: String): Module {
        return modules.firstOrNull() { module: Module -> module.name == moduleName }
            ?: throw ModuleException(moduleName, ModuleException.ErrorType.DOES_NOT_EXIST)
    }
}