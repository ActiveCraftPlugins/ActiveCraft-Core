package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.messages.ActiveCraftMessage
import org.activecraft.activecraftcore.playermanagement.dialog.DialogManager
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable.writeToDatabase
import org.activecraft.activecraftcore.utils.colorsOnly
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.util.*

class Profile(val uuid: UUID, build: Profile.() -> Unit) {

    // dialog krams hier rein
    lateinit var name: String
    lateinit var rawNickname: String
    val oldName: String
        get() = displayManager.getNickname()
    val nickname: String
        get() = displayManager.getNickname()
    var lastOnline: LocalDateTime? = LocalDateTime.now()
        get() {
            if (!isOnline)
                return field
            return null
        }
    val isOnline: Boolean
        get() = player != null
    var colorNick: ChatColor = ChatColor.WHITE
        set(value) {
            if (colorsOnly.contains(value)) {
                field = value
            }
        }
    var timesJoined: Int = 0
    var timesWarned: Int = 0
    var timesMuted: Int = 0
    var timesBanned: Int = 0
    var timesIpBanned: Int = 0
    var playtime: Int = 0
    var isAfk: Boolean = false
    var isGodmode: Boolean = false
    var isFly: Boolean = false
    var isMuted: Boolean = false
    var isVanished: Boolean = false
    var receiveLog: Boolean = false
    var bypassLockdown: Boolean = false
    var receiveSocialspy: Boolean = false
    var editSign: Boolean = false
    val warnManager: WarnManager
    val homeManager: HomeManager
    val effectManager: EffectManager
    val displayManager: DisplayManager
    val locationManager: LocationManager
    val languageManager: LanguageManager
    val dialogManager: DialogManager

    init {
        build(this)
        warnManager = WarnManager(this)
        homeManager = HomeManager(this)
        effectManager = EffectManager(this)
        displayManager = DisplayManager(this)
        locationManager = LocationManager(this)
        languageManager = LanguageManager(this)
        dialogManager = DialogManager(this)
    }

    fun getMessageSupplier(acm: ActiveCraftMessage) = languageManager.getMessageSupplier(acm)

    fun getMessageSupplier(acp: ActiveCraftPlugin) =
        acp.activeCraftMessage?.let { languageManager.getMessageSupplier(it) }

    val player: Player?
        get() = Bukkit.getPlayer(name)
    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(uuid)

    companion object {
        fun of(uuid: UUID?) = ActiveCraftCore.INSTANCE.profiles[uuid]

        fun of(profileName: String) = of(ActiveCraftCore.INSTANCE.playerlist.getUUIDByPlayername(profileName))

        fun of(player: Player) = of(player.uniqueId)!!

        fun of(sender: CommandSender) = of(sender.name)

        fun createIfNotExists(player: Player): Profile {
            ActiveCraftCore.INSTANCE.playerlist.addPlayerIfAbsent(player)

            return of(player.name) ?: Profile(player.uniqueId) {
                name = player.name
                rawNickname = player.name
            }.also {
                ActiveCraftCore.INSTANCE.profiles[player.uniqueId] = it
                writeToDatabase(it)
            }
        }
    }
}
