package de.silencio.activecraftcore.guis.profilemenu.Listener;

import de.silencio.activecraftcore.guicreator.GuiCreateEvent;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import de.silencio.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import de.silencio.activecraftcore.guis.profilemenu.inventory.MainProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MainProfileListener implements Listener {

    @EventHandler
    public void onGuiCreate(GuiCreateEvent event) {
        if (event.getGuiCreator() instanceof PotionEffectGui potionEffectGui) {
            if (GuiNavigator.getGuiStack(potionEffectGui.getPlayer()).get(0).getAssociatedGuiCreator() instanceof MainProfile)
                event.getGuiCreator().setBackItem(48);
        } else if (event.getGuiCreator() instanceof StatusEffectGui statusEffectGui) {
            if (GuiNavigator.getGuiStack(statusEffectGui.getPlayer()).get(0).getAssociatedGuiCreator() instanceof MainProfile)
                event.getGuiCreator().setBackItem(48);
        }
    }
}
