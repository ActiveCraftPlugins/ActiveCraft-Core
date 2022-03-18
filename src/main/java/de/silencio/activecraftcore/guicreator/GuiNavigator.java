package de.silencio.activecraftcore.guicreator;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Stack;

public class GuiNavigator {

    private static final HashMap<Player, Stack<Gui>> playerGuiStack = new HashMap<>();

    public static void push(Player player, Gui gui) {
        if (getGuiStack(player) == null)
            playerGuiStack.put(player, new Stack<>());
        getGuiStack(player).push(gui);
        player.openInventory(gui.getInventory());
    }

    public static void pushReplacement(Player player, Gui gui) {
        if (getGuiStack(player) == null)
            playerGuiStack.put(player, new Stack<>());
        getGuiStack(player).pop();
        getGuiStack(player).push(gui);
        player.openInventory(gui.getInventory());
    }

    public static void pushAndRemoveUntil(Player player, Gui gui) {
        if (getGuiStack(player) == null)
            playerGuiStack.put(player, new Stack<>());
        getGuiStack(player).clear();
        getGuiStack(player).push(gui);
        player.openInventory(gui.getInventory());
    }

    public static Gui pop(Player player) {
        getGuiStack(player).pop();
        Gui topGui = getGuiStack(player).peek();
        player.openInventory(topGui.rebuild().getInventory());
        return topGui;
    }

    public void remove(Player player) {
        playerGuiStack.remove(player);
    }

    public void clear(Player player) {
        GuiNavigator.playerGuiStack.put(player, new Stack<>());
    }

    public static Stack<Gui> getGuiStack(Player player) {
        return playerGuiStack.get(player);
    }
}
