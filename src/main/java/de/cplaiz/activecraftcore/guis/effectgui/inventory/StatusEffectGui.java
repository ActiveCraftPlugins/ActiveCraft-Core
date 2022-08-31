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
    private final Profilev2 profile;
    private final EffectGui effectGui;

    private GuiItem prevPage, headerItem;

    private EffectItem absorption, badLuck, badOmen, blindness, conduitPower, dolphinsGrace, glowing, haste,
            healthBoost, villageHero, hunger, levitation, miningFatigue, nausea, resistance, saturation, wither;

    public StatusEffectGui(EffectGui effectGui) {
        super("status_effect_gui", 6, effectGui.getMessageSupplier().getMessage("effectgui.header.statuseffects"));
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

        setItem(prevPage = new GuiItem(Material.ARROW)
                        .setDisplayName(ChatColor.GRAY + effectGui.getMessageSupplier().getMessage("effectgui.header.potioneffects"))
                        .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, effectGui.getPotionEffectGui().build())),
                45);

        badOmen = new EffectItem(Material.WHITE_BANNER, PotionEffectType.BAD_OMEN, effectGui);
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

        setItem(absorption = new EffectItem(Material.SHIELD, PotionEffectType.ABSORPTION, effectGui), 10);
        setItem(badLuck = new EffectItem(Material.DEAD_BUSH, PotionEffectType.UNLUCK, effectGui), 11);
        setItem(badOmen, 12);
        setItem(blindness = new EffectItem(Material.BLACK_CONCRETE, PotionEffectType.BLINDNESS, effectGui), 13);
        setItem(conduitPower = new EffectItem(Material.CONDUIT, PotionEffectType.CONDUIT_POWER, effectGui), 14);
        setItem(dolphinsGrace = new EffectItem(Material.DOLPHIN_SPAWN_EGG, PotionEffectType.DOLPHINS_GRACE, effectGui), 15);
        setItem(glowing = new EffectItem(Material.SPECTRAL_ARROW, PotionEffectType.GLOWING, effectGui), 16);
        setItem(haste = new EffectItem(Material.GOLDEN_PICKAXE, PotionEffectType.FAST_DIGGING, effectGui), 19);
        setItem(healthBoost = new EffectItem(Material.LINGERING_POTION, PotionEffectType.HEALTH_BOOST, effectGui, true), 20);
        setItem(villageHero = new EffectItem(Material.EMERALD, PotionEffectType.HERO_OF_THE_VILLAGE, effectGui), 21);
        setItem(hunger = new EffectItem(Material.ROTTEN_FLESH, PotionEffectType.HUNGER, effectGui), 22);
        setItem(levitation = new EffectItem(Material.SHULKER_SHELL, PotionEffectType.LEVITATION, effectGui), 23);
        setItem(miningFatigue = new EffectItem(Material.DARK_PRISMARINE, PotionEffectType.SLOW_DIGGING, effectGui), 24);
        setItem(nausea = new EffectItem(Material.PUFFERFISH, PotionEffectType.CONFUSION, effectGui), 25);
        setItem(resistance = new EffectItem(Material.NETHERITE_CHESTPLATE, PotionEffectType.DAMAGE_RESISTANCE, effectGui), 28);
        setItem(saturation = new EffectItem(Material.BREAD, PotionEffectType.SATURATION, effectGui), 29);
        setItem(wither = new EffectItem(Material.WITHER_SKELETON_SKULL, PotionEffectType.WITHER, effectGui), 30);
    }
}
