package de.silencio.activecraftcore.guis.effectgui.item;

import de.silencio.activecraftcore.guicreator.ClickListener;
import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.effectgui.EffectGui;
import de.silencio.activecraftcore.guis.effectgui.inventory.LevelChangerGui;
import de.silencio.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import de.silencio.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import de.silencio.activecraftcore.messages.EffectGuiMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.Effect;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EffectItem extends GuiItem {

    private Profile profile;
    private PotionEffectType effectType;

    ClickListener clickListener = guiClickEvent -> {
        GuiCreator guiCreator = guiClickEvent.getGui().getAssociatedGuiCreator();
        EffectGui effectGui = switch (guiClickEvent.getGui().getAssociatedGuiCreator().getIdentifier()) {
            case "potion_effect_gui" -> ((PotionEffectGui) guiCreator).getEffectGui();
            case "status_effect_gui" -> ((StatusEffectGui) guiCreator).getEffectGui();
            default -> null;
        };
        if (guiClickEvent.getClick() == ClickType.LEFT) {
            profile.toggleEffect(effectType);
            GuiNavigator.pushReplacement(profile.getPlayer(), guiClickEvent.getGui().rebuild());
        } else if (guiClickEvent.getClick() == ClickType.RIGHT) {
            GuiNavigator.push(effectGui.getPlayer(), new LevelChangerGui(effectGui, effectType).build());
        }
    };

    public EffectItem(Material material, PotionEffectType effectType, Profile profile) {
        super(material);
        this.profile = profile;
        this.effectType = effectType;
        setDisplayName(EffectGuiMessages.EFFECT(effectType));
        refresh();
        addClickListener(clickListener);
    }

    public EffectItem(Material material, PotionEffectType effectType, Profile profile, boolean vanillaPotion) {
        super(material);
        this.profile = profile;
        this.effectType = effectType;
        setDisplayName(EffectGuiMessages.EFFECT(effectType));
        PotionMeta potionMeta = (PotionMeta) getItemMeta();
        potionMeta.setColor(effectType.getColor());
        setItemMeta(potionMeta);
        setGlint(true);
        addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        refresh();
        addClickListener(clickListener);
    }

    public void refresh() {
        Effect effect = profile.getEffects().get(effectType);
        setLore(
                effect.active() ? EffectGuiMessages.ITEM_ACTIVE_FORMAT() : EffectGuiMessages.ITEM_INACTIVE_FORMAT(),
                EffectGuiMessages.ITEM_LEVEL_FORMAT(effect.amplifier() + 1),
                EffectGuiMessages.ITEM_TOOLTIP()
        );
    }
}