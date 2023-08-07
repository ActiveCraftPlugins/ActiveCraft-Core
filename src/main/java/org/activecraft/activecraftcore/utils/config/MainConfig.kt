package org.activecraft.activecraftcore.utils.config

class MainConfig : ActiveCraftConfig(FileConfig("config.yml")) {
    // TODO: 29.08.2022 test ConfigValue stuff

    var lockdownModt: String? by configValue("lockdown.modt", null)
    var oldModt: String? by configValue("lockdown.old-modt", null)
    var hiddenCommandsAfterPluginNameExceptions: List<String> by configValue(
        "hide-commands-after-plugin-name.except",
        listOf()
    )
    var hiddenCommands: List<String> by configValue("hide-commands", listOf())
    var databaseDialect: String? by configValue("database.dialect", null)
    var databaseLocalPath: String? by configValue("database.local-path", null)
    var databaseHost: String? by configValue("database.host", null)
    var databaseNetworkPath: String? by configValue("database.network-path", null)
    var databaseUser: String? by configValue("database.user", null)
    var databasePassword: String? by configValue("database.password", null)
    var databasePort by configValue("database.port", 3306)
    var timerTpa by configValue("tpa-timer", false)
    var timerSpawn by configValue("spawn-timer", false)
    var timerHome by configValue("home-timer", false)
    var timerWarp by configValue("warp-timer", false)
    var vanishTagEnabled by configValue("vanish-tag.enabled", false)
    var announceAfk by configValue("afk.announce", false)
    var lockedDown by configValue("lockdown.enabled", false)
    var lockChat by configValue("lock-chat", false)
    var socialSpyToConsole by configValue("socialspy-to-console", false)
    var isInSilentMode by configValue("silent-mode", false)
    var dropAllExp by configValue("drop-all-exp", false)
    var hideCommandsAfterPluginName by configValue("hide-commands-after-plugin-name.enabled", false)
    val sendJoinMessage by configValue("send-join-message", false)
    val sendQuitMessage by configValue("send-quit-message", false)
    val useCustomChatFormat by configValue("use-custom-chat-format", false)
    val features: Map<String, Boolean> by configValue("features", mapOf())

    override fun load() {}
}