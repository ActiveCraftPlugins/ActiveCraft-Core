package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.manager.PortalManager.clean
import org.activecraft.activecraftcore.manager.PortalManager.create
import org.activecraft.activecraftcore.manager.PortalManager.destroy
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class PortalCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("portal", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        clean()
        val portalList: Set<String> = ActiveCraftCore.INSTANCE.portalsConfig.portals.keys
        when (args[0].lowercase()) {
            "create" -> {
                assertCommandPermission(sender, "create")
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 8)
                create(
                    args[1],
                    parseInt(args[2]),
                    parseInt(args[3]),
                    parseInt(args[4]),
                    player.world,
                    parseInt(args[5]),
                    parseInt(args[6]),
                    parseInt(args[7]),
                    if (args.size == 9) getWorld(args[8]) else player.world
                )
                sendMessage(sender, this.cmdMsg("created"))
            }

            "destroy" -> {
                assertCommandPermission(sender, "destroy")
                if (!portalList.contains(args[1])) {
                    sendWarningMessage(sender, rawCmdMsg("does-not-exist"))
                    return
                }
                val (_, x, y, z) = ActiveCraftCore.INSTANCE.portalsConfig.portals[args[1]]!!
                destroy(args[1])
                messageFormatter.addFormatterPatterns("name" to args[1], "coords" to "$x, $y, $z")
                sendMessage(sender, this.cmdMsg("destroyed"))
            }

            "list" -> {
                assertCommandPermission(sender, "list")
                if (portalList.isEmpty()) {
                    sendWarningMessage(sender, rawCmdMsg("no-portals"))
                    return
                }
                sendMessage(sender, this.cmdMsg("list"))
                val messageBuilder = StringBuilder()
                var isFirst = true
                for (portalName in portalList) {
                    val (_, x, y, z, world) = ActiveCraftCore.INSTANCE.portalsConfig.portals[portalName]!!
                    if (isFirst) {
                        messageBuilder.append(ChatColor.BOLD.toString() + "" + ChatColor.GOLD + portalName + ": " + ChatColor.GRAY + world.name + "; " + x + ", " + y + ", " + z)
                        isFirst = false
                    } else messageBuilder.append(
                        """
    
    ${ChatColor.BOLD}${ChatColor.GOLD}$portalName: ${ChatColor.GRAY}${world.name}; $x, $y, $z
    """.trimIndent()
                    )
                }
                sendMessage(sender, messageBuilder.toString())
            }
            else -> throw InvalidArgumentException()
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        val list = ArrayList<String>()
        val p = sender as Player
        if (args.size == 1) {
            list.add("create")
            list.add("destroy")
            list.add("list")
        }
        if (args[0] == "create") {
            if (p.getTargetBlock(120) == null) return null
            when (args.size) {
                3, 6 -> list.add(p.getTargetBlock(120)!!.x.toString() + "")
                4, 7 -> list.add(p.getTargetBlock(120)!!.y.toString() + "")
                5, 8 -> list.add(p.getTargetBlock(120)!!.z.toString() + "")
                9 -> list.add(p.world.name)
            }
        } else if (args[0] == "destroy") if (args.size == 2) list.addAll(
            ActiveCraftCore.INSTANCE.portalsConfig.portals.keys
        )
        return list
    }
}