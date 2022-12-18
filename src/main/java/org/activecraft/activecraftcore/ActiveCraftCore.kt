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
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.activecraft.activecraftcore.playermanagement.Profilev2.Companion.of
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable.loadAllProfiles
import org.activecraft.activecraftcore.sql.SQLManager
import org.activecraft.activecraftcore.utils.StringUtils
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
    private val profileMenuList = HashMap<Player, ProfileMenu>()
    @JvmField
    val profiles = HashMap<UUID, Profilev2>()
    private val guiManager = GuiManager()
    lateinit var mainConfig: MainConfig
    lateinit var locationsConfig: LocationsConfig
    lateinit var warpsConfig: WarpsConfig
    lateinit var portalsConfig: PortalsConfig
    lateinit var playerlist: Playerlist
    private val sqlManager = SQLManager()
    private val features = HashMap<Feature, Boolean>()

    companion object {
        lateinit var instance: ActiveCraftCore
    }

    init {
        instance = this
    }

    override fun onPluginEnabled() {
        // connect to database
        sqlManager.init()

        // load warp perms
        loadWarpPermissions()

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
        val moduleIds = ModuleManager.getModules().stream().map(Module::id).toList()
        val acmodulesString = StringUtils.combineList(
            getInstalledPlugins().stream()
                .filter { plugin: ActiveCraftPlugin -> moduleIds.contains(plugin.spigotId) }
                .map { obj: ActiveCraftPlugin -> obj.name }
                .toList(),
            ChatColor.DARK_AQUA.toString() + ", " + ChatColor.GOLD
        )
        if (acmodulesString.isNotEmpty()) bukkitLog(ChatColor.DARK_AQUA.toString() + "Installed Modules: " + ChatColor.GOLD + acmodulesString)
    }

    private fun loadWarpPermissions() {
        for (s in warpsConfig.warps.keys) {
            val childMap: MutableMap<String, Boolean> = HashMap()
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
            of(player)!!.locationManager.setLastLocation(
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
                val profile = of(player)!!
                var playtime = profile.playtime
                playtime++
                if (!profile.isAfk) {
                    profile.playtime = playtime
                }
                if (mainConfig.defaultMuteDuration in 0..playtime) {
                    if (profile.isDefaultmuted) {
                        player.sendMessage(activeCraftMessage.getMessage("misc.default-mute-remove")) // TODO: 26.08.2022 zu msg supplier von profie ändern
                        profile.isDefaultmuted = false
                    }
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

    private fun createProfiles() {
        playerlist.load()
        profiles.clear()
        loadAllProfiles().forEach(Consumer { profile: Profilev2 -> profiles[profile.uuid] = profile })
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
        //Bukkit.getPluginCommand("atestcommand")!!.setExecutor(ATestCommand()) // TODO: 30.08.2022 remove test
        pluginManager.addCommands(
            ACLanguageCommand(this),  //new ACModulesCommand(this),
            //new ACReloadCommand(this),
            //new ACVersionCommand(this),
            //new BackCommand(this),
            AfkCommand(this),  //new BookCommand(this),
            //new BowCommand(this),
            //new BreakCommand(this),
            //new BroadCastCommand(this),
            //new ButcherCommand(this),
            //new ClearInvCommand(this),
            //new ColorNickCommand(this),
            //new CommandStickCommand(this),
            //new DrainCommand(this),
            //new EditSignCommand(this),
            //new EffectsCommand(this),
            //new EnchantCommand(this),
            //new EnderchestCommand(this),
            //new ExplodeCommand(this),
            //new FeedCommand(this),
            //new FireBallCommand(this),
            //new FireWorkCommand(this),
            //new FlyCommand(this),
            //new FlyspeedCommand(this),
            //new GodCommand(this),
            //new HatCommand(this),
            //new HealCommand(this),
            //new InvseeCommand(this),
            //new ItemCommand(this),
            //new KickAllCommand(this),
            //new KickCommand(this),
            //new KnockbackStickCommand(this),
            //new LanguageCommand(this),
            //new LastCoordsCommand(this),
            //new LastOnlineCommand(this),
            //new LeatherColorCommand(this),
            //new LockChatCommand(this),
            //new LogCommand(this),
            //new MoreCommand(this),
            //new MsgCommand(this),
            //new NickCommand(this),
            //new OffInvSeeCommand(this),
            //new OpItemsCommand(this),
            //new PingCommand(this),
            PlayerlistCommand(this) //new PlayTimeCommand(this),
            //new PortalCommand(this),
            //new ProfileCommand(this),
            //new RamCommand(this),
            //new RandomTPCommand(this),
            //new RealNameCommand(this),
            //new RepairCommand(this),
            //new ReplyCommand(this),
            //new RestartCommand(this),
            //new SetspawnCommand(this),
            //new SkullCommand(this),
            //new SpawnCommand(this),
            //new SpawnerCommand(this),
            //new StaffChatCommand(this),
            //new StrikeCommand(this),
            //new SudoCommand(this),
            //new SuicideCommand(this),
            //new SummonCommand(this),
            //new TableMenuCommand(this),
            //new ToggleSocialSpyCommand(this),
            //new TopCommand(this),
            //new TpAllCommand(this),
            //new TpCommand(this),
            //new TphereCommand(this),
            //new VanishCommand(this),
            //new VerifyCommand(this),
            //new WalkspeedCommand(this),
            //new WarnCommand(this),
            //new WeatherCommand(this),
            //new WhereAmICommand(this),
            //new WhoIsCommand(this),
            //new XpCommand(this)
        )

        // commands
        pluginManager.addCommands( //new ACLanguageCommand(this),
            ACModulesCommand(this),
            ACReloadCommand(this),
            ACVersionCommand(this),
            BackCommand(this),  //new AfkCommand(this),
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
            PingCommand(this),  //new PlayerlistCommand(this),
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
            VerifyCommand(this),
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