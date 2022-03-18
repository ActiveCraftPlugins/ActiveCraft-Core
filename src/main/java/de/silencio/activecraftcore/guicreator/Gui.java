package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.ActiveCraftCore;
import lombok.Data;
import org.bukkit.inventory.Inventory;

import java.util.Random;

@Data
public class Gui {

    private String name;
    private int id;
    private final GuiCreator associatedGuiCreator;
    private Inventory inventory;

    public Gui(Inventory inventory, GuiCreator guiCreator) {
        this.inventory = inventory;
        this.associatedGuiCreator = guiCreator;
        int randInt = newRandom(99999);
        while (true) {
            if (ActiveCraftCore.getGuiList().containsKey(randInt)) {
                randInt = newRandom(99999);
            } else {
                ActiveCraftCore.getGuiList().put(randInt, this);
                break;
            }
        }
        this.id = randInt;
    }

    private int newRandom(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    public void setId(int id) {
        ActiveCraftCore.getGuiList().remove(this.id);
        this.id = id;
        ActiveCraftCore.getGuiList().put(id, this);
    }

    public static Gui ofInventory(Inventory inventory) {
        return ActiveCraftCore.getGuiList().values().stream()
                .filter(gui -> gui.getInventory() == inventory)
                .findAny().orElse(null);
    }

    public Gui rebuild() {
        return getAssociatedGuiCreator().build();
    }
}