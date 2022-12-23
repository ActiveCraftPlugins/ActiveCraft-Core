package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.WarpManager.createWarp
import org.activecraft.activecraftcore.manager.WarpManager.deleteWarp
import org.activecraft.activecraftcore.manager.WarpManager.getWarp
import org.activecraft.activecraftcore.manager.WarpManager.warp
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class WarpCommandCollection(plugin: ActiveCraftPlugin?) : ActiveCraftCommandCollection(
    WarpCommand(plugin),
    SetwarpCommand(plugin),
    DelwarpCommand(plugin),
    WarpsCommand(plugin)
) {
    class WarpCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("warp", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            var args = args
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val type = if (args.size >= 2) CommandTargetType.OTHERS else CommandTargetType.SELF
            args = args.copyOfRange(if (args.size >= 2) 1 else 0, args.size)
            val warpname = args[0]
            assertCommandPermission(sender, type.code() + "." + args[0])
            val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
            messageFormatter.addFormatterPattern("warp", warpname)
            messageFormatter.setTarget(getProfile(target))
            if (getWarp(warpname) == null) {
                sendWarningMessage(sender, rawCmdMsg("does-not-exist"))
                return
            }
            if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target, warpname)) sendSilentMessage(
                target,
                this.cmdMsg("target-message")
            )
            warp(target, warpname)
            sendMessage(sender, this.cmdMsg(type.code()))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ): List<String>? {
            if (args.size == 1) {
                return ActiveCraftCore.INSTANCE.warpsConfig.warps.keys
                    .filter { s -> sender.hasPermission("activecraft.warp.self.$s") } + getBukkitPlayernames()
            } else if (args.size == 2 && Bukkit.getPlayer(args[0]) != null) {
                return ActiveCraftCore.INSTANCE.warpsConfig.warps.keys
                    .filter { sender.hasPermission("activecraft.warp.others.$it") }
            }
            return null
        }
    }

    class DelwarpCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("delwarp", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            assertCommandPermission(sender)
            val warpname = args[0]
            messageFormatter.addFormatterPattern("warp", warpname)
            if (getWarp(warpname) == null) {
                sendWarningMessage(sender, rawCmdMsg("does-not-exist"))
                return
            }
            deleteWarp(warpname)
            sendMessage(sender, this.cmdMsg("delwarp"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) ActiveCraftCore.INSTANCE.warpsConfig.warps.keys.toList() else null

    }

    class SetwarpCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("setwarp", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            assertCommandPermission(sender)
            val player = getPlayer(sender)
            val warpname = args[0]
            messageFormatter.addFormatterPattern("warp", warpname)
            if (getWarp(warpname) != null) {
                sendWarningMessage(sender, rawCmdMsg("already-exists"))
                return
            }
            createWarp(warpname, player.location)
            sendMessage(sender, this.cmdMsg("setwarp"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = null
    }

    class WarpsCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("warps", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 0)
            assertCommandPermission(sender)
            val warps: Map<String, Location> = ActiveCraftCore.INSTANCE.warpsConfig.warps
            if (warps.isEmpty()) {
                sendWarningMessage(sender, rawCmdMsg("no-warps"))
                return
            }
            val message = StringBuilder()
            for (s in warps.keys) {
                val loc = warps[s]
                if (sender.hasPermission("activecraft.warp.self.$s") || sender.hasPermission("activecraft.warp.others.$s")) message.append(
                    """
    ${ChatColor.GOLD}$s: ${ChatColor.GRAY}${loc!!.world.name}; ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}
    
    """.trimIndent()
                )
            }
            if (message.toString() != "") {
                sendMessage(sender, this.cmdMsg("header"))
                sendMessage(sender, message.toString())
                return
            }
            sendWarningMessage(sender, rawCmdMsg("no-warps"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = null
    }
}