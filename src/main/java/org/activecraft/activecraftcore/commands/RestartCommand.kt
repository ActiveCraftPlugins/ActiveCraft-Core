package org.activecraft.activecraftcore.commands

import com.destroystokyo.paper.Title
import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RestartCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("restart-server", plugin!!) {
    private var runnable: BukkitRunnable? = null
    private var time = 0

    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        if (args.isNotEmpty() && args[0].equals("cancel", ignoreCase = true)) {
            cancelTimer(sender)
            return
        }
        time = if (args.isEmpty()) 30 else parseInt(args[0])
        if (runnable != null && !runnable!!.isCancelled) runnable!!.cancel()
        runnable = object : BukkitRunnable() {
            override fun run() {
                if (time == 0) {
                    cancel()
                    Bukkit.getOnlinePlayers().forEach { player: Player -> player.kickPlayer(cmdMsg("message")) }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spigot:restart")
                    return
                }
                for (target in Bukkit.getOnlinePlayers()) {
                    target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 0.5f)
                    val title: Title =
                        Title(cmdMsg("title", newMessageFormatter().addFormatterPattern("time", time.toString() + "")))
                    target.sendTitle(title)
                    try {
                        Thread.sleep(250)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 0.5f)
                }
                time--
            }
        }
        runnable?.runTaskTimer(ActiveCraftCore.INSTANCE, 0, 20)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) listOf("cancel") else null


    private fun cancelTimer(sender: CommandSender) {
        if (runnable == null || runnable!!.isCancelled) return
        val title = Title(cmdMsg("title", newMessageFormatter().addFormatterPattern("time", "--")))
        Bukkit.getOnlinePlayers().forEach { it.sendTitle(title) }
        runnable!!.cancel()
        sendMessage(sender, cmdMsg("cancel"))
    }
}