package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.events.ActiveCraftEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Data
@EqualsAndHashCode(callSuper = false)
public class GuiConfirmEvent extends ActiveCraftEvent {

    private boolean cancelled;
    private Gui gui;
    private @NonNull Inventory clickedInventory;
    private @NonNull String identifier;
    private @NonNull Player player;

    public Gui getGui() {
        return gui != null ? gui : (gui = Gui.ofInventory(clickedInventory));
    }
}
