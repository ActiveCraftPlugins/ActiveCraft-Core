package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class DrainCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("drain", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        assertArgsLength(args, ComparisonType.NOT_EQUAL, 0)
        val drainedBlocks = when (args.size) {
            1 -> drain(
                startBlock = player.location.block,
                range = parseInt(args[0]),
                removeWaterlogged = false,
                applyPhysics = true
            )

            2 -> drain(
                startBlock = player.location.block,
                range = parseInt(args[0]),
                removeWaterlogged = java.lang.Boolean.parseBoolean(args[1]),
                applyPhysics = true
            )

            else -> drain(
                startBlock = player.location.block,
                range = parseInt(args[0]),
                removeWaterlogged = java.lang.Boolean.parseBoolean(args[1]),
                applyPhysics = java.lang.Boolean.parseBoolean(args[2])
            )
        }
        messageFormatter.addFormatterPattern("amount", drainedBlocks.toString() + "")
        sendMessage(player, this.cmdMsg("drain"))
    }

    private fun drain(startBlock: Block, range: Int, removeWaterlogged: Boolean, applyPhysics: Boolean): Int {
        /*val types = listOf(
            Material.LAVA,
            Material.WATER,
            Material.SEAGRASS,
            Material.TALL_SEAGRASS,
            Material.KELP_PLANT,
            Material.KELP,
            Material.BUBBLE_COLUMN
        )*/
        val types = Material.values().filter { runCatching { it.data as Class<Waterlogged> }.isSuccess } // TODO: test
        val world = startBlock.world
        val blocks: MutableSet<Block> = HashSet()
        val toBeAdded: MutableSet<Block> = HashSet()
        var totalDrainedBlocks = 0
        if (!types.contains(startBlock.type)) return 0
        blocks.add(startBlock)
        for (i in 0 until range) {
            for (block in blocks) {
                val xplus1 = world.getBlockAt(block.x + 1, block.y, block.z)
                val xminus1 = world.getBlockAt(block.x - 1, block.y, block.z)
                val yplus1 = world.getBlockAt(block.x, block.y + 1, block.z)
                val yminus1 = world.getBlockAt(block.x, block.y - 1, block.z)
                val zplus1 = world.getBlockAt(block.x, block.y, block.z + 1)
                val zminus1 = world.getBlockAt(block.x, block.y, block.z - 1)
                val neighbourBlocks = arrayOf(xplus1, xminus1, yplus1, yminus1, zplus1, zminus1)
                neighbourBlocks.filter { neighbourBlock ->
                    types.contains(neighbourBlock.type) && !toBeAdded.contains(neighbourBlock)
                }.forEach { toBeAdded.add(it) }
                if (!removeWaterlogged) continue
                for (neighbourBlock in neighbourBlocks) if (neighbourBlock.blockData is Waterlogged) if ((neighbourBlock.blockData as Waterlogged).isWaterlogged) {
                    val wl = neighbourBlock.blockData as Waterlogged
                    wl.isWaterlogged = false
                    neighbourBlock.setBlockData(wl, applyPhysics)
                    totalDrainedBlocks++
                }
            }
            for (block in blocks) {
                block.setType(Material.AIR, applyPhysics)
                totalDrainedBlocks++
            }
            blocks.clear()
            blocks.addAll(toBeAdded)
            toBeAdded.clear()
        }
        return totalDrainedBlocks
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 2 || args.size == 3) listOf("true", "false") else null

}