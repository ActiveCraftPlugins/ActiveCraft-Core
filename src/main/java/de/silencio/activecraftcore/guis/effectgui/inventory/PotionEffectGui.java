package de.silencio.activecraftcore.guis.effectgui.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.effectgui.EffectGui;
import de.silencio.activecraftcore.guis.effectgui.item.EffectItem;
import de.silencio.activecraftcore.messages.EffectGuiMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PotionEffectGui extends GuiCreator {

    private final Player player;
    private final Player target;
    private final Profile profile;
    private final EffectGui effectGui;

    private GuiItem nextPage, headerItem;

    private EffectItem nightVision, invisibility, jumpBoost, fireResistance, speed, slowness, waterBreathing,
            instantHealth, instantdamage, poison, regeneration, strength, weakness, luck, slowFalling;

    public PotionEffectGui(EffectGui effectGui) {
        super("potion_effect_gui", 6, EffectGuiMessages.HEADER_POTIONEFFECTS());
        this.effectGui = effectGui;
        this.player = effectGui.getPlayer();
        this.target = effectGui.getTarget();
        this.profile = effectGui.getProfile();
    }

    @Override
    public void refresh() {
        fillBackground(true);
        setCloseItem(49);
        setPlayerHead(target, 4);

        setItem(nextPage = new GuiItem(Material.ARROW)
                        .setDisplayName(ChatColor.GRAY + EffectGuiMessages.HEADER_STATUSEFFECTS())
                        .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, effectGui.getStatusEffectGui().build())),
                53);

        setItem(nightVision = new EffectItem(Material.POTION, PotionEffectType.NIGHT_VISION, profile, true), 10);
        setItem(invisibility = new EffectItem(Material.POTION, PotionEffectType.INVISIBILITY, profile, true), 11);
        setItem(jumpBoost = new EffectItem(Material.POTION, PotionEffectType.JUMP, profile, true), 12);
        setItem(fireResistance = new EffectItem(Material.POTION, PotionEffectType.FIRE_RESISTANCE, profile, true), 13);
        setItem(speed = new EffectItem(Material.POTION, PotionEffectType.SPEED, profile, true), 14);
        setItem(slowness = new EffectItem(Material.POTION, PotionEffectType.SLOW, profile, true), 15);
        setItem(waterBreathing = new EffectItem(Material.POTION, PotionEffectType.WATER_BREATHING, profile, true), 16);
        setItem(instantHealth = new EffectItem(Material.POTION, PotionEffectType.HEAL, profile, true), 19);
        setItem(instantdamage = new EffectItem(Material.POTION, PotionEffectType.HARM, profile, true), 20);
        setItem(poison = new EffectItem(Material.POTION, PotionEffectType.POISON, profile, true), 21);
        setItem(regeneration = new EffectItem(Material.POTION, PotionEffectType.REGENERATION, profile, true), 22);
        setItem(strength = new EffectItem(Material.POTION, PotionEffectType.INCREASE_DAMAGE, profile, true), 23);
        setItem(weakness = new EffectItem(Material.POTION, PotionEffectType.WEAKNESS, profile, true), 24);
        setItem(luck = new EffectItem(Material.POTION, PotionEffectType.LUCK, profile, true), 25);
        setItem(slowFalling = new EffectItem(Material.POTION, PotionEffectType.SLOW_FALLING, profile, true), 28);
    }
}
