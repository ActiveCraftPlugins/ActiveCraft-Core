package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage
import org.activecraft.activecraftcore.playermanagement.dialog.DialogManager
import org.activecraft.activecraftcore.playermanagement.tables.Profiles.writeToDatabase
import org.activecraft.activecraftcore.utils.ColorUtils
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.util.*

/*val Profiles.test: Column<Int>
    get() = integer("test")

fun registerExtensionTest() {
    Profiles.registerExtensionLoader {
        it[test] = 1
    }
}*/

class Profilev2(val uuid: UUID, build: Profilev2.() -> Unit) {

    // dialog krams hier rein
    lateinit var name: String
    lateinit var rawNickname: String
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
            if (ColorUtils.getColorsOnly().contains(value)) {
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
    var isDefaultmuted: Boolean = ActiveCraftCore.getInstance().mainConfig.isDefaultMuteEnabled
    var isVanished: Boolean = false
    var receiveLog: Boolean = false
        @JvmName("canReceiveLog") get
    var bypassLockdown: Boolean = false
        @JvmName("canBypassLockdown") get
    var receiveSocialspy: Boolean = false
        @JvmName("canReceiveSocialspy") get
    var editSign: Boolean = false
        @JvmName("canEditSign") get
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

    fun getMessageSupplier(acp: ActiveCraftPlugin) = languageManager.getMessageSupplier(acp.activeCraftMessagev2)

    val player: Player?
        get() = Bukkit.getPlayer(name)
    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(uuid)

    companion object {
        @JvmStatic
        fun of(profileName: String?): Profilev2? {
            return of(ActiveCraftCore.getInstance().playerlist.getUUIDByPlayername(profileName))
        }

        @JvmStatic
        fun of(uuid: UUID?): Profilev2? {
            return ActiveCraftCore.getInstance().profiles[uuid]
        }

        @JvmStatic
        fun of(player: Player): Profilev2? {
            return of(player.uniqueId)
        }

        @JvmStatic
        fun of(sender: CommandSender): Profilev2? {
            return of(sender.name)
        }

        @JvmStatic
        fun createIfNotExists(player: Player): Profilev2 {
            ActiveCraftCore.getInstance().playerlist.addPlayerIfAbsent(player)

            return of(player) ?: Profilev2(player.uniqueId) {
                name = player.name
                rawNickname = player.name
            }.also {
                ActiveCraftCore.getInstance().profiles[player.uniqueId] = it
                writeToDatabase(it)
            }
        }
    }
}
