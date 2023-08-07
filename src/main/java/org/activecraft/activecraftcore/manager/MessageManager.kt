package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.MsgEvent
import org.activecraft.activecraftcore.exceptions.InvalidPlayerException
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.OfflinePlayerActionScheduler.schedule
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object MessageManager {
    private val msgPlayerStoring = HashMap<CommandSender, Profile>()
    fun sendDM(receiver: Profile, sender: CommandSender, message: String) {
        val acCoreMessageSupplier: MessageSupplier = receiver.getMessageSupplier(ActiveCraftCore.INSTANCE)!!
        val msgFormatter: MessageFormatter =
            PlayerMessageFormatter(ActiveCraftCore.INSTANCE.activeCraftMessage!!)
                .setTarget(receiver).setSender(sender)
        val event = MsgEvent(sender, receiver, message)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        if (sender is Player) {
            schedule(receiver) {
                msgFormatter.addFormatterPattern("message", event.message)
                val target = receiver.player
                target!!.sendMessage(acCoreMessageSupplier.getFormatted("command.msg.format-from", msgFormatter))
                target.playSound(target.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f)
                msgPlayerStoring[target] = Profile.of(sender)
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
                if (ActiveCraftCore.INSTANCE.mainConfig.socialSpyToConsole) Bukkit.getConsoleSender()
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

    fun openConversation(sender: CommandSender, target: Profile) {
        msgPlayerStoring[sender] = target
    }

    @Throws(InvalidPlayerException::class)
    fun msgToActiveConversation(sender: CommandSender, message: String) {
        sendDM(msgPlayerStoring[sender] ?: throw InvalidPlayerException("No open conversation"), sender, message) // TODO: NoOpenConversationException
    }

    fun getMsgPartner(sender: CommandSender) = msgPlayerStoring[sender]
}