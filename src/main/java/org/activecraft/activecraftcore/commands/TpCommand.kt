package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidNumberException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("tp", plugin!!) {// TODO: 11.06.2022 bitte nochmal durchtesten
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        // filthy fix to make /locate tp work
        var args = args
        if (args[0].equals("@s", ignoreCase = true)) {
            args = args.copyOfRange(1, args.lastIndex)
        }
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val type = when (args.size) {
            1, 3 -> CommandTargetType.SELF
            else -> CommandTargetType.OTHERS
        }
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        val profile = getProfile(target)
        assertCommandPermission(sender, type.code())
        when (args.size) {
            1, 2 -> {
                val destPlayer = getPlayer(args[args.size - 1])
                val destProfile = getProfile(destPlayer)
                if (args.size == 2) isTargetSelf(sender, target, true)
                target.teleport(destPlayer.location)
                target.playSound(target.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
                messageFormatter
                    .setTarget(profile)
                    .addFormatterPatterns(
                        "t1_playername" to profile.name, "t1_displayname" to profile.nickname,
                        "t2_playername" to destProfile.name, "t2_displayname" to destProfile.nickname
                    )
                sendMessage(sender, this.cmdMsg((if (args.size == 2) "player-" else "") + "to-player"))
            }

            else -> {
                if (args.size >= 4) isTargetSelf(sender, target, true)
                val x = getCoordX(target, args[if (type == CommandTargetType.SELF) 0 else 1])
                val y = getCoordY(target, args[if (type == CommandTargetType.SELF) 1 else 2])
                val z = getCoordZ(target, args[if (type == CommandTargetType.SELF) 2 else 3])
                target.teleport(Location(target.world, x, y, z))
                messageFormatter.setTarget(profile)
                messageFormatter.addFormatterPattern("coords", "$x, $y, $z")
                sendMessage(sender, this.cmdMsg((if (args.size == 4) "player-" else "") + "to-coords"))
                target.playSound(target.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
            }
        }
    }

    @Throws(InvalidNumberException::class)
    private fun getCoordX(target: Player, input: String): Double {
        return if (input == "~") target.location.x else parseDouble(input)
    }

    @Throws(InvalidNumberException::class)
    private fun getCoordY(target: Player, input: String): Double {
        return if (input == "~") target.location.y else parseDouble(input)
    }

    @Throws(InvalidNumberException::class)
    private fun getCoordZ(target: Player, input: String): Double {
        return if (input == "~") target.location.z else parseDouble(input)
    }

    private fun addTargetBlockX(list: MutableList<String>, player: Player) {
        list.add(if (isValidTargetBlock(player)) player.getTargetBlock(10)!!.location.blockX.toString() + "" else "~")
    }

    private fun addTargetBlockY(list: MutableList<String>, player: Player) {
        list.add(if (isValidTargetBlock(player)) player.getTargetBlock(10)!!.location.blockY.toString() + "" else "~")
    }

    private fun addTargetBlockZ(list: MutableList<String>, player: Player) {
        list.add(if (isValidTargetBlock(player)) player.getTargetBlock(10)!!.location.blockZ.toString() + "" else "~")
    }

    private fun isValidTargetBlock(player: Player): Boolean {
        val targetBlock = player.getTargetBlock(10)
        return targetBlock != null && targetBlock.blockData.material != Material.AIR
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        var args = args
        val list: MutableList<String> = mutableListOf()
        val player = sender as Player
        if (args[0].equals("@s", ignoreCase = true)) {
            args = args.copyOfRange(1, args.lastIndex)
        }
        if (args.size == 1) {
            addTargetBlockX(list, player)
            list.addAll(getBukkitPlayernames())
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            if (args.size == 2) {
                addTargetBlockY(list, player)
            } else if (args.size == 3) {
                addTargetBlockZ(list, player)
            }
        } else if (sender.hasPermission("tp.others")) {
            when (args.size) {
                2 -> {
                    addTargetBlockX(list, player)
                    list.addAll(getBukkitPlayernames())
                }
                3 -> addTargetBlockY(list, player)
                4 -> addTargetBlockZ(list, player)
            }
        }
        return list
    }
}