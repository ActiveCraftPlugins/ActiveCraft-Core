package de.cplaiz.activecraftcore.guis.effectgui.inventory;

import de.cplaiz.activecraftcore.guicreator.GuiCreator;
import de.cplaiz.activecraftcore.guicreator.GuiItem;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guicreator.GuiPlayerHead;
import de.cplaiz.activecraftcore.guis.effectgui.EffectGui;
import de.cplaiz.activecraftcore.guis.effectgui.item.EffectItem;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
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
    private final Profilev2 profile;
    private final EffectGui effectGui;

    private GuiItem nextPage, headerItem;

    private EffectItem nightVision, invisibility, jumpBoost, fireResistance, speed, slowness, waterBreathing,
            instantHealth, instantdamage, poison, regeneration, strength, weakness, luck, slowFalling;

    public PotionEffectGui(EffectGui effectGui) {
        super("potion_effect_gui", 6, effectGui.getMessageSupplier().getMessage("effectgui.header.potioneffects"));
        this.effectGui = effectGui;
        this.player = effectGui.getPlayer();
        this.target = effectGui.getTarget();
        this.profile = effectGui.getProfile();
    }

    @Override
    public void refresh() {
        fillBackground(true);
        setItem(effectGui.getDefaultGuiCloseItem(), 49);
        setItem(new GuiPlayerHead(target), 4);

        setItem(nextPage = new GuiItem(Material.ARROW)
                        .setDisplayName(ChatColor.GRAY + effectGui.getMessageSupplier().getMessage("effectgui.header.statuseffects"))
                        .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, effectGui.getStatusEffectGui().build())),
                53);

        setItem(nightVision = new EffectItem(Material.POTION, PotionEffectType.NIGHT_VISION, effectGui, true), 10);
        setItem(invisibility = new EffectItem(Material.POTION, PotionEffectType.INVISIBILITY, effectGui, true), 11);
        setItem(jumpBoost = new EffectItem(Material.POTION, PotionEffectType.JUMP, effectGui, true), 12);
        setItem(fireResistance = new EffectItem(Material.POTION, PotionEffectType.FIRE_RESISTANCE, effectGui, true), 13);
        setItem(speed = new EffectItem(Material.POTION, PotionEffectType.SPEED, effectGui, true), 14);
        setItem(slowness = new EffectItem(Material.POTION, PotionEffectType.SLOW, effectGui, true), 15);
        setItem(waterBreathing = new EffectItem(Material.POTION, PotionEffectType.WATER_BREATHING, effectGui, true), 16);
        setItem(instantHealth = new EffectItem(Material.POTION, PotionEffectType.HEAL, effectGui, true), 19);
        setItem(instantdamage = new EffectItem(Material.POTION, PotionEffectType.HARM, effectGui, true), 20);
        setItem(poison = new EffectItem(Material.POTION, PotionEffectType.POISON, effectGui, true), 21);
        setItem(regeneration = new EffectItem(Material.POTION, PotionEffectType.REGENERATION, effectGui, true), 22);
        setItem(strength = new EffectItem(Material.POTION, PotionEffectType.INCREASE_DAMAGE, effectGui, true), 23);
        setItem(weakness = new EffectItem(Material.POTION, PotionEffectType.WEAKNESS, effectGui, true), 24);
        setItem(luck = new EffectItem(Material.POTION, PotionEffectType.LUCK, effectGui, true), 25);
        setItem(slowFalling = new EffectItem(Material.POTION, PotionEffectType.SLOW_FALLING, effectGui, true), 28);
    }
}
