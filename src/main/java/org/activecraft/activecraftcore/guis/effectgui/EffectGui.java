package org.activecraft.activecraftcore.guis.effectgui;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.guicreator.GuiBackItem;
import org.activecraft.activecraftcore.guicreator.GuiCloseItem;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead;
import org.activecraft.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import org.activecraft.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.MessageSupplierKt;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.NonNull;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.guicreator.GuiBackItem;
import org.activecraft.activecraftcore.guicreator.GuiCloseItem;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import org.activecraft.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
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
