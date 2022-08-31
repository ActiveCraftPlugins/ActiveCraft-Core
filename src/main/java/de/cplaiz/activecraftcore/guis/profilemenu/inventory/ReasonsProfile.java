package de.cplaiz.activecraftcore.guis.profilemenu.inventory;

import de.cplaiz.activecraftcore.guicreator.GuiCreator;
import de.cplaiz.activecraftcore.guicreator.GuiItem;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.profilemenu.ProfileMenu;
import de.cplaiz.activecraftcore.guis.profilemenu.item.ReasonItem;
import de.cplaiz.activecraftcore.guis.profilemenu.item.TimeItem;
import de.cplaiz.activecraftcore.manager.BanManager;
import de.cplaiz.activecraftcore.messages.Durations;
import de.cplaiz.activecraftcore.messages.Errors;
import de.cplaiz.activecraftcore.messages.Reasons;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ReasonsProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final Profilev2 profile;
    private Inventory reasonsTimeInventory, reasonsInventory;
    private final ViolationType violationType;
    private String activeReason;
    private int banTime;
    private GuiItem reasonStackHacking, reasonStackBotting, reasonStackUnauthorizedAltAcc,
            reasonStackSpam, reasonStackAbusiveLang, reasonStackScamming, reasonStackGriefing,
            m15Stack, h1Stack, h8Stack, d1Stack, d7Stack, M1Stack, permanentStack, confirmationStack;
    private MessageSupplier messageSupplier;
    private static final String PREFIX = "profile.reasons-gui.";
    private GuiItem notSelectedStack = new GuiItem(Material.RED_STAINED_GLASS_PANE)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "not-selected")).setClickSound(null);
    private GuiItem selectedStack = new GuiItem(Material.LIME_STAINED_GLASS_PANE)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "selected")).setClickSound(null);

    public enum ViolationType {
        BAN,
        BAN_IP,
        WARN,
        KICK
    }

    public ReasonsProfile(ProfileMenu profileMenu, ViolationType violationType) {
        super("reasons_profile", 6, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.messageSupplier = profileMenu.getMessageSupplier();
        this.violationType = violationType;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();

        for (int i = 19; i < 26; i++) {
            setItem(notSelectedStack, i);
            if (violationType == ViolationType.BAN || violationType == ViolationType.BAN_IP)
                setItem(notSelectedStack, i + 18);
        }
    }

    public void select(int start, int end, int selectedSlot) {
        for (int i = start; i < end; i++)
            setItem(notSelectedStack, i);
        setItem(selectedStack, selectedSlot + 9);
    }

    @Override
    public void refresh() {

        setItem(profileMenu.getDefaultGuiCloseItem(), 49);
        setItem(profileMenu.getDefaultGuiBackItem(), 48);
        setItem(profileMenu.getPlayerHead(), 4);
        fillBackground(true);

        setItem(reasonStackHacking = new ReasonItem(messageSupplier.getReasons().hacking(), this), 10);
        setItem(reasonStackBotting = new ReasonItem(messageSupplier.getReasons().botting(), this), 11);
        setItem(reasonStackAbusiveLang = new ReasonItem(messageSupplier.getReasons().abusiveLanguage(), this), 12);
        setItem(reasonStackSpam = new ReasonItem(messageSupplier.getReasons().spam(), this), 13);
        setItem(reasonStackGriefing = new ReasonItem(messageSupplier.getReasons().griefing(), this), 14);
        setItem(reasonStackScamming = new ReasonItem(messageSupplier.getReasons().stealing(), this), 15);
        setItem(reasonStackUnauthorizedAltAcc = new ReasonItem(messageSupplier.getReasons().unauthorizedAlternateAccount(), this), 16);

        setItem(confirmationStack = new GuiItem(Material.LIME_DYE).setDisplayName(messageSupplier.getMessage(PREFIX + "confirm"))
                .addClickListener(guiClickEvent -> {
                    switch (violationType) {
                        case BAN -> {
                            if (!player.hasPermission("activecraft.ban")) {
                                player.sendMessage(Errors.NO_PERMISSION());
                                return;
                            }
                            Date nowDate = new Date();
                            long nowMillis = nowDate.getTime();
                            Date expires = banTime == -1 ? null : new Date((long) banTime * 60 * 1000 + nowMillis);
                            BanManager.Name.ban(profileMenu.getTarget(), activeReason, expires, player.getName());
                            profileMenu.getTarget().kickPlayer(activeReason);
                        }
                        case BAN_IP -> {
                            if (!player.hasPermission("activecraft.ban")) {
                                player.sendMessage(Errors.NO_PERMISSION());
                                return;
                            }
                            Date nowDate = new Date();
                            long nowMillis = nowDate.getTime();
                            Date expires = banTime == -1 ? null : new Date((long) banTime * 60 * 1000 + nowMillis);
                            BanManager.IP.ban(target.getAddress().getAddress().toString().replace("/", ""), activeReason, expires, player.getName());
                            profileMenu.getTarget().kickPlayer(activeReason);
                        }
                        case WARN -> {
                            player.performCommand("warn add " + profileMenu.getTarget().getName() + " " + activeReason);
                        }
                        case KICK -> {
                            if (player.hasPermission("activecraft.kick")) {
                                profileMenu.getTarget().kickPlayer(activeReason);
                            } else player.sendMessage(Errors.NO_PERMISSION());
                        }
                    }
                    GuiNavigator.pop(player);
                }), 50);

        if (violationType == ViolationType.BAN || violationType == ViolationType.BAN_IP) {
            setItem(m15Stack = new TimeItem(15, messageSupplier.getDurations().minutes15(), this), 28);
            setItem(h1Stack = new TimeItem(60, messageSupplier.getDurations().hour1(), this), 29);
            setItem(h8Stack = new TimeItem(480, messageSupplier.getDurations().hours8(), this), 30);
            setItem(d1Stack = new TimeItem(1440, messageSupplier.getDurations().day1(), this), 31);
            setItem(d7Stack = new TimeItem(10080, messageSupplier.getDurations().days7(), this), 32);
            setItem(M1Stack = new TimeItem(302400, messageSupplier.getDurations().month1(), this), 33);
            setItem(permanentStack = new TimeItem(-1, messageSupplier.getDurations().permanent(), this), 34);
        }
    }
}
