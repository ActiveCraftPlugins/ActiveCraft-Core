package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
// FIXME: 21.08.2022 keine items angezeigt
public class GuiConfirmation extends GuiCreator {

    private Runnable runnable;

    public GuiConfirmation(String identifier) {
        this(identifier, GuiCreatorDefaults.confirmationTitle());
    }

    public GuiConfirmation(String identifier, String title) {
        this(identifier, title,
                GuiCreatorDefaults.confirmItemDisplayname(), GuiCreatorDefaults.cancelItemDisplayname());
    }

    public GuiConfirmation(String identifier, String confirmItemDisplayname, String cancelItemDisplayname) {
        this(identifier, GuiCreatorDefaults.confirmationTitle(), confirmItemDisplayname, cancelItemDisplayname);
    }

    public GuiConfirmation(String identifier, String title, String confirmItemDisplayname, String cancelItemDisplayname) {
        super(GuiCreatorDefaults.CONFIRMATION_PREFIX + identifier, 3, title);
        fillBackground(true);
        setItem(new GuiItem(Material.LIME_CONCRETE)
                .setDisplayName(confirmItemDisplayname)
                .addClickListener(guiClickEvent -> {
                    Player player = guiClickEvent.getPlayer();
                    if (runnable != null) runnable.run();
                    if (GuiNavigator.getGuiStack(player) != null && GuiNavigator.getGuiStack(player).size() >= 1)
                        GuiNavigator.pop(player);
                }), 11);
        setItem(new GuiItem(Material.RED_CONCRETE)
                .setDisplayName(cancelItemDisplayname)
                .addClickListener(guiClickEvent -> {
                    Player player = guiClickEvent.getPlayer();
                    if (GuiNavigator.getGuiStack(player) != null)
                        if (GuiNavigator.getGuiStack(player).size() >= 1)
                            GuiNavigator.pop(player);
                }), 15);
    }

    public GuiCreator performAfterConfirm(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    @Override
    public void refresh() {
    }
}

