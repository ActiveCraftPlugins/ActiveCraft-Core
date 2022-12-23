package org.activecraft.activecraftcore.commands

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.BanManager
import org.activecraft.activecraftcore.manager.BanManager.IP.isBanned
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.TimeUtils.addFromStringToDate
import org.activecraft.activecraftcore.utils.isValidInet4Address
import org.activecraft.activecraftcore.utils.strip
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class BanCommandCollection(plugin: ActiveCraftPlugin) : ActiveCraftCommandCollection(
    BanCommand(plugin),
    BanIpCommand(plugin),
    UnbanCommand(plugin),
    UnbanIpCommand(plugin),
    BanlistCommand(plugin)
) {
    class BanCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("ban", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val target = getOfflinePlayer(args[0]).name!!
            messageFormatter.setTarget(target)
            if (BanManager.Name.isBanned(target)) {
                sendWarningMessage(sender, rawCmdMsg("already-banned"))
                return
            }
            BanManager.Name.ban(
                target,
                if (args.size >= 3) joinArray(args, 2) else getMessageSupplier().reasons.moderatorBanned,
                addFromStringToDate(if (args.size >= 2) args[1] else null),
                sender.name
            )
            sendMessage(sender, this.cmdMsg("completed-message"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null

    }

    class UnbanCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("unban", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            if (!BanManager.Name.isBanned(args[0])) {
                sendMessage(sender, this.cmdMsg("not-banned"))
                return
            }
            messageFormatter.setTarget(args[0])
            BanManager.Name.unban(args[0])
            sendMessage(sender, this.cmdMsg("unbanned-player"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1 && BanManager.Name.bans.isEmpty()) BanManager.Name.bans.map { it.target } else null

    }

    class BanIpCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("ban-ip", plugin, "banip", "banip") {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            if (Bukkit.getPlayer(args[0]) != null) {
                val target = getPlayer(args[0])
                val address: String = strip(target.address.address.toString(), "/")
                if (isBanned(address)) {
                    sendMessage(sender, this.cmdMsg("already-banned"))
                    return
                }
                messageFormatter.addFormatterPattern("ip", address)
                messageFormatter.setTarget(getProfile(target))
                BanManager.IP.ban(
                    address,
                    if (args.size >= 3) joinArray(args, 2) else getMessageSupplier().reasons.moderatorBanned,
                    addFromStringToDate(if (args.size >= 2) args[1] else null),
                    sender.name
                )
                sendMessage(sender, this.cmdMsg("completed-message"))
            } else if (isValidInet4Address(args[0])) {
                if (isBanned(args[0])) {
                    sendMessage(sender, this.cmdMsg("already-banned"))
                    return
                }
                messageFormatter.addFormatterPattern("ip", args[0])
                BanManager.IP.ban(
                    args[0],
                    if (args.size >= 3) joinArray(args, 2) else getMessageSupplier().reasons.moderatorBanned,
                    addFromStringToDate(if (args.size >= 2) args[1] else null),
                    sender.name
                )
                sendMessage(sender, this.cmdMsg("completed-message-ip"))
            } else sendWarningMessage(sender, rawCmdMsg("invalid-ip"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null
    }

    class UnbanIpCommand(plugin: ActiveCraftPlugin) :
        ActiveCraftCommand("unban-ip", plugin, "unbanip", "unbanip") {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            if (!isBanned(args[0])) {
                sendWarningMessage(sender, rawCmdMsg("not-banned"))
                return
            }
            messageFormatter.addFormatterPattern("ip", args[0])
            BanManager.IP.unban(args[0])
            sendMessage(sender, this.cmdMsg("unbanned-ip"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1 && BanManager.Name.bans.isEmpty()) BanManager.IP.bans.map { it.target } else null
    }

    class BanlistCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("banlist", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            if (BanManager.Name.bans.isEmpty() && BanManager.IP.bans.isEmpty()) {
                sendMessage(sender, this.cmdMsg("no-bans"))
                return
            }
            val componentBuilder = ComponentBuilder()
            val tempBanListName = BanManager.Name.bans.map { it.target }.sorted()
            val tempBanListIP = BanManager.IP.bans.map { it.target }.sorted()
            for (i in tempBanListName.indices) {
                val name = tempBanListName[i]
                val textComponent = TextComponent()
                textComponent.hoverEvent = HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(cmdMsg("unban-on-hover", newPlayerMessageFormatter().setTarget(name)))
                )
                textComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban $name")
                if (i != 0) textComponent.text = ", $name" else textComponent.text = name
                componentBuilder.append(textComponent)
            }
            for (i in tempBanListIP.indices) {
                val ip = tempBanListIP[i]
                val textComponent = TextComponent()
                textComponent.hoverEvent = HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(
                        cmdMsg(
                            "unban-ip-on-hover",
                            newMessageFormatter().addFormatterPattern("ip", ip)
                        )
                    )
                )
                textComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban-ip $ip")
                if (i != 0) textComponent.text = ", $ip" else textComponent.text = ip
                componentBuilder.append(textComponent)
            }
            sendMessage(sender, this.cmdMsg("header"))
            sendMessage(sender, *componentBuilder.create())
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = null
    }
}