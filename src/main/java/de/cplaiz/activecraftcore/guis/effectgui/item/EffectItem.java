package de.cplaiz.activecraftcore.guis.effectgui.item;

import de.cplaiz.activecraftcore.guicreator.ClickListener;
import de.cplaiz.activecraftcore.guicreator.GuiCreator;
import de.cplaiz.activecraftcore.guicreator.GuiItem;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.effectgui.EffectGui;
import de.cplaiz.activecraftcore.guis.effectgui.inventory.LevelChangerGui;
import de.cplaiz.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import de.cplaiz.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import de.cplaiz.activecraftcore.messagesv2.MessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.config.Effect;
import kotlin.Pair;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EffectItem extends GuiItem {

    private final EffectGui effectGui;
    private @Getter(AccessLevel.NONE) MessageSupplier messageSupplier;
    private PotionEffectType effectType;

    ClickListener clickListener = guiClickEvent -> {
        GuiCreator guiCreator = guiClickEvent.getGui().getGuiCreator();
        EffectGui effectGui = switch (guiClickEvent.getGui().getGuiCreator().getIdentifier()) {
            case "potion_effect_gui" -> ((PotionEffectGui) guiCreator).getEffectGui();
            case "status_effect_gui" -> ((StatusEffectGui) guiCreator).getEffectGui();
            default -> null;
        };
        if (guiClickEvent.getClick() == ClickType.LEFT) {
            Profilev2 profile = effectGui.getProfile();
            profile.getEffectManager().toggleEffect(effectType);
            GuiNavigator.pushReplacement(effectGui.getPlayer(), guiClickEvent.getGui().rebuild());
        } else if (guiClickEvent.getClick() == ClickType.RIGHT) {
            GuiNavigator.push(effectGui.getPlayer(), new LevelChangerGui(effectGui, effectType).build());
        }
    };

    public EffectItem(Material material, PotionEffectType effectType, EffectGui effectGui) {
        this(material, effectType, effectGui, false);
    }

    public EffectItem(Material material, PotionEffectType effectType, EffectGui effectGui, boolean vanillaPotion) {
        super(material);
        this.effectGui = effectGui;
        this.effectType = effectType;
        this.messageSupplier = effectGui.getMessageSupplier();
        setDisplayName(ColorUtils.getRgbColorCode(effectType.getColor())
                + messageSupplier.getRawMessage("effects." + effectType.getName().toLowerCase().replace("_", "-")));
        if (vanillaPotion) {
            PotionMeta potionMeta = (PotionMeta) getItemMeta();
            potionMeta.setColor(effectType.getColor());
            setItemMeta(potionMeta);
            setGlint(true);
            addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        refresh();
        addClickListener(clickListener);
    }

    public void refresh() {
        Effect effect = effectGui.getProfile().getEffectManager().getEffects().get(effectType);
        boolean effectActive = effect != null && effect.active();
        int effectAmplifier = effect == null ? 0 : effect.amplifier();
        setLore(
                messageSupplier.getMessage("effectgui.effectitem." + (effectActive? "" : "in") + "active-format", effectActive ? ChatColor.GREEN : ChatColor.RED),
                messageSupplier.getFormatted("effectgui.effectitem.level-format", new MessageFormatter(messageSupplier.getActiveCraftMessage(), new Pair<>("level", "" + (effectAmplifier + 1)))),
                messageSupplier.getMessage("effectgui.effectitem.tooltip")
        );
    }
}