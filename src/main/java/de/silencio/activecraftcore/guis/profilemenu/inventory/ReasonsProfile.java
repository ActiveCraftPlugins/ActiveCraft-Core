package de.silencio.activecraftcore.guis.profilemenu.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.guis.profilemenu.item.ReasonItem;
import de.silencio.activecraftcore.guis.profilemenu.item.TimeItem;
import de.silencio.activecraftcore.manager.BanManager;
import de.silencio.activecraftcore.messages.Durations;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.messages.ProfileMessages;
import de.silencio.activecraftcore.messages.Reasons;
import de.silencio.activecraftcore.playermanagement.Profile;
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
    private final Player player;
    private final Player target;
    private final Profile profile;
    private Inventory reasonsTimeInventory;
    private Inventory reasonsInventory;

    private final ViolationType violationType;
    private Reason activeReason;
    private int banTime;
    private GuiItem reasonStackHacking, reasonStackBotting, reasonStackUnauthorizedAltAcc,
            reasonStackSpam, reasonStackAbusiveLang, reasonStackScamming, reasonStackGriefing,
            m15Stack, h1Stack, h8Stack, d1Stack, d7Stack, M1Stack, permanentStack, confirmationStack;
    private GuiItem notSelectedStack = new GuiItem(Material.RED_STAINED_GLASS_PANE).setDisplayName(ProfileMessages.ReasonsProfile.NOT_SELECTED()).setClickSound(null);
    private GuiItem selectedStack = new GuiItem(Material.LIME_STAINED_GLASS_PANE).setDisplayName(ProfileMessages.ReasonsProfile.SELECTED()).setClickSound(null);


    public enum ViolationType {
        BAN,
        BAN_IP,
        WARN,
        KICK
    }

    public ReasonsProfile(ProfileMenu profileMenu, ViolationType violationType) {
        super("reasons_profile", 6, ProfileMessages.ReasonsProfile.TITLE());
        this.profileMenu = profileMenu;
        this.violationType = violationType;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        activeReason = Reason.MODERATOR;

        for (int i = 19; i < 26; i++) {
            setItem(notSelectedStack, i);
            if (violationType == ViolationType.BAN || violationType == ViolationType.BAN_IP)
                setItem(notSelectedStack, i + 18);
        }
    }

    public enum Reason {
        MODERATOR,
        HACKING,
        BOTTING,
        UNAUTHORIZED_ALTERNATE_ACCOUNT,
        SPAM,
        ABUSIVE_LANGUAGE,
        SCAMMING,
        GRIEFING
    }

    public void select(int start, int end, int selectedSlot) {
        for (int i = start; i < end; i++)
            setItem(notSelectedStack, i);
        setItem(selectedStack, selectedSlot+9);
    }

    @Override
    public void refresh() {

        setPlayerHead(profileMenu.getPlayerHead(), 4);
        setCloseItem(49);
        setBackItem(48);
        fillBackground(true);

        setItem(reasonStackHacking = new ReasonItem(Reason.HACKING, this), 10);
        setItem(reasonStackBotting = new ReasonItem(Reason.BOTTING, this), 11);
        setItem(reasonStackAbusiveLang = new ReasonItem(Reason.ABUSIVE_LANGUAGE, this), 12);
        setItem(reasonStackSpam = new ReasonItem(Reason.SPAM, this), 13);
        setItem(reasonStackGriefing = new ReasonItem(Reason.GRIEFING, this), 14);
        setItem(reasonStackScamming = new ReasonItem(Reason.SCAMMING, this), 15);
        setItem(reasonStackUnauthorizedAltAcc = new ReasonItem(Reason.UNAUTHORIZED_ALTERNATE_ACCOUNT, this), 16);

        setItem(confirmationStack = new GuiItem(Material.LIME_DYE).setDisplayName(ProfileMessages.ReasonsProfile.CONFIRM())
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
                            BanManager.Name.ban(profileMenu.getTarget(), getReasonString(activeReason), expires, player.getName());
                            profileMenu.getTarget().kickPlayer(getReasonString(activeReason));
                        }
                        case BAN_IP -> {
                            if (!player.hasPermission("activecraft.ban")) {
                                player.sendMessage(Errors.NO_PERMISSION());
                                return;
                            }
                            Date nowDate = new Date();
                            long nowMillis = nowDate.getTime();
                            Date expires = banTime == -1 ? null : new Date((long) banTime * 60 * 1000 + nowMillis);
                            BanManager.IP.ban(target.getAddress().getAddress().toString().replace("/", ""), getReasonString(activeReason), expires, player.getName());
                            profileMenu.getTarget().kickPlayer(getReasonString(activeReason));
                        }
                        case WARN -> {
                            player.performCommand("warn add " + profileMenu.getTarget().getName() + " " + getReasonString(activeReason));
                        }
                        case KICK -> {
                            if (player.hasPermission("activecraft.kick")) {
                                profileMenu.getTarget().kickPlayer(getReasonString(activeReason));
                            } else player.sendMessage(Errors.NO_PERMISSION());
                        }
                    }
                    GuiNavigator.pop(player);
                }), 50);

        if (violationType == ViolationType.BAN || violationType == ViolationType.BAN_IP) {
            setItem(m15Stack = new TimeItem(15, Durations.MINUTES_15()), 28);
            setItem(h1Stack = new TimeItem(60, Durations.HOUR_1()), 29);
            setItem(h8Stack = new TimeItem(480, Durations.HOURS_8()), 30);
            setItem(d1Stack = new TimeItem(1440, Durations.DAY_1()), 31);
            setItem(d7Stack = new TimeItem(10080, Durations.DAYS_7()), 32);
            setItem(M1Stack = new TimeItem(302400, Durations.MONTH_1()), 33);
            setItem(permanentStack = new TimeItem(-1, Durations.PERMANENT()), 34);
        }
    }

    public String getActiveReasonString() {
        return getReasonString(activeReason);
    }

    public String getReasonString(Reason reason) {
        return switch (reason) {
            case MODERATOR -> switch (violationType) {
                case BAN, BAN_IP -> Reasons.MODERATOR_BANNED();
                case KICK -> Reasons.MODERATOR_KICKED();
                case WARN -> Reasons.MODERATOR_WARNED();
            };
            case HACKING -> Reasons.HACKING();
            case BOTTING -> Reasons.BOTTING();
            case ABUSIVE_LANGUAGE -> Reasons.ABUSIVE_LANGUAGE();
            case SPAM -> Reasons.SPAM();
            case GRIEFING -> Reasons.GRIEFING();
            case SCAMMING -> Reasons.STEALING();
            case UNAUTHORIZED_ALTERNATE_ACCOUNT -> Reasons.UNAUTHORIZED_ALTERNATE_ACCOUNT();
        };
    }
}
