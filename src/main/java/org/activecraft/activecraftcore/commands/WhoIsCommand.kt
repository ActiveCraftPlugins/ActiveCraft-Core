package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.strip
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class WhoIsCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("whois", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val target: Player
        if (args.isEmpty()) {
            assertCommandPermission(sender, "self")
            target = getPlayer(sender)
        } else if (args.size == 1) {
            assertCommandPermission(sender, "others")
            target = getPlayer(args[0])
            isTargetSelf(sender, target)
        } else throw InvalidArgumentException()
        val profile: Profile = getProfile(target)
        messageFormatter.addFormatterPatterns(
            "playername" to target.name,
            "displayname" to profile.nickname,
            "colornick" to profile.colorNick.name.lowercase(),
            "uuid" to target.uniqueId.toString(),
            "op" to target.isOp.toString(),
            "playerhealth" to target.health.toString(),
            "playerfood" to target.foodLevel.toString(),
            "world" to target.world.name,
            "coords" to ChatColor.GOLD.toString() + "X: " + ChatColor.AQUA + target.location.blockX
                    + ChatColor.GOLD + ", Y: " + ChatColor.AQUA + target.location.blockY
                    + ChatColor.GOLD + ", Z: " + ChatColor.AQUA + target.location.blockZ,
            "afk" to profile.isAfk.toString(),
            "client" to (target.clientBrandName ?: "?"),
            "ip" to strip(target.address.toString(), "/"),
            "gamemode" to target.gameMode.name.lowercase(),
            "muted" to profile.isMuted.toString(),
            "whitelisted" to target.isWhitelisted.toString(),
            "god" to profile.isGodmode.toString(),
            "vanished" to profile.isVanished.toString()
        )
        listOf(
            "name", "nickname", "colornick",
            "uuid", "is-op", "health",
            "food", "world", "coords",
            "afk", "client", "address",
            "gamemode", "muted", "whitelisted",
            "god", "vanished"
        ).forEach { sendMessage(sender, this.cmdMsg(it)) }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}