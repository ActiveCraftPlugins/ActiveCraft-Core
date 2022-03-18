package de.silencio.activecraftcore.guis.effectgui.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guis.effectgui.EffectGui;
import de.silencio.activecraftcore.guis.effectgui.item.EffectItem;
import de.silencio.activecraftcore.guis.effectgui.item.LevelChangerItem;
import de.silencio.activecraftcore.messages.EffectGuiMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class LevelChangerGui extends GuiCreator {

    private final Player player;
    private final Player target;
    private final Profile profile;
    private final EffectGui effectGui;
    private final PotionEffectType effectType;

    private GuiItem plus_1, plus_2, plus_4, plus_8, plus_16;
    private GuiItem minus_1, minus_2, minus_4, minus_8, minus_16;
    private GuiItem levelItem;

    public LevelChangerGui(EffectGui effectGui, PotionEffectType effectType) {
        super("level_changer_effect_gui", 5, EffectGuiMessages.HEADER_POTIONEFFECTS());
        this.effectGui = effectGui;
        this.effectType = effectType;
        this.player = effectGui.getPlayer();
        this.target = effectGui.getTarget();
        this.profile = effectGui.getProfile();
    }

    @Override
    public void refresh() {
        fillBackground(true);
        setCloseItem(40);
        setPlayerHead(target, 4);
        setBackItem(36);

        for (GuiItem item : Stream.concat(
                Arrays.stream(effectGui.getPotionEffectGui().getItems()),
                Arrays.stream(effectGui.getStatusEffectGui().getItems()))
                .collect(Collectors.toList())) {
            if (!(item instanceof EffectItem effectItem)) continue;
            if (effectItem.getEffectType() == effectType) {
                setItem(levelItem = new GuiItem(Material.YELLOW_CONCRETE, profile.getEffects().get(effectType).amplifier() + 1)
                        .setDisplayName(EffectGuiMessages.ITEM_LEVEL_FORMAT(profile.getEffects().get(effectType).amplifier() + 1)), 22);
                break;
            }
        }

       setItem(plus_1 = new LevelChangerItem(Material.GREEN_CONCRETE, 1), 11);
       setItem(plus_2 = new LevelChangerItem(Material.GREEN_CONCRETE, 2), 12);
       setItem(plus_4 = new LevelChangerItem(Material.GREEN_CONCRETE, 4), 13);
       setItem(plus_8 = new LevelChangerItem(Material.GREEN_CONCRETE, 8), 14);
       setItem(plus_16 = new LevelChangerItem(Material.GREEN_CONCRETE, 16), 15);
       setItem(minus_1 = new LevelChangerItem(Material.RED_CONCRETE, 1, true), 29);
       setItem(minus_2 = new LevelChangerItem(Material.RED_CONCRETE, 2, true), 30);
       setItem(minus_4 = new LevelChangerItem(Material.RED_CONCRETE, 4, true), 31);
       setItem(minus_8 = new LevelChangerItem(Material.RED_CONCRETE, 8, true), 32);
       setItem(minus_16 = new LevelChangerItem(Material.RED_CONCRETE, 16, true), 33);
    }
}
