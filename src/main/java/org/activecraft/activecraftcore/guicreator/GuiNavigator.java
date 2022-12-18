package org.activecraft.activecraftcore.guicreator;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Stack;

public final class GuiNavigator {

    @Getter
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
        playerGuiStack.computeIfAbsent(player, k -> new Stack<>());
        return playerGuiStack.get(player);
    }

    public static Gui getActiveGui(Player player) {
        return getGuiStack(player).isEmpty() ? null : getGuiStack(player).peek();
    }

    public static void refreshActiveGui(Player player) {
        if (getActiveGui(player) == null) return;
        pushReplacement(player, getActiveGui(player).rebuild());
    }
}
