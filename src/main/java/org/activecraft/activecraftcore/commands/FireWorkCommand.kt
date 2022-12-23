package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class FireWorkCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("firework", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        val runnable: BukkitRunnable = object : BukkitRunnable() {
            var counter = if (args.size == 2) parseInt(args[0]) else 1
            val amount = if (args.size == 1) parseInt(args[0]) else 1
            override fun run() {
                for (i in amount downTo 1) {
                    val idx = Random().nextInt(FireworkEffect.Type.values().size)
                    val randomtype = FireworkEffect.Type.values()[idx]
                    val colors = arrayOf(
                        Color.GREEN,
                        Color.AQUA,
                        Color.BLUE,
                        Color.GRAY,
                        Color.ORANGE,
                        Color.RED,
                        Color.WHITE,
                        Color.BLACK,
                        Color.FUCHSIA,
                        Color.LIME,
                        Color.MAROON,
                        Color.NAVY,
                        Color.OLIVE,
                        Color.PURPLE,
                        Color.SILVER,
                        Color.TEAL,
                        Color.YELLOW
                    )
                    val randomColor = colors[Random().nextInt(colors.size)]
                    val randomColor2 = colors[Random().nextInt(colors.size)]
                    val r = Random()
                    val effect =
                        FireworkEffect.builder().flicker(r.nextBoolean()).with(randomtype).withColor(randomColor)
                            .withFade(randomColor2).trail(r.nextBoolean()).build()
                    val fw = player.world.spawnEntity(player.location, EntityType.FIREWORK) as Firework
                    val fwm = fw.fireworkMeta
                    fwm.addEffect(effect)
                    fwm.power = r.nextInt(2) + 1
                    fw.fireworkMeta = fwm
                }
                counter--
                if (counter == 0) cancel()
            }
        }
        runnable.runTaskTimer(
            ActiveCraftCore.INSTANCE,
            0,
            (if (args.size == 2) parseInt(args[1]) else 20).toLong()
        )
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}