package de.silencio.activecraftcore.guis.profilemenu.listeners;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.guicreator.Gui;
import de.silencio.activecraftcore.guicreator.GuiClickEvent;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.ProfileMenu;
import de.silencio.activecraftcore.guis.profilemenu.inventories.ReasonsProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ViolationsProfileListener implements Listener {

    @EventHandler
    public void onClick(GuiClickEvent event) {
        if (!ActiveCraftCore.getProfileMenuList().containsKey((Player) event.getView().getPlayer())) return;
        Player player = (Player) event.getView().getPlayer();
        ProfileMenu profileMenu = ActiveCraftCore.getProfileMenuList().get(player);
        Gui gui = event.getGui();

        if (!event.getGui().getAssociatedGuiCreator().getInternalName().equals("violations_profile")) return;

        if (event.getCurrentItem() == profileMenu.getViolationsProfile().getWarnStack()) {
            profileMenu.getReasonsProfile().renew(false);
            player.openInventory(profileMenu.getReasonsProfile().build().getInventory());
            profileMenu.getReasonsProfile().setActiveConfirmation(ReasonsProfile.Confirmable.WARN);
            GuiNavigator.push(player, event.getClickedInventory());
        } else if (event.getCurrentItem() == profileMenu.getViolationsProfile().getBanStack()) {
            profileMenu.getReasonsProfile().renew(true);
            player.openInventory(profileMenu.getReasonsProfile().build().getInventory());
            profileMenu.getReasonsProfile().setActiveConfirmation(ReasonsProfile.Confirmable.BAN);
            GuiNavigator.push(player, event.getClickedInventory());
        } else if (event.getCurrentItem() == profileMenu.getViolationsProfile().getIpBanStack()) {
            profileMenu.getReasonsProfile().renew(true);
            player.openInventory(profileMenu.getReasonsProfile().build().getInventory());
            profileMenu.getReasonsProfile().setActiveConfirmation(ReasonsProfile.Confirmable.BAN_IP);
            GuiNavigator.push(player, event.getClickedInventory());
        } else if (event.getCurrentItem() == profileMenu.getViolationsProfile().getKickStack()) {
            profileMenu.getReasonsProfile().renew(false);
            player.openInventory(profileMenu.getReasonsProfile().build().getInventory());
            profileMenu.getReasonsProfile().setActiveConfirmation(ReasonsProfile.Confirmable.KICK);
            GuiNavigator.push(player, event.getClickedInventory());
        }  else if (event.getCurrentItem() == profileMenu.getViolationsProfile().getMuteStack()) {
            if (profileMenu.getProfile().isMuted()) {
                player.performCommand("unmute " + profileMenu.getTarget().getName());
            } else {
                player.performCommand("mute " + profileMenu.getTarget().getName());
            }
            profileMenu.getViolationsProfile().refresh();
            player.openInventory(profileMenu.getViolationsProfile().build().getInventory());
        }
    }

}
