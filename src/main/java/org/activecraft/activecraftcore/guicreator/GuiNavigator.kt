package org.activecraft.activecraftcore.guicreator

import org.bukkit.entity.Player
import java.util.*

class GuiNavigator {
    fun remove(player: Player) {
        playerGuiStack.remove(player)
    }

    fun clear(player: Player) {
        playerGuiStack[player] = Stack()
    }

    companion object {
        private val playerGuiStack = HashMap<Player, Stack<Gui>>()
        @JvmStatic
        fun push(player: Player, gui: Gui) {
            getGuiStack(player).push(gui)
            player.openInventory(gui.inventory)
        }

        @JvmStatic
        fun pushReplacement(player: Player, gui: Gui) {
            getGuiStack(player).pop()
            getGuiStack(player).push(gui)
            player.openInventory(gui.inventory)
        }

        fun pushAndRemoveUntil(player: Player, gui: Gui) {
            getGuiStack(player).clear()
            getGuiStack(player).push(gui)
            player.openInventory(gui.inventory)
        }

        @JvmStatic
        fun pop(player: Player): Gui {
            getGuiStack(player).pop()
            val topGui = getGuiStack(player).peek()
            player.openInventory(topGui.rebuild().inventory)
            return topGui
        }

        @JvmStatic
        fun getGuiStack(player: Player): Stack<Gui> {
            return playerGuiStack.getOrPut(player) { Stack() }
        }

        fun getActiveGui(player: Player): Gui? {
            return if (getGuiStack(player).isEmpty()) null else getGuiStack(player).peek()
        }

        fun refreshActiveGui(player: Player) {
            if (getActiveGui(player) == null) return
            pushReplacement(player, getActiveGui(player)!!.rebuild())
        }
    }
}