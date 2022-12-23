package org.activecraft.activecraftcore

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.activecraft.activecraftcore.commands.CommandExceptionProcessor
import org.activecraft.activecraftcore.exceptions.StartupException
import org.activecraft.activecraftcore.messages.ActiveCraftMessage
import org.activecraft.activecraftcore.utils.UpdateChecker
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Level

abstract class ActiveCraftPlugin @JvmOverloads constructor(
    val spigotId: Int = 0,
    protected val bStatsId: Int = 0,
    val permissionGroup: String? = null,
    useActiveCraftMessage: Boolean = true
) : JavaPlugin() {
    val activeCraftMessage = if (useActiveCraftMessage) ActiveCraftMessage(this) else null
    var pluginManager = PluginManager(this)
    private var metrics = if (bStatsId != 0) Metrics(this, bStatsId) else null
    var commandExceptionProcessor = CommandExceptionProcessor()

    init {
        installedPlugins.add(this)
    }

    override fun onEnable() {
        registerConfigs()
        try {
            onPluginEnabled()
        } catch (e: StartupException) {
            error("Startup error" + if (e.message == "") "" else ": " + e.message)
            e.printStackTrace()
            if (e.shutdown) {
                isEnabled = false
                return
            }
        }
        register()
    }

    override fun onDisable() {
        onPluginDisabled()
    }

    @Throws(StartupException::class)
    abstract fun onPluginEnabled()
    abstract fun onPluginDisabled()

    @JvmOverloads
    fun log(text: String, level: Level = Level.INFO) = log(this, text, level)

    fun error(text: String) = log(text, Level.SEVERE)

    fun warning(text: String) = log(text, Level.SEVERE)

    fun checkForUpdate() {
        assert(spigotId != 0)
        UpdateChecker(this, spigotId).getVersion { version: String ->
            if (description.version != version) {
                logger.info("There is a new update available.")
                logger.info("Download it at: https://www.spigotmc.org/resources/$spigotId")
                ActiveCraftCore.INSTANCE.pluginManager.addListeners(object : Listener {
                    var alreadyNotified = false

                    @EventHandler
                    fun onOperatorJoin(event: PlayerJoinEvent) {
                        val player = event.player
                        if (!player.isOp || alreadyNotified) return
                        val builder = ComponentBuilder()
                        builder.append(
                            TextComponent(
                                ChatColor.GOLD.toString() + name
                                        + ChatColor.DARK_AQUA + " - " + ChatColor.RED + "Update Available. "
                            )
                        )
                        builder.append(
                            TextComponent(
                                """${ChatColor.RED}
Current: ${ChatColor.DARK_RED}${description.version}${ChatColor.RED} Newest: ${ChatColor.DARK_RED}$version${ChatColor.RED}."""
                            )
                        )
                        val linkComponent = TextComponent()
                        linkComponent.text = ChatColor.AQUA.toString() + " [Link]"
                        linkComponent.hoverEvent =
                            HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to open link"))
                        linkComponent.clickEvent = ClickEvent(
                            ClickEvent.Action.OPEN_URL,
                            "https://www.spigotmc.org/resources/" + name.lowercase() + "." + spigotId
                        )
                        builder.append(linkComponent)
                        player.sendMessage(*builder.create())
                        alreadyNotified = true
                    }
                })
            }
        }
    }

    @Throws(StartupException::class)
    protected fun disableIfDependencyMissing(dependency: String, minVersion: String) {
        disableIfDependancyMissing(this, dependency, minVersion, minVersion.replace("[1-9a-zA-Z]".toRegex(), "0"))
    }

    @Throws(StartupException::class)
    protected fun disableIfDependencyMissing(dependency: String, minVersion: String, maxVersion: String) {
        disableIfDependancyMissing(this, dependency, minVersion, maxVersion)
    }

    protected abstract fun registerConfigs()
    protected abstract fun register()

    companion object {
        @JvmStatic
        val installedPlugins: MutableSet<ActiveCraftPlugin> = mutableSetOf()

        @JvmStatic
        fun of(name: String) = installedPlugins.find { it.name.equals(name, ignoreCase = true) }

        @JvmStatic
        fun of(plugin: Plugin) = of(plugin.name)

        @JvmStatic
        @JvmOverloads
        fun log(plugin: Plugin, text: String, level: Level = Level.INFO) = plugin.logger.log(level, text)

        @JvmStatic
        fun error(plugin: Plugin, text: String) = log(plugin, text, Level.SEVERE)

        @JvmStatic
        fun warning(plugin: Plugin, text: String) = log(plugin, text, Level.WARNING)

        @JvmStatic
        @JvmOverloads
        fun bukkitLog(text: String, level: Level = Level.INFO) = Bukkit.getLogger().log(level, text)

        @JvmStatic
        fun getActiveCraftPlugin(name: String) = installedPlugins.find { it.name == name }


        @JvmStatic
        private fun isCompatibleVersion(version: String, minVersion: String, maxVersion: String): Boolean {
            val minSplit = minVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val maxSplit = maxVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val verSplit = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            require(!(minSplit.size != maxSplit.size && maxSplit.size != verSplit.size)) { "Invalid version string" }
            val minVersionNumber = StringBuilder()
            val maxVersionNumber = StringBuilder()
            val verVersionNumber = StringBuilder()
            for (i in minSplit.indices) {
                val minLen = minSplit[i].length
                val maxLen = maxSplit[i].length
                val verLen = verSplit[i].length
                val longest = minLen.coerceAtLeast(maxLen).coerceAtLeast(verLen)
                minVersionNumber.append(fillWithZero(minSplit[i], longest))
                maxVersionNumber.append(fillWithZero(maxSplit[i], longest))
                verVersionNumber.append(fillWithZero(verSplit[i], longest))
            }
            return try {
                val min = minVersionNumber.toString().replace("_", "").replace("*", "0").toLong()
                val max = maxVersionNumber.toString().replace("_", "").replace("*", "9").toLong()
                val ver = verVersionNumber.toString().replace("_", "").toLong()
                if (max == 0L) return ver >= min
                if (min == 0L) return ver <= max
                require(min <= max) { "Invalid version string" }
                ver in min..max
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid version string")
            }
        }

        @JvmStatic
        fun isDependencyPresent(dependency: String?): Boolean {
            return Bukkit.getPluginManager().isPluginEnabled(dependency!!)
        }

        @JvmStatic
        fun isDependencyPresent(dependency: String?, minVersion: String): Boolean {
            return if (!Bukkit.getPluginManager().isPluginEnabled(dependency!!)) false else !isCompatibleVersion(
                Bukkit.getPluginManager().getPlugin(dependency)!!.description.version,
                minVersion,
                minVersion.replace("[1-9a-zA-Z]".toRegex(), "0")
            )
        }

        @JvmStatic
        fun isDependencyPresent(dependency: String?, minVersion: String, maxVersion: String): Boolean {
            return if (!Bukkit.getPluginManager().isPluginEnabled(dependency!!)) false else !isCompatibleVersion(
                Bukkit.getPluginManager().getPlugin(dependency)!!.description.version, minVersion, maxVersion
            )
        }

        @JvmStatic
        @Throws(StartupException::class)
        protected fun disableIfDependancyMissing(
            activeCraftPlugin: ActiveCraftPlugin,
            dependency: String,
            minVersion: String,
            maxVersion: String
        ) {
            if (!isDependencyPresent(dependency)) {
                activeCraftPlugin.error("*** $dependency is not installed or not enabled! ***")
                activeCraftPlugin.error("*** This plugin will be disabled! ***")
                throw StartupException(true, "Dependancy missing")
            } else if (!isCompatibleVersion(
                    Bukkit.getPluginManager().getPlugin(dependency)!!.description.version, minVersion, maxVersion
                )
            ) {
                activeCraftPlugin.error(
                    "*** The version of " + dependency + " you are using is incompatible with "
                            + activeCraftPlugin.name + " v" + activeCraftPlugin.description.version + "! ***"
                )
                activeCraftPlugin.error(
                    "*** Please use " + dependency + " " +
                            (if (isAnyVersion(minVersion)) "until" else minVersion) +
                            (if (!isAnyVersion(minVersion) && !isAnyVersion(maxVersion)) " - " else " ") +
                            (if (isAnyVersion(maxVersion)) "or later" else maxVersion) + " ***"
                )
                activeCraftPlugin.error("*** This plugin will be disabled! ***")
                throw StartupException(true, "Incompatible version")
            }
        }

        @JvmStatic
        private fun fillWithZero(target: String, max: Int): String {
            val targetBuilder = StringBuilder(target)
            for (i in target.length until max) {
                targetBuilder.insert(0, "0")
            }
            return targetBuilder.toString()
        }

        @JvmStatic
        private fun isAnyVersion(version: String) =
            version.replace(".", "").replace("0", "") == ""

    }
}