package org.activecraft.activecraftcore.guis.effectgui.inventory;

import org.activecraft.activecraftcore.guicreator.*;
import org.activecraft.activecraftcore.guis.effectgui.EffectGui;
import org.activecraft.activecraftcore.guis.effectgui.item.EffectItem;
import org.activecraft.activecraftcore.guis.effectgui.item.LevelChangerItem;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import kotlin.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class LevelChangerGui extends GuiCreator {

    private final Player player;
    private final Player target;
    private final Profilev2 profile;
    private final EffectGui effectGui;
    private final PotionEffectType effectType;

    private GuiItem plus_1, plus_2, plus_4, plus_8, plus_16;
    private GuiItem minus_1, minus_2, minus_4, minus_8, minus_16;
    private GuiItem levelItem;
    private final MessageSupplier messageSupplier;

    public LevelChangerGui(EffectGui effectGui, PotionEffectType effectType) {
        super("level_changer_effect_gui", 5, effectGui.getMessageSupplier().getMessage("effectgui.header.potioneffects"));
        this.effectGui = effectGui;
        this.effectType = effectType;
        this.messageSupplier = effectGui.getMessageSupplier();
        this.player = effectGui.getPlayer();
        this.target = effectGui.getTarget();
        this.profile = effectGui.getProfile();
    }

    @Override
    public void refresh() {
        fillBackground(true);
        setItem(effectGui.getDefaultGuiCloseItem(), 40);
        setItem(new GuiPlayerHead(target), 4);
        setItem(effectGui.getDefaultGuiBackItem(), 36);

        for (GuiItem item : Stream.concat(
                Arrays.stream(effectGui.getPotionEffectGui().getItems()),
                Arrays.stream(effectGui.getStatusEffectGui().getItems())).toList()) {
            if (!(item instanceof EffectItem effectItem)) continue;
            if (effectItem.getEffectType() == effectType) {
                var effects = profile.getEffectManager().getEffects();
                setItem(levelItem = new GuiItem(Material.YELLOW_CONCRETE, effects.get(effectType).amplifier() + 1)
                        .setDisplayName(
                                effectGui.getMessageSupplier().getFormatted("effectgui.effectitem.level-format",
                                        new MessageFormatter(activeCraftCoreMessage, new Pair<>("level", (effects.get(effectType).amplifier() + 1) + "")))), 22);
                break;
            }
        }

        setItem(plus_1 = new LevelChangerItem(Material.GREEN_CONCRETE, 1, this), 11);
        setItem(plus_2 = new LevelChangerItem(Material.GREEN_CONCRETE, 2, this), 12);
        setItem(plus_4 = new LevelChangerItem(Material.GREEN_CONCRETE, 4, this), 13);
        setItem(plus_8 = new LevelChangerItem(Material.GREEN_CONCRETE, 8, this), 14);
        setItem(plus_16 = new LevelChangerItem(Material.GREEN_CONCRETE, 16, this), 15);
        setItem(minus_1 = new LevelChangerItem(Material.RED_CONCRETE, 1, this, true), 29);
        setItem(minus_2 = new LevelChangerItem(Material.RED_CONCRETE, 2, this, true), 30);
        setItem(minus_4 = new LevelChangerItem(Material.RED_CONCRETE, 4, this, true), 31);
        setItem(minus_8 = new LevelChangerItem(Material.RED_CONCRETE, 8, this, true), 32);
        setItem(minus_16 = new LevelChangerItem(Material.RED_CONCRETE, 16, this, true), 33);
    }
}
