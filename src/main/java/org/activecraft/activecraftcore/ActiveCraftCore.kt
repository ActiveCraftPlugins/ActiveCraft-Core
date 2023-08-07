package org.activecraft.activecraftcore

import org.activecraft.activecraftcore.commands.*
import org.activecraft.activecraftcore.guicreator.GuiListener
import org.activecraft.activecraftcore.guicreator.GuiManager
import org.activecraft.activecraftcore.guis.offinvsee.OffInvSeeListener
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.guis.profilemenu.listener.ProfileMenuListeners
import org.activecraft.activecraftcore.listener.*
import org.activecraft.activecraftcore.modules.Module
import org.activecraft.activecraftcore.modules.ModuleManager
import org.activecraft.activecraftcore.playermanagement.OfflinePlayerActionScheduler.initialize
import org.activecraft.activecraftcore.playermanagement.Playerlist
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable.loadAllProfiles
import org.activecraft.activecraftcore.sql.SQLManager
import org.activecraft.activecraftcore.utils.config.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.Consumer

val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

class ActiveCraftCore : ActiveCraftPlugin(95488, 12627) {
    val profiles: MutableMap<UUID, Profile> = mutableMapOf()
    val guiManager = GuiManager()
    val profileMenuList: MutableMap<Player, ProfileMenu> = mutableMapOf()
    lateinit var mainConfig: MainConfig
    lateinit var locationsConfig: LocationsConfig
    lateinit var warpsConfig: WarpsConfig
    lateinit var portalsConfig: PortalsConfig
    lateinit var playerlist: Playerlist
    private val sqlManager = SQLManager()
    private val features = HashMap<Feature, Boolean>()

    companion object {
        lateinit var INSTANCE: ActiveCraftCore
    }

    init {
        INSTANCE = this
    }

    override fun onPluginEnabled() {
        // connect to database
        sqlManager.init()

        // load warp perms
        loadWarpPermissions()

        //
        loadFeatures()

        // Playermanagement
        playerlist = Playerlist()
        createProfiles()
        initialize()

        // start playtime timer
        startTimer()

        // check for new plugin version
        checkForUpdate()
        bukkitLog(ChatColor.DARK_AQUA.toString() + "    ___   ______ ")
        bukkitLog(ChatColor.DARK_AQUA.toString() + "   /   | / ____/ ")
        bukkitLog(ChatColor.DARK_AQUA.toString() + "  / /| |/ /      " + ChatColor.GOLD + "ActiveCraft" + ChatColor.AQUA + " v" + description.version)
        bukkitLog(ChatColor.DARK_AQUA.toString() + " / ___ / /___    " + ChatColor.DARK_GRAY + "by CPlaiz and Silencio")
        bukkitLog(ChatColor.DARK_AQUA.toString() + "/_/  |_\\____/   ")
        val moduleIds = ModuleManager.modules.map(Module::id)
        val acmodulesString =
            installedPlugins.filter { plugin: ActiveCraftPlugin -> moduleIds.contains(plugin.spigotId) }
                .joinToString(ChatColor.DARK_AQUA.toString() + ", " + ChatColor.GOLD) { obj: ActiveCraftPlugin -> obj.name }
        if (acmodulesString.isNotEmpty()) bukkitLog(ChatColor.DARK_AQUA.toString() + "Installed Modules: " + ChatColor.GOLD + acmodulesString)
    }

    private fun loadWarpPermissions() {
        for (s in warpsConfig.warps.keys) {
            val childMap: MutableMap<String, Boolean> = mutableMapOf()
            childMap["activecraft.warp.self"] = true
            childMap["activecraft.warp"] = true
            if (Bukkit.getPluginManager().getPermission("activecraft.warp.self.$s") != null) Bukkit.getPluginManager()
                .addPermission(
                    Permission(
                        "activecraft.warp.self.$s",
                        "Permission to warp yourself to a specific warp.",
                        PermissionDefault.OP,
                        childMap
                    )
                )
            childMap.clear()
            childMap["activecraft.warp.others"] = true
            if (Bukkit.getPluginManager()
                    .getPermission("activecraft.warp.others.$s") != null
            ) childMap["activecraft.warp"] = true
            Bukkit.getPluginManager().addPermission(
                Permission(
                    "activecraft.warp.others.$s",
                    "Permission to warp another player to a specific warp.",
                    PermissionDefault.OP,
                    childMap
                )
            )
        }
    }

    override fun onPluginDisabled() {
        Bukkit.getOnlinePlayers().forEach { player: Player ->
            Profile.of(player).locationManager.setLastLocation(
                player.world,
                player.location,
                true
            )
        }
        log("Saving Data...")
        profiles.values.forEach { ProfilesTable.writeToDatabase(it) }
        log("Data saved!")
        log("Plugin unloaded.")
    }

    private fun startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            for (player in Bukkit.getOnlinePlayers()) {
                val profile = Profile.of(player)
                var playtime = profile.playtime
                playtime++
                if (!profile.isAfk) {
                    profile.playtime = playtime
                }
            }
        }, (20 * 60).toLong(), (20 * 60).toLong())
    }

    fun enableFeature(feature: Feature) {
        features[feature] = true
        mainConfig["features." + feature.identifier()] = true
    }

    fun disableFeature(feature: Feature) {
        features[feature] = false
        mainConfig["features." + feature.identifier()] = false
    }

    fun isFeatureEnabled(feature: Feature): Boolean {
        return features.getOrDefault(feature, false)
    }

    private fun loadFeatures() {
        mainConfig.features.forEach {
            val feature = Feature.fromIdentifier(it.key) ?: return@forEach
            features[feature] = it.value
        }
    }

    private fun createProfiles() {
        playerlist.load()
        profiles.clear()
        loadAllProfiles().forEach(Consumer { profile: Profile -> profiles[profile.uuid] = profile })
    }

    override fun registerConfigs() {
        saveDefaultConfig()
        ConfigManager.registerConfigs(this,
            MainConfig().also {
                mainConfig = it
            }, PortalsConfig().also { portalsConfig = it },
            WarpsConfig().also {
                warpsConfig = it
            }, LocationsConfig().also { locationsConfig = it })
        val playerdataDir = File(dataFolder.toString() + File.separator + "playerdata" + File.separator)
        if (!playerdataDir.exists()) playerdataDir.mkdir()
    }

    override fun register() {
        // listeners
        pluginManager.addListeners(
            JoinQuitListener(), MessageListener(), CommandListener(),
            LockdownListener(), SignListener(), SignInteractListener(),
            VanillaCommandListener(), ServerPingListener(), RespawnListener(),
            DeathListener(), LoginListener(), BowCommand(this), GamemodeChangeListener(),
            TabCompleteListener(), CommandStickCommand(this), TeleportListener()
        )

        // gui creator
        pluginManager.addListeners(GuiListener())

        // guis
        pluginManager.addListeners(OffInvSeeListener(), ProfileMenuListeners())

        // commands
        pluginManager.addCommands(
            ACLanguageCommand(this),
            ACModulesCommand(this),
            ACReloadCommand(this),
            ACVersionCommand(this),
            BackCommand(this),
            AfkCommand(this),
            BookCommand(this),
            BowCommand(this),
            BreakCommand(this),
            BroadCastCommand(this),
            ButcherCommand(this),
            ClearInvCommand(this),
            ColorNickCommand(this),
            CommandStickCommand(this),
            DrainCommand(this),
            EditSignCommand(this),
            EffectsCommand(this),
            EnchantCommand(this),
            EnderchestCommand(this),
            ExplodeCommand(this),
            FeedCommand(this),
            FireBallCommand(this),
            FireWorkCommand(this),
            FlyCommand(this),
            FlyspeedCommand(this),
            GodCommand(this),
            HatCommand(this),
            HealCommand(this),
            InvseeCommand(this),
            ItemCommand(this),
            KickAllCommand(this),
            KickCommand(this),
            KnockbackStickCommand(this),
            LanguageCommand(this),
            LastCoordsCommand(this),
            LastOnlineCommand(this),
            LeatherColorCommand(this),
            LockChatCommand(this),
            LogCommand(this),
            MoreCommand(this),
            MsgCommand(this),
            NickCommand(this),
            OffInvSeeCommand(this),
            OpItemsCommand(this),
            PingCommand(this),
            PlayerlistCommand(this),
            PlayTimeCommand(this),
            PortalCommand(this),
            ProfileCommand(this),
            RamCommand(this),
            RandomTPCommand(this),
            RealNameCommand(this),
            RepairCommand(this),
            ReplyCommand(this),
            RestartCommand(this),
            SetspawnCommand(this),
            SkullCommand(this),
            SpawnCommand(this),
            SpawnerCommand(this),
            StaffChatCommand(this),
            StrikeCommand(this),
            SudoCommand(this),
            SuicideCommand(this),
            SummonCommand(this),
            TableMenuCommand(this),
            ToggleSocialSpyCommand(this),
            TopCommand(this),
            TpAllCommand(this),
            TpCommand(this),
            TphereCommand(this),
            VanishCommand(this),
            WalkspeedCommand(this),
            WarnCommand(this),
            WeatherCommand(this),
            WhereAmICommand(this),
            WhoIsCommand(this),
            XpCommand(this)
        )
        pluginManager.addCommandCollections(
            BanCommandCollection(this),
            GamemodeCommandCollection(this),
            HomeCommandCollection(this),
            LockdownCommandCollection(this),
            MuteCommandCollection(this),
            OfflineTpCommandCollection(this),
            TableCommandCollection(this),
            TpaCommandCollection(this),
            WarpCommandCollection(this)
        )
    }

}