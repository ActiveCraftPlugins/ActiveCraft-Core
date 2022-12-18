package org.activecraft.activecraftcore.guis.profilemenu.listener;

import org.activecraft.activecraftcore.guicreator.*;
import org.activecraft.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import org.activecraft.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.MainProfile;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.guicreator.*;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.MainProfile;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Stack;

public class ProfileMenuListeners implements Listener {

    @EventHandler
    public void onGuiCreate(GuiCreateEvent event) {
        Player player;
        if (event.getGuiCreator() instanceof PotionEffectGui potionEffectGui) {
            player = potionEffectGui.getPlayer();
        } else if (event.getGuiCreator() instanceof StatusEffectGui statusEffectGui) {
            player = statusEffectGui.getPlayer();
        } else return;
        Stack<Gui> guiStack = GuiNavigator.getGuiStack(player);
        if (guiStack.isEmpty()) return;
        if (guiStack.peek() == null) return;
        if (!(guiStack.peek().getGuiCreator() instanceof MainProfile)) {
            if (guiStack.peek().getGuiCreator() instanceof PotionEffectGui || guiStack.peek().getGuiCreator() instanceof StatusEffectGui) {
                if (guiStack.size() < 3) return;
                if (!(guiStack.get(guiStack.size() - 2).getGuiCreator() instanceof MainProfile)) return;
            } else return;
        }
        event.getGuiCreator().setItem(
                new GuiBackItem(GuiCreatorDefaults.backItemDisplayname(Profilev2.of(player)
                        .getMessageSupplier(GuiCreatorDefaults.acCoreMessage))), 48);
    }
}
