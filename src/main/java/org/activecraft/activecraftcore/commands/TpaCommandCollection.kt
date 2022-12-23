package org.activecraft.activecraftcore.commands

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TpaCommandCollection(plugin: ActiveCraftPlugin?) : ActiveCraftCommandCollection(
    TpaCommand(plugin),
    TpacceptCommand(plugin),
    TpadenyCommand(plugin)
) {
    class TpaCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("tpa", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val player = getPlayer(sender)
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val target = getPlayer(args[0])
            if (target === player) {
                sendWarningMessage(sender, rawCmdMsg("cannot-tpa-self"))
                return
            }
            val accept = TextComponent(this.cmdMsg("accept") + " ")
            accept.hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ComponentBuilder(this.cmdMsg("accept-hover", ChatColor.GREEN)).create()
            )
            accept.clickEvent =
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept")
            val deny = TextComponent(this.cmdMsg("deny"))
            deny.hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ComponentBuilder(this.cmdMsg("deny-hover", ChatColor.RED)).create()
            )
            deny.clickEvent =
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny")
            messageFormatter.setTarget(getProfile(target))
            sendMessage(sender, this.cmdMsg("request-to"))
            sendMessage(target, " ")
            sendMessage(target, this.cmdMsg("request-from"))
            sendMessage(target, accept, deny)
            sendMessage(target, " ")
            tpaList[target] = player
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null

    }

    class TpacceptCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("tpaccept", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val player = getPlayer(sender)
            assertCommandPermission(sender)
            if (!tpaList.containsKey(player)) {
                sendWarningMessage(sender, rawCmdMsg("no-requests"))
                return
            }
            val target = tpaList[player]
            messageFormatter.setTarget(getProfile(target!!))
            val loc = player.location
            sendMessage(sender, this.cmdMsg("accepted"))
            if (!ActiveCraftCore.INSTANCE.mainConfig.timerTpa) {
                tpaList[player]!!.sendActionBar(this.cmdMsg("actionbar"))
                tpaList[player]!!.teleport(loc)
                tpaList.remove(player)
                return
            }
            val runnable: BukkitRunnable = object : BukkitRunnable() {
                var time = 3
                override fun run() {
                    if (time == 0) {
                        target.sendActionBar(this@TpacceptCommand.cmdMsg("actionbar"))
                        target.teleport(loc)
                        sendMessage(target, this@TpacceptCommand.cmdMsg("receiver-message"))
                        target.playSound(target.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
                        sendMessage(sender, this@TpacceptCommand.cmdMsg("sender-message"))
                        cancel()
                        tpaTimerList[tpaList[player]] = null
                        tpaList.remove(sender as Player)
                        return
                    }
                    target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f)
                    target.sendActionBar(ChatColor.GOLD.toString() + "" + time)
                    time--
                }
            }
            if (tpaTimerList[target] != null) tpaTimerList[target]!!
                .cancel()
            tpaTimerList[tpaList[player]] = runnable
            runnable.runTaskTimer(ActiveCraftCore.INSTANCE, 0, 20)
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = null
    }

    class TpadenyCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("tpadeny", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val player = getPlayer(sender)
            assertCommandPermission(sender)
            if (!tpaList.containsKey(player)) {
                sendWarningMessage(sender, rawCmdMsg("no-requests"))
                return
            }
            val target = tpaList[player]
            messageFormatter.setTarget(getProfile(target!!))
            sendMessage(sender, this.cmdMsg("denied"))
            sendMessage(sender, this.cmdMsg("receiver-message"))
            sendMessage(target, this.cmdMsg("sender-message"))
            tpaList.remove(sender as Player)
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = null
    }

    companion object {
        // TODO: 11.06.2022 testen mit 2 person
        private val tpaList: MutableMap<Player, Player> = mutableMapOf()
        private val tpaTimerList: MutableMap<Player?, BukkitRunnable?> = mutableMapOf()
    }
}