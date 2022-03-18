package de.silencio.activecraftcore.messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ProfileMessages extends ActiveCraftCoreMessage {

    public static class MainProfile {

        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.title");
        }

        public static String EMPTY_SLOT() {
            return ChatColor.RED + getMessage(MessageType.PROFILE, "mainprofile.empty-slot");
        }

        public static String CONNECTION_TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.connection.connection-title");
        }

        public static String CONNECTION_IP(String ip) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.connection.connection-ip")
                    .replace("%ip%", ChatColor.GRAY + ip + ChatColor.DARK_AQUA);
        }

        public static String CONNECTION_PORT(int port) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.connection.connection-port")
                    .replace("%port%", ChatColor.GRAY + "" + port + ChatColor.DARK_AQUA);
        }

        public static String CONNECTION_PING(int ping) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.connection.connection-ping")
                    .replace("%ping%", ChatColor.GRAY + "" + ping + ChatColor.DARK_AQUA);
        }

        public static String PLAYER_TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.player.player-title");
        }

        public static String PLAYER_HEALTH(String health) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.player.player-health")
                    .replace("%health%", ChatColor.GRAY + "" + health + ChatColor.DARK_AQUA);
        }

        public static String PLAYER_FOOD(int food) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.player.player-food")
                    .replace("%food%", ChatColor.GRAY + "" + food + ChatColor.DARK_AQUA);
        }

        public static String PLAYER_EXP(float exp) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.player.player-exp")
                    .replace("%xp%", ChatColor.GRAY + "" + exp + ChatColor.DARK_AQUA);
        }

        public static String GAMEMODE(String gamemode) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.player.gamemode")
                    .replace("%gamemode%", ChatColor.GRAY + gamemode + ChatColor.DARK_AQUA);
        }

        public static String VIOLATIONS_TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.violations.violations-title");
        }

        public static String VIOLATIONS_BANS(int bans) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.violations.violations-bans")
                    .replace("%bans%", ChatColor.GRAY + "" + bans + ChatColor.DARK_AQUA);
        }

        public static String VIOLATIONS_IP_BANS(int ipbans) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.violations.violations-ip-bans")
                    .replace("%ipbans%", ChatColor.GRAY + "" + ipbans + ChatColor.DARK_AQUA);
        }

        public static String VIOLATIONS_WARNS(int warns) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.violations.violations-warns")
                    .replace("%warns%", ChatColor.GRAY + "" + warns + ChatColor.DARK_AQUA);
        }

        public static String VIOLATIONS_MUTES(int mutes) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.violations.violations-mutes")
                    .replace("%mutes%", ChatColor.GRAY + "" + mutes + ChatColor.DARK_AQUA);
        }

        public static String ACTIVE_EFFECTS() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.active-effects");
        }

        public static String PLAYER_LOCATION() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.player-location");
        }

        public static String PLAYTIME() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.playtime");
        }

        public static String PLAYTIME_LORE(int hours, int minutes) {
            return ChatColor.DARK_AQUA + getMessage(MessageType.PROFILE, "mainprofile.playtime-lore")
                    .replace("%hours%", ChatColor.DARK_AQUA + "" + hours + "" + ChatColor.GRAY + "")
                    .replace("%minutes%", ChatColor.DARK_AQUA + "" + minutes + "" + ChatColor.GRAY + "");
        }

        public static String VIOLATIONS_GUI() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.violations-gui");
        }

        public static String GAMEMODE_SWITCHER_GUI() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.gamemode-switcher-gui");
        }

        public static String STORAGE_GUI() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.storage-gui");
        }

        public static String ACTION_GUI() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "mainprofile.action-gui");
        }
    }

    public static class ViolationsProfile {

        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.title");
        }

        public static String BAN(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.ban")
                    .replace("%t_playername%", target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GOLD);
        }

        public static String WARN(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.warn")
                    .replace("%t_playername%", target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GOLD);
        }

        public static String KICK(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.kick")
                    .replace("%t_playername%", target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GOLD);
        }

        public static String MUTE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.mute")
                    .replace("%t_playername%", target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GOLD);
        }

        public static String MUTE_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "violations-gui.mute-lore")
                    .replace("%t_playername%", target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GRAY);
        }

        public static String UNMUTE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.unmute")
                    .replace("%t_playername%", target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GOLD);
        }

        public static String UNMUTE_LORE(Player target) {

            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "violations-gui.unmute-lore")
                    .replace("%t_playername%", target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GRAY);
        }

        public static String BAN_IP(Player target, String ip) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "violations-gui.ban-ip")
                    .replace("%t_playername%", target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", target.getDisplayName() + ChatColor.GOLD)
                    .replace("%ip%", ChatColor.AQUA + ip + ChatColor.GOLD);
        }
    }

    public static class GamemodeSwitcherProfile {

        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.title");
        }

        public static String CREATIVE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.creative")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String SURVIVAL(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.survival")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String SPECTATOR(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.spectator")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String ADVENTURE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.adventure")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String CURRENT_GAMEMODE_CREATIVE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.current-gamemode-creative")
                    .replace(":", ":" + ChatColor.AQUA);
        }

        public static String CURRENT_GAMEMODE_SURVIVAL() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.current-gamemode-survival")
                    .replace(":", ":" + ChatColor.AQUA);
        }

        public static String CURRENT_GAMEMODE_SPECTATOR() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.current-gamemode-spectator")
                    .replace(":", ":" + ChatColor.AQUA);
        }

        public static String CURRENT_GAMEMODE_ADVENTURE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "gamemode-switcher-gui.current-gamemode-adventure")
                    .replace(":", ":" + ChatColor.AQUA);
        }
    }

    public static class StorageProfile {

        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "storage-gui.title");
        }

        public static String OPEN_ENDERCHEST(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "storage-gui.open-enderchest")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String OPEN_INVENTORY(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "storage-gui.open-inventory")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String OPEN_ARMORINV(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "storage-gui.open-armorinv")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String OPEN_ARMORINV_LORE() {
            return ChatColor.RED + getMessage(MessageType.PROFILE, "storage-gui.open-armorinv-lore");
        }
    }

    public static class ActionProfile {

        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.title");
        }

        public static String VANISH_UNVANISH(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.vanish.unvanish")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String VANISH_UNVANISH_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "action-gui.vanish.unvanish-lore")
                    .replace("%t_playername%", ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", ChatColor.DARK_AQUA + target.getDisplayName() + ChatColor.GRAY);
        }

        public static String VANISH_VANISH(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.vanish.vanish")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String VANISH_VANISH_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "action-gui.vanish.vanish-lore")
                    .replace("%t_playername%", ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", ChatColor.DARK_AQUA + target.getDisplayName() + ChatColor.GRAY);
        }

        public static String GOD_DISABLE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.god.disable")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String GOD_DISABLE_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "action-gui.god.disable-lore")
                    .replace("%t_playername%", ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", ChatColor.DARK_AQUA + target.getDisplayName() + ChatColor.GRAY);
        }

        public static String GOD_ENABLE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.god.enable")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String GOD_ENABLE_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "action-gui.god.enable-lore")
                    .replace("%t_playername%", ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", ChatColor.DARK_AQUA + target.getDisplayName() + ChatColor.GRAY);
        }

        public static String FLY_DISABLE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.fly.disable")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String FLY_DISABLE_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "action-gui.fly.disable-lore")
                    .replace("%t_playername%", ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", ChatColor.DARK_AQUA + target.getDisplayName() + ChatColor.GRAY);
        }

        public static String FLY_ENABLE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.fly.enable")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String FLY_ENABLE_LORE(Player target) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "action-gui.fly.enable-lore")
                    .replace("%t_playername%", ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY)
                    .replace("%t_displayname%", ChatColor.DARK_AQUA + target.getDisplayName() + ChatColor.GRAY);
        }

        public static String CLEAR(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.clear")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String TELEPORT_THERE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.teleport-there")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String TELEPORT_HERE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.teleport-here")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String HOMES(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.homes")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String HEAL(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.heal")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String FEED(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.feed")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String STRIKE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.strike")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String EXPLODE(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.explode")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }

        public static String KILL(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "action-gui.kill")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }
    }

    public static class HomelistProfile {

        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "homelist.title");
        }

        public static String NEXT_PAGE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "homelist.next-page");
        }

        public static String PREVIOUS_PAGE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "homelist.previous-page");
        }

        public static String NO_HOMES(Player target) {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "homelist.no-homes")
                    .replace("%t_playername%", ChatColor.AQUA + target.getName() + ChatColor.GOLD)
                    .replace("%t_displayname%", ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD);
        }
    }

    public static class ReasonsProfile {
        public static String TITLE() {
            return ChatColor.GOLD + getMessage(MessageType.PROFILE, "reasons-gui.title");
        }

        public static String CONFIRM() {
            return ChatColor.GREEN + getMessage(MessageType.PROFILE, "reasons-gui.confirm");
        }

        public static String SELECTED() {
            return ChatColor.GREEN + getMessage(MessageType.PROFILE, "reasons-gui.selected");
        }

        public static String NOT_SELECTED() {
            return ChatColor.RED + getMessage(MessageType.PROFILE, "reasons-gui.not-selected");
        }

        public static String SET_TIME(String time) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "reasons-gui.set-time")
                    .replace("%time%", ChatColor.DARK_AQUA + time);
        }

        public static String SET_REASON(String reason) {
            return ChatColor.GRAY + getMessage(MessageType.PROFILE, "reasons-gui.set-reason")
                    .replace("%reason%", ChatColor.DARK_AQUA + reason);
        }
    }

}
