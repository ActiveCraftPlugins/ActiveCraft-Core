package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.messages.GuiMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GuiConfirmation extends GuiCreator {

    private final String title;
    private GuiAction guiAction;

    public GuiConfirmation(String identifier) {
        this(identifier, GuiMessages.CONFIRMATION_TITLE());
    }

    public GuiConfirmation(String identifier, String title) {
        super("confirmation_" + identifier, 3, title);
        this.title = title;
        fillBackground(true);
        setItem(new GuiItem(Material.LIME_CONCRETE)
                .setDisplayName(GuiMessages.CONFIRM_ITEM())
                .addClickListener(guiClickEvent -> {
                    Player player = guiClickEvent.getPlayer();
                    if (guiAction != null) guiAction.perform();
                    if (GuiNavigator.getGuiStack(player) != null && GuiNavigator.getGuiStack(player).size() >= 1)
                        GuiNavigator.pop(player);
                }), 11);
        setItem(new GuiItem(Material.RED_CONCRETE)
                .setDisplayName(GuiMessages.CANCEL_ITEM())
                .addClickListener(guiClickEvent -> {
                    Player player = guiClickEvent.getPlayer();
                    if (GuiNavigator.getGuiStack(player) != null)
                        if (GuiNavigator.getGuiStack(player).size() >= 1)
                            GuiNavigator.pop(player);
                }), 15);
    }

    public GuiCreator performAfterConfirm(GuiAction guiAction) {
        this.guiAction = guiAction;
        return this;
    }

    @Override
    public void refresh() {
    }
}

