package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pop
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.guis.profilemenu.item.ReasonItem
import org.activecraft.activecraftcore.guis.profilemenu.item.TimeItem
import org.activecraft.activecraftcore.manager.BanManager
import org.activecraft.activecraftcore.manager.BanManager.IP.ban
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.*

class ReasonsProfile(private val profileMenu: ProfileMenu, private val violationType: ViolationType) : GuiCreator(
    identifier = "reasons_profile",
    rows = 6,
    title = profileMenu.messageSupplier.getMessage(PREFIX + "title")
) {
    val player: Player = profileMenu.player
    var activeReason: String? = null
    private val target: Player = profileMenu.target
    private val profile: Profile = profileMenu.profile
    private val reasonsTimeInventory: Inventory? = null
    private val reasonsInventory: Inventory? = null
    var banTime = 0
    private var reasonStackHacking: GuiItem? = null
    private var reasonStackBotting: GuiItem? = null
    private var reasonStackUnauthorizedAltAcc: GuiItem? = null
    private var reasonStackSpam: GuiItem? = null
    private var reasonStackAbusiveLang: GuiItem? = null
    private var reasonStackScamming: GuiItem? = null
    private var reasonStackGriefing: GuiItem? = null
    private var m15Stack: GuiItem? = null
    private var h1Stack: GuiItem? = null
    private var h8Stack: GuiItem? = null
    private var d1Stack: GuiItem? = null
    private var d7Stack: GuiItem? = null
    private var M1Stack: GuiItem? = null
    private var permanentStack: GuiItem? = null
    private var confirmationStack: GuiItem? = null
    val messageSupplier: MessageSupplier = profileMenu.messageSupplier
    private val notSelectedStack = GuiItem(Material.RED_STAINED_GLASS_PANE)
        .setDisplayName(messageSupplier.getMessage(PREFIX + "not-selected")).setClickSound(null)
    private val selectedStack = GuiItem(Material.LIME_STAINED_GLASS_PANE)
        .setDisplayName(messageSupplier.getMessage(PREFIX + "selected")).setClickSound(null)

    enum class ViolationType {
        BAN, BAN_IP, WARN, KICK
    }

    init {
        for (i in 19..25) {
            setItem(notSelectedStack, i)
            if (violationType == ViolationType.BAN || violationType == ViolationType.BAN_IP) setItem(
                notSelectedStack,
                i + 18
            )
        }
    }

    fun select(start: Int, end: Int, selectedSlot: Int) {
        for (i in start until end) setItem(notSelectedStack, i)
        setItem(selectedStack, selectedSlot + 9)
    }

    override fun refresh() {
        setItem(profileMenu.defaultGuiCloseItem, 49)
        setItem(profileMenu.defaultGuiBackItem, 48)
        setItem(profileMenu.playerHead, 4)
        fillBackground(true)
        setItem(ReasonItem(messageSupplier.reasons.hacking, this).also { reasonStackHacking = it }, 10)
        setItem(ReasonItem(messageSupplier.reasons.botting, this).also { reasonStackBotting = it }, 11)
        setItem(ReasonItem(messageSupplier.reasons.abusiveLanguage, this).also { reasonStackAbusiveLang = it }, 12)
        setItem(ReasonItem(messageSupplier.reasons.spam, this).also { reasonStackSpam = it }, 13)
        setItem(ReasonItem(messageSupplier.reasons.griefing, this).also { reasonStackGriefing = it }, 14)
        setItem(ReasonItem(messageSupplier.reasons.stealing, this).also { reasonStackScamming = it }, 15)
        setItem(
            ReasonItem(
                messageSupplier.reasons.unauthorizedAlternateAccount,
                this
            ).also { reasonStackUnauthorizedAltAcc = it }, 16
        )
        setItem(GuiItem(Material.LIME_DYE).setDisplayName(messageSupplier.getMessage(PREFIX + "confirm"))
            .addClickListener {
                when (violationType) {
                    ViolationType.BAN -> {
                        if (!player.hasPermission("activecraft.ban")) {
                            player.sendMessage(messageSupplier.errors.noPermission)
                            return@addClickListener
                        }
                        val nowDate = Date()
                        val nowMillis = nowDate.time
                        val expires = if (banTime == -1) null else Date(banTime.toLong() * 60 * 1000 + nowMillis)
                        BanManager.Name.ban(profileMenu.target, activeReason, expires, player.name)
                        profileMenu.target.kickPlayer(activeReason)
                    }

                    ViolationType.BAN_IP -> {
                        if (!player.hasPermission("activecraft.ban")) {
                            player.sendMessage(messageSupplier.errors.noPermission)
                            return@addClickListener
                        }
                        val nowDate = Date()
                        val nowMillis = nowDate.time
                        val expires = if (banTime == -1) null else Date(banTime.toLong() * 60 * 1000 + nowMillis)
                        ban(target.address.address.toString().replace("/", ""), activeReason, expires, player.name)
                        profileMenu.target.kickPlayer(activeReason)
                    }

                    ViolationType.WARN -> {
                        player.performCommand("warn add " + profileMenu.target.name + " " + activeReason)
                    }

                    ViolationType.KICK -> {
                        if (player.hasPermission("activecraft.kick")) {
                            profileMenu.target.kickPlayer(activeReason)
                        } else player.sendMessage(messageSupplier.errors.noPermission)
                    }
                }
                pop(player)
            }.also { confirmationStack = it }, 50
        )
        if (violationType == ViolationType.BAN || violationType == ViolationType.BAN_IP) {
            setItem(TimeItem(15, messageSupplier.durations.minutes15, this).also { m15Stack = it }, 28)
            setItem(TimeItem(60, messageSupplier.durations.hour1, this).also { h1Stack = it }, 29)
            setItem(TimeItem(480, messageSupplier.durations.hours8, this).also { h8Stack = it }, 30)
            setItem(TimeItem(1440, messageSupplier.durations.day1, this).also { d1Stack = it }, 31)
            setItem(TimeItem(10080, messageSupplier.durations.days7, this).also { d7Stack = it }, 32)
            setItem(TimeItem(302400, messageSupplier.durations.month1, this).also { M1Stack = it }, 33)
            setItem(TimeItem(-1, messageSupplier.durations.permanent, this).also { permanentStack = it }, 34)
        }
    }

    companion object {
        private const val PREFIX = "profile.reasons-gui."
    }
}