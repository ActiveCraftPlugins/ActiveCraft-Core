package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import lombok.Data;
import org.bukkit.inventory.Inventory;

import java.util.Random;

@Data
public class Gui {

    private static GuiManager guiManager = ActiveCraftCore.getInstance().getGuiManager();
    private String name;
    private int id;
    private final GuiCreator guiCreator;
    private Inventory inventory;

    public Gui(Inventory inventory, GuiCreator guiCreator) {
        this.inventory = inventory;
        this.guiCreator = guiCreator;
        Random random = new Random();
        int randInt = random.nextInt(Integer.MAX_VALUE);
        while (true) {
            if (guiManager.isRegistered(randInt)) {
                randInt = random.nextInt(Integer.MAX_VALUE);
            } else {
                guiManager.register(this);
                break;
            }
        }
        this.id = randInt;
    }

    public void setId(int id) {
        guiManager.unregister(this.id);
        this.id = id;
        guiManager.register(this);
    }

    public static Gui ofInventory(Inventory inventory) {
        return guiManager.getGuiOfInventory(inventory);
    }

    public Gui rebuild() {
        return getGuiCreator().build();
    }
}