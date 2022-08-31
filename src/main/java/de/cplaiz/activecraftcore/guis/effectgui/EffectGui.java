package de.cplaiz.activecraftcore.guis.effectgui;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.guicreator.GuiBackItem;
import de.cplaiz.activecraftcore.guicreator.GuiCloseItem;
import de.cplaiz.activecraftcore.guicreator.GuiCreatorDefaults;
import de.cplaiz.activecraftcore.guicreator.GuiPlayerHead;
import de.cplaiz.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import de.cplaiz.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplierKt;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Data
public class EffectGui {

    private final Player player;
    private final Player target;
    private final Profilev2 profile;
    private final MessageSupplier messageSupplier;

    private final PotionEffectGui potionEffectGui;
    private final StatusEffectGui statusEffectGui;
    private GuiCloseItem defaultGuiCloseItem;
    private GuiBackItem defaultGuiBackItem;

    public EffectGui(@NotNull Player player, @NonNull Player target) {
        this.player = player;
        this.target = target;
        this.profile = Profilev2.of(target);
        this.messageSupplier = MessageSupplierKt.getMessageSupplier(player, ActiveCraftCore.getInstance().getActiveCraftMessagev2());
        defaultGuiCloseItem = new GuiCloseItem(GuiCreatorDefaults.closeItemDisplayname(messageSupplier));
        defaultGuiBackItem = new GuiBackItem(GuiCreatorDefaults.backItemDisplayname(messageSupplier));
        this.potionEffectGui = new PotionEffectGui(this);
        this.statusEffectGui = new StatusEffectGui(this);
    }
}
