package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.strip
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class XpCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("xp", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var amount: String
        when (args[0].lowercase()) {
            "add" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                if (args.size == 2) {
                    assertCommandPermission(sender, "self")
                    val player = getPlayer(sender)
                    messageFormatter.addFormatterPattern("amount", strip(args[1].also { amount = it }, "l"))
                    if (amount.endsWith("l")) {
                        amount = strip(amount, "l")
                        player.giveExpLevels(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("add-levels-self"))
                    } else {
                        player.giveExp(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("add-xp-self"))
                    }
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                } else if (args.size >= 2) {
                    assertCommandPermission(sender, "others")
                    val target = getPlayer(args[1])
                    messageFormatter.addFormatterPattern("amount", strip(args[2].also { amount = it }, "l"))
                    messageFormatter.setTarget(getProfile(target))
                    if (amount.endsWith("l")) {
                        amount = strip(amount, "l")
                        if (!isTargetSelf(sender, target)) sendSilentMessage(
                            target,
                            this.cmdMsg("add-levels-target-message")
                        )
                        target.giveExpLevels(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("add-levels-others"))
                    } else {
                        if (!isTargetSelf(sender, target)) sendSilentMessage(
                            target,
                            this.cmdMsg("add-xp-target-message")
                        )
                        target.giveExp(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("add-xp-others"))
                    }
                    target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                }
            }

            "set" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                if (args.size == 2) {
                    assertCommandPermission(sender, "self")
                    val player = getPlayer(sender)
                    messageFormatter.addFormatterPattern("amount", strip(args[1].also { amount = it }, "l"))
                    if (amount.endsWith("l")) {
                        amount = strip(amount, "l")
                        player.level = 0
                        player.giveExpLevels(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("set-levels-self"))
                    } else {
                        player.level = 0
                        player.exp = 0f
                        player.giveExp(parseInt(args[1]))
                        sendMessage(sender, this.cmdMsg("set-xp-self"))
                    }
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                } else if (args.size >= 2) {
                    assertCommandPermission(sender, "others")
                    val target = getPlayer(args[1])
                    messageFormatter.addFormatterPattern("amount", strip(args[2].also { amount = it }, "l"))
                    messageFormatter.setTarget(getProfile(target))
                    if (amount.endsWith("l")) {
                        amount = strip(amount, "l")
                        if (!isTargetSelf(sender, target)) sendSilentMessage(target, this.cmdMsg("set-levels-self"))
                        target.level = 0
                        target.exp = 0f
                        target.giveExpLevels(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("set-levels-others"))
                    } else {
                        if (!isTargetSelf(sender, target)) sendSilentMessage(
                            target,
                            this.cmdMsg("set-xp-target-message")
                        )
                        target.level = 0
                        target.exp = 0f
                        target.giveExp(parseInt(amount))
                        sendMessage(sender, this.cmdMsg("set-xp-others"))
                    }
                    target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                }
            }

            "clear" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
                if (args.size == 1) {
                    assertCommandPermission(sender, "self")
                    val player = getPlayer(sender)
                    player.level = 0
                    player.exp = 0f
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    sendMessage(sender, this.cmdMsg("clear-self"))
                } else {
                    assertCommandPermission(sender, "others")
                    val target = getPlayer(args[1])
                    messageFormatter.setTarget(getProfile(target))
                    if (!isTargetSelf(sender, target)) sendSilentMessage(target, this.cmdMsg("clear-target-message"))
                    target.level = 0
                    target.exp = 0f
                    target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    sendMessage(sender, this.cmdMsg("clear-others"))
                }
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (args.size == 1) return listOf("add", "set", "clear")
        return if (args.size == 2) getBukkitPlayernames() else null
    }
}