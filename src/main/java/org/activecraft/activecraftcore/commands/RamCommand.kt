package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import kotlin.math.pow

class RamCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("ram", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val runtime = Runtime.getRuntime()
        val divisor = 32.0.pow(4.0)
        val used = ((runtime.totalMemory() - runtime.freeMemory()) / divisor).toLong()
        val max = (runtime.totalMemory() / divisor).toLong()
        val free = (runtime.freeMemory() / divisor).toLong()
        messageFormatter.addFormatterPatterns(
            "freememory" to free.toString(),
            "usedmemory" to used.toString(),
            "maxmemory" to max.toString()
        )
        sendMessage(sender, this.cmdMsg("ram"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}