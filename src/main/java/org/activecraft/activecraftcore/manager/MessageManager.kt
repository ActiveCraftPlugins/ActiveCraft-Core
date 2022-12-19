package org.activecraft.activecraftcore.manager

import lombok.Getter
import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.MsgEvent
import org.activecraft.activecraftcore.exceptions.InvalidPlayerException
import org.activecraft.activecraftcore.messagesv2.MessageFormatter
import org.activecraft.activecraftcore.messagesv2.MessageSupplier
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.OfflinePlayerActionScheduler.schedule
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object MessageManager {
    private val msgPlayerStoring = HashMap<CommandSender, Profilev2>()
    fun sendDM(receiver: Profilev2, sender: CommandSender, message: String) {
        val acCoreMessageSupplier: MessageSupplier = receiver.getMessageSupplier(ActiveCraftCore)!!
        val msgFormatter: MessageFormatter =
            PlayerMessageFormatter(ActiveCraftCore.activeCraftMessagev2!!)
                .setTarget(receiver).setSender(sender)
        val event = MsgEvent(sender, receiver, message)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        if (sender is Player) {
            schedule(receiver) {
                msgFormatter.addFormatterPattern("message", event.message)
                val target = receiver.player
                target!!.sendMessage(acCoreMessageSupplier.getFormatted("command.msg.format-from", msgFormatter))
                target.playSound(target.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f)
                msgPlayerStoring[target] = Profilev2.of(sender)
                Bukkit.getOnlinePlayers()
                    .filter { player: Player -> player.hasPermission("activecraft.msg.spy") && receiver.receiveSocialspy && player !== sender && player !== target }
                    .forEach { player: Player ->
                        player.sendMessage(
                            acCoreMessageSupplier.getFormatted(
                                "command.msg.socialspy-format",
                                msgFormatter
                            )
                        )
                    }
                if (ActiveCraftCore.mainConfig.socialSpyToConsole) Bukkit.getConsoleSender()
                    .sendMessage(acCoreMessageSupplier.getFormatted("command.msg.socialspy-format", msgFormatter))
            }
        } else {
            schedule(receiver) {
                val target = receiver.player
                msgFormatter.addFormatterPattern("message", event.message)
                target!!.sendMessage(
                    acCoreMessageSupplier.getFormatted(
                        "command.msg.from-console-format",
                        msgFormatter
                    )
                )
                target.playSound(target.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f)
            }
        }
    }

    @JvmStatic
    fun openConversation(sender: CommandSender, target: Profilev2) {
        msgPlayerStoring[sender] = target
    }

    @JvmStatic
    @Throws(InvalidPlayerException::class)
    fun msgToActiveConversation(sender: CommandSender, message: String) {
        sendDM(msgPlayerStoring[sender] ?: throw InvalidPlayerException("No open conversation"), sender, message) // TODO: NoOpenConversationException
    }
}