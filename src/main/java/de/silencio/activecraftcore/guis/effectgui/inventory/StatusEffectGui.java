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
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.potion.PotionEffectType;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class StatusEffectGui extends GuiCreator {

    private final Player player;
    private final Player target;
    private final Profile profile;
    private final EffectGui effectGui;

    private GuiItem prevPage, headerItem;

    private EffectItem absorption, badLuck, badOmen, blindness, conduitPower, dolphinsGrace, glowing, haste,
            healthBoost, villageHero, hunger, levitation, miningFatigue, nausea, resistance, saturation, wither;

    public StatusEffectGui(EffectGui effectGui) {
        super("status_effect_gui", 6, EffectGuiMessages.HEADER_STATUSEFFECTS());
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

        setItem(prevPage = new GuiItem(Material.ARROW)
                        .setDisplayName(ChatColor.GRAY + EffectGuiMessages.HEADER_POTIONEFFECTS())
                        .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, effectGui.getPotionEffectGui().build())),
                45);

        badOmen = new EffectItem(Material.WHITE_BANNER, PotionEffectType.BAD_OMEN, profile);
        BannerMeta bannerMeta = (BannerMeta) badOmen.getItemMeta();
        bannerMeta.addPattern(new Pattern(DyeColor.CYAN, PatternType.RHOMBUS_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.STRIPE_BOTTOM));
        bannerMeta.addPattern(new Pattern(DyeColor.GRAY, PatternType.STRIPE_CENTER));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.BORDER));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.HALF_HORIZONTAL));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.CIRCLE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));
        badOmen.setItemMeta(bannerMeta);

        setItem(absorption = new EffectItem(Material.SHIELD, PotionEffectType.ABSORPTION, profile), 10);
        setItem(badLuck = new EffectItem(Material.DEAD_BUSH, PotionEffectType.UNLUCK, profile), 11);
        setItem(badOmen, 12);
        setItem(blindness = new EffectItem(Material.BLACK_CONCRETE, PotionEffectType.BLINDNESS, profile), 13);
        setItem(conduitPower = new EffectItem(Material.CONDUIT, PotionEffectType.CONDUIT_POWER, profile), 14);
        setItem(dolphinsGrace = new EffectItem(Material.DOLPHIN_SPAWN_EGG, PotionEffectType.DOLPHINS_GRACE, profile), 15);
        setItem(glowing = new EffectItem(Material.SPECTRAL_ARROW, PotionEffectType.GLOWING, profile), 16);
        setItem(haste = new EffectItem(Material.GOLDEN_PICKAXE, PotionEffectType.FAST_DIGGING, profile), 19);
        setItem(healthBoost = new EffectItem(Material.LINGERING_POTION, PotionEffectType.HEALTH_BOOST, profile, true), 20);
        setItem(villageHero = new EffectItem(Material.EMERALD, PotionEffectType.HERO_OF_THE_VILLAGE, profile), 21);
        setItem(hunger = new EffectItem(Material.ROTTEN_FLESH, PotionEffectType.HUNGER, profile), 22);
        setItem(levitation = new EffectItem(Material.SHULKER_SHELL, PotionEffectType.LEVITATION, profile), 23);
        setItem(miningFatigue = new EffectItem(Material.DARK_PRISMARINE, PotionEffectType.SLOW_DIGGING, profile), 24);
        setItem(nausea = new EffectItem(Material.PUFFERFISH, PotionEffectType.CONFUSION, profile), 25);
        setItem(resistance = new EffectItem(Material.NETHERITE_CHESTPLATE, PotionEffectType.DAMAGE_RESISTANCE, profile), 28);
        setItem(saturation = new EffectItem(Material.BREAD, PotionEffectType.SATURATION, profile), 29);
        setItem(wither = new EffectItem(Material.WITHER_SKELETON_SKULL, PotionEffectType.WITHER, profile), 30);
    }
}
