package de.cplaiz.activecraftcore.manager;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.events.PlayerBanEvent;
import de.cplaiz.activecraftcore.events.PlayerIpBanEvent;
import de.cplaiz.activecraftcore.events.PlayerUnbanEvent;
import de.cplaiz.activecraftcore.messagesv2.MessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.StringUtils;
import de.cplaiz.activecraftcore.utils.TimeUtils;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Date;

public class BanManager {

    public static class IP {
        public static boolean isBanned(String target) {
            for (BanEntry banEntry : Bukkit.getBanList(BanList.Type.IP).getBanEntries()) {
                if (target.equals(banEntry.getTarget())) return true;
            }
            return false;
        }

        public static void ban(String target, String reason, Date expires, String source) {
            Bukkit.getScheduler().runTask(ActiveCraftCore.getInstance(), () -> {
                PlayerIpBanEvent event = new PlayerIpBanEvent(target, true, reason, expires, source);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    Bukkit.getBanList(BanList.Type.IP).addBan(target, reason, expires, source);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getAddress().getAddress().toString().replace("/", "").equals(target)) {
                            Profilev2 profile = Profilev2.of(player);
                            MessageSupplier messageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());
                            if (profile != null)
                                profile.setTimesIpBanned(profile.getTimesIpBanned() + 1);
                            Bukkit.getScheduler().runTask(ActiveCraftCore.getInstance(), () -> {
                                String expirationString =
                                        TimeUtils.getRemainingAsString(expires).equalsIgnoreCase("never") ?
                                                messageSupplier.getMessage("command.banscreen.expiration-permanent") :
                                                messageSupplier.getFormatted("command.banscreen.expiration",
                                                        new MessageFormatter(messageSupplier.getActiveCraftMessage(), "time", TimeUtils.getRemainingAsString(expires)));
                                player.kickPlayer(messageSupplier.getMessage("command.banscreen.ip-title", ChatColor.RED)
                                        + "\n \n" + expirationString
                                        + "\n" + messageSupplier.getFormatted("command.banscreen.reason",
                                        new MessageFormatter(messageSupplier.getActiveCraftMessage(), "reason", reason)));
                            });
                        }
                    }
                }
            });
        }

        public static void ban(Player target, String reason, Date expires, String source) {
            ban(target.getName(), reason, expires, source);
        }

        public static void unban(String target) {
            if (!StringUtils.isValidInet4Address(target)) {
                return;
            }
            BanEntry entry = Bukkit.getBanList(BanList.Type.IP).getBanEntry(target);
            if (entry == null) return;
            PlayerIpBanEvent event = new PlayerIpBanEvent(target, false, entry.getReason(), entry.getExpiration(), entry.getSource());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Bukkit.getBanList(BanList.Type.IP).pardon(target);
        }

        public static void unban(Player target) {
            unban(target.getName());
        }

        public static java.util.Set<org.bukkit.BanEntry> getBans() {
            return Bukkit.getBanList(BanList.Type.IP).getBanEntries();
        }

        public static BanEntry getBanEntry(String target) {
            return Bukkit.getBanList(BanList.Type.IP).getBanEntry(target);
        }

        public static BanEntry getBanEntry(Player target) {
            return Bukkit.getBanList(BanList.Type.IP).getBanEntry(target.getName());
        }
    }

    public static class Name {
        public static boolean isBanned(String target) {
            return Bukkit.getBanList(BanList.Type.NAME).isBanned(target);
        }

        public static void ban(String target, String reason, Date expires, String source) {
            Bukkit.getScheduler().runTask(ActiveCraftCore.getInstance(), () -> {
                PlayerBanEvent event = new PlayerBanEvent(target, reason, expires, source);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target, reason, expires, source);
                    Profilev2 profile = Profilev2.of(target);
                    MessageSupplier messageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());
                    if (profile != null)
                        profile.setTimesBanned(profile.getTimesBanned() + 1);
                    if (Bukkit.getPlayer(target) != null)
                        Bukkit.getScheduler().runTask(ActiveCraftCore.getInstance(), () -> {
                            String expirationString =
                                    TimeUtils.getRemainingAsString(expires).equalsIgnoreCase("never") ?
                                            messageSupplier.getMessage("command.banscreen.expiration-permanent") :
                                            messageSupplier.getFormatted("command.banscreen.expiration",
                                                    new MessageFormatter(messageSupplier.getActiveCraftMessage(), "time", TimeUtils.getRemainingAsString(expires)));
                            Bukkit.getPlayer(target).kickPlayer(messageSupplier.getMessage("command.banscreen.title")
                                    + "\n \n" + expirationString
                                    + "\n" + messageSupplier.getFormatted("command.banscreen.reason",
                                    new MessageFormatter(messageSupplier.getActiveCraftMessage(), "reason", reason)));
                        });
                }
            });
        }

        public static void ban(Player target, String reason, Date expires, String source) {
            ban(target.getName(), reason, expires, source);
        }

        public static void unban(String target) {
            PlayerUnbanEvent event = new PlayerUnbanEvent(target);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Bukkit.getBanList(BanList.Type.NAME).pardon(target);
        }

        public static void unban(Player target) {
            unban(target.getName());
        }

        public static java.util.Set<org.bukkit.BanEntry> getBans() {
            return Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
        }

        public static BanEntry getBanEntry(String target) {
            return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(target);
        }

        public static BanEntry getBanEntry(Player target) {
            return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(target.getName());
        }
    }
}