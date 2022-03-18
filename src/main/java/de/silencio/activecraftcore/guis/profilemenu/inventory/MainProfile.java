package de.silencio.activecraftcore.guis.profilemenu.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.effectgui.EffectGui;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.messages.EffectGuiMessages;
import de.silencio.activecraftcore.messages.ProfileMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.IntegerUtils;
import de.silencio.activecraftcore.utils.ItemBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;

import java.text.NumberFormat;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class MainProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player;
    private final Player target;
    private final Profile profile;

    private GuiItem connectionInfoStack, gameStats, violationStack, violationInfoStack, activeEffectsStack,
            gamemodeSwitcherStack, actionMenuStack, storageMenuStack, playerLocationStack, playtimeStack;
    private ItemBuilder activeEffectsBuilder;

    public MainProfile(ProfileMenu profileMenu) {
        super("main_profile", 6, ProfileMessages.MainProfile.TITLE());
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        refresh();
        profileMenu.setMainProfile(this);
    }

    @Override
    public void refresh() {
        // General Settings
        fillBackground(true);
        setCloseItem(49);
        setPlayerHead(profileMenu.getPlayerHead(), 4);

        // Player Items
        GuiItem slotEmpty = new GuiItem(Material.RED_STAINED_GLASS_PANE)
                .setDisplayName(ProfileMessages.MainProfile.EMPTY_SLOT());
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
                .setDisplayName(ProfileMessages.MainProfile.CONNECTION_TITLE())
                .setLore(ProfileMessages.MainProfile.CONNECTION_IP(target.getAddress().getAddress().toString().replace("/", "")),
                        ProfileMessages.MainProfile.CONNECTION_PORT(target.getAddress().getPort()),
                        ProfileMessages.MainProfile.CONNECTION_PING(target.getPing()));
        setItem(connectionInfoStack, 21, player, "activecraft.connection.info");

        //player Stats
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        gameStats = new GuiItem(Material.GRASS_BLOCK)
                .setDisplayName(ProfileMessages.MainProfile.PLAYER_TITLE())
                .setLore(
                        ProfileMessages.MainProfile.PLAYER_HEALTH(formatter.format(target.getHealth())),
                        ProfileMessages.MainProfile.PLAYER_FOOD(target.getFoodLevel()),
                        ProfileMessages.MainProfile.PLAYER_EXP(target.getLevel()),
                        ProfileMessages.MainProfile.GAMEMODE(switch (target.getGameMode()) {
                            case CREATIVE -> "Creative";
                            case SURVIVAL -> "Survival";
                            case SPECTATOR -> "Spectator";
                            case ADVENTURE -> "Adventure";
                        }));
        setItem(gameStats, 12, player, "activecraft.stats.info");

        // Player Violations Information
        violationInfoStack = new GuiItem(Material.COMMAND_BLOCK)
                .setDisplayName(ProfileMessages.MainProfile.VIOLATIONS_TITLE())
                .setLore(ProfileMessages.MainProfile.VIOLATIONS_BANS(profile.getBans()),
                        ProfileMessages.MainProfile.VIOLATIONS_IP_BANS(profile.getIpBans()),
                        ProfileMessages.MainProfile.VIOLATIONS_WARNS(profile.getWarns()),
                        ProfileMessages.MainProfile.VIOLATIONS_MUTES(profile.getMutes()));
        setItem(violationInfoStack, 30, player, "activecraft.violations.info");

        // active effects
        activeEffectsBuilder = new ItemBuilder(Material.POTION)
                .displayname(ProfileMessages.MainProfile.ACTIVE_EFFECTS());
        target.getActivePotionEffects().forEach(
                effect -> activeEffectsBuilder.lore(
                        EffectGuiMessages.EFFECT(effect.getType()) + ChatColor.GRAY + "; "
                                + ChatColor.DARK_AQUA + effect.getAmplifier() + ChatColor.GRAY + "; "
                                + ChatColor.DARK_AQUA + IntegerUtils.shortInteger(effect.getDuration() / 20))
        );
        activeEffectsStack = new GuiItem(activeEffectsBuilder.build());
        PotionMeta potionMeta = (PotionMeta) activeEffectsStack.getItemMeta();
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        activeEffectsStack.setItemMeta(potionMeta);
        // implement effectgui
        activeEffectsStack.addClickListener(guiClickEvent -> GuiNavigator.push(player, new EffectGui(player, target).getPotionEffectGui().build()));
        setItem(activeEffectsStack, 14, player, "activecraft.activeeffects");

        // Player Location Information
        playerLocationStack = new GuiItem(Material.REDSTONE_TORCH)
                .setDisplayName(ProfileMessages.MainProfile.PLAYER_LOCATION())
                .setLore(ChatColor.DARK_AQUA + target.getWorld().getName() + "; " + target.getLocation().getBlockX() + ", "
                        + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ());
        setItem(playerLocationStack, 16, player, "activecraft.location");

        //Player Playtime
        playtimeStack = new GuiItem(Material.CLOCK)
                .setDisplayName(ProfileMessages.MainProfile.PLAYTIME())
                .setLore(ProfileMessages.MainProfile.PLAYTIME_LORE(profile.getPlaytimeHours(), profile.getPlaytimeMinutes()));
        setItem(playtimeStack, 15, player, "activecraft.playtime");


        // Other Menus
        // Player Punishment
        violationStack = new GuiItem(Material.COMMAND_BLOCK)
                .setDisplayName(ProfileMessages.MainProfile.VIOLATIONS_GUI())
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getViolationsProfile().build()));
        setItem(violationStack, 42);

        // Player Gamemode Switcher
        gamemodeSwitcherStack = new GuiItem(Material.GRASS_BLOCK)
                .setDisplayName(ProfileMessages.MainProfile.GAMEMODE_SWITCHER_GUI())
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getGamemodeSwitcherProfile().build()));
        setItem(gamemodeSwitcherStack, 32);

        // Player Storage Menu
        storageMenuStack = new GuiItem(Material.CHEST)
                .setDisplayName(ProfileMessages.MainProfile.STORAGE_GUI())
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getStorageProfile().build()));
        setItem(storageMenuStack, 33);

        // Player Actions Menu
        actionMenuStack = new GuiItem(Material.LEVER)
                .setDisplayName(ProfileMessages.MainProfile.ACTION_GUI())
                .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getActionProfile().build()));
        setItem(actionMenuStack, 34);
    }
}