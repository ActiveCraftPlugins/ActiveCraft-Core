package de.cplaiz.activecraftcore.guis.profilemenu.inventory;

import de.cplaiz.activecraftcore.guicreator.GuiCreator;
import de.cplaiz.activecraftcore.guicreator.GuiCreatorDefaults;
import de.cplaiz.activecraftcore.guicreator.GuiItem;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.effectgui.EffectGui;
import de.cplaiz.activecraftcore.guis.profilemenu.ProfileMenu;
import de.cplaiz.activecraftcore.messagesv2.ColorScheme;
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.IntegerUtils;
import de.cplaiz.activecraftcore.utils.ItemBuilder;
import de.cplaiz.activecraftcore.utils.StringUtils;
import kotlin.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;

import java.text.NumberFormat;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class MainProfile extends GuiCreator {

    private static final String PREFIX = "profile.mainprofile.";
    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final Profilev2 profile;
    private GuiItem connectionInfoStack, gameStats, violationStack, violationInfoStack, activeEffectsStack,
            gamemodeSwitcherStack, actionMenuStack, storageMenuStack, playerLocationStack, playtimeStack;
    private ItemBuilder activeEffectsBuilder;
    private final MessageSupplier messageSupplier;
    private final ColorScheme colorScheme;

    public MainProfile(ProfileMenu profileMenu) {
        super("main_profile", 6, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        messageSupplier = profileMenu.getMessageSupplier();
        colorScheme = profileMenu.getColorScheme();
        refresh();
        profileMenu.setMainProfile(this);
    }

    @Override
    public void refresh() {
        // General Settings
        fillBackground(true);
        setItem(profileMenu.getDefaultGuiCloseItem(), 49);
        setItem(profileMenu.getPlayerHead(), 4);
        PlayerMessageFormatter msgFormatter = new PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage);
        int playtime = profile.getPlaytime();
        int playtimeMinutes = playtime % 60;
        int playtimeHours = (playtime - playtimeMinutes) / 60;
        msgFormatter
                .setTarget(profile)
                .addFormatterPatterns(new Pair<>("ip", target.getAddress().getHostName()), // TODO: 22.08.2022 testen ob das klappt. wenn nicht dann wieder "target.getAddress().getAddress().toString().replace("/", "")"
                        new Pair<>("port", target.getAddress().getPort() + ""),
                        new Pair<>("ping", target.getPing() + ""),
                        new Pair<>("health", target.getHealth() + ""),
                        new Pair<>("food", target.getFoodLevel() + ""),
                        new Pair<>("xp", target.getLevel() + ""),
                        new Pair<>("gamemode", StringUtils.toJadenCase(target.getGameMode().name())),
                        new Pair<>("bans", profile.getTimesBanned() + ""),
                        new Pair<>("ipbans", profile.getTimesIpBanned() + ""),
                        new Pair<>("warns", profile.getTimesWarned() + ""),
                        new Pair<>("mutes", profile.getTimesMuted() + ""),
                        new Pair<>("hours", playtimeHours + ""),
                        new Pair<>("minutes", playtimeMinutes + "")
                );

        // Player Items
        GuiItem slotEmpty = new GuiItem(Material.RED_STAINED_GLASS_PANE)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "empty-slot"));
        GuiItem offHand = new GuiItem(target.getInventory().getItemInOffHand());
        GuiItem mainHand = new GuiItem(target.getInventory().getItemInMainHand());
        setItem(mainHand.getType() != Material.AIR ? mainHand : slotEmpty, 29);
        setItem(offHand.getType() != Material.AIR ? offHand : slotEmpty, 20);
        setItem(target.getInventory().getHelmet() != null ? new GuiItem(target.getInventory().getHelmet()) : slotEmpty, 10);
        setItem(target.getInventory().getChestplate() != null ? new GuiItem(target.getInventory().getChestplate()) : slotEmpty, 19);
        setItem(target.getInventory().getLeggings() != null ? new GuiItem(target.getInventory().getLeggings()) : slotEmpty, 28);
        setItem(target.getInventory().getBoots() != null ? new GuiItem(target.getInventory().getBoots()) : slotEmpty, 37);

        // Player Connection Information
        connectionInfoStack = new GuiItem(Material.STRUCTURE_VOID)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "connection.title"))
                .setLore(messageSupplier.getFormatted(PREFIX + "connection.ip", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "connection.port", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "connection.ping", msgFormatter));
        setItem(connectionInfoStack, 21, player, "activecraft.connection.info");

        //player Stats
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        gameStats = new GuiItem(Material.GRASS_BLOCK)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "player.title"))
                .setLore(messageSupplier.getFormatted(PREFIX + "player.health", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "player.food", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "player.exp", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "player.gamemode", msgFormatter));
        setItem(gameStats, 12, player, "activecraft.stats.info");

        // Player Violations Information
        violationInfoStack = new GuiItem(Material.COMMAND_BLOCK)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "violations.title"))
                .setLore(messageSupplier.getFormatted(PREFIX + "violations.bans", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "violations.ip-bans", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "violations.warns", msgFormatter),
                        messageSupplier.getFormatted(PREFIX + "violations.mutes", msgFormatter));
        setItem(violationInfoStack, 30, player, "activecraft.violations.info");

        // active effects
        activeEffectsBuilder = new ItemBuilder(Material.POTION)
                .displayname(messageSupplier.getMessage(PREFIX + "active-effects"));
        target.getActivePotionEffects().forEach(
                effect -> activeEffectsBuilder.lore(
                        messageSupplier.getMessage("effects." + effect.getType().getName().toLowerCase().replace("_", "-"))
                                + colorScheme.secondary() + "; "
                                + colorScheme.secondaryAccent() + effect.getAmplifier() + colorScheme.secondary() + "; "
                                + colorScheme.secondaryAccent() + IntegerUtils.shortInteger(effect.getDuration() / 20))
        );
        activeEffectsStack = new GuiItem(activeEffectsBuilder.build());
        PotionMeta potionMeta = (PotionMeta) activeEffectsStack.getItemMeta();
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        activeEffectsStack.setItemMeta(potionMeta);
        // implement effectgui
        activeEffectsStack.addClickListener(guiClickEvent -> GuiNavigator.push(player, new EffectGui(player, target).getPotionEffectGui().build()));
        setItem(activeEffectsStack, 14, player, "activecraft.activeeffects");

        // Player Location Information
        // TODO: 22.08.2022 updaten
        playerLocationStack = new GuiItem(Material.REDSTONE_TORCH)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "player-location"))
                .setLore(
                        colorScheme.secondaryAccent() + target.getWorld().getName() + colorScheme.secondary() + "; "
                                + colorScheme.secondaryAccent() + target.getLocation().getBlockX() + colorScheme.secondary() + ", "
                                + colorScheme.secondaryAccent() + target.getLocation().getBlockY() + colorScheme.secondary() + ", "
                                + colorScheme.secondaryAccent() + target.getLocation().getBlockZ()
                );
        setItem(playerLocationStack, 16, player, "activecraft.location");

        //Player Playtime
        playtimeStack = new GuiItem(Material.CLOCK)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "playtime"))
                .setLore(messageSupplier.getFormatted(PREFIX + "playtime-lore", msgFormatter));
        setItem(playtimeStack, 15, player, "activecraft.playtime");


        // Other Menus
        // Player Punishment
        violationStack = new GuiItem(Material.COMMAND_BLOCK)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "violations-gui"))
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getViolationsProfile().build()));
        setItem(violationStack, 42);

        // Player Gamemode Switcher
        gamemodeSwitcherStack = new GuiItem(Material.GRASS_BLOCK)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "gamemode-switcher-gui"))
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getGamemodeSwitcherProfile().build()));
        setItem(gamemodeSwitcherStack, 32);

        // Player Storage Menu
        storageMenuStack = new GuiItem(Material.CHEST)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "storage-gui"))
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getStorageProfile().build()));
        setItem(storageMenuStack, 33);

        // Player Actions Menu
        actionMenuStack = new GuiItem(Material.LEVER)
                .setDisplayName(messageSupplier.getMessage(PREFIX + "action-gui"))
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getActionProfile().build()));
        setItem(actionMenuStack, 34);
    }
}