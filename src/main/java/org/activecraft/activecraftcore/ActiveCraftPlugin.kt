package org.activecraft.activecraftcore

import lombok.Getter
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.activecraft.activecraftcore.commands.ActiveCraftCommand
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

@Getter
abstract class ActiveCraftPlugin @JvmOverloads constructor(
    protected val spigotId: Int = 0,
    protected val bStatsId: Int = 0,
    @JvmField val permissionGroup: String? = "activecraft",
    useActiveCraftMessage: Boolean = true
) : JavaPlugin() {
    protected var activeCraftMessage: ActiveCraftMessage? = null
    var activeCraftMessagev2: org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage? = null
        protected set
    protected lateinit var pluginManager: PluginManager
    protected var metrics: Metrics? = null
    protected var registeredCommands = HashMap<String, ActiveCraftCommand>()
    var commandExceptionProcessor: CommandExceptionProcessor? = null
        protected set

    init {
        if (bStatsId != 0) metrics = Metrics(this, bStatsId)
        if (useActiveCraftMessage) {
            activeCraftMessage = ActiveCraftMessage(this)
            activeCraftMessagev2 = org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage(this)
        }
        pluginManager = PluginManager(this)
        commandExceptionProcessor = CommandExceptionProcessor(this)
        installedPlugins.add(this)
    }

    override fun onEnable() {
        registerConfigs()
        try {
            onPluginEnabled()
        } catch (e: StartupException) {
            error("Startup error" + if (e.message == "") "" else ": " + e.message)
            if (e.isShutdown) {
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
    fun log(level: Level?, text: String?) {
        log(this, level, text)
    }

    fun log(text: String?) {
        log(Level.INFO, text)
    }

    fun error(text: String?) {
        log(Level.SEVERE, text)
    }

    fun warning(text: String?) {
        log(Level.WARNING, text)
    }

    fun checkForUpdate() {
        assert(spigotId != 0)
        UpdateChecker(this, spigotId).getVersion { version: String ->
            if (description.version != version) {
                logger.info("There is a new update available.")
                logger.info("Download it at: https://www.spigotmc.org/resources/$spigotId")
                ActiveCraftCore.getInstance().getPluginManager().addListeners(object : Listener {
                    private static
                    var alreadyNotified = false
                    @EventHandler
                    fun onOperatorJoin(event: PlayerJoinEvent) {
                        val player = event.player
                        if (!player.isOp || alreadyNotified) return@getVersion
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
                            "https://www.spigotmc.org/resources/" + name.lowercase(Locale.getDefault()) + "." + spigotId
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
    protected fun disableIfDependancyMissing(dependency: String, minVersion: String) {
        disableIfDependancyMissing(this, dependency, minVersion, minVersion.replace("[1-9a-zA-Z]".toRegex(), "0"))
    }

    @Throws(StartupException::class)
    protected fun disableIfDependancyMissing(dependency: String, minVersion: String, maxVersion: String) {
        disableIfDependancyMissing(this, dependency, minVersion, maxVersion)
    }

    protected abstract fun registerConfigs()
    protected abstract fun register()

    companion object {
        private val installedPlugins: MutableSet<ActiveCraftPlugin?> = HashSet()
        @JvmStatic
        fun of(name: String?): ActiveCraftPlugin? {
            return installedPlugins.stream()
                .filter { plugin: ActiveCraftPlugin? -> plugin!!.name.equals(name, ignoreCase = true) }
                .findFirst().orElse(null)
        }

        fun of(plugin: Plugin?): ActiveCraftPlugin? {
            assert(plugin != null)
            return of(plugin!!.name)
        }

        fun log(plugin: Plugin, level: Level?, text: String?) {
            plugin.logger.log(level, text)
        }

        fun log(plugin: Plugin, text: String?) {
            log(plugin, Level.INFO, text)
        }

        fun error(plugin: Plugin, text: String?) {
            log(plugin, Level.SEVERE, text)
        }

        fun warning(plugin: Plugin, text: String?) {
            log(plugin, Level.WARNING, text)
        }

        fun bukkitLog(level: Level?, text: String?) {
            Bukkit.getLogger().log(level, text)
        }

        fun bukkitLog(text: String?) {
            Bukkit.getLogger().log(Level.INFO, text)
        }

        fun getActiveCraftPlugin(name: String): ActiveCraftPlugin? {
            return installedPlugins.stream().filter { plugin: ActiveCraftPlugin? -> plugin!!.name == name }
                .findAny().orElse(null)
        }

        fun isCompatibleVersion(version: String, minVersion: String, maxVersion: String): Boolean {
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
                val longest = Math.max(Math.max(minLen, maxLen), verLen)
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
                ver >= min && ver <= max
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid version string")
            }
        }

        fun isDependancyPresent(dependency: String?): Boolean {
            return Bukkit.getPluginManager().isPluginEnabled(dependency!!)
        }

        fun isDependancyPresent(dependency: String?, minVersion: String): Boolean {
            return if (!Bukkit.getPluginManager().isPluginEnabled(dependency!!)) false else !isCompatibleVersion(
                Bukkit.getPluginManager().getPlugin(dependency)!!.description.version,
                minVersion,
                minVersion.replace("[1-9a-zA-Z]".toRegex(), "0")
            )
        }

        fun isDependancyPresent(dependency: String?, minVersion: String, maxVersion: String): Boolean {
            return if (!Bukkit.getPluginManager().isPluginEnabled(dependency!!)) false else !isCompatibleVersion(
                Bukkit.getPluginManager().getPlugin(dependency)!!.description.version, minVersion, maxVersion
            )
        }

        @Throws(StartupException::class)
        protected fun disableIfDependancyMissing(
            activeCraftPlugin: ActiveCraftPlugin,
            dependency: String,
            minVersion: String,
            maxVersion: String
        ) {
            if (!isDependancyPresent(dependency)) {
                activeCraftPlugin.error("*** $dependency is not installed or not enabled! ***")
                activeCraftPlugin.error("*** This plugin will be disabled! ***")
                throw StartupException("Dependancy missing")
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
                throw StartupException("Incompatible version")
            }
        }

        private fun fillWithZero(target: String, max: Int): String {
            val targetBuilder = StringBuilder(target)
            for (i in target.length until max) {
                targetBuilder.insert(0, "0")
            }
            return targetBuilder.toString()
        }

        private fun isAnyVersion(version: String): Boolean {
            return version.replace(".", "").replace("0", "") == ""
        }

        @JvmStatic
        fun getInstalledPlugins(): Set<ActiveCraftPlugin?> {
            return installedPlugins
        }
    }
}